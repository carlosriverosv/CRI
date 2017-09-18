/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dialog;

import cri.*;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author andres
 */
public class EspectroMedido extends javax.swing.JDialog {
    
    
    
     public File spdMedido=null;
     
     public DefaultTableModel dtm4=new DefaultTableModel();
     Lamp lamp;
    

    /**
     * Creates new form EspectroMedido
     */
    public EspectroMedido(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        tablaFuente();
    }
    
    public File getSpdMedido(){
    
    
        return spdMedido;
    
    }
    
    
     public void tablaFuente(){
    
    
        dtm4.addColumn("Nombre");
        dtm4.addColumn("Tensión[V]");
        dtm4.addColumn("Corriente[I]");
        dtm4.addColumn("Potencia[W]");
     //   dtm4.addColumn("Flujo[lm]");
        

           
        Object[] newRow={"","0.0","0.0","0.0"};
        dtm4.addRow(newRow);
        
       
    
    
    
    
    }
     
     public void verEspectro(File spd ){
         
          XYSeries psdSerie=new XYSeries("Medido");
     try
            {
                
                
                Scanner salida = new Scanner(spd);
                
                try // lee registros del archivo, usando el objeto Scanner
                {
                    double lamb1=0;
                    String inter;
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
                        psdSerie.add(lamb1,ampl1);//ingresa el primer valor si es entero
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
                        psdSerie.add(lamb2,ampl2);
                        lambR=lambR+1;
                    }                      
                    else if(lamb2>lambR){// if the next value is major to value requerided, then calculate and save
                            psdSerie.add(lambR,(ampl2-ampl1)/(lamb2-lamb1)*(lambR-lamb1)+ampl1);
                            lambR=lambR+1;
                        }
      
                } // fin de while
                
                } // fin de try
                catch ( NoSuchElementException elementException )
                {
                    JDialog error=  new ErrorArchivo(Lux.getFrames()[0],true);
                   error.setLocationRelativeTo(Lux.getFrames()[0]);
                   error.setVisible(true);
               //     salida.close();
               //     System.exit( 1 );
                } // fin de catch
            catch ( IllegalStateException stateException )
            {
                   JDialog error=  new ErrorLectura(Lux.getFrames()[0],true);
                   error.setLocationRelativeTo(Lux.getFrames()[0]);
                   error.setVisible(true);
            } // fin de catch*/
        } // fin de try
        catch ( FileNotFoundException fileNotFoundException )
            {
                   JDialog error=  new ErrorOpen(Lux.getFrames()[0],true);
                   error.setLocationRelativeTo(Lux.getFrames()[0]);
                   error.setVisible(true);
            } // fin de catch
          
     
     XYSeriesCollection dataset = new XYSeriesCollection();
     dataset.addSeries(psdSerie);
    
     JFreeChart spdC = ChartFactory.createXYLineChart(
        "SPD", // Title
        "Wav", // x-axis Label
        "Amplitud", // y-axis Label
        dataset, // Dataset
        PlotOrientation.VERTICAL, // Plot Orientation
        true, // Show Legend
        true, // Use tooltips
        false // Configure chart to generate URLs?
        );
        
        spdC.setBackgroundPaint(Color.white);
        
        XYPlot plot = (XYPlot) spdC.getPlot();
        //plot.getRenderer().setSeriesStroke(2, new BasicStroke(4.0f));
    //    plot.getRenderer().setSeriesPaint(1, Color.RED);
    //    plot.getRenderer().setSeriesPaint(2, Color.RED);
    //    plot.getRenderer().setSeriesPaint(3, Color.RED);

        
        
      //  XYItemRenderer xyitemrenderer = plot.getRenderer( ); 
    //  xyitemrenderer.set
        


