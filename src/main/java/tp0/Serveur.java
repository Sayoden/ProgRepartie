package tp0;

import java.io.*;
import java.net.*;

public class Serveur {

    public static void main(String[] args) {
        server();
    }

    public static void server() {
        System.out.println("*** Serveur ***");
// Enregistrement du service sur le port 6000
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(6000);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        // caca droulle
        System.out.println("*** Serveur à l'écoute du port 6000 ***");
        Socket socket = null;
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("*** Connexion établie ! ***");
        // Préparation des flux d'entrée/de sortie
        InputStream input = null;
        try {
            input = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        BufferedReader entree = new BufferedReader(new
                InputStreamReader(input));
        OutputStream output = null;
        try {
            output = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        PrintWriter sortie = new PrintWriter(output);
// Echange de données
        sortie.println("Bonjour, je suis le serveur !");
        sortie.flush();
        try {
            System.out.println(entree.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Fermeture de la connexion
        sortie.close();
        try {
            entree.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}