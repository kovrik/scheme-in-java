package core.scm;

import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.writer.Writer;

import java.util.Map;

public class MapEntry extends AFn implements Map.Entry {

  private final Object key;
  private final Object val;

  public MapEntry(Object key, Object val) {
    super(new FnArgsBuilder().min(1).max(1).mandatory(new Class[]{Type.ExactNonNegativeInteger.class}).build());
    this.key = key;
    this.val = val;
  }

  @Override
  public Object apply1(Object arg) {
    int index = ((Number)arg).intValue();
    if (index > 2) {
      throw new IndexOutOfBoundsException(getName() + ": value out of range: " + index);
    }
    return index == 0 ? key : val;
  }

  @Override
  public Object getKey() {
    return key;
  }

  @Override
  public Object getValue() {
    return val;
  }

  @Override
  public Object setValue(Object value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getName() {
    return "map entry";
  }

  @Override
  public String toString() {
    return "[" + Writer.write(key) + " " + Writer.write(val) + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MapEntry that = (MapEntry) o;
    if (key != null ? !key.equals(that.key) : that.key != null) return false;
    return val != null ? val.equals(that.val) : that.val == null;
  }

  @Override
  public int hashCode() {
    int result = key != null ? key.hashCode() : 0;
    result = 31 * result + (val != null ? val.hashCode() : 0);
    return result;
  }
}
