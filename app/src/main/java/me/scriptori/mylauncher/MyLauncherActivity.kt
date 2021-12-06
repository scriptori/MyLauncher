package me.scriptori.mylauncher

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.scriptori.mylauncher.receiver.AdminBroadcastReceiver
import me.scriptori.mylauncher.ui.fragments.ApplicationDrawerFragment

/**
 * This is main activity
 */
class MyLauncherActivity : AppCompatActivity() {
    internal lateinit var adminComponentName: ComponentName
    internal lateinit var dpm: DevicePolicyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adminComponentName = ComponentName(this, AdminBroadcastReceiver::class.java)
        dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (dpm.isDeviceOwnerApp(packageName)) {
            dpm.setKeyguardDisabled(adminComponentName, true);
            dpm.setStatusBarDisabled(adminComponentName, true);
        }

        applyFragment()
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_cisco_logo_blue)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onBackPressed() {
        applyFragment()
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
