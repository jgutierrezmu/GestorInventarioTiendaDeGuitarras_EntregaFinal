//CLASE QUE MODELA LA ARISTA DEL GRAFO
class Arista {

    //Atributos.
    private String destino;
    private int peso;

    //Métodos.
    //Constructor.
    public Arista(String destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    //Getters.
    public String getDestino() {
        return destino;
    }
    public int getPeso() {
        return peso;
    }
}