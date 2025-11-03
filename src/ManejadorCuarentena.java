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
            // Procesar UN mensaje de cuarentena
            Mensaje mensajeListo = buzonCuarentena.procesarMensaje();

            // Si hay un mensaje listo, moverlo al buzón de entrega
            if (mensajeListo != null) {
                System.out.println("[ManejadorCuarentena] Moviendo a entrega: " + mensajeListo);
                buzonEntrega.depositar(mensajeListo);
            }

            // Verificar si debe terminar
            if (buzonCuarentena.isFinRecibido() && buzonCuarentena.isEmpty()) {
                System.out.println("[ManejadorCuarentena] Fin recibido y cuarentena vacía. Terminando...");
                break;
            }

            // Esperar 1 segundo antes de procesar el siguiente mensaje
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("[ManejadorCuarentena] Terminado");
    }
}
