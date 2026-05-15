# README

## Run locally

`gradle bootRun --args='--spring.profiles.active=dev'`

`gradle bootRun --debug-jvm --args='--spring.profiles.active=dev'`

## Kubernetes

1. Build and publish a container image for this app.
2. Update the image value in `k8s/deployment.yaml`.
3. Apply manifests:

```bash
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```
