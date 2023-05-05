package com.example.myrecyclerviewexample.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private static Model model;
    private List<Usuario> usuarios;
    private List<Oficio> oficios;

    private Model() {
        usuarios = new ArrayList<>();
        oficios = new ArrayList<>();
    }


    public static Model getInstance() {
        if (model == null)
            model = new Model();

        return model;
    }

    public List<Usuario> getUsuarios() {
        if (usuarios.isEmpty()) {
            MysqlDB mysqlDB = new MysqlDB();
            usuarios = mysqlDB.getAllUsers();
        }
        return usuarios;
    }

    public List<Oficio> getOficios() {
        if (oficios.isEmpty()) {
            MysqlDB mysqlDB = new MysqlDB();
            oficios = mysqlDB.getAllOficios();
        }
        return oficios;
    }



//    public int updateUsuario(Usuario u){
//
//        String sql = "UPDATE Usuario SET nombre='"+ u.getNombre()+"',apellidos='"+ u.getApellidos()+"',Oficio_idOficio="+u.getOficio()+"WHERE idUsuario = " + u.getIdUsuario();
//
//        try(Connection connection= getConnection();
//            Statement stmnt = connection.createStatement();
//        ) {
//
//            return  stmnt.executeUpdate(sql);
//
//        } catch (SQLException ex) {
//            throw new RuntimeException(ex);
//        }
//
//    }


}
