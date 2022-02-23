package tp1;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class DialogueUDP {

    static final private Scanner scanner = new Scanner(System.in);

    private String nomDestinataire;

    private int portDestinataire;

    private InetAddress adresseDestinataire;

    private String nomSource;

    private DatagramSocket socketReception;

    public DialogueUDP(String monNomUtilisateur, int monPort, InetAddress adresseDest, int portDest) {
        this.nomSource = monNomUtilisateur;
        this.adresseDestinataire = adresseDest;
        this.portDestinataire = portDest;

        try {
            this.socketReception = new DatagramSocket(monPort);
        } catch (SocketException e) {
            e.printStackTrace();
            System.err.println("Problème lors de la création du datagramSocket socketReception");
            System.exit(1);
        }

        System.out.println("Dialogue UDP créé!");
    }

    public void receptionMessage(boolean premierReception) {
        byte[] buffer = new byte[1000];
        DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
        try {
            this.socketReception.receive(paquet);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Problème lors de la réception d'un datagramme UDP");
            System.exit(1);
        }

        String message = null;
        message = new String(paquet.getData(), 0, paquet.getLength());

        System.out.println(nomDestinataire + " > " + message);
    }

    public void envoiMessage(boolean premierEnvoi) {
        String message = null;
        System.out.println(this.nomSource + " > ");
        if (premierEnvoi) {
            message = this.nomSource;
            System.out.println(message);
        } else {
            message = scanner.nextLine();
        }

        DatagramPacket paquet = null;
        paquet = new DatagramPacket(message.getBytes(), message.getBytes().length, this.adresseDestinataire, this.portDestinataire);
        try {
            this.socketReception.send(paquet);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Problème lors de l'envoi d'un datagramme UDP");
            System.exit(1);
        }
    }

    public void dialogue(boolean premier) {
        if (premier) {
            envoiMessage(true);
            receptionMessage(true);
        } else {
            receptionMessage(false);
            envoiMessage(false);
        }

        while (true) {
            if (premier) {
                envoiMessage(false);
                receptionMessage(false);
            } else {
                receptionMessage(false);
                envoiMessage(false);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 5) {
            System.err.println("Le programme nécessite 5 arguments");
            System.exit(1);
        }

        String monNom = args[0];
        int monPort = Integer.parseInt(args[1]);
        InetAddress adresseAutre = null;

        try {
            adresseAutre = InetAddress.getByName(args[2]);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.err.println("Problème avec cette adresse ip: " + args[2]);
            System.exit(1);
        }

        int portAutre = Integer.parseInt(args[3]);
        String quiCommence = args[4];

        DialogueUDP dialogueUDP = new DialogueUDP(monNom, monPort, adresseAutre, portAutre);
        System.out.println("Mon Nom: " + monNom);
        System.out.println("*** Dialogue ***");

        dialogueUDP.dialogue(quiCommence.equals("premier"));

    }

}
