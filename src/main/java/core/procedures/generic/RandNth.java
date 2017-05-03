package core.procedures.generic;

import core.exceptions.WrongTypeException;
import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.scm.SCMVector;

import java.util.List;
import java.util.Random;

public final class RandNth extends AFn {

  private final Count count = new Count();
  private final Get get = new Get();

  public RandNth() {
    super(new FnArgsBuilder().minArgs(1).maxArgs(1));
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "rand-nth";
  }

  @Override
  public Object apply1(Object arg) {
    if (!(arg instanceof SCMVector) && !(arg instanceof List) && !(arg instanceof CharSequence)) {
      throw new WrongTypeException(getName(), "List or Vector or String", arg);
    }
    int bound = count.apply1(arg);
    if (bound == 0) {
      throw new IndexOutOfBoundsException();
    }
    int index = new Random().nextInt(bound);
    return get.apply(arg, index);
  }
}
