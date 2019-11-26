#!groovy
pipeline{
  environment{
    FOO = credentials("f1d1e486-4a23-43fe-9f45-19e17e556705")
  }
  
  agent { label "duws-3" }
  
  stages{
    stage("foo"){
      steps{
        sh 'echo "foo is $FOO"'
        sh 'echo "foo_user is $FOO_USE"'
        sh 'echo "foo_psw is $FOO_PSW"'
        
        dir("combined"){
          sh 'echo $FOO > foo.txt'
        }
        sh 'echo $FOO_PSW > foo_psw.txt'
        sh 'echo $FOO_USE > foo_user.txt'
        archive "**/*.txt"
      }
    }
  }
}
