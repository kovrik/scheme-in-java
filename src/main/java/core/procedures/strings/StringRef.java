package core.procedures.strings;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.scm.Type;

public final class StringRef extends AFn {

  public StringRef() {
    super(new FnArgsBuilder().min(2).max(2)
                             .mandatory(new Class[]{CharSequence.class, Type.ExactNonNegativeInteger.class}).build());
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "string-ref";
  }

  @Override
  public Object apply2(Object arg1, Object arg2) {
    String s = arg1.toString();
    Long pos = ((Number)arg2).longValue();
    if (pos >= s.length()) {
      throw new IndexOutOfBoundsException(String.format("%s: value out of range: %s", getName(), pos));
    }
    return s.charAt(pos.intValue());
  }
}
