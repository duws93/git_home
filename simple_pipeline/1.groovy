#!groovy
pipeline {
  environment {
    //环境变量和凭证检索
    some_var = "some value"
    cred1 = credentials("5f534b22-b547-4c16-ae2a-e166fb0c6e33")
    inbetween = "something in between"
    other_var = "${some_var}"
  }
  
  agent any
  
  stages{
    stage("foo"){
      steps{
        //环境变量未做mask处理
        sh 'echo "some_var is $some_var"'
        sh 'echo "inbetween is $inbetween"'
        sh 'echo "other_var is $other_var"'
        
        //凭证变量在console中将被mask处理，但并不存档在文件中
        sh 'echo $cred1 > cred1.txt'
        archive "**/*.txt"
      }
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
