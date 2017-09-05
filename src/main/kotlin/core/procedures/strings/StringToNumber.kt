package core.procedures.strings

import core.exceptions.IllegalSyntaxException
import core.procedures.AFn
import core.reader.Reader
import core.utils.Utils

class StringToNumber : AFn<Any?, Any?>(name = "string->number", isPure = true, minArgs = 1, maxArgs = 2,
                           mandatoryArgsTypes = arrayOf<Class<*>>(CharSequence::class.java), restArgsType = Long::class.java) {

    override operator fun invoke(args: Array<out Any?>): Any? {
        val number = args[0].toString()
        /* Check if we should override optional radix */
        /* Read radix and/or exactness and a number */
        var override = false
        var radixChar: Char? = null
        var exactness: Char? = null
        var restNumber = number
        while (restNumber.length > 1 && restNumber[0] == '#') {
            val ch = restNumber[1]
            if (Reader.isExactness(ch)) {
                exactness?.let { return false }
                exactness = ch
            }
            if (Reader.isRadix(ch)) {
                radixChar?.let { return false }
                radixChar = ch
                override = true
            }
            restNumber = restNumber.substring(2)
            continue
        }
        if (restNumber.isEmpty()) {
            return false
        }
        var radix = Utils.getRadixByChar(radixChar)
        /* Get default (optional) radix if present */
        if (args.size == 2) {
            val optRadix = (args[1] as Number).toInt()
            if (optRadix < 2 || optRadix > 16) {
                throw IllegalArgumentException(name + ": expected radix from 2 to 16")
            }
            if (!override) {
                radix = optRadix
            }
        }
        /* Read number */
        return try {
            Utils.preProcessNumber(restNumber, exactness, radix) as? Number ?: false
        } catch (e: IllegalSyntaxException) {
            false
        }
    }
}