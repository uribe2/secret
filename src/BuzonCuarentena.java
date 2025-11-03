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
        int tiempo = 10 + random.nextInt(11);
        mensaje.setTiempoCuarentena(tiempo);

        mensajes.add(mensaje);
        System.out.println("[BuzonCuarentena] Depositado: " + mensaje + " con tiempo " + tiempo + " (Total: "
                + mensajes.size() + ")");
        notifyAll();
    }

    public synchronized Mensaje procesarMensaje() {
        if (mensajes.isEmpty()) {
            return null;
        }

        // Tomar el PRIMER mensaje de la lista
        Mensaje mensaje = mensajes.remove(0);

        // Decrementar tiempo
        mensaje.decrementarTiempo();

        // Simular descarte de mensajes maliciosos
        int numAleatorio = 1 + random.nextInt(21);
        if (numAleatorio % 7 == 0) {
            System.out.println("[BuzonCuarentena] Mensaje descartado (malicioso): " + mensaje);
            return null; // No lo regresamos a la lista ni lo enviamos a entrega
        }

        // Si el tiempo llegó a 0, está listo para entrega
        if (mensaje.getTiempoCuarentena() <= 0) {
            System.out.println("[BuzonCuarentena] Mensaje listo para entrega: " + mensaje);
            return mensaje; // No lo regresamos a la lista
        }

        // Si todavía tiene tiempo, regresarlo al FINAL de la lista
        mensajes.add(mensaje);

        return null; // No hay mensaje listo aún
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
