//CLASE QUE MODELA EL VERTICE DEL GRAFO
class Vertice {

    //Atributos.
    private String nombre;
    private int distancia;

    //Métodos.
    //Constructor.
    public Vertice(String nombre, int distancia) {
        this.nombre = nombre;
        this.distancia = distancia;
    }

    //Getters.
    public String getNombre() {
        return nombre;
    }

    public int getDistancia() {
        return distancia;
    }
}