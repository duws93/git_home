/*
  配置容器版Jenkins使用宿主机得docker命令
*/
def label="jnlp-slave"
podTemplate(label:label, cloud: 'kubernetes', 
            containers:[containerTemplate(name: 'jnlp-slave', image: 'cnych/jenkins:v1')],
            volumes:[
              hostPathVolume(mountPath: '/usr/bin/docker', hostPath: '/usr/bin/docker'),
              hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')]
           ){
  node(label){
    stage('get a maven project'){
      container(label){
        stage('wait for exec check'){
          sh 'sleep 1'
          sh 'echo $JAVA_HOME'
          sh 'cat /etc/resolv.conf'
          sh 'uname -a'
          sh 'docker ps -a'
        }
      }
    }
  }
}
