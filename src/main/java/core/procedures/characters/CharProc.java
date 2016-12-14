package core.procedures.characters;

import core.procedures.AFn;
import core.scm.FnArgs;

import java.util.function.Function;

@FnArgs(args = {Character.class})
public class CharProc extends AFn {

  public static final CharProc CHAR_TO_INTEGER = new CharProc("char->integer", ch -> (long)ch);
  public static final CharProc CHAR_UPCASE     = new CharProc("char-upcase", Character::toUpperCase);
  public static final CharProc CHAR_DOWNCASE   = new CharProc("char-downcase", Character::toLowerCase);

  private final String name;
  private final Function<Character, Object> function;

  private CharProc(String name, Function<Character, Object> function) {
    this.name = name;
    this.function = function;
  }

  @Override
  public Object apply(Object... args) {
    return function.apply((Character) args[0]);
  }

  @Override
  public String getName() {
    return name;
  }
}
