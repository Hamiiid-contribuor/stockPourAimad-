package controler;

import bean.Commande;
import bean.DevisCommande;
import bean.DevisDemmande;
import bean.DevisDemmandeItem;
import bean.Produit;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.Message;
import controler.util.MessageManager;
import controler.util.SessionUtil;
import service.DevisDemmandeFacade;

import java.io.Serializable;
import java.util.Date;
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
import service.DevisDemmandeItemFacade;

@Named("devisDemmandeController")
@SessionScoped
public class DevisDemmandeController implements Serializable {

    @EJB
    private service.DevisDemmandeFacade ejbFacade;
    private List<DevisDemmande> items = null;
    private DevisDemmande selected;
    private Produit selectedProduit = new Produit();
    private DevisDemmandeItem devisDemmandeItem;
    private Message message;
    @EJB
    private service.DevisDemmandeItemFacade devisDemmandeItemFacade;
    private int booleanSwitch = 0;
    private DevisDemmande objetRecherche;
    private Date dateDevisDemmandeMax;
    private Date dateDevisDemmandeMin;

    public DevisDemmandeController() {
    }

    public DevisDemmande getSelected() {
        if (selected == null) {
            selected = new DevisDemmande();
        }
        return selected;
    }

    public void setSelected(DevisDemmande selected) {

        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    public Message getMessage() {
        return message;
    }

    public int getBooleanSwitch() {
        return booleanSwitch;
    }

    public void setBooleanSwitch(int booleanSwitch) {
        this.booleanSwitch = booleanSwitch;
    }

    public DevisDemmande getObjetRecherche() {
        if (objetRecherche == null) {
            objetRecherche = new DevisDemmande();
        }
        return objetRecherche;
    }

    public void setObjetRecherche(DevisDemmande objetRecherche) {
        this.objetRecherche = objetRecherche;
    }

    public Date getDateDevisDemmandeMax() {
        return dateDevisDemmandeMax;
    }

    public void setDateDevisDemmandeMax(Date dateDevisDemmandeMax) {
        this.dateDevisDemmandeMax = dateDevisDemmandeMax;
    }

    public Date getDateDevisDemmandeMin() {
        return dateDevisDemmandeMin;
    }

    public void setDateDevisDemmandeMin(Date dateDevisDemmandeMin) {
        this.dateDevisDemmandeMin = dateDevisDemmandeMin;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public DevisDemmandeItemFacade getDevisDemmandeItemFacade() {

        return devisDemmandeItemFacade;
    }

    public void setDevisDemmandeItemFacade(DevisDemmandeItemFacade devisDemmandeItemFacade) {
        this.devisDemmandeItemFacade = devisDemmandeItemFacade;
    }

    protected void initializeEmbeddableKey() {
    }

    private DevisDemmandeFacade getFacade() {
        return ejbFacade;
    }

    public DevisDemmandeItem getDevisDemmandeItem() {
        if (devisDemmandeItem == null) {
            devisDemmandeItem = new DevisDemmandeItem();
        }
        return devisDemmandeItem;
    }

    public void setDevisDemmandeItem(DevisDemmandeItem devisDemmandeItem) {
        this.devisDemmandeItem = devisDemmandeItem;
    }

    public DevisDemmandeFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(DevisDemmandeFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public Produit getSelectedProduit() {
        if (selectedProduit == null) {
            selectedProduit = new Produit();
        }
        return selectedProduit;
    }

    public void setSelectedProduit(Produit selectedProduit) {
        this.selectedProduit = selectedProduit;
    }

    public DevisDemmande prepareCreate() {
        selected = new DevisDemmande();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DevisDemmandeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DevisDemmandeUpdated"));
        items.set(items.indexOf(selected), ejbFacade.find(selected.getId())); // cancel valu

    }

    public void destroy() {
        devisDemmandeItemFacade.removeAllDevisDemmandeItemsFromDevisDemmande(selected);
        selected.getDevisDemmandeItems().clear();//vider la lists des items attacher a mon devis 
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DevisDemmandeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
//            ejbFacade.deleteDevisDemmande(selected);
            items.remove(items.indexOf(selected));
            selected = new DevisDemmande(); // Remove selection
        }
    }

    public List<DevisDemmande> getItems() {
        items = getFacade().findAll();

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

    public DevisDemmande getDevisDemmande(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<DevisDemmande> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<DevisDemmande> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = DevisDemmande.class)
    public static class DevisDemmandeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DevisDemmandeController controller = (DevisDemmandeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "devisDemmandeController");
            return controller.getDevisDemmande(getKey(value));
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
            if (object instanceof DevisDemmande) {
                DevisDemmande o = (DevisDemmande) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), DevisDemmande.class.getName()});
                return null;
            }
        }

    }

    //***********************************hamid************************************************************
    public void destroy(DevisDemmande devisDemmande) {
        selected = devisDemmande;
        destroy();

    }

    private void validateView() {
        if (getSelected().getProjet().getId() == null) {
            message = MessageManager.createErrorMessage(-1, "Merci de selectionner un projet");
        } else if (getSelected().getResponsable().getId() == null) {
            message = MessageManager.createErrorMessage(-2, "Merci de selectionner un Responsable");
        } else if (getSelected().getClient().getId() == null) {
            message = MessageManager.createErrorMessage(-3, "Merci de selectionner un Client");
        } else if (getSelected().getDevisDemmandeItems().isEmpty()) {
            message = MessageManager.createErrorMessage(-4, "Merci de selectionner un Produit");
        } else {
            message = MessageManager.createInfoMessage(1, "DevisDemande crée avec succes");
        }
    }

    public void createDevisDemmande() {
        validateView();
        if (message.getResultat() > 0) {
            ejbFacade.createDevisDemmande(getSelected(), SessionUtil.getConnectedUser());
            selected = new DevisDemmande();
        }
        MessageManager.showMessage(message);

    }

    private void createDevisDemmandeItem(Boolean devisDemmandeExist) {
        DevisDemmandeItem devisDemmandeItemCloned = devisDemmandeItemFacade.cloneDevisDemmande(selected, devisDemmandeItem, selectedProduit);
        getSelected().getDevisDemmandeItems().add(devisDemmandeItemCloned);
        if (devisDemmandeExist) {
            devisDemmandeItemFacade.createDevisCommandeItemForAnExistingDevisDemmande(devisDemmandeItem, selected);
        } else {
            getSelected().setMontantTotal(ejbFacade.calculerMontantTotalDevisDemmande(getSelected().getDevisDemmandeItems()));
        }
    }

    private void validateViewhamid() {
        if (getDevisDemmandeItem().getProduit() == null) {
            message = MessageManager.createErrorMessage(-1, "Merci de selectionner un produit");
        } else if (getDevisDemmandeItem().getPrix() == null) {
            message = MessageManager.createErrorMessage(-2, "Merci de Saisir le prix du produit ");
        } else if (getDevisDemmandeItem().getQte() == null) {
            message = MessageManager.createErrorMessage(-3, "Merci de Saisir la qte produits");
        } else {
            message = MessageManager.createInfoMessage(1, "");
        }
    }

    public void createDevisDemmandeItem() {
        validateViewhamid();
        if (message.getResultat() > 0) {
            createDevisDemmandeItem(false);
            devisDemmandeItem = new DevisDemmandeItem();
        }
        MessageManager.showMessage(message);
    }

    public void deleteDevisDemmandeItem(DevisDemmandeItem devisDemmandeItemToDelete) {
        getSelected().getDevisDemmandeItems().remove(getSelected().getDevisDemmandeItems().indexOf(devisDemmandeItemToDelete));
        getSelected().setMontantTotal(ejbFacade.calculerMontantTotalDevisDemmande(getSelected().getDevisDemmandeItems()));
    }

    private void clearView() {
        getSelected().getDevisDemmandeItems().clear();
    }

    private void clearOtherViews(DevisDemmande devisDemmande) {
        if (getSelected() != null && devisDemmande != null) {
            if (!Objects.equals(getSelected().getId(), devisDemmande.getId())) {
                clearView();

            }
        }
    }

    public void findDevisDemmandeItemsByIdDevisDemmande(DevisDemmande devisDemmande) {
        booleanSwitch = 1;
        clearOtherViews(devisDemmande);
        selected = devisDemmande;
        selected.setDevisDemmandeItems(devisDemmandeItemFacade.findDevisDemmandeItemsByIdDevisDemmande(devisDemmande));
    }

    private Message validateSerarchForm() {
        if (dateDevisDemmandeMin != null && dateDevisDemmandeMax != null) {
            if (dateDevisDemmandeMin.getTime() > dateDevisDemmandeMax.getTime()) {
                message = MessageManager.createErrorMessage(-1, "la dateDevisDemmandeMax doit etre superieur au dateDevisDemmandeMin");
            }
        } else {
            System.out.println("lmessage positif ");
            message = MessageManager.createErrorMessage(1, ""); // on affiche rien dans ce cas la 
        }
        return message;

    }

    public void findByCriteres() {
        System.out.println("akha tayedkhol elmethode ????");
        validateSerarchForm();
        if (message != null && message.getResultat() > 0) {
            System.out.println("lmessage manullch ");
            items = ejbFacade.findByCriteres(objetRecherche, SessionUtil.getConnectedUser().getAbonne(), dateDevisDemmandeMin, dateDevisDemmandeMax);
            System.out.println("sahbna khrej men service 3lakhiiir ");
            SessionUtil.setAttribute("ListaLesDevisDemmande", items);//pourqoui enregistrer cet list dans la session ??
            selected = new DevisDemmande();
        } else if (message.getResultat() < 0) {
            MessageManager.showMessage(message);
        }
        clearView();

    }

    public void removeDevisDemmandeItem(DevisDemmandeItem devisDemmandeItem) {
        this.devisDemmandeItem = devisDemmandeItem;
        this.devisDemmandeItem.setDevisDemmande(selected);
        removeDevisDemmandeItem();
    }

    public void removeDevisDemmandeItem() {

        devisDemmandeItemFacade.removeDevisDemmandeItem(devisDemmandeItem);
        selected.getDevisDemmandeItems().remove((devisDemmandeItem));

    }

    public void update(DevisDemmande devisDemmande) {
        selected = devisDemmande;
    }
}
