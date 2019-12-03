#!groovy
pipeline{
  agent{
  label "duws-3"
  customworkspace '/root/workspace/pipeline'
  }
  
