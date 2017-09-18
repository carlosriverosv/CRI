/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cri;

/**
 *
 * @author BALUM ETB3
 */
public class Crit {
    
    
    public Lamp referMunsell[]=new Lamp[15];
    public Lamp testMunsell[]=new Lamp[15];
    public Lamp lampaTest;
    public Lamp blackBody;
    private final double cri[]=new double[15];
    private final double Ra;
    
    
    
    Crit(Lamp lampTest){
        
        
        lampaTest=lampTest;
        blackBody = new Lamp(lampTest.CCT);
        
        ListMunsell munsell=new ListMunsell(System.getProperty("user.dir")+"/Archivos/MunsellCRI");
      
        for(int i=0; i<14; i++){   
            testMunsell[i]=new Lamp(Multiplicacion(munsell.fMunsell[i],lampTest));
            referMunsell[i]=new Lamp(Multiplicacion(munsell.fMunsell[i],blackBody));
        
        }
        
      double c_t=(4.0-lampTest.uvY[0]-10.0*lampTest.uvY[1])/lampTest.uvY[1];
      double d_t=(1.708*lampTest.uvY[1]-1.481*lampTest.uvY[0]+0.404)/lampTest.uvY[1];


      double c_r=(4.0-blackBody.uvY[0]-10.0*blackBody.uvY[1])/blackBody.uvY[1];
      double d_r=(1.708*blackBody.uvY[1]-1.481*blackBody.uvY[0]+0.404)/blackBody.uvY[1];

      double cp_t,dp_t,up_t_cat,vp_t_cat;
      double Wrstar[]= new double[15],Urstar[]=new double[15],Vrstar[]=new double[15]
              ,Wtstar[]=new double[15],Utstar[]=new double[15],Vtstar[]=new double[15],deltaE;

    double CRa=0;
    for (int i=0;i<14;i++){
      cp_t=(4.0-testMunsell[i].uvY[0]-10.0*testMunsell[i].uvY[1])/testMunsell[i].uvY[1];
      dp_t=(1.708*testMunsell[i].uvY[1]-1.481*testMunsell[i].uvY[0]+0.404)/testMunsell[i].uvY[1];

      up_t_cat=(10.872+0.404*c_r/c_t*cp_t-4*d_r/d_t*dp_t)/(16.518+1.481*c_r/c_t*cp_t-d_r/d_t*dp_t);
      vp_t_cat=5.520/(16.518+1.481*c_r/c_t*cp_t-d_r/d_t*dp_t);

      Wrstar[i]=25.0*Math.pow(referMunsell[i].XYZ[1]*100/blackBody.XYZ[1],(1.0/3.0))-17;
      Urstar[i]=13*Wrstar[i]*(referMunsell[i].uvY[0]-blackBody.uvY[0]);
      Vrstar[i]=13*Wrstar[i]*(referMunsell[i].uvY[1]-blackBody.uvY[1]);
      Wtstar[i]=25*Math.pow(testMunsell[i].XYZ[1]*100/lampTest.XYZ[1],(1.0/3.0))-17;
      Utstar[i]=13*Wtstar[i]*(up_t_cat-blackBody.uvY[0]);
      Vtstar[i]=13*Wtstar[i]*(vp_t_cat-blackBody.uvY[1]);
     // printf("a=%g\t b=%g\t Up=%g\t C=%g\n",Wrstar[i],Yuvr[i][2],Vrstar[i],Wtstar[i]);
      deltaE=Math.sqrt(Math.pow((Urstar[i]-Utstar[i]),2)+Math.pow((Vrstar[i]-Vtstar[i]),2)+Math.pow((Wrstar[i]-Wtstar[i]),2));
      cri[i]=100-4.6*deltaE;
      if(i<8)
      CRa=cri[i]+CRa;

}

    Ra=CRa/8;
    cri[14]=Ra;


     
        
    }
    
    private ListData Multiplicacion(ListData first,Lamp lampTest ){
        
        
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
    
    
    public void imprimir(){
    
        for(int i=0;i<15;i++){


              System.out.println("\n Cri_"+i+"="+cri[i]);

        }
        System.out.println(Ra);

    
    
    
    }
    
    public double[] getCri(){
 
        return cri;
    
    }
}
