package me.scriptori.mylauncher

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.scriptori.mylauncher.util.AllowedPackageHandler
import me.scriptori.mylauncher.util.DenyPackageHandler

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DenyResponseTest {
    private val expectedArray = arrayOf(
        "com.android.chrome",
        "com.google.android.apps.maps",
        "com.android.dialer",
        "com.google.android.gm"
    )

    // Context of the app under test.
    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun defaultDenyPackageStringToDenyResponse() {
        with(DenyPackageHandler) {
            defaultDenyResponse().let {
                assertEquals(4, it.denylist.size)
                assertArrayEquals(expectedArray, it.denylist.toTypedArray())
            }
        }
    }

    @Test
    fun writeToFileAndDeleteFileTest() {
        assertEquals(AllowedPackageHandler.APP_PACKAGE_NAME, appContext.packageName)
        with(DenyPackageHandler) {
            defaultDenyResponse().let {
                assertEquals(4, it.denylist.size)
                writeToFile(appContext, it)
            }
            assertTrue(getDenyListFile(appContext).exists())
            assertArrayEquals(
                expectedArray,
                readFromFile(appContext).denylist.toTypedArray()
            )
            deleteDenyListFile(appContext)
            assertFalse(getDenyListFile(appContext).exists())
        }
    }
}