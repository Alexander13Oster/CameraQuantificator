package de.alexander13oster.barcodeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.alexander13oster.shared.model.Barcode
import de.alexander13oster.barcodeapp.ui.theme.QuantificatorTheme
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.Inet4Address
import java.net.NetworkInterface

class MainActivity : ComponentActivity() {
    private val _currentBarcode = MutableLiveData<Barcode?>(null)
    private val currentBarcode: LiveData<Barcode?> = _currentBarcode

    private val barcodeServer by lazy {
        embeddedServer(factory = Netty, port = PORT, watchPaths = emptyList()) {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }

            routing {
                post(path = "barcode") {
                    val barcode = call.receive<Barcode>()
                    _currentBarcode.postValue(barcode)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            barcodeServer.start(wait = true)
        }

        setContent {
            QuantificatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val barcode: Barcode? by currentBarcode.observeAsState(initial = null)

                    if (barcode == null) Text("IP of the Server is ${getIpAddress()}")

                    barcode?.let {
                        BarcodeScreen(data = it.data, format = it.format)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        barcodeServer.stop(gracePeriodMillis = 1000, timeoutMillis = 2000)
        super.onDestroy()
    }

    private fun getIpAddress() =
        NetworkInterface.getNetworkInterfaces()
            .asSequence()
            .flatMap { networkInterface ->
                networkInterface.inetAddresses
                    .asSequence()
                    .filter {
                        it.isLoopbackAddress.not() && it is Inet4Address
                    }
                    .map {
                        it.hostAddress
                    }
            }.toList()

    companion object {
        private const val PORT = 8080
    }
}