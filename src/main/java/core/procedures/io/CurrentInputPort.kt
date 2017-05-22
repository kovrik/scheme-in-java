package core.procedures.io

import core.Repl
import core.procedures.AFn
import core.procedures.FnArgsBuilder

class CurrentInputPort : AFn(FnArgsBuilder().max(0).build()) {

    override val name: String
        get() = "current-input-port"

    override fun apply0(): Any {
        return Repl.currentInputPort
    }
}