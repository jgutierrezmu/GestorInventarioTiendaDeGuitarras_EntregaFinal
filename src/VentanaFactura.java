import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

//CLASE VENTANAFactura: Muestra el carrito final del cliente y procesa la compra
public class VentanaFactura extends JDialog {

    private Cliente cliente;
    private ArbolProductos inventario;
    private ColaClientes colaClientes;

    private Grafo mapaRutas;
    private String ubicacionTienda;
    private String resumenRutaEntrega;

    private JTable tablaFactura;
    private DefaultTableModel modeloFactura;

    private JLabel lblSubtotal;
    private JLabel lblTotal;

    //CONSTRUCTOR
    public VentanaFactura(JFrame parent, Cliente cliente,
                          ArbolProductos inventario, ColaClientes colaClientes,
                          Grafo mapaRutas, String ubicacionTienda) {

        super(parent, "Factura de " + cliente.getNombreCompleto(), true);
        this.cliente = cliente;
        this.inventario = inventario;
        this.colaClientes = colaClientes;
        this.mapaRutas = mapaRutas;
        this.ubicacionTienda = ubicacionTienda;

        setSize(780, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        Font fuente = new Font("Segoe UI", Font.PLAIN, 14);

        //PANEL SUPERIOR: INFORMACIÓN DEL CLIENTE
        JPanel panelCliente = new JPanel(new GridLayout(5, 1, 2, 2));
        panelCliente.setBorder(BorderFactory.createTitledBorder("Datos del cliente"));

        JLabel lblCedula = new JLabel("Cédula: " + cliente.getCedula());
        JLabel lblNombre = new JLabel("Nombre: " + cliente.getNombreCompleto());
        JLabel lblCorreo = new JLabel("Correo: " + cliente.getCorreo());
        JLabel lblTelefono = new JLabel("Teléfono: " + cliente.getTelefono());
        JLabel lblPrioridad = new JLabel("Prioridad: " + convertirPrioridad(cliente.getPrioridad()));
        JLabel lblUbicacion = new JLabel("Ubicacion: " + cliente.getUbicacion());

        lblCedula.setFont(fuente);
        lblNombre.setFont(fuente);
        lblCorreo.setFont(fuente);
        lblTelefono.setFont(fuente);
        lblPrioridad.setFont(fuente);
        lblUbicacion.setFont(fuente);

        panelCliente.add(lblCedula);
        panelCliente.add(lblNombre);
        panelCliente.add(lblCorreo);
        panelCliente.add(lblTelefono);
        panelCliente.add(lblPrioridad);
        panelCliente.add(lblUbicacion);

        add(panelCliente, BorderLayout.NORTH);

        //TABLA DE DETALLE (CARRITO)
        String[] columnas = {"Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        modeloFactura = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaFactura = new JTable(modeloFactura);
        tablaFactura.setFont(fuente);
        tablaFactura.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaFactura.setRowHeight(28);
        cargarCarritoEnTabla();

        JScrollPane scroll = new JScrollPane(tablaFactura);
        scroll.setBorder(BorderFactory.createTitledBorder("Detalle de compra"));

        add(scroll, BorderLayout.CENTER);

        //TOTALES
        JPanel panelTotales = new JPanel(new GridLayout(2, 1, 5, 5));
        panelTotales.setBorder(BorderFactory.createTitledBorder("Totales"));

        lblSubtotal = new JLabel();
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 14));

        lblTotal = new JLabel();
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 15));

        panelTotales.add(lblSubtotal);
        panelTotales.add(lblTotal);

        actualizarTotales();

        add(panelTotales, BorderLayout.EAST);

        //BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnConfirmar = new JButton("CONFIRMAR COMPRA");
        JButton btnCancelar = new JButton("CANCELAR");

        btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        resumenRutaEntrega = calcularRutaEntrega();

        //EVENTOS
        btnConfirmar.addActionListener(e -> procesarCompra());
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    //CARGAR CARRITO EN TABLA
    private void cargarCarritoEnTabla() {
        modeloFactura.setRowCount(0);
        Producto nodo = cliente.getCarrito().getPrimero();

        while (nodo != null) {
            double subtotal = nodo.getCantidad() * nodo.getPrecio();
            modeloFactura.addRow(new Object[]{
                    nodo.getNombre(),
                    nodo.getCantidad(),
                    "$" + nodo.getPrecio(),
                    "$" + subtotal
            });
            nodo = nodo.getSiguiente();
        }
    }

    //CALCULAR Y MOSTRAR TOTALES
    private void actualizarTotales() {
        double subtotal = cliente.getCarrito().calcularCostoTotal();
        double total = subtotal;

        lblSubtotal.setText("Subtotal: $" + subtotal);
        lblTotal.setText("Total a pagar: $" + total);
    }

    //CAMINO MAS CORTO CON DIJKSTRA
    private String calcularRutaEntrega() {

        String origen = ubicacionTienda;
        String destino = cliente.getUbicacion();

        if (destino == null || destino.trim().isEmpty()) {
            return "No se ha definido una ubicación para el cliente.";
        }

        Map<String, Integer> distancias = new HashMap<>();
        Map<String, String> predecesores = new HashMap<>();

        mapaRutas.algoritmoDijkstra(origen, distancias, predecesores);

        Integer distanciaFinal = distancias.get(destino);
        if (distanciaFinal == null || distanciaFinal == Integer.MAX_VALUE) {
            return "No existe una ruta registrada entre " + origen + " y " + destino + ".";
        }

        java.util.List<String> camino = mapaRutas.reconstruirCamino(origen, destino, predecesores);
        if (camino.isEmpty()) {
            return "No fue posible reconstruir el camino entre " + origen + " y " + destino + ".";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Ubicación de la tienda: ").append(origen).append("\n");
        sb.append("Ubicación del cliente: ").append(destino).append("\n\n");
        sb.append("Camino más corto:\n  ");

        for (int i = 0; i < camino.size(); i++) {
            sb.append(camino.get(i));
            if (i < camino.size() - 1) {
                sb.append(" -> ");
            }
        }

        sb.append("\n\nDistancia total: ").append(distanciaFinal).append(" km (aprox.)");

        return sb.toString();
    }

    //PROCESAR COMPRA
    private void procesarCompra() {
        JOptionPane.showMessageDialog(this,
                "Compra procesada exitosamente.\n\n" + resumenRutaEntrega,
                "Factura completada",
                JOptionPane.INFORMATION_MESSAGE);

        colaClientes.atenderCliente();
        cliente.getCarrito().setPrimero(null);

        dispose();
    }

    //METODO PARA CONVERTIR PRIORIDAD A TEXTO
    private String convertirPrioridad(int p) {
        return switch (p) {
            case 1 -> "Básico";
            case 2 -> "Afiliado";
            case 3 -> "Premium";
            default -> "Desconocida";
        };
    }
}
