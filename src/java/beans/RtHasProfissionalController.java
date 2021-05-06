package beans;

import model.RtHasProfissional;
import beans.util.JsfUtil;
import beans.util.PaginationHelper;
import facade.RtHasProfissionalJpaController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.Persistence;

@ManagedBean(name = "rtHasProfissionalController")
@ViewScoped
public class RtHasProfissionalController implements Serializable {

    private RtHasProfissional current;
    private DataModel items = null;
    private RtHasProfissionalJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Integer pageSize = 10;
    private RtHasProfissional filtered;
    private String filterShow;

    public RtHasProfissionalController() {
    }

    public RtHasProfissional getSelected() {
        if (current == null) {
            current = new RtHasProfissional();
            current.setRtHasProfissionalPK(new model.RtHasProfissionalPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    public RtHasProfissional getFiltered() {
        if (filtered == null) {
            filtered = new RtHasProfissional();
            filtered.setRtHasProfissionalPK(new model.RtHasProfissionalPK());
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

    private RtHasProfissionalJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new RtHasProfissionalJpaController(Persistence.createEntityManagerFactory("sisplatPU"));
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(pageSize) {

                @Override
                public int getItemsCount() {
                    return getJpaController().getRtHasProfissionalCount(filtered);
                }

                @Override
                public DataModel createPageDataModel() {
                    if (current == null) {
                        return new ListDataModel(getJpaController().findRtHasProfissionalEntities(getPageSize(), getPageFirstItem()));
                    }
                    return new ListDataModel(getJpaController().findRtHasProfissionalFilter(filtered, getPageSize(), getPageFirstItem()));
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
        current = (RtHasProfissional) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
    }

    public void prepareCreate() {
        current = new RtHasProfissional();
        current.setRtHasProfissionalPK(new model.RtHasProfissionalPK());
        selectedItemIndex = -1;
    }

    public void create() {
        try {
            current.getRtHasProfissionalPK().setProfissionalId(current.getProfissional().getId());
            current.getRtHasProfissionalPK().setRtId(current.getRt().getId());
            getJpaController().create(current);
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addSuccessMessage(FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("RtHasProfissionalCreated"));
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
        current = (RtHasProfissional) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
    }

    public void update() {
        try {
            current.getRtHasProfissionalPK().setProfissionalId(current.getProfissional().getId());
            current.getRtHasProfissionalPK().setRtId(current.getRt().getId());
            getJpaController().edit(current);
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addSuccessMessage(FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("RtHasProfissionalUpdated"));
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
        current = (RtHasProfissional) getItems().getRowData();
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
            getJpaController().destroy(current.getRtHasProfissionalPK());
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addSuccessMessage(FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("RtHasProfissionalDeleted"));
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            JsfUtil.addErrorMessage(e, FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getRtHasProfissionalCount(filtered);
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findRtHasProfissionalEntities(1, selectedItemIndex).get(0);
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
        return JsfUtil.getSelectItems(getJpaController().findRtHasProfissionalEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findRtHasProfissionalEntities(), true);
    }

    @FacesConverter(forClass = RtHasProfissional.class)
    public static class RtHasProfissionalControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RtHasProfissionalController controller = (RtHasProfissionalController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "rtHasProfissionalController");
            return controller.getJpaController().findRtHasProfissional(getKey(value));
        }

        model.RtHasProfissionalPK getKey(String value) {
            model.RtHasProfissionalPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new model.RtHasProfissionalPK();
            key.setId(Integer.parseInt(values[0]));
            key.setRtId(Integer.parseInt(values[1]));
            key.setProfissionalId(Integer.parseInt(values[2]));
            return key;
        }

        String getStringKey(model.RtHasProfissionalPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getId());
            sb.append(SEPARATOR);
            sb.append(value.getRtId());
            sb.append(SEPARATOR);
            sb.append(value.getProfissionalId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof RtHasProfissional) {
                RtHasProfissional o = (RtHasProfissional) object;
                return getStringKey(o.getRtHasProfissionalPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + RtHasProfissional.class.getName());
            }
        }

    }

}
