package core.reader;

import core.exceptions.IllegalSyntaxException;

import java.io.IOException;
import java.io.PushbackReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class StringReader extends Reader {

  public Object readFirst(String string) {
    reader = new PushbackReader(new java.io.StringReader(string), 2);
    try {
      Object token = nextToken();
      if (DOT.equals(token)) {
        throw new IllegalSyntaxException("Illegal use of '.'");
      }
      return token;
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    } finally {
      try {
        reader.close();
      } catch (IOException ignore) {
      }
    }
    return null;
  }

  public List<Object> read(String string) {
    reader = new PushbackReader(new java.io.StringReader(string), 2);
    try {
      List<Object> tokens = new ArrayList<>();
      Object token;
      while ((token = nextToken()) != null) {
        /* Read */
        if (DOT.equals(token)) {
          throw new IllegalSyntaxException("Illegal use of '.'");
        }
        tokens.add(token);
      }
      return tokens;
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    } finally {
      try {
        reader.close();
      } catch (IOException ignore) {
      }
    }
    return null;
  }
}
