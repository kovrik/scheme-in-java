package core.procedures.math.trigonometry;

import core.procedures.AFn;
import core.scm.FnArgs;
import core.scm.SCMBigComplex;
import core.scm.SCMBigRational;
import core.utils.NumberUtils;

import java.math.BigDecimal;

@FnArgs(minArgs = 1, maxArgs = 1, mandatoryArgsTypes = {Number.class})
public class Tan extends AFn {

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "tan";
  }

  @Override
  public Number apply1(Object arg) {
    /* Special cases */
    if (NumberUtils.isZero(arg)) {
      return 0L;
    }
    if (arg instanceof Long) {
      return Math.tan((Long) arg);
    } else if (arg instanceof Double) {
      return Math.tan((Double) arg);
    } else if (arg instanceof BigDecimal) {
      return tan((BigDecimal)arg);
    } else if (arg instanceof SCMBigComplex) {
      return Tan.tan((SCMBigComplex)arg);
    } else {
      return tan(((SCMBigRational)arg).toBigDecimal());
    }
  }

  public static double tan(BigDecimal bd) {
    double v = bd.doubleValue();
    if (Double.isInfinite(v) || Double.isNaN(v)) {
      return Double.NaN;
    } else {
      return Math.tan(v);
    }
  }

  public static SCMBigComplex tan(SCMBigComplex c) {
    SCMBigComplex sin = Sin.sin(c);
    SCMBigComplex cos = Cos.cos(c);
    return sin.divide(cos);
  }
}