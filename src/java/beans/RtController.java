package beans;

import model.Rt;
import beans.util.JsfUtil;
import beans.util.PaginationHelper;
import facade.ProfissionalJpaController;
import facade.RtHasProfissionalJpaController;
import facade.RtJpaController;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import model.Profissional;
import model.RtHasProfissional;
import model.RtHasProfissionalPK;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory;

@ManagedBean(name = "rtController")
@ViewScoped
public class RtController implements Serializable {

    private Rt current;
    private DataModel items = null;
    private RtJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Integer pageSize = 10;
    private Rt filtered;
    private String filterShow;
    private List<Profissional> leftSelected;
    private List<Profissional> leftAvailable;
    private List<Profissional> rightSelected;
    private List<Profissional> rightAvailable;
    ProfissionalJpaController profissionalJpaController = new ProfissionalJpaController(Persistence.createEntityManagerFactory("sisplatPU"));
    
    public RtController() {
    }  
    
    public void leftToRight(){
        leftAvailable.removeAll(leftSelected);
        rightAvailable.addAll(leftSelected);
        leftSelected = null;
    }
    
    public void rightToLeft(){
        rightAvailable.removeAll(rightSelected);
        leftAvailable.addAll(rightSelected);
        rightSelected = null;
    }
    
    public List<Profissional> getLeftSelected(){
        return leftSelected;
    }
    
    public void setLeftSelected(List<Profissional> leftSelected){
        this.leftSelected = leftSelected;
    }
    
    public List<Profissional> getRightSelected(){
        return rightSelected;
    }
    
    public void setRightSelected(List<Profissional> rightSelected){
        this.rightSelected = rightSelected;
    }
    
    public List<Profissional> getLeftAvailable(){
        return leftAvailable;
    }
    
    public List<Profissional> getRightAvailable(){
        return rightAvailable;
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
        populateProfissional();
    }

    public void create() {
        try {
            Collection<RtHasProfissional> rtHasProfissionalColNew = new ArrayList<RtHasProfissional>();
//            RtHasProfissional rtHasProfissional;
            getJpaController().create(current);
            RtHasProfissionalJpaController rtHasProfissionalJpaController = new RtHasProfissionalJpaController(Persistence.createEntityManagerFactory("sisplatPU"));
            for(Profissional itemProfissional : rightAvailable){
                RtHasProfissional rtHasProfissional = new RtHasProfissional();
                rtHasProfissional.setRtHasProfissionalPK(new model.RtHasProfissionalPK());
                rtHasProfissional.setRt(current);
                rtHasProfissional.setProfissional(itemProfissional);
                rtHasProfissional.setStatus(current.getStatus());
                rtHasProfissional.getRtHasProfissionalPK().setProfissionalId(itemProfissional.getId());
                rtHasProfissional.getRtHasProfissionalPK().setRtId(rtHasProfissional.getRt().getId());
//                rtHasProfissionalColNew.add(rtHasProfissional);
                rtHasProfissionalJpaController.create(rtHasProfissional);
            }
            
//            current.setRtHasProfissionalCollection(rtHasProfissionalColNew);
//            getJpaController().create(current);
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
        populateProfissional();     
    }

    public void update() {
        try {
//            current.setRtHasProfissionalCollection(null);
//            Collection<RtHasProfissional> rtHasProfissionalColNew = new ArrayList<RtHasProfissional>();
            getJpaController().edit(current);
            RtHasProfissionalJpaController rtHasProfissionalJpaController = new RtHasProfissionalJpaController(Persistence.createEntityManagerFactory("sisplatPU"));
            
            for(RtHasProfissional rtHasProfissionalDelete : current.getRtHasProfissionalCollection()){
                rtHasProfissionalJpaController.destroy(rtHasProfissionalDelete.getRtHasProfissionalPK());
            }
            for(Profissional itemProfissional : rightAvailable){
                RtHasProfissional rtHasProfissional = new RtHasProfissional();
                rtHasProfissional.setRtHasProfissionalPK(new model.RtHasProfissionalPK());
                
//                rtHasProfissional.setRtHasProfissionalPK(new RtHasProfissionalPK());
                rtHasProfissional.setRt(current);
                rtHasProfissional.setProfissional(itemProfissional);
                rtHasProfissional.setStatus(current.getStatus());
                rtHasProfissional.getRtHasProfissionalPK().setProfissionalId(itemProfissional.getId());
                rtHasProfissional.getRtHasProfissionalPK().setRtId(rtHasProfissional.getRt().getId());
//                rtHasProfissionalColNew.add(rtHasProfissional);
//                current.getRtHasProfissionalCollection().add(rtHasProfissional);
//                rtHasProfissionalJpaController.destroy(rtHasProfissional.getRtHasProfissionalPK());
                rtHasProfissionalJpaController.create(rtHasProfissional);
            }
            
//            current.setRtHasProfissionalCollection(rtHasProfissionalColNew);
//            current.getRtHasProfissionalCollection().addAll(rtHasProfissionalColNew);
            
//            getJpaController().edit(current);
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
        FacesContext context = FacesContext.getCurrentInstance();
        List<SelectItem> tipos = new ArrayList<SelectItem>();
        tipos.add(new SelectItem("", "---"));
        tipos.add(new SelectItem(1,FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("RtTipoEmbarque")));
        tipos.add(new SelectItem(2, FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("RtTipoDesembarque")));
        tipos.add(new SelectItem(3, FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("RtTipoTransferencia")));
        return tipos;
    }
    
    public List<SelectItem> getItemsRtStatus(){
        FacesContext context = FacesContext.getCurrentInstance();
        List<SelectItem> status = new ArrayList<SelectItem>();
        status.add(new SelectItem("", "---"));
        status.add(new SelectItem(1,FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("RtStatusProgramado")));
        status.add(new SelectItem(2, FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("RtStatusLiberado")));
        status.add(new SelectItem(3, FacesContext.getCurrentInstance().getApplication().getResourceBundle(context, "bundle").getString("RtStatusCancelado")));
        return status;
    }
    
    private void populateProfissional(){
        leftAvailable = new ArrayList<>();
        rightAvailable = new ArrayList<>();
        
        for(Profissional itemProfissional : profissionalJpaController.findProfissionalDisponivel(1)){
            leftAvailable.add(itemProfissional);
        }
        
        if(current.getRtHasProfissionalCollection() != null){
            for(RtHasProfissional itemRtHasProfissional : current.getRtHasProfissionalCollection()){
                rightAvailable.add(itemRtHasProfissional.getProfissional());
            }
            leftAvailable.removeAll(rightAvailable);
        }
    }
    
    public String printReport2() throws JRException, IOException{
        current = (Rt) getItems().getRowData();
        
        String path = "/WEB-INF/reports/rt.jrxml";
        InputStream jasperTemplate = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(path);
//        FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/rt.jrxml");
        
//        JasperReport jasperReport = JasperCompileManager.compileReport(jasperTemplate);
//        File relatorioJasper = new File(jasperReport.toString());
        File relatorioJasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/rt.jasper"));
        Map parametros = new HashMap();
        parametros.put("param_rt_id", current.getId());
        
        byte[] bytes = null;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sisplatPU");
        EntityManager em =emf.createEntityManager();
        
        try{
            parametros.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, em);
            bytes = JasperRunManager.runReportToPdf(relatorioJasper.getPath(), parametros);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
//            if(em.isOpen()) em.close();
//            if(emf.isOpen()) emf.close();
        }
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        
//        if(bytes != null && bytes.length > 0){
            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes,0,bytes.length);
            
            outputStream.flush();
            outputStream.close();
//        }
        
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros);
//        response.setHeader("Content-Disposition", "attachment; filename=\"relatorio.pdf\"");
//        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        
        
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
        
