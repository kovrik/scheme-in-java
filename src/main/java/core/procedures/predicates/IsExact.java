package core.procedures.predicates;

import core.exceptions.ArityException;
import core.exceptions.WrongTypeException;
import core.procedures.AFn;
import core.scm.SCMBoolean;

import static core.utils.NumberUtils.isExact;

public class IsExact extends AFn {

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "exact?";
  }

  @Override
  public SCMBoolean invoke(Object... args) {
    if (args.length != 1) {
      throw new ArityException(args.length, 1, getName());
    }
    if (!(args[0] instanceof Number)) {
      throw new WrongTypeException("Number", args[0]);
    }
    return SCMBoolean.toSCMBoolean(isExact(args[0]));
  }
}
