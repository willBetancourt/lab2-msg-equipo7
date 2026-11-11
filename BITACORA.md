# Bitácora — Laboratorio 2

MSG Foundation: una ventana JavaFX sin tocar el núcleo
Ingeniería de Software I (603501) — Universidad de los Llanos

## Equipo

| Rol | Nombre | Código estudiantil | Usuario de GitHub | Su PR lo revisa |
|---|---|---|---|---|
| Integrador | Brayan Zuluaga Torres | 160005243 | B5F6Z7 | Controlador |
| Formato y estilo | Javier Vesga | 160005040 | JS150 | Vista |
| Vista | Camilo Rico | 160005131 | Camilo-Rico-Acosta | Formato y estilo |
| Controlador | Willy Betancourt | 160005028 | willBetancourt | Integrador |

Los roles rotaron respecto al Laboratorio 1 y el Integrador no es quien ya lo fue.

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

## Orden de fusión de los PR

| # | PR | Rama | Estado |
|---|---|---|---|
| 1 | [#1](https://github.com/willBetancourt/lab2-msg-equipo7/pull/1) | `feature/javafx-setup` | fusionado |
| 2 | [#2](https://github.com/willBetancourt/lab2-msg-equipo7/pull/2) | `feature/format` | fusionado |
| 3 | [#3](https://github.com/willBetancourt/lab2-msg-equipo7/pull/3) | `feature/view` | fusionado |
| 4 | [#4](https://github.com/willBetancourt/lab2-msg-equipo7/pull/4) | `feature/controller` | fusionado |
| 5 | [#5](https://github.com/willBetancourt/lab2-msg-equipo7/pull/5) | `feature/app` | fusionado |
| 6 | [#6](https://github.com/willBetancourt/lab2-msg-equipo7/pull/6) | `docs/bitacora` | fusionado |
| 7 | [#7](https://github.com/willBetancourt/lab2-msg-equipo7/pull/7) | `docs/equipo` | este PR: tabla del equipo |

## Parte C — Guion de aceptación

Ejecutado sobre `main` actualizado, con los cinco PR de código fusionados.
`mvn test` en `main`: **BUILD SUCCESS**, `Tests run: 14, Failures: 0, Errors: 0, Skipped: 0`
(las 11 pruebas del Laboratorio 1 más las 3 de `MoneyFormatTest`).

| Paso | Acción | Resultado esperado | Resultado obtenido |
|---|---|---|---|
| 1 | `mvn clean javafx:run` | La salida no dice `skip non existing resourceDirectory` y abre la ventana con el título, el botón y la etiqueta vacía | ✅ La salida dice `Copying 3 resources from src\main\resources to target\classes`. Abre la ventana con el título, el botón **Calcular fondos** y la etiqueta vacía |
| 2 | Ejecutar `Launcher` desde el IDE | Abre la misma ventana | ✅ Abre la misma ventana (captura `docs/capturas/paso2-ventana-desde-launcher.png`) |
| 3 | Pulsar **Calcular fondos** | La etiqueta muestra `$ 1160.00`, grande y en negrita | ✅ Muestra `$ 1160.00` en 28 px y negrita: el CSS se aplicó (captura `docs/capturas/paso3-ventana-1160.png`) |
| 4 | Ejecutar `Main` desde el IDE | La consola sigue imprimiendo `1160.00` | ✅ Imprime `Fondos disponibles para la semana: 1160,00`. El valor es el mismo; la coma viene de `System.out.printf` en `Main`, que usa la configuración regional de la máquina (es-CO). `MoneyFormat` usa `Locale.ROOT` y por eso la ventana sí escribe `1160.00`. `Main` no se tocó |
| 5 | `git diff --stat base main -- .../domain .../application` | No imprime nada: el núcleo no cambió | ✅ No imprimió nada (captura `docs/capturas/paso5-nucleo-sin-cambios.png`) |
| 6 | Abrir `FundsController` | No hay `new` de casos de uso ni de repositorios, ni 52, ni 0.28 | ✅ La clase solo tiene el campo `@FXML`, el campo del caso de uso, el constructor y `onEstimate()` |

## Lo que realmente cambió

`git diff --name-only base main` — 9 archivos:

| Archivo | Capa | Rol |
|---|---|---|
| `pom.xml` | build | Integrador |
| `src/main/java/co/edu/unillanos/msg/App.java` | raíz de composición | Integrador |
| `src/main/java/co/edu/unillanos/msg/Launcher.java` | raíz de composición | Integrador |
| `src/main/java/co/edu/unillanos/msg/infrastructure/ui/MoneyFormat.java` | infraestructura (UI) | Formato y estilo |
| `src/test/java/co/edu/unillanos/msg/infrastructure/ui/MoneyFormatTest.java` | pruebas | Formato y estilo |
| `src/main/resources/co/edu/unillanos/msg/infrastructure/ui/funds-view.css` | infraestructura (UI) | Formato y estilo |
| `src/main/resources/co/edu/unillanos/msg/infrastructure/ui/funds-view.fxml` | infraestructura (UI) | Vista |
| `src/main/java/co/edu/unillanos/msg/infrastructure/ui/FundsController.java` | infraestructura (UI) | Controlador |
| `BITACORA.md` | documentación | Integrador |

**Comparación con la predicción D6.** El equipo predijo que **ningún** archivo de `domain`
ni de `application` iba a cambiar. Se cumplió: de los 9 archivos, cero están en `domain` y
cero en `application`; los `InMemory*Repository` y `Main` tampoco se tocaron. Todo lo nuevo
quedó en la capa externa (`infrastructure.ui`), en la raíz de composición (`App`,
`Launcher`), en el `pom.xml` y en la documentación.

Que la lista haya salido vacía dice algo del Laboratorio 1: el núcleo ya exponía lo que la
ventana necesitaba (`EstimateFundsForWeek.execute()`) y ya recibía sus repositorios por
constructor. Por eso agregar un framework entero costó 4 archivos de código nuevos y
ninguna modificación al núcleo.

## Cierre

1. **¿Cuántos archivos de `domain` y `application` se tocaron?** Cero. El diseño del
   Laboratorio 1 puso las reglas donde nadie de afuera necesita entrar: el caso de uso
   depende de puertos (interfaces), no de implementaciones, y devuelve un `double` que
   sirve igual para una consola o una ventana.
2. **Si los datos pasan a una base de datos H2, ¿qué archivos cambian?** Aparecen clases
   nuevas `H2MortgageRepository`, `H2InvestmentRepository` y `H2OperatingExpensesRepository`
   en `infrastructure`, y cambian las dos raíces de composición (`Main` y `App`) para
   cablearlas. `domain`, `application`, `FundsController`, el FXML y el CSS no cambian.
3. **Hoy `onEstimate()` no maneja errores. Con H2, ¿qué puede fallar?** La conexión, la
   consulta, un dato corrupto. Quien debe enterarse es el usuario, pero la decisión de
   *cómo* mostrárselo (un diálogo, una etiqueta en rojo, un texto en la ventana) es de la
   capa de presentación: el caso de uso se limita a dejar subir una excepción con sentido
   de negocio, y `FundsController` la traduce a algo que la persona entienda.
4. **La IA escribió código que compila. ¿Por qué no basta?** Porque compilar no dice si el
   número es correcto, si la pieza está en la capa que le toca ni si respeta el contrato del
   equipo. Ver `docs/parte-d-auditoria.md`: ese método compila y muestra `$ -750.0`. Responde
   quien abre el PR y quien lo aprueba; el historial de GitHub lleva su nombre, no el de la IA.

## Documentos de la entrega

- `docs/parte-a.md` — contrato acordado y decisiones D1–D6
- `docs/parte-d-auditoria.md` — auditoría del método generado por IA
- `docs/capturas/` — evidencia de los pasos 2, 3 y 5 del guion de aceptación
