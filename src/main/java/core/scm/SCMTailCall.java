package core.scm;

import core.environment.IEnvironment;

/**
 * Tail Call object is used for Tail Call Optimization.
 * Instead of evaluating form in a Tail position (which requires creating new stack frame),
 * we wrap that form into SCMTailCall and return (destroying one stack frame).
 *
 * Ideally, we should just return a Continuation, but Full Continuations are not implemented.
 *
 * See https://groups.csail.mit.edu/mac/ftpdir/scheme-reports/r5rs-html.old/r5rs_22.html
 */
public class SCMTailCall {

  private final Object expr;
  private final IEnvironment context;

  public SCMTailCall(Object expr, IEnvironment context) {
    this.expr = expr;
    this.context = context;
  }

  public Object getExpr() {
    return expr;
  }

  public IEnvironment getContext() {
    return context;
  }
}
