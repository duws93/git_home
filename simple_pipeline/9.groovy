pipeline{
  agent{
    node{
      label "duws-3"
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
      echo 'user: ${username} said ok'
  }
    }
  }
}
  
