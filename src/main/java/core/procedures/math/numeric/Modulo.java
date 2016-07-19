package core.procedures.math.numeric;

import core.exceptions.ArityException;
import core.exceptions.WrongTypeException;
import core.procedures.AFn;

import java.math.BigDecimal;

public class Modulo extends AFn implements INumericalOperation {

  // TODO move out
  private static final Remainder rem = new Remainder();

  @Override
  public Number invoke(Object... args) {
    if (args != null && args.length == 2) {
      if (!(args[0] instanceof Number)) {
        throw new WrongTypeException("Integer", args[0]);
      }
      if (!(args[1] instanceof Number)) {
        throw new WrongTypeException("Integer", args[1]);
      }
      return apply((Number)args[0], (Number)args[1]);
    }
    throw new ArityException(args.length, 2, "modulo");
  }

  @Override
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

  @Override
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

  @Override
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
