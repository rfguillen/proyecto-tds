# Gestión de Gastos - Proyecto TDS

Aplicación de escritorio para gestionar gastos personales y cuentas de gasto compartidas. El proyecto está desarrollado con **Java**, **JavaFX**, **Maven** y persistencia en **JSON mediante jackson**

## Integrantes del Grupo:
- Rafael Guillen García. Grupo: 3.3. Correo: rafael.guilleng@um.es
- Andrés Martínez Marin. Grupo: 2.1. Correo: a.martinezmarin3@um.es
- Pedro García Espinosa. Grupo: 1.2. Correo: pedro.g.e@um.es

## Descripción:

La aplicación permite:
- Registrar, modificar y eliminar gastos.
- Clasificar gastos mediante categorías predefinidas o creadas por el usuario.
- Consultar los gastos en una tabla
- Filtrar movimientos por intervalo de fechas, lista de meses, lista de categorías e importes.
- Visualizar estadísticas de gasto agrupadas por categoría.
- Crear cuentas compartidas con reparto equitativo o porcentual.
- Calcular saldos pendientes entre los participantes de cuentas compartidas.
- Configurar alertas semanales o mensuales, generales o por categoría.
- Consultar el historial de notificaciones generadas por alertas.
- Importar gastos desde ficheros CSV y TXT.
- Gestionar gastos desde la interfaz gráfica y desde una terminal básica integrada.

## Arquitectura y tecnologías

- **JavaFX** para la interfaz gráfica.
- **Maven** para la gestión de dependencias.
- **Jackson** para guardar y cargar los datos en formato JSON.
- Arquitectura organizada en capas: vista, controlador de dominio, modelo y persistencia.
- Patrones usados: Singleton, Fachada, Repositorio, Estrategia, Adaptador y Método Factoría.

## Cómo ejecutar

Importando el proyecto `proyecto/` como proyecto Maven en Eclipse y ejecutar la clase principal:

**umu.tds.proyecto.App**

## Documentación

La documentación principal se encuentra en la carpeta `/docs`:

- [Documentacion](docs/documentacion.md)
- [Historias De Usuario](docs/Historias%20de%20Usuario.md)
- [Manual De Usuario](docs/Manual%20de%20Usuario.md)

## Persistencia

Los datos se almacenan en el fichero `gastos_datos.json`, que se ubica en el directorio personal del usuario. 