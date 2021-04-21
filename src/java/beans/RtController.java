package beans;

import model.Rt;
import beans.util.JsfUtil;
import beans.util.PaginationHelper;
import facade.RtJpaController;

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

@ManagedBean(name = "rtController")
@SessionScoped
public class RtController implements Serializable {

    private Rt current;
    private DataModel items = null;
    private RtJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Integer pageSize = 10;
    private Rt filtered;
    private String filterShow;

    public RtController() {
    }

    public Rt getSelected() {
        if (current == null) {
            current = new Rt();
            selectedItemIndex = -1;
        }
        return current;
    }

    public Rt getFiltered() {
        if (filtered == null) {
            filtered = new Rt();
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

    private RtJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new RtJpaController(Persistence.createEntityManagerFactory("sisplatPU"));
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(pageSize) {

                @Override
                public int getItemsCount() {
                    return getJpaController().getRtCount(filtered);
                }

                @Override
                public DataModel createPageDataModel() {
                    if (current == null) {
                        return new ListDataModel(getJpaController().findRtEntities(getPageSize(), getPageFirstItem()));
                    }
                    return new ListDataModel(getJpaController().findRtFilter(filtered, getPageSize(), getPageFirstItem()));
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
        current = (Rt) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
    }

    public void prepareCreate() {
        current = new Rt();
        selectedItemIndex = -1;
    }

    public void create() {
        try {
            getJpaController().create(current);
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addSuccessMessage(FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("RtCreated"));
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
        current = (Rt) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
    }

    public void update() {
        try {
            getJpaController().edit(current);
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addSuccessMessage(FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("RtUpdated"));
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addErrorMessage(e, FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("PersistenceErrorOccured"));
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
        current = (Rt) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("RtDeleted"));
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addErrorMessage(e, FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getRtCount(filtered);
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findRtEntities(1, selectedItemIndex).get(0);
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
        return JsfUtil.getSelectItems(getJpaController().findRtEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findRtEntities(), true);
    }
    
    public List<SelectItem> getItemsRtTipo(){
        List<SelectItem> tipos = new ArrayList<SelectItem>();
        tipos.add(new SelectItem("", "---"));
        tipos.add(new SelectItem(1, "EMBARQUE"));
        tipos.add(new SelectItem(2, "DESEMBARQUE"));
        tipos.add(new SelectItem(3, "TRANSFERÊNCIA"));
        return tipos;
    }
    
    @FacesConverter(forClass = Rt.class)
    public static class RtControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RtController controller = (RtController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "rtController");
            return controller.getJpaController().findRt(getKey(value));
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
            if (object instanceof Rt) {
                Rt o = (Rt) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Rt.class.getName());
            }
        }

    }

}
