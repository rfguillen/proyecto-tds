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
- **Entonces** el sistema guarda el gasto en la cuenta activa.

**2. Registro con nueva categoría**  
- **Dado** que el usuario quiere usar una categoría que no existe,  
- **Cuando** crea una nueva categoría desde el formulario de gasto,  
- **Entonces** el sistema la añade al listado de categorías y permite asociarla al gasto.

---

## Historia de Usuario 2: Modificar y eliminar gastos
**Como** usuario,  
**Quiero** editar o borrar gastos registrados,  
**Para** corregir errores o eliminar información que ya no es válida.

### Criterios de aceptación

**1. Edición de gasto**  
- **Dado** un gasto existente,  
- **Cuando** el usuario modifica sus datos y guarda,  
- **Entonces** el sistema actualiza el gasto.

**2. Eliminación de gasto**  
- **Dado** un gasto seleccionado,  
- **Cuando** el usuario confirma la acción de borrado,  
- **Entonces** el sistema elimina el gasto de la cuenta.

---

# Actividad: Visualización y Análisis

## Historia de Usuario 3: Listar y filtrar gastos
**Como** usuario,  
**Quiero** ver un listado de gastos filtrado por distintos criterios,  
**Para** analizar periodos, meses o categorías concretas.

### Criterios de aceptación

**1. Aplicación de varios filtros**  
- **Dado** un listado de gastos,  
- **Cuando** el usuario aplica una combinación de filtros,  
- **Entonces** el sistema muestra solo los gastos que cumplen todos los filtros aplicados.

**2. Limpieza de filtros**
- **Dado** que hay filtros activos,
- **Cuando** el usuario pulse el botón de limpiar filtros,
- **Entonces** el sistema vuelve a mostrar todos los movimientos de la cuenta activa.

---

## Historia de Usuario 4: Visualización gráfica
**Como** usuario,  
**Quiero** ver gráficos de mis gastos agrupados por categoría,  
**Para** entender visualmente la distribución de mis gastos.

### Criterios de aceptación

**1. Generación de estadísticas**  
- **Dado** un conjunto de gastos registrados,  
- **Cuando** el usuario accede a la vista de estadísticas,  
- **Entonces** el sistema muestra un gráfico que representa el total gastado agrupado por categorías.

---

# Actividad: Sistema de Alertas

## Historia de Usuario 5: Configurar alertas
**Como** usuario,  
**Quiero** definir límites de gastos semanales o mensuales, generales o por categoría,  
**Para** controlar automáticamente si supero mis límites de gasto.

### Criterios de aceptación

**1. Definición de alerta**  
- **Dado** el panel de alertas,  
- **Cuando** el usuario configura periodo, umbral y opcionalmente categoría,
- **Entonces** el sistema guarda esta alerta para validarla en futuros registros.

---

## Historia de Usuario 6: Consultar notificaciones
**Como** usuario,  
**Quiero** recibir avisos cuando supero un límite y poder consultar el historial de avisos,  
**Para** estar informado al momento y revisar excesos pasados.

### Criterios de aceptación

**1. Generación de notificación**  
- **Dado** que existe una alerta configurada,  
- **Cuando** se añade un gasto que hace que el total acumulado supere el umbral,  
- **Entonces** el sistema genera una notificación visible para el usuario.

**2. Historial de notificaciones**  
- **Dado** que  existen notificaciones generadas,
- **Cuando** el usuario consulta el panel de notificaciones,  
- **Entonces** puede ver las notificaciones anteriores.

---

# Actividad: Cuentas Compartidas

## Historia de Usuario 7: Crear cuenta compartida
**Como** usuario,  
**Quiero** crear una cuenta compartida con varios participantes y un reparto de gastos,
**Para** gestionar gastos comunes con otras personas.

### Criterios de aceptación

**1. Reparto equitativo**  
- **Dado** que se crea una cuenta compartida sin porcentajes concretos,
- **Cuando** el usuario añade participantes,
- **Entonces** el sistema reparte el gasto equitativamente entre ellos.

**2. Reparto porcentual**  
- **Dado** que se crea una cuenta compartida con porcentajes,  
- **Cuando** la suma de porcentajes no es 100%,  
- **Entonces** el sistema impide guardar la cuenta.

**3. Lista de participantes fija**
- **Dado** que una cuenta compartida ya ha sido creada,
- **Cuando** se consulta su lista de participantes,
- **Entonces** el sistema no permite modificar directamente esa lista desde la cuenta.

---

## Historia de Usuario 8: Registrar gastos compartidos y consultar saldos
**Como** participante de una cuenta compartida,  
**Quiero** registrar gastos pagados por un participante y ver los saldos resultantes,  
**Para** saber quién debe dinero a quién y a quién se le debe.

### Criterios de aceptación

**1. Cálculo de saldos**  
- **Dado** una cuenta compartida con participantes y reparto definido,  
- **Cuando** se registra un gasto pagado por un participante,  
- **Entonces** el sistema actualiza el saldo de todos los participantes.

---

# Actividad: Importación de Datos

## Historia de Usuario 9: Importar gastos desde fichero
**Como** usuario,  
**Quiero** cargar un fichero externo con datos de gasto,  
**Para** evitar la introducción manual de movimientos.

### Criterios de aceptación

**1. Importación CSV/TXT**  
- **Dado** un fichero CSV o TXT con formato compatible,  
- **Cuando** el usuario lo selecciona desde la vista de importación,  
- **Entonces** el sistema procesa el fichero y registra los gastos correspondientes.

**2. Importación extensible**
- **Dado** que el sistema usa una factoría de importadores,
- **Cuando** se quiera añadir un nuevo formato,
- **Entonces** se podrá crear un nuevo adaptador sin modificar la lógica principal de gestión de gastos.

---

# Actividad: Interfaz de Línea de Comandos

## Historia de Usuario 10: Gestión mediante terminal
**Como** usuario,  
**Quiero** realizar operaciones de registro, modificación y borrado mediante comandos,  
**Para** utilizar la aplicación sin depender únicamente de la interfaz gráfica.

### Criterios de aceptación

**1. Uso por comandos**  
- **Dado** que la aplicación se utiliza por consola,  
- **Cuando** el usuario introduce un comando válido de creación, edición o borrado,  
- **Entonces** el sistema ejecuta la acción sobre la cuenta activa y actualiza los datos.
