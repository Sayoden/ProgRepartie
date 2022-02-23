package tp0;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {
        client();
    }

    public static void client() {
        System.out.println("*** Client ***");
// Demande de connexion et connexion auprès du serveur
        System.out.println("*** Connexion au serveur ... ***");
        Socket socket = null;
        InetAddress adresseServeur = null;
        try {
            adresseServeur = InetAddress.getByName("192.168.167.90");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            socket = new Socket(adresseServeur, 6000);
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
        sortie.println("Bonjour, je suis le client !");
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
