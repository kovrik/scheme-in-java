package core.procedures.equivalence;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;

public class Identical extends AFn {

  public Identical() {
    super(new FnArgsBuilder().minArgs(2));
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "identical?";
  }

  @Override
  public Boolean apply(Object... args) {
    Boolean result = Boolean.TRUE;
    for (int i = 0; i < args.length - 1; i++) {
      result = result && identical(args[i], args[i + 1]);
    }
    return result;
  }

  @Override
  public Boolean apply2(Object arg1, Object arg2) {
    return identical(arg1, arg2);
  }

  private boolean identical(Object first, Object second) {
    return first == second;
  }
}