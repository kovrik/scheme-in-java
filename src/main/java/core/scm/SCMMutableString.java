package core.scm;

/**
 * Mutable Scheme String
 *
 * Java String is immutable, StringBuilder is mutable.
 * But both classes are final and cannot be extended.
 * This SCMMutableString class holds a mutable StringBuilder instance
 * and delegates it all string operations.
 */
public class SCMMutableString implements ISCMClass {

  private final StringBuilder string;

  public SCMMutableString() {
    this.string = new StringBuilder();
  }

  public SCMMutableString(String string) {
    this.string = new StringBuilder(string);
  }

  public SCMMutableString(int length) {
    this.string = new StringBuilder(length);
  }

  public SCMMutableString append(Object c) {
    this.string.append(c);
    return this;
  }

  public int length() {
    return this.string.length();
  }

  public void setCharAt(int index, char ch) {
    this.string.setCharAt(index, ch);
  }

  public void setLength(int n) {
    string.setLength(n);
  }

  public void clear() {
    string.setLength(0);
  }

  @Override
  public SCMClass getSCMClass() {
    return SCMClass.MUTABLE_STRING;
  }

  @Override
  public String toString() {
    return string.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || (!(o instanceof String) && getClass() != o.getClass())) {
      return false;
    }
    return string.toString().equals(o.toString());
  }

  @Override
  public int hashCode() {
    return string.toString().hashCode();
  }
}
