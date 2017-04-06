/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JantarDosFilosofos;

import java.util.ArrayList;
import static javafx.application.Platform.exit;

/**
 *
 * @author Leandro
 */
public class Garfo {
    private static ArrayList<Garfo> garfos;
    private Estado estado;
    
    private enum Estado{
        NUNCAUSADO, LIVRE, OCUPADO
    }

    private Garfo() {
        estado = Estado.NUNCAUSADO;
    }
    
    public static void instanciarGarfos(){
        if(garfos == null){
            garfos = new ArrayList();
            for(int i=0; i < Main.QUANTIDADE_MAXIMA; i++){
                garfos.add(new Garfo());
            }
            if(garfos.size() == Main.QUANTIDADE_MAXIMA){
                System.out.println("PARABÉNS! Garfos instanciados!");
            }
            else{
                System.out.println("ERRO! Problema na instanciação dos garfos! " + Integer.toString(garfos.size()));
                garfos.clear();
                exit();
            }
        }
        else{
            System.out.println("ATENÇÃO! Garfos já instanciados!");
        }
    }
    
    private static int verificarId(int id){
        if(id < 0)
            id = Main.QUANTIDADE_MAXIMA-1;
        if(id == Main.QUANTIDADE_MAXIMA)
            id = 0;
        return id;
    }
    
    public static void pegar(int id){
        id = verificarId(id);
        garfos.get(id).estado = Estado.OCUPADO;
    }
    
    public static void largar(int id){
        id = verificarId(id);
        garfos.get(id).estado = Estado.LIVRE;
    }
    
    public static String getEstado(int id){
        String e;
        id = verificarId(id);
        switch(garfos.get(id).estado){
            case LIVRE:
                e="LIVRE";
                break;
            case OCUPADO:
                e="OCUPADO";
                break;
            default:
                e="NUNCAUSADO";
        }
        return e;
    }
}
