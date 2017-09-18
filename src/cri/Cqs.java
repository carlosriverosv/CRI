/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cri;

/**
 *
 * @author andres
 */
public class Cqs {
    
    
    public Lamp referMunsell[]=new Lamp[15];
    public Lamp testMunsell[]=new Lamp[15];
    public Lamp lampaTest;
    public Lamp blackBody;
    private final double cqs[]=new double[15];
    public final double qA;
   // private double XYZt[]=new double[3];
    private double lt;
    private double lr;
    private double sFac;
    private double k;
    
    
    
     Cqs(Lamp lampTest){
         
         lt=1000;
         lr=1000;
         sFac=3.2;
         
         if(0.08*Math.log(lt+lr)+0.76-0.45*(lt-lr)/(lt+lr)>1)
            k=1;
         else
             k=0.08*Math.log(lt+lr)+0.76-0.45*(lt-lr)/(lt+lr);
         
         
         lampaTest=lampTest;
         blackBody = new Lamp(lampTest.CCT);
         
         
         ListMunsell munsell=new ListMunsell(System.getProperty("user.dir")+"/Archivos/MunsellCQS");
         for(int i=0; i<15; i++){
             testMunsell[i]=new Lamp(Multiplicacion(munsell.fMunsell[i],lampTest));
             referMunsell[i]=new Lamp(Multiplicacion(munsell.fMunsell[i],blackBody));
             referMunsell[i].setLab(blackBody.XYZ);        
        
        }
         double xyt[]=new double[3];
         double XYZt[]= adaptCromatica(lampTest);
         
         double XYZm[]=new double[3];
         double xym[]=new double[3];
         double labt[]=new double[3];
         double labm[]=new double[3];
         double deltaE;
         double deltaC;
         double deltaEP;
         
         xyt[0]=XYZt[0]/(XYZt[0]+XYZt[1]+XYZt[2]);
         xyt[1]=XYZt[1]/(XYZt[0]+XYZt[1]+XYZt[2]);
         
         labt[0]=116.0*Math.pow(xyt[1]/xyt[1],1.0/3.0)-16.0;
         labt[1]=500.0*(Math.pow(xyt[0]/xyt[0],1.0/3.0)-Math.pow(xyt[1]/xyt[1],1.0/3.0));
         labt[2]=200.0*(Math.pow(xyt[1]/xyt[1],1.0/3.0)-Math.pow(XYZt[2]/XYZt[2],1.0/3.0));
         
         double sumDeltaE=0.0;
         double sumDeltaEP=0.0;
         
         for (int i=0;i<15;i++){
             
                             
             XYZm=adaptCromatica(testMunsell[i]);

             xym[0]=XYZm[0]/(XYZm[0]+XYZm[1]+XYZm[2]);
             xym[1]=XYZm[1]/(XYZm[0]+XYZm[1]+XYZm[2]);

             labm[0]=116.0*Math.pow(XYZm[1]/XYZt[1],1.0/3.0)-16.0;
             labm[1]=500.0*(Math.pow(XYZm[0]/XYZt[0],1.0/3.0)-Math.pow(XYZm[1]/XYZt[1],1.0/3.0));
             labm[2]=200.0*(Math.pow(XYZm[1]/XYZt[1],1.0/3.0)-Math.pow(XYZm[2]/XYZt[2],1.0/3.0));

             testMunsell[i].setLabP(labm);

             deltaE=Math.sqrt(Math.pow((labm[0]-referMunsell[i].Lab[0]),2.0)+
                   Math.pow((labm[1]-referMunsell[i].Lab[1]),2.0)+
                   Math.pow((labm[2]-referMunsell[i].Lab[2]),2.0));  

             deltaC=Math.sqrt(Math.pow(labm[1], 2)+Math.pow(labm[2], 2.0))-
                   Math.sqrt(Math.pow(referMunsell[i].Lab[1], 2)+Math.pow(referMunsell[i].Lab[2], 2.0));



             if (deltaC<0)
                 deltaC=0;

             if (deltaC>10)
                 deltaC=10;

             deltaEP=Math.sqrt(Math.pow(deltaE, 2)-Math.pow(deltaC, 2));
            
             sumDeltaE=sumDeltaE+deltaE;
             sumDeltaEP=sumDeltaEP+deltaE;




             cqs[i]=10*Math.log(Math.exp((100-sFac*deltaEP)/10)+1);
             System.out.print("\ncqs[]"+i+"="+cqs[i] );
             
             }
         
         
            
         double promDeltaE=sumDeltaE/15;
         double promDeltaEP=sumDeltaEP/15;
         qA=10*Math.log(Math.exp((100-sFac*promDeltaEP)/10)+1);
         
         }
     
     
     
