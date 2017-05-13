package core.procedures.generic;

import core.exceptions.WrongTypeException;
import core.procedures.AFn;
import core.procedures.FnArgsBuilder;

import java.util.*;

public final class Sort extends AFn {

  public Sort() {
    super(new FnArgsBuilder().min(1).max(1).build());
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "sort";
  }

  // TODO accept comparator as optional first argument
  @Override
  public Object apply1(Object arg) {
    try {
      if (arg instanceof Collection) {
        Collections.sort((List) arg);
        return arg;
      }
      if (arg instanceof Map) {
        return new TreeMap<>((Map) arg);
      }
    } catch (ClassCastException e) {
      // ignore
    }
    throw new WrongTypeException(getName(), "Collection of comparable elements", arg);
  }
}
