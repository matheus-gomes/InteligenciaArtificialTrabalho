/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.Agent;
import java.util.Iterator;

/**
 *
 * @author Matheus
 */
public class AgenteCliente extends Agent{
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
    }
}
