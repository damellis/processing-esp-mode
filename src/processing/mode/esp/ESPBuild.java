package processing.mode.esp;

import java.util.*;
import java.io.*;
import java.nio.file.Files;

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
    File binFolder = new File(outputFolder, "bin"); binFolder.mkdir();
    File frameworksFolder = new File(outputFolder, "Frameworks"); frameworksFolder.mkdir();
    String command="g++ -std=c++11 -I../include -I../include/ESP -I../include/ofxDatGui -I../include/ofxDatGui/components -I../include/ofxDatGui/core -I../include/ofxDatGui/libs/ofxSmartFont -I../include/ofxDatGui/themes -I../include/ofxGrt -I../include/ofxParagraph -IopenFrameworks/libs/openFrameworks -IopenFrameworks/libs/openFrameworks/3d/ -IopenFrameworks/libs/openFrameworks/app/ -IopenFrameworks/libs/openFrameworks/communication/ -IopenFrameworks/libs/openFrameworks/events/ -IopenFrameworks/libs/openFrameworks/gl/ -IopenFrameworks/libs/openFrameworks/graphics/ -IopenFrameworks/libs/openFrameworks/math/ -IopenFrameworks/libs/openFrameworks/sound/ -IopenFrameworks/libs/openFrameworks/types/ -IopenFrameworks/libs/openFrameworks/utils/ -IopenFrameworks/libs/openFrameworks/video/ -IopenFrameworks/libs/openFrameworks -IopenFrameworks/libs/poco/include -IopenFrameworks/libs/freetype/include -IopenFrameworks/libs/freetype/include/freetype2 -IopenFrameworks/libs/fmodex/include -IopenFrameworks/libs/glew/include -IopenFrameworks/libs/FreeImage/include -IopenFrameworks/libs/tess2/include -IopenFrameworks/libs/cairo/include/cairo -IopenFrameworks/libs/rtAudio/include -IopenFrameworks/libs/glfw/include -IopenFrameworks/libs/boost/include -IopenFrameworks/libs/utf8cpp/include -IopenFrameworks/libs/openssl/include -IopenFrameworks/addons/ofxOsc/src -IopenFrameworks/addons/ofxOsc/libs/oscpack/src/ip -IopenFrameworks/addons/ofxOsc/libs/oscpack/src/osc -L. -LopenFrameworks/libs/fmodex/lib/osx -LopenFrameworks/libs/glut/lib/osx -lesp -lgrt -lfmodex ./openFrameworks/libs/tess2/lib/osx/tess2.a -framework OpenGL ./openFrameworks/libs/glew/lib/osx/glew.a ./openFrameworks/libs/boost/lib/osx/boost_filesystem.a ./openFrameworks/libs/boost/lib/osx/boost_system.a ./openFrameworks/libs/boost/lib/osx/boost.a ./openFrameworks/libs/poco/lib/osx/PocoCrypto.a ./openFrameworks/libs/poco/lib/osx/PocoData.a ./openFrameworks/libs/poco/lib/osx/PocoDataSQLite.a ./openFrameworks/libs/poco/lib/osx/PocoFoundation.a ./openFrameworks/libs/poco/lib/osx/PocoJSON.a ./openFrameworks/libs/poco/lib/osx/PocoMongoDB.a ./openFrameworks/libs/poco/lib/osx/PocoNet.a ./openFrameworks/libs/poco/lib/osx/PocoNetSSL.a ./openFrameworks/libs/poco/lib/osx/PocoUtil.a ./openFrameworks/libs/poco/lib/osx/PocoXML.a ./openFrameworks/libs/poco/lib/osx/PocoZip.a ./openFrameworks/libs/openssl/lib/osx/ssl.a ./openFrameworks/libs/openssl/lib/osx/crypto.a ./openFrameworks/libs/cairo/lib/osx/cairo-script-interpreter.a ./openFrameworks/libs/cairo/lib/osx/cairo.a ./openFrameworks/libs/cairo/lib/osx/pixman-1.a ./openFrameworks/libs/cairo/lib/osx/png.a ./openFrameworks/libs/FreeImage/lib/osx/freeimage.a ./openFrameworks/libs/freetype/lib/osx/freetype.a -framework GLUT ./openFrameworks/libs/glfw/lib/osx/glfw3.a ./openFrameworks/libs/rtAudio/lib/osx/rtAudio.a -framework CoreServices -framework Foundation -lobjc -framework CoreGraphics -framework Cocoa -framework CoreVideo -framework IOKit -framework CoreAudio -framework Accelerate";
    command += " " + sketch.getCode()[0].getFile().getAbsolutePath() + " -o " + binFolder.getAbsolutePath() + "/esp";
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
      Files.copy(new File(coreLibraryFolder, "libgrt.dylib").toPath(), new File(frameworksFolder, "libgrt.dylib").toPath());
      Files.copy(new File(coreLibraryFolder, "openFrameworks/libs/fmodex/lib/osx/libfmodex.dylib").toPath(), new File(binFolder, "libfmodex.dylib").toPath());
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      Process process = Runtime.getRuntime().exec(new File(binFolder, "esp").getAbsolutePath(), null, new File(coreFolder, "Resources"));
      new SystemOutSiphon(process.getInputStream());
      new SystemOutSiphon(process.getErrorStream());
      System.out.println("Exited with: " + process.waitFor());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
}
