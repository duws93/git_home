#!groovy
pipeline{
  environment{
    FOO = credentials("f1d1e486-4a23-43fe-9f45-19e17e556705")
  }
  
  agent { label "duws-3" }
  
  stages{
    stage("foo"){
      steps{
        sh 'echo "foo is $FOO"'
        sh 'echo "foo_user is $FOO_USR"'
        sh 'echo "foo_psw is $FOO_PSW"'
        
        dir("combined"){
          sh 'echo $FOO > foo.txt'
        }
        sh 'echo $FOO_PSW > foo_psw.txt'
        sh 'echo $FOO_USR > foo_user.txt'
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
