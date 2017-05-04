package core.procedures.meta;

import core.exceptions.WrongTypeException;
import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.scm.IMeta;
import core.scm.SCMSymbol;

import java.util.Map;

public final class WIthMeta extends AFn {

  public WIthMeta() {
    super(new FnArgsBuilder().min(2).max(2).mandatory(new Class[] {IMeta.class, Map.class}).build());
  }

  @Override
  public boolean isPure() {
    return false;
  }

  @Override
  public String getName() {
    return "with-meta";
  }

  @Override
  public Object apply2(Object obj, Object meta) {
    if (obj instanceof SCMSymbol) {
      return new SCMSymbol(((SCMSymbol) obj).getName(), (Map)meta);
    }
    throw new WrongTypeException(getName(), "IMeta", obj);
  }
}
