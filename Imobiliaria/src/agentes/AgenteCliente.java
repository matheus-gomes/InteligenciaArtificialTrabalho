/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.AID;
import jade.core.Agent;
import java.util.Iterator;

/**
 *
 * @author Matheus
 */
public class AgenteCliente extends Agent{
    private Imovel imovel = new Imovel();
    
    private AID[] agentesProprietarios = {new AID("proprietario1", AID.ISLOCALNAME),
                                          new AID("proprietario1", AID.ISLOCALNAME)};
    @Override
    protected void setup(){
        System.out.println("Oi, eu sou o agente cliente!");
        System.out.println("Meu local-name é " +getAID().getLocalName());
        System.out.println("Meu GUID é " +getAID().getName());
        System.out.println("Meus endereços são:");
        Iterator it = getAID().getAllAddresses();
        while(it.hasNext()){
            System.out.println("- " +it.next());
        }
        
        Object[] args = getArguments();
        if(args != null && args.length > 0){
            imovel.setTipoImovel((String) args[0]);
            //imovel.setTamanho((double) args[1]);
            //imovel.setQuantQuartos((int) args[2]);
            //imovel.setLocalizacao((String) args[3]);
            //imovel.setValor((double) args[4]);
            
            System.out.println("Procurando um(a)" + imovel.getTipoImovel() + " de " + imovel.getTamanho() +
                                " metros quadrados, " + imovel.getQuantQuartos() + " quartos, no setor "
                                + imovel.getLocalizacao() + ", com o valor de " + imovel.getValor());
        }
        else {
            System.out.println("Sem parâmetros especificados");
            doDelete();
        }
    }
    
    protected void takeDown(){
        System.out.println("Agente cliente " + getAID().getName()+ " encerrando.");
    }
}
