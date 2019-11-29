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
      when {
        // skip this stage unless on Master branch
        branch "master"
      }
      steps {
        echo "World"
        echo "Heal it"
      }
    }
  }
}
