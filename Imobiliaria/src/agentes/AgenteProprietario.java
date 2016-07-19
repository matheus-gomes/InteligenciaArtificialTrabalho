/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matheus
 */
public class AgenteProprietario extends Agent{
    @Override
    protected void setup(){
        System.out.println("Oi, eu sou o agente proprietário!");
        System.out.println("Meu local-name é " +getAID().getLocalName());
        System.out.println("Meu GUID é " +getAID().getName());
        System.out.println("Meus endereços são:");
        Iterator it = getAID().getAllAddresses();
        while(it.hasNext()){
            System.out.println("- " +it.next());
        }
        
        //Criando uma entrada no DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        
        //Criando um serviço
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Venda de imovel");              //tipo do serviço
        sd.setName("Serviço de venda de imovel");   //nome do serviço
        //Adicionando o serviço
        dfd.addServices(sd);
        
        //Registrando o agente nas paginas amarelas
        try {
            DFService.register(this, dfd);
        } catch(FIPAException e){
            e.printStackTrace();
        }
    }
    
    protected void takeDown(){
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}
