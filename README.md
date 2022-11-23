# cdk8s experiment with Scala

Adapted from https://cdk8s.io/docs/latest/getting-started/

The whole thing takes 7 seconds from scratch.
Compiling the project itself takes 3.6 s of that.

```
> mvn compile ; cdk8s synth                                                                                                                                                                                                                                                   7s  St 23. listopadu 2022, 10:36:22
[INFO] Scanning for projects...
[INFO] 
[INFO] ----------------------< com.mycompany.app:my-app >----------------------
[INFO] Building my-app 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ my-app ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /home/ondra/Projects/cdk8s-scala-experiment/src/main/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ my-app ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- scala-maven-plugin:4.7.2:compile (default) @ my-app ---
[INFO] Using incremental compilation using Mixed compile order
[INFO] Compiler bridge file: /home/ondra/.sbt/1.0/zinc/org.scala-sbt/org.scala-sbt-compiler-bridge_2.13-1.7.1-bin_2.13.10__61.0-1.7.1_20220712T022208.jar
[INFO] compiling 1 Scala source to /home/ondra/Projects/cdk8s-scala-experiment/target/classes ...
[INFO] done compiling
[INFO] compile in 2.5 s
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.640 s
[INFO] Finished at: 2022-11-23T10:44:08+01:00
[INFO] ------------------------------------------------------------------------
Synthesizing application
  - dist/cdk8s-scala-experiment.k8s.yaml
```
