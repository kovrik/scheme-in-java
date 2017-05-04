package core.procedures.vectors;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.scm.SCMClass;
import core.scm.SCMMutableVector;
import core.scm.SCMVoid;

public final class VectorSet extends AFn {

  public VectorSet() {
    super(new FnArgsBuilder().min(3).max(3)
                             .mandatory(new Class[]{SCMMutableVector.class, SCMClass.ExactNonNegativeInteger.class}).build());
  }

  @Override
  public String getName() {
    return "vector-set!";
  }

  @Override
  public Object apply3(Object arg1, Object arg2, Object arg3) {
    SCMMutableVector vec = (SCMMutableVector)arg1;
    Long pos = ((Number)arg2).longValue();
    if (pos >= vec.length()) {
      throw new IndexOutOfBoundsException(String.format("%s: value out of range: %s", getName(), pos));
    }
    vec.set(pos.intValue(), arg3);
    return SCMVoid.VOID;
  }
}
