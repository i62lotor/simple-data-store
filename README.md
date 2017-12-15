# simple-data-store
Repositorio simple de datos, accesible por API REST

## Objetivo
Disponer de un almacén de datos espaciales (o no), accesibles vía HTTP para poder explotarlos desde cualquier cliente.
Para conseguir el objetivo se implenta una API de acceso a la información almacenada en MongoDB

## Tecnología
- [SpringBoot](https://projects.spring.io/spring-boot/)
- [MongoDB](http://www.mongodb.org/)

## API de acceso restringido
El acceso a la API require **autenticación**, así mismo el acceso a ciertas operaciones requiere **autorización**. Para esta primera versión la autorización se basa en roles.

### Roles
Existen 3 roles:
- **USER**: Usuarios con permiso para consultar la información almacenada
- **DATA_MANAGER**: Usuarios con permiso para consultar, añadir y modificar información
- **STORE_ADMIN**: Usuarios con permiso para consultar, añadir y modificar información y usuarios. Por defecto se crea un usuario con este rol (login: sds-Admin, contraseña: default-pw). Es recomendable crear otro usuario con este rol o al menos cambiar la contraseña.

### Autenticación
El acceso a la API (salvo algunos recursos de acceso libre) requiere de un proceso de login. Se usa [JSON WEB TOKEN](https://jwt.io/) para el proceso de autenticación. Para lo cual es necesario enviar la información de usuario en el body de una petición **POST** a http://deploy-url/login

`{"login":"sds-Admin","password": "default-pw"}`

Una vez logado se obtendrá un token en la cabecera *Authorization* para usarlo en el resto de peticiones HTTTP.
`Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZHMtQWRtaW4iLCJleHAiOjE1MTQxOTg4Mzl9.Fxn4Mg3BMzK5ZoHXL8Hpfbc02gEZy2N7xtOo3zn6iq_YRI3xtgDAphBmFusxmvffz3PYuDh_rkW-oCnVIQKkHQ`

## Construir el software
Para construir el software podemos usar maven de la siguiente forma:
`mvn clean package`
Se generará un jar en el directorio target, el cual podemos ejecutar directamente con el siguiente comando
`ava -jar target/simple-data-store-0.0.1-SNAPSHOT.jar`
Puedes ver mas opciones en la [documentación de SpringBoot](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html)

## API
El software incluye documentación de la api con swagger. Para poder consultarla puedes acceder a la url http://deploy-url:8080/swagger-ui.html
