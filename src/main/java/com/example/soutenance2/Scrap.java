package com.example.soutenance2;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

/**
 * La classe Scrap
 */
public class Scrap {

    String titre;
    String genre;
    LocalDate date;
    int prixmin;
    int prixmax;
    String site;
    int id_genre;

    PrintWriter ecrire;
    WebClient webClient = new WebClient();


    /**
     * Instantiates a new Scrap.
     *
     * @param titre Titre de l'œuvre, nom de l'artiste
     * @param genre Le genre musical
     * @param date  La date d'édition
     * @param prixmin Le prix minimal choisi par l'utilisateur
     * @param prixmax Le prix maximal choisi par l'utilisateur
     * @param site  Le site scrappé
     */
    public Scrap(String titre, String genre, LocalDate date, int prixmin, int prixmax, String site) {
        this.titre = titre;
        this.genre = genre;
        this.date = date;
        this.prixmin = prixmin;
        this.prixmax = prixmax;
        this.site = site;
    }

    /**
     * chercher
     *
     * Récupère le contenu des champs de recherche
     * Selon les sites sélectionnés, effectue le scrap correspondant
     * Accumule les résultats dans une chaîne de caractères
     * Ajoute chaque résultat à une liste
     *
     * @return la chaîne de caractère contenant tous les résultats
     * @throws IOException Erreur dans le code
     */
    public String chercher() throws IOException {

        // Nom du produit
        String search = titre;
        // Genre
        String gender = genre;
        // Prix min
        String min = String.valueOf(prixmin);
        // Prix max
        String max = String.valueOf(prixmax);
        // site
        String url = "";
        // Resultat
        String aff = "";
        // Date
        LocalDate dateJ = date;

        WebClient webClient = new WebClient(BrowserVersion.FIREFOX);

        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        /*--------------------------------- Scrappers ----------------------------------------*/

        if (site.equals("discog")) {
            url = "https://www.discogs.com/fr/sell/list?genre="+ gender +"q=" + search + "&format=CD&currency=EUR";
            HtmlPage htmlPage = webClient.getPage(url);
            String res = """
                    /*----------------------------------------------------------------------------------*/
                    
                     _____   _                        \s
                    |  __ \\ (_)                       \s
                    | |  | | _  ___   ___  ___    __ _\s
                    | |  | || |/ __| / __|/ _ \\  / _` |
                    | |__| || |\\__ \\| (__| (_) || (_| |
                    |_____/ |_||___/ \\___|\\___/  \\__, |
                                                  __/ |
                                                 |___/\s
                    /*----------------------------------------------------------------------------------*/
                    
                    \n""";

            List<HtmlElement> li = htmlPage.getByXPath("//td[2]/strong/a");

            for (HtmlElement e : li) {
                HtmlPage htmlPage1 = webClient.getPage(e.click().getUrl());
                String nomArticle = "";
                String ntmp = "";
                String prixArticle = "";
                String ptmp = "";
                String description = "";
                String lien = String.valueOf(htmlPage1.getUrl());

                List<HtmlElement> nom = htmlPage1.getByXPath("//div[1]/div/div[1]/h1/span[2]");
                List<HtmlElement> prix = htmlPage1.getByXPath("//div[2]/div/div[1]/div/div/p/span[1]");

                for (HtmlElement n : nom) {
                    ntmp = n.getTextContent();
                    ntmp = ntmp.trim();

                    if (ntmp.contains(titre)){
                        nomArticle = ntmp;
                    }
                }
                for (HtmlElement p : prix) {
                    ptmp = p.getTextContent();
                    ptmp = ptmp.replace("€", "");
                    ptmp = ptmp.replace(",", ".");

                    if (Double.parseDouble(ptmp) > prixmin && Double.parseDouble(ptmp) < prixmax) {
                        prixArticle = ptmp;
                    }
                }
                if (!prixArticle.equals("") && !nomArticle.equals("")) {
                    res += "\nArticle : " + nomArticle.trim() + "\nPrix : " + prixArticle + "€\nDescription de l'article : Aucune description disponible pour ce site." + "\nlien : " + lien +
                            "\n--------------------------------------------------------------------------------------------\n";
                    aff += res;
                    switch (genre) {
                        case "Rock" -> id_genre = 1;
                        case "Blues" -> id_genre = 2;
                        case "Jazz" -> id_genre = 3;
                        case "Reggae" -> id_genre = 4;
                        case "Funk" -> id_genre = 5;
                        case "Soul" -> id_genre = 6;
                        case "Electro" -> id_genre = 7;
                        default -> id_genre = 0;
                    }
                    resultatScrap resScrap = new resultatScrap(nomArticle,id_genre,dateJ,Double.parseDouble(prixArticle.replace("€","").replace(",","")),description,lien);
                    mainController.getScrapList().add(resScrap);
                }
            }
            return res;
        }

        if (site.equals("fnac")) {
            url = "https://www.fnac.com/SearchResult/ResultList.aspx?SCat=3!1&SDM=list&Search=" + search + "sft=1";
            HtmlPage htmlPage = webClient.getPage(url);

            List<HtmlElement> li = htmlPage.getByXPath("//div[2]/div/p[1]/span/a");

            String res = """
                    /*----------------------------------------------------------------------------------*/
                    
                     ______                  \s
                    |  ____|                 \s
                    | |__  _ __    __ _   ___\s
                    |  __|| '_ \\  / _` | / __|
                    | |   | | | || (_| || (__\s
                    |_|   |_| |_| \\__,_| \\___|
                                             \s
                                             \s
                    /*----------------------------------------------------------------------------------*/
                    
                    \n""";

            for (HtmlElement e : li) {
                HtmlPage htmlPage1 = webClient.getPage(e.click().getUrl());
                String nomArticle = "";
                String prixArticle = "";
                String description = "";
                String ptmp;
                String lien = String.valueOf(htmlPage1.getUrl());

                List<HtmlElement> nom = htmlPage1.getByXPath("//div[1]/div[1]/section/h1");
                List<HtmlElement> prix = htmlPage1.getByXPath(".//span[contains(@class,'userPrice')]");
                List<HtmlElement> desc = htmlPage1.getByXPath("//div[2]/div/div[4]/section[1]/div/div[1]/div/div/div/div[1]/div/div");

                for (HtmlElement n : nom) {
                    nomArticle = n.getTextContent();
                }
                for (HtmlElement p : prix) {
                    prixArticle = "";
                    ptmp = p.getTextContent().replace(",", ".");
                    ptmp = ptmp.replace("€", "");
                    if (Double.parseDouble(ptmp) > prixmin && Double.parseDouble(ptmp) < prixmax) {
                        prixArticle = ptmp;
                    }
                }
                for (HtmlElement d : desc) {
                    description = d.getTextContent();
                    if (description.equals("")) {
                        description = "Aucune description pour cet article.";
                    }
                }
                if (!prixArticle.equals("")) {
                    res += "\nArticle : " + search + "\t " + nomArticle.trim() + "\nPrix : " + prixArticle.trim() + "€\nDescription de l'article : " + description.trim() + "\nlien : " + lien +
                            "\n--------------------------------------------------------------------------------------------\n";
                    aff += res;
                    switch (genre) {
                        case "Rock" -> id_genre = 1;
                        case "Blues" -> id_genre = 2;
                        case "Jazz" -> id_genre = 3;
                        case "Reggae" -> id_genre = 4;
                        case "Funk" -> id_genre = 5;
                        case "Soul" -> id_genre = 6;
                        case "Electro" -> id_genre = 7;
                        default -> id_genre = 0;
                    }
                    resultatScrap resScrap = new resultatScrap(nomArticle,id_genre,dateJ,Double.parseDouble(prixArticle.replace("€","").replace(",","")),description,lien);
                    mainController.getScrapList().add(resScrap);
                }
            }
            return res;
        }

        if (site.equals("vinylcorner")) {
            url = "https://www.vinylcorner.fr/catalogsearch/result/?q=" + search;
            switch(genre) {
                case "Rock":
                    url = "https://www.vinylcorner.fr/catalogsearch/result/?q=" + search + "&category=5";
                    break;
                case "Blues":
                case "Jazz":
                    url = "https://www.vinylcorner.fr/catalogsearch/result/?q=" + search + "&category=11";
                    break;
                case "Reggae":
                    url = "https://www.vinylcorner.fr/catalogsearch/result/?q=" + search + "&category=10";
                    break;
                case "Funk":
                case "Soul":
                    url = "https://www.vinylcorner.fr/catalogsearch/result/?q=" + search + "&category=9";
                case "Electro":
                    url = "https://www.vinylcorner.fr/catalogsearch/result/?q=" + search + "&category=7";
                default:
                    url = "https://www.vinylcorner.fr/catalogsearch/result/?q=" + search;
            }
            HtmlPage htmlPage = webClient.getPage(url);

            List<HtmlElement> li = htmlPage.getByXPath("//div/div[2]/strong/a");

            String res = """
                     /*----------------------------------------------------------------------------------*/
                    
                     __      __ _                _   _____                               \s
                     \\ \\    / /(_)              | | / ____|                              \s
                      \\ \\  / /  _  _ __   _   _ | || |      ___   _ __  _ __    ___  _ __\s
                       \\ \\/ /  | || '_ \\ | | | || || |     / _ \\ | '__|| '_ \\  / _ \\| '__|
                        \\  /   | || | | || |_| || || |____| (_) || |   | | | ||  __/| |  \s
                         \\/    |_||_| |_| \\__, ||_| \\_____|\\___/ |_|   |_| |_| \\___||_|  \s
                                           __/ |                                         \s
                                          |___/                                          \s
                     /*----------------------------------------------------------------------------------*/
                    
                    """;

            for (HtmlElement e : li) {
                HtmlPage htmlPage1 = webClient.getPage(e.click().getUrl());
                String nomArticle = "";
                String ntmp = "";
                String prixArticle = "";
                String description = "";
                String ptmp;
                String lien = String.valueOf(htmlPage1.getUrl());

                List<HtmlElement> nom = htmlPage1.getByXPath("//div[2]/div[2]/p");
                List<HtmlElement> prix = htmlPage1.getByXPath("//div[2]/div[2]/div[4]/div/span/span/span");
                List<HtmlElement> desc = htmlPage1.getByXPath("//div/div/div/div[6]/span");

                for (HtmlElement n : nom) {
                    ntmp = n.getTextContent();
                    ntmp = ntmp.trim();

                    if (ntmp.contains(titre)){
                        nomArticle = ntmp;
                    }
                }
                for (HtmlElement p : prix) {
                    prixArticle = "";

                    ptmp = p.getTextContent();
                    ptmp = ptmp.replace(",", ".");
                    ptmp = ptmp.replace("€", "");
                    ptmp = ptmp.replace("\u00a0", "");
                    if (Double.parseDouble(ptmp) > prixmin && Double.parseDouble(ptmp) < prixmax) {
                        prixArticle = ptmp;
                    }

                }
                for (HtmlElement d : desc) {
                    description = d.getTextContent();
                    description = description.trim();
                    if (description.equals("")) {
                        description = "Aucune description pour cet article.";
                    }
                }
                if (!prixArticle.equals("") && !nomArticle.equals("")) {
                    res += "\nArticle : " + search + "\t " + nomArticle.trim() + "\nPrix : " + prixArticle.trim() + "€\nDescription de l'article : " + description.trim() + "\nlien : " + lien +
                            "\n--------------------------------------------------------------------------------------------\n";
                    aff += res;
                    switch (genre) {
                        case "Rock" -> id_genre = 1;
                        case "Blues" -> id_genre = 2;
                        case "Jazz" -> id_genre = 3;
                        case "Reggae" -> id_genre = 4;
                        case "Funk" -> id_genre = 5;
                        case "Soul" -> id_genre = 6;
                        case "Electro" -> id_genre = 7;
                        default -> id_genre = 0;
                    }
                    resultatScrap resScrap = new resultatScrap(nomArticle,id_genre,dateJ,Double.parseDouble(prixArticle.replace("€","").replace(",","")),description,lien);
                    mainController.getScrapList().add(resScrap);
                }
            }
            return res;
        }

        if (site.equals("leboncoin")) {
            url = "https://leboncoin.fr/recherche?text=" + search + "&price=" + min + "-" + max;
            HtmlPage htmlPage = webClient.getPage(url);

            List<HtmlElement> li = htmlPage.getByXPath("//div[1]/div[1]/p");

            String res = """
                    /*----------------------------------------------------------------------------------*/
                    
                     _            _                                _       \s
                    | |          | |                              (_)      \s
                    | |      ___ | |__    ___   _ __    ___  ___   _  _ __ \s
                    | |     / _ \\| '_ \\  / _ \\ | '_ \\  / __|/ _ \\ | || '_ \\\s
                    | |____|  __/| |_) || (_) || | | || (__| (_) || || | | |
                    |______|\\___||_.__/  \\___/ |_| |_| \\___|\\___/ |_||_| |_|
                                                                           \s
                                                                           \s
                                                                           
                    /*----------------------------------------------------------------------------------*/
                    """;


            for (HtmlElement e : li) {
                HtmlPage htmlPage1 = webClient.getPage(e.click().getUrl());
                String nomArticle = "";
                String ntmp = "";
                String prixArticle = "";
                String description = "";
                String lien = String.valueOf(htmlPage1.getUrl());

                List<HtmlElement> nom = htmlPage1.getByXPath("//div[3]/div/div/h1");
                List<HtmlElement> prix = htmlPage1.getByXPath("//div[3]/div/span//div/div[1]/div/span");
                List<HtmlElement> desc = htmlPage1.getByXPath("//div[5]/div/div/p");

                for (HtmlElement n : nom) {
                    nomArticle = n.getTextContent();
                }
                for (HtmlElement p : prix) {
                    prixArticle = p.getTextContent();
                    prixArticle = prixArticle.replace("\u00a0", "");
                }
                for (HtmlElement d : desc) {
                    description = d.getTextContent();
                    if (description.equals("")) {
                        description = "Aucune description pour cet article.";
                    }
                }
                if (!prixArticle.equals("") && !nomArticle.equals("")) {
                    res += "\nArticle : " + nomArticle + "\nPrix : " + prixArticle + "\nDescription de l'article : " + description + "\nlien : " + lien +
                            "\n--------------------------------------------------------------------------------------------\n";
                    aff += res;
                    switch (genre) {
                        case "Rock" -> id_genre = 1;
                        case "Blues" -> id_genre = 2;
                        case "Jazz" -> id_genre = 3;
                        case "Reggae" -> id_genre = 4;
                        case "Funk" -> id_genre = 5;
                        case "Soul" -> id_genre = 6;
                        case "Electro" -> id_genre = 7;
                        default -> id_genre = 0;
                    }
                    resultatScrap resScrap = new resultatScrap(nomArticle,id_genre,dateJ,Double.parseDouble(prixArticle.replace("€","").replace(",","")),description,lien);
                    mainController.getScrapList().add(resScrap);
                }
            }
            return res;
        }

        if (site.equals("mesvinyles")) {
            url = "https://mesvinyles.fr/en/search?controller=search&s=Linkin+Park";
            HtmlPage htmlPage = webClient.getPage(url);

            List<HtmlElement> li = htmlPage.getByXPath("//div/div[2]/h3/a");
            String res = """
                    /*----------------------------------------------------------------------------------*/
                    
                                        __      __ _                _           \s
                                        \\ \\    / /(_)              | |          \s
                     _ __ ___    ___  ___\\ \\  / /  _  _ __   _   _ | |  ___  ___\s
                    | '_ ` _ \\  / _ \\/ __|\\ \\/ /  | || '_ \\ | | | || | / _ \\/ __|
                    | | | | | ||  __/\\__ \\ \\  /   | || | | || |_| || ||  __/\\__ \\
                    |_| |_| |_| \\___||___/  \\/    |_||_| |_| \\__, ||_| \\___||___/
                                                              __/ |             \s
                                                             |___/              \s
                                                             
                    /*----------------------------------------------------------------------------------*/
                    """;


            for (HtmlElement e : li) {
                HtmlPage htmlPage1 = webClient.getPage(e.click().getUrl());
                String nomArticle = "";
                String ntmp = "";
                String prixArticle = "";
                String description = "";
                String ptmp = "";
                String lien = String.valueOf(htmlPage1.getUrl());

                List<HtmlElement> nom = htmlPage1.getByXPath("//div[1]/div[2]/h1");
                List<HtmlElement> prix = htmlPage1.getByXPath("//div[1]/div[2]/div[2]/div[2]/form/div[2]/div[1]/div/span");
                List<HtmlElement> desc = htmlPage1.getByXPath("/html/body/main/section/div/div/div/section/section/div/div/div[1]/div");

                for (HtmlElement n : nom) {
                    ntmp = n.getTextContent();
                    ntmp = ntmp.trim();

                    if (ntmp.contains(titre)){
                        nomArticle = ntmp;
                    }
                }
                for (HtmlElement p : prix) {
                    ptmp = p.getTextContent();
                    ptmp = ptmp.replace(",", ".");
                    ptmp = ptmp.replace("€", "");
                    ptmp = ptmp.replace("\u00a0", "");

                    if (Double.parseDouble(ptmp) > prixmin && Double.parseDouble(ptmp) < prixmax) {
                        prixArticle = ptmp;
                    }
                }
                for (HtmlElement d : desc) {
                    description = d.getTextContent().trim();
                    if (description.equals("")) {
                        description = "Aucune description pour cet article.";
                    }
                }
                if (!prixArticle.equals("") && !nomArticle.equals("")) {
                    res += "\nArticle : " + nomArticle + "\nPrix : " + prixArticle + "€\nDescription de l'article : " + description + "\nlien : " + lien +
                            "\n--------------------------------------------------------------------------------------------\n";
                    aff += res;
                    switch (genre) {
                        case "Rock" -> id_genre = 1;
                        case "Blues" -> id_genre = 2;
                        case "Jazz" -> id_genre = 3;
                        case "Reggae" -> id_genre = 4;
                        case "Funk" -> id_genre = 5;
                        case "Soul" -> id_genre = 6;
                        case "Electro" -> id_genre = 7;
                        default -> id_genre = 0;
                    }
                    resultatScrap resScrap = new resultatScrap(nomArticle,id_genre,dateJ,Double.parseDouble(prixArticle.replace("€","").replace(",","")),description,lien);
                    mainController.getScrapList().add(resScrap);
                }
            }
            return res;
        }

        if (site.equals("culturefactory")) {
            url = "https://culturefactory.fr/recherche?controller=search&s=" + search;
            HtmlPage htmlPage = webClient.getPage(url);

            List<HtmlElement> li = htmlPage.getByXPath("//article/div[2]/h4/a");
            String res = """
                    /*----------------------------------------------------------------------------------*/
                    
                      _____        _  _                      ______            _                     \s
                     / ____|      | || |                    |  ____|          | |                    \s
                    | |     _   _ | || |_  _   _  _ __  ___ | |__  __ _   ___ | |_  ___   _ __  _   _\s
                    | |    | | | || || __|| | | || '__|/ _ \\|  __|/ _` | / __|| __|/ _ \\ | '__|| | | |
                    | |____| |_| || || |_ | |_| || |  |  __/| |  | (_| || (__ | |_| (_) || |   | |_| |
                     \\_____|\\__,_||_| \\__| \\__,_||_|   \\___||_|   \\__,_| \\___| \\__|\\___/ |_|    \\__, |
                                                                                                 __/ |
                                                                                                |___/\s
                                                                                                
                    /*----------------------------------------------------------------------------------*/
                    """;

            for (HtmlElement e : li) {
                HtmlPage htmlPage1 = webClient.getPage(e.click().getUrl());
                String nomArticle = "";
                String ntmp = "";
                String prixArticle = "";
                String description = "";
                String ptmp = "";
                String lien = String.valueOf(htmlPage1.getUrl());

                List<HtmlElement> nom = htmlPage1.getByXPath("//div[1]/div[2]/h1");
                List<HtmlElement> prix = htmlPage1.getByXPath("//div[1]/div[2]/div/div/section/div[1]/div[2]/div[1]/div[1]/div/span");
                List<HtmlElement> desc = htmlPage1.getByXPath("//div[1]/div[2]/div[2]/div/p[2]");


                for (HtmlElement n : nom) {
                    ntmp = n.getTextContent();
                    ntmp = ntmp.trim();

                    if (ntmp.contains(titre)){
                        nomArticle = ntmp;
                    }
                }
                for (HtmlElement p : prix) {
                    ptmp = p.getTextContent();
                    ptmp = ptmp.replace(",", ".");
                    ptmp = ptmp.replace("€", "");
                    ptmp = ptmp.replace("\u00a0", "");

                    if (Double.parseDouble(ptmp) > prixmin && Double.parseDouble(ptmp) < prixmax) {
                        prixArticle = ptmp;
                    }
                }
                for (HtmlElement d : desc) {
                    description = d.getTextContent().trim();
                    if (description.equals("")) {
                        description = "Aucune description pour cet article.";
                    }
                }
                if (!prixArticle.equals("") && !nomArticle.equals("")) {
                    res += "\nArticle : " + nomArticle + "\nPrix : " + prixArticle + "€\nDescription de l'article : " + description + "\nlien : " + lien +
                            "\n--------------------------------------------------------------------------------------------\n";
                    aff += res;
                    switch (genre) {
                        case "Rock" -> id_genre = 1;
                        case "Blues" -> id_genre = 2;
                        case "Jazz" -> id_genre = 3;
                        case "Reggae" -> id_genre = 4;
                        case "Funk" -> id_genre = 5;
                        case "Soul" -> id_genre = 6;
                        case "Electro" -> id_genre = 7;
                        default -> id_genre = 0;
                    }
                    resultatScrap resScrap = new resultatScrap(nomArticle,id_genre,dateJ,Double.parseDouble(prixArticle.replace("€","").replace(",","")),description,lien);
                    mainController.getScrapList().add(resScrap);
                }
            }

            return res;
        }

        return aff;
    }

    @Override
    public String toString() {
        return "com.example.soutenance2.Scrap{" +
                "titre='" + titre + '\'' +
                ", genre='" + genre + '\'' +
                ", date=" + date +
                ", prixmin=" + prixmin +
                ", prixmax=" + prixmax +
                ", site='" + site + '\'' +
                '}';
    }
}
