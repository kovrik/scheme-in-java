package core.procedures.strings;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;

public final class StartsWith extends AFn {

  public StartsWith() {
    super(new FnArgsBuilder().min(2).max(2).mandatory(new Class[]{CharSequence.class, CharSequence.class}).build());
  }

  @Override
  public String getName() {
    return "starts-with?";
  }

  @Override
  public Boolean apply2(Object arg1, Object arg2) {
    return arg1.toString().startsWith(arg2.toString());
  }
}
