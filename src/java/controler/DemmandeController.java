package controler;

import bean.Abonne;
import bean.DemmandeItem;
import bean.Demmande;
import bean.Famille;
import bean.Magasin;
import bean.Produit;
import bean.Reception;
import bean.User;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.Message;
import controler.util.MessageManager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import service.DemmandeFacade;
import service.PaiementDemmandeFacade;
import service.ReceptionFacade;
import service.ReceptionItemFacade;

@Named("demmandeController")
@SessionScoped
public class DemmandeController implements Serializable {

    private @EJB
    service.DemmandeFacade ejbFacade;
    private @EJB
    service.DemmandeItemFacade demmandeItemFacade;
    private @EJB
    ReceptionFacade receptionFacade;
    private @EJB
    PaiementDemmandeFacade paiementDemmandeFacade;
    private @EJB
    service.ProduitFacade produitFacade;
    private @EJB
    service.MagasinFacade magasinFacade;
    private @EJB
    service.FamilleFacade familleFacade;
    private @EJB
    ReceptionItemFacade receptionItemFacade;

    private List<Demmande> items;
    private List<Demmande> itemsByAbonne;//radi ykono fiha les demmande dial l abonne tl user li mconecti
    private Demmande selected;
    private Reception reception;
    private DemmandeItem commandItem;
    private Produit selectedProduit = new Produit();
    private Magasin selectedMagasin = new Magasin();
    private Famille selectedFamille = new Famille();

    private List<Produit> produits;
    private List<Magasin> magasins;
    private List<Famille> familles;
    private List<DemmandeItem> demmandeItems;

    private Demmande critereObj;
    private int etatReception;
    private int etatPaiement;
    private Date dateDemmandeMin = new Date();
    private Date dateDemmandeMax;
    private Date dateEcheanceMin;
    private Date dateEcheanceMax;
    private int booleanSwitch = 0;
    private boolean alertSelected;
    private Message message;

    public int compareDateToNow(Date date) {
        return date.compareTo(new Date());
    }

    public void initAbonne(Abonne abonne) {
        if (abonne != null && abonne.getId() != null) {
            getSelected().setAbonne(abonne);
//            items = ejbFacade.findyCriteres(selected, selected.getAbonne(), 0, etatPaiement, etatReception, dateDemmandeMin, dateDemmandeMax, dateEcheanceMin, dateEcheanceMax);
        }

    }

    private Message validateSerarchForm() {
        if (dateDemmandeMin != null && dateDemmandeMax != null) {
            if (dateDemmandeMin.getTime() > dateDemmandeMax.getTime()) {
                message = MessageManager.createErrorMessage(-1, "la dateDemmandeMax doit etre superieur au dateDemmandeMin");
            }
        } else if (dateEcheanceMin != null && dateEcheanceMax != null) {
            if (dateEcheanceMin.getTime() > dateEcheanceMax.getTime()) {
                message = MessageManager.createErrorMessage(-2, "la dateEcheanceMax doit etre superieur au dateEcheanceMin");
            }
        } else {
            message = MessageManager.createErrorMessage(1, "");
        }
        return message;

    }

    public void findByCriteres(Abonne abonne, int deleted) {
        validateSerarchForm();
        if (message != null && message.getResultat() > 0) {
//            items = getFacade().findyCriteres(critereObj, abonne, deleted, etatPaiement, etatReception, dateDemmandeMin, dateDemmandeMax, dateEcheanceMin, dateEcheanceMax);
            selected = new Demmande();
        } else if (message.getResultat() < 0) {
            MessageManager.showMessage(message);
        }
    }

    public void findCommadeItemsByIdCmd(Demmande demmande) {
        booleanSwitch = 1;
        selected = demmande;
        //selected.setDemmandeItems(demmandeItemFacade.findCommadeItemsByIdCmd(demmande));
    }

    public void findReceptionByDemmande(Demmande demmande) {
        if (reception != null) {
            reception = new Reception();
        }
        selected = demmande;
        //selected.setReceptions(receptionFacade.findReceptionByDemmande(demmande));
    }

