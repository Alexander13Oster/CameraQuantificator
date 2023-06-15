package de.alexander13oster.cameraquantificator

import android.util.Log
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.rule.DisableOnAndroidDebug
import androidx.test.rule.GrantPermissionRule
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import okhttp3.*
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.rules.Timeout
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.concurrent.TimeUnit

/**
 * Camera Brightness Test for use with static Barcode, for example on Paper
 *
 * Change 'IP_LIGHT_CONTROLLER' in 'TestSetupClient' if needed
 */
@LargeTest
@RunWith(Parameterized::class)
@OptIn(ExperimentalPermissionsApi::class)
class StaticBarcodeCameraBrightnessTest(private val brightness: Int) {
    private val TAG = "StaticBarcodeCameraBrightnessTest"

    var testSetupClient: TestSetupClient = TestSetupClient()

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule = GrantPermissionRule.grant("android.permission.CAMERA")

    @Rule
    @JvmField
    var timeout: TestRule = DisableOnAndroidDebug(Timeout(10, TimeUnit.SECONDS))

    @After
    fun tearDown() {
        testSetupClient.changeBrightness(0)
    }

    @Test
    fun quantify() {
        val response = testSetupClient.changeBrightness(brightness)
        Log.d(TAG, "set brightness to $brightness ${response.message}")

        assert(response.isSuccessful)
        Thread.sleep(1000)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun brightnessValues(): Array<Int> = arrayOf(0, 1, 5, 13, 20, 40, 80, 100)
    }
}