pipeline {
  agent {label 'duws-3'}
  stages{
    stage('evaluate branch'){
      when{
        branch "dev"
      }
      steps{
        echo "work in dev"
      }
    }
  }
}
