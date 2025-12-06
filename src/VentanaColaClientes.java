import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

//CLASE VENTANACOLACLIENTES: Muestra la cola de clientes con sus prioridades
public class VentanaColaClientes extends JDialog {

    private ColaClientes cola;
    private JTable tablaCola;
    private DefaultTableModel modelo;

    public VentanaColaClientes(JFrame parent, ColaClientes cola) {
        super(parent, "Cola de Clientes", true);
        this.cola = cola;

        //CONFIGURACIÓN GENERAL DE VENTANA
        setSize(700, 390);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        //FUENTE UNIFICADA
        Font fuente = new Font("Segoe UI", Font.PLAIN, 14);

        //TABLA DE CLIENTES
        String[] columnas = {"Cédula", "Nombre", "Prioridad", "Cant. Productos"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //NINGUNA CELDA EDITABLE
            }
        };

        tablaCola = new JTable(modelo);
        tablaCola.setFont(fuente);
        tablaCola.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaCola.setDefaultRenderer(Object.class, new ColorRenderer());
        tablaCola.setRowHeight(28);

        cargarTabla();

        JScrollPane scroll = new JScrollPane(tablaCola);
        scroll.setBorder(BorderFactory.createTitledBorder("Clientes en cola"));
        add(scroll, BorderLayout.CENTER);

        //BOTÓN INFERIOR
        JButton btnCerrar = new JButton("CERRAR");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.addActionListener(e -> dispose());
        add(btnCerrar, BorderLayout.SOUTH);

        setVisible(true);
    }

    //  LLENAR TABLA DESDE COLA
    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Cliente> listaTemp = cola.obtenerListaClientes();

        for (Cliente c : listaTemp) {
            String prioridadString = convertirPrioridad(c.getPrioridad());
            int cantidadProductos = contarProductosCarrito(c);

            modelo.addRow(new Object[]{
                    c.getCedula(),
                    c.getNombreCompleto(),
                    prioridadString,
                    cantidadProductos
            });
        }
    }

    //CONVERTIR PRIORIDAD NUMÉRICA A TEXTO
    private String convertirPrioridad(int p) {
        return switch (p) {
            case 1 -> "Básico";
            case 2 -> "Afiliado";
            case 3 -> "Premium";
            default -> "Desconocida";
        };
    }

    //CONTAR CANTIDAD TOTAL DE PRODUCTOS EN EL CARRITO
    private int contarProductosCarrito(Cliente cliente) {
        int total = 0;
        Producto nodo = cliente.getCarrito().getPrimero();
        while (nodo != null) {
            total += nodo.getCantidad();
            nodo = nodo.getSiguiente();
        }
        return total;
    }

    //COLORES
    private class ColorRenderer extends JLabel implements TableCellRenderer {
        public ColorRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setHorizontalAlignment(CENTER);
        }
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            //Color por priridad (columna 2)
            String prioridad = table.getValueAt(row, 2).toString();
            switch (prioridad) {
                case "Premium" -> setBackground(new Color(255, 204, 204));  //Rojo suave
                case "Afiliado" -> setBackground(new Color(255, 255, 204)); //Amarillo suave
                default -> setBackground(new Color(235, 235, 235));         //Gris claro
            }

            if (isSelected) {
                setBackground(new Color(150, 188, 255)); //Azul al seleccionar
            }
            return this;
        }
    }
}
