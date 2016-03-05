/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.util;

import bean.Abonne;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.commons.io.FileUtils;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author abdelmoughit
 */
public class FileUtil {

    public static String vmParam = "stock.projet.files.path";//chemin dont laquelle on va creer le dosqsier globale qui aura pour bute de contenir la totalitees des dossier d un abonnee

    //generer la totalitees des dossier qui concerne un new abonne
    public static String generateFiles(String indice) {

        List<File> files = new ArrayList<File>();
        String path = System.getProperty(vmParam);
        if (path == null) {
            FacesMessage message = new FacesMessage("Erreur", "option JVM manquante \"" + vmParam + "\"");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {

            File file3 = new File(path + "/" + indice + "/DevisCommande");
            File file4 = new File(path + "/" + indice + "/DevisDemande");
            File file5 = new File(path + "/" + indice + "/PaimentAchat");
            File file6 = new File(path + "/" + indice + "/PaimentCommande");
            File file7 = new File(path + "/" + indice + "/PaimentVenteDirect");
            File file8 = new File(path + "/" + indice + "/PhotoCheque");
            File file9 = new File(path + "/" + indice + "/PhotoEffet");
            File file10 = new File(path + "/" + indice + "/PhotoProduit");
            File file11 = new File(path + "/" + indice + "/Livraison");
            File file12 = new File(path + "/" + indice + "/Logo");

            files.add(file3);
            files.add(file4);
            files.add(file5);
            files.add(file6);
            files.add(file7);
            files.add(file8);
            files.add(file9);
            files.add(file10);
            files.add(file11);
            files.add(file12);

            for (File loaded : files) {
                System.out.println(mkdir(loaded));
            }
            return path + "\\" + indice;
        }
        return "";

    }

    //creer les dossier et c est sous dossier
    private static String mkdir(File file) {
        if (!file.exists()) {
            if (file.mkdirs()) {//mkdir
                return file.getName() + " Directory is created!";
            }
            return "Failed to create " + file.getName() + " directory!";
        }
        return file.getName() + " Directory already existe!";
    }

    public static void upload(UploadedFile file, String emplacement, Abonne abonne, File f) throws IOException {
        System.out.println("upload");
        //String vmParam = "irisi.projet.upload.path";
        String fullPath = "";
        if (file != null) {
            String path = System.getProperty(vmParam);
            if (path == null) {
                JsfUtil.addErrorMessage(null, "option JVM manquante \"" + vmParam + "\"");
            } else {
                File folder = new File(path + "\\" + abonne.getNom() + "XX" + abonne.getId() + "\\" + emplacement + "\\");//creer la path qui va contenir notre fichier
                if (!folder.exists()) {
                    folder.mkdirs(); // Création de l'arborescense (dossier et sous dossier)
                }
                System.out.println("file.getFileName() ==> " + file.getFileName());
                String nameModified = file.getFileName().replace('.', ':');
                String[] str = nameModified.split(":");
                String fileName = str[0];
                String extension = str[1];
                String outputPath = path + "\\" + abonne.getNom() + "XX" + abonne.getId() + "\\" + emplacement + "\\" + fileName + new Date().getTime() + "." + extension;//hna fin kayéthéét l fichier selectionné
                System.out.println(outputPath);
                File outputFile = new File(outputPath);
                Files.copy(file.getInputstream(), outputFile.toPath());
                JsfUtil.addSuccessMessage(file.getFileName() + " est bien reçcu.");
            }
        }
    }

    public static String uploadV2(UploadedFile file, String emplacement, Abonne abonne) throws IOException {
        System.out.println("upload");
        //String vmParam = "irisi.projet.upload.path";
        String fullPath = "";
        if (file != null) {
            String path = System.getProperty(vmParam);
            if (path == null) {
                JsfUtil.addErrorMessage(null, "option JVM manquante \"" + vmParam + "\"");
            } else {
                File folder = new File(abonne.getCheminDossier() + "\\" + emplacement + "\\");//creer la path qui va contenir notre fichier
                if (!folder.exists()) {
                    folder.mkdirs(); // Création de l'arborescense (dossier et sous dossier)
                }
                System.out.println("file.getFileName() ==> " + file.getFileName());
                String nameModified = file.getFileName().replace('.', ':');
                String[] str = nameModified.split(":");
                String fileName = str[0];
                String extension = str[1];
                String outputPath = abonne.getCheminDossier() + "\\" + emplacement + "\\" + fileName + new Date().getTime() + "." + extension;//hna fin kayéthéét l fichier selectionné
                System.out.println(outputPath);
                File outputFile = new File(outputPath);
                Files.copy(file.getInputstream(), outputFile.toPath());
                JsfUtil.addSuccessMessage(file.getFileName() + " est bien reçcu.");
                return outputPath;
            }
        }
        return "";
    }

    //haadi hiia li kané3tiiha téswira okaté3tini l inputeStream dialha bach apres nézréé3ha fjasper pour le logo 
    public static InputStream transFileToInputeStream(File initialFile) throws IOException {
        initialFile = new File("C:\\photography-logo-design (21).gif");
        InputStream targetStream = FileUtils.openInputStream(initialFile);
        return targetStream;
    }

    //cree un fichier apartir d un tableau des bytes .. en general c est les bytes cree par jasper lors de la generation d un pdf
    public static void CreateFileFromAnArrayOfBytes(byte[] bFile) throws IOException {
        File file = new File("E:\\testing.pdf");
        FileUtils.writeByteArrayToFile(file, bFile);//au cas ou kan deja wahéd lfichier blmeme nom+type katecrasiih

    }

    public static void uploadVersionFile(Abonne abonne, String typeRaportGenerated, String fileName, byte[] DataGenerated) {

    }

}
