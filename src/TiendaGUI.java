import javax.swing.*;
import java.awt.*;

//MENU PRINCIPAL DEL SISTEMA
public class TiendaGUI extends JFrame {

    //ATRIBUTOS PRINCIPALES DEL SISTEMA
    private ArbolProductos inventario;
    private ColaClientes colaClientes;

    //ATRIBUTOS PARA GRAFO
    private Grafo mapaRutas;
    private final String UBICACION_TIENDA = "San José";

    //CONSTRUCTOR
    public TiendaGUI() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus no disponible, usando look and feel por defecto.");
        }

        setTitle("Sistema de Gestión - Tienda de Guitarras");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);

        inventario = new ArbolProductos();
        colaClientes = new ColaClientes();
        cargarInventarioInicial();

        //CARGAR EL GRAFO PREDETERMINADO
        mapaRutas = new Grafo();
        cargarMapaRutas();

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(5, 1, 10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnGestionarProductos = new JButton("Gestionar inventario");
        JButton btnRegistrarCliente = new JButton("Registrar cliente");
        JButton btnVerCola = new JButton("Ver cola de clientes");
        JButton btnAtenderCliente = new JButton("Atender cliente");
        JButton btnSalir = new JButton("Salir");

        Font fuente = new Font("Segoe UI", Font.BOLD, 14);
        btnGestionarProductos.setFont(fuente);
        btnRegistrarCliente.setFont(fuente);
        btnVerCola.setFont(fuente);
        btnAtenderCliente.setFont(fuente);
        btnSalir.setFont(fuente);

        panelPrincipal.add(btnGestionarProductos);
        panelPrincipal.add(btnRegistrarCliente);
        panelPrincipal.add(btnVerCola);
        panelPrincipal.add(btnAtenderCliente);
        panelPrincipal.add(btnSalir);

        add(panelPrincipal);

        btnGestionarProductos.addActionListener(e -> new VentanaInventarioProductos(inventario));
        btnRegistrarCliente.addActionListener(e -> new VentanaRegistrarCliente(TiendaGUI.this, colaClientes, inventario));
        btnVerCola.addActionListener(e -> {
            if (colaClientes.estaVacia()) {
                JOptionPane.showMessageDialog(
                        TiendaGUI.this,
                        "No hay clientes en la cola.",
                        "Cola vacía",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            new VentanaColaClientes(TiendaGUI.this, colaClientes);
        });

        //BOTON ATENDER CLIENTE
        btnAtenderCliente.addActionListener(e -> {
            if (colaClientes.estaVacia()) {
                JOptionPane.showMessageDialog(
                        TiendaGUI.this,
                        "No hay clientes en la cola.",
                        "Cola vacía",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            Cliente siguiente = colaClientes.verFrente();
            new VentanaFactura(TiendaGUI.this, siguiente, inventario,
                    colaClientes, mapaRutas, UBICACION_TIENDA);
        });
        btnSalir.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TiendaGUI().setVisible(true));
    }

    //METODO PARA CARGAR INVENTARIO POR DEFAULT
    private void cargarInventarioInicial() {

        java.util.ArrayList<String> imagenes = new java.util.ArrayList<>();

        imagenes.clear();
        imagenes.add("img/fender_strat.jpg");
        inventario.insertarProducto(new Producto("Fender Stratocaster", "Fender", "Guitarra Eléctrica",
                1200.00, 5, imagenes, "Modelo clásico con 3 single coils."));

        imagenes.clear();
        imagenes.add("img/gibson_lespaul.jpg");
        inventario.insertarProducto(new Producto("Gibson Les Paul Standard", "Gibson", "Guitarra Eléctrica",
                1800.00, 3, imagenes, "Cuerpo sólido, pastillas humbucker."));

        imagenes.clear();
        imagenes.add("img/ibanez_rg.jpg");
        inventario.insertarProducto(new Producto("Ibanez RG450", "Ibanez", "Guitarra Eléctrica",
                650.00, 8, imagenes, "Guitarra rápida ideal para metal."));

        imagenes.clear();
        imagenes.add("img/taylor_214ce.jpg");
        inventario.insertarProducto(new Producto("Taylor 214ce", "Taylor", "Guitarra Acústica",
                999.99, 4, imagenes, "Construcción de alta calidad, electroacústica."));

        imagenes.clear();
        imagenes.add("img/yamaha_f310.jpg");
        inventario.insertarProducto(new Producto("Yamaha F310", "Yamaha", "Guitarra Acústica",
                180.00, 10, imagenes, "Ideal para principiantes."));

        imagenes.clear();
        imagenes.add("img/fender_champion.jpg");
        inventario.insertarProducto(new Producto("Fender Champion 20", "Fender", "Amplificador",
                120.00, 7, imagenes, "Amplificador sólido de 20W."));

        imagenes.clear();
        imagenes.add("img/marshall_code50.jpg");
        inventario.insertarProducto(new Producto("Marshall Code 50", "Marshall", "Amplificador",
                350.00, 4, imagenes, "Modelado digital con presets clásicos."));

        imagenes.clear();
        imagenes.add("img/boss_ds1.jpg");
        inventario.insertarProducto(new Producto("Boss DS-1 Distortion", "Boss", "Pedal",
                55.00, 12, imagenes, "Pedal de distorsión clásico."));

        imagenes.clear();
        imagenes.add("img/crybaby_wah.jpg");
        inventario.insertarProducto(new Producto("Cry Baby Wah", "Dunlop", "Pedal",
                90.00, 6, imagenes, "Uno de los wah más famosos del mundo."));
    }

    //CARGAR MAPA DE RUTAS PREDETERMINADO
    private void cargarMapaRutas() {

        //VÉRTICES (PROVINCIAS)
        mapaRutas.agregarVertice("San José");
        mapaRutas.agregarVertice("Alajuela");
        mapaRutas.agregarVertice("Cartago");
        mapaRutas.agregarVertice("Heredia");
        mapaRutas.agregarVertice("Puntarenas");
        mapaRutas.agregarVertice("Limón");
        mapaRutas.agregarVertice("Guanacaste");

        //ARISTAS (DISTANCIAS EN KM APROX)
        mapaRutas.agregarArista("San José", "Alajuela", 22);
        mapaRutas.agregarArista("San José", "Cartago", 25);
        mapaRutas.agregarArista("San José", "Heredia", 10);
        mapaRutas.agregarArista("San José", "Limón", 95);
        mapaRutas.agregarArista("San José", "Puntarenas", 100);
        mapaRutas.agregarArista("Alajuela", "Puntarenas", 80);
        mapaRutas.agregarArista("Alajuela", "Guanacaste", 130);
        mapaRutas.agregarArista("Heredia", "Alajuela", 17);
        mapaRutas.agregarArista("Cartago", "Limón", 110);
        mapaRutas.agregarArista("Guanacaste", "Puntarenas", 145);
    }

}
