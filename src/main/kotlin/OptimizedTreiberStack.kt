import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.atomicArrayOfNulls

private const val COLLISION_SIZE = 16

class OptimizedTreiberStack<T>: Stack<T> {
    private val headNode = atomic<Node<T>?>(null)
    private val collisionArray = atomicArrayOfNulls<T>(COLLISION_SIZE)
    private val collisionPosition = atomic(0)

    override fun top(): T? {
        return headNode.value?.value
    }

    private fun tryCollisionPush(value: T) {
        val pos = collisionPosition.getAndIncrement() % COLLISION_SIZE
        val collisionPos = collisionArray[pos]
        if (collisionPos.value == null) {
            if (collisionPos.compareAndSet(null, value)) {
                collisionPos.getAndSet(null) ?: return
            }
        }
        push(value)
    }

    override fun push(value: T) {
        val curHead = headNode.value
        val newHead = Node(curHead, value)
        if (headNode.compareAndSet(curHead, newHead)) {
            return
        }
        tryCollisionPush(value)
    }

    private fun tryCollisionPop(): T? {
        val pos = collisionPosition.getAndIncrement() % COLLISION_SIZE
        val collisionPos = collisionArray[pos]
        if (collisionPos.value != null) {
            val value = collisionPos.value
            if (collisionPos.compareAndSet(value, null)) {
                return value
            }
        }
        return pop()
    }

    override fun pop(): T? {
        val curHead = headNode.value
        val nextNode = curHead?.nextNode
        if (headNode.compareAndSet(curHead, nextNode)) {
            return curHead?.value
        }
        return tryCollisionPop()
    }
}
