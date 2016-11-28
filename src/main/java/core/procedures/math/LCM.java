package core.procedures.math;

import core.exceptions.WrongTypeException;
import core.procedures.AFn;
import core.scm.SCMBigRational;
import core.utils.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class LCM extends AFn {

  private static final Abs ABS = new Abs();

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "lcm";
  }

  @Override
  public Object invoke(Object... args) {
    if (args != null) {
      if (args.length == 0) {
        return 1L;
      }
      Object result = args[0];
      if (!(result instanceof Number)) {
        throw new WrongTypeException("Integer", result);
      }
      if (args.length == 1) {
        return ABS.invoke(args[0]);
      }
      for (int i = 1; i < args.length; i++) {
        Number first = (Number)result;
        if (!(args[i] instanceof Number)) {
          throw new WrongTypeException("Integer", args[i]);
        }
        result = invoke((Number)first, (Number)args[i]);
      }
      return result;
    }
    return 1L;
  }

  private static long lcm(Long a, Long b) {
    if ((a.intValue() == 0) && (b.intValue() == 0)) {
      return 0L;
    }
    return (a / GCD.gcd(a, b)) * b;
  }

  private static Double lcm(Double a, Double b) {
    if ((a.intValue() == 0) && (b.intValue() == 0)) {
      return 0d;
    }
    return (a / GCD.gcd(a, b).doubleValue()) * b;
  }

  public static BigInteger lcm(BigInteger first, BigInteger second) {
    return first.multiply(second.divide(GCD.gcd(first, second)));
  }

  public static SCMBigRational lcm(SCMBigRational first, SCMBigRational second) {
    return new SCMBigRational(lcm(first.getNumerator(), second.getNumerator()),
                              GCD.gcd(first.getDenominator(), second.getDenominator()));
  }

  private static Number lcm(BigDecimal a, BigDecimal b) {
    if ((BigDecimal.ZERO.compareTo(a) == 0) && (BigDecimal.ZERO.compareTo(b) == 0)) {
      return BigDecimal.ZERO;
    }
    int scale = Math.max(a.scale(), b.scale());
    if (scale == 0) {
      return new BigDecimal(a.toBigInteger().gcd(b.toBigInteger()));
    } else {
      // TODO Check if works
      SCMBigRational first  = (SCMBigRational)NumberUtils.toExact(a.setScale(scale));
      SCMBigRational second = (SCMBigRational)NumberUtils.toExact(b.setScale(scale));
      return NumberUtils.toInexact(lcm(first, second));
    }
  }

  public Number invoke(Number first, Number second) {
    if ((first instanceof Long) && (second instanceof Long)) {
      return lcm((Long)first, (Long)second);
    }
    if ((first instanceof SCMBigRational) && (second instanceof SCMBigRational)) {
      return lcm((SCMBigRational) first, (SCMBigRational)second);
    }
    if (first instanceof SCMBigRational) {
      return lcm(((SCMBigRational) first).toBigDecimal(), new BigDecimal(second.toString()));
    }
    if (second instanceof SCMBigRational) {
      return lcm(new BigDecimal(first.toString()), ((SCMBigRational) second).toBigDecimal());
    }
    if ((first instanceof BigDecimal) && (second instanceof BigDecimal)) {
      return lcm((BigDecimal)first, (BigDecimal)second);
    }
    if (first instanceof BigDecimal) {
      return lcm((BigDecimal)first, new BigDecimal(second.toString()));
    }
    if (second instanceof BigDecimal) {
      return lcm(new BigDecimal(first.toString()), (BigDecimal)second);
    }

    return lcm(first.doubleValue(), second.doubleValue());
  }
}
