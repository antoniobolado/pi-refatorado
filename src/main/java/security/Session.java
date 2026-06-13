
package security;

import model.User;

public class Session {

    private static Session instance;
    private User usuarioLogado;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) instance = new Session();
        return instance;
    }

    public void login(User usuario) {
        this.usuarioLogado = usuario;
    }

    public User getUsuario() {
        return usuarioLogado;
    }

    public void logout() {
        this.usuarioLogado = null;
    }

    public boolean isLoggedIn() {
        return usuarioLogado != null;
    }
}
