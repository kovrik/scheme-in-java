package core.procedures.bit;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.scm.SCMClass;

public final class BItShiftLeft extends AFn {

  public BItShiftLeft() {
    super(new FnArgsBuilder().minArgs(2).maxArgs(2).mandatoryArgsTypes(new Class[] {SCMClass.BitOp.class, Long.class}));
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "bit-shift-left";
  }

  @Override
  public Long apply2(Object arg1, Object arg2) {
    return ((Number) arg1).longValue() << ((Number)arg2).longValue();
  }
}
