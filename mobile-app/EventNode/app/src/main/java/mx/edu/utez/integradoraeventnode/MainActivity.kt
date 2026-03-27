package mx.edu.utez.integradoraeventnode

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import mx.edu.utez.integradoraeventnode.utils.LocaleHelper
import mx.edu.utez.integradoraeventnode.utils.PreferencesHelper
import mx.edu.utez.integradoraeventnode.ui.screens.auth.LoginScreen
import mx.edu.utez.integradoraeventnode.ui.screens.auth.RegisterScreen
import mx.edu.utez.integradoraeventnode.ui.screens.admin.agenda.*
import mx.edu.utez.integradoraeventnode.ui.screens.admin.analytics.*
import mx.edu.utez.integradoraeventnode.ui.screens.admin.diplomas.*
import mx.edu.utez.integradoraeventnode.ui.screens.admin.home.*
import mx.edu.utez.integradoraeventnode.ui.screens.admin.profile.*
import mx.edu.utez.integradoraeventnode.ui.screens.admin.scanner.*
import mx.edu.utez.integradoraeventnode.ui.screens.student.profile.*
import mx.edu.utez.integradoraeventnode.ui.screens.student.home.*
import mx.edu.utez.integradoraeventnode.ui.screens.student.diplomas.*
import mx.edu.utez.integradoraeventnode.ui.screens.student.agenda.*
import mx.edu.utez.integradoraeventnode.ui.theme.IntegradoraEventNodeTheme

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IntegradoraEventNodeTheme {
                val context = androidx.compose.ui.platform.LocalContext.current
                val mantenerSesion = PreferencesHelper.getMantenerSesion(context)
                val rol = PreferencesHelper.getRol(context)
                val token = PreferencesHelper.getToken(context)

                val startScreen = if (mantenerSesion && token.isNotEmpty()) {
                    if (rol.contains("ADMIN", ignoreCase = true)) AppScreen.AdminHome else AppScreen.Home
                } else {
                    AppScreen.Login
                }

                var currentScreen by remember { mutableStateOf(startScreen) }
                var selectedEventId by remember { mutableStateOf<Int?>(null) }
                var adminSelectedEventId by remember { mutableStateOf<Int?>(null) }
                var selectedCheckinEventId by remember { mutableStateOf<Int?>(null) }
                var selectedCheckinEventName by remember { mutableStateOf("") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (currentScreen) {
                        AppScreen.Login -> LoginScreen(
                            modifier = Modifier.padding(innerPadding),
                            onCreateAccount = { currentScreen = AppScreen.Register },
                            onLogin = { isAdmin ->
                                currentScreen = if (isAdmin) AppScreen.AdminHome else AppScreen.Home
                            }
                        )
                        AppScreen.Register -> RegisterScreen(
                            modifier = Modifier.padding(innerPadding),
                            onBackToLogin = { currentScreen = AppScreen.Login }
                        )
                        AppScreen.Home -> HomeScreen(
                            modifier  = Modifier.padding(innerPadding),
                            onViewDetails = { eventId ->
                                selectedEventId = eventId
                                currentScreen = AppScreen.StudentEventDetail
                            },
                            onAgenda = { currentScreen = AppScreen.Agenda },
                            onDiplomas = { currentScreen = AppScreen.Diplomas },
                            onProfile = { currentScreen = AppScreen.Profile }
                        )
                        AppScreen.AdminHome -> AdminHomeScreen(
                            modifier = Modifier.padding(innerPadding),
                            onViewEventDetail = { eventId ->
                                adminSelectedEventId = eventId
                                currentScreen = AppScreen.AdminEventDetail
                            },
                            onAgenda = { currentScreen = AppScreen.AdminAgenda },
                            onEscanear = { currentScreen = AppScreen.AdminScanner },
                            onDiplomas = { currentScreen = AppScreen.AdminDiplomas },
                            onAnalitica = { currentScreen = AppScreen.AdminAnalytics },
                            onProfile = { currentScreen = AppScreen.AdminProfile },
                            onLogout = {
                                PreferencesHelper.setMantenerSesion(context, false)
                                currentScreen = AppScreen.Login
                            }
                        )
                        AppScreen.AdminAgenda -> AdminAgendaScreen(
                            modifier = Modifier.padding(innerPadding),
                            onHome = { currentScreen = AppScreen.AdminHome },
                            onEscanear = { currentScreen = AppScreen.AdminScanner },
                            onDiplomas = { currentScreen = AppScreen.AdminDiplomas },
                            onAnalitica = { currentScreen = AppScreen.AdminAnalytics },
                            onProfile = { currentScreen = AppScreen.AdminProfile },
                            onViewDetail = { eventId ->
                                adminSelectedEventId = eventId
                                currentScreen = AppScreen.AdminEventDetail
                            }
                        )
                        AppScreen.AdminScanner -> AdminScannerScreen(
                            modifier = Modifier.padding(innerPadding),
                            onHome = { currentScreen = AppScreen.AdminHome },
                            onAgenda = { currentScreen = AppScreen.AdminAgenda },
                            onDiplomas = { currentScreen = AppScreen.AdminDiplomas },
                            onAnalitica = { currentScreen = AppScreen.AdminAnalytics },
                            onProfile = { currentScreen = AppScreen.AdminProfile }
                        )
                        AppScreen.AdminDiplomas -> AdminDiplomasScreen(
                            modifier = Modifier.padding(innerPadding),
                            onHome = { currentScreen = AppScreen.AdminHome },
                            onAgenda = { currentScreen = AppScreen.AdminAgenda },
                            onEscanear = { currentScreen = AppScreen.AdminScanner },
                            onAnalitica = { currentScreen = AppScreen.AdminAnalytics },
                            onProfile = { currentScreen = AppScreen.AdminProfile }
                        )
                        AppScreen.AdminAnalytics -> AdminAnalyticsScreen(
                            modifier = Modifier.padding(innerPadding),
                            onHome = { currentScreen = AppScreen.AdminHome },
                            onAgenda = { currentScreen = AppScreen.AdminAgenda },
                            onEscanear = { currentScreen = AppScreen.AdminScanner },
                            onDiplomas = { currentScreen = AppScreen.AdminDiplomas },
                            onProfile = { currentScreen = AppScreen.AdminProfile }
                        )
                        AppScreen.AdminProfile -> AdminProfileScreen(
                            modifier = Modifier.padding(innerPadding),
                            onHome = { currentScreen = AppScreen.AdminHome },
                            onAgenda = { currentScreen = AppScreen.AdminAgenda },
                            onEscanear = { currentScreen = AppScreen.AdminScanner },
                            onDiplomas = { currentScreen = AppScreen.AdminDiplomas },
                            onAnalitica = { currentScreen = AppScreen.AdminAnalytics },
                            onEditProfile = { currentScreen = AppScreen.AdminEditProfile },
                            onLogout = {
                                PreferencesHelper.setMantenerSesion(context, false)
                                currentScreen = AppScreen.Login
                            }
                        )
                        AppScreen.AdminEditProfile -> AdminEditProfileScreen(
                            modifier = Modifier.padding(innerPadding),
                            onBack = { currentScreen = AppScreen.AdminProfile },
                            onHome = { currentScreen = AppScreen.AdminHome },
                            onAgenda = { currentScreen = AppScreen.AdminAgenda },
                            onEscanear = { currentScreen = AppScreen.AdminScanner },
                            onDiplomas = { currentScreen = AppScreen.AdminDiplomas },
                            onAnalitica = { currentScreen = AppScreen.AdminAnalytics },
                            onProfile = { currentScreen = AppScreen.AdminProfile }
                        )
                        AppScreen.AdminEventDetail -> AdminEventDetailScreen(
                            eventId = adminSelectedEventId ?: -1,
                            modifier = Modifier.padding(innerPadding),
                            onBack = { currentScreen = AppScreen.AdminAgenda },
                            onHome = { currentScreen = AppScreen.AdminHome },
                            onAgenda = { currentScreen = AppScreen.AdminAgenda },
                            onEscanear = { currentScreen = AppScreen.AdminScanner },
                            onDiplomas = { currentScreen = AppScreen.AdminDiplomas },
                            onAnalitica = { currentScreen = AppScreen.AdminAnalytics },
                            onProfile = { currentScreen = AppScreen.AdminProfile },
                        )
                        AppScreen.Agenda -> AgendaScreen(
                            modifier = Modifier.padding(innerPadding),
                            onHome = { currentScreen = AppScreen.Home },
                            onViewQr = { eventId, eventName ->
                                selectedCheckinEventId = eventId
                                selectedCheckinEventName = eventName
                                currentScreen = AppScreen.CheckinQr
                            },
                            onViewDetail = { eventId ->
                                selectedEventId = eventId
                                currentScreen = AppScreen.StudentEventDetail
                            },
                            onDiplomas = { currentScreen = AppScreen.Diplomas },
                            onProfile = { currentScreen = AppScreen.Profile }
                        )
                        AppScreen.CheckinQr -> CheckinQrScreen(
                            eventId = selectedCheckinEventId ?: -1,
                            eventName = selectedCheckinEventName,
                            modifier = Modifier.padding(innerPadding),
                            onBack = { currentScreen = AppScreen.Agenda },
                            onHome = { currentScreen = AppScreen.Home },
                            onDiplomas = { currentScreen = AppScreen.Diplomas },
                            onProfile = { currentScreen = AppScreen.Profile }
                        )
                        AppScreen.EventDetail -> EventDetailScreen(
                            eventId = selectedEventId ?: -1,
                            modifier = Modifier.padding(innerPadding),
                            onBack = { currentScreen = AppScreen.Home },
                            onAgenda = { currentScreen = AppScreen.Agenda },
                            onDiplomas = { currentScreen = AppScreen.Diplomas },
                            onProfile = { currentScreen = AppScreen.Profile }
                        )
                        AppScreen.StudentEventDetail -> StudentEventDetailScreen(
                            eventId = selectedEventId ?: -1,
                            modifier = Modifier.padding(innerPadding),
                            onBack = { currentScreen = AppScreen.Agenda },
                            onHome = { currentScreen = AppScreen.Home },
                            onAgenda = { currentScreen = AppScreen.Agenda },
                            onDiplomas = { currentScreen = AppScreen.Diplomas },
                            onProfile = { currentScreen = AppScreen.Profile }
                        )
                        AppScreen.Diplomas -> DiplomasScreen(
                            modifier = Modifier.padding(innerPadding),
                            onHome = { currentScreen = AppScreen.Home },
                            onAgenda = { currentScreen = AppScreen.Agenda },
                            onProfile = { currentScreen = AppScreen.Profile }
                        )
                        AppScreen.Profile -> ProfileScreen(
                            modifier = Modifier.padding(innerPadding),
                            onHome = { currentScreen = AppScreen.Home },
                            onAgenda = { currentScreen = AppScreen.Agenda },
                            onDiplomas = { currentScreen = AppScreen.Diplomas },
                            onEditProfile = { currentScreen = AppScreen.EditProfile },
                            onLogout = {
                                PreferencesHelper.setMantenerSesion(context, false)
                                currentScreen = AppScreen.Login
                            }
                        )
                        AppScreen.EditProfile -> EditProfileScreen(
                            modifier = Modifier.padding(innerPadding),
                            onBack = { currentScreen = AppScreen.Profile },
                            onHome = { currentScreen = AppScreen.Home }
                        )
                    }
                }
            }
        }
    }
}

private enum class AppScreen {
    Login,
    Register,
    Home,
    AdminHome,
    AdminAgenda,
    AdminScanner,
    AdminDiplomas,
    AdminAnalytics,
    AdminProfile,
    AdminEditProfile,
    AdminEventDetail,
    Agenda,
    CheckinQr,
    EventDetail,
    StudentEventDetail,
    Diplomas,
    Profile,
    EditProfile
}
