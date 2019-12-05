pipeline {
  agent {label 'duws-3'}
  stages {
    stage('evaluate dev') {
      when{
        branch "dev"
      }
      steps{
        echo "you are in branch dev"
        echo "you can deal your job in dev"
      }
    }
  }
}
