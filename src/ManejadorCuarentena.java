public class ManejadorCuarentena extends Thread {

    private final BuzonCuarentena buzonCuarentena;
    private final BuzonEntrega buzonEntrega;

    public ManejadorCuarentena(BuzonCuarentena buzonCuarentena, BuzonEntrega buzonEntrega) {
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
        this.setName("ManejadorCuarentena");
    }

    @Override
    public void run() {
        System.out.println("[ManejadorCuarentena] Iniciando...");

        while (true) {
            // Procesar todos los mensajes actualmente en cuarentena
            for (Mensaje mensajeListo : buzonCuarentena.procesarMensajes(1000)) {
                System.out.println("[ManejadorCuarentena] Moviendo a entrega: " + mensajeListo);
                buzonEntrega.depositar(mensajeListo);
            }

            if (buzonCuarentena.isFinRecibido() && buzonCuarentena.isEmpty()) {
                System.out.println("[ManejadorCuarentena] Fin recibido y cuarentena vacia. Terminando...");
                break;
            }

            // Esperar 1 segundo antes de procesar el siguiente ciclo
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[ManejadorCuarentena] Interrumpido");
                break;
            }
        }

        System.out.println("[ManejadorCuarentena] Terminado");
    }
}
