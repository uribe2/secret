public class FiltroSpam extends Thread {
    private final int id;
    private final BuzonEntrada buzonEntrada;
    private final BuzonCuarentena buzonCuarentena;
    private final BuzonEntrega buzonEntrega;

    private static final Object lock = new Object();
    private static int clientesEsperados = 0;
    private static int clientesTerminados = 0;
    private static boolean finGlobalEnviado = false;

    public FiltroSpam(int id, BuzonEntrada buzonEntrada, BuzonCuarentena buzonCuarentena, BuzonEntrega buzonEntrega) {
        this.id = id;
        this.buzonEntrada = buzonEntrada;
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
        this.setName("Filtro-" + id);
    }

    @Override
    public void run() {
        System.out.println("[Filtro-" + id + "] Iniciando...");

        while (true) {
            Mensaje mensaje = buzonEntrada.extraer();

            if (mensaje == null) {
                if (buzonEntrada.isEmpty() && finGlobalEnviado)
                    break;
                continue;
            }

            if (mensaje.getTipo() == Mensaje.Tipo.INICIO) {
                synchronized (lock) {
                    clientesEsperados++;
                    System.out.println("[Filtro-" + id + "] Mensaje INICIO recibido. Total clientes detectados: "
                            + clientesEsperados);
                }
                continue;
            }

            if (mensaje.getTipo() == Mensaje.Tipo.FIN) {
                boolean esUltimo = false;
                synchronized (lock) {
                    clientesTerminados++;
                    System.out.println("[Filtro-" + id + "] Mensaje FIN recibido (" + clientesTerminados + "/"
                            + clientesEsperados + ")");
                    if (clientesTerminados == clientesEsperados && !finGlobalEnviado) {
                        esUltimo = true;
                        finGlobalEnviado = true;
                    }
                }

                if (esUltimo) {
                    esperarBuzonesVacios();
                    buzonEntrada.cerrar();
                    buzonEntrega.enviarFinAServidores();
                    buzonCuarentena.depositar(new Mensaje(Mensaje.Tipo.FIN));
                    System.out.println("[Filtro-" + id + "] Envio de mensajes FIN completado. Finalizando...");
                    break;
                }
                continue;
            }

            // Caso: correo normal
            if (mensaje.esSpam()) {
                System.out.println("[Filtro-" + id + "] SPAM detectado, enviando a cuarentena: " + mensaje);
                buzonCuarentena.depositar(mensaje);
            } else {
                System.out.println("[Filtro-" + id + "] Mensaje valido, enviando a entrega: " + mensaje);
                buzonEntrega.depositar(mensaje);
            }
        }

        System.out.println("[Filtro-" + id + "] Terminado.");
    }

    private void esperarBuzonesVacios() {
        System.out.println("[Filtro-" + id + "] Esperando a que todos los buzones queden vacíos...");
        try {
            while (!buzonEntrada.isEmpty() || !buzonCuarentena.isEmpty()) {
                Thread.sleep(200);
            }
            while (!buzonEntrega.isEmpty()) {
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[Filtro-" + id + "] Buzones vacíos, listo para finalizar.");
    }
}
