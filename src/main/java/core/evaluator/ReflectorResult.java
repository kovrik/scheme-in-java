package core.evaluator;

/* Wrapper to avoid downcast */
@Deprecated
public final class ReflectorResult {

  private final Object value;

  private ReflectorResult(Object value) {
    this.value = value;
  }

  public Object get() {
    return value;
  }

  public static Object maybeWrap(Object value) {
    return value instanceof Number ? new ReflectorResult(value) : value;
  }
}

