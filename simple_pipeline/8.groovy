pipeline{
  agent{
    node{
      label 'duws-3'
    }
  }
  
  options {
    timestamps()
  }
  
  environment {
    IMAGE = readMavenPom().getArtifactId()
    VERSION = readMavenPom().getVersion()
  }
  
  stages {
    stage('Build'){
      agent{
        docker{
          reuseNode true
          image 'maven'
        }
      }
      steps{
        withMaven(options: [findbugsPublisher(), junitPublisher(ignoreAttachments: false)]) {
          sh 'mvn clean findbugs: findbugs package'
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
          //用于将生成的文件进行归档，配合使用include的pattern捕获匹配的文件
          archiveArtifacts(artifacts: '**/target/*.jar', allowEmptyArchive: true) 
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
    stage('quality analysis'){
      parallel{
        stage('integration test'){
          agent { label "duws-2" }
          steps {
            echo 'run integration test here ...'
          }
        }
        stage('Sonar Scan') {
          agent {
            docker {
              // we can use the same image and workspace as we did previously
              reuseNode true
              image 'maven'
            }
          }
          environment {
            //use 'sonar' credentials scoped only to this stage
            SONAR = credentials('sonar')
          }
          steps {
            sh 'mvn sonar:sonar -Dsonar.login=$SONAR_PSW'
          }
        }
      }
    }
    stage('Build and Publish Image') {
      when {
        branch 'master'  //only run these steps on the master branch
      }
      steps {
        /*
         * Multiline strings can be used for larger scripts. It is also possible to put scripts in your shared library
         * and load them with 'libaryResource'
         */
        sh """
          docker build -t ${IMAGE} .
          docker tag ${IMAGE} ${IMAGE}:${VERSION}
          docker push ${IMAGE}:${VERSION}
        """
      }
    }
  }
}
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
