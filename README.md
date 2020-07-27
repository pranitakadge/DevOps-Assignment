# Assignment - DevOps 

## Problem statement

- Customer working in E-commerce platform wants to host web based application which connects   with MOngoDB.The environment should be completely scalable and highly available and database should be clustered.

### Steps Followed

- AWS cloud formation for cloud services provisioning.
- Web application development in Django Rest framework.
- Deploy clustered MongoDB in EKS.
- Create helm chart for application.
- Setup Jenkins server setup.
- Create CICD pipeline for deployment.

### Implementation

1. AWS cloud formation for cloud services provisioning.

- Services such as VPC,Subnet,Internet Gateway,Security Groups,EKS Cluster,EKS Node Group are created using cloud formation.

```sh
create stack using aws-infra.yml
```

2. Web application development in Django Rest framework.

- Endpoint for application http://IP/customer/{customer_id}
- Make sure you have {project_name}.zip file of your code.
- Create docker image and push to ECR registry.

```sh
docker build --build-arg env_project_name={project_name} -t web_app .
docker tag web_app repo_path
docker push repo_path
```

3. Deploy clustered MongoDB in EKS.

- Create storage class for ebs volume.
- Create cluster role binding for service account.
- Create statefulsets for mongo.
- Create headless service for mongo.

```sh
kubectl apply -f aws_ebs.yml
kubectl apply -f role_binding.yml
kubectl apply -f mongo_statefulset.yml
kubectl apply -f mongo_service.yml
```

Mongo connection uri

```sh
mongodb://mongo-0.mongo,mongo-1.mongo,mongo-2.mongo:27017/dbname_?
```

4. Create helm chart for application

- created helm chart
- Service type - LoadBalancer
- Update repository name, resources , replica count in values.yaml.
- create base64 of mongo uri string and update in secrets.mongoHost in values.yaml
- For scalability Horizontal pod autoscaler added.

```sh
cd Web_app_helm_chart
helm install web-app .
```

5. Setup Jenkins server setup

- Create EC2 instance
- Install ansible on local
- Add host in /etc/ansible/hosts

```
host_ip ansible_ssh_user=ec2-user ansible_ssh_private_key_file=/home/user/Downloads/aws-key.pem
```

- cd Jenkins_Server_Setup and run ansible

```
ansible-playbook setup-jenkins.yml -vvv
```

- Add tcp 8080 port in your security group
- Login to jenkins using init password from your ansible output.
- Install git,helm,aws,docker in jenkins master
- Add github and aws credentials to jenkins
- Set git path in global tool configuration
- Install Amazon ECR plugin and Docker pipeline plugin in jenkins
- Create repository in AWS ECR for docker image

```
sudo usermod -a -G docker jenkins
```

6. Create CICD pipeline for deployment

- cd Jenkins_Server_Setup
- Create CI and CD pipeline in jenkins 

 CI pipeline Flow

- Git clone code
- Build docker image
- Push image to registry

 CD pipeline Flow

- Git clone helm chart
- Set context of aws cluster
- Install helm chart
