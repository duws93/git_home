#!groovy
pipeline{
  agent {
    label "duws-3"
  }
  //trigger定义pipeline被自动触发的方式
  triggers{
    //上层工作流，将一组jobs定义为该pipeline触发的条件，如：只要列表中任一任务执行结束后，即可触发
    upstream(upstreamProjects: "5.groovy,6.groovy", threshold:  hudson.model.Result.SUCCESS)
  }
  stages{
    stage('stash'){
      agent{
        label "duws-3"
      }
      steps{
        writeFile file: "a.txt", text: "$BUILD_NUMBER"
        stash name: "abc", includes: "a.txt"
      }
    }
    
    stage("unstash"){
      agent{
        label "duws-2"
      }
      steps{
        script{
          unstash("abc")
          def content = readFile("a.txt")
          echo "${content}"
        }
      }
    }
    stage('pre deploy'){
      steps{
        script{
          BRANCHES = sh returnStdout: true, script: 'git branch -r | grep -v HEAD > out.txt; git tag >> out.txt; cat out.txt;'
          dataMap = input message: '准备发布到哪个环境', ok: '确定', parameters: [choice(choices: ['dev', 'master'], description: '部署环境', name: 'ENV'), choice(choices: "${BRANCHES}", description: '分支', name: 'TAG')], submitterParameter: 'APPROVER'
        }
      }
    }
    stage('演示一下'){
      steps{
        echo "${dataMap['APPROVER']}"
        echo "${dataMap['ENV']}"
        echo "${dataMap['TAG']}"
      }
    }
  }
}
         
