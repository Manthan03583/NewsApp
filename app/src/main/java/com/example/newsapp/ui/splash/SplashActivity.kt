package com.example.newsapp.ui.splash

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import com.example.newsapp.R
import com.example.newsapp.ui.main.MainActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val app_logo = findViewById<LinearLayout>(R.id.app_logo)

        var prefrences: MutableLiveData<String> = MutableLiveData()
        prefrences.postValue("Light")

        prefrences.observe(this, Observer{
            if(it.equals("Light")) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else if (it.equals("Dark")) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        })



        object: CountDownTimer(2000, 1000){
            override fun onFinish() {
                val goToMainActivity = Intent(applicationContext, MainActivity::class.java)
                val activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    this@SplashActivity,
                    app_logo,
                    "app_logo"
                )


                startActivity(goToMainActivity, activityOptions.toBundle())

                lifecycle.addObserver(object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event == Lifecycle.Event.ON_STOP) {
                            lifecycle.removeObserver(this)
                            finish()
                        }
                    }
                })

            }

            override fun onTick(millisUntilFinished: Long) {

            }

        }.start()
    }
}