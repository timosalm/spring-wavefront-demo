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
    cf push
    ```

4. Call the REST API to create one or more items.
    ```
    curl --request POST \
      --url https://trace-explorer-demo-impressive-impala.cfapps.io/items
    ```

5. Call the REST API to fetch the available items.
    ```
    curl --request GET \
      --url https://trace-explorer-demo-impressive-impala.cfapps.io/items
    ```