     private ListData Multiplicacion(ListData first2,Lamp lampTest ){
        
        ListData first=first2;
        ListData second=lampTest.spd;
        ListData multiplication=new ListData("Multiplicacion");
        first.resetNode();
        second.resetNode();
    
        double minor;
        double major;
         
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
            
            multiplication.insertNode(nodeFirts.getAmplitude()*nodeSecond.getAmplitude()/lampTest.max, nodeFirts.getLengthWave());
 
            
        int i=0;
        while(major!=second.getNodoActual().getLengthWave()){
            
            nodeFirts=first.getNodo();
            nodeSecond=second.getNodo();
            
            multiplication.insertNode(nodeFirts.getAmplitude()*nodeSecond.getAmplitude()/lampTest.max, nodeFirts.getLengthWave());
 
//            if(i==398)
  //              System.out.println("");
            
    //        i++;
        }
        

        return multiplication;
    
    }
    
       
       
    public double [] MulMatriz(double XYZ[] ){
    
        double RGB[]=new double[3];
        
        RGB[0]=XYZ[0]*0.7982/XYZ[1]+XYZ[1]*0.3389/XYZ[1]-XYZ[2]*(0.137)/XYZ[1];
        RGB[1]=XYZ[1]*1.5512/XYZ[1]-XYZ[0]*0.5918/XYZ[1]+XYZ[2]*(0.0406)/XYZ[1];
        RGB[2]=XYZ[0]*0.0008/XYZ[1]+XYZ[1]*0.0239/XYZ[1]+XYZ[2]*(0.9753)/XYZ[1];

        return RGB;
    
    }
    
    public double [] MulMatrizInv(double XYZ[] ){
    
        double RGB[]=new double[3];
        
        RGB[0]=XYZ[0]*1.076450049-XYZ[1]*0.237662388+XYZ[2]*(0.161212339);
        RGB[1]=XYZ[0]*0.410964325+XYZ[1]*0.554341804+XYZ[2]*(0.03469387);
        RGB[2]=XYZ[2]*1.024343122-XYZ[1]*0.01338936-XYZ[0]*(0.010953765);

        return RGB;
        
    
    }
    
    
    public double [] adaptCromatica(Lamp munsellSample){
        
        double t[]=  MulMatriz(lampaTest.XYZ);
        double b[]=  MulMatriz(blackBody.XYZ);
        double m[]=  MulMatriz(munsellSample.XYZ);
        double p[]=new double[3];
        double xy[]=new double[3];
        double uv[]=new double[3];
        double uvp[]=new double[3];
        
        p[0]=(k*(b[0]/t[0])+1-k)*m[0];
        p[1]=(k*(b[1]/t[1])+1-k)*m[1];
        p[2]=(k*(b[2]/t[2])+1-k)*m[2];
        
        p[0]=p[0]*munsellSample.XYZ[1];
        p[1]=p[1]*munsellSample.XYZ[1];
        p[2]=p[2]*munsellSample.XYZ[1];

        double XYZt []=MulMatrizInv(p);
        
        xy[0]=XYZt[0]/(XYZt[0]+XYZt[1]+XYZt[2]);
        xy[1]=XYZt[1]/(XYZt[0]+XYZt[1]+XYZt[2]);
        uv[0]=4*XYZt[0]/(XYZt[0]+15*XYZt[1]+3*XYZt[2]);
        uv[1]=6*XYZt[0]/(XYZt[0]+15*XYZt[1]+3*XYZt[2]);
        uvp[0]=uv[0];
        uvp[1]=9*XYZt[0]/(XYZt[0]+15*XYZt[1]+3*XYZt[2]);
        
        return XYZt;
    
    }

    
    
    
    
    
     public double[] getCqs(){
 
        return cqs;
    
    }
    
}
