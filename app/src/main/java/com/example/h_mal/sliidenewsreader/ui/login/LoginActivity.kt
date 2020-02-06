package com.example.h_mal.sliidenewsreader.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.h_mal.sliidenewsreader.ui.main.MainActivity
import com.example.h_mal.sliidenewsreader.R
import com.example.h_mal.sliidenewsreader.data.db.entities.User
import com.example.h_mal.sliidenewsreader.databinding.ActivityLoginBinding
import com.example.h_mal.sliidenewsreader.ui.CompletionListener
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : AppCompatActivity(),
    CompletionListener, KodeinAware {

    override val kodein by kodein()
    private val factory : LoginViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val loginViewModel: LoginViewModel = ViewModelProviders.of(this, factory)
            .get(LoginViewModel::class.java)
        binding.viewmodel = loginViewModel

        loginViewModel.authListener = this

        loginViewModel.getCurrentLoggedInUser().observe(this@LoginActivity, Observer { user ->

            if(user != null){
                Intent(this, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        })

    }



    override fun onStarted() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progress_bar.visibility = View.GONE
    }

    override fun onFailure(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        progress_bar.visibility = View.GONE
    }
}
