public class FiltroSpam extends Thread {
    private final int id;
    private final BuzonEntrada buzonEntrada;
    private final BuzonCuarentena buzonCuarentena;
    private final BuzonEntrega buzonEntrega;
    private final int numClientes;

    private static final Object lock = new Object();
    private static int clientesFinalizados = 0;
    private static boolean finEnviado = false;

    public FiltroSpam(int id, BuzonEntrada buzonEntrada, BuzonCuarentena buzonCuarentena, BuzonEntrega buzonEntrega,
            int numClientes) {
        this.id = id;
        this.buzonEntrada = buzonEntrada;
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
        this.numClientes = numClientes;
        this.setName("Filtro-" + id);
    }

    @Override
    public void run() {
        System.out.println("[Filtro-" + id + "] Iniciando...");

        while (true) {
            Mensaje mensaje = buzonEntrada.extraer();

            if (mensaje == null) {
                System.out.println("[Filtro-" + id + "] Buzón de entrada cerrado. Terminando...");
                break;
            }

            if (mensaje.getTipo() == Mensaje.Tipo.INICIO) {
                System.out.println("[Filtro-" + id + "] Mensaje INICIO recibido");
                continue;
            }

            if (mensaje.getTipo() == Mensaje.Tipo.FIN) {
                boolean esUltimo = false;
                synchronized (lock) {
                    clientesFinalizados++;
                    System.out.println("[Filtro-" + id + "] Mensaje FIN recibido (" + clientesFinalizados + "/"
                            + numClientes + ")");

                    if (clientesFinalizados == numClientes && !finEnviado) {
                        esUltimo = true;
                    }
                }

                if (esUltimo) {
                    esperarProcesamiento();
                    buzonEntrada.cerrar();
                    buzonEntrega.enviarFinAServidores();
                    buzonCuarentena.depositar(new Mensaje(Mensaje.Tipo.FIN));
                    synchronized (lock) {
                        finEnviado = true;
                        lock.notifyAll();
                    }
                    System.out.println("[Filtro-" + id + "] Finalizando tras mensaje FIN");
                    break;
                }

                continue;
            }
            if (mensaje.esSpam()) {
                System.out.println("[Filtro-" + id + "] SPAM detectado, enviando a cuarentena: " + mensaje);
                buzonCuarentena.depositar(mensaje);
            } else {
                System.out.println("[Filtro-" + id + "] Mensaje válido, enviando a entrega: " + mensaje);
                buzonEntrega.depositar(mensaje);

            }
        }

    }

    private void esperarProcesamiento() {
        System.out.println("[Filtro-" + id + "] Esperando a que todos los buzones queden vacíos...");

        try {
            while (!buzonEntrada.isEmpty() || !buzonCuarentena.isEmpty()) {
                Thread.sleep(100);
            }

            while (!buzonEntrega.isEmpty()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[Filtro-" + id + "] Buzones listos, enviando mensajes de finalización");
    }
}
