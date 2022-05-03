# Run the token service
All of the code require a Token Service that provides the tokens we need to secure the system we are building.

For this purpose `keycloak` is run through a docker container, using `docker-compose`. The configuration of keycloak can be found in the `keycloak` folder.

You can start the token service by running the following command:
```sh
docker-compose up -d
```

For administration GUI open a browser and visit: `http://localhost:8180`, the username and password is `admin/admin`
