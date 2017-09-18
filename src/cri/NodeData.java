/*

Esta clase corresponde a un nodo de la lista que se estará armando

 */

package cri;

/**
 *
 * @author Andrés Corredor B
 */
public class NodeData {
    
    private double amplitude;
    private double lengthWave;
    NodeData nextNodo;
    
    
    NodeData(double amplitude, double lengthWave){
    
        this(amplitude,lengthWave,null);
            
    }
    
    NodeData(double amplitude, double lengthWave, NodeData nextNodo){
    
    
        this.amplitude=amplitude;
        this.lengthWave=lengthWave;
        this.nextNodo=nextNodo;
    
    }
    
    public double getAmplitude(){
    
    
        return amplitude;
    
    }
    
     public double getLengthWave(){
    
    
        return lengthWave;
    
    }

    
}
