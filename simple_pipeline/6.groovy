#!groovy
pipeline {
  agent { label "duws-3" }

  stages {
    stage("One") {
      steps {
        echo "Hello"
      }
    }
    stage("Evaluate Master") {
      steps{
        script{
          if(env.GIT_NAME == "dev"){
            echo "deploy to dev"
          }
        }
        sh 'echo $env.GIT_NAME'
      }
    }
  }
}
