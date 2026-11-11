# Bitácora — Laboratorio 2

MSG Foundation: una ventana JavaFX sin tocar el núcleo
Ingeniería de Software I (603501) — Universidad de los Llanos

## Equipo

| Rol | Nombre | Usuario de GitHub |
|---|---|---|
| Integrador | _(completar)_ | willBetancourt |
| Formato y estilo | _(completar)_ | _(completar)_ |
| Vista | _(completar)_ | _(completar)_ |
| Controlador | _(completar)_ | _(completar)_ |

Rotación respecto al Laboratorio 1: Hipotecas → Integrador, Inversiones → Vista,
Registro → Controlador, Integrador → Formato y estilo.

## Contrato

Acordado en la Parte A, sin cambios respecto a la propuesta del enunciado.

| Elemento | Valor acordado | Lo usan |
|---|---|---|
| Carpeta de la vista y la hoja de estilos | `src/main/resources/co/edu/unillanos/msg/infrastructure/ui/` | Vista, Formato y estilo, Integrador |
| Archivo de la vista | `funds-view.fxml`, en esa carpeta | Vista, Integrador |
| Controlador declarado en la vista | `fx:controller="co.edu.unillanos.msg.infrastructure.ui.FundsController"` | Vista, Controlador |
| Etiqueta del total | un `Label` con `fx:id="totalLabel"` | Vista, Controlador |
| Acción del botón | `onAction="#onEstimate"` | Vista, Controlador |
| Constructor del controlador | `FundsController(EstimateFundsForWeek estimateFunds)` | Controlador, Integrador |
| Formato del dinero | `MoneyFormat.format(double)` devuelve, por ejemplo, `"$ 1160.00"` | Formato y estilo, Controlador |
| Hoja de estilos | `funds-view.css`, junto al FXML, con las clases `title` y `total` | Vista, Formato y estilo |

Un valor del contrato solo cambia si los cuatro lo acuerdan y lo corrigen en este archivo.

### Registro de cambios al contrato

| Fecha | Valor | Antes | Después | Quiénes lo acordaron |
|---|---|---|---|---|
| — | — | — | — | ninguno: el contrato se mantuvo como se propuso |

## Parte C — Guion de aceptación

_(se completa en la rama `docs/bitacora`)_

## Lo que realmente cambió

_(se completa en la rama `docs/bitacora`)_
