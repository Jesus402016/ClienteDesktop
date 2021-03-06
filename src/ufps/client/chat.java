/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import ufps.cliente.DTO.usuario;
import ufps.controlador.controlador;

/**
 *
 * @author Jesus
 */
public class chat extends Thread {
    
    private usuario usuario;
    private String host="127.0.0.1";
    private int puerto=3000;
    private boolean activo;
    private int conectados;
    private controlador cont;

   
    
    
    public chat(String nombre,controlador con){
       this.usuario=new usuario(nombre);
       this.cont=con;
       this.start();
    }
    
       
    
    @Override
    public void run(){
        try {
            usuario.setSocket(new Socket(host,puerto));
            this.usuario.setEnviar(new ObjectOutputStream(usuario.getSocket().getOutputStream()));
            this.usuario.setRecibir(new ObjectInputStream(usuario.getSocket().getInputStream()));
            this.enviarSolicitud();
            this.escuchar();
        } catch (IOException ex) {
            System.out.print(ex);
        }
    }
    
    //metodo encargado enviar solicitud
    public void enviarSolicitud() throws IOException{
        String usuario=this.usuario.getNombre();
        String []list=new String[2];
        list[0]="CONEXION";
        list[1]=this.usuario.getNombre();
        this.usuario.getEnviar().writeObject(list);
        
    }
    
    public void crearUsuario(String nombre) throws IOException, ClassNotFoundException{
        String list[]=new String[1];
        list[0]="CONECTADOS";
        this.usuario.getEnviar().writeObject(host);
        Object o= this.usuario.getRecibir().readObject();
        String []li=(String[])o;
        for (int i = 0; i <li.length; i++) {
            if(li[i].equals(nombre)){
                System.out.print("no se puede crear usuario de dos nombres");
            }
        }
    }
    //Enviar Mensaje solo usuario
    public void enviarMensaje(String mensaje,String destino) throws IOException{
            String[] c = new String[4];
            c[0] = "MENSAJE";
            c[1] = this.usuario.getNombre();
            c[2]=destino;
            c[3]=mensaje;
            this.usuario.getEnviar().writeObject(c);
        
    }
    
     //Enviar Mensaje solo usuario
    public void enviarMensajePriv(String mensaje,String destino) throws IOException{
            String[] c = new String[4];
            c[0] = "MENSAJEPRIVADO";
            c[1] = this.usuario.getNombre();
            c[2]=destino;
            c[3]=mensaje;
            this.usuario.getEnviar().writeObject(c);
        
    }
    
    public void EnviaraTodos(String mensaje) throws IOException{
          String[] c = new String[4];
            c[0] = "MENSAJETODOS";
            c[1] = this.usuario.getNombre();
            c[3] = mensaje;
            this.usuario.getEnviar().writeObject(c);
    }
    
    public void escuchar(){
        activo=true;
        while(activo){
            try {
                Object aux=this.usuario.getRecibir().readObject();
                String list[]=(String [])aux;
                if(list[0].equals("MENSAJE")){
                this.cont.recibirMensaje(list);
                cont.setNombre(list[1]);
                }else if(list[0].equals("MENSAJEPRIVADO")){
                this.cont.recibirMensajePriva(list);
                cont.setNombre(list[1]);            
                    
                }
                else if(list[0].equals("MENSAJETODOS")){
                    this.cont.recibirMensaje(list);
                }
                else if(list[0].equals("CONECTADOS"))
                  {
                   conectados(list);
                  }
                else if(list[0].equals("DESCONECTAR"))
                  {
                  this.cont.desconectar(list[1]+" "+"Salió");
                  }
                else if(list[0].equals("CONECTAR"))
                {
                    
                }
            } catch (IOException ex) {
                System.out.println("se perdio la conexion con el servidor");
                activo=false;
            } catch (ClassNotFoundException ex) {
                
            }
        }
    }
    
    //Metodo que retorna todos los conectados
    public void conectados(String []list){
          String cad="";
                    for (int i = 1; i < list.length; i++) {
                       cad+=list[i]+""+",";
                    }
                    cad+="";
         this.cont.conectados(cad);
         
    }
     public boolean getContectados() {
        if(this.getConectados()>2){
            return true;
        }else{
            return false;
        }
    }
     
     public void responder(String nombre) throws IOException{       
              
          System.out.print("\n"+"Responder:");
          BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
          String mensaje=br.readLine();
          this.enviarMensaje(mensaje,nombre);
        
      
   }
     

    /**
     * @return the conectados
     */
    public int getConectados() {
        return conectados;
    }

    /**
     * @param conectados the conectados to set
     */
    public void setConectados(int conectados) {
        this.conectados = conectados;
    }
}
