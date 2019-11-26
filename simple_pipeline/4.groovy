#!groovy
pipeline{
  environment{
    foo = "bar"
    build_num_env = currentBuild.getNumber()
    another_env = "${currentBuild.getNumber()}"
    inherited_env = "\${build_num_env} is inherited"
}
  agent { label "duws-3"}
  
  stages{
    stage("environment"){
      steps{
        sh 'echo "foo is $foo"'
        sh 'echo "build_num_env is $build_num_env"'
        sh 'echo "another_env is $another_env"'
        sh 'echo "inherited_env is $inherited_env"'
      }
    }
    
    stage("foo"){
      steps{
        writeFile text: 'hello', file: 'msg out'
        step([$class: 'ArtifactArchiver', artifacts: 'msg.out', fingerprint: true])
        
        sh 'echo $PATH'
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
