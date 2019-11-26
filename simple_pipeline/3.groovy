#!groovy
pipeline{
  agent{
    dockerfile{
      filename "Dockerfile.alternate"
      args "-v /tmp:/tmp -p 8000:8000"
    }
  }
}
