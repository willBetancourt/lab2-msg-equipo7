# Parte D — Auditoría del método generado por IA

Laboratorio 2 — MSG Foundation con JavaFX
Ingeniería de Software I (603501) — Universidad de los Llanos

> La Parte D es **individual y en papel**. Esta hoja queda en el repositorio como
> referencia de lo que se discutió; cada estudiante entrega la suya escrita a mano.

## El código auditado

Un estudiante le pidió a un asistente de IA «el método del botón Calcular fondos para la
ventana de la MSG» y obtuvo esto. **Compila sin errores.**

```java
@FXML
private void onEstimate() {
    MortgageRepository mortgages = new InMemoryMortgageRepository();
    InvestmentRepository investments = new InMemoryInvestmentRepository();
    double total = 0;
    for (Investment i : investments.findAll()) {
        total += i.weeklyReturn();
    }
    for (Mortgage m : mortgages.findAll()) {
        total += m.weeklyCoupleContribution();
        total -= m.weeklyEscrow();
    }
    total -= 39000.0 / 52;
    totalLabel.setText("$ " + total);
}
```

## 1. Cuatro problemas

### P1 — El controlador construye sus propios repositorios

**Líneas 3 y 4:** `new InMemoryMortgageRepository()` y `new InMemoryInvestmentRepository()`.

**Qué viola:** la inyección de dependencias y la raíz de composición única (decisión D2
del laboratorio). Un adaptador de interfaz no decide de dónde salen los datos; los recibe.
De paso, `infrastructure.ui` queda dependiendo de las implementaciones concretas de
`infrastructure`, en vez de depender solo del caso de uso.

**Consecuencia:** esos repositorios nacen vacíos, así que la ventana no muestra el estado
del sistema sino el de dos objetos recién creados que nadie más conoce (ver la pregunta 2).
Para el equipo: el día que los datos pasen a H2 hay que abrir y modificar la ventana,
aunque la ventana no tenga nada que ver con dónde se guardan los datos.

### P2 — La fórmula del caso de uso está copiada dentro de la ventana

**Líneas 5 a 12:** los dos `for` que suman rendimientos, aportes de las parejas y escrows.

**Qué viola:** Expert / separación de capas, y la regla explícita del laboratorio «ningún
archivo de `infrastructure.ui` contiene una fórmula». Ese cálculo ya existe, probado, en
`EstimateFundsForWeek.execute()`.

**Consecuencia:** el sistema pasa a tener dos verdades para la misma pregunta. Cuando
cambie una regla de negocio, alguien actualizará el caso de uso (que tiene pruebas) y la
consola dará un número mientras la ventana da otro. Ninguna prueba de JUnit cubre esta
copia, porque vive en una clase que necesita el toolkit de JavaFX para ejecutarse.

### P3 — Número mágico con la regla de los gastos operativos

**Línea 13:** `total -= 39000.0 / 52;`.

**Qué viola:** el dato (39000) y la regla (RN7: el gasto anual se reparte entre las 52
semanas) quedan escritos a mano en la capa de presentación, en vez de pedírselos a
`OperatingExpensesRepository` y a `OperatingExpenses.weeklyAmount()`. Es justo lo que el
laboratorio prohíbe con «ni 52, ni 0.28».

**Consecuencia:** si la fundación cambia su presupuesto anual hay que recompilar la ventana,
y el valor que muestra la pantalla puede contradecir al que usa la consola. Además nadie
que lea `OperatingExpenses` se enterará de que existe otra copia del número.

### P4 — El dinero se arma concatenando, no con `MoneyFormat`

**Línea 14:** `totalLabel.setText("$ " + total);`.

**Qué viola:** el contrato acordado en la Parte A (`MoneyFormat.format(double)`) y la
responsabilidad única del formato.

**Consecuencia:** la concatenación de un `double` no da dos decimales. El usuario ve
`$ -750.0`, y con otros datos puede ver `$ 1160.000000000001` o un número en notación
científica. Como no pasa por `Locale.ROOT`, el resultado además depende de la configuración
regional de la máquina. La prueba `MoneyFormatTest` existe y no protege nada, porque este
código no la usa.

## 2. ¿Qué número ve el estudiante y por qué no es 1160.00?

Ve **`$ -750.0`**.

Los dos repositorios se crean dentro del método y están **vacíos**: los datos de ejemplo
(las hipotecas `M-001` y `M-002` y las inversiones `I-01` e `I-02`) los cargan `Main` y
`App` en *otros* objetos repositorio, que este método nunca ve. Entonces los dos `for` no
iteran nada y `total` sigue en `0`. Lo único que se ejecuta de verdad es la línea del gasto
operativo:

```
total = 0 - 39000.0 / 52 = -750.0
```

Y como el texto se arma con `"$ " + total`, el `double` se escribe con un solo decimal:
`$ -750.0`, no `$ -750.00`.

No es 1160.00 porque al cálculo solo entró el gasto operativo escrito a mano. El valor
correcto necesita los datos del sistema:

```
1500.00 (rendimiento semanal de las inversiones)
+ 520.00 (aporte semanal de las dos parejas)
-  750.00 (gasto operativo semanal)
-  110.00 (impuesto y seguro semanal de las dos hipotecas)
= 1160.00
```

## 3. El método reescrito en cinco líneas o menos

```java
@FXML
private void onEstimate() {
    totalLabel.setText(MoneyFormat.format(estimateFunds.execute()));
}
```

Con el campo que ya exige el contrato:

```java
private final EstimateFundsForWeek estimateFunds;

public FundsController(EstimateFundsForWeek estimateFunds) {
    this.estimateFunds = estimateFunds;
}
```

El método quedó en **tres líneas** y no contiene ningún `new`, ninguna fórmula, ningún
número del negocio y ningún formato: solo traduce un clic en una llamada al caso de uso y
el resultado en texto.

## Para el cierre

**¿Por qué no basta con que el código compile?** Compilar solo dice que la sintaxis y los
tipos están bien. No dice si el número es el correcto (aquí no lo es), si la pieza está en
la capa que le toca (no lo está), ni si respeta el contrato que el equipo acordó (no lo
respeta). El compilador no conoce las reglas del negocio ni el diseño.

**¿Quién responde por lo que entra al repositorio?** Quien abre el pull request y quien lo
aprueba. La IA no firma commits: el historial de GitHub muestra el nombre de una persona.
Por eso la revisión pide verificar que `mvn test` pasa, que el PR no toca `domain` ni
`application` y que cada archivo está en la ruta de la tabla.
