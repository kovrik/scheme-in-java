package core.procedures.io;

import core.exceptions.ArityException;
import core.exceptions.SCMIOException;
import core.exceptions.WrongTypeException;
import core.procedures.AFn;
import core.scm.SCMOutputPort;
import core.scm.SCMString;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class OpenOutputFile extends AFn {

  @Override
  public String getName() {
    return "open-output-file";
  }

  @Override
  public Object invoke(Object... args) {
    if (args.length != 1) {
      throw new ArityException(args.length, 1, getName());
    }
    if (!(args[0] instanceof String || args[0] instanceof SCMString)) {
      throw new WrongTypeException("String", args[0]);
    }
    String filename = args[0].toString();
    try {
      return new SCMOutputPort(new FileOutputStream(filename));
    } catch (FileNotFoundException e) {
      throw new SCMIOException(e);
    }
  }
}