    public void findPaiementByDemmande(Demmande demmande) {
        booleanSwitch = 2;
        selected = demmande;
//        selected.setPaiementDemmandes(paiementDemmandeFacade.findPaiementByDemmande(demmande));
    }

    public void findReceptionItemsByReception(Reception recep) {
        reception = recep;
        reception.setReceptionItems(receptionItemFacade.findReceptionItemsByReception(recep));
    }

    public String versDemmandeItems(Demmande demmande) {
        selected = demmande;
        return "versDemmandeItems";
    }

    public void generateReferenceIndexDemmande() {
//        getSelected().setReferenceIndex(ejbFacade.generateReferenceIndexDemmande(selected));
    }

    public List<Famille> findFamilleByAbonne(Abonne abonne) {
        familles = familleFacade.findByAbonne(abonne);
        return familles;
    }

    public void findProduitByMagasinAndFamille(Magasin magasin, Famille famille, int deleted) {
        produits = produitFacade.findProduitByMagasinAndFamille(magasin, famille, deleted);
    }

    public List<Magasin> findMagasinByAbonne(Abonne abonne, int deleted) {
        magasins = magasinFacade.findByAbonne(abonne, deleted);
        return magasins;
    }

    private void constructReference() {
//        String reference = "";
//        if (getSelected().getReferencePriffix() != null && !selected.getReferencePriffix().equals("")) {
//            reference = selected.getReferencePriffix() + "-";
//        }
//        reference += selected.getReferenceIndex();
//        if (selected.getReferenceSuffix() != null && !selected.getReferenceSuffix().equals("")) {
//            reference += ("-" + selected.getReferenceSuffix());
//        }
//        selected.setReference(reference);
    }

    private boolean verifierExistenceReference() {
         return true;
                 //ejbFacade.verifierExistenceReference(selected, selected.getAbonne(), 0);
    }

    private void validateView() {
        constructReference();
        if (getSelected().getProjet().getId() == null) {
            message = MessageManager.createErrorMessage(-1, "Merci de selectionner un projet");
        } else if (getSelected().getResponsable().getId() == null) {
            message = MessageManager.createErrorMessage(-2, "Merci de selectionner un Responsable");
        } //else if (getSelected().getFournisseur().getId() == null) {
        //            message = Message.createErrorMessage(-3, "Merci de selectionner un Fournisseur");
        //        } 
        else if (getDemmandeItems().isEmpty()) {
            message = MessageManager.createErrorMessage(-4, "Merci de selectionner un Produit");
        } else if (verifierExistenceReference() == true) {
            message = MessageManager.createErrorMessage(-5, "La valeur de la référence est déjà affectée");
        } else {
            message = MessageManager.createErrorMessage(1, "Demmande crée avec succes");
        }
    }

    public void createDemmande(User user) {

        validateView();
        if (message.getResultat() > 0) {
            selected.setDemmandeItems(demmandeItems);
         //   ejbFacade.createDemmande(selected, user);
            demmandeItems = null;
            selected = null;
        }
        MessageManager.showMessage(message);

    }

    public void deleteDemmandeItem(DemmandeItem demmandeItem) {
        int position = produitExist(demmandeItem.getProduit());
        if (position != -1) {
            demmandeItems.remove(position);
            JsfUtil.addSuccessMessage("Produit Supprimer avec succes!!");
        }
        selected.setMontantTotal(calculerMontantTotal());
    }

