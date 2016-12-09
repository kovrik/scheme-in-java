package core.scm;

import java.util.Arrays;

/* Mutable vector */
public class SCMMutableVector extends SCMVector {

  private final Object[] vector;

  public SCMMutableVector() {
    this.vector = new Object[0];
  }

  public SCMMutableVector(int size) {
    this.vector = new Object[size];
  }

  public SCMMutableVector(int size, Object init) {
    this.vector = new Object[size];
    Arrays.fill(vector, init);
  }

  public SCMMutableVector(Object... elements) {
    this.vector = elements;
  }

  public SCMMutableVector(Object e) {
    this.vector = new Object[] {e};
  }

  @Override
  public Object get(int index) {
    return vector[index];
  }

  public void set(int index, Object value) {
    vector[index] = value;
  }

  @Override
  public int length() {
    return vector.length;
  }

  @Override
  public Object[] getArray() {
    return vector;
  }

  @Override
  public SCMClass getSCMClass() {
    return SCMClass.MUTABLE_VECTOR;
  }

  @Override
  public String toString() {
    if (vector.length == 0) {
      return "#()";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("#(");
    for (int i = 0; i < vector.length; i++) {
      Object e = vector[i];
      if (e == this) {
        sb.append("(this Vector)");
      } else if (e instanceof String || e instanceof SCMMutableString) {
        sb.append('"').append(e).append('"');
      } else {
        sb.append(e);
      }
      if (i == (vector.length - 1)) {
        return sb.append(')').toString();
      }
      sb.append(' ');
    }
    return sb.toString();
  }

  public Object first() {
    if (vector.length == 0) {
      throw new IllegalArgumentException("Value out of range");
    }
    return vector[0];
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof SCMMutableVector && Arrays.equals(vector, ((SCMMutableVector) obj).vector);
  }
}
