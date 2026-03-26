package mx.edu.utez.integradoraeventnode.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    private const val PREF_LANGUAGE = "app_language"

    fun setLocale(context: Context, language: String): Context {
        saveLanguage(context, language)
        return updateResources(context, language)
    }

    fun getLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(PREF_LANGUAGE, "") ?: ""
    }

    fun getCurrentLanguage(context: Context): String {
        val lang = getLanguage(context)
        if (lang.isEmpty()) {
            val systemLang = Locale.getDefault().language
            return if (systemLang == "en") "en" else "es"
        }
        return lang
    }

    fun onAttach(context: Context): Context {
        val lang = getLanguage(context)
        if (lang.isEmpty()) {
            // Use system default language, map to supported
            val systemLang = Locale.getDefault().language
            val supported = if (systemLang == "en") "en" else "es"
            return updateResources(context, supported)
        }
        return updateResources(context, lang)
    }

    private fun saveLanguage(context: Context, language: String) {
        val prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_LANGUAGE, language).apply()
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
