pipeline {
  agent {label 'duws-3'}
  stages{
    stage('evaluate branch'){
      when{
        branch "master"
      }
      steps{
        echo "work in master"
      }
    }
  }
}

