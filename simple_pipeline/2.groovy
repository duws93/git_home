#!groovy
pipeline{
  environment{
    foo = credentials("f1d1e486-4a23-43fe-9f45-19e17e556705")
  }
  
  agent { label "duws-3" }
  
  stages{
    stage("foo"){
      steps{
        sh 'echo "foo is $foo"'
        sh 'echo "foo_user is $foo_user"'
        sh 'echo "foo_psw is $foo_pws"'
        
        dir("combined"){
          sh 'echo $foo > foo.txt'
        }
        sh 'echo $foo_pws > foo_psw.txt'
        sh 'echo $foo_user > foo_user.txt'
        archive "**/*.txt"
      }
    }
  }
}