    public void createDemmandeItem() {
        DemmandeItem clonedDemmandeItem = cloneDemmandeItem();
        if (clonedDemmandeItem.getProduit().getQteParStock().compareTo(clonedDemmandeItem.getQte()) >= 0) {
            int position = produitExist(clonedDemmandeItem.getProduit());
            if (position == -1) {
                demmandeItems.add(clonedDemmandeItem);
                JsfUtil.addSuccessMessage("Quantité disponnible: Le Stock disponnible est "
                        + clonedDemmandeItem.getProduit().getQteParStock() + " alors que la quantité "
                        + "commandé est " + clonedDemmandeItem.getQte());
            } else {
                demmandeItems.set(position, commandItem);
                JsfUtil.addSuccessMessage("Produit Modifier avec succes!!");
            }
            selected.setMontantTotal(calculerMontantTotal());
        } else {
            demmandeItems.add(clonedDemmandeItem);
            JsfUtil.addWrningMessage("Quantité disponnible: Le Stock disponnible est "
                    + clonedDemmandeItem.getProduit().getQteParStock() + " alors que la quantité "
                    + "commandé est " + clonedDemmandeItem.getQte());
        }
    }

    private DemmandeItem cloneDemmandeItem() {
        DemmandeItem demmandeItemClone = new DemmandeItem();
        demmandeItemClone.setMagasin(cloneMagasin());
        demmandeItemClone.setPrix(getCommandItem().getPrix());
        demmandeItemClone.setQte(getCommandItem().getQte());
        demmandeItemClone.setProduit(cloneProduitWithFamille());
        demmandeItemClone.setDemmande(getSelected());
        demmandeItemClone.setQteAvoir(new BigDecimal(0));
//        demmandeItemClone.setQteRecu(new BigDecimal(0));
        return demmandeItemClone;
    }

    private Produit findProduit(Produit produit) {
        for (Produit item : produits) {
            if (Objects.equals(item.getId(), produit.getId())) {
                System.out.println("haa founded item ==> id = " + item.getId() + " qteStock = " + item.getQteParStock());
                return item;
            }
        }
        return null;
    }

    private Produit cloneProduitWithFamille() {
        Produit produit = new Produit();
        Produit produitInList = findProduit(getSelectedProduit());
        produit.setId(produitInList.getId());
        produit.setLibelle(produitInList.getLibelle());
        produit.setQteParStock(produitInList.getQteParStock());
        produit.setFamille(cloneFamille());
        return produit;
    }

    private Famille cloneFamille() {
        Famille famille = new Famille();
        famille.setId(getSelectedFamille().getId());
        famille.setLibelle(getSelectedFamille().getLibelle());
        return famille;
    }

    private Magasin cloneMagasin() {
        Magasin magasin = new Magasin();
        magasin.setId(getSelectedMagasin().getId());
        magasin.setNom(getSelectedMagasin().getNom());
        return magasin;
    }

    private int produitExist(Produit produit) {
        int i = 0;
        for (DemmandeItem item : getDemmandeItems()) {
            System.out.println("ana f item " + item.getProduit().getId() + " smyeto " + item.getProduit().getReference());
            if (Objects.equals(produit.getId(), item.getProduit().getId())) {
                System.out.println("ana fost if == " + item.getProduit().getId());
                return i;
            }
            i++;
        }
        return -1;
    }

    private BigDecimal calculerMontantTotal() {
        BigDecimal montantTotal = new BigDecimal(0);
        for (DemmandeItem item : getDemmandeItems()) {
            montantTotal = montantTotal.add(item.getPrix().multiply(item.getQte()));
        }
        return montantTotal;
    }

    public void findAll() {
        items = null;
    }

    public Demmande getCritereObj() {
        if (critereObj == null) {
            critereObj = new Demmande();
        }

        return critereObj;
    }

    public void setCritereObj(Demmande critereObj) {
        if (critereObj == null) {
            critereObj = new Demmande();
        }
        this.critereObj = critereObj;
    }

    public Reception getReception() {
        if (reception == null) {
            reception = new Reception();
        }
        return reception;
    }

    public void setReception(Reception reception) {
        this.reception = reception;
    }

    public int getBooleanSwitch() {
        return booleanSwitch;
    }

    public void setBooleanSwitch(int booleanSwitch) {
        this.booleanSwitch = booleanSwitch;
    }

    public Date getDateDemmandeMin() {
        return dateDemmandeMin;
    }

