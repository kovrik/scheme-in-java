package core.procedures.io;

import core.Repl;
import core.procedures.AFn;
import core.procedures.FnArgsBuilder;

public final class CurrentInputPort extends AFn {

  public CurrentInputPort() {
    super(new FnArgsBuilder().max(0).build());
  }

  @Override
  public String getName() {
    return "current-input-port";
  }

  @Override
  public Object apply0() {
    return Repl.getCurrentInputPort();
  }
}
