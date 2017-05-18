package core.procedures.generic;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.utils.Utils;

import java.util.Map;
import java.util.Random;

public final class RandNth extends AFn {

  private final Count count = new Count();
  private final Get get = new Get();

  public RandNth() {
    super(new FnArgsBuilder().min(1).max(1).build());
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
    if (arg instanceof Map) {
      throw new UnsupportedOperationException("nth not supported on this type: " + arg.getClass());
    }
    if (!Utils.INSTANCE.isSeqable(arg)) {
      throw new IllegalArgumentException("don't know how to create Sequence from " + arg.getClass());
    }
    int bound = count.apply1(arg);
    if (bound == 0) {
      throw new IndexOutOfBoundsException();
    }
    int index = new Random().nextInt(bound);
    return get.apply(arg, index);
  }
}
