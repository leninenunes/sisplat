package beans;

import model.Profissional;
import beans.util.JsfUtil;
import beans.util.PaginationHelper;
import facade.ProfissionalJpaController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.Persistence;

@ManagedBean(name = "profissionalController")
@SessionScoped
public class ProfissionalController implements Serializable {

    private Profissional current;
    private DataModel items = null;
    private ProfissionalJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Integer pageSize = 10;
    private Profissional filtered;
    private String filterShow;
    private Boolean statusCheck = false;

    public ProfissionalController() {
    }
    
    public void setStatusCheck(Boolean statusCheck){
        this.statusCheck = statusCheck;
    }
    
    public Boolean getStatusCheck(){
        return statusCheck;
    }
    
    public boolean isHasStatusIndisponivel(){
        if(current.getStatus() != null){
            return (current.getStatus() == 2 || current.getStatus() == 3);
        }
        return false;
    }
    
    public Profissional getSelected() {
        if (current == null) {
            current = new Profissional();
            selectedItemIndex = -1;
        }
        return current;
    }

    public Profissional getFiltered() {
        if (filtered == null) {
            filtered = new Profissional();
            selectedItemIndex = -1;
        }
        return filtered;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getFilterShow() {
        return filterShow;
    }

    public void setFilterShow(String filterShow) {
        this.filterShow = filterShow;
    }

    public void updatePageSize() {
        recreatePagination();
        items = getPagination().createPageDataModel();
    }

    public List<Integer> getListPageSize() {
        ArrayList<Integer> lista = new ArrayList();
        lista.add(5);
        lista.add(10);
        lista.add(20);
        lista.add(50);
        lista.add(100);
        return lista;
    }

    private ProfissionalJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new ProfissionalJpaController(Persistence.createEntityManagerFactory("sisplatPU"));
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(pageSize) {

                @Override
                public int getItemsCount() {
                    return getJpaController().getProfissionalCount(filtered);
                }

                @Override
                public DataModel createPageDataModel() {
                    if (current == null) {
                        return new ListDataModel(getJpaController().findProfissionalEntities(getPageSize(), getPageFirstItem()));
                    }
                    return new ListDataModel(getJpaController().findProfissionalFilter(filtered, getPageSize(), getPageFirstItem()));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public void prepareView() {
        current = (Profissional) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
    }

    public void prepareCreate() {
        current = new Profissional();
        selectedItemIndex = -1;
        statusCheck = false;
    }

    public void create() {
        try {
            verifyStatusCheckUpdate();
            getJpaController().create(current);
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addSuccessMessage(FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("ProfissionalCreated"));
            clearFilter();
            recreateModel();
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addErrorMessage(e, FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("PersistenceErrorOccured"));
            recreateModel();
        }
    }

    public boolean getHasCreate() {
        return current == null;
    }

    public void prepareEdit() {
        current = (Profissional) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        if(current.getStatus() == 4){
            statusCheck = true;
        }else{
            statusCheck = false;
        }
    }

    public void update() {
        try {
            verifyStatusCheckUpdate();
            getJpaController().edit(current);
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addSuccessMessage(FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("ProfissionalUpdated"));
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addErrorMessage(e, FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    public void verifyStatusCheckUpdate(){
        if(statusCheck == true){
            current.setStatus(4);
        }else{
            if(current.getStatus() != null){
                if(current.getStatus() == 4 && statusCheck == false){
                    current.setStatus(1);
                }
            }else{
                current.setStatus(1);
            }
        }
    }

    public void filter() {
        items = getPagination().createPageDataModel();
        filterShow = "show";
    }

    public void clearFilter() {
        items = null;
        filtered = null;
        filterShow = "";
        getPagination().firstPage();
    }

    public void prepareDestroy() {
        current = (Profissional) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
    }

    public void destroy() {
        performDestroy();
        recreatePagination();
        recreateModel();
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getJpaController().destroy(current.getId());
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addSuccessMessage(FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("ProfissionalDeleted"));
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addErrorMessage(e, FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getProfissionalCount(filtered);
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findProfissionalEntities(1, selectedItemIndex).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
        current = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public void next() {
        getPagination().nextPage();
        items = null;
    }

    public void previous() {
        getPagination().previousPage();
        items = null;
    }

    public void first() {
        getPagination().firstPage();
        items = null;
    }

    public void last() {
        getPagination().lastPage();
        items = null;
    }

    public void priPage() {
        getPagination().primaryPage();
        items = null;
    }

    public void secPage() {
        getPagination().secondPage();
        items = null;
    }

    public void thiPage() {
        getPagination().thirdPage();
        items = null;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findProfissionalEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findProfissionalEntities(), true);
    }
    
    public List<SelectItem> getItemsStatus(){
        List<SelectItem> status = new ArrayList<SelectItem>();
        status.add(new SelectItem("", "---"));
        status.add(new SelectItem(1,"Disponível"));
        status.add(new SelectItem(2, "Programado"));
        status.add(new SelectItem(3, "Embarcado"));
        status.add(new SelectItem(4, "Indisponível"));
        return status;
    }

    @FacesConverter(forClass = Profissional.class)
    public static class ProfissionalControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProfissionalController controller = (ProfissionalController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "profissionalController");
            return controller.getJpaController().findProfissional(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Profissional) {
                Profissional o = (Profissional) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Profissional.class.getName());
            }
        }

    }

}