        return null;
    }
    
    public String printReport() throws JRException, IOException{
        current = (Rt) getItems().getRowData();
        
        String path = "/WEB-INF/reports/rt.jrxml";
        String pathSubReportHead = "/WEB-INF/reports/rtHeader.jrxml";
        String pathSubReportTitle = "/WEB-INF/reports/rtTitle.jrxml";
        InputStream jasperTemplate = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(path);
        InputStream jasperTemplateSrHead = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(pathSubReportHead);
        InputStream jasperTemplateSrTitle = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(pathSubReportTitle);
        
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperTemplate);
        JasperReport jasperReportSrHead = JasperCompileManager.compileReport(jasperTemplateSrHead);
        JasperReport jasperReportSrTitle = JasperCompileManager.compileReport(jasperTemplateSrTitle);
        
        Map parametros = new HashMap();
        parametros.put("param_rt_id", current.getId());
        parametros.put("SubReportParamHead", jasperReportSrHead);
        parametros.put("SubReportParamTitle", jasperReportSrTitle);
        
        Connection conn = null;
        
        try{
            conn = DriverManager.getConnection("jdbc:mysql://mysql.sisplat.com:3306/sisplat?useSSL=false&useTimezone=true&serverTimezone=UTC", "sisplat", "bode123");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conn);
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        
        ServletOutputStream outputStream = response.getOutputStream();
        
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"RT_"+current.getId()+".pdf\"");
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        
        outputStream.flush();
        outputStream.close();
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
        
        return null;
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
    
    @FacesConverter(value = "profissionalConverter", forClass = Profissional.class)
    public static class ProfissionalControllerConverter implements Converter {
        ProfissionalJpaController profissionalJpaController = new ProfissionalJpaController(Persistence.createEntityManagerFactory("sisplatPU"));
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProfissionalController controller = (ProfissionalController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "profissionalController");
            return profissionalJpaController.findProfissional(getKey(value));
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
    
    @FacesConverter(value="rtHasProfissional", forClass = RtHasProfissional.class)
    public static class RtHasProfissionalControllerConverter implements Converter {
        RtHasProfissionalJpaController rtHasProfissionalJpaController = new RtHasProfissionalJpaController(Persistence.createEntityManagerFactory("sisplatPU"));
        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RtHasProfissionalController controller = (RtHasProfissionalController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "rtHasProfissionalController");
            return rtHasProfissionalJpaController.findRtHasProfissional(getKey(value));
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
