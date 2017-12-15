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

