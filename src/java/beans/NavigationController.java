/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author lenine.nunes
 */
@ManagedBean (name = "navigationController")
@RequestScoped
public class NavigationController implements Serializable {
    
    public NavigationController() {
    }
    public String home(){
        return "index?faces-redirect=true";
    }
    
    public String usuario(){
        return "/usuario/List?faces-redirect=true";
    }
        
    public String rt(){
        return "/rt/List?faces-redirect=true";
    }
    
    public String apropriacao(){
        return "/apropriacao/List?faces-redirect=true";
    }
    
    public String local(){
        return "/local/List?faces-redirect=true";
    }
    
    public String contrato(){
        return "/contrato/List?faces-redirect=true";
    }
    
    public String escopo(){
        return "/escopo/List?faces-redirect=true";
    }
    
    public String unidadeMedida(){
        return "/unidadeMedida/List?faces-redirect=true";
    }
    
    public String empresa(){
        return "/empresa/List?faces-redirect=true";
    }
    
    public String profissional(){
        return "/profissional/List?faces-redirect=true";
    }
    
    public String funcao(){
        return "/funcao/List?faces-redirect=true";
    }
    
    public String rtHasProfissional(){
        return "/rtHasProfissional/List?faces-redirect=true";
    }
    
    public String dashboardEmbarques(){
        return "/dashboards/embarques?faces-redirect=true";
    }
    
    public String dashboardDesmbarques(){
        return "/dashboards/desembarques?faces-redirect=true";
    }
    
    public String dashboardApropriacoes(){
        return "/dashboards/apropriacoes?faces-redirect=true";
    }
    
    public String dashboardProfissionais(){
        return "/dashboards/profissionais?faces-redirect=true";
    }
}
