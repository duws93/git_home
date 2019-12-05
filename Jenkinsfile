pipeline {
  agent {label 'duws-3'}
  stages {
    stage('evaluate branch'){
      steps{
        script{
          BRANCHES = sh returnStdout: true, script: 'git branch -r | grep -v HEAD'
          echo "${BRANCHES}"
        }
      }
    }
    stage('judge branch'){
      when{
        branch "${BRANCHES}"
      }
      steps{
        sh 'echo "you are in ${BRANCHES}"'
      }
    }
  }
}
