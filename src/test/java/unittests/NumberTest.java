package unittests;

import core.environment.DefaultEnvironment;
import core.environment.IEnvironment;
import core.evaluator.Evaluator;
import core.evaluator.IEvaluator;
import core.exceptions.ArityException;
import core.reader.IReader;
import core.reader.Reader;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static core.scm.SCMBoolean.FALSE;
import static core.scm.SCMBoolean.TRUE;
import static org.junit.Assert.*;

public class NumberTest {

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

  @Before
  public void setUp() throws Exception {
    // TODO Create new environment for each test?
  }

  @Test
  public void testEvalNumbers() {
    assertEquals(1L, eval("1", env));
    assertEquals(-15L, eval("-15", env));
    assertEquals(-2.5d, eval("-2.5", env));
    assertEquals(5L, eval("#x5", env));
    assertEquals(15L, eval("#xf", env));
    assertEquals(13L, eval("#b1101", env));
  }

  @Test
  public void testEvalMath() {
    assertEquals(6L,  eval("(+ 1 2 3)", env));
    assertEquals(5.5, eval("(/ (+ 1 2 3 (- (* 2 2.5 2) 5)) 2)", env));
    assertEquals(5.0, eval("(/ 10.0 2)", env));
    assertEquals(0.1, eval("(/ 10)", env));
    assertEquals(3.25, eval("(/ 13 4)", env));
    assertEquals(2L, eval("(/ 10 5)", env));
    assertEquals(2d, eval("(/ 10.0 5)", env));
    assertEquals(2d, eval("(/ 10 5.0)", env));

    assertEquals(5L, eval("(abs 5)", env));
    assertEquals(5L, eval("(abs -5)", env));

    // abs
    try {
      eval("(abs)", env);
      fail();
    } catch (ArityException e) {
      assertTrue(e.getMessage().contains("Wrong number of arguments (actual: 0, expected: 1) passed to: abs"));
    }
    try {
      eval("(abs 1 2 3)", env);
      fail();
    } catch (ArityException e) {
      assertTrue(e.getMessage().contains("Wrong number of arguments (actual: 3, expected: 1) passed to: abs"));
    }
    try {
      eval("(abs \"not-a-number\")", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Wrong argument type. Expected: Number, actual: \"not-a-number\""));
    }

    // sqrt
    assertEquals(5d, eval("(sqrt 25)", env));
    assertEquals(3d, eval("(sqrt 9.0)", env));
    assertTrue(Double.isNaN((Double)eval("(sqrt -5)", env)));

