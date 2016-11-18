package core.procedures.io;

import core.Main;
import core.exceptions.ArityException;
import core.exceptions.SCMIOException;
import core.exceptions.WrongTypeException;
import core.procedures.AFn;
import core.scm.SCMBoolean;
import core.scm.SCMInputPort;

import java.io.IOException;

// FIXME Doesn't work properly
public class IsCharReady extends AFn {

  @Override
  public String getName() {
    return "char-ready?";
  }

  @Override
  public Object invoke(Object... args) {
    if (args.length > 1) {
      throw new ArityException(args.length, 1, getName());
    }
    SCMInputPort inputPort;
    if (args.length == 0) {
      inputPort = Main.getCurrentInputPort();
    } else {
      if (!(args[0] instanceof SCMInputPort)) {
        throw new WrongTypeException("Input Port", args[0]);
      }
      inputPort = ((SCMInputPort)args[0]);
    }
    int bytesAvailable;
    try {
      bytesAvailable = inputPort.available();
    } catch (IOException e) {
      throw new SCMIOException(e);
    }
    return SCMBoolean.toSCMBoolean(bytesAvailable > 0);
  }
}