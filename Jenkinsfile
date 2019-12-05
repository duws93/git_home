pipeline {
  agent {label 'duws-3'}
  stages {
    stage('evaluate master') {
      when{
        branch "master"
      }
      steps{
        echo "you are in branch master"
        echo "you can deal job in master"
      }
    }
  }
}
