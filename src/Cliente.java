//CLASE QUE MODELA LOS CLIENTES DEL SISTEMA
public class Cliente {

    //ATRIBUTOS
    private String cedula;
    private String nombreCompleto;
    private String correo;
    private String contrasenna;
    private String telefono;
    private ListaProductos carrito;   //LISTA ENLAZADA SIMPLE DE PRODUCTOS
    private int prioridad;            //1 = BASICO, 2 = AFILIADO, 3 = PREMIUM
    private String ubicacion;         //UBICACION PARA EL GRAFO

    //CONSTRUCTOR
    public Cliente(String cedula, String nombreCompleto, String correo,
                   String contrasenna, String telefono, int prioridad,
                   String ubicacion) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.contrasenna = contrasenna;
        this.telefono = telefono;
        this.carrito = new ListaProductos(); //SE INSTANCIA LA LISTA DE PRODUCTOS VACIA
        this.setPrioridad(prioridad);        //SE VALIDA QUE LA PRIORIDAD ESTE ENTRE 1 Y 3
        this.ubicacion = ubicacion;          //ASIGNAR UBICACION
    }

    //SETTERS
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public void setCarrito(ListaProductos carrito) {
        this.carrito = carrito;
    }
    public void setPrioridad(int prioridad) {
        if (prioridad < 1 || prioridad > 3) {
            throw new IllegalArgumentException("La prioridad debe ser 1 (Básico), 2 (Afiliado) o 3 (Premium).");
        }
        this.prioridad = prioridad;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    //GETTERS
    public String getCedula() {
        return cedula;
    }
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public String getCorreo() {
        return correo;
    }
    public String getContrasenna() {
        return contrasenna;
    }
    public String getTelefono() {
        return telefono;
    }
    public ListaProductos getCarrito() {
        return carrito;
    }
    public int getPrioridad() {
        return prioridad;
    }
    public String getUbicacion() {
        return ubicacion;
    }

    //METODO PARA AGREGAR UN PRODUCTO AL CARRITO DEL CLIENTE
    //SE AGREGA AL FINAL DE SU LISTA ENLAZADA DE PRODUCTOS
    public void agregarProductoAlCarrito(Producto productoAgregar, int cantidadSolicitada) {
        carrito.insertarFinal(
                productoAgregar.getNombre(),
                productoAgregar.getMarca(),
                productoAgregar.getCategoria(),
                productoAgregar.getPrecio(),
                cantidadSolicitada,
                productoAgregar.getImagenes(),
                productoAgregar.getNotasAdicionales()
        );
    }

    //METODO PARA MOSTRAR EL CARRITO DEL CLIENTE (YA NO SE USA PORQUE TIENE GUI)
    public void mostrarCarrito() {
        System.out.println("-------------------------------------");
        System.out.println("CARRITO DEL CLIENTE: " + nombreCompleto);
        System.out.println("PRIORIDAD: " + prioridad);
        System.out.println("UBICACIÓN: " + ubicacion);
        System.out.println("-------------------------------------");

        //SE OBTIENE EL PRIMER ELEMENTO DE LA LISTA
        Producto temporal = carrito.getPrimero();

        if (temporal == null) {
            System.out.println("El carrito está vacío.");
        } else {
            while (temporal != null) {
                System.out.println(temporal.toString());
                System.out.println("-------------------------------------");
                temporal = temporal.getSiguiente();
            }
        }
        //REPORTE FINAL DE COSTOS
        carrito.generarReporteProductos();
        System.out.println("-------------------------------------");
    }

}
