#!groovy

def label = "jnlp"
podTemplate(label: label, cloud: 'kubernetes',containers: [
    containerTemplate(name: 'jnlp', image: 'cnych/jenkins:jnlp')
  ],
  volumes: [hostPathVolume(mountPath:'/var/run/docker.sock',hostPath:'/var/run/docker.sock'),
            hostPathVolume(mountPath:' /root/.kube',hostPath:'/root/.kube')]) {
    node(label) {
        stage('Get a Maven project') {
            container(label) {
                stage('wait for exec check'){
                    sh 'kubectl get pod'
                }
 
            }
        }
    }
}
