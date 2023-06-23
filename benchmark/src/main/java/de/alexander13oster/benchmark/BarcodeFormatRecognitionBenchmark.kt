package de.alexander13oster.benchmark

import android.Manifest
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BarcodeFormatRecognitionBenchmark {

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.CAMERA)

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() {
        var firstStart = true

        benchmarkRule.measureRepeated(
            packageName = "de.alexander13oster.cameraquantificator",
            metrics = listOf(StartupTimingMetric()),
            iterations = 5,
            startupMode = StartupMode.COLD,
            setupBlock = {
                if (firstStart) {
                    startActivityAndWait()
                    firstStart = false
                }
            }
        ) {
            clickOnId("camera_x_camera_screen")
            //waitForTextShown("RecyclerView Sample")
            //device.pressBack()
            waitForTextShown("result: ")
            //waitForTextGone("RecyclerView Sample")
        }
    }

    private fun MacrobenchmarkScope.waitForTextShown(text: String) {
        check(device.wait(Until.hasObject(By.text(text)), 500)) {
            "View showing '$text' not found after waiting 500 ms."
        }
    }

    private fun MacrobenchmarkScope.waitForTextGone(text: String) {
        check(device.wait(Until.gone(By.text(text)), 500)) {
            "View showing '$text' not found after waiting 500 ms."
        }
    }

    private fun MacrobenchmarkScope.clickOnText(text: String) {
        device
            .findObject(By.text(text))
            .click()
    }

    private fun MacrobenchmarkScope.clickOnId(resourceId: String) {
        val selector = By.res(resourceId)
        if (!device.wait(Until.hasObject(selector), 2_500)) {
            TestCase.fail("Did not find object with id $resourceId")
        }

        device
            .findObject(selector)
            .click()
        // Chill to ensure we capture the end of the click span in the trace.
        Thread.sleep(100)
    }
}