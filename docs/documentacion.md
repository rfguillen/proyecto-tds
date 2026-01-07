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

# 4.  Breve explicación de la arquitectura de la aplicación y decisiones de diseño que se consideren de interés para la comprensión del trabajo.

# 5.  Explicación de los patrones de diseño usados.

# 6.  Breve manual de usuario (debe incluir capturas de las ventanas para apoyar las explicaciones)
