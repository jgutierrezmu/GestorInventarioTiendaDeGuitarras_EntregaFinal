import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

//CLASE VENTANAREGISTRARCLIENTE: Permite agregar clientes al sistema
public class VentanaRegistrarCliente extends JDialog {

    private ColaClientes colaClientes;
    private ArbolProductos inventario;

    //CAMPOS DE TEXTO
    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JPasswordField txtContrasenna;
    private JTextField txtTelefono;
    private JComboBox<String> cbPrioridad;
    private JComboBox<String> cbUbicacion; // UBICACION DE CLIENTE

    //CONSTRUCTOR
    public VentanaRegistrarCliente(JFrame parent, ColaClientes cola, ArbolProductos inventario) {

        super(parent, "Registrar nuevo cliente", true);
        this.colaClientes = cola;
        this.inventario = inventario;

        //CONFIGURACIÓN GENERAL
        setSize(480, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        //FUENTE UNIFICADA
        Font fuente = new Font("Segoe UI", Font.PLAIN, 14);

        //PANEL PRINCIPAL
        JPanel panelCampos = new JPanel(new GridLayout(8, 2, 8, 8));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        //CÉDULA
        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setFont(fuente);
        txtCedula = new JTextField();
        txtCedula.setFont(fuente);
        panelCampos.add(lblCedula);
        panelCampos.add(txtCedula);

        //NOMBRE
        JLabel lblNombre = new JLabel("Nombre completo:");
        lblNombre.setFont(fuente);
        txtNombre = new JTextField();
        txtNombre.setFont(fuente);
        panelCampos.add(lblNombre);
        panelCampos.add(txtNombre);

        //CORREO
        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setFont(fuente);
        txtCorreo = new JTextField();
        txtCorreo.setFont(fuente);
        panelCampos.add(lblCorreo);
        panelCampos.add(txtCorreo);

        //CONTRASEÑA
        JLabel lblContrasenna = new JLabel("Contraseña:");
        lblContrasenna.setFont(fuente);
        txtContrasenna = new JPasswordField();
        txtContrasenna.setFont(fuente);
        panelCampos.add(lblContrasenna);
        panelCampos.add(txtContrasenna);

        //TELEFONO
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(fuente);
        txtTelefono = new JTextField();
        txtTelefono.setFont(fuente);
        panelCampos.add(lblTelefono);
        panelCampos.add(txtTelefono);

        //PRIORIDAD
        JLabel lblPrioridad = new JLabel("Prioridad:");
        lblPrioridad.setFont(fuente);
        cbPrioridad = new JComboBox<>(new String[]{
                "1 - Básico",
                "2 - Afiliado",
                "3 - Premium"
        });
        cbPrioridad.setFont(fuente);
        panelCampos.add(lblPrioridad);
        panelCampos.add(cbPrioridad);

        //UBICACIÓN (ASOCIADA AL GRAFO)
        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setFont(fuente);
        cbUbicacion = new JComboBox<>(new String[]{
                "San José",
                "Alajuela",
                "Cartago",
                "Heredia",
                "Puntarenas",
                "Limón",
                "Guanacaste"
        });

        cbUbicacion.setFont(fuente);
        panelCampos.add(lblUbicacion);
        panelCampos.add(cbUbicacion);

        //BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnRegistrar = new JButton("REGISTRAR CLIENTE");
        JButton btnCancelar = new JButton("CANCELAR");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);

        //AGREGAR PANELES
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        //ACCIONES DE BOTONES
        btnRegistrar.addActionListener(e -> registrarCliente());
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    //VALIDACIONES Y REGISTRO DE CLIENTE
    private void registrarCliente() {
        try {
            String cedula = txtCedula.getText().trim();
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String contrasenna = new String(txtContrasenna.getPassword());
            String telefono = txtTelefono.getText().trim();
            int prioridad = cbPrioridad.getSelectedIndex() + 1;
            String ubicacion = cbUbicacion.getSelectedItem().toString();

            //VALIDACIONES DE CAMPOS VACÍOS
            if (cedula.isEmpty() || nombre.isEmpty() || correo.isEmpty() || contrasenna.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe completar todos los campos obligatorios.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //VALIDACION DE CEDULA DUPLICADA
            if (colaClientes.cedulaExistente(cedula)) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe un cliente con esa cédula en la cola.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //VALIDACIÓN EMAIL
            if (!Pattern.matches("^[\\w-\\.]+@[\\w-]+\\.[A-Za-z]{2,4}$", correo)) {
                JOptionPane.showMessageDialog(this,
                        "El correo no tiene un formato válido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //VALIDACIÓN TELÉFONO SOLO NÚMEROS
            if (!telefono.matches("\\d{8,12}")) {
                JOptionPane.showMessageDialog(this,
                        "El teléfono debe contener entre 8 y 12 dígitos numéricos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //VALIDACIÓN CONTRASEÑA MÍNIMO 6 CARACTERES
            if (contrasenna.length() < 6) {
                JOptionPane.showMessageDialog(this,
                        "La contraseña debe tener al menos 6 caracteres.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //CREACION DEL CLIENTE
            Cliente nuevo = new Cliente(cedula, nombre, correo, contrasenna, telefono, prioridad, ubicacion);

            //INSERCION A COLA
            colaClientes.insertarCliente(nuevo);

            JOptionPane.showMessageDialog(this,
                    "Cliente registrado correctamente.\nAhora deberá llenar su carrito.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            dispose();

            //ABRIR VENTANA DE CARRITO
            new VentanaCarrito((JFrame) getParent(), nuevo, inventario);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado en los datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
