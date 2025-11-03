import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BuzonCuarentena {
    private final List<Mensaje> mensajes;
    private final Random random;
    private boolean finRecibido;

    public BuzonCuarentena() {
        this.mensajes = new ArrayList<>();
        this.random = new Random();
        this.finRecibido = false;
    }

    public synchronized void depositar(Mensaje mensaje) {
        // No hay espera dado que hay capacidad ilimitada

        if (mensaje.getTipo() == Mensaje.Tipo.FIN) {
            finRecibido = true;
            System.out.println("[BuzonCuarentena] Mensaje FIN recibido");
            notifyAll();
            return;
        }

        int tiempoMs = 10000 + random.nextInt(10001);
        int tiempoSegundos = (tiempoMs + 999) / 1000;
        mensaje.setTiempoCuarentena(tiempoSegundos);

        mensajes.add(mensaje);
        System.out.println("[BuzonCuarentena] Depositado: " + mensaje + " con tiempo " + tiempoSegundos + "s (Total: "
                + mensajes.size() + ")");
        notifyAll();
    }

    public synchronized Mensaje procesarSiguiente() {
        if (mensajes.isEmpty()) {
            return null;
        }

        Mensaje mensaje = mensajes.remove(0);

        int numAleatorio = 1 + random.nextInt(21);
        if (numAleatorio % 7 == 0) {
            System.out.println("[BuzonCuarentena] Mensaje descartado (malicioso): " + mensaje);
            return null;
        }

        mensaje.decrementarTiempo(1);

        if (mensaje.getTiempoCuarentena() <= 0) {
            System.out.println("[BuzonCuarentena] Mensaje listo para entrega: " + mensaje);
            return mensaje;
        }

        mensajes.add(mensaje);
        return null;
    }

    public synchronized boolean isFinRecibido() {
        return finRecibido;
    }

    public synchronized boolean isEmpty() {
        return mensajes.isEmpty();
    }

    public synchronized int getSize() {
        return mensajes.size();
    }

}
