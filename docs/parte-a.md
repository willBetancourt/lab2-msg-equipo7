# Parte A — Contrato y decisiones

Laboratorio 2 — MSG Foundation con JavaFX
Ingeniería de Software I (603501) — Universidad de los Llanos

## A1. El contrato

El equipo revisó la propuesta del enunciado y la confirmó sin cambios. Queda también
copiada en `BITACORA.md`.

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

## A2. Decisiones de diseño

### D1 — ¿`FundsController` es el Controller de GRASP? (Rol: Controlador) — *Tomada*

**No.** El Controller de GRASP de este sistema es **`EstimateFundsForWeek`**: es la clase
que recibe el evento del sistema «estimar los fondos de la semana», decide a quién
preguntarle cada dato (`Investment.weeklyReturn()`, `Mortgage.weeklyCoupleContribution()`,
`Mortgage.weeklyEscrow()`, `OperatingExpenses.weeklyAmount()`) y arma el resultado, sin
saber que existe una ventana.

`FundsController` es un **adaptador de JavaFX**: traduce un clic en una llamada a
`execute()` y escribe el resultado en un `Label`. Se llama «controlador» porque así lo
nombra el framework, no porque cumpla el patrón.

**Por qué importa la diferencia:** si se confunden, el cálculo termina en la clase que
conoce `Label` y `Button`. Ese día (a) el mismo cálculo deja de estar disponible para
`Main`, que no tiene ventana; (b) no se puede probar con JUnit sin arrancar el toolkit de
JavaFX; y (c) el sistema queda con dos respuestas posibles para la misma pregunta de
negocio. Hoy `Main` y la ventana dan 1160.00 porque los dos llaman al mismo caso de uso.

### D2 — `FundsController` recibe el caso de uso por su constructor, desde `App` (Rol: Integrador) — *Tomada*

**Qué se pierde si el controlador hiciera `new EstimateFundsForWeek(...)` con sus propios
repositorios:**

1. **El resultado deja de ser el del sistema.** Los repositorios que creara el controlador
   serían objetos nuevos y vacíos: sin las dos hipotecas ni las dos inversiones que cargan
   `Main` y `App`. La ventana mostraría `$ -750.00` (solo el gasto operativo semanal), no
   `$ 1160.00`. Es exactamente lo que le pasa al código de la Parte D.
2. **Se pierde la raíz de composición única.** Hoy solo `Main` (consola) y `App` (ventana)
   saben qué implementación concreta se usa. Si el controlador construye repositorios,
   el cableado queda repartido en tres lugares y cada uno puede quedar distinto.
3. **Se invierte la dirección de la dependencia.** `infrastructure.ui` pasaría a depender de
   `infrastructure.InMemory*Repository`, es decir, de otra pieza externa. Cuando los datos
   pasen a H2 habría que abrir la ventana para cambiarlos, aunque la ventana no tenga nada
   que ver con dónde se guardan los datos.
4. **Se pierde la posibilidad de probar el controlador** con un repositorio falso, porque ya
   no se le podría pasar nada.

### D3 — `MoneyFormat` va en `infrastructure.ui` y no en `domain` (Rol: Formato y estilo) — *Tomada*

**Qué tiene que ver `$ 1160.00` con la ventana y no con las reglas del negocio:** el signo
`$`, el espacio, los dos decimales y el punto como separador decimal son decisiones sobre
**cómo se le muestra** un número a una persona. Ninguna regla del caso MSG (RN2 a RN7)
dice cómo se escribe el dinero: `EstimateFundsForWeek.execute()` devuelve un `double`, y
ese `double` vale lo mismo en la consola, en la ventana o en un reporte.

La prueba está en qué los hace cambiar: si mañana la ventana muestra `COP 1.160,00`, no
cambia ninguna regla del negocio; si cambia el porcentaje máximo del ingreso de la pareja,
no cambia el formato. Son dos razones de cambio distintas, así que van en dos capas
distintas. Poner `MoneyFormat` en `domain` haría que el núcleo —que hoy no conoce ninguna
capa externa— dependiera de una decisión de presentación.

El detalle de `Locale.ROOT` refuerza lo mismo: sin él, un Windows configurado en español
escribiría `1160,00` y la prueba pasaría en una máquina y fallaría en otra.

### D4 — Los nombres del contrato (`fx:id`, `onAction`) se acuerdan antes de escribir la vista (Rol: Vista) — *Tomada*

**Qué pasaría si la vista y el controlador los eligieran cada uno por su cuenta:** nada,
hasta que fuera tarde. El enlace entre el FXML y el controlador **no lo revisa el
compilador**: se resuelve por nombre cuando el `FXMLLoader` carga el archivo, ya con la
aplicación corriendo.

- Si la vista dice `fx:id="resultado"` y el controlador declara `totalLabel`, el campo
  queda en `null` y el primer clic revienta con `NullPointerException`.
- Si la vista dice `onAction="#calcular"` y el controlador tiene `onEstimate()`, la carga
  falla con `LoadException`.
- Si la vista dice `styleClass="titulo"` y el CSS define `.title`, no falla nada: el estilo
  simplemente no se aplica y nadie se entera hasta la demostración.

Los tres errores aparecerían solo en la Parte C, cuando las cuatro piezas ya están en
`main`, y bloquearían al mismo tiempo a la Vista y al Controlador. Acordar los nombres en
papel cuesta cinco minutos y permite que los cuatro programen en paralelo sin esperarse.

### D5 — ¿En qué orden se fusionan los cinco PR? (Rol: Integrador) — *Abierta → resuelta*

| # | Rama | La dependencia que obliga ese lugar |
|---|---|---|
| 1 | `feature/javafx-setup` | Todo lo que importa `javafx` solo compila cuando JavaFX está en el `pom.xml`. Sin este PR, ni el controlador ni `App` compilan. |
| 2 | `feature/format` | No depende de nadie (`MoneyFormat` es Java puro), pero el Controlador la necesita: sin ella, `cannot find symbol: MoneyFormat`. |
| 3 | `feature/view` y `feature/controller` | En cualquier orden: no se tocan. Uno escribe un `.fxml` en `resources` y el otro un `.java` en `java`; se enlazan por nombre en tiempo de carga, no de compilación. |
| 4 | `feature/app` | `App.start` nombra a `FundsController` y carga `funds-view.fxml`: necesita los dos PR anteriores en `main`. |
| 5 | `docs/bitacora` | Va al final porque registra el resultado del guion de aceptación, que solo se puede correr con todo lo anterior fusionado. |

### D6 — Predicción: ¿qué archivos de `domain` y de `application` van a cambiar? (Rol: Equipo) — *Abierta → verificada*

**Predicción del equipo: ninguno. La lista es vacía.**

Razón: la ventana no agrega ninguna regla nueva. Lo único que necesita es el número que
`EstimateFundsForWeek.execute()` ya devuelve, y el caso de uso ya recibe sus repositorios
por constructor, así que `App` puede cablearlo igual que `Main` sin modificarlo.

**Verificación al final de la Parte C:**

```
git diff --stat base main -- src/main/java/co/edu/unillanos/msg/domain src/main/java/co/edu/unillanos/msg/application
```

No imprimió nada. La predicción se cumplió: cero archivos de `domain` y cero de
`application`. Los 9 archivos que cambiaron están todos en la capa externa y en la
documentación (ver `BITACORA.md`, sección «Lo que realmente cambió»).
