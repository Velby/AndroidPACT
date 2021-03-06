package com.example.paquetcoachapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
//import java.util.regex.*;
import java.util.ArrayList;

public class LienServeurWeb {
	
	

	
	static BigInteger Encrypt;
    static BigInteger NDecrypt;
    static BigInteger Decrypt;
    static BigInteger NEncrypt;
	
	
	public static boolean creationPossiblePseudo(String pseudo,String IP){
		String url = "http://" + IP + "/premiertest/presence.php?user=" + pseudo;
		String bool = getCode(url).get(0);
		if(bool.equals(0)){
			return true;
		}
		else{
			return false;
		}
	}
	
	 public static void getCles(String User, String IP){//on s'en sert qu'une seule fois
         BigInteger[] cles = new BigInteger[2];
         String urlCle = "http://" + IP + "/secu/getCle.php?user=" + User;
         String eatn = (LienServeurWeb.getCode(urlCle)).get(0);
         System.out.println(urlCle);
         BigInteger e = null;
         BigInteger n = null;
         int i =0;
         String lettre="";
         boolean x = true;
                 while(x){
                         lettre = eatn.substring(i,i+1);
                         if (lettre.equals("@")){
                                 //System.out.println(i);
                                 x=false;
                                 String part1 = eatn.substring(0, i);
                                 Encrypt = new BigInteger(part1);
                        // System.out.println(e);
                                 int fin = eatn.length();
                                 String part2 = eatn.substring(i+1, fin-1);
                                 NEncrypt = new BigInteger(part2);
                         //        System.out.println(n);
                         }
                         else {
                                 i++;
                         }

         }
         System.out.println(Encrypt);
         System.out.println(NEncrypt);

 }
	
	public static String setCles(String user, String IP){ // on s'en sert qu'une seule fois
        GenerateRSAKeys Claves = new GenerateRSAKeys(31);
BigInteger e = Claves.getE();
NDecrypt = Claves.getN();
Decrypt = Claves.getD();
        String url = "http://" + IP + "/secu/setCle.php?user="+user+"&CleCryptage=" + e + "&NCryptage=" + NDecrypt;
        System.out.println(url);
        return url; // a mettre dans un web view
}
	
	
	public static String nouvelUser(String user, String IP){// a lancer a la cr�ation d'un nouvel user
        setCles(user, IP);
        getCles(user,IP);
 String url = "http://" + IP + "/premiertest/nouvelUser.php?user=" + user;
 return url; // A Mettre dans un webview
}
	
	
	public static ArrayList<String> getCigarette(String User,String cible, String IP){
        String contenuClair= "user=" + User;
        String contenuCode= "cible="+RSA.encryptString(cible, Encrypt, NEncrypt);
        String url = "http://"+IP+"/premiertest/getCigarette.php?" + contenuClair+"&"+contenuCode;
        System.out.println(url);
        return getCode(url,Decrypt,NDecrypt);
}

	
	public static String nouveauMessage(String Envoyeur, String Destinataire, String Message, String Date, String IP) {

        BigInteger[] cles = new BigInteger[2];
        String contenuClair= Destinataire + "@" + Message + "@" + Date +"@";
        String contenuCode= "user="+Envoyeur+ "&info=" +RSA.encryptString(contenuClair, Encrypt, NEncrypt);

        String url = "http://"+IP+"/premiertest/message.php?" + contenuCode;
        System.out.println(url);
       return url; // a mettre dans webview
}
	
	public static String nouvelleCigarette(String User, String Date, String NbCigarettes, String IP){

        BigInteger[] cles = new BigInteger[2];
        String contenuClair= "info=(" + User + "@" + Date + "@" + NbCigarettes + "@)";
        String contenuCode= RSA.encryptString(contenuClair, Encrypt, NEncrypt);

        String url = "http://"+IP+"/premiertest/cigarette.php?" + contenuCode;
        return url; // a mettre dans webview
}
	
	  public static ArrayList<String> messageRecu(String user,String IP){
          String contenuClair = "user="+user;
          String url="http://"+IP+"/premiertest/getMessageRecu.php?" + contenuClair;
          System.out.println(url);
          return getCode(url,Decrypt,NDecrypt);
  }
	  
	  public static ArrayList<String> messageEnvoyee(String user,String IP){
          String contenuClair = "user="+user;
          String url="http://"+IP+"/premiertest/getMessageEnvoyee.php?" + contenuClair;
          System.out.println(url);
          return getCode(url,Decrypt,NDecrypt);
  }

      public static ArrayList<String> getCode(String url, BigInteger d,BigInteger n) {
          String code = "";

          if (urlExists(url)) {
                  BufferedReader in = null;

                  try {
                          URL site = new URL(url);
                          in = new BufferedReader(
                                          new InputStreamReader(site.openStream()));

                          String inputLine;
                          while ((inputLine = in.readLine()) != null) {
                                  code = code + "\n" + (inputLine);
                          }

                          in.close();

                  } catch (IOException ex) {
                          System.out.println("Erreur dans l'ouverture de l'URL : " + ex);
                  } finally {
                          try {
                                  in.close();
                          } catch (IOException ex) {
                                  System.out.println("Erreur dans la fermeture du buffer : "
                                                  + ex);
                          }
                  }
          } else {
                  System.out.println("Le site n'existe pas !");
          }
          // return code;
          ArrayList<String> resultat = extraireInfo(code,d,n);
          return resultat;
  }
	
