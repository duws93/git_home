def label="jnlp-slave"
podTemplate(label:label, cloud: 'kubernetes', 
            containers:[containerTemplate(name: 'jnlp-slave', image: 'cnych/jenkins:v1')],
            volumes:[
              hostPathVolume(mountPath: '/usr/bin/docker', hostPath: '/usr/bin/docker'),
              hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
              hostPathVolume(mountPath: '/root/.kube', hostPath: '/root/.kube')])
{
  node(label){
    stage('get a maven project'){
      container(label){
        stage('wait for exec check'){
          sh 'kubectl get pod -n jekkins'
        }
      }
    }
  }
}
