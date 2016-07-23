/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportamentos.cliente;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 *
 * @author Matheus
 */
public class GerenciadorDeNegociacao extends TickerBehaviour{
    private String tipoImovel;
    
    
    private GerenciadorDeNegociacao(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