      public static ArrayList<String> getCode(String url) {
          String code = "";

          if (urlExists(url)) {
                  BufferedReader in = null;

                  try {
                          URL site = new URL(url);
                          in = new BufferedReader(
                                          new InputStreamReader(site.openStream()));

                          String inputLine;
                          while ((inputLine = in.readLine()) != null) {
                                  code = code + "\n" + (inputLine);
                          }

                          in.close();

                  } catch (IOException ex) {
                          System.out.println("Erreur dans l'ouverture de l'URL : " + ex);
                  } finally {
                          try {
                                  in.close();
                          } catch (IOException ex) {
                                  System.out.println("Erreur dans la fermeture du buffer : "
                                                  + ex);
                          }
                  }
          } else {
                  System.out.println("Le site n'existe pas !");
          }
          // return code;
          ArrayList<String> resultat = extraireInfo(code);
          return resultat;
  }

      public static boolean urlExists(String url) {
          try {
                  URL site = new URL(url);
                  try {
                          site.openStream();
                          return true;
                  } catch (IOException ex) {
                          return false;
                  }
          } catch (MalformedURLException ex) {
                  return false;
          }
  }
      
      
      public static ArrayList<String> extraireInfo(String s, BigInteger d, BigInteger n) {//Ca marche!

          ArrayList<String> data = new ArrayList();
          int i = 0; Character c; String info="";
          while (i<s.length()){
                  Buffer buffer1 = new Buffer(6);
                  while (buffer1.equal("<info>")&&i<s.length()) //boucle qui lit tout le fichier jusqu'a trouver <info>
                  {
                          c = s.charAt(i);
                          buffer1.add(c);
                          i++;
                  }
                  Buffer buffer2 = new Buffer(7);
                  while (buffer2.equal("</info>)")&&i<s.length()){ //boucle qui lit le fichier jusqu'a ce qu'il trouve </info>
                          c=s.charAt(i);
                          buffer2.add(c);
                          info=info+c;
                          i++;
                  }
                  if (info.length()>buffer2.size){
                  info = info.substring(0, info.length()-buffer2.size);

                  data.add(info);//on ajoute a data le contenu de la balise info courante
                  }
                  info="";
          }
          return data; //ArrayList contenant les infos demand�s non d�crypt�

  }

      public static ArrayList<String> extraireInfo(String s) {//Ca marche!

          ArrayList<String> data = new ArrayList();
          int i = 0; Character c; String info="";
          while (i<s.length()){
                  Buffer buffer1 = new Buffer(6);
                  while (buffer1.equal("<info>")&&i<s.length()) //boucle qui lit tout le fichier jusqu'a trouver <info>
                  {
                          c = s.charAt(i);
                          buffer1.add(c);
                          i++;
                  }
                  Buffer buffer2 = new Buffer(7);
                  while (buffer2.equal("</info>)")&&i<s.length()){ //boucle qui lit le fichier jusqu'a ce qu'il trouve </info>
                          c=s.charAt(i);
                          buffer2.add(c);
                          info=info+c;
                          i++;
                  }
                  if (info.length()>buffer2.size){
                  info = info.substring(0, info.length()-buffer2.size);

                  data.add(info);//on ajoute a data le contenu de la balise info courante
                  }
                  info="";
          }
          return data; //ArrayList contenant les infos demand�s non d�crypt�

  }
    
    public static String[] enleverArobasesString(String valeur){
    	String[] resultat = new String[3];
    	int[] positionarobases = new int[3];
    	
    	int i =0;
		String lettre="";
		int compteur = 0;
		while(compteur!=3){
			lettre = valeur.substring(i,i+1);
			if (lettre.equals("@")){
				positionarobases[compteur] =i;
				compteur ++;
				i++;
			}
			else {
				i++;
			}
    	
    	
    }
		
		
		resultat[0]=valeur.substring(positionarobases[0]+1,positionarobases[1]);
		//System.out.println(resultat[0]);
		resultat[1]=valeur.substring(positionarobases[1]+1,positionarobases[2]);
		//System.out.println(resultat[1]);
		resultat[2]=valeur.substring(positionarobases[2]+1,valeur.length());
		//System.out.println(resultat[2]);
    	return resultat;
    	}
    
