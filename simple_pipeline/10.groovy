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
        docker{
          reuseNode true
          image 'mysql:5.7'
          args '--name mysql-server2 -it --restart=always -e MYSQL_DATABASE="zabbix" -e MYSQL_USER="root" -e MYSQL_PASSWORD="root" -e MYSQL_ROOT_PASSWORD="root" -v /data/mysql:/var/lib/mysql --character-set-server=utf8 --collation-server=utf8_bin'
        }
      }
      steps{
        echo "${image} container is startting"
      }
    }
    
    stage('zabbix-server'){
      agent{
        docker{
          reuseNode true
          image 'zabbix/zabbix-server-mysql:latest'
          args '--name zabbix-server-mysql2 -it --restart=always -e DB_SERVER_HOST="mysql-server" -e MYSQL_DATABASE="zabbix" -e MYSQL_USER="root" -e MYSQL_PASSWORD="root" -e MYSQL_ROOT_PASSWORD="root" --link mysql-server:mysql2 -p 10052:10051'
        }
      }
      steps{
        echo "${image} container is startting"
      }
    }
    
    stage('zabbix-web'){
      agent{
        docker{
          reuseNode true
          image 'zabbix/zabbix-web-nginx-mysql:latest'
          args '--name zabbix-web-nginx-mysql2 -it --restart=always -e DB_SERVER_HOST="mysql-server" -e MYSQL_DATABASE="zabbix" -e MYSQL_USER="root" -e MYSQL_PASSWORD="root" -e MYSQL_ROOT_PASSWORD="root" --link mysql-server:mysql2 --link zabbix-server-mysql:zabbix-server2 -p 8081:80'
        }
      }
      steps{
        echo "${image} container is startting"
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
}
