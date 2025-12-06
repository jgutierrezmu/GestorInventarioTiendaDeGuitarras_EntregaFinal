import java.util.List;

//CLASE ARBOLPRODUCTOS: Esta clase implementa un árbol binario de búsqueda
//donde cada nodo es un Producto y la llave para ordenar es el nombre del producto
public class ArbolProductos {

    //ATRIBUTO
    private Producto raiz;

    //CONSTRUCTOR POR DEFECTO
    public ArbolProductos() {
        this.raiz = null;
    }

    //GETTER Y SETTER
    public Producto getRaiz() {
        return raiz;
    }
    public void setRaiz(Producto raiz) {
        this.raiz = raiz;
    }

    //METODO PARA INSERTAR UN PRODUCTO EN EL ARBOL
    //La comparación se hace por el nombre del producto (ORDEN ALFABETICO)
    public void insertarProducto(Producto nuevoProducto) {
        if (raiz == null) {
            raiz = nuevoProducto;
            return;
        }

        Producto nodoActual = raiz;
        Producto padreActual;

        //Se recorre el árbol para encontrar el lugar correcto
        while (true) {
            padreActual = nodoActual;

            //Comparación por nombre (ignoreCase para evitar problemas de mayúsculas)
            int comparacion = nuevoProducto.getNombre().compareToIgnoreCase(nodoActual.getNombre());

            if (comparacion < 0) {
                //Insertar hacia la izquierda
                nodoActual = nodoActual.getHijoIzquierdo();
                if (nodoActual == null) {
                    padreActual.setHijoIzquierdo(nuevoProducto);
                    return;
                }
            } else if (comparacion > 0) {
                //Insertar hacia la derecha
                nodoActual = nodoActual.getHijoDerecho();
                if (nodoActual == null) {
                    padreActual.setHijoDerecho(nuevoProducto);
                    return;
                }
            } else {
                //Nombres iguales --- NO se insertan duplicados
                System.out.println("Ya existe un producto con ese nombre en el inventario.");
                return;
            }
        }
    }

    //METODO PARA BUSCAR UN PRODUCTO POR SU NOMBRE
    public Producto buscarProducto(String nombreBuscar) {
        Producto nodoTemp = raiz;

        while (nodoTemp != null) {
            int comparacion = nombreBuscar.compareToIgnoreCase(nodoTemp.getNombre());

            if (comparacion == 0) {
                return nodoTemp; //Producto encontrado
            } else if (comparacion < 0) {
                nodoTemp = nodoTemp.getHijoIzquierdo();
            } else {
                nodoTemp = nodoTemp.getHijoDerecho();
            }
        }
        return null; //No encontrado
    }

    //METODO PARA ELIMINAR UN PRODUCTO DEL ARBOL BINARIO DE BUSQUEDA
    public void eliminarProducto(String nombreEliminar) {
        raiz = eliminarRec(raiz, nombreEliminar);
    }

    //METODO AUXILIAR RECURSIVO PARA ELIMINAR UN PRODUCTO
    private Producto eliminarRec(Producto nodoActual, String nombreEliminar) {

        if (nodoActual == null) {
            System.out.println("No se encontró el producto: " + nombreEliminar);
            return null;
        }

        int comparacion = nombreEliminar.compareToIgnoreCase(nodoActual.getNombre());

        if (comparacion < 0) {
            //Buscar por la izquierda
            nodoActual.setHijoIzquierdo(eliminarRec(nodoActual.getHijoIzquierdo(), nombreEliminar));
        }
        else if (comparacion > 0) {
            //Buscar por la derecha
            nodoActual.setHijoDerecho(eliminarRec(nodoActual.getHijoDerecho(), nombreEliminar));
        }
        else {
            //SE ENCONTRÓ EL PRODUCTO A ELIMINAR

            //CASO 1: NO TIENE HIJOS
            if (nodoActual.getHijoIzquierdo() == null && nodoActual.getHijoDerecho() == null) {
                return null;
            }

            //CASO 2: SOLO TIENE UN HIJO (IZQUIERDO O DERECHO)
            else if (nodoActual.getHijoIzquierdo() == null) {
                return nodoActual.getHijoDerecho();
            }
            else if (nodoActual.getHijoDerecho() == null) {
                return nodoActual.getHijoIzquierdo();
            }

            //CASO 3: TIENE DOS HIJOS
            //Se reemplaza con el sucesor in-order (el más pequeño del subárbol derecho)
            Producto sucesor = encontrarMinimo(nodoActual.getHijoDerecho());
            nodoActual.setNombre(sucesor.getNombre());
            nodoActual.setMarca(sucesor.getMarca());
            nodoActual.setCategoria(sucesor.getCategoria());
            nodoActual.setPrecio(sucesor.getPrecio());
            nodoActual.setCantidad(sucesor.getCantidad());
            nodoActual.setImagenes(sucesor.getImagenes());
            nodoActual.setNotasAdicionales(sucesor.getNotasAdicionales());

            //Eliminar el sucesor (que ya fue copiado)
            nodoActual.setHijoDerecho(
                    eliminarRec(nodoActual.getHijoDerecho(), sucesor.getNombre())
            );
        }

        return nodoActual;
    }

    //METODO AUXILIAR PARA ENCONTRAR EL MINIMO (SUCESOR IN-ORDER)
    private Producto encontrarMinimo(Producto nodo) {
        while (nodo.getHijoIzquierdo() != null) {
            nodo = nodo.getHijoIzquierdo();
        }
        return nodo;
    }

    //METODO PARA IMPRIMIR EL INVENTARIO COMPLETO (IN-ORDER) (YA NO SIRVE PORQUE EXISTE GUI)
    public void mostrarInventario() {
        if (raiz == null) {
            System.out.println("El inventario está vacío.");
            return;
        }
        System.out.println("----------------------- INVENTARIO DISPONIBLE -----------------------");
        recorrerInOrder(this.raiz);
        System.out.println("---------------------------------------------------------------------");
    }

    //METODO AUXILIAR PARA RECORRIDO IN-ORDER
    private void recorrerInOrder(Producto raizTemp) {
        if (raizTemp != null) {
            recorrerInOrder(raizTemp.getHijoIzquierdo());
            System.out.println(raizTemp.toString());
            System.out.println("---------------------------------------------------------------------");
            recorrerInOrder(raizTemp.getHijoDerecho());
        }
    }

    //METODOS ADICIONALES PARA FACILIDAD DE GUI
    //METODO PUBLICO PARA OBTENER LOS PRODUCTOS EN UNA LISTA (IN-ORDER)
    public void agregarProductosATablaInventario(List<Producto> lista) {
        recorrerInOrder(raiz, lista);
    }

    //RECORRIDO INORDEN PRIVADO
    private void recorrerInOrder(Producto nodo, List<Producto> lista) {
        if (nodo != null) {
            recorrerInOrder(nodo.getHijoIzquierdo(), lista);
            lista.add(nodo);
            recorrerInOrder(nodo.getHijoDerecho(), lista);
        }
    }
}
