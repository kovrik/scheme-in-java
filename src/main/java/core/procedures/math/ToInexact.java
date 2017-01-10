package core.procedures.math;

import core.procedures.AFn;
import core.scm.FnArgs;
import core.scm.SCMBigComplex;
import core.scm.SCMBigRational;
import core.utils.NumberUtils;

import java.math.BigDecimal;

@FnArgs(minArgs = 1, maxArgs = 1, mandatoryArgsTypes = {Number.class})
public class ToInexact extends AFn {

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "exact->inexact";
  }

  @Override
  public Number apply1(Object arg) {
    return toInexact(arg);
  }

  public static Number toInexact(Object o) {
    if (o instanceof SCMBigComplex) {
      SCMBigComplex c = ((SCMBigComplex)o);
      return new SCMBigComplex(toInexact(c.getRe()), toInexact(c.getIm()));
    }
    if (o instanceof SCMBigRational) {
      return ((SCMBigRational)o).toBigDecimalInexact();
    }
    if (o instanceof BigDecimal) {
      int scale = Math.max(1, ((BigDecimal)o).scale());
      return ((BigDecimal)o).setScale(scale, NumberUtils.ROUNDING_MODE);
    }
    return ((Number)o).doubleValue();
  }
}
