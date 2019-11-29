#!groovy

pipeline {
  tools{
    maven "apache-maven-3.6.3"
    jdk "default"
  }
  agent { label "duws-3"}
  stages{
    stage("foo"){
      steps{
        dir("tmp"){
          git changelog: false, poll: false, url: 'https://github.com/duws93/git_home.git', branch: 'master'
          sh ' echo "M2_HOME: ${M2_HOME}"'
          sh 'echo "JAVA_HOME: ${JAVA_HOME}"'
          //sh 'mvn clean verify -Dmaven.test.failure.ignore=true'
        }
      }
    }
    
    stage("evaluate master"){
      when{
        not{
          branch "master"
        }
      }
      steps{
        echo "this is master branch"
      }
    }
    
    stage("branch test"){
      when{
        not{
          branch "master"
        }
      }
      steps{
        echo "this is not master branch"
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
    aborted{
      emailext (
        subject: "ABORTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
        body: """<p>ABORTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
<p>Check console output at "<a href="${env.BUILD_URL}">${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>"</p>""",
        to: "wenshu.du.ext@nokia-sbell.com",
        from: "wenshu.du.ext@nokia-sbell.com"
      )
    }
  }
}
