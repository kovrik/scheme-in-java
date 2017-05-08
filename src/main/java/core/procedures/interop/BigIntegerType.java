package core.procedures.interop;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.utils.Utils;

import java.math.BigInteger;

public final class BigIntegerType extends AFn {

  public BigIntegerType() {
    super(new FnArgsBuilder().min(1).max(1).build());
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "bigint";
  }

  @Override
  public BigInteger apply1(Object arg) {
    if (arg instanceof Number) {
      return Utils.toBigInteger((Number) arg);
    }
    return new BigInteger(arg.toString());
  }
}