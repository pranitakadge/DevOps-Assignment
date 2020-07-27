def helmDeploy='''#!/bin/bash
cd Web_app_helm_chart
helm install $project_name .
'''

node('master') {
    env.environment = "dev"
    env.gitBranch = "https://github.com/pranitakadge/DevOps-Assignment"
    env.project_name = "web_app"
    
    stage('git-clone'){
        cleanWs()
        git branch: 'master', credentialsId: 'a3abaa1c-a844-428e-a3e6-0ea169996c8a', url: env.gitBranch
    }

withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'ecr-creds', variable: 'AWS_ACCESS_KEY_ID']]){
    sh 'aws eks --region us-east-1 update-kubeconfig --name cluster-name'
}
    

    stage('helm-deploy') {
        sh(helmDeploy)
    }
    
}

