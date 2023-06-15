#include <Adafruit_NeoPixel.h>
#include <ESPmDNS.h>
#include <WebServer.h>
#include <WiFi.h>
#include <WiFiClient.h>
#ifdef __AVR__
  #include <avr/power.h>
#endif

String version = String("0.1.0");

const char* ssid = "";
const char* password = "";
const int port = 80;

#define PIN 15

Adafruit_NeoPixel strip = Adafruit_NeoPixel(25, PIN, NEO_GRB + NEO_KHZ800);

WebServer server(port);

int red = 128;
int green = 128;
int blue = 128;
int brightness = 10;

void handleInfo() {
  server.send(200, "text/plain", "Light Controller Version: " + version);
}

void handleNotFound() {
  server.send(404, "text/plain", "Endpoint Not Found");
}

void setBrightness() {
  if(!server.hasArg("brightness")) {
    server.send(400, "Bad Request: Please supply brightness value");
  }
  int mBrightness = -1;
  mBrightness = server.arg("brightness").toInt();
  if(mBrightness < 0 || mBrightness > 100) {
    server.send(400, "Bad Request: Could not parse argument");
  }
  brightness = mBrightness;
  server.send(200, "text/plain", "Set brightness to: " + String(mBrightness));
}

void setColor() {  
  if (!(server.hasArg("red") && server.hasArg("green") && server.hasArg("blue"))) {
    server.send(400, "Bad Request: Please supply red, green and blue color values");
  }
  int mRed = -1;
  int mGreen = -1;
  int mBlue = -1;
  mRed = server.arg("red").toInt();
  mGreen = server.arg("green").toInt();
  mBlue = server.arg("blue").toInt();
  if(mRed < 0 || mRed > 255 || mGreen < 0 || mGreen > 255 || mBlue < 0 || mBlue > 255) {
    server.send(400, "Bad Request: Could not parse argument");
  }
  red = mRed;
  green = mGreen;
  red = mBlue;
  server.send(200, "text/plain", "Color changed");
}

void setup() {
  // Setup LED
  strip.begin();
  //strip.setBrightness(brightness);
  strip.show();

  // Setup WiFi
  Serial.begin(115200);
  Serial.println("Setting up server");
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.println("");

  // Wait for Connection
  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");    
  }
  Serial.print("Connected to ");
  Serial.println(String(ssid));
  Serial.print("IP address ");
  Serial.println(WiFi.localIP());

  // Setup Server
  if(MDNS.begin("esp32")) {
    Serial.println("MDNS responder started");
  }

  server.on("/info", handleInfo);
  server.on("/brightness", setBrightness);
  server.on("/color", setColor);
  server.onNotFound(handleNotFound);

  server.begin();
  Serial.println("HTTP server started");
}

void loop() {
  server.handleClient();
  strip.setBrightness(brightness);
  int color = strip.Color(red, green, blue);
  for(uint16_t i = 0; i < strip.numPixels(); i++) {
    strip.setPixelColor(i, color);
  }
  strip.show();
}
