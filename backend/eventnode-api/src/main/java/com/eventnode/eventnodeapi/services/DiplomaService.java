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

            // Sanitize text for font compatibility
            String safeName = sanitizeForFont(studentName);
            String safeEventName = sanitizeForFont(diploma.getNombreEvento());
            String safeFirma = sanitizeForFont(diploma.getFirma());

            // ── Draw student name centered at ~50% height ──
            PDType1Font nameFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            float nameFontSize = 28f;
            // Adjust font size if name is too long
            float nameWidth = nameFont.getStringWidth(safeName) / 1000 * nameFontSize;
            while (nameWidth > pageWidth * 0.85f && nameFontSize > 14f) {
                nameFontSize -= 2f;
                nameWidth = nameFont.getStringWidth(safeName) / 1000 * nameFontSize;
            }
            float nameX = (pageWidth - nameWidth) / 2;
            float nameY = pageHeight * 0.48f;

            contentStream.beginText();
            contentStream.setFont(nameFont, nameFontSize);
            contentStream.setNonStrokingColor(0.1f, 0.1f, 0.1f);
            contentStream.newLineAtOffset(nameX, nameY);
            contentStream.showText(safeName);
            contentStream.endText();

            // ── Draw event name centered below student name at ~40% height ──
            if (safeEventName != null && !safeEventName.isBlank()) {
                PDType1Font eventFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                float eventFontSize = 16f;
                float eventWidth = eventFont.getStringWidth(safeEventName) / 1000 * eventFontSize;
                while (eventWidth > pageWidth * 0.85f && eventFontSize > 10f) {
                    eventFontSize -= 1f;
                    eventWidth = eventFont.getStringWidth(safeEventName) / 1000 * eventFontSize;
                }
                float eventX = (pageWidth - eventWidth) / 2;
                float eventY = pageHeight * 0.38f;

                contentStream.beginText();
                contentStream.setFont(eventFont, eventFontSize);
                contentStream.setNonStrokingColor(0.2f, 0.2f, 0.4f);
                contentStream.newLineAtOffset(eventX, eventY);
                contentStream.showText(safeEventName);
                contentStream.endText();
            }

            // ── Draw signature image if available ──
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
                    // If signature image fails, continue without it
                }
            }

            // ── Draw signer name below signature at ~14% height ──
            if (safeFirma != null && !safeFirma.isBlank()) {
                PDType1Font signerFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                float signerFontSize = 12f;
                float signerWidth = signerFont.getStringWidth(safeFirma) / 1000 * signerFontSize;
                float signerX = (pageWidth - signerWidth) / 2;
                float signerY = pageHeight * 0.14f;

                contentStream.beginText();
                contentStream.setFont(signerFont, signerFontSize);
                contentStream.setNonStrokingColor(0.3f, 0.3f, 0.3f);
                contentStream.newLineAtOffset(signerX, signerY);
                contentStream.showText(safeFirma);
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

    /**
     * Sanitize text to ensure compatibility with PDType1Font (WinAnsiEncoding).
     * Replaces characters not supported by the encoding.
     */
    private String sanitizeForFont(String text) {
        if (text == null) return "";
        // WinAnsiEncoding supports basic Latin + accented chars (á,é,í,ó,ú,ñ,ü,Á,É,Í,Ó,Ú,Ñ,Ü)
        // Replace any problematic characters that might cause encoding issues
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c < 256) {
                sb.append(c);
            } else {
                // Replace unsupported Unicode chars with closest ASCII equivalent
                sb.append('?');
            }
        }
        return sb.toString();
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

    private void enviarCorreoDiplomaActualizado(String correo, String nombre, String nombreEvento, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("caszn06@gmail.com");
            helper.setTo(correo);
            helper.setSubject("Tu diploma ha sido actualizado - " + nombreEvento);
            helper.setText(buildDiplomaUpdatedEmailHtml(nombre, nombreEvento), true);
            helper.addAttachment("Diploma_" + nombreEvento.replaceAll("[^a-zA-Z0-9]", "_") + ".pdf",
                    new ByteArrayResource(pdfBytes), "application/pdf");

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar correo de actualización: " + e.getMessage(), e);
        }
    }

    private String buildDiplomaUpdatedEmailHtml(String nombre, String nombreEvento) {
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'></head><body style='margin:0;padding:0;font-family:Arial,sans-serif;background-color:#f5f6fa;'>" +
                "<table width='100%' cellpadding='0' cellspacing='0' style='background-color:#f5f6fa;padding:40px 20px;'>" +
                "<tr><td align='center'>" +
                "<table width='600' cellpadding='0' cellspacing='0' style='background-color:#ffffff;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.08);'>" +
                "<tr><td style='background:linear-gradient(135deg,#F59E0B 0%,#D97706 100%);padding:40px 30px;text-align:center;'>" +
                "<h1 style='color:#ffffff;margin:0 0 8px 0;font-size:28px;'>&#128221; Diploma Actualizado</h1>" +
                "<p style='color:rgba(255,255,255,0.9);margin:0;font-size:16px;'>Tu diploma ha sido modificado</p>" +
                "</td></tr>" +
                "<tr><td style='padding:40px 30px;'>" +
                "<p style='font-size:16px;color:#333;margin:0 0 20px 0;'>Hola <strong>" + nombre + "</strong>,</p>" +
                "<p style='font-size:15px;color:#555;line-height:1.6;margin:0 0 24px 0;'>" +
                "Te informamos que el diploma del siguiente evento ha sido actualizado:</p>" +
                "<div style='background-color:#FFF7ED;border-left:4px solid #F59E0B;border-radius:8px;padding:16px 20px;margin:0 0 24px 0;'>" +
                "<p style='margin:0;font-size:18px;font-weight:bold;color:#D97706;'>" + nombreEvento + "</p>" +
                "</div>" +
                "<p style='font-size:15px;color:#555;line-height:1.6;margin:0 0 8px 0;'>" +
                "Te adjuntamos la nueva versión de tu diploma en formato PDF. También puedes descargarlo desde la aplicación de EventNode.</p>" +
                "</td></tr>" +
                "<tr><td style='background-color:#f8f9fb;padding:24px 30px;text-align:center;border-top:1px solid #eee;'>" +
                "<p style='margin:0;font-size:13px;color:#999;'>EventNode — Sistema de Gestión de Eventos</p>" +
                "</td></tr>" +
                "</table></td></tr></table></body></html>";
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

    @Transactional
    public void actualizarDiploma(Integer idDiploma, String firma, String diseno,
                                   String plantillaPdf, String firmaImagen) {
        Diploma diploma = diplomaRepository.findById(idDiploma)
                .orElseThrow(() -> new IllegalArgumentException("Diploma no encontrado"));

        if (firma != null && !firma.isBlank()) diploma.setFirma(firma);
        if (diseno != null && !diseno.isBlank()) diploma.setDiseno(diseno);
        if (plantillaPdf != null && !plantillaPdf.isBlank()) diploma.setPlantillaPdf(plantillaPdf);
        if (firmaImagen != null && !firmaImagen.isBlank()) diploma.setFirmaImagen(firmaImagen);

        diplomaRepository.save(diploma);

        // Re-generate and re-send PDFs to all recipients who already received the diploma
        List<DiplomaEmitido> emitidos = diplomaEmitidoRepository.findByIdDiploma(idDiploma);
        for (DiplomaEmitido de : emitidos) {
            Usuario usuario = usuarioRepository.findById(de.getIdUsuario()).orElse(null);
            if (usuario == null) continue;

            String fullName = buildFullName(usuario);
            try {
                byte[] pdfBytes = generarDiplomaPdf(diploma, fullName);
                enviarCorreoDiplomaActualizado(usuario.getCorreo(), fullName, diploma.getNombreEvento(), pdfBytes);
                de.setEstadoEnvio("ENVIADO");
                de.setFechaEnvio(LocalDateTime.now());
            } catch (Exception e) {
                de.setEstadoEnvio("ERROR");
            }
            diplomaEmitidoRepository.save(de);
        }
    }

    @Transactional
    public void eliminarDiploma(Integer idDiploma) {
        Diploma diploma = diplomaRepository.findById(idDiploma)
                .orElseThrow(() -> new IllegalArgumentException("Diploma no encontrado"));
        diploma.setEstado("ELIMINADO");
        diplomaRepository.save(diploma);
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
