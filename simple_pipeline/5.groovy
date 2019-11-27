#!groovy

pipeline {
  agent { label "duws-3"}
  stages{
    stage("foo"){
      tools{
        maven "apache-maven-3.6.3"
        jdk "default"
      }
      steps{
        dir("/root/tools/hudson.tasks.Maven_MavenInstallation/apache-maven-3.6.3/bin"){
          sh './mvn -v'
          sh 'printenv | grep env'
        }
      }
    }
  }
}
