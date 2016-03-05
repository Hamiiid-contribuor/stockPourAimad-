package controler;

import bean.Abonne;
import bean.Fournisseur;
import bean.User;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.FournisseurFacade;

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

@Named("fournisseurController")
@SessionScoped
public class FournisseurController implements Serializable {

    @EJB
    private service.FournisseurFacade ejbFacade;
    private List<Fournisseur> items = null;
    private List<Fournisseur> fournisseurs = null;
    private Fournisseur selected;

    public List<Fournisseur> findFournisseurByAbonne(Abonne abonne, int deleted) {
        if (fournisseurs == null) {
            fournisseurs = SessionUtil.getConnectedUser().getAbonne().getFournisseurs();
        }
        return fournisseurs;
    }

    public List<Fournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public void setFournisseurs(List<Fournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }

    public FournisseurController() {
    }

    public Fournisseur getSelected() {
        if (selected == null) {
            selected = new Fournisseur();
        }
        return selected;
    }

    public void setSelected(Fournisseur selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FournisseurFacade getFacade() {
        return ejbFacade;
    }

    public Fournisseur prepareCreate() {
        selected = new Fournisseur();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        if (selected.getAbonne().getId() == null) {
            selected.setAbonne(SessionUtil.getConnectedUser().getAbonne());
        }
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FournisseurCreated"));
        if (!JsfUtil.isValidationFailed()) {
            SessionUtil.getConnectedUser().getAbonne().getFournisseurs().add(selected);
            items = SessionUtil.getConnectedUser().getAbonne().getFournisseurs();// Invalidate list of items to trigger re-query.

        }
    }

    public void update() {
        selected.getAbonne().getFournisseurs().set(selected.getAbonne().getFournisseurs().indexOf(selected), selected);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FournisseurUpdated"));
    }

    public void destroy() {
        selected.getAbonne().getFamilles().remove(selected.getAbonne().getFamilles().indexOf(selected));
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FournisseurDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Fournisseur> getItems() {
        if (items == null) {
            items = SessionUtil.getConnectedUser().getAbonne().getFournisseurs();
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

    public Fournisseur getFournisseur(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Fournisseur> getItemsAvailableSelectMany() {
        return (SessionUtil.getConnectedUser().getAbonne().getFournisseurs());
    }

    public List<Fournisseur> getItemsAvailableSelectOne() {
        return (SessionUtil.getConnectedUser().getAbonne().getFournisseurs());
    }

    @FacesConverter(forClass = Fournisseur.class)
    public static class FournisseurControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FournisseurController controller = (FournisseurController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "fournisseurController");
            return controller.getFournisseur(getKey(value));
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
            if (object instanceof Fournisseur) {
                Fournisseur o = (Fournisseur) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Fournisseur.class.getName()});
                return null;
            }
        }

    }

}
