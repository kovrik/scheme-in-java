package core.scm.specialforms;

import core.environment.IEnvironment;
import core.evaluator.IEvaluator;
import core.scm.ISCMClass;

import java.util.List;

// TODO
public enum SyntaxRules implements ISpecialForm, ISCMClass {
  SYNTAX_RULES;

  private final String syntax = "syntax-rules";

  @Override
  public Object eval(List<Object> expression, IEnvironment env, IEvaluator evaluator) {
    throw new UnsupportedOperationException("NOT IMPLEMENTED YET!");
  }

  @Override
  public String toString() {
    return syntax;
  }
}
