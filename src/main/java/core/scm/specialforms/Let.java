package core.scm.specialforms;

import core.environment.Environment;
import core.environment.IEnvironment;
import core.evaluator.IEvaluator;
import core.exceptions.IllegalSyntaxException;
import core.scm.*;

import java.util.List;

/* Syntax:
 * (let <bindings> <body>)
 *
 * <bindings>: ((<variable1> <init1>) ...)
 */
public enum Let implements ISpecialForm {
  LET;

  private static final String syntax = "let";

  @Override
  public Object eval(List<Object> expression, IEnvironment env, IEvaluator evaluator) {
    if (expression.size() < 3) {
      throw IllegalSyntaxException.of(syntax, expression);
    }
    /* Normal let:
     * (let ((id expr) ...) body ...+) */
    if (expression.get(1) instanceof List) {
      IEnvironment localEnv = new Environment(env);
      /* Evaluate inits */
      List bindings = (List) expression.get(1);
      for (Object binding : bindings) {
        Object var  = ((List)binding).get(0);
        Object init = ((List)binding).get(1);
        if (localEnv.get(var) != null) {
          throw IllegalSyntaxException.of(syntax, expression, String.format("duplicate identifier `%s`", var));
        }
        localEnv.put(var, evaluator.eval(init, env));
      }

      /* Evaluate body */
      for (int i = 2; i < expression.size() - 1; i++) {
        evaluator.eval(expression.get(i), localEnv);
      }
      /* Return Tail Call of the last expression */
      return new SCMTailCall(expression.get(expression.size() - 1), localEnv);

    } else if (expression.get(1) instanceof SCMSymbol) {
      // TODO Optimize and cleanup
      /* Named let:
       * (let proc-id ((arg-id init-expr) ...) body ...+) */
      Object o = expression.get(1);
      if (!(o instanceof SCMSymbol)) {
        throw IllegalSyntaxException.of(syntax, expression);
      }
      /* Construct lambda */
      SCMCons<Object> lambdaArgs = SCMCons.list();
      SCMCons<Object> initValues = SCMCons.list();
      List bindings = (List)expression.get(2);
      for (Object binding : bindings) {
        Object arg = ((List)binding).get(0);
        if (lambdaArgs.contains(arg)) {
          throw IllegalSyntaxException.of(syntax, expression, String.format("duplicate identifier `%s`", arg));
        }
        lambdaArgs.add(arg);
        initValues.add(((List)binding).get(1));
      }
      Object lambdaBody = expression.get(3);
      SCMCons lambda = SCMCons.list(Lambda.LAMBDA, lambdaArgs, lambdaBody);
      SCMSymbol name = (SCMSymbol)o;
      SCMCons<SCMCons> l = SCMCons.list();
      l.add(SCMCons.list(name, lambda));

      SCMCons<Object> body = SCMCons.list(name);
      body.addAll(initValues);

      /* Named let is implemented via letrec */
      SCMCons<Object> letrec = SCMCons.list(LetRec.LETREC);
      letrec.add(l);
      letrec.add(body);
      /* Letrec has TCO */
      return LetRec.LETREC.eval(letrec, new Environment(env), evaluator);
    }
    throw IllegalSyntaxException.of(syntax, expression);
  }

  @Override
  public String toString() {
    return syntax;
  }
}
