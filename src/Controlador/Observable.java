/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

/**
 *
 * @author Carlos
 */
public interface Observable {

    void notifyObservers();

    void register(Observer obs);

    void unRegister(Observer obs);

}
