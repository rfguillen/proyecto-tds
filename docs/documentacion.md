# <ins> **DOCUMENTACION**</ins>
Esta documentación describe el diseño y funcionamiento de la aplicación de gestión de gastos, incluyendo el modelo de dominio, las historias de usuario, estructura del programa y un manual de usuario.


## Diagrama de clases del dominio.

![Diagrama de clases](imagenes/ModeladoDeClases2.png)


En nuestro programa se utilizará el controlador **GestionGastos** para gestionar de forma correcta la aplicación y sus datos. Este contendrá al usuario actual y ofrecerá las funciones principales del programa, como registrar, modificar y eliminar movimientos, así como crear cuentas compartidas. También utilizará un importador, capaz de importar gastos desde un archivo. Para la correcta organización y gestión de la información se utilizarán repositorios tanto para los usuarios como para las categorías.

La clase **Usuario** contendrá la información del usuario. Tendrá su nombre, la información de su cuenta personal y una lista de cuentas a las que pertenece, pudiendo ser estas tanto personales como compartidas. Esta clase permitirá obtener los gastos y el saldo de las cuentas.

**Cuenta** es la clase abstracta usada en este programa para representar los datos de la cuenta del usuario, como el nombre, la fecha de creación, los movimientos y el saldo. Contendrá funciones para ingresar o retirar dinero y para consultar el saldo. Las clases **CuentaPersonal** y **CuentaCompartida** son clases heredadas de **Cuenta** y representan cuentas individuales y cuentas con varios participantes, respectivamente. Esta última estará compuesta por más de un **Participante**, los cuales, según el reparto de gastos, pagarán un porcentaje de cada pago.

Todas las clases **Cuenta** contienen una colección de **Movimiento**. Estos representan un gasto o un ingreso y poseen los atributos concepto, cantidad, fecha y categoría. Las **Categoría** sirven para clasificar los movimientos y facilitar su análisis. En las cuentas compartidas, se pueden tener movimientos de tipo **GastoCompartido**, que además de los datos anteriores indican qué **Participante** ha realizado el pago. Esto es lo que permite realizar los cálculos de cuánto se debe a cada participante o cuánto debe cada participante dentro de la cuenta.

## Diagrama de interacción para Registrar Gasto.
![Diagrama de clases](imagenes/DiagramaDeInteraccion.png)

Este diagrama de interacción representa el proceso de registro de un gasto. El usuario introduce los datos desde la vista de registro, que valida la información y delega la operación en **GestionGastos**. El sistema confirma la categoría del gasto, creándola si no existe, y registra el movimiento en la cuenta correspondiente. En el caso de una cuenta compartida, se recalculan los saldos de los participantes. Finalmente, se guarda el estado actualizado del usuario y, si se supera algún límite configurado, se muestra una notificación al usuario antes de confirmar el registro del gasto.

## Arquitectura y decisiones de diseño

La aplicación sigue una arquitectura basada en la separación entre la interfaz de usuario, la lógica de negocio y el acceso a datos. La interfaz gráfica se encarga de la interacción con el usuario y de realizar validaciones básicas, delegando siempre las operaciones principales en la capa de control.

La clase **GestionGastos**, actuara como principal controlador del sistema. Esta clase coordina los casos de uso más importantes, como el registro, modificación y eliminación de gastos, la creación de cuentas compartidas, la importación de datos y la comprobación de alertas. Además, mantiene el usuario actual y evita que la interfaz acceda directamente a las clases del dominio.

El acceso y persistencia de la información se realiza mediante repositorios, como **RepositorioUsuarios** y **RepositorioCategorias**, con ellos podremos gestionar correctamente los datos y facilitar posibles cambios futuros sin afectar al resto de la aplicación. La importación de gastos se gestiona a través de un componente **Importador**, desacoplado de la lógica principal.

Como decisiones de diseño relevantes, se ha utilizado el patrón *Singleton* en **GestionGastos** para garantizar una única instancia de control. También se ha optado por el uso de herencia para diferenciar entre **CuentaPersonal** y **CuentaCompartida**, permitiendo reutilizar comportamiento de **Cuenta** y poder diferenciar cuando existen varios participantes. El sistema de alertas se integra en el flujo de registro de gastos, permitiendo detectar automáticamente cuando se superan límites definidos y generar notificaciones al usuario.


# 5.  Explicación de los patrones de diseño usados.

# 6.  Breve manual de usuario (debe incluir capturas de las ventanas para apoyar las explicaciones)
