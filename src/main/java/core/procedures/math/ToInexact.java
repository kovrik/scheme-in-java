package core.procedures.math;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.scm.SCMBigComplex;
import core.scm.SCMBigRational;
import core.utils.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class ToInexact extends AFn {

  public ToInexact() {
    super(new FnArgsBuilder().min(1).max(1).mandatory(new Class[]{Number.class}).build());
  }

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
    if (o instanceof BigInteger) {
      return new BigDecimal(o.toString());
    }
    if (o instanceof BigDecimal) {
      int scale = Math.max(1, ((BigDecimal)o).scale());
      return ((BigDecimal)o).setScale(scale, NumberUtils.ROUNDING_MODE);
    }
    return ((Number)o).doubleValue();
  }
}
