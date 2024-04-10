import sequentialStack.SequentialIntStack
import treiberStack.TreiberStack
import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis
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

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    @Test
    fun performanceTest() {
        val threads = 8
        val iterations = 1000000
        val stack = TreiberStack<Int>()

        var executionTime = 0L
        repeat(10) {
            val jobs = mutableListOf<Job>()
            val time = measureTimeMillis {
                runBlocking {
                    repeat(threads) { curThread ->
                        jobs.add(launch(newSingleThreadContext(curThread.toString() + "PUSH")) {
                            repeat(iterations) {
                                stack.push(Random.nextInt(1_000_000_000))
                            }
                        })
                        jobs.add(launch(newSingleThreadContext((curThread + threads).toString() + "POP")) {
                            repeat(iterations) {
                                stack.pop()
                            }
                        })
                    }
                    jobs.joinAll()
                }
            }
            executionTime += time
        }
        executionTime /= 10

        println("Execution time of treiberStack.TreiberStack: $executionTime milliseconds")
    }
}
