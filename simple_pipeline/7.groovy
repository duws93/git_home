#!groovy
pipeline{
  agent {
    label "duws-3"
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
  }
}
         
