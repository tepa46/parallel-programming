class SequentialIntStack: Stack<Int> {
    private val q = mutableListOf<Int>()

    override fun top(): Int? = q.lastOrNull()

    override fun push(value: Int) {
        q.add(value)
    }

    override fun pop(): Int? = q.removeLastOrNull()
}
