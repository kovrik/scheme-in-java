package core.procedures.functional;

import core.exceptions.ArityException;
import core.procedures.AFn;
import core.procedures.cons.Append;
import core.scm.SCMCons;
import core.scm.specialforms.Quote;
import core.scm.specialforms.TailCall;

import java.util.Arrays;
import java.util.List;

public class Apply extends AFn {

  @Override
  public String getName() {
    return "apply";
  }

  @Override
  public Object invoke(Object... args) {

    // TODO Implement directly, not via tail call + optimize
    if (args.length < 2) {
      throw new ArityException(args.length, "apply");
    }
    SCMCons sexp = SCMCons.list(args[0]);
    if (args.length > 2) {
      SCMCons<Object> list = SCMCons.list();
      list.addAll(Arrays.asList(args).subList(1, args.length - 1));
      sexp = (SCMCons) Append.append(sexp, list);
    }

    Object last = args[args.length - 1];
    if (!(last instanceof List)) {
      throw new IllegalArgumentException(String.format("Error: (%s) bad argument type - not a List: %s", getName(), last));
    }
    for (Object o : (List) last) {
      sexp.add((Object)SCMCons.list(Quote.QUOTE, o));
    }
    return new TailCall(sexp, null);
  }
}