import java.util.ArrayList;

//CLASE PRODUCTO: Esta clase modela los productos de la tienda de guitarras
public class Producto {

    //ATRIBUTOS
    private String nombre;
    private String marca;
    private String categoria;
    private double precio;
    private int cantidad;
    private ArrayList<String> imagenes; //COMO ARRAY PARA ALMACENAR VARIAS IMAGENES
    private String notasAdicionales;
    private Producto siguiente;

    //ATRIBUTOS PARA ARBOL BINARIO DE BUESQUEDA
    private Producto hijoIzquierdo;
    private Producto hijoDerecho;

    //CONSTRUCTOR
    public Producto(String nombre, String marca, String categoria, double precio, int cantidad, ArrayList<String> imagenes, String notasAdicionales) {
        this.nombre = nombre;
        this.marca = marca;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
        this.imagenes = imagenes;
        this.notasAdicionales = notasAdicionales;
        this.siguiente = null;
        this.hijoIzquierdo = null;
        this.hijoDerecho = null;
    }

    //SETTERS
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setImagenes(ArrayList<String> imagenes) {
        this.imagenes = imagenes;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public void setNotasAdicionales(String notasAdicionales) {
        this.notasAdicionales = notasAdicionales;
    }
    public void setSiguiente(Producto siguiente) {
        this.siguiente = siguiente;
    }
    public void setHijoIzquierdo(Producto hijoIzquierdo) {
        this.hijoIzquierdo = hijoIzquierdo;
    }
    public void setHijoDerecho(Producto hijoDerecho) {
        this.hijoDerecho = hijoDerecho;
    }

    //GETTERS
    public String getCategoria() {
        return categoria;
    }
    public String getMarca() {
        return marca;
    }
    public String getNombre() {
        return nombre;
    }
    public ArrayList<String> getImagenes() {
        return imagenes;
    }
    public double getPrecio() {
        return precio;
    }
    public int getCantidad() {
        return cantidad;
    }
    public String getNotasAdicionales() {
        return notasAdicionales;
    }
    public Producto getSiguiente() {
        return siguiente;
    }
    public Producto getHijoIzquierdo() {
        return hijoIzquierdo;
    }
    public Producto getHijoDerecho() {
        return hijoDerecho;
    }

    //METODO TO STRING PARA RETORNAR INFORMACION DE CADA PRODUCTO
    public String toString() {
        return  "Nombre = " + nombre +
                "\nMarca = " + marca +
                "\nCategoría = " + categoria +
                "\nPrecio = $" + precio +
                "\nCantidad = " + cantidad +
                "\nDescripción = " + notasAdicionales +
                "\nImágenes = " + imagenes;
    }
}


