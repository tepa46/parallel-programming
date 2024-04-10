package sequentialStack

import Stack

/*
 * Used as a parameter in the sequentialSpecification() method of LinCheck.
 *
 * SequentialStack<T> implementation for SequentialIntStack is not written due to LinCheck errors.
 */
class SequentialIntStack : Stack<Int> {
    private val q = mutableListOf<Int>()

    override fun top(): Int? = q.lastOrNull()

    override fun push(value: Int) {
        q.add(value)
    }

    override fun pop(): Int? = q.removeLastOrNull()
}
