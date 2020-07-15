pipeline {
  agent {label 'master'}
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

