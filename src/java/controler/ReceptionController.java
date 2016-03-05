package controler;

import bean.Commande;
import bean.CommandeItem;
import bean.Reception;
import bean.ReceptionItem;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.ReceptionFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import service.CommandeItemFacade;
import service.ReceptionItemFacade;

@ManagedBean(name = "receptionController")
@SessionScoped
public class ReceptionController implements Serializable {

    @EJB
    private service.ReceptionFacade ejbFacade;
    @EJB
    private CommandeItemFacade commandeItemFacade;
    private List<Reception> items = null;
    private Reception selected;
    private ReceptionItem receptionItem;
    private CommandeItem commandeItem;
    private Commande commande;
    private int validerBoolean = 0; // wach tbane hadik li lfo9 ola la
    private @EJB
    ReceptionItemFacade receptionItemFacade;

    public String forwardToReceptionCreate(Commande commande) {
        this.commande = commande;
        this.commande.setCommandeItems(commandeItemFacade.findCommadeItemsEnCourByIdCmd(commande));
        this.commande.setAbonne(SessionUtil.getConnectedUser().getAbonne());
        getSelected().setCommande(commande);
        return "/reception/Create";
    }

    public void verAjouterReception(CommandeItem commandeItem) {
        this.commandeItem = commandeItem;
        receptionItem.setProduit(commandeItem.getProduit());
        receptionItem.setQte(commandeItem.getQte().subtract(commandeItem.getQteRecu()));
        receptionItem.getReception().setCommande(commandeItem.getCommande());
    }

    public void addToReception() {
        int res = receptionItemFacade.addReceptionItemToReception(receptionItem, selected, commandeItem);
        System.out.println("haa size d receptionItems ==> "+selected.getReceptionItems().size());
        if (res == -1) {
            JsfUtil.addErrorMessage("Merçi de verifier la  qte spécifier");
        } else if (res == -2) {
            JsfUtil.addErrorMessage("la qte spécifier est superieur au qte resté merçi de la verifier");
        }
    }

    public void save() {
        if (selected.getReceptionItems().size() > 0) {
            selected.setCommande(commande);
            getFacade().save(commande, selected, commande.getAbonne());
            selected = new Reception();
            validerBoolean = 0;
            JsfUtil.addSuccessMessage("Reception Ajouter avec succes!!");
            return;
        }
        JsfUtil.addErrorMessage("Merci de remplir les lignes de la Reception !!");
    }

    public void valider() {
        if (!selected.getReceptionItems().isEmpty()) {
            validerBoolean = 1;
        }
    }

    public void deleteReceptionItem(ReceptionItem item) {
        selected.getReceptionItems().remove(item);
        JsfUtil.addSuccessMessage("Ligne de Reception Supprimer avec succes!!");
    }

    public ReceptionItem getReceptionItem() {
        if (receptionItem == null) {
            receptionItem = new ReceptionItem();
        }
        return receptionItem;
    }

    public void setReceptionItem(ReceptionItem receptionItem) {
        this.receptionItem = receptionItem;
    }

    public ReceptionController() {
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Reception getSelected() {
        if (selected == null) {
            selected = new Reception();
        }
        return selected;
    }

    public void setSelected(Reception selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ReceptionFacade getFacade() {
        return ejbFacade;
    }

    public Reception prepareCreate() {
        selected = new Reception();
        initializeEmbeddableKey();
        return selected;
    }

    public Reception prepareCreate(Reception reception) {
        selected = reception;
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ReceptionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ReceptionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ReceptionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Reception> getItems() {
        if (items == null) {
            items = new ArrayList<>();
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

    public List<Reception> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Reception> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Reception.class)
    public static class ReceptionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ReceptionController controller = (ReceptionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "receptionController");
            return controller.getFacade().find(getKey(value));
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
            if (object instanceof Reception) {
                Reception o = (Reception) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Reception.class.getName()});
                return null;
            }
        }

    }

    public int getValiderBoolean() {
        return validerBoolean;
    }

    public void setValiderBoolean(int validerBoolean) {
        this.validerBoolean = validerBoolean;
    }

}
