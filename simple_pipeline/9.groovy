pipeline{
  agent{
    node{
      label "duws-5"
      customWorkspace '/root/workspace/pipeline'
    }
  }
  stages{
    stage('build'){
      input{
        message "press ok to continue"
        submitter "admin,wenshud"
        parameters{
          string(name:'username',defaultValue: 'user',description:'username of the user pressing ok')
        }
      }
      steps{
        echo "user: ${username} said ok"
      }
    }
    stage('run test'){
      parallel{
        stage('test on duws-2'){
          agent { 
            node{
              label "duws-2"
              customWorkspace '/root/workspace/pipeline'
            }
          }
          steps{
            echo "work on duws-2"
          }
        }
        stage('test on duws-1'){
          agent {
            node{
              label "duws-1"
              customWorkspace '/root/workspace/pipeline'
            }
          }
          steps{
            echo "work on duws-1"
          }
        }
      }
    }
  }
}
  
