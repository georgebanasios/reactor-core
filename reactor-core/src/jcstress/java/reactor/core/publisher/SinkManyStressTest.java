package reactor.core.publisher;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

import java.util.Queue;

public class SinkManyStressTest {

    @JCStressTest
    @Outcome(id = {"0"}, expect = Expect.ACCEPTABLE, desc = "Queue was correctly cleared.")
    @Outcome(id = {"1"}, expect = Expect.FORBIDDEN, desc = "Item was leaked into the queue.")
    @State
    public static class SinkManyTakeZeroTest {
        final SinkManyEmitterProcessor<Object> sink =
                new SinkManyEmitterProcessor<>(true, 16);


        @Actor
        public void takeZero() {
            sink.asFlux().take(0).blockLast();
        }

        @Actor
        public void emit() {
            sink.tryEmitNext("Test emit");
        }

        @Arbiter
        public void arbiter(I_Result result) {
            Queue<?> q = sink.queue;
            result.r1 = q == null ? 0 : q.size();
        }

    }
}
