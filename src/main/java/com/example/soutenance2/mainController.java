package com.example.soutenance2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;
import java.awt.Desktop;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class mainController {
    @FXML
    private Button effacer;
    @FXML
    private TextField Titre;
    @FXML
    private ComboBox<String> Genre;
    @FXML
    private TextArea Resultats;
    @FXML
    private DatePicker dateRecherche;
    @FXML
    private TextField Prixmin;
    @FXML
    private TextField Prixmax;
    @FXML
    private TextArea mode_demploi;
    @FXML
    private CheckBox discog;
    @FXML
    private CheckBox fnac;
    @FXML
    private CheckBox vinylcorner;
    @FXML
    private CheckBox leboncoin;
    @FXML
    private CheckBox mesvinyles;
    @FXML
    private CheckBox culturefactory;
    @FXML
    private TextField Email;
    @FXML
    private TextField nomServeur;
    @FXML
    private TextField nomBdd;
    @FXML
    private TextField portBdd;
    @FXML
    private TextField loginUser;
    @FXML
    private TextField mdpUser;

    public TextField getTitre() {
        return Titre;
    }

    public void setTitre(TextField titre) {
        Titre = titre;
    }

    public ComboBox getGenre() {
        return Genre;
    }

    public void setGenre(ComboBox genre) {
        Genre = genre;
    }

    public DatePicker getDate() {
        return dateRecherche;
    }

    public void setDate(DatePicker date) {
        this.dateRecherche = date;
    }

    public TextField getPrixmin() {
        return Prixmin;
    }

    public void setPrixmin(TextField prixmin) {
        Prixmin = prixmin;
    }

    public TextField getPrixmax() {
        return Prixmax;
    }

    public void setPrixmax(TextField prixmax) {
        Prixmax = prixmax;
    }

    public TextField getEmail() {
        return Email;
    }

    public TextField getNomServeur() {
        return nomServeur;
    }

    public TextField getNomBdd() {
        return nomBdd;
    }

    public TextField getPortBdd() {
        return portBdd;
    }

    public TextField getLoginUser() {
        return loginUser;
    }

    public TextField getMdpUser() {
        return mdpUser;
    }
    static ArrayList<resultatScrap> scrapList = new ArrayList<>();
    public static ArrayList<resultatScrap> getScrapList() {
        return scrapList;
    }

    /**
     * onValidateEffacer
     * Permet de réintialiser les champs de l'IHM
     * suite au clic du bouton "Effacer".
     */
    @FXML
    protected void onValidateEffacer() {
        /*-----------------------reset champs-------------------------------- */
        Titre.setText("");
        Genre.setValue("Sélectionnez un genre");
        dateRecherche.getEditor().clear();
        Prixmin.setText("");
        Prixmax.setText("");

        /*-----------------------check box-------------------------------- */

        discog.setSelected(false);
        fnac.setSelected(false);
        vinylcorner.setSelected(false);
        leboncoin.setSelected(false);
        mesvinyles.setSelected(false);
        culturefactory.setSelected(false);
    }

    /**
     * onValidateRechercher
     * Récupère le contenu des champs de l'IHM
     * et appelle la fonction scrap de chaque site sélectionné grâce à ces champs.
     *
     * @throws IOException Erreur au niveau du code.
     */
    @FXML
    protected void onValidateRechercher() throws IOException {
        Scrap scrap;
        String titre = "";
        String genre;
        LocalDate date;
        int prixmin = 0;
        int prixmax = 0;
        String site = "";
        String res = "";

        titre = Titre.getText();
        genre = Genre.getSelectionModel().

                getSelectedItem();

        date = dateRecherche.getValue();

        if (titre.equals("")) {
            Popup("Veuillez entrer un titre ou un artiste à rechercher.");
        }
        try {
            prixmin = Integer.parseInt(Prixmin.getText());
            prixmax = Integer.parseInt(Prixmax.getText());
            if (prixmin < 0 || prixmax < prixmin) throw new Exception();
        } catch (
                Exception e) {
            Popup("Veuillez entrer un entier");
            Prixmin.setText("");
            Prixmax.setText("");
        }

        if (discog.isSelected()) {
            site = "discog";
            scrap = new Scrap(titre, genre, date, prixmin, prixmax, site);
            res += scrap.chercher();
        }
        if (fnac.isSelected()) {
            site = "fnac";
            scrap = new Scrap(titre, genre, date, prixmin, prixmax, site);
            res += (scrap.chercher());
        }
        if (vinylcorner.isSelected()) {
            site = "vinylcorner";
            scrap = new Scrap(titre, genre, date, prixmin, prixmax, site);
            res += scrap.chercher();
        }
        if (leboncoin.isSelected()) {
            site = "leboncoin";
            scrap = new Scrap(titre, genre, date, prixmin, prixmax, site);
            res += scrap.chercher();
        }
        if (mesvinyles.isSelected()) {
            site = "mesvinyles";
            scrap = new Scrap(titre, genre, date, prixmin, prixmax, site);
            res += scrap.chercher();
        }
        if (culturefactory.isSelected()) {
            site = "culturefactory";
            scrap = new Scrap(titre, genre, date, prixmin, prixmax, site);
            res += scrap.chercher();
        }
        if (!discog.isSelected() && !fnac.isSelected() && !vinylcorner.isSelected() && !leboncoin.isSelected() && !mesvinyles.isSelected() && !culturefactory.isSelected()) {
            Popup("Veuillez sélectionner un site");
        }
        Resultats.setText(res);
    }


    /**
     * afficherMail.
     * Affiche la fenêtre relative à l'envoi d'email.
     *
     * @throws IOException Erreur au niveau du code.
     */
    @FXML
    protected void afficherMail() throws IOException {
        Stage mail = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(musicApplication.class.getResource("mail.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 270);
        mail.setTitle("Envoyer par mail");
        mail.setScene(scene);
        mail.show();
    }

    /**
     * envoyerMail
     * Permet d'envoyer un email au destinataire renseigné
     * avec en pièce jointe le fichier choisi par l'utilisateur.
     *
     * @throws IOException Erreur au niveau du code.
     */
    @FXML
    protected void envoyerMail() throws IOException {
        String email = "";
        email = Email.getText();

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        // Configure API key authorization: api-key
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");

        // version github
        apiKey.setApiKey("Insérez la clé api ici");

        try {
            // expéditeur

            TransactionalEmailsApi api = new TransactionalEmailsApi();
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail("francois.leplain@gmail.com");
            sender.setName("François Le Plain");


            // Destinataire

            List<SendSmtpEmailTo> toList = new ArrayList<SendSmtpEmailTo>();
            SendSmtpEmailTo to = new SendSmtpEmailTo();

            to.setEmail(email);
            toList.add(to);

            // Pièce jointe

            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Ouvrir");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File fichierResultat = fileChooser.showOpenDialog(stage);

            SendSmtpEmailAttachment attachment = new SendSmtpEmailAttachment();
            attachment.setName(fichierResultat.getName());
            String fichierPath = (fichierResultat.getPath());
            byte[] encode = Files.readAllBytes(Paths.get(fichierPath));
            attachment.setContent(encode);
            List<SendSmtpEmailAttachment> attachmentList = new ArrayList<SendSmtpEmailAttachment>();
            attachmentList.add(attachment);

            // Propriétés du mail

            Properties headers = new Properties();
            headers.setProperty("Some-Custom-Name", "unique-id-1234");
            Properties params = new Properties();
            params.setProperty("parameter", "My param value");      // ??
            params.setProperty("subject", "Rapport de recherche");              // sujet du mail
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);
            sendSmtpEmail.setHtmlContent("<html><body><h4>Bonjour,<br><br>Vous trouverez en pièce jointe, le fichier texte " +
                    "contenant les résultats de votre recherche.<br><br>Bonne journée.</h4></body></html>");         // corps du mail
            sendSmtpEmail.setSubject("{{params.subject}}");
            sendSmtpEmail.setAttachment(attachmentList);
            sendSmtpEmail.setHeaders(headers);
            sendSmtpEmail.setParams(params);


            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);

        } catch (Exception e) {
            System.out.println("Exception occurred:- " + e.getMessage());
        }
    }

    /**
     * ecrireDansUnFichier
     * Permet de sauvegarder le résultat de la recherche préalablement effectuée
     * dans un fichier texte à l'emplacement souhaité par l'utilisateur.
     *
     * @throws IOException Erreur au niveau du code
     */
    @FXML
    protected void ecrireDansUnFichier() throws IOException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegardez votre recherche");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File fichierResultat = fileChooser.showSaveDialog(stage);
        if (fichierResultat != null) {
            PrintWriter ecrire = new PrintWriter(fichierResultat);
            ecrire.print(Resultats.getText());
            ecrire.close();
        }
    }

    /**
     * afficherBdd
     * Affiche la fenêtre relative au paramétrage de la base de données.
     *
     * @throws IOException Erreur au niveau du code.
     */
    @FXML
    protected void afficherBdd() throws IOException {
        Stage mail = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(musicApplication.class.getResource("bdd.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 483, 498);
        mail.setTitle("Paramètres de la base de données");
        mail.setScene(scene);
        mail.show();
    }

    /**
     * onValidateBdd
     * Récupère le contenu des champs, tente une connexion à la BDD donnée
     * Si la tentative est un succès, stocke le contenu des champs dans un fichier texte
     * Sinon, averti l'utilisateur de l'échec de la connexion.
     *
     * @throws IOException Erreur au niveau du code.
     */
    @FXML
    protected void onValidateBdd() throws IOException {
        String serveur = nomServeur.getText();
        int port = Integer.parseInt(portBdd.getText());
        String bdd = nomBdd.getText();
        String user = loginUser.getText();
        String mdp = mdpUser.getText();

        // auto close connection and preparedStatement
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://" + serveur + ":" + port + "/" + bdd + "?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", user, mdp)) {
            if (conn != null) {
                System.out.println("Connected to the database!");
                File rep = new File("db");
                rep.mkdir();
                String nomFichierSortie = rep + File.separator + "db_connexion.txt";
                PrintWriter ecrire;
                String db;
                ecrire = new PrintWriter(new BufferedWriter
                        (new FileWriter(nomFichierSortie)));
                db = serveur + "\n" + port + "\n" + bdd + "\n" + user + "\n" + mdp;
                ecrire.println(db);
                ecrire.close();
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * transfertBdd
     * Affiche la fenêtre relative au transfert des données vers la BDD.
     */
    @FXML
    protected void transfertBdd() {
        Stage bdd = new Stage();
        bdd.initModality(Modality.APPLICATION_MODAL);
        bdd.setTitle("Transmission BDD");
        Label label1 = new Label("Transmission des données à la base de données");
        Label label2 = new Label("Cliquez sur valider pour lancer la transmission");
        Button button1 = new Button("Valider");
        button1.setOnAction(e -> {
            try {
                enregistrerBdd();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        Button button2 = new Button("Fermer");
        button2.setOnAction(e -> bdd.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, label2, button1, button2);
        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 350, 200);
        bdd.setScene(scene1);
        bdd.show();
    }

    /**
     * enregistrerBdd
     * Se connecte à la BDD configurée à l'aide du fichier texte
     * Enregistre dans la BDD chaque résultat scrappé contenu dans la liste.
     *
     * @throws IOException Erreur au niveau du code.
     */
    @FXML
    protected void enregistrerBdd() throws IOException {
        BufferedReader reader;
        String nomServeur;
        int port;
        String nomBdd;
        String user;
        String mdp;

        reader = new BufferedReader(new FileReader("db/db_connexion.txt"));
        nomServeur = reader.readLine();
        port = Integer.parseInt(reader.readLine());
        nomBdd = reader.readLine();
        user = reader.readLine();
        mdp = reader.readLine();

        // auto close connection and preparedStatement
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://" + nomServeur + ":" + port + "/" + nomBdd + "?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", user, mdp)) {
            for (resultatScrap resScrap : scrapList) {
                enregistrementBDD.saveBdd(resScrap.titre, resScrap.desc, resScrap.prix, resScrap.genre, resScrap.lien);
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * afficherAide
     * Ouvre le pdf "aide", guide utilisateur.
     *
     * @throws IOException Erreur au niveau du code
     */
    @FXML
    protected void afficherAide() throws IOException {
        Desktop bureau = Desktop.getDesktop();
        File aide = new File("mode_demploi" + File.separator + "aide.pdf");
        bureau.open(aide);
    }
    /**
     * Popup
     * Affiche une fenêtre contenant le message d'erreur
     * que l'on souhaite transmettre à l'utilisateur
     *
     * @param erreur Message d'erreur à afficher
     * @throws IOException Erreur au niveau du code
     */
    public static void Popup(String erreur) throws IOException {
        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Erreur");
        Label label1 = new Label(erreur);
        Button button1 = new Button("fermer");
        button1.setOnAction(e -> popupwindow.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, button1);
        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 300, 200);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();
    }
}