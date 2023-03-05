## Lab
1. application kubernetes-api
2. Helm Chart
3. Deploy application with different values into dev/stage cluster
4. Try different sync mode
5. Update git repo(Helm/application)

### Create Repository
點選 Settings/Repositories 再透過 CONNECT REPO 建立要連線的資訊，建立內容如下


![](images/argo-created-repo.png)

當中 `project`，選擇系統預設建立的 `default`。該物件負責做 RBAC 的應用功能。如下圖所示再 `settings/project` 下

![](images/argo-created-project-default.png)

再 Destinations 選項中可以針對 Cluster 和 namespece 進行設定。


### Create Application

點選 Application 頁籤，再點擊 NEW APP，即可建立一個 application

1. 定義 GENERAL
![](images/argo-created-app-general.png)

定義應用程式名稱這邊是 api-dev
Sync 策略選擇自動
Sync 選項勾選 `AUTOCREATE NAMESPACE`

2. 定義 Source 和 Destination

![](images/argo-created-app-source-destination.png)

來源選擇一開始我們要監控的 Repo，`Revision` 選擇遠方 Repo 的分支，`Path` 遠方 Repo 的應用程式目錄，這邊所使用的會是該 Repo 下 `kubernete-api` 的 Helm Chart

接下去是佈署的目標這邊選擇 k3d 所建立的 dev 集群環境，namespace 這邊使用 `dev`

3. Helm

這邊沒意外事系統自行偵測，如下圖

![](images/argo-created-app-helm.png)

當中 `VALUES FILES` 會自動偵測 Chart 中 values.yaml 這個檔案。下面都是參數的定義。

4. 建立

Helm 選項填入這些值
![](images/argocd-app-helm-values.png)

這邊必須說明一下，假設再 Helm Chart 定義了

```yaml
annotation: {}
```

這將不會再 `PARAMETRS` 出現，那我們將其定義再 `VALUES` 中，對 values.yaml 進行覆蓋。

這時畫面將會跳到 Application 的集合，如下

![](images/argocd-app-helm-values-sync-status.png)

點擊該 App 會跳到更詳細的頁面，基本上是將佈署再 Cluster 上的 Kubernetes 資源列出

![](images/argocd-app-helm-values-sync-details-status.png)

基本上上面流程都是 `AUTO SYNC` 的流程。

刪除剛剛所佈建的 `api-dev` app

![](images/argocd-app-delete.png)

操作很直覺。

#### 手動 

將建立 APPLICATION 中 `GENERAL` 選項中的 `SYNC POLICY` 選擇 `Manual`。

這邊將環境布置到 stage 的集群中。

HELM 的 `VALUE FILE` 指向我們所同步的 Repo `env/values-stage.yaml`。

![](images/argocd-app-stage-helm.png)

因為設定為手動，所以建立時是 `OUTOfSync` 狀態，但該 Application 是知道遠方有個 Repo。

![](images/argocd-app-stage-sync-manual.png)

此時需要手動點擊 `Sync`，並跳出下圖右方頁籤接這再點擊頁籤的 `SYNCHRONIZE` 即可同步。


>SYNCHRONIZE RESOURCES 屬於選擇性同步，他不會被記錄在 `HISTORY AND ROLLBACK` 中


這邊我們再建立一個 prod 集群環境，並在 `SYNC POLICY` 選項中選擇 `Automatic` 和 `SELF HEAL` 自我修復。

### 應用程式其他應用功能

點擊 Applications 下 api-dev 應用程式再點擊 `APP DETAILS` 再選擇 `PARAMETERS`，可以看到詳細 Helm 的配置內容如下

![](images/argocd-app-helm-dev-app-details-parameters.png)

將應用程式佈署至 dev 集群的時候透過以下方式可以得到當前 API `/hello` 內容

```bash
$ curl http://dev.cch.com:8081/api/dev/hello
Hello dev
```

接著透過修改 `ingress.hosts[0].paths[0].path` 這個值，來看 ArgoCD 的處理。從上圖當前的狀態來看使用 `values-dev.yaml` 檔案進行佈署。藉由上圖中的 `EDIT` 即可來更新，將`ingress.hosts[0].paths[0].path` 值變成 `/api/test(/|$)(.*)`，並按下 `SAVE`，最後結果如下圖

![](images/argocd-app-parameters-change-dev-values.png)

ArgoCD 上會有一個圖式表示那個值有被異動。

再透過 `curl` 測試執行

```bash
$ curl http://dev.cch.com:8081/api/dev/hello
<html>
<head><title>404 Not Found</title></head>
<body>
<center><h1>404 Not Found</h1></center>
<hr><center>nginx</center>
</body>
</html>
$ curl http://dev.cch.com:8081/api/test/hello
Hello dev
```

透過 `SYNC STATUS` 可以看到異動資訊，如下

![](images/argocd-app-change-dev-values-sync-status.png)

DURATION 什麼時間點前異動
INTIATED BY 誰做的，這邊是自動。如果是手動，那就會顯示是哪個登入者觸發。



### SELF HEAL

前面再 Prod 環境中有開啟此自動修復功能，目前 Prod 所佈署的內容為 1 個副本，此時我們遠端操作該 Prod 所佈署的 `Deployment` 資源。



