package controler;

import bean.AvoirLivraisonItem;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.AvoirLivraisonItemFacade;

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


@Named("avoirLivraisonItemController")
@SessionScoped
public class AvoirLivraisonItemController implements Serializable {


    @EJB private service.AvoirLivraisonItemFacade ejbFacade;
    private List<AvoirLivraisonItem> items = null;
    private AvoirLivraisonItem selected;

    public AvoirLivraisonItemController() {
    }

    public AvoirLivraisonItem getSelected() {
        return selected;
    }

    public void setSelected(AvoirLivraisonItem selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AvoirLivraisonItemFacade getFacade() {
        return ejbFacade;
    }

    public AvoirLivraisonItem prepareCreate() {
        selected = new AvoirLivraisonItem();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AvoirLivraisonItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AvoirLivraisonItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AvoirLivraisonItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<AvoirLivraisonItem> getItems() {
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

    public AvoirLivraisonItem getAvoirLivraisonItem(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<AvoirLivraisonItem> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AvoirLivraisonItem> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass=AvoirLivraisonItem.class)
    public static class AvoirLivraisonItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AvoirLivraisonItemController controller = (AvoirLivraisonItemController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "avoirLivraisonItemController");
            return controller.getAvoirLivraisonItem(getKey(value));
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
            if (object instanceof AvoirLivraisonItem) {
                AvoirLivraisonItem o = (AvoirLivraisonItem) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AvoirLivraisonItem.class.getName()});
                return null;
            }
        }

    }

}
