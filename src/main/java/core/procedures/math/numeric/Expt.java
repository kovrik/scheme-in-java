package core.procedures.math.numeric;

import core.exceptions.ArityException;
import core.exceptions.WrongTypeException;
import core.procedures.AFn;

import java.math.BigDecimal;

public class Expt extends AFn implements INumericalOperation {

  @Override
  public Number invoke(Object... args) {
    if (args != null && args.length == 2) {
      if (!(args[0] instanceof Number)) {
        throw new WrongTypeException("Number", args[0]);
      }
      if (!(args[1] instanceof Number)) {
        throw new WrongTypeException("Number", args[1]);
      }
      return apply((Number)args[0], (Number)args[1]);
    }
    throw new ArityException(args.length, 2, "expt");
  }

  @Override
  public Number zero() {
    throw new ArityException(0, 2, "expt");
  }

  @Override
  public Number apply(Number first, Number second) {
    double result = Math.pow(first.doubleValue(), second.doubleValue());
    if (Double.isInfinite(result)) {
      return new BigDecimal(first.longValue()).pow(second.intValue());
    }
    return result;
  }

  @Override
  public Object apply(Object first, Object second) {
    return invoke(first, second);
  }
}
