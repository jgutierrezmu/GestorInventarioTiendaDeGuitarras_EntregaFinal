import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//VENTANA INVENTARIO: PERMITE AGREGAR, VER Y ELIMINAR PRODUCTOS DEL ÁRBOL
public class VentanaInventarioProductos extends JFrame {

    //REFERENCIA AL INVENTARIO
    private ArbolProductos inventario;

    //CARDLAYOUT PARA CAMBIAR ENTRE PANELES
    private JPanel panelContenido;
    private CardLayout cardLayout;

    //COMPONENTES PARA TABLA (SOLO SE USAN EN BOTON "VER INVENTARIO")
    private JTable tablaInventario;
    private DefaultTableModel modeloInventario;

    //CONSTRUCTOR PRINCIPAL
    public VentanaInventarioProductos(ArbolProductos inventario) {
        this.inventario = inventario;

        setTitle("Gestión de Inventario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);

        //PANEL PRINCIPAL
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //PANEL SUPERIOR CON BOTONES DE ACCIÓN
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));

        Font fuenteBotones = new Font("Segoe UI", Font.BOLD, 12);

        JButton btnAgregar = new JButton("AGREGAR PRODUCTO");
        JButton btnEliminar = new JButton("ELIMINAR PRODUCTO");
        JButton btnVer = new JButton("VER INVENTARIO");

