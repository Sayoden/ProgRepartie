package tp2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class ClientImage extends JFrame {

    /*
     *  (3) envoiRequete
     *  Méthode permettant d'envoyer une requête (une chaîne de caractères) au serveur
     *  au travers d'un paquet UDP.
     *  Le numéro port et l'adresse du serveur sont donnés en paramètres ainsi que la requête à envoyer.
     *
     */

    private void envoiRequete(String requete,InetAddress adresseServeur,int portServeur){
        byte[] buffer;
        DatagramPacket paquet;
        DatagramSocket socketEnvoi = null;

        buffer = requete.getBytes();
        paquet = new DatagramPacket(buffer, buffer.length, adresseServeur, portServeur);

        try {
            socketEnvoi = new DatagramSocket();
            socketEnvoi.send(paquet);
            socketEnvoi.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     *  (6) receptionHistorique
     *  Méthode permettant d'envoyer une requête (une chaîne de caractères) au serveur
     *  au travers d'un paquet UDP.
     *  Le numéro port et l'adresse du serveur sont donnés en paramètres ainsi que la requête à envoyer.
     *
     */

    private void receptionHistorique(int portServeurTransfertImage,InetAddress adresseServeur){
        Socket socketAvecServeur=null;
        BufferedReader fluxEntrant=null;
        // A Compléter
        System.out.println("*** Demande de connexion au serveur ***");
        try{
            socketAvecServeur = new Socket(adresseServeur,portServeurTransfertImage);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problème lors d'une demande de connexion !");
            System.exit(1);
        }
        System.out.println("*** Connexion établie ***");
        fluxEntrant = fluxEntrant(socketAvecServeur);
        String requete = null;
        do{
            try{
                requete = fluxEntrant.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Problème");
                System.exit(1);            }
            System.out.println(requete);
            afficheDansHistorique(requete);
        }while (!requete.contentEquals("FIN"));

        try{
            socketAvecServeur.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Problème à la fermeture");
            System.exit(1);
        }
        System.out.println("*** Fin de la réception de l'historique ***");
    }

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // Vous ne devez en aucune manière modifier les attributs/constructeurs/méthodes/classes qui suivent !
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!



    private InetAddress adresseServeur;
    private int portServeurReq;
    private int portServeurTransfertImage;




    private BufferedReader fluxEntrant(Socket socket){
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader fluxEntrant=null;
        try{
            inputStream=socket.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            fluxEntrant=new BufferedReader(inputStreamReader);
        }catch(IOException e ){
            System.err.println( "Problème lors de la création du flux entrant\n");
            System.exit(1);
        }
        return fluxEntrant;
    }

    private JTextArea texteHistorique;
    private JTextField texteMessage;

    public ClientImage(){
        super();
        setTitle("ClientImage");
        setSize(600,400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setBackground(Color.ORANGE);
        setContentPane(panel);
        texteHistorique= new JTextArea(20,40);
        JScrollPane scrollPane = new JScrollPane(texteHistorique);
        texteHistorique.setEditable(false);
        texteHistorique.append("Historique des requêtes reçues par le serveur :\n");

        texteMessage=new JTextField(25);
        texteMessage.addKeyListener(new TexteMessageListener(this,texteMessage));
        panel.add(scrollPane);
        panel.add(texteMessage);
        setVisible(true);
        try{
            byte adrSer[]={127,0,0,1};
            adresseServeur= InetAddress.getByAddress(adrSer);
            portServeurReq=2000;
            portServeurTransfertImage=2001;
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("Problème d'initialisation ClientImage");
            System.exit(1);
        }
        //initParametres(nomUsager,portUsager,adresseApplicationCentrale,portApplicationCentrale);
        //envoiMessage(">"+nomUsager+":"+portUsager);
    }



    private void afficheDansHistorique(String s){
        texteHistorique.append("\n"+s);
        texteHistorique.setCaretPosition(texteHistorique.getText().length());
    }

    public static void main(String[] args) {


        ClientImage application=new ClientImage();

    }


    private class TexteMessageListener implements KeyListener {
        private ClientImage applicationUsager;
        private JTextField texteMessage;

        public TexteMessageListener(ClientImage applicationUsager,JTextField texteMessage){
            this.applicationUsager=applicationUsager;
            this.texteMessage=texteMessage;
        }
        public void keyTyped(KeyEvent e) {
        }
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                applicationUsager.envoiRequete(texteMessage.getText(),adresseServeur,portServeurReq);
                if ((texteMessage.getText()).equals("HISTORIQUE"))
                    receptionHistorique(portServeurTransfertImage,adresseServeur);
		        	 /*
		        	 System.out.println(applicationUsager.getNomUsager()+">"+texteMessage.getText());
		        	 texteMessage.setText("");
		        	 */
            }
        }
        public void keyReleased(KeyEvent e) {
        }
    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;



}
