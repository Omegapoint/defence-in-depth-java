# Run the token service
All of the code require a Token Service that provides the tokens we need to secure the system we are building.

For this purpose `keycloak` is run through a docker container, using `docker-compose`. The configuration of keycloak can be found in the `keycloak` folder.

We need to generate a certificate and private key in order for keycloak to run https. 

For this, you can for example use [mkcert](https://github.com/FiloSottile/mkcert). Ensure that there exists a file `server.crt.pem` and `server.key.pem` inside the `keycloak/conf` folder.

Then you can start the token service by running the following command:
```sh
docker-compose up -d
```

For administration GUI open a browser and visit: `https://localhost:4000`, the username and password is `admin/admin`
