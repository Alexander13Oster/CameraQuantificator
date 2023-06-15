# Camera Quantificator
### Test setup to quantify the camera performance of Android smartphones, camera libraries and under different lighting conditions.

- [Motives](#motives)
- [Workflow](#workflow)

# Motives

In many projects there is a point where barcode scanning functionality is needed.
Usually you select some libraries, paid or not, and run some benchmarks on them to find the best one for your use case.
In my experience, the benchmarks often compare whether a particular barcode can be scanned in a certain amount of time or go a step further and measure scan times.
What is often forgotten is the environment. How does a smartphone or scan library perform in different lighting conditions.
Factors like different symbologies and how performance scales with data size are also important.

Even after selecting your barcode library, there are cases where you need to be able to quantify performance.
One such case was the catalyst for me to start this project.
We received feedback from a customer who had only 2 different devices in use that camera performance decreased with a new release.
Our own manual testing was conflicting, some developers and testers had no problem, others felt the performance decreased.
But we couldn't quantify it.
After the test setup was attached and I ran some benchmarks with different devices and lighting conditions, we found that the problem only occurred on one device and was added with a new version of the Barcode Library we were using.
The new Barcode Library version had a problem with less than ideal lighting conditions.
After we found the problem and were able to quantify it, we were able to tell the developers of the Barcode Library where the problem was and they were able to fix it.

# Workflow

The way the quantification works is that you use the UI test in The Android Instrumentation Test uses http calls to control the lighting, which is controlled by the light_controller, and the barcode to be scanned, which is displayed by a second smartphone running the BarcodeApp. The tested app logged the time needed to recognize a barcode.

## Components

### App

The test object; the app with the desired implementation of your camera

### Barcode App (Optional)

The barcode app is an application that displays barcodes with different symbologies and data. The data input is an http request for which a web server runs in the app.
The advantages over a static image are obvious, but depending on the use case, it is not necessary to have different barcodes.

### [Box with light controller](box/light_controller.md)

The box contains a microcontroller to which an RGB LED strip is connected. The lighting inside the box can be controlled via http. The box is optional, but a key component to ensure test reproducibility and comparability. My prototype was a shoebox, so you don't necessarily need to build such a box.

The 3D model for the box and a parts list are stored in the repository. 
