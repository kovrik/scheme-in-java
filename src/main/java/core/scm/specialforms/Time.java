package core.scm.specialforms;

import core.environment.IEnvironment;
import core.evaluator.IEvaluator;
import core.exceptions.IllegalSyntaxException;
import core.scm.ISCMClass;
import core.scm.SCMClass;
import core.scm.SCMSymbol;
import core.scm.SCMUnspecified;
import core.writer.Writer;

import java.util.List;

/**
 * Time Special Form:
 *
 * Counts time taken for evaluation.
 *
 * Syntax:
 * (time <expression1> ... <expression n>)
 */
public class Time implements ISpecialForm, ISCMClass {

  public static final Time TIME = new Time();

  private final String syntax = "time";
  private final SCMSymbol symbol = new SCMSymbol(this.syntax);

  private Time() {}

  @Override
  public Object eval(List<Object> expression, IEnvironment env, IEvaluator evaluator) {
    if (expression.size() < 2) {
      throw new IllegalSyntaxException("time: bad syntax");
    }
    long start = System.nanoTime();
    for (int i = 1; i < expression.size() - 1; i++) {
      evaluator.eval(expression.get(i), env);
    }
    System.out.println(Writer.write(evaluator.eval(expression.get(expression.size() - 1), env)));
    long diff = (System.nanoTime() - start) / 1000000;
    System.out.println(String.format("time: %s ms", diff));
    return SCMUnspecified.UNSPECIFIED;
  }

  public SCMSymbol symbol() {
    return symbol;
  }

  @Override
  public String toString() {
    return syntax;
  }

  @Override
  public SCMClass getSCMClass() {
    return SCMClass.SPECIALFORM;
  }
}