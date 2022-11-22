package com.mycompany.app

import software.constructs.Construct

import scala.jdk.CollectionConverters._
import org.cdk8s.App
import org.cdk8s.Chart
import org.cdk8s.ChartProps
import imports.k8s.IntOrString
import imports.k8s.LabelSelector
import imports.k8s.ObjectMeta
import imports.k8s.PodTemplateSpec
import imports.k8s.KubeService
import imports.k8s.KubeServiceProps
import imports.k8s.ServicePort
import imports.k8s.ServiceSpec
import imports.k8s.DeploymentSpec
import imports.k8s.PodSpec
import imports.k8s.Container
import imports.k8s.ContainerPort
import imports.k8s.KubeDeployment
import imports.k8s.KubeDeploymentProps

object Main {
  def main(args: Array[String]): Unit = {
    val app = new App
    new Main(app, "cdk8s-scala-experiment")
    app.synth()
  }
}

class Main(scope: Construct, id: String, options: Option[ChartProps]) extends Chart(scope, id, options.orNull) {

  def this(scope: Construct, id: String) { this(scope, id, None) }

  private val serviceType = "LoadBalancer"
  private val selector: Map[String, String] = Map("app" -> "cdk8s-scala-experiment-k8s")

  // Defining a LoadBalancer Service
  val service: KubeService = {
    val servicePort: ServicePort = new ServicePort.Builder()
      .port(80)
      .targetPort(IntOrString.fromNumber(8080))
      .build
    val servicePorts: List[ServicePort] = List(servicePort)
    val serviceSpec: ServiceSpec = new ServiceSpec.Builder()
      .`type`(serviceType)
      .selector(selector.asJava)
      .ports(servicePorts.asJava)
      .build
    val serviceProps: KubeServiceProps = new KubeServiceProps.Builder()
      .spec(serviceSpec)
      .build

    // notice that there is no assignment necessary when creating resources.
    // simply instantiating the resource is enough because it adds it to the construct tree via
    // the first argument, which is always the parent construct.
    // its a little confusing at first glance, but this is an inherent aspect of the constructs
    // programming model, and you will encounter it many times.
    // you can still perform an assignment of course, if you need to access
    // attributes of the resource in other parts of the code.

    new KubeService(this, "service", serviceProps)
  }

  // Defining a Deployment
  val deployment: KubeDeployment = {
    val labelSelector: LabelSelector = new LabelSelector.Builder().matchLabels(selector.asJava).build
    val objectMeta: ObjectMeta = new ObjectMeta.Builder().labels(selector.asJava).build
    val containerPort: ContainerPort = new ContainerPort.Builder()
      .containerPort(8080)
      .build
    val containerPorts: List[ContainerPort] = List(containerPort)
    val container: Container = new Container.Builder()
      .name("cdk8s-scala-experiment-kubernetes")
      .image("paulbouwer/hello-kubernetes:1.7")
      .ports(containerPorts.asJava)
      .build
    val containers: List[Container] = List(container)
    val podSpec: PodSpec = new PodSpec.Builder()
      .containers(containers.asJava)
      .build
    val podTemplateSpec: PodTemplateSpec = new PodTemplateSpec.Builder()
      .metadata(objectMeta)
      .spec(podSpec)
      .build
    val deploymentSpec: DeploymentSpec = new DeploymentSpec.Builder()
      .replicas(1)
      .selector(labelSelector)
      .template(podTemplateSpec)
      .build
    val deploymentProps: KubeDeploymentProps = new KubeDeploymentProps.Builder()
      .spec(deploymentSpec)
      .build

    new KubeDeployment(this, "deployment", deploymentProps)
  }
}
