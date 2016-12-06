package core.procedures.io;

import core.Main;
import core.procedures.AFn;
import core.scm.FnArgs;

@FnArgs(args = {})
public class CurrentOutputPort extends AFn {

  @Override
  public String getName() {
    return "current-output-port";
  }

  @Override
  public Object invoke(Object... args) {
    return Main.getCurrentOutputPort();
  }
}
