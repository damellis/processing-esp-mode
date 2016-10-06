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
    
    String command="g++ -std=c++11 -I../include -I../include/ESP -I../include/ofxDatGui -I../include/ofxDatGui/components -I../include/ofxDatGui/core -I../include/ofxDatGui/libs/ofxSmartFont -I../include/ofxDatGui/themes -I../include/ofxGrt -I../include/ofxParagraph -IopenFrameworks/libs/openFrameworks -IopenFrameworks/libs/openFrameworks/3d/ -IopenFrameworks/libs/openFrameworks/app/ -IopenFrameworks/libs/openFrameworks/communication/ -IopenFrameworks/libs/openFrameworks/events/ -IopenFrameworks/libs/openFrameworks/gl/ -IopenFrameworks/libs/openFrameworks/graphics/ -IopenFrameworks/libs/openFrameworks/math/ -IopenFrameworks/libs/openFrameworks/sound/ -IopenFrameworks/libs/openFrameworks/types/ -IopenFrameworks/libs/openFrameworks/utils/ -IopenFrameworks/libs/openFrameworks/video/ -IopenFrameworks/libs/openFrameworks -IopenFrameworks/libs/poco/include -IopenFrameworks/libs/freetype/include -IopenFrameworks/libs/freetype/include/freetype2 -IopenFrameworks/libs/fmodex/include -IopenFrameworks/libs/glew/include -IopenFrameworks/libs/FreeImage/include -IopenFrameworks/libs/tess2/include -IopenFrameworks/libs/cairo/include/cairo -IopenFrameworks/libs/rtAudio/include -IopenFrameworks/libs/glfw/include -IopenFrameworks/libs/boost/include -IopenFrameworks/libs/utf8cpp/include -IopenFrameworks/libs/openssl/include -IopenFrameworks/addons/ofxOsc/src -IopenFrameworks/addons/ofxOsc/libs/oscpack/src/ip -IopenFrameworks/addons/ofxOsc/libs/oscpack/src/osc -L. -LopenFrameworks/libs/fmodex/lib/osx -LopenFrameworks/libs/glut/lib/osx -lesp -lgrt -lfmodex ./openFrameworks/libs/tess2/lib/osx/tess2.a -framework OpenGL ./openFrameworks/libs/glew/lib/osx/glew.a ./openFrameworks/libs/boost/lib/osx/boost_filesystem.a ./openFrameworks/libs/boost/lib/osx/boost_system.a ./openFrameworks/libs/boost/lib/osx/boost.a ./openFrameworks/libs/poco/lib/osx/PocoCrypto.a ./openFrameworks/libs/poco/lib/osx/PocoData.a ./openFrameworks/libs/poco/lib/osx/PocoDataSQLite.a ./openFrameworks/libs/poco/lib/osx/PocoFoundation.a ./openFrameworks/libs/poco/lib/osx/PocoJSON.a ./openFrameworks/libs/poco/lib/osx/PocoMongoDB.a ./openFrameworks/libs/poco/lib/osx/PocoNet.a ./openFrameworks/libs/poco/lib/osx/PocoNetSSL.a ./openFrameworks/libs/poco/lib/osx/PocoUtil.a ./openFrameworks/libs/poco/lib/osx/PocoXML.a ./openFrameworks/libs/poco/lib/osx/PocoZip.a ./openFrameworks/libs/openssl/lib/osx/ssl.a ./openFrameworks/libs/openssl/lib/osx/crypto.a ./openFrameworks/libs/cairo/lib/osx/cairo-script-interpreter.a ./openFrameworks/libs/cairo/lib/osx/cairo.a ./openFrameworks/libs/cairo/lib/osx/pixman-1.a ./openFrameworks/libs/cairo/lib/osx/png.a ./openFrameworks/libs/FreeImage/lib/osx/freeimage.a ./openFrameworks/libs/freetype/lib/osx/freetype.a -framework GLUT ./openFrameworks/libs/glfw/lib/osx/glfw3.a ./openFrameworks/libs/rtAudio/lib/osx/rtAudio.a -framework CoreServices -framework Foundation -lobjc -framework CoreGraphics -framework Cocoa -framework CoreVideo -framework IOKit -framework CoreAudio -framework Accelerate";
    command += " " + sketch.getCode()[0].getFile().getAbsolutePath() + " -o " + binFolder.getAbsolutePath() + "/ESP";
    System.out.println(command);
    try {
      Process process = Runtime.getRuntime().exec(command, null, coreLibraryFolder);
      new SystemOutSiphon(process.getInputStream());
      new SystemOutSiphon(process.getErrorStream());
      System.out.println("Exited with: " + process.waitFor());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      Process process = Runtime.getRuntime().exec("open " + appFolder.getAbsolutePath());
      new SystemOutSiphon(process.getInputStream());
      new SystemOutSiphon(process.getErrorStream());
      System.out.println("Exited with: " + process.waitFor());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
}
