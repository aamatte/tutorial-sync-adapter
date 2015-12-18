# Tutorial SyncAdapter
La consultora Gartner estima que para 2016 el 40% de las aplicaciones
móviles usará servicios de la nube. Esto claramente obliga a los
desarrolladores a aprender y a utilizar las herramientas que nos
proporcionan las distintos plataformas para utilizar estos servicios de
manera eficiente. Si bien cada desarrollador puede implementar su propio
sistema de sincronización de datos, no hay necesidad de reinventar la
rueda. Android nos ofrece el SyncAdapter, un componente complejo pero poderoso que nos
ayuda a manejar y automatizar estas transferencias.

Beneficios
----------

1.  Ejecución automatizada: la sincronización estará definida de acuerdo
    a tu configuración y se ejecutará automáticamente de acuerdo a eso.
    Con esto podemos eliminar el botón de refresh.

2.  Revisión de conectividad: por ejemplo, si en algún momento no hay
    conexión a internet y corresponde una sincronización el framework se
    encargará de posponer la sincronización.

3.  Encolación de transferencias fallidas: si se está descargando un
    archivo y esto falla, se vuelve a intentar.

4.  Gasta menos batería: muchas veces el framework realizará la
    sincronización de más de una aplicación al mismo tiempo. De esta
    forma la antena celular se enciende con menor frecuencia.

5.  Centralización de la transferencia: la sincronización de los datos
    se realiza toda al mismo tiempo y en el mismo lugar.

6.  Manejo de cuenta y autenticación: si el usuario de tu aplicación
    requiere credenciales especiales se puede integrar el manejo de
    cuentas y autenticación en la transferencia.

Qué haremos
-----------

####

Durante el siguiente tutorial se utilizará una aplicación sencilla que
liste a los integrantes de un curso. Se podrá añadir estudiantes, tanto
en el servidor web como en la aplicación android, y se deberán mantener
sincronizados los datos a través de un sync adapter. No se entrará en
temas que escapan del objetivo del tutorial como el servidor, las vistas
o sobre como se realizan los requests. De todas formas se puede revisar 
el código fuente de la aplicación en este repositorio.

Qué se necesita
---------------

####

Para que nuestro SyncAdapter esté funcionando necesitamos cubrir e
implementar los siguientes componentes o funcionalidades:

1.  [Base de datos] (https://github.com/aamatte/EjemploSyncAdapter/wiki/Base-de-datos)

2.  [Content provider] (https://github.com/aamatte/EjemploSyncAdapter/wiki/Base-de-datos)

3.  [Authenticator](https://github.com/aamatte/EjemploSyncAdapter/wiki/Base-de-datos)

4.  [Clase SyncAdapter](https://github.com/aamatte/EjemploSyncAdapter/wiki/Base-de-datos)

5.  [Correr el SyncAdapter]((https://github.com/aamatte/EjemploSyncAdapter/wiki/Base-de-datos)
