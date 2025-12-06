import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//CLASE VENTANACARRITO: Permite agregar productos al carrito del cliente
public class VentanaCarrito extends JDialog {

    //ATRIBUTOS
    private Cliente cliente;
    private ArbolProductos inventario;

    //TABLAS
    private JTable tablaInventario;
    private JTable tablaCarrito;

    //MODELOS PARA TABLAS
    private DefaultTableModel modeloInventario;
    private DefaultTableModel modeloCarrito;

    //CONTROL PARA CANTIDAD
    private JSpinner spCantidad;

    //CONSTRUCTOR
    public VentanaCarrito(JFrame parent, Cliente cliente, ArbolProductos inventario) {

        super(parent, "Carrito de " + cliente.getNombreCompleto(), true);
        this.cliente = cliente;
        this.inventario = inventario;

        //CONFIGURACIÓN GENERAL DE VENTANA
        setSize(930, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        //FUENTE UNIFICADA
        Font fuenteGeneral = new Font("Segoe UI", Font.PLAIN, 14);

        //TABLA SUPERIOR: INVENTARIO DE LA TIENDA
        String[] columnasInv = {"Nombre", "Marca", "Categoría", "Precio", "Stock"};
        modeloInventario = new DefaultTableModel(columnasInv, 0);
        tablaInventario = new JTable(modeloInventario);
        tablaInventario.setFont(fuenteGeneral);
        tablaInventario.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        cargarInventario();

        JScrollPane scrollInv = new JScrollPane(tablaInventario);
        scrollInv.setBorder(BorderFactory.createTitledBorder("Inventario de la tienda"));

        //TABLA INFERIOR: CARRITO DEL CLIENTE
        String[] columnasCarr = {"Nombre", "Cantidad", "Precio Unitario", "Subtotal"};
        modeloCarrito = new DefaultTableModel(columnasCarr, 0);
        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setFont(fuenteGeneral);
        tablaCarrito.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollCarr = new JScrollPane(tablaCarrito);
        scrollCarr.setBorder(BorderFactory.createTitledBorder("Carrito del cliente"));

        //PANEL DE ACCIONES DE CARRITO
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel lblCant = new JLabel("Cantidad a agregar:");
        lblCant.setFont(fuenteGeneral);

        spCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spCantidad.setFont(fuenteGeneral);

        JButton btnAgregar = new JButton("AGREGAR AL CARRITO");
        JButton btnEliminar = new JButton("ELIMINAR DEL CARRITO");
        JButton btnTerminar = new JButton("TERMINAR CARRITO");

        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTerminar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panelBotones.add(lblCant);
        panelBotones.add(spCantidad);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnTerminar);

        //PANEL INFERIOR (TABLA + BOTONES)
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(scrollCarr, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        //SPLIT PANE (Inventario arriba / Carrito abajo)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollInv, panelInferior);
        splitPane.setResizeWeight(0.52);
        splitPane.setDividerLocation(250);
        add(splitPane, BorderLayout.CENTER);

        //EVENTOS PRINCIPALES DE BOTONES
        btnAgregar.addActionListener(e -> agregarProductoSeleccionado());
        btnEliminar.addActionListener(e -> eliminarProductoCarrito());
        btnTerminar.addActionListener(e -> dispose());

        setVisible(true);
    }

    //CARGAR INVENTARIO (RECORRIDO IN-ORDER)
    private void cargarInventario() {
        modeloInventario.setRowCount(0);
        List<Producto> lista = new ArrayList<>();
        inventario.agregarProductosATablaInventario(lista);

        for (Producto p : lista) {
            modeloInventario.addRow(new Object[]{
                    p.getNombre(),
                    p.getMarca(),
                    p.getCategoria(),
                    "$" + p.getPrecio(),
                    p.getCantidad()
            });
        }
    }

    //  AGREGAR DESDE INVENTARIO AL CARRITO
    private void agregarProductoSeleccionado() {
        int fila = tablaInventario.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un producto.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreProducto = tablaInventario.getValueAt(fila, 0).toString();
        int cantidadSolicitada = (int) spCantidad.getValue();

        Producto encontrado = inventario.buscarProducto(nombreProducto);
        if (encontrado == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: producto no encontrado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            cargarInventario();
            return;
        }

        if (cantidadSolicitada > encontrado.getCantidad()) {
            JOptionPane.showMessageDialog(this,
                    "Stock insuficiente. Disponible: " + encontrado.getCantidad(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //DESCONTAR DEL INVENTARIO
        int nuevaCantidad = encontrado.getCantidad() - cantidadSolicitada;
        encontrado.setCantidad(nuevaCantidad);

        //PRODUCTO SIN STOCK SE ELIMINA DEL ÁRBOL
        if (nuevaCantidad <= 0) {
            inventario.eliminarProducto(encontrado.getNombre());
        }


        //AGREGAR AL CARRITO
        cliente.agregarProductoAlCarrito(encontrado, cantidadSolicitada);

        cargarInventario();
        cargarCarrito();

        spCantidad.setValue(1);

        JOptionPane.showMessageDialog(this,
                "Producto agregado al carrito.");
    }

    //ELIMINAR PRODUCTO DEL CARRITO
    private void eliminarProductoCarrito() {
        int fila = tablaCarrito.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un producto del carrito.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreProducto = tablaCarrito.getValueAt(fila, 0).toString();
        int cantidad = Integer.parseInt(tablaCarrito.getValueAt(fila, 1).toString());

        Producto original = inventario.buscarProducto(nombreProducto);
        if (original != null) {
            original.setCantidad(original.getCantidad() + cantidad);
        }

        cliente.getCarrito().eliminarProductoPorNombre(nombreProducto);

        cargarInventario();
        cargarCarrito();

        JOptionPane.showMessageDialog(this,
                "Producto eliminado del carrito exitosamente.");
    }

    // CARGAR TABLA DEL CARRITO (VISUAL)
    private void cargarCarrito() {
        modeloCarrito.setRowCount(0);
        List<Producto> lista = cliente.getCarrito().generarListaProductos();
        for (Producto p : lista) {
            double subtotal = p.getPrecio() * p.getCantidad();
            modeloCarrito.addRow(new Object[]{
                    p.getNombre(),
                    p.getCantidad(),
                    "$" + p.getPrecio(),
                    "$" + subtotal
            });
        }
    }
}
