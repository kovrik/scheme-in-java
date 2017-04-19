package core.procedures.equivalence;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;

import static core.scm.SCMCons.EMPTY;

public class Eq extends AFn {

  public Eq() {
    super(new FnArgsBuilder().minArgs(2));
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "eq?";
  }

  @Override
  public Boolean apply(Object... args) {
    Boolean result = Boolean.TRUE;
    for (int i = 0; i < args.length - 1; i++) {
      result = result && eq(args[i], args[i + 1]);
    }
    return result;
  }

  @Override
  public Boolean apply2(Object arg1, Object arg2) {
    return eq(arg1, arg2);
  }

  private boolean eq(Object first, Object second) {
    return EMPTY.equals(first) && (EMPTY.equals(second)) || first == second;
  }
}
