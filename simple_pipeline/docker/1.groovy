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
      steps{
        script{
          def mysql = docker.build("mysql:5.7")
          def mysql_container = mysql.run("--name mysql-server -it --restart=always -e MYSQL_DATABASE='zabbix' -e MYSQL_USER='root' -e MYSQL_PASSWORD='root' -e MYSQL_ROOT_PASSWORD='root' -v /data/mysql:/var/lib/mysql")
          echo "mysql container is running"
        }
      }
    }
    
    stage('zabbix-server'){
      steps{
        script{
          def server = docker.build("zabbix/zabbix-server-mysql:latest")
          def server_container = server.run("--name zabbix-server-mysql -it --restart=always -e DB_SERVER_HOST='mysql-server' -e MYSQL_DATABASE='zabbix' -e MYSQL_USER='root' -e MYSQL_PASSWORD='root' -e MYSQL_ROOT_PASSWORD='root' --link mysql-server:mysql -p 10051:10051")
          echo "zabbix-server container is running" 
        }
      }
    }
    
    stage('zabbix-web'){
      steps{
        script{
          def web = docker.build("zabbix/zabbix-web-nginx-mysql:latest")
          def web_container = web.run("--name zabbix-web-nginx-mysql -it --restart=always -e DB_SERVER_HOST='mysql-server' -e MYSQL_DATABASE='zabbix' -e MYSQL_USER='root' -e MYSQL_PASSWORD='root' -e MYSQL_ROOT_PASSWORD='root' --link mysql-server:mysql --link zabbix-server-mysql:zabbix-server -p 8088:80")
          echo "zabbix-web container is running"
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
