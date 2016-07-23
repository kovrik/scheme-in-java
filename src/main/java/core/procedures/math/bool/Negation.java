package core.procedures.math.bool;

import core.scm.SCMBoolean;
import core.exceptions.ArityException;
import core.procedures.AFn;

public class Negation extends AFn implements IBooleanOperation {

  @Override
  public SCMBoolean invoke(Object... args) {
    if (args != null && args.length == 1) {
      return SCMBoolean.toSCMBoolean(!SCMBoolean.valueOf(args[0]));
    }
    throw new ArityException(args.length, "not");
  }

  @Override
  public Boolean zero() {
    throw new ArityException(0, "not");
  }

  @Override
  public Boolean apply(Boolean first, Boolean second) {
    throw new ArityException(2, "not");
  }

  @Override
  public Object apply(Object first, Object second) {
    throw new ArityException(2, "not");
  }
}
