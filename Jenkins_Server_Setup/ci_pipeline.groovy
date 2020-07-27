def  executeArchieveBuilds='''#!/bin/bash
cd Web_Application
rm -rf $project_name.zip
zip -r $project_name *
chmod 777 $project_name.zip
sudo docker build --build-arg env_project_name=$project_name -t web_app .
'''

node('master') {
    env.environment = "dev"
    env.gitBranch = "https://github.com/pranitakadge/DevOps-Assignment"
    env.project_name = "test_api"
    
    stage('git-clone'){
        cleanWs()
        git branch: 'master', credentialsId: 'a3abaa1c-a844-428e-a3e6-0ea169996c8a', url: env.gitBranch
    }
    
    stage('archieve-build') {
        sh(executeArchieveBuilds)
    }

    
    docker.withRegistry('https://account-id.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:ecr-creds') {
    docker.image('web_app').push('latest')
  }
   
    stage('post-build') {
        sh('sudo docker rmi web_app:latest')
    }
}

