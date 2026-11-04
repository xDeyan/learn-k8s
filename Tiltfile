helm_yaml = helm(
    'app',
    name = 'app',
    set=["pricing-service.image.repository=pricing-service", "product-service.image.repository=product-service"])

k8s_yaml(helm_yaml)

local_resource(
    name = 'urls',
    links = [link('http://localhost:30001/services/pricing-service/swagger-ui/index.html', 'Application gateway')],
    labels = ['infrastructure'],
)

custom_build(
    # The spec.containers.image.name of the deployment.yml
    ref = 'pricing-service:latest',
    command = "task pricing:build-image",
    deps = ['./pricing-service/pom.xml', 'pom.xml', './pricing-service/target/classes'],
    live_update = [
        sync('./pricing-service/target/classes', '/workspace/BOOT-INF/classes')
    ]
)

k8s_resource(workload = "pricing-service", labels = "backend-service")

custom_build(
    # The spec.containers.image.name of the deployment.yml
    ref = 'product-service:latest',
    command = "task product:build-image",
    deps = ['./product-service/pom.xml', 'pom.xml', './product-service/target/classes'],
    live_update = [
        sync('./product-service/target/classes', '/workspace/BOOT-INF/classes')
    ]
)

k8s_resource(workload = "product-service", labels = "backend-service")

#k8s_resource(
#    workload = "product-service",
#    labels = "backend-service",
##    port_forwards=[port_forward(8080, 80)]
#)
