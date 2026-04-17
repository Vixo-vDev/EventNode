package mx.edu.utez.integradoraeventnode

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: prueba instrumentada base para validar contexto real de la app.
 * Por que existe: confirma que el modulo Android puede ejecutarse con infraestructura de test.
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("mx.edu.utez.integradoraeventnode", appContext.packageName)
    }
}