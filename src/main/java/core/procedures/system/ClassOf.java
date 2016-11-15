package core.procedures.system;

import core.exceptions.ArityException;
import core.procedures.AFn;
import core.scm.ISCMClass;
import core.scm.SCMClass;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassOf extends AFn {

  private static final Map<Class, SCMClass> JAVA_TO_SCM_CLASSES = new HashMap<Class, SCMClass>();
  static {
    JAVA_TO_SCM_CLASSES.put(Integer.class,    SCMClass.INTEGER);
    JAVA_TO_SCM_CLASSES.put(Long.class,       SCMClass.INTEGER);
    JAVA_TO_SCM_CLASSES.put(Double.class,     SCMClass.DOUBLE);
    JAVA_TO_SCM_CLASSES.put(Float.class,      SCMClass.DOUBLE);
    JAVA_TO_SCM_CLASSES.put(String.class,     SCMClass.STRING);
    JAVA_TO_SCM_CLASSES.put(Character.class,  SCMClass.CHARACTER);
    JAVA_TO_SCM_CLASSES.put(Boolean.class,    SCMClass.BOOLEAN);
  }

  @Override
  public boolean isPure() {
    return true;
  }

  @Override
  public String getName() {
    return "class-of";
  }

  @Override
  public Object invoke(Object... args) {
    if (args.length != 1) {
      throw new ArityException(args.length, 1, getName());
    }
    Object object = args[0];
    if (object == null) {
      return SCMClass.NIL;
    }

    /* All custom SCM Classes should implement ISCMClass interface */
    if (object instanceof ISCMClass) {
      return ((ISCMClass)object).getSCMClass();
    }

    /* Must be a Java object */
    if (object instanceof BigDecimal) {
      /* Check if it is integral */
      if (((BigDecimal)object).remainder(BigDecimal.ONE).equals(BigDecimal.ZERO)) {
        return SCMClass.INTEGER;
      }
      return SCMClass.DOUBLE;
    }
    /* Check Pair and Nil */
    if (object instanceof List) {
      if (((List) object).isEmpty()) {
        return SCMClass.NIL;
      }
      return SCMClass.PAIR;
    }
    SCMClass scmClass = JAVA_TO_SCM_CLASSES.get(object.getClass());
    if (scmClass == null) {
      throw new IllegalArgumentException("Unknown class: " + object.getClass());
    }
    return scmClass;
  }
}
