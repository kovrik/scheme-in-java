package core.procedures.io

import core.Repl
import core.procedures.AFn
import core.scm.InputPort

class IsCharReady : AFn<Any?, Boolean>(name = "char-ready?", maxArgs = 1, restArgsType = InputPort::class.java) {

    override operator fun invoke(args: Array<out Any?>): Boolean {
        val inputPort = if (args.isEmpty()) Repl.currentInputPort else args[0]!! as InputPort
        return inputPort.available() > 0
    }
}
