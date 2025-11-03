public class Mensaje {
    public enum Tipo {
        INICIO, FIN, CORREO
    }

    private final Tipo tipo;
    private final String id;
    private final boolean esSpam;
    private int tiempoCuarentena;

    // Const. Mensajes de control
    public Mensaje(Tipo tipo) {
        this.tipo = tipo;
        this.id = null;
        this.esSpam = false;
        this.tiempoCuarentena = 0;
    }

    // Constructor Correos
    public Mensaje(String id, boolean esSpam) {
        this.tipo = Tipo.CORREO;
        this.id = id;
        this.esSpam = esSpam;
        this.tiempoCuarentena = 0;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }

    public boolean esSpam() {
        return esSpam;
    }

    public int getTiempoCuarentena() {
        return tiempoCuarentena;
    }

    public void setTiempoCuarentena(int tiempo) {
        this.tiempoCuarentena = tiempo;
    }

    public void decrementarTiempo(int delta) {
        if (tiempoCuarentena > 0) {
            tiempoCuarentena = Math.max(0, tiempoCuarentena - delta);
        }
    }

    @Override
    public String toString() {
        if (tipo == Tipo.CORREO) {
            return "Mensaje[" + id + ", spam=" + esSpam + "]";
        }
        return "Mensaje[" + tipo + "]";
    }

}
