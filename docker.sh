docker build -t shopping-list-app:latest .

aws ecr get-login-password --region \
  us-east-2 docker login --username AWS \
  --password-stdin <account-id>.dkr.ecr.us-east-2.amazonaws.com

aws ecr create-repository --repository-name shopping-list

docker tag shopping-list-app:latest \
  <account-id>.dkr.ecr.us-east-2.amazonaws.com/shopping-list-app:latest

docker push <account-id>.dkr.ecr.us-east-2.amazonaws.com/myapp:latest