    assertEquals(0.01, eval("(/ 1 10 10)", env));
  }

  @Test
  public void testEvalNumericalComparison() {
    assertEquals(TRUE,  eval("(= 1 1 1)", env));
    assertEquals(FALSE, eval("(= 1 0 1)", env));
    assertEquals(TRUE,  eval("(= 0)", env));
    assertEquals(TRUE,  eval("(= 0.57 0.5700)", env));
    assertEquals(TRUE,  eval("(= 7 7.00)", env));

    assertEquals(TRUE,  eval("(> 2 1)", env));
    assertEquals(TRUE,  eval("(> 2 1.123)", env));
    assertEquals(TRUE,  eval("(>= 2 1.123)", env));
    assertEquals(TRUE,  eval("(>= 2.5 1.123)", env));
    assertEquals(TRUE,  eval("(<= -2.5 1.123)", env));
    assertEquals(TRUE,  eval("(< -2.5 1.123)", env));
  }

  @Test
  public void testNumberTheoreticDivision() {
    // quotient
    assertEquals(3L,  eval("(quotient 13 4)", env));
    assertEquals(3d,  eval("(quotient 13.0 4)", env));
    assertEquals(1L,  eval("(quotient 5 5)", env));
    assertEquals(1d,  eval("(quotient 5.0 5)", env));
    assertEquals(1d,  eval("(quotient -5 -5.0)", env));
    assertEquals(-1L, eval("(quotient -5 5)", env));
    assertEquals(-1d, eval("(quotient -5 5.)", env));
    try {
      eval("(quotient -10 0.0001)", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Error: (quotient) bad argument type - not an integer: 1.0E-4", e.getMessage());
    }
    try {
      eval("(quotient -10 0.0)", env);
      fail();
    } catch (ArithmeticException e) {
      assertEquals("Error: (quotient) undefined for 0", e.getMessage());
    }

    // remainder
    assertEquals(-1L, eval("(remainder -13 4)", env));
    assertEquals(1L, eval("(remainder 13 -4)", env));
    assertEquals(-1L, eval("(remainder -13 -4)", env));
    assertEquals(-1.0, eval("(remainder -13 -4.0)", env));
    assertEquals(1L, eval("(remainder 13 4)", env));
    assertEquals(0L, eval("(remainder 10 2)", env));
    assertEquals(0d, eval("(remainder 10 2.0)", env));
    assertEquals(0d, eval("(remainder -10 2.0)", env));
    try {
      eval("(remainder -10 0.0001)", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Error: (remainder) bad argument type - not an integer: 1.0E-4", e.getMessage());
    }
    try {
      eval("(remainder -10 0.0)", env);
      fail();
    } catch (ArithmeticException e) {
      assertEquals("Error: (remainder) undefined for 0", e.getMessage());
    }

    // modulo
    assertEquals(2L,  eval("(modulo 5 3)", env));
    assertEquals(2d,  eval("(modulo 5 3.0)", env));
    assertEquals(1L,  eval("(modulo 13 4)", env));
    assertEquals(-1L, eval("(modulo -13 -4)", env));
    try {
      eval("(modulo -10 0.0001)", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Error: (modulo) bad argument type - not an integer: 1.0E-4", e.getMessage());
    }
    try {
      eval("(modulo -10 0.0)", env);
      fail();
    } catch (ArithmeticException e) {
      assertEquals("Error: (modulo) undefined for 0", e.getMessage());
    }
    assertEquals(3L,  eval("(modulo -13 4)", env));
    assertEquals(-3L, eval("(modulo 13 -4)", env));
  }

  @Test
  public void testEvalIsZero() {
    assertEquals(TRUE,  eval("(zero? 0)", env));
    assertEquals(TRUE,  eval("(zero? 0.0)", env));
    assertEquals(FALSE, eval("(zero? 1)", env));
    assertEquals(FALSE, eval("(zero? -5)", env));

    try {
      eval("(zero? \"test\")", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Number, actual: \"test\"", e.getMessage());
    }
  }

  @Test
  public void testEvalNegative() {
    assertEquals(FALSE, eval("(negative? 0)", env));
    assertEquals(FALSE, eval("(negative? 0.0)", env));
    assertEquals(FALSE, eval("(negative? 1)", env));
    assertEquals(FALSE, eval("(negative? (* -5 -6))", env));
    assertEquals(TRUE,  eval("(negative? -5)", env));
    try {
      eval("(negative? \"test\")", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Number, actual: \"test\"", e.getMessage());
    }
  }

  @Test
  public void testEvalPositive() {
    assertEquals(FALSE, eval("(positive? 0)", env));
    assertEquals(FALSE, eval("(positive? 0.0)", env));
    assertEquals(TRUE,  eval("(positive? 1)", env));
    assertEquals(TRUE,  eval("(positive? (* -5 -6))", env));
    assertEquals(FALSE, eval("(positive? -5)", env));
    try {
      eval("(positive? \"test\")", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Number, actual: \"test\"", e.getMessage());
    }
  }

  @Test
  public void testEvalEven() {
    assertEquals(TRUE,  eval("(even? 0)", env));
    assertEquals(TRUE,  eval("(even? 0.0)", env));
    assertEquals(TRUE,  eval("(even? 4)", env));
    assertEquals(TRUE,  eval("(even? 100)", env));
    assertEquals(FALSE, eval("(even? 1)", env));
    assertEquals(TRUE,  eval("(even? (* -5 -6))", env));
    assertEquals(FALSE, eval("(even? -5)", env));
    try {
      eval("(even? \"test\")", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Integer, actual: \"test\"", e.getMessage());
    }
  }

  @Test
  public void testEvalOdd() {
    assertEquals(FALSE, eval("(odd? 0)", env));
    assertEquals(FALSE, eval("(odd? 0.0)", env));
    assertEquals(FALSE, eval("(odd? 4)", env));
    assertEquals(FALSE, eval("(odd? 100)", env));
    assertEquals(TRUE,  eval("(odd? 1)", env));
    assertEquals(FALSE, eval("(odd? 4)", env));
    assertEquals(FALSE, eval("(odd? (* -5 -6))", env));
    assertEquals(TRUE,  eval("(odd? -5)", env));
    try {
      eval("(odd? \"test\")", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Integer, actual: \"test\"", e.getMessage());
    }
  }

  @Test
  public void testEvalRound() {
    assertEquals(0L,   eval("(round 0)",    env));
    assertEquals(4L,   eval("(round 4)",    env));
    assertEquals(-4L,  eval("(round -4)",   env));
    assertEquals(0.0,  eval("(round 0.0)",  env));
    assertEquals(1.0,  eval("(round 1.0)",  env));
    assertEquals(2.0,  eval("(round 1.5)",  env));
    assertEquals(-2.0, eval("(round -1.5)", env));
    assertEquals(2.0,  eval("(round 2.5)",  env));
    assertEquals(-0.0, eval("(round -0.5)", env));
    assertEquals(-2.0, eval("(round -1.7)", env));
    assertEquals(4.0,  eval("(round 3.7)",  env));
    assertEquals(3.0,  eval("(round 2.7)",  env));
    assertEquals(2.0,  eval("(round 2.5)",  env));
    try {
      eval("(round \"test\")", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Number, actual: \"test\"", e.getMessage());
    }
  }

  @Test
  public void testEvalFloor() {
    assertEquals(0L,   eval("(floor 0)",    env));
    assertEquals(4L,   eval("(floor 4)",    env));
    assertEquals(-5.0, eval("(floor -4.3)", env));
    assertEquals(3.0,  eval("(floor 3.5)",  env));
    assertEquals(1.0,  eval("(floor 1.2)",  env));
    assertEquals(-2.0, eval("(floor -1.2)", env));
    try {
      eval("(floor \"test\")", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Number, actual: \"test\"", e.getMessage());
    }
  }

  @Test
  public void testEvalCeiling() {
    assertEquals(0L,   eval("(ceiling 0)",    env));
    assertEquals(4L,   eval("(ceiling 4)",    env));
    assertEquals(-4.0, eval("(ceiling -4.3)", env));
    assertEquals(4.0,  eval("(ceiling 3.5)",  env));
    assertEquals(2.0,  eval("(ceiling 1.2)",  env));
    assertEquals(-1.0, eval("(ceiling -1.2)", env));
    try {
      eval("(ceiling \"test\")", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Number, actual: \"test\"", e.getMessage());
    }
  }

  @Test
  public void testEvalTruncate() {
    assertEquals(0L,   eval("(truncate 0)",    env));
    assertEquals(4L,   eval("(truncate 4)",    env));
    assertEquals(-4L,  eval("(truncate -4)",   env));
    assertEquals(3.0,  eval("(truncate 3.5)",  env));
    assertEquals(-3.0, eval("(truncate -3.5)", env));
    assertEquals(2.0,  eval("(truncate 2.2)",  env));
    assertEquals(-1.0, eval("(truncate -1.2)", env));
    try {
      eval("(truncate \"test\")", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Number, actual: \"test\"", e.getMessage());
    }
  }

  @Test
  public void testEvalMax() {
    assertEquals(0L,   eval("(max 0)",    env));
    assertEquals(5.0,  eval("(max 5.0)",  env));
    assertEquals(-5.0, eval("(max -5.0)", env));
    assertEquals(-5.0, eval("(max -6 -7 -5.0)", env));
    assertEquals(7.0,  eval("(max 6 7 5.0)",    env));

    try {
      eval("(max \"test\" 1 2 3)", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Number, actual: \"test\"", e.getMessage());
    }

    try {
      eval("(max 0 \"test\")", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Number, actual: \"test\"", e.getMessage());
    }
  }

  @Test
  public void testEvalMin() {
    assertEquals(0L,   eval("(min 0)",    env));
    assertEquals(5.0,  eval("(min 5.0)",  env));
    assertEquals(-5.0, eval("(min -5.0)", env));
    assertEquals(-7.0, eval("(min -6 -7 -5.0)", env));
    assertEquals(5.0,  eval("(min 6 7 5.0)",    env));
    try {
      eval("(min \"test\" 1 2 3)", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Number, actual: \"test\"", e.getMessage());
    }

    try {
      eval("(min 0 \"test\")", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Number, actual: \"test\"", e.getMessage());
    }
  }

  @Test
  public void testEvalGCD() {
    // gcd of no args is 0
    assertEquals(0L, eval("(gcd)", env));
    // gcd of 0(s) is 0
    assertEquals(0L, eval("(gcd 0)", env));
    assertEquals(0d, eval("(gcd 0.0)", env));
    assertEquals(0L, eval("(gcd 0 0)", env));
    assertEquals(0d, eval("(gcd 0 0.0)", env));
    assertEquals(5L, eval("(gcd 5 0)", env));
    assertEquals(5d, eval("(gcd 5.0 0)", env));
    assertEquals(5L, eval("(gcd 0 5)", env));

    // gcd of n is n
    assertEquals(5L, eval("(gcd 5)", env));
    assertEquals(5L, eval("(gcd -5)", env));

    // gcd of multiple numbers
    assertEquals(5L, eval("(gcd 5 10)", env));
    assertEquals(1L, eval("(gcd 3 6 8)", env));

    // TODO Doubles
    assertEquals(3d, eval("(gcd 3.0 6)", env));
    assertEquals(40000d, eval("(gcd 200000.0 40000.0)", env));
    try {
      eval("(gcd 3.3 6)", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Integer, actual: 3.3", e.getMessage());
    }

    assertEquals(new BigDecimal("9"), eval("(gcd 99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999 9)", env));

    /* Check switch from Double to BigDecimal for big numbers */
    assertEquals(3L,                  eval("(gcd 99999999999999999 123)", env));
    assertEquals(3L,                  eval("(gcd 999999999999999999 123)", env));
    assertEquals(new BigDecimal("3"),   eval("(gcd 9999999999999999999 123)", env));
    assertEquals(new BigDecimal("123"), eval("(gcd 99999999999999999999 123)", env));
    assertEquals(new BigDecimal("3"),   eval("(gcd 999999999999999999999 123)", env));
    assertEquals(new BigDecimal("3"),   eval("(gcd 9999999999999999999999 123)", env));
  }

  @Test
  public void testEvalLCM() {
    // lcm of no args is 0
    assertEquals(0L, eval("(lcm)", env));
    // lcm of 0(s) is 0
    assertEquals(0L, eval("(lcm 0)", env));
    assertEquals(0d, eval("(lcm 0.0)", env));
    assertEquals(0L, eval("(lcm 0 0)", env));

    // lcm of n is n
    assertEquals(5L, eval("(lcm 5)", env));
    assertEquals(5d, eval("(lcm 5.0)", env));
    assertEquals(5L, eval("(lcm -5)", env));

    // lcm of multiple numbers
    assertEquals(10L, eval("(lcm 5 10)", env));
    assertEquals(24L, eval("(lcm 3 6 8)", env));
    assertEquals(24d, eval("(lcm 3 6 8.0)", env));

    // TODO Doubles
    assertEquals(6d, eval("(lcm 3.0 6)", env));
    assertEquals(200000d, eval("(lcm 200000.0 40000.0)", env));
    try {
      eval("(lcm 3.3 6)", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Integer, actual: 3.3", e.getMessage());
    }

    String big = "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
    assertEquals(new BigDecimal(big), eval(String.format("(lcm %s 9)", big), env));
  }

  @Test
  public void testEvalExpt() {
    assertEquals(1.0, eval("(expt 9 0)", env));
    assertEquals(0.0, eval("(expt 0 10)", env));
    assertEquals(1.0, eval("(expt 1 1)", env));
    assertEquals(8.0, eval("(expt 2 3)", env));
    assertEquals(16777216.0, eval("(expt 4 12)", env));
    assertEquals(25.0, eval("(expt -5 2)", env));
    assertEquals(-125.0, eval("(expt -5 3)", env));
    assertEquals(13.489468760533386, eval("(expt 2.2 3.3)", env));
    try {
      eval("(expt \"test\" 1)", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Number, actual: \"test\"", e.getMessage());
    }
    try {
      eval("(expt 1)", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong number of arguments (actual: 1, expected: 2) passed to: expt", e.getMessage());
    }
  }

  @Test
  public void testBigDecimal() {

    String big0 = "2000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    assertEquals(new BigDecimal(big0), eval(big0, env));

    BigDecimal big1 = new BigDecimal("46253220748483971668312221557921994865355615937726643119142534763573384386717010179" +
        "33035570379376866259256597659610145236756255853523897837700465755231009606767424706" +
        "73348011864143704094117912896787287887428473700404535710995567015427340013704721113" +
        "33316343260021388334157118647272603383652782933091786683436259682248241271823609303" +
        "74088996645418723950501545025386234033486857262740020740808886229945286599837304752" +
        "25837944037056491016035642078654527374207462210630264065442615967117912099739418879" +
        "84132513");

    assertEquals(big1, eval("(expt 33 333)", env));
    assertEquals(TRUE, eval(String.format("(number? %s)", big0), env));

    assertEquals(new BigDecimal(big0), eval(String.format("(* (/ %s 10) 10)", big0), env));
    assertEquals(new BigDecimal(big0).multiply(new BigDecimal("2")), eval(String.format("(+ %s %s)", big0, big0), env));
    assertEquals(new BigDecimal(big0).multiply(new BigDecimal("2")).subtract(new BigDecimal(big0)),
        eval(String.format("(- (* 2 %s) %s)", big0, big0), env));


    assertEquals(new BigDecimal(big0), eval(String.format("(truncate (+ 0.2 %s))", big0),  env));
    assertEquals(new BigDecimal(big0).negate(), eval(String.format("(truncate (+ 0.2 -%s))", big0),  env));
    assertEquals(new BigDecimal(big0), eval(String.format("(floor (+ 0.2 %s))", big0),  env));
    assertEquals(new BigDecimal(big0).add(BigDecimal.ONE),
        eval(String.format("(ceiling (+ 0.2 %s))", big0),  env));

    assertEquals(new BigDecimal(big0), eval(String.format("(abs -%s)", big0),  env));
    assertEquals(new BigDecimal(big0).add(BigDecimal.ONE),
        eval(String.format("(max (+ 1 %s) %s)", big0, big0),  env));

    assertEquals(new BigDecimal(big0),
        eval(String.format("(min (+ 1 %s) %s)", big0, big0),  env));

    String big2 = "941737268473075634481294063531333847658485002458168527101639838005582185517473483816983389228732066437165294377295109210176795859047876399460771530181828861843994801526320659067260600443063376955200810073997787724454002350759571876705644517946943898492214066331998886559185229835330687165577365519449395424366904222913306696961330084086377946063169138303897697242206192836209273444873251023411764271944704088313845446589768727760791185170266144604537045173629663045739300767985189493967771010336173962367396474652866334212802605674879313278209206179544726008444885447395757991883875945457869103573901612777316112247438629624081718143710269108788904389008167209091151002216893051746019091645742839251513268837094248809018521046734530253606053753445604156050903737280600427015788467630468023527367174845920094011539693975275654700093627716413";
    assertEquals(BigDecimal.ONE, eval(String.format("(modulo %s 4)", big2), env));
    assertEquals(new BigDecimal("-2"), eval(String.format("(modulo %s -5)", big2), env));

    assertEquals(BigDecimal.ONE, eval(String.format("(remainder %s 4)", big2), env));
    assertEquals(new BigDecimal("3"), eval(String.format("(remainder %s -5)", big2), env));

    String quotientResult1 = "470868634236537817240647031765666923829242501229084263550819919002791092758736741908491694614366033218582647188647554605088397929523938199730385765090914430921997400763160329533630300221531688477600405036998893862227001175379785938352822258973471949246107033165999443279592614917665343582788682759724697712183452111456653348480665042043188973031584569151948848621103096418104636722436625511705882135972352044156922723294884363880395592585133072302268522586814831522869650383992594746983885505168086981183698237326433167106401302837439656639104603089772363004222442723697878995941937972728934551786950806388658056123719314812040859071855134554394452194504083604545575501108446525873009545822871419625756634418547124404509260523367265126803026876722802078025451868640300213507894233815234011763683587422960047005769846987637827350046813858206";
    assertEquals(new BigDecimal(quotientResult1), eval(String.format("(quotient %s 2)", big2), env));
    assertEquals(new BigDecimal(2), eval(String.format("(quotient %s (quotient %s 2))", big2, big2), env));

    assertEquals(TRUE, eval(String.format("(eqv? %s %s)", big2, big2), env));
    assertEquals(TRUE, eval(String.format("(<= %s %s)", big2, big2), env));
    assertEquals(FALSE, eval(String.format("(< %s %s)", big2, big2), env));
    assertEquals(TRUE, eval(String.format("(> (+ 1 %s) %s)", big2, big2), env));
    assertEquals(TRUE, eval(String.format("(< (+ 1 2) %s)", big2), env));

    // FIXME
    assertEquals(Double.POSITIVE_INFINITY, eval(String.format("(sqrt %s)", big2), env));
  }

  @Test
  public void testEvalIsInteger() {
    assertEquals(TRUE,  eval("(integer? 0)", env));
    assertEquals(TRUE,  eval("(integer? 0.0)", env));
    assertEquals(TRUE,  eval("(integer? 4)", env));
    assertEquals(TRUE,  eval("(integer? 100)", env));
    assertEquals(TRUE,  eval("(integer? 1)", env));
    assertEquals(TRUE,  eval("(integer? (* -5 -6))", env));
    assertEquals(TRUE,  eval("(integer? -5)", env));
    assertEquals(FALSE, eval("(integer? -5.4)", env));
    assertEquals(FALSE, eval("(integer? 3.14)", env));
    assertEquals(FALSE, eval("(integer? .123)", env));
    try {
      eval("(integer? \"test\")", env);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Wrong argument type. Expected: Number, actual: \"test\"", e.getMessage());
    }
  }

  @Test
  public void testNumberToString() {
    assertEquals("5",  eval("(number->string 5)", env));
    assertEquals("5.5",  eval("(number->string 5.5)", env));
    assertEquals("9999999999999999999999999999999",  eval("(number->string #d9999999999999999999999999999999)", env));
    assertEquals("1.000000E+31",  eval("(number->string 9999999999999999999999999999999.5)", env));

    assertEquals("5", eval("(number->string #b101)", env));
    assertEquals("309461373397964671249896789", eval("(number->string #b1111111111111010111111101010101010111011010101101010101010101110110101010101010101010101)", env));

    assertEquals("449", eval("(number->string #o701)", env));
    assertEquals("29889", eval("(number->string #o72301)", env));
    assertEquals("1237940039285380274899121345", eval("(number->string #o777777777777777777777777772301)", env));

    assertEquals("0", eval("(number->string #x0)", env));
    assertEquals("15", eval("(number->string #xf)", env));
    assertEquals("255", eval("(number->string #xff)", env));
    assertEquals("324518553658426726783156020576255", eval("(number->string #xfffffffffffffffffffffffffff)", env));

    assertEquals("777777777777777777777777777777777777", eval("(number->string #xfffffffffffffffffffffffffff 8)", env));
    assertEquals("11111111", eval("(number->string #xff 2)", env));
    assertEquals("1111111111111010111111101010101010111011010101101010101010101110110101010101010101010101", eval("(number->string #b1111111111111010111111101010101010111011010101101010101010101110110101010101010101010101 2)", env));
    assertEquals("3a885", eval("(number->string 239749 16)", env));

    assertEquals("-3a885", eval("(number->string -239749 16)", env));
    assertEquals("-777777777777777777777777777777777777", eval("(number->string #x-fffffffffffffffffffffffffff 8)", env));
  }
}