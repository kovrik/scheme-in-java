package core.scm

import core.procedures.AFn
import kotlin.collections.MutableSet

class MutableHashSet<T>(val set: MutableSet<T?>) : AFn<Any?, Any?>(minArgs = 1, maxArgs = 1),
                                                 MutableSet<T?> by set {

    constructor() : this(mutableSetOf())

    constructor(size: Int) : this(HashSet(size))

    constructor(coll: Collection<T>) : this(HashSet(coll))

//    fun toImmutableSet() = Hashset(this)

    override fun invoke(arg: Any?) = when (set.contains(arg)) {
        true -> arg
        else -> null
    }
}
