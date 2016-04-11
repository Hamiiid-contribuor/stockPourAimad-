package controler;

import bean.Abonne;
import bean.Famille;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.FamilleFacade;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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

@Named("familleController")
@SessionScoped
public class FamilleController implements Serializable {

    @EJB
    private service.FamilleFacade ejbFacade;
    private List<Famille> items = null;
    private Famille selected;

    public List<Famille> findFamilleByAbonne(Abonne abonne) {
        System.out.println(SessionUtil.getConnectedUser().getAbonne().getFamilles());
        return SessionUtil.getConnectedUser().getAbonne().getFamilles();
        
    }

    public FamilleController() {
    }

    public Famille getSelected() {
        if (selected == null) {
            selected = new Famille();
        }
        return selected;
    }

    public void setSelected(Famille selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FamilleFacade getFacade() {
        return ejbFacade;
    }

    public Famille prepareCreate() {
        selected = new Famille();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        if (selected.getAbonne().getId() == null) {
            selected.setAbonne(SessionUtil.getConnectedUser().getAbonne());
        }
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FamilleCreated"));
        if (!JsfUtil.isValidationFailed()) {
            SessionUtil.getConnectedUser().getAbonne().getFamilles().add(selected);
            items = SessionUtil.getConnectedUser().getAbonne().getFamilles();// Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        selected.getAbonne().getFamilles().set(selected.getAbonne().getFamilles().indexOf(selected), selected);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FamilleUpdated"));
    }

    public void destroy() {
        selected.getAbonne().getFamilles().remove(selected.getAbonne().getFamilles().indexOf(selected));
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FamilleDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection

        }
    }

    public List<Famille> getItems() {
        if (items == null) {
            items = (SessionUtil.getConnectedUser().getAbonne().getFamilles());
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

    public Famille getFamille(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Famille> getItemsAvailableSelectMany() {
        return (SessionUtil.getConnectedUser().getAbonne().getFamilles());
    }

    public List<Famille> getItemsAvailableSelectOne() {
        return (SessionUtil.getConnectedUser().getAbonne().getFamilles());
    }

    @FacesConverter(forClass = Famille.class)
    public static class FamilleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FamilleController controller = (FamilleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "familleController");
            return controller.getFamille(getKey(value));
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
            if (object instanceof Famille) {
                Famille o = (Famille) object;
                return getStringKey(o.getId());
            } else {
                System.out.println("getString fihaaa mochkilll ");
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Famille.class.getName()});
                return null;
            }
        }

    }

}
