package com.example.myrecyclerviewexample.model;

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

    public boolean addNewUsuario(Usuario u){
        MysqlDB mysqlDB = new MysqlDB();
        boolean operationDone = mysqlDB.addNewUsuario(u);
        usuarios = mysqlDB.getAllUsers();

        return operationDone;

    }

    public boolean updateUsuario(Usuario u){
        MysqlDB mysqlDB = new MysqlDB();
        boolean operationDone = mysqlDB.updateUser(u);
        usuarios = mysqlDB.getAllUsers();

        return operationDone;

    }

    public boolean deleteUsuario(Usuario u){
        MysqlDB mysqlDB = new MysqlDB();
        boolean operationDone = mysqlDB.deleteUser(u);
        usuarios = mysqlDB.getAllUsers();

        return operationDone;

    }


    public void reinsertUser(int position, Usuario u) {
        MysqlDB mysqlDB = new MysqlDB();
        usuarios = mysqlDB.getAllUsers();
        usuarios.add(position, u);
    }
}
