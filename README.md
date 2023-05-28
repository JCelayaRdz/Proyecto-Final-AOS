# Segunda práctica de AOS
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Language: Java](https://img.shields.io/badge/Language-Java-green)](https://www.oracle.com/es/java/)
[![Equipo: 6](https://img.shields.io/badge/Equipo-6-blue)]()

Este repositorio contiene el código necesario para la realización de la segunda práctica de la
asignatura Arquitecturas Orientadas a Servicios del grado de Ingeniería del Software de la UPM.
Los objetivos de esta práctica son:

* Especificar una API REST para la gestión de clientes de un taller usando OpenAPI
* Implementar la API
* Desarrollar un Dockerfile para construir una imagen de la API
* Publicar la imagen en DockerHub
* Usar Docker Compose para ejecutar un mock de la API mediante Stoplight Prism y otros contenedores
* Usar Docker Compose para ejecutar la API así como su base de datos y la interfaz de Swagger
* Desarrollar todos los ficheros necesarios para desplegar la API en clúster de Kubernetes
* Crear una breve memoria de la práctica

## Índice
1. [Stack Tecnológico usado](#stack-tecnologico-usado)
2. [Como ejecutar el proyecto](#como-ejecutar-el-proyecto)
   1. [Usando Docker Compose](#usando-docker-compose)
   2. [Usando Kubernetes](#usando-kubernetes)
3. [Explicación de los servicios](#explicacion-de-los-servicios)
   1. [Servicios de Docker Compose de la API](#servicios-de-docker-compose-de-la-api)
   2. [Servicios de Docker Compose del mock](#servicios-de-docker-compose-del-mock)
   3. [Objetos de Kubernetes](#objetos-de-kubernetes)
4. [Consideraciones importantes](#consideraciones-importantes)
   1. [Endpoints de la API](#endpoints-de-la-api)
   2. [Query parameters del endpoint CGET](#query-parameters-del-endpoint-cget)
   3. [Validación de los objetos](#validacion-de-los-objetos)
   4. [Atributo vehículos y mock del servicio](#atributo-vehiculos-y-mock-del-servicio)
   5. [Peticiones OPTIONS a la API usando Swagger y Docker Compose](#peticiones-options-a-la-api-usando-swagger-y-docker-compose)
5. [Autores](#autores-equipo-6) 

## Stack Tecnologico usado
Para realizar esta práctica se han usado, entre otras, las siguientes tecnologías:

* [Spring Boot](https://spring.io/projects/spring-boot): Como framework web para desarrollar la API.
* [Spring Data JPA](https://spring.io/projects/spring-data): Como ORM.
* [Hibernate Validator](https://hibernate.org/validator/): Para validar los campos de las peticiones HTTP.
* [Jackson](https://github.com/FasterXML/jackson): Como librería para serializar y deserializar objetos Java.
* [Stoplight Prism](https://stoplight.io/open-source/prism): Como mock de la API.
* [Caddy Server](https://caddyserver.com): Como servidor proxy para el mock.
* [Swagger](https://swagger.io): Como interfaz para realizar peticiones a la API y al mock.
* [Maven](https://maven.apache.org): Como gestor de dependencias.
* [MySQL](https://www.mysql.com): Como sistema gestor de bases de datos.
* [Docker](https://www.docker.com): Como software de contenerización.
* [Kubernetes](https://kubernetes.io/es/): Como plataforma de orquestación de contenedores. Usamos 
[Minikube](https://minikube.sigs.k8s.io/docs/) para crear un clúster en local.

## Como ejecutar el proyecto

### Usando Docker Compose
La API y el mock de esta están separados en dos ficheros diferentes, hemos considerado que esta es una
mejor opción que tener todos los servicios en un único fichero, pues rara vez se ejecutará la API y el mock
al mismo tiempo.<br>
Para ejecutar el proyecto primero deberemos de clonar este repositorio con el siguiente comando:
```bash
git clone https://github.com/JCelayaRdz/Proyecto-Final-AOS.git
```
Una vez hecho esto entramos dentro de la carpeta del proyecto:
```bash
cd Proyecto-Final-AOS
```
Situándonos en la raíz de este proyecto, para ejecutar el mock debemos teclear el siguiente comando:
```bash
docker compose -f docker-compose-mock.yml up -d
```
Para interactuar con el mock de la API se pueden usar herramientas como Postman, Curl, o la interfaz de Swagger
en la siguiente URL:
```url
http://localhost:8001/
```
Para ejecutar la API es necesario teclear el siguiente comando. Hay que esperar en torno a unos 20 segundos
para poder enviar peticiones a la API, pues el servidor de MySQL tarda un poco en iniciar.
```bash
docker compose up -d
```
Para interactuar con la API se pueden usar herramientas como Postman y Curl. El puerto en local de la API es el 8080.
También se puede usar la interfaz de Swagger en la siguiente URL:
```url
http://localhost:8000/
```
Si por algún casual se quieren ejecutar al mismo tiempo la API y el mock (no es recomendable):
```bash
docker compose -f docker-compose-mock.yml -f docker-compose.yml up -d
```
Para limpiar los recursos de cualquiera de los comandos anteriores:
```bash
docker compose down -v
```

### Usando Kubernetes
Suponiendo que se usa Minikube y que nos encontramos en la raíz de este proyecto los pasos para ejecutar el proyecto
son los siguientes.<br>
Debemos iniciar Minikube:
```bash
minikube start
```
Creamos los objetos de Kubernetes necesarios para desplegar la aplicación:
```bash
kubectl apply -f k8s/
```
Comprobamos que los Pods estén ejecutándose:
```bash
kubectl get pods
```
Debemos obtener una salida similar a esta:
```bash
NAME                                    READY   STATUS    RESTARTS        AGE
api-gestion-clientes-5c85cf488d-wqtc4   1/1     Running   5 (13s ago)     4h4m
mysql-0                                 1/1     Running   1 (3h59m ago)   4h4m
swagger-ui-d566cc454-fgr8k              1/1     Running   1 (3h59m ago)   4h4m
```
A continuación debemos permitir que los servicios de Kubernetes sean accesibles mediante el comando:
```bash
minikube service --all
```
Minikube abrirá varias pestañas en el navegador con las IP's de los servicios en local y debemos obtener una salida 
similar a la siguiente:
```bash
|-----------|------------------------------|-------------|------------------------|
| NAMESPACE |             NAME             | TARGET PORT |          URL           |
|-----------|------------------------------|-------------|------------------------|
| default   | api-gestion-clientes-service |             | http://127.0.0.1:51001 |
| default   | kubernetes                   |             | http://127.0.0.1:51003 |
| default   | mysql                        |             | http://127.0.0.1:51005 |
| default   | swagger-ui                   |             | http://127.0.0.1:51007 |
|-----------|------------------------------|-------------|------------------------|
```

Es importante mencionar que no se pueden realizar peticiones a la API a través de la interfaz de Swagger cuando se usa
Kubernetes, no sabemos exactamente por qué. No se trata de un problema de CORS, ya que hemos configurado la API para que
acepte peticiones de cualquier IP. Y tampoco se trata de un problema de DNS porque al ejecutar el comando:
```bash
kubectl exec -it <id del pod de swagger> -- sh
```
y una vez dentro del Pod el comando:

```bash
curl -X 'GET' \
  'http://api-gestion-clientes-service:80/api/v1/clientes' \
  -H 'accept: application/json'
```
La API responde de manera correcta. No hemos sabido resolver este error por falta de tiempo. Si se quieren realizar
peticiones a la API se deben de usar herramientas como Postman o Curl y se deberá de usar la IP que se obtiene al
ejecutar el comando `minikube service --all`, en el ejemplo anterior sería `http://127.0.0.1:51001/api/v1/` y se pueden
usar los mismos [endpoints](#endpoints-de-la-api) que aparecen en la interfaz de Swagger.

Finalmente, para limpiar los recursos creados dentro del clúster debemos de ejecutar los siguientes comandos:
```bash
kubectl delete all --all
kubectl delete pvc --all 
kubectl delete pv --all
```

Y para detener el clúster el siguiente comando:
```bash
minikube stop
```

## Explicacion de los servicios

### Servicios de Docker Compose de la API
El siguiente diagrama muestra de manera resumida los contenedores que se ejecutan
al utilizar el fichero `docker-compose.yml`<br><br>

<img src="https://github.com/JCelayaRdz/Practica-AOS/assets/77314339/4082dac7-08b4-419f-a175-63531bf81773">

Como podemos observar, todos los contenedores se comunican por medio de la red `proyecto-final-aos_default_api-network`.
Tenemos tres contenedores:

* **frontend**: Es el contenedor que ejecuta la interfaz de Swagger. Usa un bind mount para leer el archivo
que contiene la especificación y la monta en la carpeta `/aos`, usa la variable de entorno `SWAGGER_JSON`
para indicar la ruta del fichero de la especificación dentro del contenedor. Los puertos y dependencias entre 
contenedores las podemos observar en el diagrama.
* **api**: Es el contenedor que ejecuta el servidor de Spring Boot. Tiene una variable de entorno `MYSQL_HOST` para poder
conectarse a la base de datos de forma correcta. Los puertos y dependencias las podemos observar en el
diagrama.
* **mysql**: Es el servidor de MySQL. Usa las variables de entorno `MYSQL_HOST`, `MYSQL_ROOT_PASSWORD` y `MYSQL_DATABASE`
para configurar la base de datos así como el acceso a esta. Usa el volumen `gestion-clientes-data` para almacenar la
información de los clientes. El volumen mencionado anteriormente es montado en la carpeta `/var/lib/mysql` del contenedor.

### Servicios de Docker Compose del mock
El siguiente diagrama muestra de manera resumida los contenedores que son necesarios para ejecutar
el mock de la API al utilizar el fichero `docker-compose-mock.yml`<br><br>

<img src="https://github.com/JCelayaRdz/Practica-AOS/assets/77314339/10b7be06-d25b-4032-b513-14964dd6591e">

Todos los contenedores se comunican por medio de la red `proyecto-final-aos_default_mock-network`
Tenemos tres contenedores:

* **mock_backend**: Es el contenedor de Stoplight Prism que actúa como mock de la API. Usa un bind mount
para leer el fichero que tiene la especificación OpenAPI, y la monta en la carpeta `/aos`. El mapeo de puertos
se puede observar en el diagrama.
* **frontend**: Es el contenedor que ejecuta la interfaz de Swagger. Usa un bind mount para lo mismo que el contenedor
anterior, pero además tiene la variable de entorno `SWAGGER_JSON` para indicar la ruta del fichero de la especificación
dentro del contenedor. El mapeo de puertos se observa en el diagrama.
* **proxy**: Es el contenedor que ejecuta el servidor de Caddy. Actúa como proxy, redirige todas las peticiones
que acaben en `/api/v1` al mock de la API. Utiliza dos bind mounts para funcionar de manera correcta, monta el fichero
`Caddyfile` (con las instrucciones para la redirección) en la ruta `/etc/caddy/Caddyfile` y la carpeta `caddy_data` la
monta en la ruta `/data`. Este contenedor no se pondrá en marcha hasta que lo haya hecho el del servicio `mock_backend`

### Objetos de Kubernetes
El siguiente diagrama muestra los diferentes objetos que se crean dentro del clúster de Kubernetes así como
las relaciones entre ellos.

<img src="https://github.com/JCelayaRdz/Proyecto-Final-AOS/assets/77314339/617c558d-a9e9-43b7-88e7-f3db52dc0159">

Contamos con los siguientes objetos:

* **Pod `api-gestion-clientes`**: Es el Pod que ejecuta la API. Utiliza la imagen de DockerHub
`jcelayardz/api-gestion-clientes:latest` para ejecutar un contenedor llamado `api-gestion-clientes`.
El contenedor expone el puerto `8080` y tiene tres variables de entorno: `SPRING_DATASOURCE_URL`,
`SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD`. Las variables de entorno se extraen de un objeto
Secret llamado `mysql-secrets` para a su vez ser usadas en el ConfigMap `api-gestion-clientes-config`. Dichas variables
de entorno son usadas para que la API se pueda conectar a la base de datos. Este Pod envía peticiones al servicio `mysql`
para obtener los datos de los clientes.
* **Deployment `api-gestion-clientes`**: Se encarga de gestionar que siempre se encuentre en ejecución en el clúster una
réplica del Pod anterior.
* **Service `api-gestion-clientes-service`**: Expone la API como un servicio accesible desde fuera del clúster
gracias a que es del tipo `NodePort`. Redirige el tráfico del puerto `80` al puerto `8080` del contenedor que se ejecuta
en el Pod mencionado anteriormente. Este servicio es accesible desde fuera del clúster por medio del puerto `30080`.
* **ConfigMap `api-gestion-clientes-config`**: Se encarga de crear un archivo de configuración `application.properties`,
que entre otras cosas, define las credenciales para acceder a la base de datos  por medio de las variables de
entorno que tiene el contenedor `api-gestion-clientes`.<br><br>

* **Pod `swagger-ui`**: Es el Pod que ejecuta la interfaz de Swagger. Utiliza la imagen de DockerHub
`swaggerapi/swagger-ui` para ejecutar un contenedor llamado `swagger-ui`, el cual expone el puerto `8080`.
El contenedor usa la variable de entorno `SWAGGER_JSON` para definir la ruta en la cual se encuentra la especificación
OpenAPI. La especificación es extraída del ConfigMap `openapi-config` y montada por medio del volumen `openapi-volume`.
Este Pod llama al servicio `api-gestion-clientes-service`.
*  **Deployment `swagger-ui`**: Se encarga de gestionar que siempre se encuentre en ejecución en el clúster una
réplica del Pod anterior.
* **Service `swagger-ui`**: Expone la interfaz de Swagger como un servicio accesible desde fuera del clúster. Este
servicio es del tipo `NodePort` y redirige el tráfico del puerto `80` al puerto `8080` del contenedor `swagger-ui`.
* **ConfigMap `openapi-config`**: Se encarga de crear el archivo `openapi.yml` que contiene la especificación OpenAPI.
<br><br>

* **Pod `mysql`**: Es el Pod que ejecuta el servidor de MySQL. Ejecuta el contenedor `mysql`, que usa la imagen
`mysql:8.0.33` de DockerHub. El contenedor tiene definidas dos variables de entorno: `MYSQL_ROOT_PASSWORD` (extraída del
objeto Secret `mysql-secrets`) y `MYSQL_DATABASE`, necesarias para el correcto funcionamiento de la base de datos. El
contenedor mencionado anteriormente expone el puerto `3306` y usa el volumen `mysql-persistent-storage` para usar el
PVC (Persistent Volume Claim) `mysql-pvc`.
* **StatefulSet `mysql`**: Se encarga de gestionar que siempre se encuentre en ejecución en el clúster una
réplica del Pod anterior, además está asociado con el servicio `mysql`.
* **Service `mysql`**: Expone el servidor de MySQL como un servicio accesible desde el clúster. Redirige el tráfico
del puerto `3306` al puerto `3306` del contenedor ejecutado por el Pod mencionado anteriormente.
* **Secret `mysql-secrets`**: Se encarga de almacenar la URL, el nombre de usuario y la contraseña
de la base de datos codificados en formato Base64.
* **PVC `mysql-pvc`**: Se utiliza para solicitar almacenamiento persistente de 1 GB para la base de datos MySQL.


## Consideraciones importantes
### Endpoints de la API
La API tiene los siguientes endpoints:

| Método HTTP | Endpoint                     | Resultado                                                           |
|-------------|------------------------------|---------------------------------------------------------------------|
| GET         | /api/v1/clientes             | Obtiene todos los clientes                                          |
| POST        | /api/v1/clientes             | Guarda un nuevo cliente                                             |
| OPTIONS     | /api/v1/clientes             | Devuelve la lista de los métodos HTTP soportados por este endpoint  |
| GET         | /api/v1/clientes/{clienteId} | Devuelve al cliente identificado por `clienteId` si existe          |
| OPTIONS     | /api/v1/clientes/{clienteId} | Devuelve la lista de los métodos HTTP soportados por este endpoint  |
| PUT         | /api/v1/clientes/{clienteId} | Actualiza el cliente identificado por `clienteId` si existe         |
| DELETE      | /api/v1/clientes/{clienteId} | Elimina el cliente identificado por `clienteId` si existe           |

La documentación completa de la API se encuentra en la interfaz de Swagger, o en la especificación OpenAPI
incluida en el fichero `/openapi/api/openapi.yml`

### Query parameters del endpoint CGET
El endpoint GET `/api/v1/clientes` acepta tres query paremeters (o parámetros de búsqueda en español): `page`, `order`
y `ordering`, el comportamiento de estos parámetros es el siguiente:

* `page`: Si no está presente, devuelve la primera página de la API. Si está presente y existe la página
devuelve dicha página, si la página no existe se devuelve un código de estado `404`. Las páginas tienen un tamaño fijo
de 5 elementos (clientes), que no se puede cambiar.
* `order`: Si no está presente ordena a los clientes por su id. Si está presente ordena a los clientes por el campo 
correspondiente, si es que este existe.
* `ordering`: Si no está presente ordena a los clientes de manera ascendente `ASC`. Si está presente y su valor
es `DESC` ordena a los clientes de manera descendente.

### Validacion de los objetos
La API tiene validación, esto quiere decir que tanto al enviar peticiones que requieren de un cuerpo en formato JSON
como al guardar clientes en la base de datos se asegura de que:

* Aparezcan los siguientes atributos: `id`, `nombre`, `apellidos`, `numero_telefono`, `vehiculos` y el objeto `direccion`
compuesto obligatoriamente por `numero_edificacion` y `nombre_calle` y opcionalmente por `detalles`
* Se respeten las siguientes expresiones regulares: `id` debe de ser un DNI o NIE válido, `numero_telefono` debe de ser
un número español que opcionalmente puede empezar por los prefijos `0034`, `34` o `+34`, `correo_electronico` debe de ser
un correo válido, `nombre_calle` debe de seguir el formato `C/ Ejemplo` y `edad` debe de ser mayor o igual a 0.

Si no se proporciona un atributo obligatorio o si se proporciona, pero este no respeta las reglas mencionadas 
anteriormente, la API devolvera un código de estado `422`. Además, se debe de usar `snake_case` para los nombres de los
atributos JSON.

### Atributo `vehiculos` y mock del servicio
Nuestro servicio debería de integrarse con el de vehículos para que cada cliente tuviera links `HATEOAS` con una
referencia a los códigos VIN de sus vehículos, es decir, que si existe un cliente determinado deberíamos
de consultar los códigos VIN al servicio correspondiente. Dado que hay varios equipos que desarrollan
el servicio de vehículos y que estos no están puestos en marcha en un entorno de producción, hemos optado por crear la
clase `org.grupo6aos.apigestionclientes.service.VinService` que actúa como mock del servicio, devolviendo un array 
aleatorio (de tamaño entre 1 y 3) de códigos VIN. Es de esta forma como se genera el atributo `vehiculos`.<br>
Esto quiere decir que cada vez que se envíe una petición a la API cada cliente tendrá códigos VIN diferentes, incluso
cuando se envíe una petición POST, pues en nuestra base de datos solo guardamos datos de clientes, como indica la teoría
de microservicios (cada servicio tiene su propia base de datos).

### Peticiones OPTIONS a la API usando Swagger y Docker Compose
Si se quieren realizar peticiones OPTIONS a la API desde la interfaz de Swagger cuando se usa Docker Compose
es necesario desactivar el CORS en el navegador, de lo contario hay que usar herramientas como Postman o Curl.

## Autores [Equipo 6]
* Juan Antonio Celaya Rodríguez
* Miguel Biondi Romero
* Fernando de Santos Montalvo
* Marcos Zapata Bueno