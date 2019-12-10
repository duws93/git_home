pipeline {
  agent {label 'duws-3'}
  options{
    durabilityHint 'PERFORMANCE_OPTIMIZED'
    timeout(time:5,unit:'MINUTES')
    timestamps()
    skipStagesAfterUnstable() 
    skipDefaultCheckout true
    buildDiscarder logRotator(artifactDaysToKeepStr: '1',artifactNumToKeepStr:'1', daysToKeepStr:'10', numToKeepStr:'5')
  }
  stages{
    stage('拉取代码'){
      steps{
        echo '正在拉取代码....'
        script{
          try{
            //下载git仓库代码,支持cherry pick 某个patch
            checkout([$class:'GitSCM',branches:[[name:'*/master']],doGenerateSubmoduleConfigurations: false, \
                      extensions: [[$class: 'CloneOption', noTags: true, shallow: true, depth: 1, honorRefspec:true]], \
                      submoduleCfg: [], userRemoteConfigs: [[credentialsId: '5f534b22-b547-4c16-ae2a-e166fb0c6e33',\
                                                             refspec: '+refs/heads/master:refs/remotes/origin/master', \
                                                             url: 'https://github.com/duws93/git_home.git']]])
          }catch(Exception err){
            echo err.getMessage()
            echo err.toString()
            unstable '拉取代码失败'
            warnError('拉取代码失败信息回调失败'){
              retry(5){httpRequest acceptType: 'APPLICATION_JSON', consoleLogResponseBody: true, contentType: 'APPLICATION_JSON', \
                       httpMode: 'POST', ignoreSslErrors: true, requestBody: "{\"step\":\"pull\",\"id\":\"${JOB_NAME}\",\"build_number\":\"${BUILD_NUMBER}\"}", \
                       timeout: 5, url: 'http://135.251.206.39:80/jenkins/job-finish', validResponseCodes: '200'//, validResponseContent: 'ok'
                      }
            }
          }
        }
      }
    }
    def BUILD_VERSION = version()
    if(BUILD_VERSION){
      echo "building version ${BUILD_VERSION}"
    }  
    def GIT_REVISION = GIT_Revision()
    if(GIT_REVISION){
      echo "git_revision: ${GIT_REVISION}"
    }
    stage('构建'){
      options{
        timeout(time:3,unit:'MINUTES')
      }
      steps{
        echo '正在构建'
        script{
          try{
            sh 'cat Dockerfile > tmp.txt'
            sh 'tar -zcvf website.tar.gz website'
            sh 'scp website.tar.gz root@135.251.206.39:/var/www/html/'
            sh 'rm -rf website.tar.gz'
          }catch (Exception err) {
            echo err.getMessage()
            echo err.toString()
            unstable '依赖性检查失败'
            warnError('依赖性检查失败信息回调失败') {
              retry(5) {
                httpRequest acceptType: 'APPLICATION_JSON', consoleLogResponseBody: true, contentType: 'APPLICATION_JSON', \
                httpMode: 'POST', ignoreSslErrors: true, requestBody: "{\"step\":\"check\",\"id\":\"${JOB_NAME}\",\"build_number\":\"${BUILD_NUMBER}\"}", \
                timeout: 5, url: 'http://135.251.206.39:80/jenkins/job-finish', validResponseCodes: '200'//,validResponseContent: 'ok'
              }
            }
          }
        }
      }
    }
    stage('返回依赖性文件校验'){
      steps{
        echo '正在校验返回依赖性文件'
        script{
          try{
            retry(5){
              httpRequest acceptType: 'APPLICATION_JSON', consoleLogResponseBody: true, contentType: 'APPLICATION_OCTETSTREAM', \
              customHeaders: [[maskValue: false, name: 'Content-Disposition', value: 'id=tmp.txt']], \
              httpMode: 'POST', ignoreSslErrors: true, multipartName: 'file', requestBody: "{\"id\":\"${JOB_NAME}\"}",\
              timeout: 5, uploadFile: 'tmp.txt', url: 'http://135.251.206.39:80/jenkins/job-finish'\
              ,validResponseCodes: '200'//, validResponseContent: 'ok'
            } 
          }catch (Exception err) {
            echo err.getMessage()
            echo err.toString()
            unstable '依赖性检查文件返回给erebus失败'
            warnError('依赖性检查文件返回给erebus失败信息回调失败') {
              retry(5) {
                httpRequest acceptType: 'APPLICATION_JSON', consoleLogResponseBody: true, contentType: 'APPLICATION_JSON',\
                httpMode: 'POST', ignoreSslErrors: true, requestBody: "{\"step\":\"callback\",\"id\":\"${JOB_NAME}\",\"build_number\":\"${BUILD_NUMBER}\"}", \
                timeout: 5, url: 'http://135.251.206.39:80/jenkins/job-finish', validResponseCodes: '200'//, validResponseContent: 'ok'
              }
            }
          }
        }
      }
    }
    stage('完成'){
      steps{
        echo '执行完成，正在返回完成信息....'
        retry(5){
          httpRequest contentType: 'APPLICATION_OCTETSTREAM', customHeaders: [[maskValue: false, name: 'Content-type', value: 'application/json'], [maskValue: false, name: 'Accept', value: 'application/json']], \
          httpMode: 'POST', ignoreSslErrors: true, requestBody: "{\"id\":\"${JOB_NAME}\",\"build_number\":\"${BUILD_NUMBER}\"}", \
          responseHandle: 'NONE', timeout: 5, url: 'http://135.251.206.39:80/jenkins/job-finish',validResponseCodes: '200'//,validResponseContent: 'ok'
        }
      }
    }
  }
  post{
    success{
      emailext(
        subject: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
        body: """<p>SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}][${env.BUILD_VERSION}][${env.GIT_REVISION}]':</p>
<p>Check console output at "<a href="${env.BUILD_URL}">${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>"</p>""",
        to: "wenshu.du.ext@nokia-sbell.com",
        from: "wenshu.du.ext@nokia-sbell.com"
      )
    }
    failure{
      emailext (
        subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
        body: """<p>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}][${env.BUILD_VERSION}][${env.GIT_REVISION}]':</p>
<p>Check console output at "<a href="${env.BUILD_URL}">${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>"</p>""",
        to: "wenshu.du.ext@nokia-sbell.com",
        from: "wenshu.du.ext@nokia-sbell.com"
      )
    }
    aborted{
      emailext (
        subject: "ABORTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
        body: """<p>ABORTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}][${env.BUILD_VERSION}][${env.GIT_REVISION}]':</p>
<p>Check console output at "<a href="${env.BUILD_URL}">${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>"</p>""",
        to: "wenshu.du.ext@nokia-sbell.com",
        from: "wenshu.du.ext@nokia-sbell.com"
      )
    }
  }
}
