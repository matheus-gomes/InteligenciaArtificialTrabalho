/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imobiliaria.clientes;

/**
 *
 * @author Matheus
 */
public interface AgenteClienteGui {
    void setAgent(AgenteCliente a);
    void show();
    void hide();
    void notifyUser(String message);
}
