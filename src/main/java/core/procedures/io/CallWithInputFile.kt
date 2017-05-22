package core.procedures.io

import core.exceptions.SCMFileNotFoundException
import core.procedures.AFn
import core.procedures.FnArgsBuilder
import core.procedures.IFn
import core.scm.Cons
import core.scm.InputPort
import core.scm.Thunk

import java.io.FileInputStream
import java.io.FileNotFoundException

class CallWithInputFile : AFn(FnArgsBuilder().min(2).max(2).mandatory(arrayOf(CharSequence::class.java, IFn::class.java)).build()) {

    override val name: String
        get() = "call-with-input-file"

    override fun apply(args: Array<Any?>): Any {
        val filename = args[0].toString()
        val inputPort: InputPort
        try {
            inputPort = InputPort(FileInputStream(filename))
        } catch (e: FileNotFoundException) {
            throw SCMFileNotFoundException(filename)
        }
        val proc = args[1] as IFn<*, *>
        val sexp = Cons.list(proc, inputPort)
        return Thunk(sexp)
    }
}
