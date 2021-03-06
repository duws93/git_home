#!groovy
pipeline {
  agent { label "135.251.206.38" }

  environment {
      // This returns 0 or 1 depending on whether build number is even or odd
      FOO = "${currentBuild.getNumber() % 2}"
  }

  stages {
    stage("Hello") {
      steps {
        echo "Hello"
      }
    }
    stage("Evaluate FOO") {
      when {
        // stage won't be skipped as long as FOO == 0, build number is even
        environment name: "FOO", value: "0"
      }
      steps {
        echo "World"
      }
    }
    stage("always skip"){
      when{
        expression {
          echo "should i run?"
          return false
        }
      }
      steps{
        echo "over"
      }
    }
    stage("last stage"){
      steps{
        echo "last stage"
      }
    }
  }
}
