package core.procedures.cons;

import core.procedures.AFn;
import core.scm.FnArgs;
import core.scm.ICons;
import core.scm.SCMClass;

import java.util.List;

@FnArgs(minArgs = 1, maxArgs = 1, mandatoryArgsTypes = {SCMClass.SCMPair.class})
public class Cdr extends AFn {

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "cdr";
  }

  @Override
  public Object apply1(Object arg) {
    return cdr(arg);
  }

  public static Object cdr(Object o) {
    if (o instanceof ICons) {
      return ((ICons)o).cdr();
    }
    List list = (List) o;
    return list.subList(1, list.size());
  }
}
