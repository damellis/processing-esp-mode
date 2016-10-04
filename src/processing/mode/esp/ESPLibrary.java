package processing.mode.esp;

import java.io.File;

import processing.app.Library;


public class ESPLibrary extends Library {

  public ESPLibrary(File folder) {
    super(folder);
  }


  @Override
  protected void handle() {
    // no platform-specific stuff to do here; clear out the superclass
    // parsing of the .jar file and whatnot
  }
}
