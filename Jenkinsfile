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
                       timeout: 5, url: 'http://135.251.206.39:32143/job/multi-pipeline', validResponseCodes: '200', validResponseContent: 'ok'
                      }
            }
          }
        }
      }
    }
  }
}
