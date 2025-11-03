import java.util.LinkedList;
import java.util.Queue;

public class BuzonEntrada {
    private final Queue<Mensaje> mensajes;
    private final int capacidad;
    private boolean cerrado;

    public BuzonEntrada(int capacidad) {
        this.mensajes = new LinkedList<>();
        this.capacidad = capacidad;
        this.cerrado = false;
    }

    public synchronized void depositar(Mensaje mensaje) {
        while (!cerrado && mensajes.size() == capacidad) {
            try {
                wait(); // Espera pasiva
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        if (cerrado) {
            return;
        }

        mensajes.add(mensaje);
        System.out.println(
                "[BuzonEntrada] Depositado: " + mensaje + " Total: " + mensajes.size() + "/" + capacidad + ")");

        // Despertar un filtro de spam
        notifyAll();

    }

    public synchronized Mensaje extraer() {
        while (mensajes.isEmpty() && !cerrado) {
            try {
                wait(); // Espera pasiva
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        if (mensajes.isEmpty()) {
            return null;
        }

        Mensaje mensaje = mensajes.poll();
        System.out
                .println("[BuzonEntrada] Extraido: " + mensaje + " (Total: " + mensajes.size() + "/" + capacidad + ")");

        // Despertar un cliente
        notifyAll();

        return mensaje;
    }

    public synchronized boolean isEmpty() {
        return mensajes.isEmpty();
    }

    public synchronized int getSize() {
        return mensajes.size();
    }

    public synchronized void cerrar() {
        cerrado = true;
        notifyAll();
    }
}
