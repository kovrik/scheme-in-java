package core.procedures.math;

import core.exceptions.ArityException;
import core.exceptions.WrongTypeException;
import core.procedures.AFn;
import core.scm.SCMBigRational;
import core.utils.BigDecimalMath;
import core.utils.NumberUtils;

import java.math.BigDecimal;

public class Log extends AFn {

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "log";
  }

  @Override
  public Number invoke(Object... args) {
    if (args != null && args.length == 1) {
      if (!(args[0] instanceof Number)) {
        throw new WrongTypeException("Number", args[0]);
      }
      return invoke((Number)args[0]);
    }
    throw new ArityException(args.length, 1, getName());
  }

  public Number invoke(Number number) {
    if (number instanceof Double) {
      if ((Double.isNaN((Double) number)) || (Double.isInfinite((Double) number))) {
        return number;
      }
      return Math.log(number.doubleValue());
    }
    if (number instanceof Long) {
      if (number.longValue() == 0) {
        throw new ArithmeticException("log: undefined for 0");
      }
      return Math.log(number.doubleValue());
    }
    if (number instanceof SCMBigRational) {
      if (((SCMBigRational) number).isZero()){
        throw new ArithmeticException("log: undefined for 0");
      }
      return BigDecimalMath.log((SCMBigRational)number, NumberUtils.DEFAULT_CONTEXT);
    }
    if (number instanceof BigDecimal) {
      if (((BigDecimal)number).compareTo(BigDecimal.ZERO) == 0) {
        throw new ArithmeticException("log: undefined for 0");
      }
      return BigDecimalMath.log((SCMBigRational) ToExact.toExact(number), NumberUtils.DEFAULT_CONTEXT);
    }
    return Math.log(number.doubleValue());
  }
}
