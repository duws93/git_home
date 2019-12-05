#!groovy
pipeline{
  agent { label "duws-3"}
  stages{
    stpes{
      echo "this jenkinsfile in branch dev/python"
    }
  }
}
