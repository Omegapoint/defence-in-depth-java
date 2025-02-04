# Setup https locally
In order to run the application with https we need a p12 certificate. One easy way is to use [mkcert](https://github.com/FiloSottile/mkcert) for this. Make sure to run the command in some other location than the git repo, for example `~/.mkcert/` folder.

Note: to install certificate in the JAVA trust store, ensure that `JAVA_HOME` variable points to your java installation before running `mkcert -install`.

Then generate the .p12 certificate: 
```
openssl pkcs12 -export -in localhost+1.pem -inkey localhost+1-key.pem -out defence-in-depth.p12 -name defence-in-depth
```

Default the application expects to find the configuration for the certificate path and password from AWS Secrets Manager, in region eu-west-1. 

The application expects a secret named `defence-in-depth`, with the structure:
```json
{
    "server.ssl.key-store": "<certificate-path>/defence-in-depth.p12",
    "server.ssl.key-store-password": "<password>"
}
```

To not use Secrets Manager for this (NOT RECOMMENDED), then prefix with `optional:` in `application.yml` under the spring.config.import key, and set the certificate and password properties manually. 
