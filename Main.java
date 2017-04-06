/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JantarDosFilosofos;

import static java.lang.Thread.sleep;
import java.util.ArrayList;

/**
 *
 * @author Leandro
 */
public class Main {
    private static ArrayList<Thread> listaDeTarefas;
    public static final int QUANTIDADE_MAXIMA = 5;
    
    public static void main(String[] args) throws InterruptedException{
        Garfo.instanciarGarfos();
        Filosofo.instanciarFilosofos();
        
        listaDeTarefas = new ArrayList();
        for(int i=0; i<Main.QUANTIDADE_MAXIMA; i++){
            Filosofo f = Filosofo.getFilosofo(i);
            listaDeTarefas.add(new Thread(f));
            listaDeTarefas.get(i).setName(f.getNome());
            listaDeTarefas.get(i).start();
        }
        
        for(int i=0; i<20; i++){
            System.out.println("\nTABELA DE ESTADOS:");
            for(int j=0; j<QUANTIDADE_MAXIMA; j++){
                System.out.println(Filosofo.getFilosofo(j).getEstados());
            }
            System.out.println();
            sleep(1000);
        }
        Filosofo.terminar();
        System.out.println("Main Encerrada!");
    }
}
