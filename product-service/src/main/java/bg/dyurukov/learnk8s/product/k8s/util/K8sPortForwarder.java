package bg.dyurukov.learnk8s.product.k8s.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

@Slf4j
public class K8sPortForwarder implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<Service, Process> processes = new EnumMap<>(Service.class);

        for (Service service : Service.values()) {
            if (!isServicePortforwardingEnabled(environment, service)) {
                continue;
            }

            try {
                Process process = new ProcessBuilder()
                        .command("kubectl", "-n", "dev", "port-forward", "services/" + service.getLabel(), service.defaultPort + ":8080")
                        .start();
                processes.put(service, process);
                log.info("Started port forwarding to " + service.getLabel() + " [localPort:" + service.defaultPort + ", remotePort:" + "8080" +"]");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        MapPropertySource servicesPropertySource = processes.keySet().stream()
                .map(service -> Map.<String, Object>entry(service.getLabel() + ".url", "http://localhost:" + service.defaultPort + "/" + service.getLabel()))
                .collect(collectingAndThen(toMap(Entry::getKey, Entry::getValue), map -> new MapPropertySource("mcp-services", map)));

        environment.getPropertySources().addFirst(servicesPropertySource);

        Runtime.getRuntime().addShutdownHook(new Thread(new PortforwardShutdownRunnable(processes)));
    }

    private static final class PortforwardShutdownRunnable implements Runnable {

        private final Map<Service, Process> processes;

        public PortforwardShutdownRunnable(Map<Service, Process> processes) {
            this.processes = processes;
        }


        @Override
        public void run() {
            for (Entry<Service, Process> process : processes.entrySet()) {
                try {
                    log.info("Stopping port forwarding to " + process.getKey().getLabel());
                    process.getValue().destroy();
                    log.info("Successfully stopped port forwarding to " + process.getKey().getLabel());
                } catch (Exception e) {
                    log.error("Could not stop port forwarding to " + process.getKey().getLabel());
                }
            }
        }
    }

    private static Boolean isServicePortforwardingEnabled(ConfigurableEnvironment environment, Service service) {
        return environment.getProperty("mcp.services." + service.getLabel() + ".port-forward.enabled", boolean.class, false);
    }

    public enum Service {
        DEAL(49001), INTEREST_COVERAGE(49002);

        Service(int defaultPort) {
            this.defaultPort = defaultPort;
        }

        int defaultPort;

        public String getLabel() {
            return name().toLowerCase().replace('_', '-');
        }
    }
}
