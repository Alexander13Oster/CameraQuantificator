package de.alexander13oster.barcodeapp

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import de.alexander13oster.barcodeapp.ui.theme.QuantificatorTheme

@Composable
fun BarcodeScreen(data: String, format: BarcodeFormat) {
    val barcodeEncoder = BarcodeEncoder()

    runCatching {
        val bitmap = barcodeEncoder.encodeBitmap(data, format, 1000, 1000)
        BitmapImage(bitmap)
    }.onFailure {
        Text("Error at Creating Barcode")
    }
}

@Composable
fun BitmapImage(bitmap: Bitmap) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "barcode to scan",
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BarcodeScreenPreview() {
    QuantificatorTheme {
        BarcodeScreen("abc", BarcodeFormat.AZTEC)
    }
}