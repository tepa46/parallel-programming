import org.jetbrains.kotlinx.lincheck.LoggingLevel
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.jupiter.api.Test

@Suppress("UNUSED")
class TreiberStackTest {
    private val treiberStack = TreiberStack<Int>()

    @Operation
    fun push(value: Int) = treiberStack.push(value)

    @Operation
    fun pop() = treiberStack.pop()

    @Operation
    fun top() = treiberStack.top()

    @Test
    fun modelTest() =
        ModelCheckingOptions()
            .iterations(50)
            .invocationsPerIteration(30_000)
            .threads(3)
            .actorsPerThread(3)
            .sequentialSpecification(SequentialIntStack::class.java)
            .checkObstructionFreedom()
            .logLevel(LoggingLevel.INFO)
            .check(this::class.java)

    @Test
    fun stressTest() =
        StressOptions()
            .iterations(50)
            .invocationsPerIteration(50_000)
            .threads(3)
            .actorsPerThread(3)
            .sequentialSpecification(SequentialIntStack::class.java)
            .logLevel(LoggingLevel.INFO)
            .check(this::class.java)
}
