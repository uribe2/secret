public class FiltroSpam extends Thread {
    private final int id;
    private final BuzonEntrada buzonEntrada;
    private final BuzonCuarentena buzonCuarentena;
    private final BuzonEntrega buzonEntrega;

    private static final Object lock = new Object();
    private static int clientesIniciados = 0;
    private static int mensajesInicioRecibidos = 0;
    private static int clientesFinalizados = 0;
    private static boolean todosInicioRecibidos = false;
    private static boolean finEnviado = false;

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
                System.out.println("[Filtro-" + id + "] Buzon de entrada cerrado. Terminando...");
                break;
            }

            if (mensaje.getTipo() == Mensaje.Tipo.INICIO) {
                synchronized (lock) {
                    mensajesInicioRecibidos++;
                    System.out.println("[Filtro-" + id + "] Mensaje INICIO recibido (" + mensajesInicioRecibidos + ")");
                    
                    // El primer filtro que recibe un mensaje no-INICIO establece la barrera
                    if (!todosInicioRecibidos && mensajesInicioRecibidos > 0) {
                        // Esperamos hasta tener al menos un INICIO antes de continuar
                        clientesIniciados = mensajesInicioRecibidos;
                    }
                }
                continue;
            }

            if (mensaje.getTipo() == Mensaje.Tipo.FIN) {
                boolean esUltimo = false;
                synchronized (lock) {
                    // Establecer barrera: primera vez que vemos un FIN, fijamos el numero de clientes
                    if (!todosInicioRecibidos) {
                        clientesIniciados = mensajesInicioRecibidos;
                        todosInicioRecibidos = true;
                        System.out.println("[Filtro-" + id + "] BARRERA: Total de clientes detectados = " + clientesIniciados);
                    }
                    
                    clientesFinalizados++;
                    System.out.println("[Filtro-" + id + "] Mensaje FIN recibido (" + clientesFinalizados + "/"
                            + clientesIniciados + ")");

                    if (clientesFinalizados == clientesIniciados && !finEnviado) {
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
                System.out.println("[Filtro-" + id + "] Mensaje valido, enviando a entrega: " + mensaje);
                buzonEntrega.depositar(mensaje);

            }
        }

    }

    private void esperarProcesamiento() {
        System.out.println("[Filtro-" + id + "] Esperando a que todos los buzones queden vacios...");

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

        System.out.println("[Filtro-" + id + "] Buzones listos, enviando mensajes de finalizacion");
    }
}
