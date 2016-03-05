package controler;

import bean.AvoirReception;
import bean.AvoirReceptionItem;
import bean.Commande;
import bean.Reception;
import bean.ReceptionItem;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.AvoirReceptionFacade;

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
import service.AvoirReceptionItemFacade;
import service.ReceptionItemFacade;

@Named("avoirReceptionController")
@SessionScoped
public class AvoirReceptionController implements Serializable {

    @EJB
    private service.AvoirReceptionFacade ejbFacade;
    private List<AvoirReception> items = null;
    private AvoirReception selected;
    private Reception reception;
    private Commande commande;
    private ReceptionItem receptionItem;
    private AvoirReceptionItem avoirReceptionItem;
    @EJB
    private ReceptionItemFacade receptionItemFacade;
    @EJB
    private AvoirReceptionItemFacade avoirReceptionItemFacade;

    public void verAjouterReception(ReceptionItem receptionItem) {
        this.receptionItem = receptionItem;
        this.receptionItem.setReception(reception);
        getAvoirReceptionItem().setProduit(this.receptionItem.getProduit());
        getAvoirReceptionItem().setQte(receptionItem.getQte().subtract(receptionItem.getQteAvoir()));
    }

    public String forwardToAvoirReceptionCreate(Commande commande, Reception reception) {
        this.commande = commande;
        this.reception = reception;
        this.reception.setCommande(commande);
        this.reception.getCommande().setAbonne(SessionUtil.getConnectedUser().getAbonne());
        this.reception.setReceptionItems(receptionItemFacade.findReceptionItemsNonAvoirByReception(reception));
        return "/avoirReception/Create";
    }

    public AvoirReception prepareCreate() {
        selected = new AvoirReception();
        initializeEmbeddableKey();
        return selected;
    }

    public void addAvoirReceptionItemToAvoirReception() {
        int res = avoirReceptionItemFacade.addAvoirReceptionItemToAvoirReception(avoirReceptionItem, selected, receptionItem);
        if (res == -1) {
            JsfUtil.addErrorMessage("Merçi de verifier la  qte spécifier");
        } else if (res == -2) {
            JsfUtil.addErrorMessage("la qte spécifier est superieur au qte resté merçi de la verifier");
        } else if (res == -3) {
            JsfUtil.addErrorMessage("Produit existe deja!!");
        }
    }

    public void save() {

    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AvoirReceptionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AvoirReceptionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<AvoirReception> getItems() {
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

    public AvoirReception getAvoirReception(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<AvoirReception> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AvoirReception> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = AvoirReception.class)
    public static class AvoirReceptionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AvoirReceptionController controller = (AvoirReceptionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "avoirReceptionController");
            return controller.getAvoirReception(getKey(value));
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
            if (object instanceof AvoirReception) {
                AvoirReception o = (AvoirReception) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AvoirReception.class.getName()});
                return null;
            }
        }

    }

    public Reception getReception() {
        return reception;
    }

    public void setReception(Reception reception) {
        this.reception = reception;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public ReceptionItem getReceptionItem() {
        return receptionItem;
    }

    public void setReceptionItem(ReceptionItem receptionItem) {
        this.receptionItem = receptionItem;
    }

    public AvoirReceptionItem getAvoirReceptionItem() {
        if (avoirReceptionItem == null) {
            avoirReceptionItem = new AvoirReceptionItem();
        }
        return avoirReceptionItem;
    }

    public void setAvoirReceptionItem(AvoirReceptionItem avoirReceptionItem) {
        this.avoirReceptionItem = avoirReceptionItem;
    }

    public AvoirReceptionController() {
    }

    public AvoirReception getSelected() {
        if (selected == null) {
            selected = new AvoirReception();
        }
        return selected;
    }

    public void setSelected(AvoirReception selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AvoirReceptionFacade getFacade() {
        return ejbFacade;
    }

}
