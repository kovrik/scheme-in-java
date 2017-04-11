package core.procedures.io;

import core.exceptions.SCMFileNotFoundException;
import core.procedures.AFn;
import core.procedures.FnArgsBuilder;
import core.procedures.IFn;
import core.scm.SCMCons;
import core.scm.SCMInputPort;
import core.scm.SCMThunk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public final class CallWithInputFile extends AFn {

  public CallWithInputFile() {
    super(new FnArgsBuilder().minArgs(2).maxArgs(2).mandatoryArgsTypes(new Class[]{CharSequence.class, IFn.class}));
  }

  @Override
  public String getName() {
    return "call-with-input-file";
  }

  @Override
  public Object apply(Object... args) {
    String filename = args[0].toString();
    SCMInputPort inputPort;
    try {
      inputPort = new SCMInputPort(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      throw new SCMFileNotFoundException(filename);
    }
    IFn proc = ((IFn)args[1]);
    SCMCons sexp = SCMCons.list(proc, inputPort);
    return new SCMThunk(sexp);
  }
}
