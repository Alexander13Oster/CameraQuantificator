package de.alexander13oster.shared.model

import com.google.zxing.BarcodeFormat

data class Barcode(
    val data: String,
    val format: BarcodeFormat
)