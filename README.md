# Spring Boot ~~Wavefront~~ VMware Tanzu Observability demo running on Cloud Foundry

### Prerequisites
- JDK 11
- ~~Wavefront~~ [VMware Tanzu Observability](https://tanzu.vmware.com/observability) account
- Cloud Foundry environment (e.g. sign up for a free [Pivotal Web Services account](https://run.pivotal.io/))
- 'cf' command-line interface for Cloud Foundry:
    
    Follow the instructions on how to install the cf CLI using a **package manager** on Mac OS X and Linux operating systems [here](https://docs.cloudfoundry.org/cf-cli/install-go-cli.html#pkg).
    
    You can install the cf CLI using a **compressed binary** on Windows, Mac OS X, and Linux operating systems [here](https://github.com/cloudfoundry/cli#installers-and-compressed-binaries).
    
    Log in to the cf CLI targeting your environment. E.g.
    ```
     cf login -a https://api.run.pivotal.io
    ```
### Install Wavefront Proxy on CF

1. Push the Docker container to CF with an internal route
    ```
    cf push wavefront-proxy --docker-image wavefronthq/proxy:latest --no-start -d apps.internal
    ```
2. Change the type of the healthcheck
    ```
   cf set-health-check wavefront-proxy process
   ```
3. Login to ~~Wavefront~~ VMware Tanzu Observability, navigate to ***Browse &rarr; Proxies*** and click on ***Add new proxy***. Select ***Docker*** in the ***How to Add a Proxy*** section and set replace the values of the ENV variables in the following commands with yours
    ```
    cf set-env wavefront-proxy WAVEFRONT_URL "YOUR_WAVEFRONT_URL"
    cf set-env wavefront-proxy WAVEFRONT_TOKEN YOUR_WAVEFRONT_TOKEN
   ``` 
4. Set the following ENV variable to activate the ZipKin integration
    ```
    cf set-env wavefront-proxy WAVEFRONT_PROXY_ARGS "--traceZipkinListenerPorts 9411"
    ```
5. Start the Proxy
    ```
     cf start wavefront-proxy
    ```
6. Switch back to ~~Wavefront~~ VMware Tanzu Observability. Now the proxy should be detected. Press ***Done*** to add the proxy.

### Run the application on CF

1. Create a MySQL service instance with the name `mysql`
    ```
    cf create-service cleardb spark mysql
    ```   
2. Build the application
    ```
    ./gradlew test bootJar
    ```
3. Push the application to CF
    ```
    cf push --no-start
    ```
4. Configure [Container-to-Container Networking](https://docs.pivotal.io/platform/application-service/2-8/devguide/deploy-apps/cf-networking.html) from the application to the deployed ~~Wavefront~~ VMware Tanzu Observability proxy 
    ```
    cf add-network-policy spring-wavefront-demo --destination-app wavefront-proxy --protocol tcp --port 9411
    ```
5. Set ZipKin base url to the proxy's integration endpoint (possible via application.yml, too)
    ```
    cf set-env spring-wavefront-demo SPRING_ZIPKIN_BASE-URL "http://wavefront-proxy.apps.internal:9411/"
    ```
6. Start the application
    ```
     cf start spring-wavefront-demo
    ```

### View traces of the app in ~~Wavefront~~ VMware Tanzu Observability
1. Call the REST API to create one or more items.
    ```
    curl --request POST --url https://YOUR-APP-URL/items
    ```
2. Call the REST API to fetch the available items.
    ```
    curl --request GET --url https://YOUR-APP-URL/items
    ```
3. Switch back to ~~Wavefront~~ VMware Tanzu Observability, and navigate to ***Applications &rarr; Traces***.