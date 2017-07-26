package core

import core.environment.DefaultEnvironment
import core.environment.Environment
import core.evaluator.Evaluator
import core.exceptions.ExInfoException
import core.exceptions.ThrowableWrapper
import core.reader.FileReader
import core.reader.Reader
import core.reader.StringReader
import core.scm.Error
import core.scm.InputPort
import core.scm.OutputPort
import core.scm.Symbol
import core.writer.Writer
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger

object Repl {

    private const val SYM_LIMIT = 25
    private const val WELCOME = "Welcome to Scheme in Kotlin!"
    private const val PROMPT = "> "

    private val symCounter = AtomicInteger(0)
    private val evaluator = Evaluator()
    private val environment = DefaultEnvironment().apply {
        /* Eval lib procedures */
        with (StringReader()) {
            libraryProcedures.forEach { evaluator.eval(this.readOne(it), this@apply) }
        }
    }

    var currentInputPort  = InputPort(BufferedInputStream(System.`in`))
    var currentOutputPort = OutputPort(System.out)

    private val reader = Reader(currentInputPort.inputStream)

    private fun getNextID(): Symbol? {
        val i = symCounter.incrementAndGet()
        if (i == SYM_LIMIT) {
            symCounter.set(0)
        }
        return Symbol.intern("$$i")
    }

    @Throws(IOException::class)
    @JvmStatic fun main(args: Array<String>) {
        when {
            args.isEmpty() -> repl(WELCOME, PROMPT, environment)
            else           -> evaluateFile(args[0], environment)
        }
    }

    /**
     * Read and evaluate a file and then exit
     */
    @Throws(IOException::class)
    private fun evaluateFile(filename: String, env: Environment) {
        FileReader().read(File(filename)).forEach { evaluator.macroexpandAndEvaluate(it, env) }
    }

    /**
     * Run REPL and evaluate user inputs
     */
    @Throws(IOException::class)
    private fun repl(welcomeMessage: String, prompt: String, env: Environment) {
        currentOutputPort.writeln(welcomeMessage)
        while (true) {
            try {
                currentOutputPort.write(prompt)
                /* Read, macroexpand and then Evaluate each S-expression */
                val result = evaluator.macroexpandAndEvaluate(reader.read(), env)
                /* Do not print and do not store void results */
                if (result === Unit) {
                    continue
                }
                /* nil, on the other hand, is a valid result - print it, but not store it */
                if (result == null) {
                    currentOutputPort.writeln(Writer.write(result))
                    continue
                }
                /* Put result into environment */
                val id = getNextID()
                env.put(id, result)
                /* Print */
                currentOutputPort.writeln("$id = ${Writer.write(result)}")
            } catch (e: ThrowableWrapper) {
                /* Unwrap */
                error(e.cause ?: e)
            } catch (e: Throwable) {
                error(e)
            }
        }
    }

    @Throws(IOException::class)
    private fun error(e: Throwable) {
        val errorMessage: String
        when (e) {
            is Error -> errorMessage = "Error: ${e.message}"
            is ExInfoException -> errorMessage = e.toString()
            else -> {
                val sb = StringBuilder(e.javaClass.simpleName)
                e.message?.let { sb.append(": ").append(e.message) }
                filterStackTrace(e.stackTrace)?.let { sb.append(" (").append(it.fileName).append(':')
                                                        .append(it.lineNumber).append(')') }
                errorMessage = sb.toString()
            }
        }
        currentOutputPort.writeln(errorMessage)
    }

    private fun filterStackTrace(stackTraceElements: Array<StackTraceElement>) = stackTraceElements.firstOrNull {
        !it.isNativeMethod && !it.className.startsWith("sun.reflect") && !it.className.startsWith("java.lang.reflect")
    }
}
