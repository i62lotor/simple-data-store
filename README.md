# simple-data-store
Repositorio simple de datos, accesible por API REST

## Objetivo
Disponer de un almacén de datos espaciales (o no), accesibles vía HTTP para poder explotarlos desde cualquier cliente.
Para conseguir el objetivo se implementa una API de acceso a la información almacenada en MongoDB.
La solución planteada está pensada para proyectos relativamente pequeños, y con necesidades de consulta/edición de capas. Por el momento no se contempla la posibilidad de realizar consultas o análisis espaciales, para ello siempre es mejor usar sistemas de gestión de bases de datos espaciales y Sistemas de Información Geográfica.

## Tecnología
- Java 8
- [SpringBoot](https://projects.spring.io/spring-boot/)
- [MongoDB](http://www.mongodb.org/)

## API de acceso restringido
El acceso a la API require **autenticación**, así mismo el acceso a ciertas operaciones requiere **autorización**. ~~Para esta primera versión la autorización se basa en roles~~. A partir de la versin 0.1.0 el acceso a los recursos puede ser establecido a nivel de usuario.

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

`java -jar target/simple-data-store-0.0.1-SNAPSHOT.jar`

Puedes ver mas opciones en la [documentación de SpringBoot](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html)

## API
El software incluye documentación de la api con swagger. Para poder consultarla puedes acceder a la url http://deploy-url:8080/swagger-ui.html

![Api-doc](simple-data-store-api-0.1.0.png)

## Datos almacenados
Para esta versión se debe tener una instancia de MongoDB. El software usa los parámetros por defecto (test, puerto,...). Para cambiar esos parámetros modificar el fichero *application.properties*

Se almacenarán datos de tipo Layer cuya especificación se corresponde con el siguiente json:
```javascript
{
  "data": "string: datos a almacenar",
  "description": "string: descripción de los datos",
  "format": "GEOJSON | GEOJSON_CSS | WKT | GML | KML | TOPO_JSON | CUSTOM_TEXT",
  "id": "string: identificador autoasignado (debe indicarse para actualizar o consultar)",
  "lastUpdate": "última fecha de modificacion: por ejemplo 2017-12-15T11:20:38.987Z",
  "name": "string: nombre de la capa (no puede quedar vacío",
  "openData": true,
  "projection": "string: proyección correspondiente a los datos"
}
```
## Normas de contribución
Solo pido por favor que se sigua el patrón: **KISS** Pensad que lo va a revisar @i62lotor y a veces es un poco "tikis-mikis".

- Código fuente: podeis enviar vuestras PR, y encantado las incorporo, si al menos siguen un mínimo de limpieza de código, y algún test unitario.
- Testing: me parecen fundamentales. Si quieres hacer pruebas de rendimiento, estabilidad, carga, etc. ¡Genial!
- Documentación: intentaré ir completando la wiki. ¿Quieres ayudarme?

## TODO
Bajo la premisa de mantener el sistema simple, se aceptan sugerencias de todo tipo. En principio tengo en mente incluir:
- ~~Granularidad en el control de acceso a los recursos (capas) a nivel de usuario.~~ Implementado en versin 0.1.0.
- Implementar test de integración.
- Realizar test de estabilidad y carga.
- Generar documentación de uso y api.
- Generar una versión con MongoDB embebido.

