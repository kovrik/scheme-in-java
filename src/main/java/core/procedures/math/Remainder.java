package core.procedures.math;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.scm.BigRational;
import core.utils.Utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class Remainder extends AFn {

  private static final String NAME = "remainder";

  public Remainder() {
    super(new FnArgsBuilder().min(2).max(2).mandatory(new Class[]{Long.class, Long.class}).build());
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Number apply2(Object arg1, Object arg2) {
    return apply((Number)arg1, (Number)arg2);
  }

  private static Number apply(BigDecimal first, BigDecimal second) {
    if (second.signum() == 0) {
      throw new ArithmeticException(String.format("%s: undefined for 0", NAME));
    }
    return first.remainder(second);
  }

  private static Number apply(BigInteger first, BigInteger second) {
    if (second.signum() == 0) {
      throw new ArithmeticException(String.format("%s: undefined for 0", NAME));
    }
    return first.remainder(second);
  }

  public static Number apply(Number first, Number second) {
    if (first instanceof BigRational) {
      first = ((BigRational) first).toBigDecimal();
    }
    if (second instanceof BigRational) {
      second = ((BigRational) second).toBigDecimal();
    }

    if ((first instanceof BigDecimal) && (second instanceof BigDecimal)) {
      return apply((BigDecimal)first, (BigDecimal)second);
    }
    if (first instanceof BigDecimal) {
      return apply((BigDecimal)first, Utils.toBigDecimal(second));
    }
    if (second instanceof BigDecimal) {
      return apply(Utils.toBigDecimal(first), (BigDecimal)second);
    }
    if ((first instanceof BigInteger) && (second instanceof BigInteger)) {
      return apply((BigInteger) first, (BigInteger)second);
    }
    if (first instanceof BigInteger) {
      return apply((BigInteger)first, new BigInteger(second.toString()));
    }
    if (second instanceof BigInteger) {
      return apply(new BigInteger(first.toString()), (BigInteger)second);
    }
    if ((first instanceof Double) || (second instanceof Double) || (first instanceof Float) || (second instanceof Float)) {
      if (second.intValue() == 0) {
        throw new ArithmeticException(String.format("%s: undefined for 0", NAME));
      }

      double result = first.doubleValue() % second.doubleValue();
      // Don't want negative zero
      if (result == -0.0) {
        return Math.abs(result);
      }
      return result;
    }
    if (second.longValue() == 0) {
      throw new ArithmeticException(String.format("%s: undefined for 0", NAME));
    }
    return first.longValue() % second.longValue();
  }
}
