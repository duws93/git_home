#!groovy
pipeline{
  agent{
    dockerfile{
      label "duws-3"
      filename "simple_pipeline/Dockerfile.alternate"
      args "-v /tmp:/tmp -p 8000:8000"
    }
  }
  
  stages{
    stage("foo"){
      steps{
        sh 'cat hi-there'
        sh 'echo "the answer is 42.5"'
      }
    }
  }
}
