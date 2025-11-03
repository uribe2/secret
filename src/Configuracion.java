import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Configuracion {
    private int numClientes;
    private int numMensajesPorCliente;
    private int numFiltros;
    private int numServidores;
    private int capacidadBuzonEntrada;
    private int capacidadBuzonEntrega;

    public Configuracion(String archivo) throws IOException {
        cargarConfiguracion(archivo);
    }

    private void cargarConfiguracion(String archivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                // Ignorar lineas vacias y comentarios
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                String[] partes = linea.split("=");
                if (partes.length != 2) {
                    continue;
                }

                String clave = partes[0].trim();
                int valor = Integer.parseInt(partes[1].trim());

                switch (clave) {
                    case "numClientes":
                        numClientes = valor;
                        break;
                    case "numMensajesPorCliente":
                        numMensajesPorCliente = valor;
                        break;
                    case "numFiltros":
                        numFiltros = valor;
                        break;
                    case "numServidores":
                        numServidores = valor;
                        break;
                    case "capacidadBuzonEntrada":
                        capacidadBuzonEntrada = valor;
                        break;
                    case "capacidadBuzonEntrega":
                        capacidadBuzonEntrega = valor;
                        break;
                }
            }
        }
    }

    public int getNumClientes() {
        return numClientes;
    }

    public int getNumMensajesPorCliente() {
        return numMensajesPorCliente;
    }

    public int getNumFiltros() {
        return numFiltros;
    }

    public int getNumServidores() {
        return numServidores;
    }

    public int getCapacidadBuzonEntrada() {
        return capacidadBuzonEntrada;
    }

    public int getCapacidadBuzonEntrega() {
        return capacidadBuzonEntrega;
    }

    @Override
    public String toString() {
        return "Configuracion{" +
                "numClientes=" + numClientes +
                ", numMensajesPorCliente=" + numMensajesPorCliente +
                ", numFiltros=" + numFiltros +
                ", numServidores=" + numServidores +
                ", capacidadBuzonEntrada=" + capacidadBuzonEntrada +
                ", capacidadBuzonEntrega=" + capacidadBuzonEntrega +
                '}';
    }
}
