
## Diagrama de clases del dominio.

![Diagrama de clases](imagenes/ModeladoDeClases.png)

El modelo de dominio representa la gestion de gastos de un usuario en sus diferentes tipos de cuentas.

En la parte superior de la imagen se encuentra **GestionGastos**, que sera el controlador/coordinador de la aplicacion. Esta nos permitiria hacer operaciones sobre la cuenta del usuario (usuarioActual). Ofrecera operaciones como registrarGasto, crearCuentaCompartida, importarGastos y obtener la cuenta global. El gestor usara repositorios para guardar y recuperar datos.

La entidad principal es **Usuario**, que se identifica por *nombre* y *nick*. Un usuario **tiene** (composición) múltiples **Cuenta**, lo que refleja que las cuentas pertenecen al usuario y no tienen sentido sin él. El usuario permite consultar sus gastos totales y saldo total agregando la información de sus cuentas.

# 3.  Un diagrama de interacción para la una de las historias de usuario (a elección del grupo)

# 4.  Breve explicación de la arquitectura de la aplicación y decisiones de diseño que se consideren de interés para la comprensión del trabajo.

# 5.  Explicación de los patrones de diseño usados.

# 6.  Breve manual de usuario (debe incluir capturas de las ventanas para apoyar las explicaciones)
