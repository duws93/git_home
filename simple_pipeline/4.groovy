#!groovy
pipeline{
  
  //这定义了在运行作业或使用默认值之前填充的作业参数
  parameters {
    booleanParam(defaultValue: true, description: 'flag 默认为true', name: 'flag')
    string(defaultValue: 'default', description: '默认是default', name: 'SOME_STRING')
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
  //类型cron,pollSCM
  triggers {
    cron('@daily')
  }
  
  //选项涵盖应用于整个管道的所有其他作业属性或包装器函数。
  //在stage内部只能使用skipDefaultCheckout,timeout,retry,timestamps
  options {
    //指定build history与console 保存的数量
    buildDiscarder(logRotator(numToKeepStr:'5'))
    //设置job不能够并行操作
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
        //产生文件flag,可用于后期内容检索判断
        writeFile text: 'hello', file: 'msg.out'
        //step接受类型为map的参数来调用
        step([$class: 'ArtifactArchiver', artifacts: 'msg.out', fingerprint: true])
        //artifacts会产生不可变文件，用于用户检索
        //fingerprint是保存文件指纹记录，用于追踪多项目之间文件使用的关联
        sh 'echo $PATH'
      }
    }
    
    stage("foo2"){
      steps{
        script{
          foo = docker.image("ubuntu")
          env.bar = "${foo.imageName()}"
          echo "foo: ${foo.imageName()}"
        }
      }
    }
    stage("bar"){
      steps{
        echo "bar is : ${env.bar}"
        echo "foo is : ${foo.imageName()}"
      }
    }
    
    stage("foo3"){
      steps{
        echo "start"
        script {
          //除环境变量，变量赋值只能在脚本中完成
          //复杂的全局变量，只能在脚本块运行
          //env变量也可以在脚本中设置
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
    stage("foo4"){
      steps{
        //任何管道步骤和包装器都可以在步骤中使用，也可以嵌套
        timeout(time:5, unit:"SECONDS"){
          retry(5){
            echo "hi"
          }
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
