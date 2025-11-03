import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal del sistema de mensajeria
 */
public class Main {

    public static void main(String[] args) {
        try {
            // Cargar configuracion
            Configuracion config = new Configuracion("config.txt");
            System.out.println("=== CONFIGURACION DEL SISTEMA ===");
            System.out.println(config);
            System.out.println("=================================\n");

            // Crear buzones
            BuzonEntrada buzonEntrada = new BuzonEntrada(config.getCapacidadBuzonEntrada());
            BuzonCuarentena buzonCuarentena = new BuzonCuarentena();
            BuzonEntrega buzonEntrega = new BuzonEntrega(config.getCapacidadBuzonEntrega(), config.getNumServidores());

            // Crear listas de threads
            List<ClienteEmisor> clientes = new ArrayList<>();
            List<FiltroSpam> filtros = new ArrayList<>();
            List<ServidorEntrega> servidores = new ArrayList<>();

            // Crear y arrancar clientes emisores
            System.out.println("=== INICIANDO CLIENTES EMISORES ===");
            for (int i = 1; i <= config.getNumClientes(); i++) {
                ClienteEmisor cliente = new ClienteEmisor(i, config.getNumMensajesPorCliente(), buzonEntrada);
                clientes.add(cliente);
                cliente.start();
            }

            // Crear y arrancar filtros de spam
            System.out.println("\n=== INICIANDO FILTROS DE SPAM ===");
            for (int i = 1; i <= config.getNumFiltros(); i++) {
                FiltroSpam filtro = new FiltroSpam(i, buzonEntrada, buzonCuarentena, buzonEntrega);
                filtros.add(filtro);
                filtro.start();
            }

            // Crear y arrancar manejador de cuarentena
            System.out.println("\n=== INICIANDO MANEJADOR DE CUARENTENA ===");
            ManejadorCuarentena manejador = new ManejadorCuarentena(buzonCuarentena, buzonEntrega);
            manejador.start();

            // Crear y arrancar servidores de entrega
            System.out.println("\n=== INICIANDO SERVIDORES DE ENTREGA ===");
            for (int i = 1; i <= config.getNumServidores(); i++) {
                ServidorEntrega servidor = new ServidorEntrega(i, buzonEntrega);
                servidores.add(servidor);
                servidor.start();
            }

            System.out.println("\n=== SISTEMA EN EJECUCION ===\n");

            // Esperar a que terminen los clientes
            for (ClienteEmisor cliente : clientes) {
                cliente.join();
            }
            System.out.println("\n=== TODOS LOS CLIENTES HAN TERMINADO ===");

            // Esperar a que terminen los filtros
            for (FiltroSpam filtro : filtros) {
                filtro.join();
            }
            System.out.println("\n=== TODOS LOS FILTROS HAN TERMINADO ===");

            // Esperar a que termine el manejador
            manejador.join();
            System.out.println("\n=== MANEJADOR DE CUARENTENA HA TERMINADO ===");

            // Esperar a que terminen los servidores
            for (ServidorEntrega servidor : servidores) {
                servidor.join();
            }
            System.out.println("\n=== TODOS LOS SERVIDORES HAN TERMINADO ===");

            // Verificar estado final
            System.out.println("\n=== ESTADO FINAL DEL SISTEMA ===");
            System.out.println("Buzon de Entrada vacio: " + buzonEntrada.isEmpty() + " (Tamano: "
                    + buzonEntrada.getSize() + ")");
            System.out.println("Buzon de Cuarentena vacio: " + buzonCuarentena.isEmpty() + " (Tamano: "
                    + buzonCuarentena.getSize() + ")");
            System.out.println("Buzon de Entrega vacio: " + buzonEntrega.isEmpty() + " (Tamano: "
                    + buzonEntrega.getSize() + ")");

            System.out.println("\n=== SISTEMA FINALIZADO CORRECTAMENTE ===");

        } catch (IOException e) {
            System.err.println("Error al cargar el archivo de configuracion: " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            System.err.println("Sistema interrumpido: " + e.getMessage());
            Thread.currentThread().interrupt();
            System.exit(1);
        }
    }
}