pipeline {
  agent {label '135.251.206.38'}
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

