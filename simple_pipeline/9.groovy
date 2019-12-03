#!groovy
pipeline{
  agent{
    label "duws-3"
    customworkspace '/root/workspace/pipeline'
  }
  
  stages{
    stage('build'){
      input{
        message "press ok to continue"
        submitter "admin,wenshud"
        parameters{
          strings(name:'username',defaultValue: 'user',description:'username of the user pressing ok')
        }
      }
      steps{
        echo 'user: ${username} said ok'
      }
    }
  }
}
  
