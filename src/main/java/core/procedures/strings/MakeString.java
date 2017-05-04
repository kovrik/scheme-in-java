package core.procedures.strings;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.scm.SCMClass;
import core.scm.SCMMutableString;

public final class MakeString extends AFn {

  public MakeString() {
    super(new FnArgsBuilder().min(1).max(2)
                             .mandatory(new Class[]{SCMClass.ExactNonNegativeInteger.class})
                             .rest(Character.class).build());
  }

  @Override
  public String getName() {
    return "make-string";
  }

  @Override
  public SCMMutableString apply(Object... args) {
    Long s = ((Number)args[0]).longValue();
    Object c = (args.length == 1) ? Character.MIN_VALUE : args[1];
    SCMMutableString string = new SCMMutableString();
    for (long i = 0; i < s; i++) {
      string.append(c);
    }
    return string;
  }
}
