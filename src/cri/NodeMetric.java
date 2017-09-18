/*
Aquí
 */

package cri;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author Andres Corredor
 */
public class NodeMetric {
    
    public static ListData photopic;
    public static ListData mesopic;
    public static ListData mesopicN;
    public static ListData scotopic;
    public static double maxP;
    public static double maxS;
    public static double maxM;
    public static double lengthWave;
    NodeMetric nextNodo;
    double lP;
    double lS;
    double lMes;
    double sP;
    double m;
    double mPic;
    
    
    
    NodeMetric(String Path){
    
        maxP=0;
        maxS=0;
        maxM=0;
        setEspectroPhoto(Path+"/Archivos/Photopic.txt");
        setEspectroScoto(Path+"/Archivos/Scotopic.txt");
        
     //   this.photopic=photopic;
     //   this.scotopic=scotopic;
            
    }
    
    public void setEspectroPhoto(String ubicationLamp){
        
        
        photopic=new ListData(new File(ubicationLamp));
        maxP=photopic.max;
       
       /* 
          try
            {
                Scanner salida = new Scanner(new File(ubicationLamp));
                
                try // lee registros del archivo, usando el objeto Scanner
                {
                    double lamb1=0;
                    double ampl1=0;
                    double lambR;
                    double lamb2;
                    double ampl2;
                    if(salida.hasNext()){// read the first values
                        lamb1=Double.parseDouble(salida.next().replace(',','.'));
                        ampl1=Double.parseDouble(salida.next().replace(',','.'));
                        System.out.println(lamb1);
                        
                    }


                    if(Math.ceil(lamb1)==Math.floor(lamb1)){
                        setSpdPhoto(lamb1,ampl1);//ingresa el primer valor si es entero
                        lambR=lamb1+1;       //aumenta la sigueinte requerida                 
                    }
                    else
                        lambR=Math.ceil(lamb1);// Si no es entero calcula el techo del valor actual
                    lamb2=Double.parseDouble(salida.next().replace(',','.'));// the next Lamp
                    ampl2=Double.parseDouble(salida.next().replace(',','.'));// the next Ampl
                    
                   
                    while (salida.hasNext())
                {
                    
                    if(lambR>lamb2){
                        lamb1=lamb2;
                        ampl1=ampl2;
                        lamb2=Double.parseDouble(salida.next().replace(',','.'));
                        ampl2=Double.parseDouble(salida.next().replace(',','.'));
                    }
                    if(lamb2==lambR){// if the next value is equal to value requerided, then save
                        setSpdPhoto(lamb2,ampl2);
                        lambR=lambR+1;
                    }                      
                    else if(lamb2>lambR){// if the next value is major to value requerided, then calculate and save
                            setSpdPhoto(lambR,(ampl2-ampl1)/(lamb2-lamb1)*(lambR-lamb1)+ampl1);
                            lambR=lambR+1;
                        }
      
                } // fin de while
                
                } // fin de try
                catch ( NoSuchElementException elementException )
                {
                    System.err.println( "El archivo no esta bien formado." );
               //     salida.close();
               //     System.exit( 1 );
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
          

          */
                  
    }   
    
    private void setSpdPhoto(double lamb ,double ampl){
        
        
       photopic.insertNode(ampl, lamb);
       if(ampl>maxP)
           maxP=ampl;
       
       
    }
    
    
    public void setEspectroScoto(String ubicationLamp){
        
        
        scotopic=new ListData(new File(ubicationLamp));
        maxS=scotopic.max;
       
      /*  
          try
            {
                Scanner salida = new Scanner(new File(ubicationLamp));
                
                try // lee registros del archivo, usando el objeto Scanner
                {
                    double lamb1=0;
                    double ampl1=0;
                    double lambR;
                    double lamb2;
                    double ampl2;
                    if(salida.hasNext()){// read the first values
                        lamb1=Double.parseDouble(salida.next().replace(',','.'));
                        ampl1=Double.parseDouble(salida.next().replace(',','.'));
                        System.out.println(lamb1);
                        
                    }


                    if(Math.ceil(lamb1)==Math.floor(lamb1)){
                        setSpdScoto(lamb1,ampl1);//ingresa el primer valor si es entero
                        lambR=lamb1+1;       //aumenta la sigueinte requerida                 
                    }
                    else
                        lambR=Math.ceil(lamb1);// Si no es entero calcula el techo del valor actual
                    lamb2=Double.parseDouble(salida.next().replace(',','.'));// the next Lamp
                    ampl2=Double.parseDouble(salida.next().replace(',','.'));// the next Ampl
                    
                   
                    while (salida.hasNext())
                {
                    
                    if(lambR>lamb2){
                        lamb1=lamb2;
                        ampl1=ampl2;
                        lamb2=Double.parseDouble(salida.next().replace(',','.'));
                        ampl2=Double.parseDouble(salida.next().replace(',','.'));
                    }
                    if(lamb2==lambR){// if the next value is equal to value requerided, then save
                        setSpdScoto(lamb2,ampl2);
                        lambR=lambR+1;
                    }                      
                    else if(lamb2>lambR){// if the next value is major to value requerided, then calculate and save
                            setSpdScoto(lambR,(ampl2-ampl1)/(lamb2-lamb1)*(lambR-lamb1)+ampl1);
                            lambR=lambR+1;
                        }
      
                } // fin de while
                
                } // fin de try
                catch ( NoSuchElementException elementException )
                {
                    System.err.println( "El archivo no esta bien formado." );
               //     salida.close();
               //     System.exit( 1 );
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
          

          
                  */
    }   
    
