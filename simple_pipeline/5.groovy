#!groovy

pipeline {
  agent { label "duws-3"}
  stages{
    stage("foo"){
      tools{
        maven "apache-maven-3.6.3"
      }
      steps{
        echo "test maven"
      }
    }
  }
}
