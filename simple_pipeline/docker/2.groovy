/*
  配置容器版Jenkins使用宿主机得docker命令
*/
def label="jnlp-slave"
podTemplate(label:label, cloud: 'kubernetes', 
            containers:[containerTemplate(name: 'jnlp-slave', image: 'cnych/jenkins:jnlp')],
            volumes:[
              hostPathVolume(mountPath: '/usr/bin/docker', hostPath: '/usr/bin/docker'),
              hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
              hostPathVolume(mountPath: '/home/jenkins/.kube', hostPath: '/root/kube')
            ]
           ){
  node(label){
    stage('get a maven project'){
      container(label){
        stage('wait for exec check'){
          sh 'kubectl get pod '
        }
      }
    }
  }
}
