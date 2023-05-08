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

    public boolean addUsuario(Usuario u){
        MysqlDB mysqlDB = new MysqlDB();
        Usuario usuarioCreado = mysqlDB.addNewUsuario(u);
        usuarios.add(u);

        return usuarioCreado != null;

    }

    public boolean addUsuarioById(int idUsuario, Usuario u){
        MysqlDB mysqlDB = new MysqlDB();
        Usuario usuarioCreado = mysqlDB.addNewUsuarioById(idUsuario,u);
        usuarios.add(u);

        return usuarioCreado != null;

    }

    public boolean updateUsuario(Usuario u){

        MysqlDB mysqlDB = new MysqlDB();
        boolean operationDone = mysqlDB.updateUser(u);

        if(operationDone){
            Usuario usuario = usuarios.stream()
                    .filter(user->user.getIdUsuario()==1)
                    .findFirst()
                    .get();

            usuario.setNombre(u.getNombre());
            usuario.setApellidos(u.getApellidos());
            usuario.setOficio(u.getOficio());
        }


        return operationDone;

    }

    public boolean deleteUsuario(Usuario u){
        MysqlDB mysqlDB = new MysqlDB();
        boolean operationDone = mysqlDB.deleteUser(u);
        usuarios = mysqlDB.getAllUsers();

        return operationDone;

    }

}
