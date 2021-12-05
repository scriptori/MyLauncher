package me.scriptori.mylauncher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.scriptori.mylauncher.retrofit.DenyListResponse
import me.scriptori.mylauncher.ui.fragments.ApplicationDrawerFragment
import me.scriptori.mylauncher.util.Constants.TAG

/**
 * This is main activity
 */
class MyLauncherActivity : AppCompatActivity() {
    companion object {
        var currentDeniedList = mutableListOf<String>()
    }

    /**
     * Since this is a customer face dedicated device, you have to specify which apps packages are
     * allowed to be launched
     */
//    val APP_PACKAGES = arrayOf(KIOSK_PACKAGE, PLAYER_PACKAGE)

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
        DenyListResponse.deleteDenyListFile(this)
        super.onDetachedFromWindow()
    }

    private fun applyFragment() {
        supportFragmentManager.let { fm ->
            if (fm.findFragmentByTag(TAG) == null) {
                fm.beginTransaction().replace(
                    R.id.appContainer,
                    ApplicationDrawerFragment(),
                    TAG
                ).commit()
            }
        }
    }
}
