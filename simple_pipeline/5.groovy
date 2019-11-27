#!groovy

pipeline {
  agent { label "duws-3"}
  stages{
    stage("foo"){
      steps{
        dir("/root/workspace/tools"){
          echo "hi"
        }
        tools{
          maven "apache-maven-3.6.3"
        }
      }
    }
  }
}
