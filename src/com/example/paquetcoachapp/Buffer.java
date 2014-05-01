package com.example.paquetcoachapp;

public class Buffer {
	
	 Character[] buffer;
     int size;
     Buffer(int size){ //création d'une file on met les caracteres au 5 et on les degages au 0;
             buffer = new Character[size];
             for (int i = 0;i<size;i++)
             {buffer[i] = '-';}
             this.size=size;
     }

     public void add(char c){//ajoute une lettre a la fin de la file

             for (int j = 0; j<size -1; j++){
                     buffer[j]=buffer[j+1];
             }
             buffer[size-1]= c;
             //for (int x = 0; x<size;x++){System.out.println(buffer[x]);} //ligne affichage de buffer A EFFACER
     }

     public boolean equal(String code){ //test l'egalité du buffer avec code
             Character[] codeChar = new Character[size];
             int i;
             
             for(int j=0;j<size;j++){  // Copie code dans un tableau de Character
                     codeChar[j]=code.charAt(j);
             }

             for(i=0;i<size;i++){
                     if(buffer[i]!=codeChar[i]){
                             return true;}
             }
             return false;
     }


}