//plot.get
        
        /*XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(1, true);
        
        */
        
        plot.getRenderer().setSeriesPaint(0, Color.BLACK);
        plot.setBackgroundPaint(Color.white);
        
        plot.setDomainGridlinePaint(Color.CYAN);
        plot.setOutlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.CYAN);
        
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        
        
        jPanel1.removeAll();
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(new ChartPanel(spdC));
        jPanel1.validate();
        
     
     
     
     }
     
     
     public static void copyFile(File sourceFile, File destFile) throws IOException {
        
    if(!destFile.exists()) {
        destFile.createNewFile();
    }
 
    FileChannel origen = null;
    FileChannel destino = null;
    try {
        origen = new FileInputStream(sourceFile).getChannel();
        destino = new FileOutputStream(destFile).getChannel();
 
        long count = 0;
        long size = origen.size();              
        while((count += destino.transferFrom(origen, count, size-count))<size);
    }
    finally {
        if(origen != null) {
            origen.close();
        }
        if(destino != null) {
            destino.close();
        }
    }
    
    }
    
     
     
    public void escribirArchivo(String nombre){
        
       // Datos.actualNode=null;
       // NodeData data=Datos.getNodo();
        
        
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(System.getProperty("user.dir")+"/Calibracion/Tabla_"+nombre);
       //     FileWriter fichero = new FileWriter("c:/prueba.txt",true);
            pw = new PrintWriter(fichero);
            Vector datos=(Vector)dtm4.getDataVector().elementAt(0);
         //    while(data!=null){
             pw.println("nombreSpd"+"\t"+datos.elementAt(0).toString());
             pw.println("Tensión"+"\t"+Double.parseDouble(datos.elementAt(1).toString()));
             pw.println("Corriente"+"\t"+Double.parseDouble(datos.elementAt(2).toString()));
             pw.println("Potencia"+"\t"+Double.parseDouble(datos.elementAt(3).toString()));
         //   psdSerie.add(data.getLengthWave(),data.getAmplitude());
          //  data=Datos.getNodo();
        
     //   }       
 
        } catch (Exception e) {
            
            
            e.printStackTrace();
            
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setText("Seleccionar Espectro Medido:");

        jButton4.setLabel("Espectro Medido");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel3.setText("Condiciones Electricas");

        jTable1.setModel(dtm4);
        jScrollPane1.setViewportView(jTable1);

        jButton5.setLabel("Finalizar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 252, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 22, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton5))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        JFileChooser fc = new JFileChooser();
        int respuesta = fc.showOpenDialog(this);
        if (respuesta == JFileChooser.APPROVE_OPTION)
        {
            
            
           verEspectro(fc.getSelectedFile());
           spdMedido=fc.getSelectedFile();
            

        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:

        if(spdMedido!=null){
        
        boolean si=(((Vector)dtm4.getDataVector().elementAt(0)).elementAt(0).toString().isEmpty());
        if(si){
            JDialog errorNombre=new ErrorNombre(Lux.getFrames()[0],true);
            errorNombre.setResizable(false);
            errorNombre.setLocationRelativeTo(Lux.getFrames()[0]);
            errorNombre.setVisible(true);
           
            
            
        }
        else{
            this.dispose();
            
        escribirArchivo(((Vector)dtm4.getDataVector().elementAt(0)).elementAt(0).toString());
        try {
                copyFile(spdMedido,new File(System.getProperty("user.dir")+"/Calibracion/"+((Vector)dtm4.getDataVector().elementAt(0)).elementAt(0).toString()));
            } catch (IOException ex) {
                JDialog eImport=new ErrorImportando(Lux.getFrames()[0],true);
                eImport.setLocationRelativeTo(Lux.getFrames()[0]);
                eImport.setVisible(true);
            }catch(NullPointerException n){
                System.out.print("No se eligió ningun Archivo");
            }
        
       
        
        }
        }
        else
        this.dispose();

    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EspectroMedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EspectroMedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EspectroMedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EspectroMedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EspectroMedido dialog = new EspectroMedido(new javax.swing.JFrame(), true);
                
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
