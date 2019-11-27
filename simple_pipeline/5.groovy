#!groovy

pipeline {
  tools{
    maven "apache-maven-3.6.3"
    jdk "default"
  }
  agent { label "duws-3"}
  stages{
    stage("foo"){
      steps{
        dir("tmp"){
          git changelog: false, poll: false, url: 'https://github.com/duws93/git_home.git', branch: 'master'
          sh ' echo "M2_HOME: ${M2_HOME}"'
          sh 'echo "JAVA_HOME: ${JAVA_HOME}"'
          sh 'mvn clean verify -Dmaven.test.failure.ignore=true'
        }
      }
    }
  }
}
