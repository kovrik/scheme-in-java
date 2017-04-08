package core.procedures.generic;

import core.exceptions.WrongTypeException;
import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.scm.SCMMutableString;
import core.scm.SCMVector;

import java.util.Collection;
import java.util.Map;

public class Count extends AFn {

  public Count() {
    super(new FnArgsBuilder().minArgs(1).maxArgs(1));
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "count";
  }

  @Override
  public Integer apply1(Object arg) {
    if (arg instanceof Map) {
      return ((Map)arg).size();
    } else if (arg instanceof Collection) {
      return ((Collection)arg).size();
    } else if (arg instanceof SCMVector) {
      return ((SCMVector)arg).length();
    } else if (arg instanceof String) {
      return ((String) arg).length();
    } else if (arg instanceof SCMMutableString)  {
      return ((SCMMutableString) arg).length();
    } else if (arg instanceof StringBuilder)  {
      return ((StringBuilder) arg).length();
    }
    throw new WrongTypeException(getName(), "List or Map or Vector or Set or String", arg);
  }
}