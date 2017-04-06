/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JantarDosFilosofos;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import static javafx.application.Platform.exit;

/**
 *
 * @author Leandro
 */
public class Filosofo implements Runnable {
    private static final Semaphore SEMAFORO[] = new Semaphore[Main.QUANTIDADE_MAXIMA];
    private static final Object MUTEX_ESTADO = new Object();
    private static ArrayList<Filosofo> filosofos;
    private static boolean terminar;
    private final int identificador;
    private final String nome;
    private Estado estado; //area critica, mesmo que não sendo estático outras threads conseguem mexer
    
    private enum Estado{
        PENSANDO, FAMINTO, COMENDO, ESPERANDO
    }
    
    private Filosofo() {
        identificador = filosofos.size();
        nome = "FILOSOFO"+Integer.toString(identificador);
        estado = Estado.PENSANDO;
    }
    
    public static void instanciarFilosofos(){
        if(filosofos == null){
            filosofos = new ArrayList();
            for(int i=0; i < Main.QUANTIDADE_MAXIMA; i++){
                filosofos.add(new Filosofo());
                SEMAFORO[i] = new Semaphore(0); 
            }
            if(filosofos.size() == Main.QUANTIDADE_MAXIMA){
                System.out.println("PARABÉNS! Filosofos instanciados!");
            }
            else{
                System.out.println("ERRO! Problema na instanciação dos filosofos! " + Integer.toString(filosofos.size()));
                filosofos.clear();
                exit();
            }
        }
        else{
            System.out.println("ATENÇÃO! Filosofos já instanciados!");
        }
    }
    
    public static Filosofo getFilosofo(int id){
        return filosofos.get(id);
    }
    
    @Override
    public void run(){
        System.out.println("THREAD "+nome+" INICIADA!");
        while(true){
            try {
                sleep(2000);
                pegarGarfos();
                sleep(6000);
                largarGarfos();
                sleep(2000);
                if(terminar == true) break;
            } catch (InterruptedException ex) {
                System.out.println("ERRO! InterruptedException, encerrando...");
                exit();
            }
        }
        System.out.println("THREAD "+nome+" ENCERRADA!");
    }
    
    private void pensando(){
        estado = Estado.PENSANDO;
    }
    
    private void faminto(){
        estado = Estado.FAMINTO;
    }
    
    private void comendo(int id){
        filosofos.get(id).estado = Estado.COMENDO;
    }
    
    private void esperando(int id){
        filosofos.get(id).estado = Estado.ESPERANDO;
    }
      
    private void pegarGarfos() throws InterruptedException{
        System.out.println(nome+" INDO PEGAR OS GARFOS!");
        synchronized(MUTEX_ESTADO){
            faminto();
            verificarVizinho(identificador);
        }
        SEMAFORO[identificador].acquire();
    }
    
    private void largarGarfos(){
        System.out.println(nome+" LARGANDO OS GARFOS!");
        
        synchronized(MUTEX_ESTADO){
            pensando();
            
            Garfo.largar(identificador);
            Garfo.largar(identificador+1);
            System.out.println(nome+" LARGOU OS GARFOS! Avisando seus vizinhos...");
            verificarVizinho(identificador-1);
            verificarVizinho(identificador+1);
        }
    }
    
    private int verificarId(int id){
        if(id < 0)
            id = Main.QUANTIDADE_MAXIMA-1;
        if(id == Main.QUANTIDADE_MAXIMA)
            id = 0;
        return id;
    }
    
    private void verificarVizinho(int id){
        int direita, esquerda;
        
        id          = verificarId(id);
        direita     = verificarId(id+1);
        esquerda    = verificarId(id-1);
        
        if((filosofos.get(id).estado == Estado.FAMINTO || filosofos.get(id).estado == Estado.ESPERANDO) && filosofos.get(esquerda).estado != Estado.COMENDO && filosofos.get(direita).estado != Estado.COMENDO){
            Garfo.pegar(id);
            Garfo.pegar(id+1);
            comendo(id);
            System.out.println(filosofos.get(id).nome+" PEGOU OS GARFOS! Comendo...");
            SEMAFORO[id].release(); //dando um passe para thread continuar na acquire
        }
        else{
            if(filosofos.get(id).estado == Estado.FAMINTO){
                esperando(id); //estado extra pra fins didáticos, intermediário entre faminto e comendo
                System.out.println(filosofos.get(id).nome+" NÃO CONSEGUIU PEGAR OS GARFOS! Esperando...");
            }
        }
    }

    public String getEstados(){
        String fs, g1s, g2s;
        
        fs = Integer.toString(identificador)+"="+estado;
        g1s = Integer.toString(identificador)+"="+Garfo.getEstado(identificador);
        g2s = Integer.toString(verificarId(identificador+1))+"="+Garfo.getEstado(identificador+1);
        
        return "G"+g1s+"\t F"+fs+"\t G"+g2s;
    }
        
    public static void terminar(){
        terminar = true;
    }
}
