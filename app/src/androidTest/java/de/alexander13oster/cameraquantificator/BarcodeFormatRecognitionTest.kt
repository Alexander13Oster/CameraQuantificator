package de.alexander13oster.cameraquantificator

import android.util.Log
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.rule.DisableOnAndroidDebug
import androidx.test.rule.GrantPermissionRule
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.zxing.BarcodeFormat
import de.alexander13oster.shared.model.Barcode
import okhttp3.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.rules.Timeout
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.concurrent.TimeUnit

/**
 * Barcode Format Recognition Test for use with second smartphone using barcodeapp
 *
 * Change 'IP_LIGHT_CONTROLLER' and 'IP_BARCODE_APP' in 'TestSetupClient' if needed
 */
@LargeTest
@RunWith(Parameterized::class)
@OptIn(ExperimentalPermissionsApi::class)
class BarcodeFormatRecognitionTest(private val barcode: Barcode) {
    private val TAG = "BarcodeFormatRecognitionTest"

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

    @Before
    fun setUp() {
        val response = testSetupClient.changeBrightness(50)
        assert(response.isSuccessful)
    }

    @After
    fun tearDown() {
        testSetupClient.changeBrightness(0)
    }

    @Test
    fun quantify() {
        val response = testSetupClient.changeBarcode(barcode)
        Log.d(TAG, "set barcode to $barcode ${response.message}")

        assert(response.isSuccessful)
        Thread.sleep(1000)
    }

    companion object {
        private const val testDataLongString = "1234567890987654321abcdefghijklmnopqrstuvwxyz1234567890987654321"

        @JvmStatic
        @Parameterized.Parameters
        fun barcodes(): Array<Barcode> = arrayOf(
            Barcode(
                data = testDataLongString,
                format = BarcodeFormat.QR_CODE
            ),
            Barcode(
                data = testDataLongString,
                format = BarcodeFormat.AZTEC
            ),
            Barcode(
                data = testDataLongString,
                format = BarcodeFormat.DATA_MATRIX
            ),
            Barcode(
                data = testDataLongString,
                format = BarcodeFormat.PDF_417
            ),
            Barcode(data = "1234567890", format = BarcodeFormat.CODE_128)
        )
    }
}