package tp0;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class RecevoirUDP {

    public static void main(String[] args) {
        System.out.println("*** Destinataire UDP ***");
        // Création d'un socket à l'écoute du port 6000
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(6000);
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("*** Socket attaché au port 6000 créé ***");
        // Création d'un DatagramPacket pour la réception
        DatagramPacket paquet = null;
        byte[] buffer = new byte[1000];
        paquet = new DatagramPacket(buffer, buffer.length);
        // Attente et réception d'un paquet UDP.
        System.out.println("*** Attente d'un paquet UDP ***");
        try {
            socket.receive(paquet);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        // Affichage de la chaîne reçue
        String donnees=new String(buffer,0,paquet.getLength());
        System.out.println("Données : "+donnees);
        // Fermeture du socket
        socket.close();
        System.out.println("*** Socket fermé ***");
    }
}
