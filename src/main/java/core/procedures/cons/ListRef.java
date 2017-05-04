package core.procedures.cons;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.scm.SCMClass;
import core.scm.SCMCons;

import java.util.List;

public final class ListRef extends AFn {

  public ListRef() {
    super(new FnArgsBuilder().min(2).max(2)
                             .mandatory(new Class[]{SCMClass.SCMPair.class, SCMClass.ExactNonNegativeInteger.class}).build());
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "list-ref";
  }

  @Override
  public Object apply2(Object arg1, Object arg2) {
    List list = (List)arg1;
    Long p = ((Number)arg2).longValue();
    if (p >= list.size()) {
      throw new IndexOutOfBoundsException(String.format("%s: value out of range: %s", getName(), p));
    }
    /* Cons cell */
    if ((list instanceof SCMCons) && !((SCMCons)list).isList()) {
      if (p == 0) {
        return ((SCMCons)list).car();
      } else {
        throw new IllegalArgumentException(String.format("%s: index (%s) reaches a non-pair", getName(), p));
      }
    }
    return list.get(p.intValue());
  }
}
