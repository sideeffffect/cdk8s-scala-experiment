package com.mycompany.app

import software.constructs.Construct

import scala.jdk.CollectionConverters._
import org.cdk8s._
import org.cdk8s.plus25.k8s._

object Main {
  def main(args: Array[String]): Unit = {
    val app = new App
    new Main(app, "cdk8s-scala-experiment")
    app.synth()
  }
}

class Main(scope: Construct, id: String, options: Option[ChartProps]) extends Chart(scope, id, options.orNull) {

  def this(scope: Construct, id: String) = this(scope, id, None)

  private val serviceType = "LoadBalancer"
  private val selector = Map("app" -> "cdk8s-scala-experiment-k8s")

  // Defining a LoadBalancer Service
  val service: KubeService = {
    val servicePort = new ServicePort.Builder()
      .port(80)
      .targetPort(IntOrString.fromNumber(8080))
      .build
    val serviceSpec = new ServiceSpec.Builder()
      .`type`(serviceType)
      .selector(selector.asJava)
      .ports(List(servicePort).asJava)
      .build
    val serviceProps = new KubeServiceProps.Builder()
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
    val labelSelector = new LabelSelector.Builder().matchLabels(selector.asJava).build
    val objectMeta = new ObjectMeta.Builder().labels(selector.asJava).build
    val containerPort = new ContainerPort.Builder()
      .containerPort(8080)
      .build
    val container = new Container.Builder()
      .name("cdk8s-scala-experiment-kubernetes")
      .image("paulbouwer/hello-kubernetes:1.7")
      .ports(List(containerPort).asJava)
      .build
    val podSpec = new PodSpec.Builder()
      .containers(List(container).asJava)
      .build
    val podTemplateSpec = new PodTemplateSpec.Builder()
      .metadata(objectMeta)
      .spec(podSpec)
      .build
    val deploymentSpec = new DeploymentSpec.Builder()
      .replicas(1)
      .selector(labelSelector)
      .template(podTemplateSpec)
      .build
    val deploymentProps = new KubeDeploymentProps.Builder()
      .spec(deploymentSpec)
      .build

    new KubeDeployment(this, "deployment", deploymentProps)
  }
}
