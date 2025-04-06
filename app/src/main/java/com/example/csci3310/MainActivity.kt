package com.example.csci3310

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.example.afer_login.Fitting_and_cart.FittingPage
import com.example.afer_login.Fitting_and_cart.cartPage
import com.example.afer_login.Homepage.HomeNavigation
import com.example.csci3310.databinding.ActivityMainBinding
import me.ibrahimsn.lib.SmoothBottomBar
import com.example.afer_login.SettingPage
import com.example.login.ui.theme.LoginTheme
import com.example.afer_login.dataFetch.User

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var composeView: ComposeView

    var LoggedIn: Boolean = false
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomBar: SmoothBottomBar = findViewById(R.id.bottomBar)
        composeView = findViewById(R.id.compose_view)

        if (!LoggedIn) {
            bottomBar.visibility = View.GONE
            composeView.setContent {
                LoginTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                        BeforeLoginNavigation(
                            onLoginSuccess = { user ->
                                LoggedIn = true
                                currentUser = user
                                println(LoggedIn)
                                Toast.makeText(applicationContext, "Login success", Toast.LENGTH_SHORT).show()
                                composeView.setContent {
                                    LoginTheme {
                                        HomeNavigation()
                                    }
                                }
                                bottomBar.visibility = View.VISIBLE
                                setupBottomBar(bottomBar)
                            }
                        )
                    }
                }
            }
        } else {
            bottomBar.visibility = View.VISIBLE
            composeView.setContent { LoginTheme { HomeNavigation() } }
            setupBottomBar(bottomBar)
        }
    }

    private fun setupBottomBar(bottomBar: SmoothBottomBar) {
        bottomBar.setOnItemSelectedListener { index ->
            when (index) {
                0 -> composeView.setContent { HomeNavigation() }
                1 -> composeView.setContent { FittingPage() }
                2 -> composeView.setContent { cartPage() }
                3 -> composeView.setContent { SettingPage(user = currentUser) }
            }
        }
    }
}
