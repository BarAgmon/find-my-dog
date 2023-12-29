package com.idz.lecture4_demo3.Model;

public class Model {
    private static final Model instance = new Model();

    public static Model instance(){
        return instance;
    }

//    public void signIn(String email, String password, ModelFirebase.SignIn listener) {
//        modelFirebase.signIn(email, password, listener);
//    }
}
