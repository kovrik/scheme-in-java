package unittests;

import core.exceptions.IllegalSyntaxException;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ReflectorTest extends AbstractTest {

  @Test
  public void testEvalDot() {
    try {
      eval(".", env);
    } catch (IllegalSyntaxException e) {
      // expected
    }
  }

  @Test
  public void testEvalStaticMethods() {
    assertTrue(((List)eval("(java.util.Collections/emptyList)", env)).isEmpty());
    assertTrue(((List)eval("(. java.util.Collections emptyList)", env)).isEmpty());
    assertEquals(-15L, eval("(Long/valueOf -15)", env));
    assertEquals(-15L, eval("(. Long valueOf -15)", env));
    // TODO
  }

  @Test
  public void testEvalStaticFields() {
    assertEquals(BigDecimal.ONE, eval("java.math.BigDecimal/ONE", env));
    assertEquals(BigDecimal.ONE, eval("BigDecimal/ONE", env));
    assertEquals(BigInteger.ONE, eval("BigInteger/ONE", env));
    assertEquals(Math.PI, eval("Math/PI", env));
    assertEquals(Math.PI, eval("(. java.lang.Math PI)", env));
    // TODO
  }

  @Test
  public void testEvalMemberFields() {
    // TODO getting instance fields directly (.-field instance)
    // TODO
  }

  @Test
  public void testEvalMemberMethods() {
    assertEquals(String.class, eval("(.getClass \"\")", env));
    assertEquals(String.class, eval("(. \"\" getClass)", env));
    assertEquals(Long.class, eval("(.getClass 1)", env));
    assertEquals(Long.class, eval("(. 1 getClass)", env));
    assertEquals(Long.class, eval("(. (+ 2 3 4) getClass)", env));
    assertEquals("123", eval("(.toString 123)", env));
    assertEquals(1L, eval("(. (+ 1 2 3) compareTo (+ 1 2))", env));
    try {
      eval("(.getClass nil)", env);
      fail();
    } catch (NullPointerException e) {
      // expected
    }
    try {
      eval("(.toString nil)", env);
      fail();
    } catch (NullPointerException e) {
      // expected
    }
    // TODO
  }

  @Test
  public void testEvalConstructors() {
    assertTrue(eval("(new Object)", env) != null);
    assertTrue(eval("(Object.)", env) != null);
    assertEquals("123", eval("(new String \"123\")", env));
    assertEquals("123", eval("(String. \"123\")", env));
    // TODO
  }

  @Test
  public void testJavaTypes() {
    assertEquals((short)-123, eval("(short -123)", env));
    assertEquals((byte)-123, eval("(byte -123)", env));
    assertEquals((int)-123, eval("(int -123)", env));
    assertEquals((long)-123, eval("(long -123)", env));
    assertEquals((float)-123, eval("(float -123)", env));
    assertEquals((double)-123, eval("(double -123)", env));
    assertEquals((char)98, eval("(char 98)", env));
    assertEquals(new BigInteger("-123"), eval("(bigint -123)", env));
    assertEquals(new BigDecimal("-123.456"), eval("(bigdec -123.456)", env));
    assertTrue((Boolean) eval("(boolean 98)", env));
  }
}