    private void setSpdScoto(double lamb ,double ampl){
        
        
       scotopic.insertNode(ampl, lamb);
       if(ampl>maxS)
           maxS=ampl;
       
       
    }
    
    
    public void setParametros(double lp){
    
    lP=lp;
    
    
    }
    
    
    public void curvaMesopica(ListData testData,double maxT){
        
        double Ls=multiplicacion(testData,maxT,scotopic,maxS);
        double Lp=multiplicacion(testData,maxT,photopic,maxP);
        
        
        sP=Ls/Lp*1699.0/683.0;
        lS=sP*lP;
        System.out.print("\nsP="+lS+"\tLs"+Ls+"\tLp:"+Lp);
        
        
        
        m=0.5;
        double md=0.0;
      //  double Lsp=Lpp*SP;
        double Lm=0.0;
    
        while(Math.sqrt(Math.pow(m-md,2.0))>0.001){
            md=m;
            Lm=(m*lP+(1-m)*(lS)*(683.0/1699.0))/(m+(1-m)*(683.0/1699.0));
            m=(0.767+0.3334*Math.log10(Lm));
        }
        
        
         mesopic=suma(photopic,maxP,scotopic,maxS);  
         
         normalizar(mesopic);
         
         System.out.print("mPic"+mPic);
         lMes=multiplicacion(testData, 1.0,mesopic,maxM)*(lP/68300.0)*(683.0/mPic);
    
    
    
    }
    
    
    private  double  multiplicacion(ListData first,double maxP, ListData second, double maxS){
        
    
    double resultado=0.0;
    double minor;
    double major;
         first.resetNode();
        second.resetNode();
        
        if(first.getFirstNode().getLengthWave()<second.getFirstNode().getLengthWave())
            minor=second.getFirstNode().getLengthWave();
        else
            minor=first.getFirstNode().getLengthWave();
         
        if(first.getFinalNode().getLengthWave()>second.getFinalNode().getLengthWave())
            major=second.getFinalNode().getLengthWave();
        else
            major=first.getFinalNode().getLengthWave();

        NodeData nodeFirts=first.getNodo();
        NodeData nodeSecond=second.getNodo();
        
            while(nodeFirts.getLengthWave()<minor){
                nodeFirts=first.getNodo();                
            }
            while(nodeSecond.getLengthWave()<minor){
                nodeSecond=second.getNodo();
            }
            
        
        while(major!=second.getNodoActual().getLengthWave() | 
                major!=first.getNodoActual().getLengthWave()){
            
            resultado=nodeFirts.getAmplitude()*nodeSecond.getAmplitude()/(maxP*maxS)+resultado;
 
            nodeFirts=first.getNodo();
            nodeSecond=second.getNodo();
            
        }
        
        return resultado;
    
    }
    
    
    public ListData suma(ListData first,double maxP, ListData second, double maxS){
    
        ListData suma=new ListData("Mesópica");
        double minor;
        double major;
        maxM=0.0;
        first.resetNode();
        second.resetNode();

        if(first.getFirstNode().getLengthWave()<second.getFirstNode().getLengthWave())
            minor=second.getFirstNode().getLengthWave();
        else
            minor=first.getFirstNode().getLengthWave();
         
        if(first.getFinalNode().getLengthWave()>second.getFinalNode().getLengthWave())
            major=second.getFinalNode().getLengthWave();
        else
            major=first.getFinalNode().getLengthWave();

        NodeData nodeFirts=first.getNodo();
        NodeData nodeSecond=second.getNodo();
        
        while(nodeFirts.getLengthWave()<minor){
            nodeFirts=first.getNodo();                
        }
        while(nodeSecond.getLengthWave()<minor){
            nodeSecond=second.getNodo();
        }
            
        double ampl;
        while(major!=second.getNodoActual().getLengthWave() || 
                major!=first.getNodoActual().getLengthWave()){
            
            ampl=m*nodeFirts.getAmplitude()/maxP+(1-m)*nodeSecond.getAmplitude()/maxS;
                        
            suma.insertNode(ampl,first.getNodoActual().getLengthWave());
            
            if(ampl>maxM)
                maxM=ampl;
            
            
            nodeFirts=first.getNodo();
            nodeSecond=second.getNodo();
            
        }
        
        return suma;
    
    }
    
    
    public void normalizar(ListData curva){
        
        
        mesopicN=new ListData("Mesopica");
        curva.resetNode();
        NodeData nodeFirst=curva.getNodo();
       
            while(nodeFirst!=curva.getFinalNode()){
                mesopicN.insertNode(nodeFirst.getAmplitude()/maxM, nodeFirst.getLengthWave());
                if(nodeFirst.getLengthWave()==555)
                    mPic=nodeFirst.getAmplitude()/maxM;
                nodeFirst=curva.getNodo();                
            }
    
    
    
    
    
    }
    
    
    }
    
    
    
    
    
    
    
    
    
    
 
    /*
    public ListData getAmplitude(){
    
    
        return photopic;
    
    }
    
     public double getLengthWave(){
    
    
        return lengthWave;
    
    }
    
    */
//}
