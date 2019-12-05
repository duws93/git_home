pipeline {
  agent {label 'duws-3'}
  stages {
    stage('evaluate branch'){
      steps{
        script{
          BRANCHES = sh returnStdout: true, script: 'git branch -r | grep -v HEAD'
        }
        when{
          branch '$BRANCHES'
        }
        step{
          echo "you are in branch $BRANCHES"
        }
      }
    }
  }
}
