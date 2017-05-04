package core.procedures.math;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.scm.SCMBigComplex;
import core.scm.SCMBigRational;
import core.utils.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class Addition extends AFn {

  public Addition() {
    super(new FnArgsBuilder().rest(Number.class).build());
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "+";
  }

  @Override
  public Number apply(Object... args) {
    switch (args.length) {
      case 0:  return 0L;
      case 1:  return (Number)args[0];
      default: {
        Object result = 0L;
        for (Object arg : args) {
          result = add((Number) result, (Number) arg);
        }
        return (Number) result;
      }
    }
  }

  public static Number add(Number first, Number second) {
    /* Special cases */
    if (NumberUtils.isZero(first)) {
      return NumberUtils.inexactnessTaint(second, first);
    }
    if (NumberUtils.isZero(second)) {
      return NumberUtils.inexactnessTaint(first, second);
    }
    /* Complex numbers*/
    if (first instanceof SCMBigComplex) {
      return ((SCMBigComplex) first).plus(second);
    }
    if (second instanceof SCMBigComplex) {
      return ((SCMBigComplex) second).plus(first);
    }
    /* Big Rational numbers */
    if ((first instanceof SCMBigRational) && (second instanceof SCMBigRational)) {
      return ((SCMBigRational)first).plus((SCMBigRational)second);
    }
    if (first instanceof SCMBigRational) {
      if (second instanceof Long || second instanceof BigDecimal) {
        return ((SCMBigRational) first).plus(SCMBigRational.valueOf(second.toString(), "1"));
      } else {
        first = ((SCMBigRational)first).doubleOrBigDecimalValue();
      }
    }
    if (second instanceof SCMBigRational) {
      if (first instanceof Long || first instanceof BigDecimal) {
        return SCMBigRational.valueOf(first.toString(), "1").plus((SCMBigRational) second);
      } else {
        second = ((SCMBigRational)second).doubleOrBigDecimalValue();
      }
    }
    if (first instanceof Float && second instanceof Float) {
      float result = first.floatValue() + second.floatValue();
      if (Float.isNaN(result) || Float.isInfinite(result)) {
        return new BigDecimal(first.toString()).add(new BigDecimal(second.toString()));
      }
      return result;
    }
    if (first instanceof Double || second instanceof Double || first instanceof Float || second instanceof Float) {
      double result = first.doubleValue() + second.doubleValue();
      if (Double.isNaN(result) || Double.isInfinite(result)) {
        return new BigDecimal(first.toString()).add(new BigDecimal(second.toString()));
      }
      return result;
    }
    if (first instanceof BigDecimal) {
      return ((BigDecimal)first).add(NumberUtils.toBigDecimal(second));
    }
    if (second instanceof BigDecimal) {
      return ((BigDecimal) second).add(NumberUtils.toBigDecimal(first));
    }
    if (first instanceof BigInteger) {
      return ((BigInteger)first).add(new BigInteger(second.toString()));
    }
    if (second instanceof BigInteger) {
      return ((BigInteger)second).add(new BigInteger(first.toString()));
    }
    long f = first.longValue();
    long s = second.longValue();
    try {
      return Math.addExact(f, s);
    } catch (ArithmeticException e) {
      return BigDecimal.valueOf(f).add(BigDecimal.valueOf(s));
    }
  }
}
