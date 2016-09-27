package unittests;

import core.environment.DefaultEnvironment;
import core.environment.IEnvironment;
import core.evaluator.Evaluator;
import core.evaluator.IEvaluator;
import core.reader.IReader;
import core.reader.Reader;

public abstract class AbstractTest {

  private final IReader reader = new Reader();
  protected final IEvaluator eval = new Evaluator();
  protected final DefaultEnvironment env = new DefaultEnvironment();
  {
    /* Eval lib procedures */
    for (String proc : env.getLibraryProcedures()) {
      for (Object p : reader.read(proc)) {
        eval.eval(p, env);
      }
    }
  }
  /* Helper method: evaluates first S-expression */
  protected Object eval(String sexp, IEnvironment env) {
    return eval.eval(reader.readFirst(sexp), env);
  }
}