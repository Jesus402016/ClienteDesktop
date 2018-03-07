/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.controlador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import ufps.client.chat;
import ufps.gui.JFchat;

/**
 *
 * @author Jesus
 */
public class controlador extends Thread {
   private chat chat;
   private String nombre="";
   private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
   private boolean activo=true;
   private JFchat ventana;
 
   
 public controlador(String nombre,JFchat ventana){
   this.chat=new chat(nombre,this);
   this.ventana=ventana;
 }

    public controlador() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
   @Override
 public void run(){
      
          while(activo){ 
              System.out.print("");
              if (this.chat.getContectados()) {
                  
              }
        }
 }
 
//Metodo para enviar Mensaje al destino 
 public void enviarMensaje(String mensaje,String nombre ){
     this.setNombre(nombre);
     
       try {
           this.chat.enviarMensaje(mensaje, nombre);
       } catch (IOException ex) {
          System.out.println("Error al enviar mensaje");
       }
     
 }
 
 public void enviarMensajeTodos(String mensaje){
      try {
           this.chat.EnviaraTodos(mensaje);
       } catch (IOException ex) {
          System.out.println("Error al enviar mensaje a Todos");
       }
 }
 
 public void recibirMensaje(String []list){
     String cad="";
     cad=list[1]+":"+""+list[3];
     this.ventana.ingresarMensaje(cad);
 }
 
 //Metodo que muestra los conectados aFrame
 public void conectados(String cad){

     this.ventana.mostraConectados(cad);
 }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
 
 
    
 
 
 
}
