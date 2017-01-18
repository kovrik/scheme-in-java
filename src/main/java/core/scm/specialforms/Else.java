package core.scm.specialforms;

import core.environment.Environment;
import core.evaluator.Evaluator;
import core.exceptions.IllegalSyntaxException;
import core.scm.SCMSymbol;

import java.util.List;

public enum Else implements ISpecialForm {
  ELSE;

  public static final SCMSymbol ELSE_SYMBOL = SCMSymbol.of("else");

  @Override
  public Object eval(List<Object> expression, Environment env, Evaluator evaluator) {
    throw IllegalSyntaxException.of(toString(), expression, "not allowed as an expression");
  }

  @Override
  public String toString() {
    return "else";
  }
}