package processing.mode.esp;

import java.util.*;
import java.io.*;

import processing.app.Base;
import processing.app.Platform;
import processing.app.Sketch;
import processing.app.SketchCode;
import processing.app.SketchException;
import processing.app.Util;
import processing.app.exec.SystemOutSiphon;

public class ESPBuild {
  public ESPBuild(Sketch sketch, File modeFolder) {
    File coreFolder = new File(modeFolder, "core");
    File coreLibraryFolder = new File(coreFolder, "library");
    File outputFolder = sketch.makeTempFolder();

    String command = "";

    String[] includes = {
      "", // the root "include" directory
      "ESP",
      "ofxDatGui",
      "ofxDatGui/components",
      "ofxDatGui/core",
      "ofxDatGui/libs/ofxSmartFont",
      "ofxDatGui/themes",
      "ofxGrt",
      "ofxParagraph",
    };

    String[] libraryIncludes = {
      "openFrameworks/libs/openFrameworks",
      "openFrameworks/libs/openFrameworks/3d",
      "openFrameworks/libs/openFrameworks/app",
      "openFrameworks/libs/openFrameworks/communication",
      "openFrameworks/libs/openFrameworks/events",
      "openFrameworks/libs/openFrameworks/gl",
      "openFrameworks/libs/openFrameworks/graphics",
      "openFrameworks/libs/openFrameworks/math",
      "openFrameworks/libs/openFrameworks/sound",
      "openFrameworks/libs/openFrameworks/types",
      "openFrameworks/libs/openFrameworks/utils",
      "openFrameworks/libs/openFrameworks/video",
      "openFrameworks/libs/poco/include",
      "openFrameworks/libs/freetype/include",
      "openFrameworks/libs/freetype/include/freetype2",
      "openFrameworks/libs/fmodex/include",
      "openFrameworks/libs/glew/include",
      "openFrameworks/libs/FreeImage/include",
      "openFrameworks/libs/tess2/include",
      "openFrameworks/libs/cairo/include/cairo",
      "openFrameworks/libs/rtAudio/include",
      "openFrameworks/libs/glfw/include",
      "openFrameworks/libs/boost/include",
      "openFrameworks/libs/utf8cpp/include",
      "openFrameworks/libs/openssl/include",
      "openFrameworks/addons/ofxOsc/src",
      "openFrameworks/addons/ofxOsc/libs/oscpack/src/ip",
      "openFrameworks/addons/ofxOsc/libs/oscpack/src/osc"
    };

    if (Platform.isMacOS()) {
      File appFolder = new File(outputFolder, "ESP.app"); appFolder.mkdir();
      File contentsFolder = new File(appFolder, "Contents"); contentsFolder.mkdir();
      File frameworksFolder = new File(contentsFolder, "Frameworks"); frameworksFolder.mkdir();
      File binFolder = new File(contentsFolder, "MacOS"); binFolder.mkdir();
    
      // Assemble .app contents.
      try {
        Util.copyDir(new File(coreFolder, "Resources"), new File(contentsFolder, "Resources"));
        Util.copyFile(new File(coreFolder, "Info.plist"), new File(contentsFolder, "Info.plist"));
        Util.copyFile(new File(coreLibraryFolder, "libgrt.dylib"), new File(frameworksFolder, "libgrt.dylib"));
        Util.copyFile(new File(coreLibraryFolder, "openFrameworks/libs/fmodex/lib/osx/libfmodex.dylib"), new File(binFolder, "libfmodex.dylib"));
        Util.copyFile(new File(coreLibraryFolder, "openFrameworks/libs/openFrameworksCompiled/project/osx/icon-debug.icns"), new File(new File(contentsFolder, "Resources"), "icon-debug.icns"));
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }

      String[] libs = {
        "openFrameworks/libs/tess2/lib/osx/tess2.a",
        "openFrameworks/libs/glew/lib/osx/glew.a",
        "openFrameworks/libs/boost/lib/osx/boost_filesystem.a",
        "openFrameworks/libs/boost/lib/osx/boost_system.a",
        "openFrameworks/libs/boost/lib/osx/boost.a",
        "openFrameworks/libs/poco/lib/osx/PocoCrypto.a",
        "openFrameworks/libs/poco/lib/osx/PocoData.a",
        "openFrameworks/libs/poco/lib/osx/PocoDataSQLite.a",
        "openFrameworks/libs/poco/lib/osx/PocoFoundation.a",
        "openFrameworks/libs/poco/lib/osx/PocoJSON.a",
        "openFrameworks/libs/poco/lib/osx/PocoMongoDB.a",
        "openFrameworks/libs/poco/lib/osx/PocoNet.a",
        "openFrameworks/libs/poco/lib/osx/PocoNetSSL.a",
        "openFrameworks/libs/poco/lib/osx/PocoUtil.a",
        "openFrameworks/libs/poco/lib/osx/PocoXML.a",
        "openFrameworks/libs/poco/lib/osx/PocoZip.a",
        "openFrameworks/libs/openssl/lib/osx/ssl.a",
        "openFrameworks/libs/openssl/lib/osx/crypto.a",
        "openFrameworks/libs/cairo/lib/osx/cairo-script-interpreter.a",
        "openFrameworks/libs/cairo/lib/osx/cairo.a",
        "openFrameworks/libs/cairo/lib/osx/pixman-1.a",
        "openFrameworks/libs/cairo/lib/osx/png.a",
        "openFrameworks/libs/FreeImage/lib/osx/freeimage.a",
        "openFrameworks/libs/freetype/lib/osx/freetype.a",
        "openFrameworks/libs/glfw/lib/osx/glfw3.a",
        "openFrameworks/libs/rtAudio/lib/osx/rtAudio.a"
      };

      String[] dylibs = {
        "grt",
        "esp",
        "fmodex",
        "objc"
      };

      String[] frameworks = {
        "OpenGL",
        "GLUT",
        "CoreServices",
        "Foundation",
        "CoreGraphics",
        "Cocoa",
        "CoreVideo",
        "IOKit",
        "CoreAudio",
        "Accelerate"
      };

      String[] libdirs = {
        "", // the root "library" directory
        "openFrameworks/libs/fmodex/lib/osx",
        "openFrameworks/libs/glut/lib/osx"
      };
    
      command="g++ -std=c++11";
      for (String include : includes) command += " -Iinclude/" + include;
      for (String include : libraryIncludes) command += " -Ilibrary/" + include;
      for (String libdir : libdirs) command += " -Llibrary/" + libdir;
      for (String dylib : dylibs) command += " -l" + dylib;
      for (String framework : frameworks) command += " -framework " + framework;
      for (String lib : libs) command += " library/" + lib;
      command += " " + sketch.getCode()[0].getFile().getAbsolutePath() + " -o " + binFolder.getAbsolutePath() + "/ESP";
    }

    if (Platform.isWindows()) {
      try {
        Util.copyDir(new File(new File(coreFolder, "Resources"), "data"), new File(outputFolder, "data"));
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }

      command = "cl.exe ";
      for (String include : includes) command += "/Iinclude/" + include + " ";
      for (String include : libraryIncludes) command += "/Ilibrary" + include + " ";
    }

    System.out.println(command);
    try {
      Process process = Runtime.getRuntime().exec(command, null, coreFolder);
      new SystemOutSiphon(process.getInputStream());
      new SystemOutSiphon(process.getErrorStream());
      System.out.println("Exited with: " + process.waitFor());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      File appFolder = new File(outputFolder, "ESP.app");
      Process process = Runtime.getRuntime().exec("open " + appFolder.getAbsolutePath());
      new SystemOutSiphon(process.getInputStream());
      new SystemOutSiphon(process.getErrorStream());
      System.out.println("Exited with: " + process.waitFor());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
}