    public static ArrayList<String> enleverArobasesArrayList(ArrayList<String> tableau){
    	int longueur = tableau.size();
    	ArrayList<String> resultat = new ArrayList<String>();
    	String[] sansarobase = new String[3];
    	String text ;
    	int i =0;
    	for(i=0;i<longueur;i++){
    		text=tableau.get(i);
    		sansarobase=enleverArobasesString(text);
    		
    		
    		/*System.out.println(textearobase[0]);
    		
    		System.out.println(textearobase[1]);
    		
    		System.out.println(textearobase[2]);*/
    		
    		resultat.add(3*i,sansarobase[0]);
    		resultat.add(3*i+1,sansarobase[1]);
    		resultat.add(3*i+2,sansarobase[2]);
    	}
    	/*int h=0;
    	for(h=0;h<resultat.size();h++){
    		System.out.println(resultat.get(h));
    	}*/
    	return resultat;
    }
    
    public static ArrayList<String> triConversation(ArrayList<String> envoye,ArrayList<String> recu){
    	
    	ArrayList<String> resultat = new ArrayList<String>();
    	ArrayList<String> resultatenvoye = new ArrayList<String>();
    	ArrayList<String> resultatrecu = new ArrayList<String>();
    	ArrayList<String> concatene = new ArrayList<String>();
    	resultatenvoye=enleverArobasesArrayList(envoye);
    	resultatrecu=enleverArobasesArrayList(recu);
    	int f =0;
    	for(f=0;f<resultatenvoye.size();f++){
    		concatene.add(resultatenvoye.get(f));
    	}
    	int h =0;
    	for(h=0;h<resultatrecu.size();h++){
    		concatene.add(resultatrecu.get(h));
    	}
    	
    	
    	
    	int compteur = 0;
    	for(compteur=0;compteur<(concatene.size()/3);compteur++){
    	int nbmsg = concatene.size()/3;
    	String pivot=concatene.get(2);
    	
    	Long pivott = Long.parseLong(pivot);
    	int positionpetit =0;
    	int l=0;
    	for(l=0;l<nbmsg;l++){
    		String comparestring = concatene.get(3*l+2);
    		long compareint = Long.parseLong(comparestring);
    		if(compareint<pivott){
    			pivott=compareint;
    			positionpetit=l;    			
    		}
    		
    		
    	}
    	resultat.add(concatene.get(3*positionpetit));
		resultat.add(concatene.get(3*positionpetit+1));
		resultat.add(concatene.get(3*positionpetit+2));
		concatene.remove(3*positionpetit);
		concatene.remove(3*positionpetit);
		concatene.remove(3*positionpetit);
		//System.out.println("resultat = " + resultat.size());
		//System.out.println("concatene = " + concatene.size());
    
    	
    	}
    	resultat.add(concatene.get(0));
		resultat.add(concatene.get(1));
		resultat.add(concatene.get(2));
    	
    	return resultat;
    }	
    
    	public static ArrayList<String> triDestinataire(ArrayList<String> envoye,ArrayList<String> recudedestinataire,String destinataire){
    		ArrayList<String> resultat = new ArrayList<String>();
    		ArrayList<String> envoyeadestinataire = new ArrayList<String>();
    		ArrayList<String> envoyesansarobases = new ArrayList<String>();
    		envoyesansarobases=enleverArobasesArrayList(envoyesansarobases);
    		int i=0;
    		for(i=0;i<envoyesansarobases.size();i++){
    			if(envoyesansarobases.get(3*i+1).equals(destinataire)){
    				envoyeadestinataire.add(envoyesansarobases.get(3*i));
    				envoyeadestinataire.add(envoyesansarobases.get(3*i+1));
    				envoyeadestinataire.add(envoyesansarobases.get(3*i)+2);
    				
    				
    			}
    		}
    		resultat=triConversation(recudedestinataire,envoyeadestinataire);
    		
    		
    		return resultat;
    	}
    	
    	public static String conversationBalisee(ArrayList<String> envoye,ArrayList<String> recudedestinataire,String destinataire){
    		ArrayList<String> conversation = new ArrayList<String>();
    		conversation = triDestinataire(envoye, recudedestinataire, destinataire);
    		String resultat = "";
    		int nbmsg = conversation.size();
    		int i=0;
    		for(i=0;i<nbmsg;i++){
    			String datestringnonformatee = conversation.get(3*i+2);
    			long datelongnonformatee = Long.parseLong(datestringnonformatee);
    			String datestringformatee = formatdate(datelongnonformatee);
    			resultat = resultat + "<br><u>" + conversation.get(3*i+1) + "("+datestringformatee+")"+"</u></br>" + "<br>"+conversation.get(3*i+1)+"</br>"+ "<br><br/>";
    		}
    		
    		return resultat;
    	}
    	
    	public static String formatdate(long datenonformatee){
    		String datenon = String.valueOf(datenonformatee) ;
    		String date = datenon.substring(4, 6)+"/"+datenon.substring(2, 4)+"/"+datenon.substring(0, 2)+" � "+datenon.substring(6, 8)+"h"+datenon.substring(8, 10);
    	return date;
    	}
    	
    	 public static String replaceSpace(String url){
             String replaced = "";
             for (int i =0;i<url.length();i++){
                     Character ci= new Character(url.charAt(i));
                     if (ci.equals(' ')){
                             replaced=replaced+"%20";
                     }
                     else {replaced=replaced+ci;}
             }
             System.out.println("lol="+replaced);
             return replaced;
     }
}