        btnAgregar.setFont(fuenteBotones);
        btnEliminar.setFont(fuenteBotones);
        btnVer.setFont(fuenteBotones);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVer);

        //CARDLAYOUT
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        //SE CREAN LOS PANELES
        crearPanelAgregar();
        crearPanelEliminar();
        crearPanelVerInventario();

        //BOTON PARA VOLVER
        JButton btnVolver = new JButton("VOLVER AL MENÚ PRINCIPAL");
        btnVolver.setFont(fuenteBotones);

        //SE AGREGAN LOS COMPONENTES AL PANEL PRINCIPAL
        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(btnVolver, BorderLayout.SOUTH);

        add(panelPrincipal);

        //EVENTOS PARA BOTONES
        btnAgregar.addActionListener(e -> cardLayout.show(panelContenido, "agregar"));
        btnEliminar.addActionListener(e -> cardLayout.show(panelContenido, "eliminar"));

        //ANTES DE MOSTRAR EL PANEL, SE CARGA LA TABLA
        btnVer.addActionListener(e -> {
            cargarTablaInventario();
            cardLayout.show(panelContenido, "ver");
        });

        btnVolver.addActionListener(e -> dispose());

        setVisible(true);
    }

    // PESTAÑA "AGREGAR PRODUCTO"
    private void crearPanelAgregar() {

        //PANEL FORMULARIO AGREGAR
        JPanel panelAgregar = new JPanel(new GridLayout(8, 2, 10, 10));
        panelAgregar.setBorder(BorderFactory.createTitledBorder("AGREGAR NUEVO PRODUCTO"));

        Font fuente = new Font("Segoe UI", Font.PLAIN, 13);

        JTextField txtNombre = new JTextField();
        JTextField txtMarca = new JTextField();
        JComboBox<String> cbCategoria = new JComboBox<>(new String[]{
                "Guitarra Eléctrica",
                "Guitarra Acústica",
                "Amplificador",
                "Pedal / Efecto",
                "Accesorio",
                "Cuerdas",
                "Cable",
                "Plumilla",
                "Otro"
        });
        JTextField txtPrecio = new JTextField();
        JTextField txtCantidad = new JTextField();
        JTextField txtNotas = new JTextField();
        JTextField txtImagenes = new JTextField();

        txtNombre.setFont(fuente);
        txtMarca.setFont(fuente);
        cbCategoria.setFont(fuente);
        txtPrecio.setFont(fuente);
        txtCantidad.setFont(fuente);
        txtNotas.setFont(fuente);
        txtImagenes.setFont(fuente);

        panelAgregar.add(new JLabel("Nombre:"));
        panelAgregar.add(txtNombre);

        panelAgregar.add(new JLabel("Marca:"));
        panelAgregar.add(txtMarca);

        panelAgregar.add(new JLabel("Categoría:"));
        panelAgregar.add(cbCategoria);

        panelAgregar.add(new JLabel("Precio ($):"));
        panelAgregar.add(txtPrecio);

        panelAgregar.add(new JLabel("Cantidad:"));
        panelAgregar.add(txtCantidad);

        panelAgregar.add(new JLabel("Notas adicionales:"));
        panelAgregar.add(txtNotas);

        panelAgregar.add(new JLabel("Imagenes (separe con comas):"));
        panelAgregar.add(txtImagenes);

        JButton btnGuardar = new JButton("GUARDAR PRODUCTO");
        panelAgregar.add(new JLabel()); //ESPACIADO
        panelAgregar.add(btnGuardar);

        //EVENTO BOTÓN GUARDAR
        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                String marca = txtMarca.getText().trim();
                String categoria = cbCategoria.getSelectedItem().toString();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                String notas = txtNotas.getText().trim();

                ArrayList<String> listaImagenes = new ArrayList<>();
                if (!txtImagenes.getText().trim().isEmpty()) {
                    String[] rutas = txtImagenes.getText().split(",");
                    for (String r : rutas) listaImagenes.add(r.trim());
                }

                Producto nuevo = new Producto(nombre, marca, categoria, precio, cantidad, listaImagenes, notas);
                inventario.insertarProducto(nuevo);

                JOptionPane.showMessageDialog(null,
                        "Producto agregado correctamente.");

                txtNombre.setText("");
                txtMarca.setText("");
                txtPrecio.setText("");
                txtCantidad.setText("");
                txtNotas.setText("");
                txtImagenes.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error. Verifique los datos ingresados.");
            }
        });

        panelContenido.add(panelAgregar, "agregar");
    }

    // PESTAÑA "ELIMINAR PRODUCTO"
    private void crearPanelEliminar() {

        JPanel panelEliminar = new JPanel(new BorderLayout(10, 10));
        panelEliminar.setBorder(BorderFactory.createTitledBorder("ELIMINAR PRODUCTO"));

        JPanel panelBuscar = new JPanel(new FlowLayout());
        JLabel lblNombre = new JLabel("Nombre del producto a eliminar:");
        JTextField txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("BUSCAR");

        panelBuscar.add(lblNombre);
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);

        JTextArea areaInfo = new JTextArea(8, 45);
        areaInfo.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaInfo);

        panelEliminar.add(panelBuscar, BorderLayout.NORTH);
        panelEliminar.add(scroll, BorderLayout.CENTER);

        //EVENTO DE BOTÓN BUSCAR
        btnBuscar.addActionListener(e -> {
            String nombre = txtBuscar.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Ingrese un nombre para buscar.");
                return;
            }

            Producto prod = inventario.buscarProducto(nombre);

            if (prod == null) {
                areaInfo.setText("No se encontró el producto.");
                return;
            }

            areaInfo.setText(
                    "Producto encontrado:\n\n" +
                            "Nombre: " + prod.getNombre() + "\n" +
                            "Marca: " + prod.getMarca() + "\n" +
                            "Categoría: " + prod.getCategoria() + "\n" +
                            "Precio: $" + prod.getPrecio() + "\n" +
                            "Cantidad: " + prod.getCantidad() + "\n" +
                            "Notas: " + prod.getNotasAdicionales() + "\n\n" +
                            "¿Desea eliminar este producto?"
            );

            JButton btnConfirmar = new JButton("CONFIRMAR ELIMINACIÓN");

            JPanel panelConf = new JPanel(new FlowLayout());
            panelConf.add(btnConfirmar);

            panelEliminar.add(panelConf, BorderLayout.SOUTH);
            panelEliminar.revalidate();
            panelEliminar.repaint();

            //EVENTO CONFIRMAR ELIMINACIÓN
            btnConfirmar.addActionListener(ev -> {
                inventario.eliminarProducto(nombre);
                JOptionPane.showMessageDialog(null,
                        "Producto eliminado correctamente.");
                areaInfo.setText("");
                txtBuscar.setText("");
                panelEliminar.remove(panelConf);
                panelEliminar.revalidate();
                panelEliminar.repaint();
            });
        });

        panelContenido.add(panelEliminar, "eliminar");
    }

    // PESTAÑA "VER INVENTARIO"
    private void crearPanelVerInventario() {

        //PANEL TABLA INVENTARIO
        JPanel panelVer = new JPanel(new BorderLayout());
        panelVer.setBorder(BorderFactory.createTitledBorder("INVENTARIO ACTUAL"));

        String[] columnas = {"Nombre", "Marca", "Categoría", "Precio", "Cantidad", "Notas"};

        modeloInventario = new DefaultTableModel(columnas, 0);
        tablaInventario = new JTable(modeloInventario);

        JScrollPane scroll = new JScrollPane(tablaInventario);
        panelVer.add(scroll, BorderLayout.CENTER);

        panelContenido.add(panelVer, "ver");
    }

    //METODO DE APOYO PARA CARGAR TABLA DESDE ABB
    private void cargarTablaInventario() {
        modeloInventario.setRowCount(0);

        List<Producto> lista = new ArrayList<>();
        inventario.agregarProductosATablaInventario(lista);

        for (Producto p : lista) {
            modeloInventario.addRow(new Object[]{
                    p.getNombre(),
                    p.getMarca(),
                    p.getCategoria(),
                    "$" + p.getPrecio(),
                    p.getCantidad(),
                    p.getNotasAdicionales()
            });
        }
    }
}
