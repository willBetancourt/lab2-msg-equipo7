package co.edu.unillanos.msg.infrastructure.ui;

import java.util.Locale;

/**
 * Formato del dinero para la ventana.
 * Rol responsable: Formato y estilo.
 *
 * Decisión D3: vive en infrastructure.ui y no en domain. Poner el signo $ y dos
 * decimales es una decisión de presentación: cambia si la ventana cambia, no si
 * cambian las reglas del negocio.
 */
public final class MoneyFormat {

    private MoneyFormat() { }

    /** Devuelve el monto con dos decimales y punto decimal, por ejemplo "$ 1160.00". */
    public static String format(double amount) {
        return String.format(Locale.ROOT, "$ %.2f", amount);
    }
}
