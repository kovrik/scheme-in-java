package core.scm.specialforms;

import core.environment.IEnvironment;
import core.evaluator.IEvaluator;
import core.scm.ISCMClass;

import java.util.List;

// TODO
public enum LetRecSyntax implements ISpecialForm, ISCMClass {
  LETREC_SYNTAX;

  private static final String syntax = "letrec-syntax";

  @Override
  public Object eval(List<Object> expression, IEnvironment env, IEvaluator evaluator) {
    throw new UnsupportedOperationException("NOT IMPLEMENTED YET!");
  }

  @Override
  public String toString() {
    return syntax;
  }
}
