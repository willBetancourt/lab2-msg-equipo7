# Laboratorio 2 — MSG Foundation con JavaFX

Ingeniería de Software I (603501) — Universidad de los Llanos

Una ventana JavaFX sobre el núcleo del Laboratorio 1, **sin tocar una sola línea**
de `domain` ni de `application`. La ventana usa el mismo caso de uso que la consola:
`Main` sigue imprimiendo `1160.00`.

## Las piezas nuevas

| Archivo | Ruta | Rol |
|---|---|---|
| `pom.xml` (nombre + JavaFX) | raíz | Integrador |
| `Launcher.java`, `App.java` | `src/main/java/co/edu/unillanos/msg/` | Integrador |
| `MoneyFormat.java` | `src/main/java/co/edu/unillanos/msg/infrastructure/ui/` | Formato y estilo |
| `MoneyFormatTest.java` | `src/test/java/co/edu/unillanos/msg/infrastructure/ui/` | Formato y estilo |
| `funds-view.css` | `src/main/resources/co/edu/unillanos/msg/infrastructure/ui/` | Formato y estilo |
| `funds-view.fxml` | `src/main/resources/co/edu/unillanos/msg/infrastructure/ui/` | Vista |
| `FundsController.java` | `src/main/java/co/edu/unillanos/msg/infrastructure/ui/` | Controlador |
| todo `domain`, todo `application`, `InMemory*Repository`, `Main` | — | nadie: no se tocan |

El código Java va en `src/main/java`; el `.fxml` y el `.css` van en
`src/main/resources`. Maven solo copia a la aplicación lo que está en `resources`.

## Cómo ejecutar

```bash
mvn test                   # BUILD SUCCESS (pruebas del Lab 1 + MoneyFormatTest)
mvn clean javafx:run       # abre la ventana
mvn -q compile exec:java   # ejecuta Main en consola; esperado: 1160.00
```

Desde el IDE se ejecuta `Launcher`, no `App`: una clase que extiende
`Application` falla con «JavaFX runtime components are missing».

## Orden de fusión de las ramas

1. `feature/javafx-setup` — JavaFX en el `pom.xml`, `App` mínima, `Launcher`, contrato
2. `feature/format` — `MoneyFormat`, su prueba y la hoja de estilos
3. `feature/view` y `feature/controller` — en cualquier orden
4. `feature/app` — `App` arma el caso de uso y carga la vista
5. `docs/bitacora` — bitácora y hoja de la Parte A

## Documentos

- `BITACORA.md` — equipo, contrato, guion de aceptación y lo que realmente cambió
- `docs/parte-a.md` — contrato acordado y decisiones D1–D6
- `docs/parte-d-auditoria.md` — auditoría del método generado por IA
- `docs/capturas/` — evidencia de ejecución (pasos 3 y 5 del guion)
