/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import beans.util.JsfUtil;
import beans.util.SessionContext;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author lenine.nunes
 */
@ManagedBean(name="loginController")
@SessionScoped
public class LoginController implements Serializable {
    boolean logged = false;
    private String login;
    private String senha;
    
    public LoginController() {
    }
    
    public String doLogin(){
        if("lenine".equals(login) && "admin".equals(senha)){
            SessionContext.getInstance().setAttribute("usuarioLogado","TESTE");
            return "/index?faces-redirect=true";
        }
        
        FacesContext context = FacesContext.getCurrentInstance();
        JsfUtil.addErrorMessage(FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("UserLogin"));
        return null;
    }
    
    public String doLogout(){
        SessionContext.getInstance().encerrarSessao();
        return "/security/login?faces-redirecencerrarSessaot=true";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}
