package com.example.soutenance2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class enregistrementBDD {
    private static final String SQL_INSERT = "INSERT INTO recherche(Titre,Description,Prix,id_genre,site) VALUES (?,?,?,?,?)";

    public static void saveBdd(String titre, String description, double prix, int genre, String site) throws SQLException, IOException {
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

        try (Connection conn = DriverManager.getConnection(
//                "jdbc:mysql://109.234.161.28:3308/gretaxao_francois-leplain?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC","gretaxao_francois-leplain","FLeplain2022!");
//                "jdbc:mysql://127.0.0.1:3308/musique?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC","root","");
                "jdbc:mysql://" + nomServeur +":" + port + "/" + nomBdd + "?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC",user,mdp);

             PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {
            preparedStatement.setString(1, titre);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, prix);
            preparedStatement.setInt(4, genre);
            preparedStatement.setString(5, site);
            int row = preparedStatement.executeUpdate();
            System.out.println(row);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}