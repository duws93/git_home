#!groovy
pipeline{
  
  //这定义了在运行作业或使用默认值之前填充的作业参数
  parameters {
    booleanParam(defaultValue: true, description: '', name: 'flag')
    string(defaultValue: '', description: '', name: 'SOME_STRING')
  }
  
  environment{
  foo = "bar"
  build_num_env = currentBuild.getNumber()
  another_env = "${currentBuild.getNumber()}"
  inherited_env = "\${build_num_env} is inherited"
  }
  
  agent { label "duws-3"}

  //触发器定义如何触发作业。
  //在这里，作业仍然可以手动或由webhook触发。
  triggers {
    cron('@daily')
  }
  
  //选项涵盖应用于整个管道的所有其他作业属性或包装器函数。
  //在stage内部只能使用skipDefaultCheckout,timeout,retry,timestamps
  options {
    //指定build history与console 保存的数量
    buildDiscarder(logRotator(numToKeepStr:'1'))
    //设置job不能够同时运行
    disableConcurrentBuilds()
    //跳过默认的代码check out
    skipDefaultCheckout(true)
    //一旦构建状态变得unstable，跳过该阶段
    skipStagesAfterUnstable()
    //在工作空间的子目录进行check out
    //checkoutToSubdirectory('children_path')
    //设置jenkins运行的超时时间，超过超时时间，job会自动被终止
    timeout(time: 5, unit: 'MINUTES')
    //设置retry作用域范围的重试次数
    retry(3)
    //为控制台输出增加时间戳
    timestamps()
  }
  
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
        writeFile text: 'hello', file: 'msg.out'
        step([$class: 'ArtifactArchiver', artifacts: 'msg.out', fingerprint: true])
        
        sh 'echo $PATH'
      }
    }
    
    stage("foo2"){
      steps{
        echo "start"
        script {
          String res = env.MAKE_RESULT
          echo "res is ${res}"
          if ( res != null ){
            echo "setting build result ${res}"
            currentBuild.result = res
          }else {
            echo " all is well"
          }
        }
      }
      post
      {
        aborted {
          echo "Stage 'foo2' WAS ABORTED"
        }
        always {
          echo "Stage 'foo2' finished"
        }
        changed {
          echo "Stage HAVE CHANGED"
        }
        failure {
          echo "Stage FAILED"
        }
        success {
          echo "Stage was Successful"
        }
        unstable {
          echo "Stage is Unstable"
        }
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
