1. install kubectl and helm on wsl
2. disable traefik and use nginx instead
```bash
$ helm repo add ingress-nginx  https://kubernetes.github.io/ingress-nginx
$ helm search repo ingress-nginx
NAME                            CHART VERSION   APP VERSION     DESCRIPTION
ingress-nginx/ingress-nginx     4.4.2           1.5.1           Ingress controller for Kubernetes using NGINX a...
$ helm install ingress-nginx ingress-nginx/ingress-nginx --version 4.4.2 --namespace ingress-nginx --create-namespace
$ kubectl get all -n ingress-nginx
NAME                                            READY   STATUS    RESTARTS   AGE
pod/ingress-nginx-controller-7d5fb757db-gx8hp   1/1     Running   0          2m36s

NAME                                         TYPE           CLUSTER-IP      EXTERNAL-IP                        PORT(S)                      AGE
service/ingress-nginx-controller-admission   ClusterIP      10.43.138.141   <none>                             443/TCP                      2m36s
service/ingress-nginx-controller             LoadBalancer   10.43.236.109   172.19.0.2,172.19.0.3,172.19.0.4   80:32369/TCP,443:31027/TCP   2m36s

NAME                                       READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/ingress-nginx-controller   1/1     1            1           2m36s

NAME                                                  DESIRED   CURRENT   READY   AGE
replicaset.apps/ingress-nginx-controller-7d5fb757db   1         1         1       2m36s
```

## Install
1. Argo

```bash
k3d cluster create -c argocd-conf.yaml --servers-memory 6G
```

```bash
helm repo add argo https://argoproj.github.io/argo-helm
~/gitops/argo-cd$ helm install argo-cd argo/argo-cd --version 5.23.2 --namespace argo --create-namespace  -f values.yaml
NAME: argo-cd
LAST DEPLOYED: Mon Feb 27 14:54:00 2023
NAMESPACE: argo
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
In order to access the server UI you have the following options:

1. kubectl port-forward service/argo-cd-argocd-server -n argo 8080:443

    and then open the browser on http://localhost:8080 and accept the certificate

2. enable ingress in the values file `server.ingress.enabled` and either
      - Add the annotation for ssl passthrough: https://argo-cd.readthedocs.io/en/stable/operator-manual/ingress/#option-1-ssl-passthrough
      - Set the `configs.params."server.insecure"` in the values file and terminate SSL at your ingress: https://argo-cd.readthedocs.io/en/stable/operator-manual/ingress/#option-2-multiple-ingress-objects-and-hosts


After reaching the UI the first time you can login with username: admin and the random password generated during the installation. You can find the password by running:

kubectl -n argo get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d

(You should delete the initial secret afterwards as suggested by the Getting Started Guide: https://argo-cd.readthedocs.io/en/stable/getting_started/#4-login-using-the-cli)
```

[ingress setting](https://argo-cd.readthedocs.io/en/stable/operator-manual/ingress/)

2. dev

```bash
k3d cluster create -c dev-conf.yaml --servers-memory 2G
```

3. stage

```bash
k3d cluster create -c stage-conf.yaml --servers-memory 2G
```

4. prod

```bash
k3d cluster create -c prod-conf.yaml --servers-memory 2G
```
