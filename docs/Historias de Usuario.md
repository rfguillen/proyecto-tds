# Historias de Usuario  
**Actividad:** Gestión de Gastos Personales

---

## Historia de Usuario 1: Registro de gastos
**Como** usuario,  
**Quiero** registrar un gasto indicando cantidad, fecha, concepto y categoría,  
**Para** guardar un registro de mis movimientos financieros.

### Criterios de aceptación

**1. Registro con categoría existente**  
- **Dado** que el usuario accede al formulario de creación,  
- **Cuando** introduce cantidad, fecha, concepto y selecciona una categoría existente,  
- **Entonces** el sistema guarda el gasto y confirma la operación.

**2. Registro con nueva categoría**  
- **Dado** que el usuario quiere usar una categoría que no existe,  
- **Cuando** introduce un nombre de categoría nuevo al crear el gasto,  
- **Entonces** el sistema crea la categoría, la asocia al gasto y guarda ambos registros.

---

## Historia de Usuario 2: Modificar y eliminar gastos
**Como** usuario,  
**Quiero** editar o borrar gastos que ya he introducido,  
**Para** corregir errores o eliminar información que ya no es válida.

### Criterios de aceptación

**1. Edición de gasto**  
- **Dado** un gasto existente,  
- **Cuando** el usuario modifica uno de sus campos (fecha, cantidad, concepto, etc.) y guarda,  
- **Entonces** el sistema actualiza la información.

**2. Eliminación de gasto**  
- **Dado** un gasto seleccionado,  
- **Cuando** el usuario confirma la acción de borrado,  
- **Entonces** el gasto se elimina permanentemente del sistema.

---

# Actividad: Visualización y Análisis

## Historia de Usuario 3: Listar y filtrar gastos
**Como** usuario,  
**Quiero** ver un listado de gastos filtrado por distintos criterios,  
**Para** analizar periodos concretos o tipos de gasto específicos.

### Criterios de aceptación

**1. Aplicación de varios filtros**  
- **Dado** un listado de gastos,  
- **Cuando** el usuario aplica una combinación de filtros,  
- **Entonces** el listado se actualiza mostrando solo los gastos que cumplen todas las condiciones simultáneamente.

---

## Historia de Usuario 4: Visualización gráfica
**Como** usuario,  
**Quiero** ver gráficos de barras y circulares de mis gastos,  
**Para** entender visualmente la distribución de mis gastos por categorías.

### Criterios de aceptación

**1. Generación de gráficos**  
- **Dado** un conjunto de gastos registrados,  
- **Cuando** el usuario accede a la vista de estadísticas,  
- **Entonces** se muestra un gráfico que representa el total gastado agrupado por categorías.

---

## Historia de Usuario 5: Visualización en calendario
**Como** usuario,  
**Quiero** ver mis gastos ubicados en un calendario,  
**Para** tener una visión temporal de cuándo realizo gastos.

### Criterios de aceptación

**1. Vista de calendario**  
- **Dado** que existen gastos con fechas asignadas,  
- **Cuando** el usuario abre la vista de calendario,  
- **Entonces** los gastos aparecen representados en el día correspondiente.

---

# Actividad: Sistema de Alertas

## Historia de Usuario 6: Configurar alertas
**Como** usuario,  
**Quiero** definir límites de gastos temporales y opcionalmente por categoría,  
**Para** que el sistema vigile mis gastos automáticamente.

### Criterios de aceptación

**1. Definición de alerta**  
- **Dado** el panel de alertas,  
- **Cuando** el usuario configura un límite de dinero para un periodo y opcionalmente una categoría,  
- **Entonces** el sistema guarda esta alerta para validarla en futuros registros.

---

## Historia de Usuario 7: Gestión de notificaciones
**Como** usuario,  
**Quiero** recibir avisos cuando supero un límite y poder consultar el historial de avisos,  
**Para** estar informado al momento y revisar excesos pasados.

### Criterios de aceptación

**1. Salto de notificación**  
- **Dado** una alerta configurada,  
- **Cuando** se añade un gasto que hace que el total acumulado supere el límite,  
- **Entonces** el sistema genera una notificación visible para el usuario.

**2. Historial de notificaciones**  
- **Dado** que se han generado alertas en el pasado,  
- **Cuando** el usuario accede al buzón de notificaciones,  
- **Entonces** puede ver una lista de todas las alertas generadas anteriormente.

---

# Actividad: Cuentas Compartidas

## Historia de Usuario 8: Crear grupo de gasto
**Como** usuario,  
**Quiero** crear un grupo de personas y definir cómo se reparten los gastos,  
**Para** gestionar finanzas compartidas.

### Criterios de aceptación

**1. Validación de porcentajes**  
- **Dado** que se crea un grupo con reparto porcentual,  
- **Cuando** se asignan las cuotas de participación a los miembros,  
- **Entonces** el sistema impide guardar el grupo si la suma de los porcentajes no es 100.

**2. Inmutabilidad de miembros**  
- **Dado** un grupo ya creado,  
- **Cuando** el usuario intenta modificar la lista de personas del grupo,  
- **Entonces** el sistema no debe permitir la operación.

---

## Historia de Usuario 9: Gestión de gastos compartidos y saldos
**Como** miembro de un grupo,  
**Quiero** registrar gastos pagados por un miembro y ver los saldos resultantes,  
**Para** saber quién debe dinero a quién.

### Criterios de aceptación

**1. Cálculo de saldos**  
- **Dado** un grupo con reglas de reparto definidas,  
- **Cuando** se registra un gasto pagado por un miembro específico,  
- **Entonces** el sistema actualiza el saldo de todos los miembros:  
  - positivo para el pagador (le deben dinero),  
  - negativo para el resto (lo que deben).

---

# Actividad: Importación de Datos

## Historia de Usuario 10: Importar ficheros de gastos
**Como** usuario,  
**Quiero** cargar un fichero de texto con un listado de gastos,  
**Para** evitar la introducción manual de datos bancarios.

### Criterios de aceptación

**1. Importación correcta**  
- **Dado** un fichero externo en formato compatible,  
- **Cuando** el usuario ejecuta la importación,  
- **Entonces** el sistema procesa el fichero y genera automáticamente los registros de gasto correspondientes.

---

# Actividad: Interfaz de Línea de Comandos

## Historia de Usuario 11: Gestión vía consola
**Como** usuario,  
**Quiero** realizar operaciones de registro, modificación y borrado mediante comandos,  
**Para** utilizar la aplicación sin usar la interfaz gráfica.

### Criterios de aceptación

**1. Uso por comandos**  
- **Dado** que la aplicación se utiliza por consola,  
- **Cuando** el usuario introduce un comando válido de creación, edición o borrado,  
- **Entonces** el sistema ejecuta la acción sobre la base de datos y muestra el resultado en la salida estándar.
