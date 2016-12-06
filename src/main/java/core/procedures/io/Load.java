package core.procedures.io;

import core.procedures.AFn;
import core.reader.FileReader;
import core.scm.FnArgs;
import core.scm.SCMCons;
import core.scm.specialforms.Begin;
import core.scm.specialforms.TailCall;

import java.io.File;
import java.util.List;

@FnArgs(args = {String.class})
public class Load extends AFn {

  private final FileReader reader = new FileReader();

  @Override
  public String getName() {
    return "load";
  }

  @Override
  public Object invoke(Object... args) {
    File file = new File(args[0].toString());
    // TODO Is BEGIN Ok here?
    List<Object> sexps = SCMCons.list(Begin.BEGIN);
    sexps.addAll(reader.read(file));
    return new TailCall(sexps, null);
  }
}
