package main.core.procedures.equivalence;

import main.core.procedures.IFn;
import main.core.procedures.math.IOperation;

public class CharEqCi implements IOperation, IFn {

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
    return Boolean.TRUE;
  }

  public Boolean apply(Object first, Object second) {
    if (!(first instanceof Character) || !(second instanceof Character)) {
      throw new IllegalArgumentException("Wrong type of argument to `char-ci=?`");
    }
    return Character.toLowerCase((Character)first) == Character.toLowerCase((Character)second);
  }

  public Object call() throws Exception {
    return invoke();
  }

  public void run() {
    invoke();
  }
}
