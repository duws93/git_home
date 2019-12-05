pipeline {
  agent {label 'duws-3'}
  stages {
    stage('evaluate branch'){
      steps{
        script{
          BRANCHES = sh returnStdout: true, script: 'git branch -r | grep -v HEAD'
        }
      }
      when{
        branch "${BRANCHES}"
      }
      steps{
        echo "you are in branch ${BRANCHES}"
      }
    }
  }
}
