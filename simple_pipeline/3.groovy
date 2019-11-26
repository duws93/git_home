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
        sh 'cat simple_pipeline/hi-there'
        sh 'echo "the answer is 42.5"'
      }
    }
    stage("local"){
      environment{
        bar = "stage"
      }
      steps{
        sh 'echo "in local stage,bar is $bar"'
      }
    }
    stage("local2"){
      steps{
        sh 'echo "in local2 stage,bar is $bar"'
    }
  }
  post{
         success{
            emailext(
               subject: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
               body: """<p>SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
<p>Check console output at "<a href="${env.BUILD_URL}">${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>"</p>""",
               to: "wenshu.du.ext@nokia-sbell.com",
               from: "wenshu.du.ext@nokia-sbell.com"
            )
         }
         failure{
            emailext (
                subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """<p>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
<p>Check console output at "<a href="${env.BUILD_URL}">${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>"</p>""",
                to: "wenshu.du.ext@nokia-sbell.com",
                from: "wenshu.du.ext@nokia-sbell.com"
            )
         }
  }
}
