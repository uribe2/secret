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

        int tiempo = 10000 + random.nextInt(10001);
        mensaje.setTiempoCuarentena(tiempo);

        mensajes.add(mensaje);
        System.out.println("[BuzonCuarentena] Depositado: " + mensaje + " con tiempo " + tiempo + " (Total: "
                + mensajes.size() + ")");
        notifyAll();
    }

    public synchronized List<Mensaje> procesarMensajes(int deltaTiempo) {
        if (mensajes.isEmpty()) {
            return new ArrayList<>();
        }

        List<Mensaje> listos = new ArrayList<>();
        for (int i = 0; i < mensajes.size();) {
            Mensaje mensaje = mensajes.get(i);

            mensaje.decrementarTiempo(deltaTiempo);

            int numAleatorio = 1 + random.nextInt(21);
            if (numAleatorio % 7 == 0) {
                System.out.println("[BuzonCuarentena] Mensaje descartado (malicioso): " + mensaje);
                mensajes.remove(i);
                continue;
            }

            if (mensaje.getTiempoCuarentena() <= 0) {
                System.out.println("[BuzonCuarentena] Mensaje listo para entrega: " + mensaje);
                listos.add(mensaje);
                mensajes.remove(i);
                continue;
            }

            i++;
        }

        return listos;
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
