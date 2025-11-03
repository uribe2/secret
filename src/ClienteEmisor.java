import java.util.Random;

public class ClienteEmisor extends Thread {

    private final int id;
    private final int numMensajes;
    private final BuzonEntrada buzonEntrada;
    private final Random random;

    public ClienteEmisor(int id, int numMensajes, BuzonEntrada buzonEntrada) {
        this.id = id;
        this.numMensajes = numMensajes;
        this.buzonEntrada = buzonEntrada;
        this.random = new Random();
        this.setName("Cliente-" + id);
    }

    @Override
    public void run() {
        System.out.println("[Cliente-" + id + "] Iniciando...");
        buzonEntrada.depositar(new Mensaje(Mensaje.Tipo.INICIO));

        for (int i = 0; i < numMensajes; i++) {
            String idMensaje = "C" + id + "-M" + (i + 1);
            boolean esSpam = random.nextBoolean();
            Mensaje correo = new Mensaje(idMensaje, esSpam);

            System.out.println("[Cliente-" + id + "] Generando: " + correo);
            buzonEntrada.depositar(correo);

            try {
                Thread.sleep(random.nextInt(100) + 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("[Cliente-" + id + "] Finalizando...");
        buzonEntrada.depositar(new Mensaje(Mensaje.Tipo.FIN));
    }
}
