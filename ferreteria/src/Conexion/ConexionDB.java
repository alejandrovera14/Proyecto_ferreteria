package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionDB {
    public static Connection getConnection(){
        Connection con = null;
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto_ferreteria","root","");
        } catch (Exception e){
            e.printStackTrace();
        }
        return con;
    }
}