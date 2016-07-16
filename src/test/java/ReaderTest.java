import core.reader.IReader;
import core.reader.Reader;
import core.scm.SCMBoolean;
import core.scm.SCMCons;
import core.scm.SCMSymbol;
import core.scm.SCMVector;
import core.scm.specialforms.SCMSpecialForm;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class ReaderTest {

  private final IReader reader = new Reader();

  @Test
  public void testReadNumbers() {
    assertEquals(1L, reader.read("1"));
    assertEquals(12345L, reader.read("12345"));
    assertEquals(-12345L, reader.read("-12345"));
    assertEquals(0L, reader.read("0"));

    assertEquals(0.5d, reader.read("0.5"));
    assertEquals(-0.5d, reader.read("-0.5"));
    assertEquals(0.0d, reader.read("0.0"));
    assertEquals(1235.0d, reader.read("1235.0"));
    assertEquals(1235.0d, reader.read("1235."));
    assertEquals(-1235.0d, reader.read("-1235."));
    assertEquals(.5d, reader.read(".5"));
    assertEquals(-.5d, reader.read("-.5"));

    assertEquals(-.5d, reader.read("#e#d-.5"));
    assertEquals(+4.5d, reader.read("#i#d+4.5"));
    assertEquals(4999999.5d, reader.read("#i#d+4999999.5"));
    assertEquals(5L, reader.read("#e#b101"));
    assertEquals(5L, reader.read("#b#e101"));
    assertEquals(1L, reader.read("#b#e0001"));
    assertEquals(455L, reader.read("#o0707"));
    assertEquals(585L, reader.read("#o1111"));
    assertEquals(new BigDecimal("324518553658426726783156020576255"), reader.read("#xfffffffffffffffffffffffffff"));
    try {
      reader.read("#o9999");
    } catch (IllegalArgumentException e) {
      assertEquals("Bad number!", e.getMessage());
    }
    try {
      reader.read("#df999");
    } catch (IllegalArgumentException e) {
      assertEquals("Bad number!", e.getMessage());
    }
    try {
      reader.read("#xz999");
    } catch (IllegalArgumentException e) {
      assertEquals("Bad number!", e.getMessage());
    }
    try {
      reader.read("#b2222");
    } catch (IllegalArgumentException e) {
      assertEquals("Bad number!", e.getMessage());
    }
    try {
      reader.read("#d+5+5");
    } catch (IllegalArgumentException e) {
      assertEquals("Bad number!", e.getMessage());
    }
    try {
      reader.read("+5+5");
    } catch (IllegalArgumentException e) {
      assertEquals("Bad number!", e.getMessage());
    }
    assertEquals(255.99609375, reader.read("#d255.99609375"));
    assertEquals(255.99609375, reader.read("#xff.ff"));
    assertEquals(171.67111108726925, reader.read("#xab.abcdefabcdef"));
    assertEquals(3.3125, reader.read("#b11.0101"));
    assertEquals(2730.661460876465, reader.read("#b101010101010.10101001010101011"));
    assertEquals(83.97128295898438, reader.read("#o123.76123"));
    assertEquals(2054353.1632647514, reader.read("#o7654321.1234567"));
  }

  @Test
  public void testReadStrings() {
    assertEquals("1", reader.read("\"1\""));
    assertEquals("Lorem ipsum", reader.read("\"Lorem ipsum\""));
    assertEquals("Lorem \\\"ipsum\\\" ", reader.read("\"Lorem \\\"ipsum\\\" \""));
    assertEquals("", reader.read("\"\""));
  }

  @Test
  public void testReadVector() {
    assertEquals(new SCMVector(), reader.read("#()"));
    assertEquals(new SCMVector(0L), reader.read("#(0)"));
    assertEquals(new SCMVector(1L, 2L, 3L), reader.read("#(1 2 3)"));
    assertEquals(new SCMVector(1L, "test", 3L), reader.read("#(1 \"test\" 3)"));
    assertEquals(new SCMVector(1L, new SCMVector(2L), 3L), reader.read("#(1 #(2) 3)"));
  }

  @Test
  public void testReadList() {
    assertEquals(SCMCons.list(), reader.read("()"));
    assertEquals(SCMCons.list(0L), reader.read("(0)"));
    assertEquals(SCMCons.list(1L, 2L, 3L), reader.read("(1 2 3)"));
    assertEquals(SCMCons.list(1L, "test", 3L), reader.read("(1 \"test\" 3)"));
    assertEquals(SCMCons.list(1L, new SCMVector(2L), 3L), reader.read("(1 #(2) 3)"));
    assertEquals(SCMCons.list(1L, SCMCons.list(2L), 3L), reader.read("(1 (2) 3)"));
  }

  @Test
  public void testReadWhitespace() {
    assertEquals(null, reader.read(""));
    assertEquals(null, reader.read("\t"));
    assertEquals(null, reader.read("\n\r"));
  }

  @Test
  public void testReadQuote() {
    assertEquals(SCMCons.list(SCMSpecialForm.QUOTE, 1L), reader.read("'1"));
    assertEquals(SCMCons.list(SCMSpecialForm.QUOTE, SCMCons.list(1L, "test")), reader.read("'(1 \"test\")"));
    assertEquals(SCMCons.list(SCMSpecialForm.QUOTE, SCMCons.list(SCMSpecialForm.QUOTE, 1L)), reader.read("''1"));
  }

  @Test
  public void testReadQuasiquote() {
    assertEquals(SCMCons.list(SCMSpecialForm.QUASIQUOTE, 1L), reader.read("`1"));
    assertEquals(SCMCons.list(SCMSpecialForm.QUASIQUOTE, SCMCons.list(1L, "test")), reader.read("`(1 \"test\")"));
    assertEquals(SCMCons.list(SCMSpecialForm.QUASIQUOTE, SCMCons.list(SCMSpecialForm.QUOTE, 1L)), reader.read("`'1"));
  }

  @Test
  public void testReadComment() {
    assertEquals(null, reader.read(";test"));
    assertEquals(null, reader.read(";test\ttest"));
    assertEquals(null, reader.read(";test\t\ntest"));
    assertEquals(reader.read("1"), reader.read("1; wefewffewfwfwe \t \t few fwe f wf wfw ;w effw efw e "));
    assertEquals(reader.read("1"), reader.read("1 ; test"));
    assertEquals(reader.read("'(1 \"a\" 5)"), reader.read("'(1 \"a\" 5) ; test"));
  }

  @Test
  public void testReadCharacter() {
    assertEquals('A', reader.read("#\\A"));
    assertEquals('z', reader.read("#\\z"));
    assertEquals('5', reader.read("#\\5"));
    assertEquals(' ', reader.read("#\\space"));
    assertEquals('\n', reader.read("#\\newline"));
    assertEquals('\r', reader.read("#\\13"));
    assertEquals('\u000b', reader.read("#\\11"));
    assertEquals('0', reader.read("#\\0"));
    assertEquals('8', reader.read("#\\8"));
    assertEquals('3', reader.read("#\\3"));
    assertEquals('\r', reader.read("#\\13"));
    assertEquals('\u0007', reader.read("#\\alarm"));
    assertEquals('\u0000', reader.read("#\\nul"));
    assertEquals('\u0000', reader.read("#\\null"));
    assertEquals(Character.MIN_VALUE, reader.read("#\\null"));
    assertEquals(Character.MIN_VALUE, reader.read("#\\x00000"));
    assertEquals(Character.MIN_VALUE, reader.read("#\\x0"));
    assertEquals('\r', reader.read("#\\xd"));
    assertEquals('\u000b', reader.read("#\\xb"));
    assertEquals('\u000b', reader.read("#\\x000b"));
    assertEquals('\u000b', reader.read("#\\x000000b"));
  }

  @Test
  public void testReadBoolean() {
    assertEquals(SCMBoolean.TRUE,  reader.read("#t"));
    assertEquals(SCMBoolean.FALSE, reader.read("#f"));
  }

  @Test
  public void testReadIdentifier() {
    assertEquals(new SCMSymbol("test"), reader.read("test"));
    assertEquals(SCMSpecialForm.LAMBDA, reader.read("lambda"));
    assertEquals(new SCMSymbol("list->vector"), reader.read("list->vector"));
    assertEquals(new SCMSymbol("+"), reader.read("+"));
    assertEquals(new SCMSymbol("<=?"), reader.read("<=?"));
    assertEquals(new SCMSymbol("the-word-recursion-has-many-meanings"), reader.read("the-word-recursion-has-many-meanings"));
    assertEquals(new SCMSymbol("a34kTMNs"), reader.read("a34kTMNs"));
    assertEquals(new SCMSymbol("V17a"), reader.read("V17a"));
    assertEquals(new SCMSymbol("soup"), reader.read("soup"));
    assertEquals(new SCMSymbol("a"), reader.read("a"));
    assertEquals(new SCMSymbol("ab"), reader.read("ab"));
  }
}
