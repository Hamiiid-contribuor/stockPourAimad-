package controler;

import bean.AvoirAchatDirect;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.AvoirAchatDirectFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@Named("avoirAchatDirectController")
@SessionScoped
public class AvoirAchatDirectController implements Serializable {


    @EJB private service.AvoirAchatDirectFacade ejbFacade;
    private List<AvoirAchatDirect> items = null;
    private AvoirAchatDirect selected;

    public AvoirAchatDirectController() {
    }

    public AvoirAchatDirect getSelected() {
        return selected;
    }

    public void setSelected(AvoirAchatDirect selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AvoirAchatDirectFacade getFacade() {
        return ejbFacade;
    }

    public AvoirAchatDirect prepareCreate() {
        selected = new AvoirAchatDirect();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AvoirAchatDirectCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AvoirAchatDirectUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AvoirAchatDirectDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<AvoirAchatDirect> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public AvoirAchatDirect getAvoirAchatDirect(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<AvoirAchatDirect> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AvoirAchatDirect> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass=AvoirAchatDirect.class)
    public static class AvoirAchatDirectControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AvoirAchatDirectController controller = (AvoirAchatDirectController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "avoirAchatDirectController");
            return controller.getAvoirAchatDirect(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AvoirAchatDirect) {
                AvoirAchatDirect o = (AvoirAchatDirect) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AvoirAchatDirect.class.getName()});
                return null;
            }
        }

    }

}
