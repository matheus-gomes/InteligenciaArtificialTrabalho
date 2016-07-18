/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imobiliaria.clientes;

import jade.core.AID;
import jade.core.Agent;
import java.util.Vector;

/**
 *
 * @author Matheus
 */
public class AgenteCliente extends Agent {

    //A lista de todos proprietários conhecidos

    private Vector agentesProprietarios = new Vector();

    //A GUI para interagir com o usuário
    private AgenteClienteGui myGui;

    @Override
    protected void setup() {
        //Mensagem de entrada
        System.out.println("Agente-cliente " + getAID().getName() + " está pronto");

        //Pega os nomes dos agentes proprietarios e seus argumentos
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                AID proprietario = new AID((String) args[i], AID.ISLOCALNAME);
                agentesProprietarios.addElement(proprietario);
            }
        }

        //Mostrar a GUI para interagir com o usuário
        myGui = new AgenteClienteGuiImpl();
        myGui.setAgent(this);
        myGui.show();
    }

    @Override
    protected void takeDown() {
        //Dispor a GUI se ela existir
        if(myGui != null){
            myGui.dispose();
        }
    }
}
