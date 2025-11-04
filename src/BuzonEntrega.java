import java.util.LinkedList;
import java.util.Queue;

public class BuzonEntrega {
    private final Queue<Mensaje> mensajes;
    private final int capacidad;
    private final int numServidores;
    private boolean finEnviado;
    private int servidoresFinalizados;

    public BuzonEntrega(int capacidad, int numServidores) {
        this.mensajes = new LinkedList<>();
        this.capacidad = capacidad;
        this.numServidores = numServidores;
        this.finEnviado = false;
        this.servidoresFinalizados = 0;
    }

    // Espera semiactiva: si está lleno, el hilo libera CPU y reintenta
    public synchronized void depositar(Mensaje mensaje) {
        while (mensajes.size() == capacidad) {
            try {
                wait(50); // libera CPU un momento
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        mensajes.add(mensaje);
        System.out.println(
                "[BuzonEntrega] Depositado: " + mensaje + " (Total: " + mensajes.size() + "/" + capacidad + ")");
        notifyAll();
    }

    // Enviar FIN cuando todo ha sido procesado
    public synchronized void enviarFinAServidores() {
        if (finEnviado)
            return;
        finEnviado = true;
        System.out.println("[BuzonEntrega] Enviando mensaje FIN a todos los servidores");

        for (int i = 0; i < numServidores; i++) {
            while (mensajes.size() == capacidad) {
                try {
                    wait(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            mensajes.add(new Mensaje(Mensaje.Tipo.FIN));
            notifyAll();
        }
    }

    public synchronized Mensaje extraer() {
        while (mensajes.isEmpty()) {
            try {
                wait(100); // espera pasiva hasta que haya algo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        Mensaje mensaje = mensajes.poll();

        if (mensaje.getTipo() == Mensaje.Tipo.FIN) {
            servidoresFinalizados++;
            System.out.println(
                    "[BuzonEntrega] Servidor recibio FIN (" + servidoresFinalizados + "/" + numServidores + ")");
        } else {
            System.out.println(
                    "[BuzonEntrega] Extraido: " + mensaje + " (Total: " + mensajes.size() + "/" + capacidad + ")");
        }

        notifyAll();
        return mensaje;
    }

    public synchronized boolean isEmpty() {
        return mensajes.isEmpty();
    }

    public synchronized boolean finEnviado() {
        return finEnviado;
    }

    public synchronized int getSize() {
        return mensajes.size();
    }

    public synchronized boolean todosServidoresFinalizados() {
        return servidoresFinalizados == numServidores;
    }
}
