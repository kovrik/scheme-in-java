package core.scm.specialforms;

import core.environment.Environment;
import core.evaluator.Evaluator;

import java.util.List;

// TODO
public enum LetSyntax implements ISpecialForm {
  LET_SYNTAX;

  @Override
  public Object eval(List<Object> expression, Environment env, Evaluator evaluator) {
    throw new UnsupportedOperationException("NOT IMPLEMENTED YET!");
  }

  @Override
  public String toString() {
    return "let-syntax";
  }
}
