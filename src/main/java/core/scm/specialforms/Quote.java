package core.scm.specialforms;

import core.environment.Environment;
import core.evaluator.Evaluator;
import core.scm.SCMSymbol;

import java.util.List;

/* Literal expressions
 * Syntax:
 * (quote <datum>)
 * '<datum>
 * <constant>
 */
public enum Quote implements ISpecialForm {
  QUOTE;

  public static final SCMSymbol QUOTE_SYMBOL = SCMSymbol.of(QUOTE.toString());

  @Override
  public Object eval(List<Object> expression, Environment env, Evaluator evaluator) {
    return expression.get(1);
  }

  @Override
  public String toString() {
    return "quote";
  }
}
