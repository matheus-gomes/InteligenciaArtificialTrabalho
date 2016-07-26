/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Matheus
 */
public class AgenteCliente extends Agent {

    private Imovel imovel = new Imovel();

    private AID[] agentesProprietarios;

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

        addBehaviour(new WakerBehaviour(this, 10000) {
            @Override
            protected void onWake() {
                //Criando uma entrada no DF
                DFAgentDescription template = new DFAgentDescription();

                //Criando um objeto contendo a descrição do serviço
                ServiceDescription sd = new ServiceDescription();
                sd.setType(imovel.getTipoImovel());
                //Adicionando o serviço na entrada
                template.addServices(sd);
                try {
                    //Array com agentes que possuem o serviço
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    agentesProprietarios = new AID[result.length];
                    //Imprimir resultados
                    for (int i = 0; i < result.length; i++) {
//                        String out = result[i].getName().getLocalName() + " provê ";
//
//                        Iterator iter = result[i].getAllServices();
//
//                        while (iter.hasNext()) {
//                            ServiceDescription SD = (ServiceDescription) iter.next();
//
//                            System.out.println(out + " " + SD.getName());
//                        }
                        agentesProprietarios[i] = result[i].getName();
                        System.out.println(agentesProprietarios[i].getName());

                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        for (int j = 0; j < agentesProprietarios.length; j++) {
                            msg.addReceiver(agentesProprietarios[j]);
                        }
                        msg.addReceiver(new AID(result[i].getName().getLocalName(), AID.ISLOCALNAME));
                        msg.setLanguage("Português");
                        msg.setContent("Estou procurando um(a) " + imovel.getTipoImovel());
                        send(msg);

                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });

//        addBehaviour(new CyclicBehaviour(this) {
//            
//        });
        addBehaviour(new TrocaInformacoes());

    }

    protected void takeDown() {
        System.out.println("Agente cliente " + getAID().getName() + " encerrando.");
    }

    private class TrocaInformacoes extends Behaviour {

        private int step = 0;   //etapa na qual a comunicação se encontra
        private int grade = 0;  //o grau de cumprimento das requisições

        @Override
        public void action() {

            switch (step) {
                case 0:
                    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                    ACLMessage msg = myAgent.receive(mt);
                    if (msg != null) {
                        if (msg.getPerformative() == ACLMessage.INFORM) {
                            System.out.println(msg.getSender().getName() + " informa que o imóvel está disponível");
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.REQUEST);
                            reply.setContent("Quantos metros quadrados tem seu imovel?");
                            send(reply);

                            step = 1;
                            //tratar problema
                        } else {
                            System.out.println(msg.getSender().getName() + " informa que o imóvel não está disponível");
                            step = 5;
                        }

                    } else {
                        block();
                    }
                    break;

                case 1:
                    mt = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
                    msg = myAgent.receive(mt);

                    if (msg != null) {
                        double tamanho;
                        tamanho = Double.valueOf(msg.getContent());
                        System.out.println(msg.getSender().getName() + " informa que o tamanho do imovel é: " + tamanho);
                        if ((tamanho >= (imovel.getTamanho() * 0.80)) && (tamanho <= (imovel.getTamanho() * 1.20))) {
                            grade++;
                        }
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.REQUEST);
                        reply.setContent("Em qual setor fica o imovel?");
                        send(reply);

                        step++;
                    } else {
                        block();
                    }
                    break;

                case 2:
                    mt = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
                    msg = myAgent.receive(mt);

                    if (msg != null) {
                        String setor;
                        setor = msg.getContent();
                        System.out.println(msg.getSender().getName() + " informa que a localização do imovel é: " + setor);
                        if (setor.equals(imovel.getLocalizacao())) {
                            grade++;
                        }
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.REQUEST);
                        reply.setContent("Qual o valor do imovel?");
                        send(reply);

                        step++;
                    } else {
                        block();
                    }
                    break;

                case 3:
                    mt = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
                    msg = myAgent.receive(mt);

                    if (msg != null) {
                        double valor;
                        valor = Double.valueOf(msg.getContent());
                        System.out.println(msg.getSender().getName() + " informa que o valor do imovel é: " + valor);
                        if ((valor >= (imovel.getValor() * 0.80)) && (valor <= (imovel.getValor() * 1.20))) {
                            grade++;
                        }

                        ACLMessage reply = msg.createReply();
                        if (grade >= 2) {
                            if (valor <= imovel.getValor()) {
                                reply.setPerformative(ACLMessage.CFP);
                                reply.setContent("Eu quero comprar o imovel");
                            } else {
                                reply.setPerformative(ACLMessage.CFP);
                                reply.setContent(String.valueOf(imovel.getValor()));
                            }
                        } else {
                            reply.setPerformative(ACLMessage.FAILURE);
                            reply.setContent("Nao tenho interesse no imovel");
                        }
                        send(reply);

                        step++;
                    } else {
                        block();
                    }
                    break;

                case 4:
                    mt = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
                    msg = myAgent.receive(mt);

                    if (msg != null) {
                        double valor;
                        valor = Double.valueOf(msg.getContent());
                        if ((valor >= (imovel.getValor() * 0.80)) && (valor <= (imovel.getValor() * 1.20))) {
                            grade++;
                        }
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent("Qual o valor do imovel?");
                        send(reply);

                        step++;
                    } else {
                        block();
                    }
                    break;
            }
        }

        @Override
        public boolean done() {
            return step == 4;
        }

    }

}
