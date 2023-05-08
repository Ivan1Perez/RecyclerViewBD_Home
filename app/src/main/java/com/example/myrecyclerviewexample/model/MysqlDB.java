package com.example.myrecyclerviewexample.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MysqlDB {

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return DriverManager.getConnection("jdbc:mysql://192.168.1.36:3306/java","iperez","1111");
    }

    public List<Usuario> getAllUsers(){
        List<Usuario> usuarios = new ArrayList<>();

        try(Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Usuario")
        ){
            int id,oficio;
            String nombre,apellidos;
            while(rs.next()){
                id=rs.getInt("idUsuario");
                nombre = rs.getString("nombre");
                apellidos= rs.getString("apellidos");
                oficio = rs.getInt("Oficio_idOficio");
                usuarios.add(new Usuario(id,nombre,apellidos,oficio));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public List<Oficio> getAllOficios() {
        List<Oficio> oficios = new ArrayList<>();

        try(Connection c = getConnection();
            Statement statement = c.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Oficio")
        ){

            int idOficio;
            String descripcion;

            while (rs.next()){
                idOficio = rs.getInt("idOficio");
                descripcion = rs.getString("descripcion");
                oficios.add(new Oficio(idOficio,descripcion));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return oficios;
    }

    public boolean addNewUsuario(Usuario u){
        String sql = "INSERT INTO Usuario (nombre,apellidos,Oficio_idOficio)" +
                "VALUES ('" +
                u.getNombre()+"', '"+
                u.getApellidos()+"', "+
                u.getOficio()+")";

        try(Connection connection = getConnection();
            Statement stmnt = connection.createStatement();) {
            return stmnt.execute(sql);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    public boolean updateUser(Usuario u) {
        String sql = "UPDATE Usuario SET nombre = '" + u.getNombre() + "', apellidos = '" + u.getApellidos() + "', Oficio_idOficio = " + u.getOficio() + " WHERE idUsuario = " + u.getIdUsuario();
        int rowsAffected;

        try(Connection connection = getConnection();
            Statement stmnt = connection.createStatement();) {

            rowsAffected = stmnt.executeUpdate(sql);
            if(rowsAffected > 0){
                return true;
            }
            return false;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean deleteUser(Usuario u) {
        String sql = "DELETE FROM Usuario WHERE idUsuario = " + u.getIdUsuario();
        int rowsAffected;

        try(Connection connection = getConnection();
            Statement stmnt = connection.createStatement();) {

            rowsAffected = stmnt.executeUpdate(sql);
            if(rowsAffected > 0){
                return true;
            }
            return false;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
