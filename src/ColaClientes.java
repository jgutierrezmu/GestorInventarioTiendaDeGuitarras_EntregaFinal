import java.util.ArrayList;
import java.util.List;

//CLASE COLACLIENTES: Esta clase modela una cola de prioridad para atender clientes
public class ColaClientes {

    //ATRIBUTO PRINCIPAL
    private ArrayList<Cliente> colaClientes;

    //CONSTRUCTOR POR DEFECTO
    public ColaClientes() {
        this.colaClientes = new ArrayList<>();
    }

    //METODO PARA SABER SI LA COLA ESTA VACIA
    public boolean estaVacia() {
        return colaClientes.isEmpty();
    }

    //METODO PARA INSERTAR UN CLIENTE SEGUN PRIORIDAD
    //PRIORIDAD: 3 > 2 > 1 y en caso de empate, se mantiene el orden FIFO
    public void insertarCliente(Cliente nuevo) {
        if (colaClientes.isEmpty()) {
            colaClientes.add(nuevo);
            return;
        }
        int prioridadNuevo = nuevo.getPrioridad();
        int i = 0;

        //PRIMERO SE AVANZA SOBRE LOS CLIENTES DE MAYOR PRIORIDAD
        while (i < colaClientes.size() &&
                colaClientes.get(i).getPrioridad() > prioridadNuevo) {
            i++;
        }

        //LUEGO SE AVANZA SOBRE LOS DE LA MISMA PRIORIDAD PARA MANTENER FIFO
        while (i < colaClientes.size() &&
                colaClientes.get(i).getPrioridad() == prioridadNuevo) {
            i++;
        }

        //SE INSERTA EN LA POSICION CORRECTA
        colaClientes.add(i, nuevo);
    }

    //METODO PARA ATENDER AL CLIENTE AL FRENTE DE LA COLA
    public Cliente atenderCliente() {
        if (colaClientes.isEmpty()) {
            return null;
        }
        return colaClientes.remove(0);
    }

    //METODO PARA VER EL CLIENTE AL FRENTE SIN REMOVERLO
    public Cliente verFrente() {
        if (colaClientes.isEmpty()) {
            return null;
        }
        return colaClientes.get(0);
    }

    //METODO PARA VER EL CLIENTE AL FINAL DE LA COLA
    public Cliente verFin() {
        if (colaClientes.isEmpty()) {
            return null;
        }
        return colaClientes.get(colaClientes.size() - 1);
    }

    //METODO PARA VERIFICAR SI UNA CEDULA YA EXISTE EN LA COLA
    public boolean cedulaExistente(String cedula) {
        for (Cliente c : colaClientes) {
            if (c.getCedula().equals(cedula)) {
                return true;
            }
        }
        return false;
    }

    //METODO PARA MOSTRAR TODA LA COLA (YA NO SE USA PORQUE SE ESTA USANDO GUI)
    public void mostrarCola() {
        if (colaClientes.isEmpty()) {
            System.out.println("La cola está vacía.");
            return;
        }
        System.out.println("----------------------- COLA DE CLIENTES -----------------------");
        for (int i = 0; i < colaClientes.size(); i++) {
            Cliente c = colaClientes.get(i);
            System.out.println((i + 1) + ". " + c.getNombreCompleto() +
                    " | Cédula: " + c.getCedula() +
                    " | Prioridad: " + c.getPrioridad());
        }
        System.out.println("----------------------------------------------------------------");
    }

    //METODOS ADICIONALES PARA FACILIDAD DE GUI
    //DEVUELVE UNA LISTA TEMPORAL DE CLIENTES (PARA MOSTRAR EN TABLAS)
    public List<Cliente> obtenerListaClientes() {
        return new ArrayList<>(colaClientes);
    }

}

