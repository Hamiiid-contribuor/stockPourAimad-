package controler;

import bean.Abonne;
import bean.Commande;
import bean.PaiementCommande;
import bean.User;
import controler.util.FileUtil;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import java.io.IOException;
import service.PaiementCommandeFacade;

import java.io.Serializable;
import java.math.BigDecimal;
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
import org.primefaces.model.UploadedFile;
import service.AbonneFacade;
import service.CommandeFacade;

@Named("paiementCommandeController")
@SessionScoped
public class PaiementCommandeController implements Serializable {

    @EJB
    private service.PaiementCommandeFacade ejbFacade;
    private List<PaiementCommande> items = null;
    private PaiementCommande selected;
    private UploadedFile file;


    public void preparePaiement(Commande commande) {
        selected = new PaiementCommande();
        selected.setCommande(commande);
    }

    public void versDetailPaiement(PaiementCommande paiementCommande) {
        selected = paiementCommande;
    }

    public void save() {
        int res = getFacade().save(selected);
        if (res > 0) {
            JsfUtil.addSuccessMessage("paiement avec succes");
            if (selected.getCommande().getPaiementCommandes() != null) {
                selected.getCommande().getPaiementCommandes().add(selected);
            }
        } else {
            BigDecimal montantRestant = selected.getCommande().getMontantTotal().subtract(selected.getCommande().getPaiement().add(selected.getCommande().getPaiementEffetEnCour()));
            JsfUtil.addErrorMessage("Error de paiement: Dépassement du Montant restant est " + montantRestant);
        }

    }

    public void remove(Commande commande,PaiementCommande paiementCommande){
        paiementCommande.setCommande(commande);
        getFacade().remove(paiementCommande);
    }

    public void checkBox(PaiementCommande paiementCommande) {
        selected = paiementCommande;
    }

    public void cancelCheckBox() {
        if (selected.getEncaisser()) {
            selected.setEncaisser(false);
        } else {
            selected.setEncaisser(true);
        }
    }

    public void changerEtatEncaissement(Commande commande) {
        selected.setCommande(commande);
        getFacade().changerEtatEncaissement(selected);
        if (!JsfUtil.isValidationFailed()) {
            if (selected.getEncaisser()) {
                JsfUtil.addSuccessMessage("Encaissement avec succes");
            } else {
                JsfUtil.addSuccessMessage("Decaissement avec succes");
            }
        } else {
            JsfUtil.addErrorMessage("Encaissement/Decaissement ne peut pas effectuer !!");
        }

    }

    public PaiementCommandeController() {
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public PaiementCommande getSelected() {
        return selected;
    }

    public void setSelected(PaiementCommande selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PaiementCommandeFacade getFacade() {
        return ejbFacade;
    }

    public PaiementCommande prepareCreate() {
        selected = new PaiementCommande();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PaiementCommandeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PaiementCommandeUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PaiementCommandeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PaiementCommande> getItems() {
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

    public PaiementCommande getPaiementCommande(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<PaiementCommande> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PaiementCommande> getItemsAvailableSelectOne() {
        return getFacade().findAll();

    }

    @FacesConverter(forClass = PaiementCommande.class)
    public static class PaiementCommandeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PaiementCommandeController controller = (PaiementCommandeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "paiementCommandeController");
            return controller.getPaiementCommande(getKey(value));
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
            if (object instanceof PaiementCommande) {
                PaiementCommande o = (PaiementCommande) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PaiementCommande.class.getName()});
                return null;
            }
        }

    }

}
