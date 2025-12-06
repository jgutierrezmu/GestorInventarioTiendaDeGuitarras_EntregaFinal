import java.util.ArrayList;
import java.util.List;

//CLASE LISTAPRODUCTOS: ESTA CLASE MODELA LA LISTA ENLAZADA SIMPLE QUE RELACIONAN LOS DIFERENTES PRODUCTOS
public class ListaProductos {

    //ATRIBUTOS
    private Producto primero;

    //CONSTRUCTOR POR DEFECTO
    public ListaProductos() {
        this.primero = null;
    }

    //GETTER
    public Producto getPrimero() {
        return primero;
    }

    //SETTER
    public void setPrimero(Producto primero) {
        this.primero = primero;
    }

    //METODO PARA INSTERTAR PRODUCTO AL INICIO DE LA LISTA ENLAZADA SIMPLE
    public void insertarInicio(String nombreNuevo, String marcaNueva, String categoriaNueva, double precioNuevo, int cantidadNueva, ArrayList<String> nuevasImagenes, String notasAdicionalesNuevas ) {
        Producto productoInsertar = new Producto(nombreNuevo,marcaNueva,categoriaNueva,precioNuevo,cantidadNueva,nuevasImagenes,notasAdicionalesNuevas);
        if (primero == null) {
            setPrimero(productoInsertar);
            return;
        }
        productoInsertar.setSiguiente(primero);
        this.setPrimero(productoInsertar);
        System.out.println("Se agregó el producto al inicio");
    }

    //METODO PARA INSERTAR EL FINAL DE LA LISTA ENLAZADA SIMPLE
    public void insertarFinal(String nombreNuevo, String marcaNueva, String categoriaNueva, double precioNuevo, int cantidadNueva, ArrayList<String> nuevasImagenes, String notasAdicionalesNuevas ) {
        Producto productoInsertar = new Producto(nombreNuevo,marcaNueva,categoriaNueva,precioNuevo,cantidadNueva,nuevasImagenes,notasAdicionalesNuevas);
        if (primero == null) {
            setPrimero(productoInsertar);
            return;
        }
        Producto temporal = primero;
        while (temporal.getSiguiente() != null) {
            temporal = temporal.getSiguiente();
        }
        temporal.setSiguiente(productoInsertar);
        System.out.println("Se agregó el producto al final");
    }

    //METODO PARA MOSTRAR ELEMENTOS QUE HAN SIDO AGREGADOS A LA LISTA (YA NO ESTÁ EN USO)
    public void mostarListaDeProductos () {
        if (primero == null) {
            System.out.println("La lista no contiene elementos");
            return;
        }
        Producto temporal = primero;
        while (temporal != null) {
            System.out.println(temporal.toString());
            temporal = temporal.getSiguiente();
        }
    }

    //METODO PARA ELIMINAR UN NODO DE LA LISTA SEGUN UN NOMBRE EN PARTICULAR
    public boolean eliminarProductoPorNombre(String nombreEliminar) {
        if (primero == null) {
            System.out.println("La lista no contiene elementos");
            return false;
        }

        //SE VERIFICA SI EL PRIMER ELEMENTO DE LA LISTA COINCIDE CON EL NOMBRE A ELIMINAR
        if (primero.getNombre().equals(nombreEliminar)) {
            primero = primero.getSiguiente();
            return true;
        }

        //SE REVISAN LOS DEMAS ELEMENTOS DE LA LISTA
        Producto temporal = primero;
        while (temporal.getSiguiente() != null) {
            if (temporal.getSiguiente().getNombre().equals(nombreEliminar)) {
                temporal.setSiguiente(temporal.getSiguiente().getSiguiente());
                return true;
            }
            temporal = temporal.getSiguiente();
        }
        System.out.println("Producto no encontrado");
        return false;
    }

    //METODO PARA IMPRIMR EL REPORTE COMPLETO DEL CARRITO
    public void generarReporteProductos() {
        if (primero == null) {
            System.out.println("La lista no contiene elementos, no se puede generar el reporte");
            return;
        }
        Producto temporal = primero;
        double totales = 0;
        while (temporal != null) {
            System.out.println("------------------------------------------------");
            System.out.println("Nombre de producto: " + temporal.getNombre());
            System.out.println("Cantidad: " + temporal.getCantidad());
            System.out.println("Precio: $" + temporal.getPrecio());
            double costosProductosEspecificos = temporal.getCantidad()*temporal.getPrecio();
            System.out.println("Costo de producto especifico: $" + costosProductosEspecificos);
            totales += costosProductosEspecificos;
            temporal = temporal.getSiguiente();
        }
        System.out.println("------------------------------------------------");
        System.out.println("Costo TOTAL de inventario: $" + totales);
    }

    //METODO PARA CALCULAR EL COSTO TOTAL DEL CARRITO
    public double calcularCostoTotal() {
        double total = 0;
        Producto temporal = primero;

        while (temporal != null) {
            total += temporal.getPrecio() * temporal.getCantidad();
            temporal = temporal.getSiguiente();
        }

        return total;
    }

    //METODO PARA CREAR UN OBJETO TIPO "LIST" DEL CARRITO PARA FACILIDAD DE INTERFAZ GRAFICA
    public List<Producto> generarListaProductos() {
        List<Producto> lista = new java.util.ArrayList<>();
        Producto temporal = this.primero;

        while (temporal != null) {
            //SE CREA UNA COPIA DEL PRODUCTO SOLO PARA REPORTE / TABLAS
            Producto copia = new Producto(
                    temporal.getNombre(),
                    temporal.getMarca(),
                    temporal.getCategoria(),
                    temporal.getPrecio(),
                    temporal.getCantidad(), //️CANTIDAD REAL EN EL CARRITO
                    temporal.getImagenes(), //solo referencia el array (no necesita clonarse)
                    temporal.getNotasAdicionales()
            );
            lista.add(copia);
            temporal = temporal.getSiguiente();
        }
        return lista;
    }


}
