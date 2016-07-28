/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Iterator;

/**
 *
 * @author Matheus
 */
public class AgenteProprietario extends Agent {

    private Imovel imovel = new Imovel();

    @Override
    protected void setup() {
        System.out.println("Oi, eu sou o agente proprietário!");
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
            imovel.setLocalizacao((String) args[2]);
            imovel.setValor(Double.valueOf((String) args[3]));

            System.out.println("Agente: " + getAID().getLocalName());
            System.out.println("Possuo um(a)" + imovel.getTipoImovel() + " de " + imovel.getTamanho()
                    + " metros quadrados, no setor " + imovel.getLocalizacao() + ", com o valor de " + imovel.getValor());
            System.out.println("-----------------------------------------------------------------------");
        } else {
            System.out.println("Sem parâmetros especificados");
            doDelete();
        }

        //Criando uma entrada no DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        //Criando um serviço
        ServiceDescription sd = new ServiceDescription();
        sd.setType(imovel.getTipoImovel());         //tipo do serviço
        sd.setName("Serviço de venda de imovel");   //nome do serviço
        //Adicionando o serviço
        dfd.addServices(sd);

        //Registrando o agente nas paginas amarelas
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        addBehaviour(new ReceberSolicitacao());
        addBehaviour(new TrocaInformações());
        addBehaviour(new Negociar());
        addBehaviour(new FecharNegocio());
        addBehaviour(new OfertaRecusada());
        addBehaviour(new NegociacaoRecusada());
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    private class ReceberSolicitacao extends CyclicBehaviour {

        private boolean available = true; //variavel que indica se o imovel esta disponivel
        //private int step = 0;

        @Override
        public void action() {

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                //String content = msg.getContent();

                System.out.println("O agente " + msg.getSender().getName() + " está procurando um(a) " + imovel.getTipoImovel());
                System.out.println("-----------------------------------------------------------------------");

                ACLMessage reply = msg.createReply();
                if (available) {
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("Disponivel");
                } else {
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("Indisponivel");
                }
                send(reply);
            } else {
                block();
            }

        }

    }

    private class TrocaInformações extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                ACLMessage reply = msg.createReply();
                System.out.println(msg.getSender().getName() + "perguntou: " + msg.getContent());
                if (msg.getContent().equals("Quantos metros quadrados tem seu imovel?")) {
                    reply.setPerformative(ACLMessage.AGREE);
                    reply.setContent(String.valueOf(imovel.getTamanho()));
                }
                if (msg.getContent().equals("Em qual setor fica o imovel?")) {
                    reply.setPerformative(ACLMessage.AGREE);
                    reply.setContent(imovel.getLocalizacao());
                }
                if (msg.getContent().equals("Qual o valor do imovel?")) {
                    reply.setPerformative(ACLMessage.AGREE);
                    reply.setContent(String.valueOf(imovel.getValor()));
                    //step = 2;
                }

                send(reply);
            } else {
                block();
            }
        }

    }

    private class Negociar extends CyclicBehaviour {

        private double valorProposta = imovel.getValor();
        private int numeroPropostas = 0;

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                ACLMessage reply = msg.createReply();
                System.out.println(msg.getSender().getName() + " pode pagar " + msg.getContent());

                double valorOferecido = Double.valueOf(msg.getContent()); //valor oferecido pelo cliente

                if (valorOferecido >= valorProposta * 0.95) {
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setContent("Proposta aceita.");
                } else if ((valorOferecido < valorProposta) && (numeroPropostas < 3)) {
                    valorProposta = valorProposta * 0.95;
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent(String.valueOf(valorProposta));
                    numeroPropostas++;
                } else {
                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    reply.setContent("Proposta recusada.");
                }
                send(reply);
            } else {
                block();
            }

        }

    }

    private class FecharNegocio extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                System.out.println(msg.getSender() + " aceitou sua proposta.");
                myAgent.doDelete();
            } else {
                block();
            }
        }

    }

    private class OfertaRecusada extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                System.out.println(msg.getSender() + " recusou sua proposta.");
                myAgent.doDelete();
            } else {
                block();
            }
        }
    }

    private class NegociacaoRecusada extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.FAILURE);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                System.out.println(msg.getSender() + " não tem interesse em seu imóvel.");
                myAgent.doDelete();
            } else {
                block();
            }
        }
    }

}
