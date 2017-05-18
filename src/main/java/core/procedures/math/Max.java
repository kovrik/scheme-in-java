package core.procedures.math;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.scm.BigRatio;
import core.scm.Type;
import core.utils.Utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class Max extends AFn {

  public Max() {
    super(new FnArgsBuilder().min(1).mandatory(new Class[]{Type.Real.class})
                             .rest(Type.Real.class).build());
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "max";
  }

  @Override
  public Number apply(Object... args) {
    if (args.length == 1) {
      return (Number) args[0];
    }
    Object result = args[0];
    for (Object arg : args) {
      result = max((Number) result, (Number) arg);
    }
    return (Number) result;
  }

  private Number max(Number first, Number second) {
    /* Big Ratio numbers */
    if ((first instanceof BigRatio) && (second instanceof BigRatio)) {
      return ((BigRatio)first).compareTo((BigRatio)second) > 0 ? first : second;
    }
    if (first instanceof BigRatio) {
      first = first.doubleValue();
    }
    if (second instanceof BigRatio) {
      second = second.doubleValue();
    }
    if ((first instanceof Integer) && (second instanceof Integer)) {
      return Math.max((int)first, (int)second);
    }
    if ((first instanceof Long) && (second instanceof Long)) {
      return Math.max((long)first, (long)second);
    }
    if ((first instanceof Float) && (second instanceof Float)) {
      return Math.max((float)first, (float)second);
    }
    if ((first instanceof Double) && (second instanceof Double)) {
      return Math.max((double)first, (double) second);
    }
    if ((first instanceof BigInteger) && (second instanceof BigInteger)) {
      return ((BigInteger)first).max((BigInteger) second);
    }
    if ((first instanceof BigDecimal) && (second instanceof BigDecimal)) {
      return ((BigDecimal)first).max((BigDecimal) second);
    }
    if (first instanceof BigDecimal) {
      int i = ((BigDecimal) first).compareTo(Utils.INSTANCE.toBigDecimal(second));
      return (i < 0) ? second : first;
    }
    if (second instanceof BigDecimal) {
      int i = ((BigDecimal) second).compareTo(Utils.INSTANCE.toBigDecimal(first));
      return (i < 0) ? first : second;
    }
    if (first.doubleValue() == second.doubleValue()) {
      return first;
    } else if (first.doubleValue() > second.doubleValue()) {
      return first;
    }
    return second;
  }
}
