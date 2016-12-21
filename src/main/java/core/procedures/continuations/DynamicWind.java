package core.procedures.continuations;

import core.procedures.AFn;
import core.procedures.IFn;
import core.scm.FnArgs;

@FnArgs(args = {IFn.class, IFn.class, IFn.class})
public class DynamicWind extends AFn {

  @Override
  public boolean isPure() {
    return false;
  }

  @Override
  public String getName() {
    return "dynamic-wind";
  }

  @Override
  public Object apply(Object... args) {
    // FIXME
    throw new UnsupportedOperationException("Must be evaluated in Evaluator!");
  }
}
