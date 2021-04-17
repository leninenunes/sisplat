/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 *
 * @author lenine.nunes
 */
@ManagedBean (name = "localeMB")
@SessionScoped

public class LocaleMB {
    private Locale currentLocale = new Locale("pt", "BR");
    
    public void englishLocale(){
        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        currentLocale = Locale.US;
        viewRoot.setLocale(currentLocale);
    }
    
    public void portugueseLocale(){
        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        currentLocale = new Locale("pt", "BR");
        viewRoot.setLocale(currentLocale);
    }
    
    public Locale getCurrentLocale(){
        return currentLocale;
    }
    
    public String getClassPt(){
        if("pt_BR".equals(currentLocale.toString())){
            return "text-success";
        }
        return "text-light";
    }
    
    public String getClassEn(){
        if("en_US".equals(currentLocale.toString())){
            return "text-success";
        }
        return "text-light";
    }
}
