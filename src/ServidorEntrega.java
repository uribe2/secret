import java.util.Random;

public class ServidorEntrega extends Thread {
    private final int id;
    private final BuzonEntrega buzonEntrega;
    private final Random random;

    public ServidorEntrega(int id, BuzonEntrega buzonEntrega) {
        this.id = id;
        this.buzonEntrega = buzonEntrega;
        this.random = new Random();
        this.setName("Servidor-" + id);
    }

    @Override
    public void run() {
        try {
            System.out.println("[Servidor-" + id + "] Iniciando operación desde el arranque...");

            while (true) {
                Mensaje mensaje = buzonEntrega.extraer();

                if (mensaje.getTipo() == Mensaje.Tipo.FIN) {
                    System.out.println("[Servidor-" + id + "] Mensaje FIN recibido. Terminando...");
                    break;
                }

                // Ignorar mensajes INICIO (no deberían llegar aquí normalmente)
                if (mensaje.getTipo() == Mensaje.Tipo.INICIO) {
                    continue;
                }

                // Procesar mensaje de correo
                System.out.println("[Servidor-" + id + "] Procesando: " + mensaje);

                // Simular tiempo de procesamiento aleatorio
                int tiempoProcesamiento = random.nextInt(200) + 100;
                Thread.sleep(tiempoProcesamiento);

                System.out.println("[Servidor-" + id + "] Completado: " + mensaje);
            }

            System.out.println("[Servidor-" + id + "] Terminado");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[Servidor-" + id + "] Interrumpido");
        }
    }
}