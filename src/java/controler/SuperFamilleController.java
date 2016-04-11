package controler;

import bean.Abonne;
import bean.SuperFamille;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.SuperFamilleFacade;

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

@Named("superFamilleController")
@SessionScoped
public class SuperFamilleController implements Serializable {

    @EJB
    private service.SuperFamilleFacade ejbFacade;
    private List<SuperFamille> items = null;
    private SuperFamille selected;

    public SuperFamilleController() {
    }

    public SuperFamille getSelected() {
        return selected;
    }

    public void setSelected(SuperFamille selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SuperFamilleFacade getFacade() {
        return ejbFacade;
    }

    public SuperFamille prepareCreate() {
        selected = new SuperFamille();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        if (selected.getAbonne().getId() == null) {
            selected.setAbonne(SessionUtil.getConnectedUser().getAbonne());
        }
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SuperFamilleCreated"));
        if (!JsfUtil.isValidationFailed()) {
            SessionUtil.getConnectedUser().getAbonne().getSuperFamilles().add(selected);
            items = SessionUtil.getConnectedUser().getAbonne().getSuperFamilles();// Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SuperFamilleUpdated"));
    }

    public void destroy() {
        selected.getAbonne().getSuperFamilles().remove(selected.getAbonne().getSuperFamilles().indexOf(selected));
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SuperFamilleDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection

        }
    }

    public List<SuperFamille> getItems() {
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

    public SuperFamille getSuperFamille(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<SuperFamille> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<SuperFamille> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = SuperFamille.class)
    public static class SuperFamilleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SuperFamilleController controller = (SuperFamilleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "superFamilleController");
            return controller.getSuperFamille(getKey(value));
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
            if (object instanceof SuperFamille) {
                SuperFamille o = (SuperFamille) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), SuperFamille.class.getName()});
                return null;
            }
        }

    }

    //*********************************************hamid*******************************
    public List<SuperFamille> findSuperFamilleByAbonne(Abonne abonne) {
        System.out.println(SessionUtil.getConnectedUser().getAbonne().getSuperFamilles());
        return SessionUtil.getConnectedUser().getAbonne().getSuperFamilles();
    }

}
