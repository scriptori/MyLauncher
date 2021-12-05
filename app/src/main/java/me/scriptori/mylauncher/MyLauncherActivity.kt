package me.scriptori.mylauncher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.scriptori.mylauncher.ui.fragments.ApplicationDrawerFragment
import me.scriptori.mylauncher.util.DenyList

/**
 * This is main activity
 */
class MyLauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applyFragment()
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_cisco_logo_blue)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onBackPressed() {
        applyFragment()
    }

    override fun onDetachedFromWindow() {
        DenyList.deleteDenyListFile(this)
        super.onDetachedFromWindow()
    }

    private fun applyFragment() {
        supportFragmentManager.let { fm ->
            if (fm.findFragmentByTag(ApplicationDrawerFragment.TAG) == null) {
                fm.beginTransaction().replace(
                    R.id.appContainer,
                    ApplicationDrawerFragment(),
                    ApplicationDrawerFragment.TAG
                ).commit()
            }
        }
    }
}
