package Proveedores;

import Conexion.ConexionDB;
import PruebaMenu.MenuPrueba;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProveedoresGUI {
    private JTable table1;
    private JTextField id_proveedorTextField;
    private JTextField contactoTextField;
    private JTextField nombreTextField;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton actualizarButton;
    private JPanel main;
    private JComboBox categoriaComboBox;
    private JButton volverAlMenúButton;
    private int filas = 0;

    private ProveedoresDAO proveedoresDAO = new ProveedoresDAO();
    private ConexionDB conexionDB = new ConexionDB();

    public JPanel getMainPanel() {
        return main; // Return the actual main panel instead of null
    }

    public ProveedoresGUI() {
        id_proveedorTextField.setEnabled(false);
        obtenerDatos();

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreTextField.getText();
                String contacto = contactoTextField.getText();
                String categoria_producto = categoriaComboBox.getSelectedItem().toString();

                Proveedores proveedores = new Proveedores(0, nombre, contacto, categoria_producto);
                proveedoresDAO.agregar(proveedores);
                obtenerDatos();
                limpiar();
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreTextField.getText();
                String contacto = contactoTextField.getText();
                String categoria_producto = categoriaComboBox.getSelectedItem().toString();
                int id_proveedor = Integer.parseInt(id_proveedorTextField.getText());

                Proveedores proveedores = new Proveedores(id_proveedor, nombre, contacto, categoria_producto);
                proveedoresDAO.actualizar(proveedores);
                obtenerDatos();
                limpiar();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id_proveedor = Integer.parseInt(id_proveedorTextField.getText());
                proveedoresDAO.eliminar(id_proveedor);
                obtenerDatos();
                limpiar();
            }
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectFila = table1.getSelectedRow();

                if (selectFila >= 0) {
                    id_proveedorTextField.setText(table1.getValueAt(selectFila, 0).toString());
                    nombreTextField.setText(table1.getValueAt(selectFila, 1).toString());
                    contactoTextField.setText(table1.getValueAt(selectFila, 2).toString());
                    categoriaComboBox.setSelectedItem(table1.getValueAt(selectFila, 3).toString());

                    filas = selectFila;
                }
            }
        });
        volverAlMenúButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(volverAlMenúButton);
                jFrame.dispose();
                MenuPrueba.main(null);
            }
        });
    }

    public void limpiar() {
        id_proveedorTextField.setText("");
        nombreTextField.setText("");
        contactoTextField.setText("");
        categoriaComboBox.setSelectedItem("");
    }

    public void obtenerDatos() {
        DefaultTableModel dtm = new DefaultTableModel();

        dtm.addColumn("Id_proveedor");
        dtm.addColumn("Nombre");
        dtm.addColumn("Contacto");
        dtm.addColumn("Categoría_producto");

        table1.setModel(dtm);
        Object[] dato = new Object[4];
        Connection con = conexionDB.getConnection();

        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM proveedores";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                dato[0] = rs.getInt(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getString(4);
                dtm.addRow(dato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Gestión de Proveedores");
        frame.setContentPane(new ProveedoresGUI().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1006,550);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }
}