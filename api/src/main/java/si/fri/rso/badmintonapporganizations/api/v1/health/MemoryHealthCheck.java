package si.fri.rso.badmintonapporganizations.api.v1.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@Liveness
@ApplicationScoped
public class MemoryHealthCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        long memory_used = memBean.getHeapMemoryUsage().getUsed();
        long mem_max = memBean.getHeapMemoryUsage().getMax();

        HealthCheckResponse response = HealthCheckResponse.named("heap-memory-check")
                .withData("used", memory_used)
                .withData("max", mem_max)
                .state(memory_used < mem_max * 0.95)
                .build();
        return response;
    }
}
