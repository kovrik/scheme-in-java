package core.scm.specialforms;

import core.environment.Environment;
import core.evaluator.Evaluator;
import core.exceptions.IllegalSyntaxException;
import core.exceptions.ThrowableWrapper;
import core.exceptions.WrongTypeException;

import java.util.List;

public enum Throw implements ISpecialForm {
  THROW;

  private static final StackTraceElement[] EMPTY = new StackTraceElement[0];

  @Override
  public Object eval(List<Object> expression, Environment env, Evaluator evaluator) {
    if (expression.size() < 2) {
      throw IllegalSyntaxException.of(toString(), expression);
    }
    Object obj = evaluator.eval(expression.get(1), env);
    if (!(obj instanceof Throwable)) {
      throw new WrongTypeException(toString(), "Throwable", obj);
    }
    Throwable throwable = (Throwable) obj;
    /* Clear stacktrace */
    throwable.setStackTrace(EMPTY);
    throw new ThrowableWrapper(throwable);
  }

  @Override
  public String toString() {
    return "throw";
  }
}