    public void setDateDemmandeMin(Date dateDemmandeMin) {
        this.dateDemmandeMin = dateDemmandeMin;
    }

    public Date getDateDemmandeMax() {
        return dateDemmandeMax;
    }

    public void setDateDemmandeMax(Date dateDemmandeMax) {
        this.dateDemmandeMax = dateDemmandeMax;
    }

    public Date getDateEcheanceMin() {
        return dateEcheanceMin;
    }

    public void setDateEcheanceMin(Date dateEcheanceMin) {
        this.dateEcheanceMin = dateEcheanceMin;
    }

    public Date getDateEcheanceMax() {
        return dateEcheanceMax;
    }

    public void setDateEcheanceMax(Date dateEcheanceMax) {
        this.dateEcheanceMax = dateEcheanceMax;
    }

    public int getEtatPaiement() {
        return etatPaiement;
    }

    public void setEtatPaiement(int etatPaiement) {
        this.etatPaiement = etatPaiement;
    }

    public int getEtatReception() {
        return etatReception;
    }

    public void setEtatReception(int etatReception) {
        this.etatReception = etatReception;
    }

    public DemmandeController() {
    }

    public Demmande getSelected() {
        if (selected == null) {
            selected = new Demmande();
        }
        return selected;
    }

    public void setSelected(Demmande selected) {
        this.selected = selected;
    }

    public DemmandeItem getCommandItem() {
        if (commandItem == null) {
            commandItem = new DemmandeItem();
        }
        return commandItem;
    }

    public void setCommandItem(DemmandeItem commandItem) {
        this.commandItem = commandItem;
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

    public Magasin getSelectedMagasin() {
        if (selectedMagasin == null) {
            selectedMagasin = new Magasin();
        }
        return selectedMagasin;
    }

    public void setSelectedMagasin(Magasin selectedMagasin) {
        this.selectedMagasin = selectedMagasin;
    }

    public Famille getSelectedFamille() {
        if (selectedFamille == null) {
            selectedFamille = new Famille();
        }
        return selectedFamille;
    }

    public void setSelectedFamille(Famille selectedFamille) {
        this.selectedFamille = selectedFamille;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DemmandeFacade getFacade() {
        return ejbFacade;
    }

    public Demmande prepareCreate() {
        selected = new Demmande();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DemmandeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DemmandeUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DemmandeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Demmande> getItemsByAbonne() {
        itemsByAbonne = getFacade().findAll();//apres radi nbédloha b findDemmandeByAbonne
        return itemsByAbonne;
    }

    public void setItemsByAbonne(List<Demmande> itemsByAbonne) {
        this.itemsByAbonne = itemsByAbonne;
    }

    public List<Demmande> getItems() {
        return items;
    }

    public List<Produit> getProduits() {
        if (produits == null) {
            produits = new ArrayList();
        }
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }

    public List<Magasin> getMagasins() {
        if (magasins == null) {
            magasins = new ArrayList();
        }
        return magasins;
    }

    public void setMagasins(List<Magasin> magasins) {
        this.magasins = magasins;
    }

    public List<Famille> getFamilles() {
        if (familles == null) {
            familles = new ArrayList();
        }
        return familles;
    }

    public void setFamilles(List<Famille> familles) {
        this.familles = familles;
    }

    public List<DemmandeItem> getDemmandeItems() {
        if (demmandeItems == null) {
            demmandeItems = new ArrayList();
        }
        return demmandeItems;
    }

    public void setDemmandeItems(List<DemmandeItem> demmandeItems) {
        this.demmandeItems = demmandeItems;
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

    public List<Demmande> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Demmande> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Demmande.class)
    public static class DemmandeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DemmandeController controller = (DemmandeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "demmandeController");
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
            if (object instanceof Demmande) {
                Demmande o = (Demmande) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Demmande.class.getName()});
                return null;
            }
        }

    }

    public boolean isAlertSelected() {
        return alertSelected;
    }

    public void setAlertSelected(boolean alertSelected) {
        this.alertSelected = alertSelected;
    }

}
