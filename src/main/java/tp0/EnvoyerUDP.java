package tp0;

import java.io.IOException;
import java.net.*;

public class EnvoyerUDP {

    public static void main(String[] args) {
        System.out.println("*** Expéditeur UDP ***");
        // Création d'un DatagramPacket pour l'envoi.
        DatagramPacket paquet;
        InetAddress adrDest = null;
        try {
            adrDest = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
            System.exit(1);
        }
        byte[] donnees = "Bonjour !".getBytes();
        paquet = new DatagramPacket(donnees, donnees.length, adrDest, 6000);
        System.out.println("*** Paquet UDP créé ***");

        // Création d'un socket pour expédier le paquet
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("*** Socket créé ***");
        // Envoi du paquet
        try {
            socket.send(paquet);
        } catch (IOException e1) {
            e1.printStackTrace();
            System.exit(1);
        }
        System.out.println("*** Paquet UDP envoyé ***");
        // Fermeture du socket
        socket.close();
        System.out.println("*** Socket fermé ***");

    }
}
