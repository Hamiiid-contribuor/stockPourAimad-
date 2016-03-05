package controler;

import bean.AvoirVenteDirectItem;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.AvoirVenteDirectItemFacade;

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


@Named("avoirVenteDirectItemController")
@SessionScoped
public class AvoirVenteDirectItemController implements Serializable {


    @EJB private service.AvoirVenteDirectItemFacade ejbFacade;
    private List<AvoirVenteDirectItem> items = null;
    private AvoirVenteDirectItem selected;

    public AvoirVenteDirectItemController() {
    }

    public AvoirVenteDirectItem getSelected() {
        return selected;
    }

    public void setSelected(AvoirVenteDirectItem selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AvoirVenteDirectItemFacade getFacade() {
        return ejbFacade;
    }

    public AvoirVenteDirectItem prepareCreate() {
        selected = new AvoirVenteDirectItem();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AvoirVenteDirectItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AvoirVenteDirectItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AvoirVenteDirectItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<AvoirVenteDirectItem> getItems() {
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

    public AvoirVenteDirectItem getAvoirVenteDirectItem(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<AvoirVenteDirectItem> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AvoirVenteDirectItem> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass=AvoirVenteDirectItem.class)
    public static class AvoirVenteDirectItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AvoirVenteDirectItemController controller = (AvoirVenteDirectItemController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "avoirVenteDirectItemController");
            return controller.getAvoirVenteDirectItem(getKey(value));
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
            if (object instanceof AvoirVenteDirectItem) {
                AvoirVenteDirectItem o = (AvoirVenteDirectItem) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AvoirVenteDirectItem.class.getName()});
                return null;
            }
        }

    }

}
