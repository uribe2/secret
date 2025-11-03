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

    public synchronized void depositar(Mensaje mensaje) {
        while (mensajes.size() == capacidad) {
            Thread.yield(); // Espera semi-activa
        }

        mensajes.add(mensaje);
        System.out.println(
                "[BuzonEntrega] Depositado: " + mensaje + " (Total: " + mensajes.size() + "/" + capacidad + ")");

        notifyAll();
    }

    public synchronized void enviarFinAServidores() {
        if (finEnviado)
            return;

        while (mensajes.size() + numServidores > capacidad) {
            Thread.yield();
        }

        finEnviado = true;
        System.out.println("[BuzonEntrega] Enviando mensaje FIN a todos los servidores");

        for (int i = 0; i < numServidores; i++) {
            mensajes.add(new Mensaje(Mensaje.Tipo.FIN));
        }
        notifyAll();
    }

    public synchronized Mensaje extraer() {
        while (mensajes.isEmpty()) {
            try {
                wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Mensaje mensaje = mensajes.poll();

        if (mensaje.getTipo() == Mensaje.Tipo.FIN) {
            servidoresFinalizados++;
            System.out.println(
                    "[BuzonEntrega] Servidor recibió FIN (" + servidoresFinalizados + "/" + numServidores + ")");
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
