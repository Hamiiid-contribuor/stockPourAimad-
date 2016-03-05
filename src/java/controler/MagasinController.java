package controler;

import bean.Magasin;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.MagasinFacade;

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

@Named("magasinController")
@SessionScoped
public class MagasinController implements Serializable {

    @EJB
    private service.MagasinFacade ejbFacade;
    private List<Magasin> items = null;
    private Magasin selected;

    public List<Magasin> findByAbonne() {
        return SessionUtil.getConnectedUser().getAbonne().getMagasins();
    }

    public MagasinController() {
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MagasinFacade getFacade() {
        return ejbFacade;
    }

    public Magasin prepareCreate() {
        selected = new Magasin();
        initializeEmbeddableKey();
        return selected;
    }

    public Magasin getSelected() {
        if (selected == null) {
            selected = new Magasin();
        }
        return selected;
    }

    public void setSelected(Magasin selected) {
        this.selected = selected;
    }

    public void create() {
        if (selected.getAbonne().getId() == null) {
            selected.setAbonne(SessionUtil.getConnectedUser().getAbonne());
        }
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MagasinCreated"));
        if (!JsfUtil.isValidationFailed()) {
            SessionUtil.getConnectedUser().getAbonne().getMagasins().add(selected);
            items = SessionUtil.getConnectedUser().getAbonne().getMagasins();
        }
    }

    public void update() {
        selected.getAbonne().getMagasins().set(selected.getAbonne().getMagasins().indexOf(selected), selected);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MagasinUpdated"));
    }

    public void destroy() {
        selected.getAbonne().getMagasins().remove(selected.getAbonne().getMagasins().indexOf(selected));
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MagasinDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
        }
    }

    public List<Magasin> getItems() {
        if (items == null) {
            items = (SessionUtil.getConnectedUser().getAbonne().getMagasins());
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    getFacade().create(selected);
                } else if (persistAction == PersistAction.UPDATE) {
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

    public Magasin getMagasin(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Magasin> getItemsAvailableSelectMany() {
        return (SessionUtil.getConnectedUser().getAbonne().getMagasins());
    }

    public List<Magasin> getItemsAvailableSelectOne() {
        return (SessionUtil.getConnectedUser().getAbonne().getMagasins());
    }

    @FacesConverter(forClass = Magasin.class)
    public static class MagasinControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MagasinController controller = (MagasinController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "magasinController");
            return controller.getMagasin(getKey(value));
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
            if (object instanceof Magasin) {
                Magasin o = (Magasin) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Magasin.class.getName()});
                return null;
            }
        }

    }

}
