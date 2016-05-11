package main.core.procedures.equivalence;

import main.core.procedures.IFn;
import main.core.procedures.math.IOperation;

public class StringEq implements IOperation, IFn {

  public Boolean invoke(Object... args) {
    Boolean result = zero();
    if (args != null && args.length > 1) {
      for (int i = 0; i < args.length - 1; i++) {
        result = result && apply(args[i], args[i + 1]);
      }
    }
    return result;
  }

  public Boolean zero() {
    return Boolean.FALSE;
  }

  public Boolean apply(Object first, Object second) {
    if (!(first instanceof String) || !(second instanceof String)) {
      throw new IllegalArgumentException("Wrong type of argument to `string=?`");
    }
    return first.equals(second);
  }

  public Object call() throws Exception {
    return invoke();
  }

  public void run() {
    invoke();
  }
}
