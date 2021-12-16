package si.fri.rso.badmintonapporganizations.api.v1.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import si.fri.rso.badmintonapporganizations.services.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Liveness
@ApplicationScoped
public class BreakerHealthCheck implements HealthCheck {
    @Inject
    private RestProperties restProperties;

    @Override
    public HealthCheckResponse call() {
        if (restProperties.getBroken()) {
            return HealthCheckResponse.down(BreakerHealthCheck.class.getSimpleName());
        }
        else {
            return HealthCheckResponse.up(BreakerHealthCheck.class.getSimpleName());
        }
    }
}
