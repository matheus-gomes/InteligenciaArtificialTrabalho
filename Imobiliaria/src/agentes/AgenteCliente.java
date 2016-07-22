/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Matheus
 */
public class AgenteCliente extends Agent {

    private Imovel imovel = new Imovel();

//    private Vector agentesProprietarios;

    @Override
    protected void setup() {
        System.out.println("Oi, eu sou o agente cliente!");
        System.out.println("Meu local-name é " + getAID().getLocalName());
        System.out.println("Meu GUID é " + getAID().getName());
        System.out.println("Meus endereços são:");
        Iterator it = getAID().getAllAddresses();
        while (it.hasNext()) {
            System.out.println("- " + it.next());
        }

        addBehaviour(new TickerBehaviour(this, 60000) {

            @Override
            protected void onTick() {
                //Criando uma entrada no DF
                DFAgentDescription template = new DFAgentDescription();

                //Criando um objeto contendo a descrição do serviço
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Venda de imovel");
                //Adicionando o serviço na entrada
                template.addServices(sd);
                try {
                    //Array com agentes que possuem o serviço
                    DFAgentDescription[] result = DFService.search(myAgent, template);
//                    agentesProprietarios.clear();
                    //Imprimir resultados
                    for (int i = 0; i < result.length; i++) {
                        String out = result[i].getName().getLocalName() + " provê ";

                        Iterator iter = result[i].getAllServices();

                        while (iter.hasNext()) {
                            ServiceDescription SD = (ServiceDescription) iter.next();

                            System.out.println(out + " " + SD.getName());
                        }
//                        agentesProprietarios.addElement(result[i].getName());
//                        System.out.println(agentesProprietarios.get(i));
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });

        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            imovel.setTipoImovel((String) args[0]);
            imovel.setTamanho(Double.valueOf((String) args[1]));
            //imovel.setQuantQuartos(Integer.parseInt((String) args[2]));
            imovel.setLocalizacao((String) args[2]);
            imovel.setValor(Double.valueOf((String) args[3]));

            System.out.println("Procurando um(a)" + imovel.getTipoImovel() + " de " + imovel.getTamanho()
                    + " metros quadrados, no setor " + imovel.getLocalizacao() + ", com o valor de " + imovel.getValor());
        } else {
            System.out.println("Sem parâmetros especificados");
            doDelete();
        }
    }

    protected void takeDown() {
        System.out.println("Agente cliente " + getAID().getName() + " encerrando.");
    }
}
