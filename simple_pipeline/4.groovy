#!groovy
pipeline{
  environment{
    foo = "bar"
    build_num_env = currentBuild.getNumber()
    another_env = "${currentBuild.getNumber()}"
    inherited_env = "\${build_num_env} is inherited"
}
  agent { label "duws-3"}
  
  stages{
    stage("environment"){
      steps{
        sh 'echo "foo is $foo"'
        sh 'echo "build_num_env is $build_num_env"'
        sh 'ehco "another_env is $another_env"'
        sh 'echo "inherited_env is $inherited_env"'
      }
    }
  }
}
