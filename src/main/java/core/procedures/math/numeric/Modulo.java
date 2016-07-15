package core.procedures.math.numeric;

import core.exceptions.ArityException;
import core.procedures.AFn;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Modulo extends AFn implements INumericalOperation {

  // TODO move out
  private static final Remainder rem = new Remainder();

  @Override
  public Number invoke(Object... args) {
    if (args != null && args.length == 2) {
      if (!(args[0] instanceof Number)) {
        throw new IllegalArgumentException("Wrong argument type. Expected: Integer, actual: " + args[0].getClass().getSimpleName());
      }
      if (!(args[1] instanceof Number)) {
        throw new IllegalArgumentException("Wrong argument type. Expected: Integer, actual: " + args[1].getClass().getSimpleName());
      }
      return apply((Number)args[0], (Number)args[1]);
    }
    throw new ArityException(args.length, 2, "modulo");
  }

  public Number zero() {
    throw new ArityException(0, 2, "modulo");
  }

  public BigDecimal apply(BigDecimal first, BigDecimal second) {
    if (second.compareTo(BigDecimal.ZERO) == 0) {
      throw new ArithmeticException("Error: (modulo) undefined for 0");
    }
    BigDecimal remainder = first.remainder(second);
    if (remainder.compareTo(BigDecimal.ZERO) == 0) {
      return remainder;
    }
    if ((first.compareTo(BigDecimal.ZERO) > 0) == (second.compareTo(BigDecimal.ZERO) > 0)) {
      return remainder;
    }
    return second.add(remainder);
  }

  public BigInteger apply(BigInteger first, BigInteger second) {
    if (second.compareTo(BigInteger.ZERO) == 0) {
      throw new ArithmeticException("Error: (modulo) undefined for 0");
    }
    BigInteger remainder = first.remainder(second);
    if (remainder.compareTo(BigInteger.ZERO) == 0) {
      return remainder;
    }
    if ((first.compareTo(BigInteger.ZERO) > 0) == (second.compareTo(BigInteger.ZERO) > 0)) {
      return remainder;
    }
    return second.add(remainder);
  }

  public Number apply(Number first, Number second) {

    if ((first instanceof BigDecimal) && (second instanceof BigDecimal)) {
      return apply((BigDecimal) first, (BigDecimal)second);
    }
    if (first instanceof BigDecimal) {
      return apply((BigDecimal) first, new BigDecimal(second.toString()));
    }
    if (second instanceof BigDecimal) {
      return apply((BigDecimal) second, new BigDecimal(first.toString()));
    }

    if ((first instanceof BigInteger) && (second instanceof BigInteger)) {
      return apply((BigInteger) first, (BigInteger)second);
    }
    if (first instanceof BigInteger) {
      return apply((BigInteger) first, new BigInteger(second.toString()));
    }
    if (second instanceof BigInteger) {
      return apply((BigInteger) second, new BigInteger(first.toString()));
    }

    // check if they are integral
    if (first.doubleValue() != Math.floor(first.doubleValue())) {
      throw new IllegalArgumentException("Error: (modulo) bad argument type - not an integer: " + first);
    }
    if (second.doubleValue() != Math.floor(second.doubleValue())) {
      throw new IllegalArgumentException("Error: (modulo) bad argument type - not an integer: " + second);
    }
    if (second.intValue() == 0) {
      throw new ArithmeticException("Error: (modulo) undefined for 0");
    }

    Number m = rem.apply(first, second);
    if (m.intValue() == 0) {
      return m;
    }
    if ((first.longValue() > 0) == (second.longValue() > 0)) {
      return m;
    }
    if ((first instanceof Double) || (second instanceof Double)) {
      return m.doubleValue() + second.doubleValue();
    } else {
      return m.longValue() + second.longValue();
    }
  }

  public Object apply(Object first, Object second) {
    return invoke(first, second);
  }

  @Override
  public Object call() throws Exception {
    return invoke();
  }

  @Override
  public void run() {
    invoke();
  }
}
