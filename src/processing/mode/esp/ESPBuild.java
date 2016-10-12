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
        Util.copyDir(new File(coreFolder, "data"), new File(new File(contentsFolder, "Resources"), "data"));
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
    
      // Suppress warnings about OSAtomicIncrement32() and OSAtomicDecrement32() in poco/include/Poco/AtomicCounter.h
      command="g++ -std=c++11 -Wno-deprecated-declarations";
      for (String include : includes) command += " -Iinclude/" + include;
      for (String include : libraryIncludes) command += " -Ilibrary/" + include;
      for (String libdir : libdirs) command += " -Llibrary/" + libdir;
      for (String dylib : dylibs) command += " -l" + dylib;
      for (String framework : frameworks) command += " -framework " + framework;
      for (String lib : libs) command += " library/" + lib;
      command += " " + sketch.getCode()[0].getFile().getAbsolutePath() + " -o " + binFolder.getAbsolutePath() + "/ESP";
    }

    if (Platform.isWindows()) {
      File objFolder = new File(outputFolder, "obj"); objFolder.mkdir();
      File binFolder = new File(outputFolder, "bin");

      try {
        Util.copyDir(new File(coreFolder, "library\\openFrameworks\\export\\vs\\Win32"), binFolder);
        Util.copyDir(new File(coreFolder, "data"), new File(binFolder, "data"));
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }

      String[] windowsLibraryIncludes = {
        "openFrameworks/libs/glu/include",
        "openFrameworks/libs/videoInput/include"
      };

      String[] libs = {
        "openframeworksLib_debug.lib",
        "ESPlib_debug.lib",
        "grt.lib",
        "\"cairo-static.lib\"",
        "\"pixman-1.lib\"",
        "libpng.lib",
        "msimg32.lib",
        "OpenGL32.lib",
        "GLu32.lib",
        "kernel32.lib",
        "setupapi.lib",
        "Vfw32.lib",
        "comctl32.lib",
        "glut32.lib",
        "rtAudioD.lib",
        "videoInputD.lib",
        "libfreetype.lib",
        "FreeImage.lib",
        "qtmlClient.lib",
        "dsound.lib",
        "user32.lib",
        "gdi32.lib",
        "winspool.lib",
        "comdlg32.lib",
        "advapi32.lib",
        "shell32.lib",
        "ole32.lib",
        "oleaut32.lib",
        "uuid.lib",
        "glew32s.lib",
        "fmodex_vc.lib",
        "glu32.lib",
        "ssleay32MD.lib",
        "libeay32MD.lib",
        "crypt32.lib",
        "PocoFoundationmdd.lib",
        "PocoNetmdd.lib",
        "PocoUtilmdd.lib",
        "PocoXMLmdd.lib",
        "Ws2_32.lib",
        "tess2.lib",
        "glfw3.lib",
        "winmm.lib",
        "kernel32.lib",
        "user32.lib",
        "gdi32.lib",
        "winspool.lib",
        "comdlg32.lib",
        "advapi32.lib",
        "shell32.lib",
        "ole32.lib",
        "oleaut32.lib",
        "uuid.lib",
        "odbc32.lib",
        "odbccp32.lib",	  
      };

      String[] excludeLibraries = {
        "PocoFoundationmdd.lib",
        "atlthunk.lib",
        "msvcrt",
        "libcmt",
        "LIBC",
        "LIBCMTD"
      };

      String[] libdirs = {
        "",
        "openFrameworks/libs/glut/lib/vs/Win32",
        "openFrameworks/libs/glfw/lib/vs/Win32",
        "openFrameworks/libs/rtAudio/lib/vs/Win32",
        "openFrameworks/libs/FreeImage/lib/vs/Win32",
        "openFrameworks/libs/freetype/lib/vs/Win32",
        "openFrameworks/libs/quicktime/lib/vs/Win32",
        "openFrameworks/libs/fmodex/lib/vs/Win32",
        "openFrameworks/libs/videoInput/lib/vs/Win32",
        "openFrameworks/libs/cairo/lib/vs/Win32",
        "openFrameworks/libs/glew/lib/vs/Win32",
        "openFrameworks/libs/glu/lib/vs/Win32",
        "openFrameworks/libs/openssl/lib/vs/Win32",
        "openFrameworks/libs/Poco/lib/vs/Win32",
        "openFrameworks/libs/tess2/lib/vs/Win32",
        "openFrameworks/libs/boost/lib/vs/Win32",
      };

      String compileCommand = "cl.exe /c";
      for (String include : includes) compileCommand += " /Iinclude/" + include;
      for (String include : libraryIncludes) compileCommand += " /Ilibrary/" + include;
      for (String include : windowsLibraryIncludes) compileCommand += " /Ilibrary/" + include;
      compileCommand += " /Zi /W3 /WX- /MP /Od /Oy- /D WIN32 /D _DEBUG /D _CONSOLE /D POCO_STATIC /D CAIRO_WIN32_STATIC_BUILD /D DISABLE_SOME_FLOATING_POINT /D _UNICODE /D UNICODE /Gm- /EHsc /RTC1 /MDd /GS /fp:precise /Zc:wchar_t /Zc:forScope /Zc:inline /Fo" + '"' + new File(objFolder, "user.obj").getAbsolutePath() + '"' + " /Fd" + '"' + new File(objFolder, "vc140.pdb").getAbsolutePath() + '"' + " /Gd /TP /analyze- /errorReport:prompt";
      compileCommand += " " + '"'+ sketch.getCode()[0].getFile().getAbsolutePath() + '"';

      String linkCommand = "link.exe /ERRORREPORT:PROMPT /OUT:" + '"' + new File(binFolder, "ESP_debug.exe").getAbsolutePath() + '"' + " /INCREMENTAL /NOLOGO ";
      for (String lib : libs) linkCommand += " " + lib;
      for (String libdir : libdirs) linkCommand += " /LIBPATH:library/" + libdir;
      for (String lib : excludeLibraries) linkCommand += " /NODEFAULTLIB:" + lib;
      linkCommand += " /MANIFEST /MANIFESTUAC:\"level='asInvoker' uiAccess='false'\" /manifest:embed /DEBUG /PDB:" + '"' + new File(binFolder, "ESP_debug.pdb").getAbsolutePath() + '"' + 
      " /SUBSYSTEM:CONSOLE /TLBID:1 /DYNAMICBASE:NO /NXCOMPAT /IMPLIB:" + '"' + new File(binFolder, "ESP_debug.lib").getAbsolutePath() + '"' + " /MACHINE:X86 /SAFESEH /ignore:4099 " + '"' + new File(objFolder, "user.obj").getAbsolutePath() + '"'; //  obj\\Debug\\icon.res

      try {
        PrintWriter pw = new PrintWriter(new File(outputFolder, "compile.bat"));
        pw.println("CALL \"C:\\Program Files (x86)\\Microsoft Visual Studio 14.0\\VC\\vcvarsall.bat\"");
        pw.println("COPY \"%UniversalCRTSdkDir%\\bin\\x86\\ucrt\\ucrtbased.dll\" " + '"' + outputFolder.getAbsolutePath() + "\\bin" + '"');
        pw.println(compileCommand);
        pw.println(linkCommand);
        pw.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
        return;
      }

      command = "cmd /c " + '"' + new File(outputFolder, "compile.bat").getAbsolutePath() + '"';
    }

    System.out.println(command);
    final String cmd = command;
    Thread t = new Thread() {
      public void run() {
        try {
          Process process = Runtime.getRuntime().exec(cmd, null, coreFolder);
          new SystemOutSiphon(process.getInputStream());
          new SystemOutSiphon(process.getErrorStream());
          int result = process.waitFor();
          System.out.println("Exited with: " + result);
          if (result != 0) return;
        } catch (Exception e) {
          e.printStackTrace();
        }
        try {
          String runCommand = "";
          File runFolder = null;
          if (Platform.isMacOS()) {
            File appFolder = new File(outputFolder, "ESP.app");
            runCommand = "open " + appFolder.getAbsolutePath();
          }
          if (Platform.isWindows()) {
            File binFolder = new File(outputFolder, "bin");
            runCommand = new File(binFolder, "ESP_debug.exe").getAbsolutePath();
            runFolder = binFolder;
          }
          Process process = Runtime.getRuntime().exec(runCommand, null, runFolder);
          new SystemOutSiphon(process.getInputStream());
          new SystemOutSiphon(process.getErrorStream());
          System.out.println("Exited with: " + process.waitFor());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    t.start();
  }
}
