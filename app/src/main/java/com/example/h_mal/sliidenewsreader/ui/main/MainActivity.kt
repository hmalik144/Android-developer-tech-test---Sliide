package com.example.h_mal.sliidenewsreader.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.h_mal.sliidenewsreader.R
import com.example.h_mal.sliidenewsreader.data.db.entities.User
import com.example.h_mal.sliidenewsreader.data.network.responses.FeedResponse
import com.example.h_mal.sliidenewsreader.ui.CompletionListener
import com.example.h_mal.sliidenewsreader.ui.login.LoginActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.progress_bar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware, CompletionListener {

    override val kodein by kodein()
    private val factory : MainViewModelFactory by instance()

    lateinit var viewModel: MainViewModel

    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this,factory).get(MainViewModel::class.java)
        viewModel.completionLister = this

        viewModel.setList()

        viewModel.feed.observe(this, Observer {
            val adapter = ListAdapter(this,it)
            list_view.adapter = adapter
        })


        viewModel.getCurrentLoggedInUser().observe(this, Observer {user ->
            if (user != null){
                currentUser = user
            }

        })
    }

    //create a menu to navigate to other activities
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.sign_out ->{
                viewModel.logOut(currentUser)
                Intent(this@MainActivity, LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)

                }
            }
        }

        return super.onOptionsItemSelected(item)
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
