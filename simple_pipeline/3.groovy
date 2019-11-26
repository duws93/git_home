#!groovy
pipeline{
  agent{
    label "duws-3"
    dockerfile{
      filename "Dockerfile.alternate"
      args "-v /tmp:/tmp -p 8000:8000"
    }
  }
  
  stages{
    stage("foo"){
      steps{
        sh 'cat /hi-there'
        sh 'echo "the answer is 42.5"'
      }
    }
  }
}
