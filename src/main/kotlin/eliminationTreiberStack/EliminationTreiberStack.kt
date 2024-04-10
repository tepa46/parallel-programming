package eliminationTreiberStack

import Node
import Stack
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.loop

const val ELIMINATION_ARRAY_SIZE = 4

class EliminationTreiberStack<T> : Stack<T> {
    private var headNode = atomic<Node<T>?>(null)

    private val eliminationArray =
        EliminationArray<T>(ELIMINATION_ARRAY_SIZE)

    override fun top(): T? {
        return headNode.value?.value
    }

    override fun push(value: T): Unit =
        headNode.loop { curHead ->
            val newHead = Node(curHead, value)
            if (headNode.compareAndSet(curHead, newHead)) {
                return
            }

            val visitResult = eliminationArray.visit(value)
            if (visitResult.isSuccess && visitResult.getOrNull() == null) {
                return
            }
        }

    override fun pop(): T? = headNode.loop { curHead ->
        val nextNode = curHead?.nextNode
        if (headNode.compareAndSet(curHead, nextNode)) {
            return curHead?.value
        }

        val visitResult = eliminationArray.visit(null)
        if (visitResult.isSuccess) {
            visitResult.getOrNull()?.let { return it }
        }
    }
}
