package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.models.Diploma;
import com.eventnode.eventnodeapi.models.DiplomaEmitido;
import com.eventnode.eventnodeapi.models.Asistencia;
import com.eventnode.eventnodeapi.models.Evento;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.repositories.DiplomaRepository;
import com.eventnode.eventnodeapi.repositories.DiplomaEmitidoRepository;
import com.eventnode.eventnodeapi.repositories.AsistenciaRepository;
import com.eventnode.eventnodeapi.repositories.EventoRepository;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import jakarta.mail.internet.MimeMessage;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class DiplomaService {

    private final DiplomaRepository diplomaRepository;
    private final DiplomaEmitidoRepository diplomaEmitidoRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final JavaMailSender mailSender;

    public DiplomaService(DiplomaRepository diplomaRepository,
                         DiplomaEmitidoRepository diplomaEmitidoRepository,
                         AsistenciaRepository asistenciaRepository,
                         EventoRepository eventoRepository,
                         UsuarioRepository usuarioRepository,
                         JavaMailSender mailSender) {
        this.diplomaRepository = diplomaRepository;
        this.diplomaEmitidoRepository = diplomaEmitidoRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    public void crearDiploma(Integer idEvento, String firma, String diseno,
                             String plantillaPdf, String firmaImagen) {
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));

        if (diplomaRepository.findByIdEvento(idEvento).isPresent()) {
            throw new IllegalStateException("Ya existe un diploma para este evento");
        }

        Diploma diploma = new Diploma();
        diploma.setIdEvento(idEvento);
        diploma.setNombreEvento(evento.getNombre());
        diploma.setFirma(firma.isBlank() ? "Administrador" : firma);
        diploma.setDiseno(diseno.isBlank() ? "Personalizado" : diseno);
        diploma.setPlantillaPdf(plantillaPdf);
        diploma.setFirmaImagen(firmaImagen);
        diploma.setFechaCreacion(LocalDateTime.now());
        diploma.setEstado("ACTIVO");

        diplomaRepository.save(diploma);
    }

    public List<Map<String, Object>> listarDiplomas() {
        List<Diploma> diplomas = diplomaRepository.findByEstado("ACTIVO");

        return diplomas.stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idDiploma", d.getIdDiploma());
            map.put("idEvento", d.getIdEvento());
            map.put("nombreEvento", d.getNombreEvento());
            map.put("firma", d.getFirma());
            map.put("diseno", d.getDiseno());
            map.put("fechaCreacion", d.getFechaCreacion());
            map.put("tienePlantilla", d.getPlantillaPdf() != null && !d.getPlantillaPdf().isBlank());
            map.put("tieneFirma", d.getFirmaImagen() != null && !d.getFirmaImagen().isBlank());

            long totalEmitidos = diplomaEmitidoRepository.countByIdDiploma(d.getIdDiploma());
            map.put("totalEmitidos", totalEmitidos);

            long countAsistencias = asistenciaRepository.countByIdEvento(d.getIdEvento());
            long totalPendientes = countAsistencias - totalEmitidos;
            map.put("totalPendientes", totalPendientes);

            return map;
        }).collect(Collectors.toList());
    }

    @Transactional
    public long emitirDiplomas(Integer idDiploma) {
        Diploma diploma = diplomaRepository.findById(idDiploma)
                .orElseThrow(() -> new IllegalArgumentException("Diploma no encontrado"));

        if (diploma.getPlantillaPdf() == null || diploma.getPlantillaPdf().isBlank()) {
            throw new IllegalArgumentException("El diploma no tiene una plantilla PDF configurada");
        }

        List<Asistencia> asistencias = asistenciaRepository.findByIdEvento(diploma.getIdEvento());

        long count = 0;
        List<DiplomaEmitido> existentes = diplomaEmitidoRepository.findByIdDiploma(idDiploma);

        for (Asistencia asistencia : asistencias) {
            boolean yaEmitido = existentes.stream()
                    .anyMatch(de -> de.getIdUsuario().equals(asistencia.getIdUsuario()));

            if (!yaEmitido) {
                Usuario usuario = usuarioRepository.findById(asistencia.getIdUsuario()).orElse(null);
                if (usuario == null) continue;

                String fullName = buildFullName(usuario);

                DiplomaEmitido diplomaEmitido = new DiplomaEmitido();
                diplomaEmitido.setIdDiploma(idDiploma);
                diplomaEmitido.setIdUsuario(asistencia.getIdUsuario());
                diplomaEmitido.setFechaEnvio(LocalDateTime.now());

                try {
                    byte[] pdfBytes = generarDiplomaPdf(diploma, fullName);
                    enviarCorreoDiploma(usuario.getCorreo(), fullName, diploma.getNombreEvento(), pdfBytes);
                    diplomaEmitido.setEstadoEnvio("ENVIADO");
                } catch (Exception e) {
                    diplomaEmitido.setEstadoEnvio("ERROR");
                }

                diplomaEmitidoRepository.save(diplomaEmitido);
                count++;
            }
        }

        return count;
    }

    public byte[] generarDiplomaPdf(Integer idDiploma, Integer idUsuario) {
        Diploma diploma = diplomaRepository.findById(idDiploma)
                .orElseThrow(() -> new IllegalArgumentException("Diploma no encontrado"));

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String fullName = buildFullName(usuario);
        return generarDiplomaPdf(diploma, fullName);
    }

    private byte[] generarDiplomaPdf(Diploma diploma, String studentName) {
        try {
            String pdfBase64 = diploma.getPlantillaPdf();
            if (pdfBase64.contains(",")) {
                pdfBase64 = pdfBase64.substring(pdfBase64.indexOf(",") + 1);
            }
            byte[] templateBytes = Base64.getDecoder().decode(pdfBase64);

            PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(templateBytes));
            PDPage page = document.getPage(0);
            PDRectangle mediaBox = page.getMediaBox();
            float pageWidth = mediaBox.getWidth();
            float pageHeight = mediaBox.getHeight();

            PDPageContentStream contentStream = new PDPageContentStream(
                    document, page, PDPageContentStream.AppendMode.APPEND, true);

            // Draw student name centered
            PDType1Font nameFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            float nameFontSize = 28f;
            float nameWidth = nameFont.getStringWidth(studentName) / 1000 * nameFontSize;
            float nameX = (pageWidth - nameWidth) / 2;
            float nameY = pageHeight * 0.50f;

            contentStream.beginText();
            contentStream.setFont(nameFont, nameFontSize);
            contentStream.setNonStrokingColor(0.1f, 0.1f, 0.1f);
            contentStream.newLineAtOffset(nameX, nameY);
            contentStream.showText(studentName);
            contentStream.endText();

            // Draw signature image if available
            if (diploma.getFirmaImagen() != null && !diploma.getFirmaImagen().isBlank()) {
                try {
                    String sigBase64 = diploma.getFirmaImagen();
                    if (sigBase64.contains(",")) {
                        sigBase64 = sigBase64.substring(sigBase64.indexOf(",") + 1);
                    }
                    byte[] sigBytes = Base64.getDecoder().decode(sigBase64);

                    PDImageXObject sigImage = PDImageXObject.createFromByteArray(document, sigBytes, "firma");

                    float sigWidth = 150;
                    float sigHeight = (float) sigImage.getHeight() / sigImage.getWidth() * sigWidth;
                    float sigX = (pageWidth - sigWidth) / 2;
                    float sigY = pageHeight * 0.18f;

                    contentStream.drawImage(sigImage, sigX, sigY, sigWidth, sigHeight);
                } catch (Exception e) {
                    // If signature fails, continue without it
                }
            }

            // Draw signer name below signature
            if (diploma.getFirma() != null && !diploma.getFirma().isBlank()) {
                PDType1Font signerFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                float signerFontSize = 12f;
                float signerWidth = signerFont.getStringWidth(diploma.getFirma()) / 1000 * signerFontSize;
                float signerX = (pageWidth - signerWidth) / 2;
                float signerY = pageHeight * 0.14f;

                contentStream.beginText();
                contentStream.setFont(signerFont, signerFontSize);
                contentStream.setNonStrokingColor(0.3f, 0.3f, 0.3f);
                contentStream.newLineAtOffset(signerX, signerY);
                contentStream.showText(diploma.getFirma());
                contentStream.endText();
            }

            contentStream.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF del diploma: " + e.getMessage(), e);
        }
    }

    private void enviarCorreoDiploma(String correo, String nombre, String nombreEvento, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("caszn06@gmail.com");
            helper.setTo(correo);
            helper.setSubject("¡Has recibido un diploma! - " + nombreEvento);
            helper.setText(buildDiplomaEmailHtml(nombre, nombreEvento), true);
            helper.addAttachment("Diploma_" + nombreEvento.replaceAll("[^a-zA-Z0-9]", "_") + ".pdf",
                    new ByteArrayResource(pdfBytes), "application/pdf");

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar correo: " + e.getMessage(), e);
        }
    }

    private String buildDiplomaEmailHtml(String nombre, String nombreEvento) {
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'></head><body style='margin:0;padding:0;font-family:Arial,sans-serif;background-color:#f5f6fa;'>" +
                "<table width='100%' cellpadding='0' cellspacing='0' style='background-color:#f5f6fa;padding:40px 20px;'>" +
                "<tr><td align='center'>" +
                "<table width='600' cellpadding='0' cellspacing='0' style='background-color:#ffffff;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.08);'>" +
                // Header
                "<tr><td style='background:linear-gradient(135deg,#1A56DB 0%,#3B82F6 100%);padding:40px 30px;text-align:center;'>" +
                "<h1 style='color:#ffffff;margin:0 0 8px 0;font-size:28px;'>&#127891; ¡Felicidades!</h1>" +
                "<p style='color:rgba(255,255,255,0.9);margin:0;font-size:16px;'>Has recibido un diploma</p>" +
                "</td></tr>" +
                // Body
                "<tr><td style='padding:40px 30px;'>" +
                "<p style='font-size:16px;color:#333;margin:0 0 20px 0;'>Hola <strong>" + nombre + "</strong>,</p>" +
                "<p style='font-size:15px;color:#555;line-height:1.6;margin:0 0 24px 0;'>" +
                "Nos complace informarte que has recibido tu diploma por tu participación en el evento:</p>" +
                "<div style='background-color:#F0F7FF;border-left:4px solid #1A56DB;border-radius:8px;padding:16px 20px;margin:0 0 24px 0;'>" +
                "<p style='margin:0;font-size:18px;font-weight:bold;color:#1A56DB;'>" + nombreEvento + "</p>" +
                "</div>" +
                "<p style='font-size:15px;color:#555;line-height:1.6;margin:0 0 8px 0;'>" +
                "Tu diploma se encuentra adjunto a este correo en formato PDF. También puedes descargarlo desde la aplicación de EventNode.</p>" +
                "</td></tr>" +
                // Footer
                "<tr><td style='background-color:#f8f9fb;padding:24px 30px;text-align:center;border-top:1px solid #eee;'>" +
                "<p style='margin:0;font-size:13px;color:#999;'>EventNode — Sistema de Gestión de Eventos</p>" +
                "</td></tr>" +
                "</table></td></tr></table></body></html>";
    }

    public List<Map<String, Object>> listarDiplomasEstudiante(Integer idUsuario) {
        List<DiplomaEmitido> diplomasEmitidos = diplomaEmitidoRepository.findByIdUsuario(idUsuario);

        return diplomasEmitidos.stream().map(de -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idEmitido", de.getIdEmitido());
            map.put("idDiploma", de.getIdDiploma());
            map.put("fechaEnvio", de.getFechaEnvio());
            map.put("estadoEnvio", de.getEstadoEnvio());

            Diploma diploma = diplomaRepository.findById(de.getIdDiploma()).orElse(null);
            if (diploma != null) {
                map.put("nombreEvento", diploma.getNombreEvento());
                map.put("diseno", diploma.getDiseno());
            }

            return map;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> obtenerDiploma(Integer idDiploma) {
        Diploma diploma = diplomaRepository.findById(idDiploma)
                .orElseThrow(() -> new IllegalArgumentException("Diploma no encontrado"));

        Map<String, Object> map = new HashMap<>();
        map.put("idDiploma", diploma.getIdDiploma());
        map.put("idEvento", diploma.getIdEvento());
        map.put("nombreEvento", diploma.getNombreEvento());
        map.put("firma", diploma.getFirma());
        map.put("diseno", diploma.getDiseno());
        map.put("fechaCreacion", diploma.getFechaCreacion());
        map.put("estado", diploma.getEstado());
        map.put("tienePlantilla", diploma.getPlantillaPdf() != null && !diploma.getPlantillaPdf().isBlank());
        map.put("tieneFirma", diploma.getFirmaImagen() != null && !diploma.getFirmaImagen().isBlank());

        List<DiplomaEmitido> emitidos = diplomaEmitidoRepository.findByIdDiploma(idDiploma);
        List<Map<String, Object>> emitidosList = emitidos.stream().map(de -> {
            Map<String, Object> emMap = new HashMap<>();
            emMap.put("idEmitido", de.getIdEmitido());
            emMap.put("idUsuario", de.getIdUsuario());
            emMap.put("fechaEnvio", de.getFechaEnvio());
            emMap.put("estadoEnvio", de.getEstadoEnvio());

            Usuario usuario = usuarioRepository.findById(de.getIdUsuario()).orElse(null);
            if (usuario != null) {
                emMap.put("nombre", buildFullName(usuario));
            }

            return emMap;
        }).collect(Collectors.toList());

        map.put("emitidos", emitidosList);

        return map;
    }

    private String buildFullName(Usuario usuario) {
        String fullName = usuario.getNombre() + " " + usuario.getApellidoPaterno();
        if (usuario.getApellidoMaterno() != null && !usuario.getApellidoMaterno().isEmpty()) {
            fullName += " " + usuario.getApellidoMaterno();
        }
        return fullName;
    }
}
