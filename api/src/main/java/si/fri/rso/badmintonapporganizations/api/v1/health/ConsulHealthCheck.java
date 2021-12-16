package si.fri.rso.badmintonapporganizations.api.v1.health;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;
import si.fri.rso.badmintonapporganizations.services.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Liveness
@ApplicationScoped
public class ConsulHealthCheck implements HealthCheck {
    @Inject
    private RestProperties restProperties;

    private static final Logger LOG = Logger.getLogger(ConsulHealthCheck.class.getName());

    private static final String HEALTHY = "{\"health\":\"true\"}";

    @Override
    public HealthCheckResponse call() {

        HealthCheckResponseBuilder healthCheckResponseBuilder = HealthCheckResponse.named(ConsulHealthCheck.class.getSimpleName()).up();

        String connectionUrl = ConfigurationUtil.getInstance().get("kumuluzee.config.consul.agent").orElse("http://127.0.0.1:8500");

        checkConsulStatus(connectionUrl,healthCheckResponseBuilder);

        return healthCheckResponseBuilder.build();
    }

    private void checkConsulStatus(String connectionUrl, HealthCheckResponseBuilder healthCheckResponseBuilder) {
        WebTarget webTarget = ClientBuilder.newClient().target(connectionUrl);
        Response response = null;

        try {
            response = webTarget.request().get();

            if (response.getStatus() == 200) {

                healthCheckResponseBuilder.withData(connectionUrl, HealthCheckResponse.State.UP.toString());
                return;

            }
        } catch (Exception exception) {
            LOG.log(Level.SEVERE, "An exception occurred when trying to get consul status.", exception);
        } finally {
            if (response != null) {
                response.close();
            }
        }

        healthCheckResponseBuilder.withData(connectionUrl, HealthCheckResponse.State.DOWN.toString());
        healthCheckResponseBuilder.down();
    }
}