package core.procedures.math.complex;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.scm.BigComplex;

public final class RealPart extends AFn {

  public RealPart() {
    super(new FnArgsBuilder().min(1).max(1).mandatory(new Class[]{Number.class}).build());
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "real-part";
  }

  @Override
  public Number apply1(Object arg) {
    if (arg instanceof BigComplex) {
      return ((BigComplex)arg).getRe();
    }
    return (Number)arg;
  }
}
