#!groovy
pipeline{
  agent{
    node{
      label "duws-3"
      customWorkspace "/root/workspace/zabbix"
    }
  }
  
  options{
    timestamps()
  }
  
  stages{
    stage('mysql'){
      agent{
        steps{
          def mysql = docker.build("mysql:5.7")
          def mysql_container = mysql.run('--name mysql-server2 -it --restart=always -e MYSQL_DATABASE="zabbix" -e MYSQL_USER="root" -e MYSQL_PASSWORD="root" -e MYSQL_ROOT_PASSWORD="root" -v /data/mysql:/var/lib/mysql -d mysql:5.7 --character-set-server=utf8 --collation-server=utf8_bin')
          echo "mysql container is running"
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
