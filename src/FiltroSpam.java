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

            if (mensaje.getTipo() == Mensaje.Tipo.INICIO) {
                System.out.println();
                continue;
            }

            if (mensaje.getTipo() == Mensaje.Tipo.FIN) {
                synchronized (lock) {
                    clientesFinalizados++;
                    System.out.println();

                    if (clientesFinalizados == numClientes && !finEnviado) {
                        esperarProcesamiento();
                        System.out.println();
                        buzonEntrega.enviarFinAServidores();
                        buzonCuarentena.depositar(new Mensaje(Mensaje.Tipo.FIN));
                        finEnviado = true;
                        lock.notifyAll();
                    }
                    while (!finEnviado) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println();
                break;
            }
            if (mensaje.esSpam()) {
                System.out.println();
                buzonCuarentena.depositar(mensaje);
            } else {
                System.out.println();
                buzonEntrega.depositar(mensaje);

            }
        }

    }

    private void esperarProcesamiento() {
        System.out.println();

        while (!buzonCuarentena.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (!buzonEntrega.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }
}