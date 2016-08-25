package unittests;

import core.environment.DefaultEnvironment;
import core.environment.IEnvironment;
import core.evaluator.Evaluator;
import core.evaluator.IEvaluator;
import core.reader.IReader;
import core.reader.Reader;
import org.junit.Test;

import static core.scm.SCMBoolean.TRUE;
import static org.junit.Assert.assertEquals;

public class TCOTest {

  private final IReader reader = new Reader();
  private final IEvaluator eval = new Evaluator();
  private final DefaultEnvironment env = new DefaultEnvironment();
  {
    /* Eval lib procedures */
    for (String proc : env.getLibraryProcedures()) {
      eval(proc, env);
    }
  }

  /* Helper method */
  private Object eval(String sexp, IEnvironment env) {
    return eval.eval(reader.read(sexp), env);
  }

  @Test
  public void testIfTCO() {
    String recursive = "(define (recursive n)" +
                       "  (if (zero? n)" +
                       "      \"DONE\"" +
                       "      (recursive (- n 1))))";
    eval(recursive, env);

    assertEquals("DONE", eval("(recursive 5)", env));
    assertEquals("DONE", eval("(recursive 100000)", env));
  }

  @Test
  public void testOrTCO() {
    String recursive = "(define (recOr n) (or (zero? n) (recOr (- n 1))))";
    eval(recursive, env);

    assertEquals(TRUE, eval("(recOr 5)", env));
    assertEquals(TRUE, eval("(recOr 100000)", env));
  }

  @Test
  public void testDefineAndBeginTCO() {
    String recursive = "(define (foo n) (if (= n 100000) n (foo (+ n 1)))";
    eval(recursive, env);
    assertEquals(100000L, eval("(foo 5)", env));
  }

  // TODO Check `cond`
  // TODO Tests for other Special Forms

}
