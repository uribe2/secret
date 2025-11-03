import java.util.LinkedList;
import java.util.Queue;

public class BuzonEntrada {
    private final Queue<Mensaje> mensajes;
    private final int capacidad;

    public BuzonEntrada(int capacidad) {
        this.mensajes = new LinkedList<>();
        this.capacidad = capacidad;
    }

    public synchronized void depositar(Mensaje mensaje) {
        while (mensajes.size() == capacidad) {
            try {
                wait(); // Espera pasiva
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mensajes.add(mensaje);
        System.out.println(
                "[BuzonEntrada] Depositado: " + mensaje + " Total: " + mensajes.size() + "/" + capacidad + ")");

        // Despertar un filtro de spam
        notify();

    }

    public synchronized Mensaje extraer() {
        while (mensajes.isEmpty()) {
            try {
                wait(); // Espera pasiva
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Mensaje mensaje = mensajes.poll();
        System.out
                .println("[BuzonEntrada] Extraido: " + mensaje + " (Total: " + mensajes.size() + "/" + capacidad + ")");

        // Despertar un cliente
        notify();

        return mensaje;
    }

    public synchronized boolean isEmpty() {
        return mensajes.isEmpty();
    }

    public synchronized int getSize() {
        return mensajes.size();
    }

}
