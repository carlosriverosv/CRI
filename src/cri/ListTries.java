/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cri;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author BALUM ETB3
 */
public class  ListTries {
    
    
    private String nameData;
    private static NodeTries firstNode;
    private static NodeTries finalNode;
    private static NodeTries actualNode;

    
    
    ListTries(String nameData){
    
        this.nameData=nameData;
        
         try
            {
            //    ((Properties))
                        
                
               
                System.out.print(nameData);
                Scanner salida = new Scanner(new File(nameData));
                
                try // lee registros del archivo, usando el objeto Scanner
                {
                    double lamb1=0;
                    double amplX=0;
                    double amplY=0;
                    double amplZ=0;
                    
                    double lambR;
                    double lamb2;
                    double amplX2;
                    double amplY2;
                    double amplZ2;
                    if(salida.hasNext()){// read the firsts values
                        lamb1=salida.nextDouble();
                        amplX=salida.nextDouble();
                        amplY=salida.nextDouble();
                        amplZ=salida.nextDouble();
                        
                    }
                    

                    if(Math.ceil(lamb1)==Math.floor(lamb1)){
                        insertNode(lamb1,amplX,amplY,amplZ);
                        lambR=lamb1+1;       //aumenta la sigueinte requerida                 
                    }
                    else
                        lambR=Math.ceil(lamb1);// Si no es entero calcula el techo del valor actual
                    lamb2=salida.nextDouble();// the neext Lamb
                    amplX2=salida.nextDouble();
                    amplY2=salida.nextDouble();
                    amplZ2=salida.nextDouble();
                    
                   
                    while ( salida.hasNext() )
                {
                    
                    if(lambR>lamb2){
                        lamb1=lamb2;
                        amplX=amplX2;
                        amplY=amplY2;
                        amplZ=amplZ2;
                        lamb2=salida.nextDouble();
                        amplX2=salida.nextDouble();
                        amplY2=salida.nextDouble();
                        amplZ2=salida.nextDouble();
                    }
                          

                   if(lamb2==lambR){// if the next value is equal to value requerided then save
                        insertNode(lamb2,amplX2,amplY2,amplZ2);
                        lambR=lambR+1;
                    }                      
                    else if(lamb2>lambR){// if the next value is major to value requerided tehn calculate and save
                            insertNode(lambR,(amplX2-amplX)/(lamb2-lamb1)*(lambR-lamb2)+amplX2,
                                    
                                    (amplY2-amplY)/(lamb2-lamb1)*(lambR-lamb2)+amplY2,
                                    (amplZ2-amplZ)/(lamb2-lamb1)*(lambR-lamb2)+amplZ2);
                            lambR=lambR+1;
                        }
                            
      
                } // fin de while
                
                } // fin de try
                catch ( NoSuchElementException elementException )
                {
                    System.err.println( "El archivo no esta bien formado." );
                    salida.close();
                    System.exit( 1 );
                } // fin de catch
            catch ( IllegalStateException stateException )
            {
                System.err.println( "Error al leer del archivo." );
                System.exit( 1 );
            } // fin de catch
        } // fin de try
        catch ( FileNotFoundException fileNotFoundException )
            {
            System.err.println( "Error al abrir el archivo." );
            System.exit( 1 );
            } // fin de catch
          
             
    }
    
    
    private void insertNode(double lengthWave,double amplX,double amplY,double amplZ){

    
        if(firstNode==null){
            firstNode=finalNode=new NodeTries(amplX,amplY,amplZ,lengthWave);  
        }
        else
            finalNode=finalNode.nextNodo=new NodeTries(amplX,amplY,amplZ,lengthWave);
            
     }
    
    
    

    public static NodeTries getNodo(){
    
        
        if(actualNode==finalNode)
            actualNode=null;
        else if(actualNode==null)
            actualNode=firstNode;
        else
            actualNode=actualNode.nextNodo;
        
        
    return actualNode;
            
          
    }
    
    
    public NodeTries getNodoActual(){
    
        return actualNode;
   
    }
    
    public static NodeTries getFirstNode(){
    
    
        return firstNode;
    }
    
    public static NodeTries getFinalNode(){
    
        return finalNode;
    
    }
    
    public static void resetNode(){
    
    
        actualNode=null;
    
    }
    
    public void printSpd(){
    
    
        resetNode();
        NodeTries node=getNodo();
        
        while(node!=null){
        
           System.out.println("Ampl="+node.getAmplitude()+"\tlmp="+node.getLengthWave()+"\n");
            node=getNodo();
        
        }
        
        

    
    
    }
    
 
    
}
