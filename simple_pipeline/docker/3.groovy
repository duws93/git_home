#!groovy

def label = "jnlp"
podTemplate(label: label, cloud: 'kubernetes',containers: [
    containerTemplate(name: 'jnlp', image: 'cnych/jenkins:jnlp')
  ],
  volumes: [hostPathVolume(mounntPath:'/var/run/docker.sock',hostPath:'/var/run/docker.sock'),
            hostPathVolume(mounntPath:' /home/jenkins/.kube',hostPath:'/root/.kube')]) {
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
