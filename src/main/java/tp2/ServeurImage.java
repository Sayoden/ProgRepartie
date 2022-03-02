package tp2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.crypto.Data;


public class ServeurImage extends JFrame {


    // le socket permettant la réception des requêtes des clients
    private DatagramSocket socketReceptionReq;
    // le socket permettant l'attente d'une connexion pour le transfert d'un historique vers un client
    private ServerSocket socketTransfertHistorique;
    // un tableau pour stocker l'historique des requêtes
    private String historiqueReq[];
    // le nombre des requêtes dans l'historique
    private int nbReqHistorique;

    /* (1) initSockets
     *
     * Méthode permettant d'initialiser (et uniquement d'intialiser) les quatres attributs précédents :
     * socketReceptionReq et socketTransfertHistorique par des sockets associés aux numéros de port
     * donnés en paramètres, historiqueReq un tableau permettant de contenir l'historique des requêtes
     * reçues et nbReqHistorique le nombre de requêtes reçues.
     *
     * Parmaètres :
     *
     * portReceptionReq est le port sur lequel seront envoyés les paquets UDP correspondant aux requêtes
     * des clients (pour le socket socketReceptionReq).
     *
     * portTransfertHistorique  (pour le socket socketTransfertHistorique) est le port sur lequel sera à l'écoute le serveur pour établir de nouvelles
     * connexions TCP pour envoyer les historiques aux clients.
     *
     */

    private void initSockets(int portReceptionReq,int portTransfertHistorique){
        this.historiqueReq = new String[1000];
        this.nbReqHistorique = 0;
        try {
            this.socketReceptionReq = new DatagramSocket(portReceptionReq);
            this.socketTransfertHistorique = new ServerSocket(portTransfertHistorique);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("*** Problème lors de l'initialisation ***");
        }
        System.out.println("*** Initialisation terminée ***");
    }

    /* (2) attenteEtReceptionRequeteClient
     * Méthode permettant d'attendre et de réceptionner une requête envoyée par un client
     * (un paquet UDP attendu et réceptionné par socketReceptionReq).
     * La méthode devra retourner la chaîne de caractères correspondant à la requête du client.
     *
     */

    private String attenteEtReceptionRequeteClient(){
        String requeteClient=null;
        byte[] data = new byte[1000];

        DatagramPacket paquet = null;

        paquet = new DatagramPacket(data, data.length);

        System.out.println("*** Attente requête client ***");

        try {
            this.socketReceptionReq.receive(paquet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        requeteClient = new String(paquet.getData(), 0, paquet.getLength());

        System.out.println("*** Message reçu: " + requeteClient + " ***");

        if (this.nbReqHistorique < 1000) {
            this.historiqueReq[this.nbReqHistorique++] = requeteClient;
        }

        return requeteClient;
    }

    /*
     * (4) analyseEtExecutionRequete
     * Méthode permettant d'analyser une requête client et de lancer l'opération
     * correspondante (dessin d'un rectangle ou d'un cercle, transfert de l'image).
     *
     */

    private void analyseEtExecutionRequete(String requeteClient){
        String[] decompositionReq =requeteClient.split(" ");
        int x1, x2, x3, x4, rayon;
        String couleur;
        /* Requête pour transférer l'image au client */
        if ((decompositionReq.length==1) && (decompositionReq[0].equals("HISTORIQUE"))){
            envoiHistorique();
        }

        /* Requête pour dessiner un disque */

        // A Compléter
        if((decompositionReq.length == 5) && (decompositionReq[0].equals("DISQUE"))){
            x1 = Integer.parseInt(decompositionReq[1]);
            x2 = Integer.parseInt(decompositionReq[2]);
            rayon = Integer.parseInt(decompositionReq[3]);
            couleur = decompositionReq[4];
            dessineDisque(x1,x2,rayon,couleur);
        }
        /* Requête pour dessiner un rectangle */

        // A Compléter
        if((decompositionReq.length == 6) && (decompositionReq[0].equals("RECTANGLE"))){
            x1 = Integer.parseInt(decompositionReq[1]);
            x2 = Integer.parseInt(decompositionReq[2]);
            x3 = Integer.parseInt(decompositionReq[3]);
            x4 = Integer.parseInt(decompositionReq[4]);
            couleur = decompositionReq[5];
            dessineRectangle(x1,x2,x3,x4,couleur);
        }
    }

    /*
     * (5) analyseEtExecutionRequete
     * Méthode permettant de réaliser l'envoi de l'historique à un client.
     *
     */

    private void envoiHistorique(){
        Socket socketAvecClient=null;
        PrintWriter fluxSortant=null;

        try {
            socketAvecClient = this.socketTransfertHistorique.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fluxSortant = fluxSortant(socketAvecClient);

        for (int i = 0; i < this.nbReqHistorique; i++) {
            fluxSortant.println(this.historiqueReq[i]);
        }

        System.out.println("*** Fin de transmission du flux ***");

        fluxSortant.flush();

        try {
            socketAvecClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // Vous ne devez en aucune manière modifier les attributs/constructeurs/méthodes/classes qui suivent !
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public void traitementPrincipal(){
        String requeteClient;
        while(true){
            requeteClient=attenteEtReceptionRequeteClient();
            //if (requeteClient!=null) System.out.println("Rquête reçue : "+requeteClient);
            if (requeteClient!=null) analyseEtExecutionRequete(requeteClient);
        }

    }

    public static void main(String[] args) {
        ServeurImage serveur=new ServeurImage();
        serveur.traitementPrincipal();

    }
    private static final long serialVersionUID = 1L;
    private BufferedImage image;
    private Graphics2D g;

    public ServeurImage(){
        super("Serveur Image");
        image = new BufferedImage(350, 350, BufferedImage.TYPE_INT_RGB);
        g=(Graphics2D)image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        add(new MyJPanel(image.getWidth(),image.getHeight()),BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false);
        setVisible(true);
        initSockets(2000,2001);

    }

    PrintWriter fluxSortant(Socket socket){
        OutputStream outputStream;
        PrintWriter fluxSortant=null;
        try{
            outputStream=socket.getOutputStream();
            fluxSortant=new PrintWriter(outputStream);
        }catch(IOException e ){
            System.err.println( "Problème lors de la création du flux sortant\n");
            System.exit(1);
        }
        return fluxSortant;

    }

    private void changeCouleur(String couleur){
        if (couleur.equals("VERT")) g.setColor(Color.green);
        else if (couleur.equals("BLEU")) g.setColor(Color.blue);
        else if (couleur.equals("ROUGE")) g.setColor(Color.red);
        else if (couleur.equals("JAUNE")) g.setColor(Color.yellow);
        else g.setColor(Color.white);

    }

    void dessineDisque(int centreX,int centreY,int rayon,String couleur){
        changeCouleur(couleur);
        g.fillOval(centreX,centreY,rayon,rayon);
        repaint();

    }

    void dessineRectangle(int coinX,int coinY,int largeur,int hauteur,String couleur){
        changeCouleur(couleur);
        g.fillRect(coinX,coinY,largeur,hauteur);
        repaint();
    }

    private class MyJPanel extends JPanel{
        private static final long serialVersionUID = 1L;
        public MyJPanel(int largeur,int hauteur){
            super();
            setPreferredSize(new Dimension(largeur,hauteur));
        }
        public void paintComponent(Graphics g) {
            g.drawImage(image, 0, 0, null);
        }
    }
}

