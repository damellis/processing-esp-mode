## ESP Mode for Processing

[ESP](https://github.com/damellis/ESP/) is a system for applying machine learning to real-time sensor data. With ESP, machine learning pipelines are authored in C++ using the [Gesture Recognition Toolkit (GRT)](http://nickgillian.com/grt/). The ESP interface runs the pipeline, visualizing live sensor data and allowing for the collection of training data. For more information, see the [ESP wiki](https://github.com/damellis/ESP/wiki).

This repository allows ESP examples to be edited and run from with the [Processing](https://processing.org) Development Environment (PDE).

### Pre-Requisites

[Download Processing](https://processing.org/download/) and install it. You'll need a recent version, probably 3.2.1 or greater. Run Processing if you haven't used it before.

### Installation: Mac OS X

1. Install the Mac Command Line Tools: [macOS 10.12 Sierra](http://adcdownload.apple.com/Developer_Tools/Command_Line_Tools_macOS_10.12_for_Xcode_8/Command_Line_Tools_macOS_10.12_for_Xcode_8.dmg) or [OS X 10.11](http://adcdownload.apple.com/Developer_Tools/Command_Line_Tools_OS_X_10.11_for_Xcode_7.3.1/Command_Line_Tools_OS_X_10.11_for_Xcode_7.3.1.dmg).
2. Download [ESPMode-macosx.zip](https://github.com/damellis/processing-esp-mode/releases/download/v1.0.1/ESPMode-macosx.zip). 
3. Unzip to the `modes` sub-folder of your Processing sketchbook folder (probably `~/Documents/Processing3` or `~/Documents/Processing`).

### Installation: Windows

1. Download and install the [Visual C++ Build Tools](http://landinghub.visualstudio.com/visual-cpp-build-tools).
2. Download [ESPMode-windows.zip](https://github.com/damellis/processing-esp-mode/releases/download/v1.0.1/ESPMode-windows.zip).
3. Unzip to the `modes` sub-directory of your Processing sketchbook directory (probably `Documents\Processing` or `Documents\Processing3`.

### Using ESP Mode

Launch or restart Processing. Select "ESP" from the mode selection drop-down in the upper-right corner (which defaults to "Java"). See the examples window for some starting points for using ESP.
