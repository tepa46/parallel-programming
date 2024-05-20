package treiberStack

import Node
import Stack
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.loop

class TreiberStack<T> : Stack<T> {
    private val headNode = atomic<Node<T>?>(null)

    override fun top(): T? {
        return headNode.value?.value
    }

    override fun push(value: T): Unit =
        headNode.loop { curHead ->
            val newHead = Node(curHead, value)
            if (headNode.compareAndSet(curHead, newHead)) {
                return
            }
        }


    override fun pop(): T? = headNode.loop { curHead ->
        val nextNode = curHead?.nextNode
        if (headNode.compareAndSet(curHead, nextNode)) {
            return curHead?.value
        }
    }
}
