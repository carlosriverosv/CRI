/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cri;

import Controlador.Conexion;
import Controlador.Observable;
import Controlador.Observer;
import Controlador.Resources;
import Controlador.Spectrometer;
import Controlador.TreeSpec;
import Controlador.USBSpectrometer;
import dialog.*;
import dialog.SobreAlux;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
/**
 *
 * @author BALUM ETB3
 */
public class Lux extends javax.swing.JFrame implements Observer{
    
    DefaultMutableTreeNode USBSPec;

    @Override
    public void update(Observable subject) {
        if (((TreeSpec)subject).getSelectedNode().getParent().toString().equals(TreeSpec.FILES)) {
            
            if (selectedSpec != null) {
                selectedSpec.stop();
            }
            cargarDatosLampara(path + "/Lamparas/" + ((TreeSpec)subject).getSelectedNode().toString());
            jTable2.setVisible(true);
            jTable6.setVisible(true);
            jTabbedPane1.setEnabledAt(1, true);
            jTabbedPane1.setEnabledAt(2, true);
            pnlSpecControl.setVisible(false);
            pnlChkFileSpec.setVisible(true);
            loadPanelSpecInfo(false);
        } else {
            selectedSpec = ((TreeSpec)subject).getSelectedSpec();
            loadSpecData(selectedSpec);
            loadPanelSpecInfo(true);
            selectedSpec.start();
            jTable2.setVisible(false);
            jTable6.setVisible(false);
            jTabbedPane1.setEnabledAt(1, false);
            jTabbedPane1.setEnabledAt(2, false);
            pnlChkFileSpec.setVisible(false);
            pnlSpecControl.setVisible(true);
        }
    }

     class CustomRenderer extends BarRenderer {

        /** The colors. */
        private Paint[] colors;

        /**
         * Creates a new renderer.
         *
         * @param colors  the colors.
         */
        public CustomRenderer(final Paint[] colors) {
            this.colors = colors;
        }

        /**
         * Returns the paint for an item.  Overrides the default behaviour inherited from
         * AbstractSeriesRenderer.
         *
         * @param row  the series.
         * @param column  the category.
         *
         * @return The item color.
         */
        @Override
        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    }

    Lamp lampTest;
    JFreeChart spd;
    JFreeChart cri;
    JFreeChart cqs;
    ListTries tries;
    Crit rCri;
    Cqs rCqs;
    Dialogo1 dia1;
    EspectroMedido dia2;
    Calibration calibrar;
    String path=System.getProperty("user.dir");
    DefaultTableModel dtm=new DefaultTableModel();
    DefaultTableModel dtm2=new DefaultTableModel();
    DefaultTableModel dtm3=new DefaultTableModel();
    DefaultTableModel dtm4=new DefaultTableModel();
    DefaultTableModel dtmCordenadasCri= new DefaultTableModel();
    DefaultTableModel dtmCordenadasCqs= new DefaultTableModel();
    NodeMetric metric;
    Conexion conexion;
    Spectrometer selectedSpec = null;
    GregorianCalendar gCalendar;
    

    public Lux() { 
        conexion=new Conexion();
        conexion.crearBD();
        
        metric=new NodeMetric(path);
        crearTablas();
        validarWorkLamparas();

        tries =new ListTries(path+"/Archivos/Triestimulus");
        //spectrometer1 = new SpectrometerTest1(this);
        //spectrometer1.run(); 
        Spectrometer.setLux(this);
        initComponents();
       pnlChkFileSpec.setVisible(false);
       pnlSpecControl.setVisible(false);
       loadPanelSpecInfo(false);
       
       jTable2.setVisible(false);
       jTable6.setVisible(false);
       jTabbedPane1.setEnabledAt(1, false);
       jTabbedPane1.setEnabledAt(2, false);
       
       gCalendar = new GregorianCalendar();
        
        jTable6.getModel().addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {
         boolean si=(Double.parseDouble(((Vector)dtm4.getDataVector().elementAt(0)).elementAt(1).toString())!=metric.lP);
         if(si){
             cargarMesopica();
             cargarTablaMesopica();
             CreateSpd(lampTest.spd);
         }
      }
    });
        agregarEventosMenu();
    }
    
    public void loadSpecData(Spectrometer selectedSpec) {
        txtBoxcarWidth.setText(String.valueOf(selectedSpec.getBoxCarWidth()));
        txtScansToAverage.setText(String.valueOf(selectedSpec.getScansToAverage()));
        txtIntegrationTime.setText(String.valueOf(selectedSpec.getIntegrationTime()));
        jchkElecDarkCorr.setSelected(selectedSpec.isElectricalDarkCorr());
    }
    
    public void loadPanelSpecInfo(boolean load) {
        if(load) { 
            lblNameSpec.setText(selectedSpec.getName());
            System.out.println(selectedSpec.getName());
            lblSerialNumberSpec.setText(selectedSpec.getSerialNumber());
            lblFirmwareSpec.setText(selectedSpec.getFirmware());
            lblChannelsSpec.setText(String.valueOf(selectedSpec.getChannels()));
        }
        pnlSpecInfo.setVisible(load);
    }
    
    public void setTemperature(String tempValue) {
        lblTemperatureSpec.setText(tempValue);
    }
        
    public void cargarMesopica(){
        
    String lP=((Vector)dtm4.getDataVector().elementAt(0)).elementAt(1).toString();
    System.out.print("\nLPPPP"+lP);
    metric.setParametros(Double.parseDouble(lP));
    
    metric.curvaMesopica(lampTest.spd, lampTest.max);
    
}

    public void crearTablas(){
    
    
        dtm3.addColumn("Parameters");
        dtm3.addColumn("Values");
        
        dtm4.addColumn("Parameters");
        dtm4.addColumn("Values");
        
        dtmCordenadasCqs.addColumn("Parameters");
        dtmCordenadasCqs.addColumn("[X Y Z]");
        dtmCordenadasCqs.addColumn("[x y z]");
        dtmCordenadasCqs.addColumn("[U V Y]");
        dtmCordenadasCqs.addColumn("[r g b]");
        
        dtmCordenadasCri.addColumn("Parameters");
        dtmCordenadasCri.addColumn("[X Y Z]");
        dtmCordenadasCri.addColumn("[x y z]");
        dtmCordenadasCri.addColumn("[U V Y]");
        dtmCordenadasCri.addColumn("[r g b]");
        
        dtm2.addColumn("Parameters");
        dtm2.addColumn("Values");
        
        dtm.addColumn("Parameters");
        dtm.addColumn("Values");
        
        
        Object[] newRow={"Lp",1.0};
        dtm4.addRow(newRow);
        
        
        
        
    
    
    
    
    }
     public void cargarDatosLampara(String ruta){
      //lampTest=new Lamp
        lampTest=new Lamp(ruta);
        rCri=new Crit(lampTest);
        rCqs=new Cqs(lampTest);
        
        cargarMesopica();
        // System.out.print("\nsP="+metric.lS+"\tLs"+metric.lMes+"\tLp:"+metric.lP);
        cargarTablaMesopica();
        if(!jCheckBox1.isSelected() && !jCheckBox2.isSelected() )
            jCheckBox1.setSelected(true);
        
        if(!jRadioButton7.isSelected() && !jRadioButton8.isSelected() && !jRadioButton1.isSelected())
            jRadioButton7.setSelected(true);
        
         if(!jRadioButton4.isSelected() && !jRadioButton5.isSelected() )
            jRadioButton4.setSelected(true);
        
        CreateSpd(lampTest.spd);
        
        if(jRadioButton7.isSelected())
             CreateCri(rCri.getCri());
        else if(jRadioButton1.isSelected())
            crearDCromaticidadUV();
        else if(jRadioButton8.isSelected()) 
            crearDCromaticidadXY();
        
        if(jRadioButton4.isSelected())
            CreateCqs(rCqs.getCqs());
        else if(jRadioButton5.isSelected())
            crearDCromaticidadLab();
        
        ingresarDatosTablaCri(rCri.getCri());
        ingresarDatosTablaCqs(rCqs.getCqs());
        
        IngresarDatosCoordenadasCri();
        IngresarDatosCoordenadasCqs();
        
        cargarTablaColores();
        cargarTablaInicial();
       
        
        cargarTablaMesopica();
        
        
    }
    
    
//    public void cargarDatosLampara(Lamp lampTest){
//      //lampTest=new Lamp
//        //lampTest=new Lamp(ruta);
//        rCri=new Crit(lampTest);
//        rCqs=new Cqs(lampTest);
//        
//        cargarMesopica();
//        // System.out.print("\nsP="+metric.lS+"\tLs"+metric.lMes+"\tLp:"+metric.lP);
//        cargarTablaMesopica();
//        if(!jCheckBox1.isSelected() && !jCheckBox2.isSelected() )
//            jCheckBox1.setSelected(true);
//        
//        if(!jRadioButton7.isSelected() && !jRadioButton8.isSelected() && !jRadioButton1.isSelected())
//            jRadioButton7.setSelected(true);
//        
//         if(!jRadioButton4.isSelected() && !jRadioButton5.isSelected() )
//            jRadioButton4.setSelected(true);
//        
//        CreateSpd(lampTest.spd);
//        
//        if(jRadioButton7.isSelected())
//             CreateCri(rCri.getCri());
//        else if(jRadioButton1.isSelected())
//            crearDCromaticidadUV();
//        else if(jRadioButton8.isSelected()) 
//            crearDCromaticidadXY();
//        
//        if(jRadioButton4.isSelected())
//            CreateCqs(rCqs.getCqs());
//        else if(jRadioButton5.isSelected())
//            crearDCromaticidadLab();
//        
//        ingresarDatosTablaCri(rCri.getCri());
//        ingresarDatosTablaCqs(rCqs.getCqs());
//        
//        IngresarDatosCoordenadasCri();
//        IngresarDatosCoordenadasCqs();
//        
//        cargarTablaColores();
//        cargarTablaInicial();
//       
//        
//        cargarTablaMesopica();
//        
//        
//    }
     
    
     
     public void cargarTablaInicial(){
     
                        
      //  dtm3=new DefaultTableModel();
        
       while(dtm3.getRowCount()!=0)
           dtm3.removeRow(0);

        Object[] newRow={"CCT",lampTest.CCT};
        dtm3.addRow(newRow);
        
        Object[] newRow1={"Ra",rCri.getCri()[14]};
        dtm3.addRow(newRow1);
        
        Object[] newRow2={"Qa",rCqs.qA};
        dtm3.addRow(newRow2);
        
        Object[] newRow3={"flujo",lampTest.XYZ[0]};
        dtm3.addRow(newRow3);
    }
     
     
     public void cargarTablaMesopica(){
        
      //  dtm4= new DefaultTableModel();
        
         
      //   int i=0;
         while(dtm4.getRowCount()!=1 && dtm4.getRowCount()!=0 ){
         
         //    if(((Vector)dtm4.getDataVector(i).elementAt(0)).elementAt(0).toString()=="Lp"){
          //   System.out.print(dtm4.getDataVector().elementAt(1));
                 dtm4.removeRow(1);
         }
                 
         
                      
         
         
        // System.out.print(dtm4.toString());
           
        
        Object[] newRow1={"Ls",metric.lS};
        dtm4.addRow(newRow1);
        
        Object[] newRow2={"Lmes",metric.lMes};
        dtm4.addRow(newRow2);
        
        Object[] newRow3={"S/p",metric.sP};
        dtm4.addRow(newRow3);
        Object[] newRow4={"m",metric.m};
        dtm4.addRow(newRow4);

     }
    
    public void agregarEventosMenu(){
    
          ManejadorEventoMenu manejadorMenu=new ManejadorEventoMenu();
          //ManejadorEventoGrafico manejadorButton = new ManejadorEventoGrafico();
          jMenuItem11.addActionListener(manejadorMenu);
          jMenuItem3.addActionListener(manejadorMenu);

          jMenuItem8.addActionListener(manejadorMenu);
          jMenuItem12.addActionListener(manejadorMenu);

          jMenuItem1.addActionListener(manejadorMenu);

          
    
    }
    
    public void validarWorkLamparas(){
    
        
        File folder = new File(path+"/Lamparas");
        
        if (!folder.exists()) {
             File workSpace=new File(path);
             workSpace.mkdirs();
      
        }
            
    }
    
    
    
    private File obtenerSpd(){
    
        JFileChooser fc = new JFileChooser();
        int respuesta = fc.showOpenDialog(this);
        File archivoElegido=null;
        if (respuesta == JFileChooser.APPROVE_OPTION)
        {
            archivoElegido = fc.getSelectedFile();
       
        }
        
        return archivoElegido;
    
    }
    
    
    
    
    private void cargarTablaColores(){
    
        jPanel7.getComponent(0).setBackground(new java.awt.Color(rCri.blackBody.rgb[0],rCri.blackBody.rgb[1],rCri.blackBody.rgb[2]));
        jLabel17.setBackground(new java.awt.Color(lampTest.rgb[0],lampTest.rgb[1],lampTest.rgb[2]));
        jLabel114.setBackground(new java.awt.Color(rCri.blackBody.rgb[0],rCri.blackBody.rgb[1],rCri.blackBody.rgb[2]));
        jLabel129.setBackground(new java.awt.Color(lampTest.rgb[0],lampTest.rgb[1],lampTest.rgb[2]));
        for(int i=0;i<15;i++){
            jPanel7.getComponent(i+1).setBackground(new java.awt.Color(rCqs.referMunsell[i].rgb[0],rCqs.referMunsell[i].rgb[1],rCqs.referMunsell[i].rgb[2]));
            jPanel7.getComponent(i+17).setBackground(new java.awt.Color(rCqs.testMunsell[i].rgb[0],rCqs.testMunsell[i].rgb[1],rCqs.testMunsell[i].rgb[2]));
        }
        for(int i=0;i<14;i++){
            jPanel27.getComponent(i+1).setBackground(new java.awt.Color(rCri.referMunsell[i].rgb[0],rCri.referMunsell[i].rgb[1],rCri.referMunsell[i].rgb[2]));
            jPanel27.getComponent(i+16).setBackground(new java.awt.Color(rCri.testMunsell[i].rgb[0],rCri.testMunsell[i].rgb[1],rCri.testMunsell[i].rgb[2]));
        }
    
    }
    
    
    private void IngresarDatosCoordenadasCqs(){
        
        while(dtmCordenadasCqs.getRowCount()!=0)
            dtmCordenadasCqs.removeRow(0);

        Object[] newrow2={"Referencia","[" +String.format("%.2f", rCqs.blackBody.XYZ[0] )+" "+String.format("%.2f",rCqs.blackBody.XYZ[1])+" "+String.format("%.2f",rCqs.blackBody.XYZ[2])+"]",
               "[" +String.format("%.2f", rCqs.blackBody.xyz[0] )+" "+String.format("%.2f",rCqs.blackBody.xyz[1])+" "+String.format("%.2f",rCqs.blackBody.xyz[2])+"]",
               "[" +String.format("%.2f", rCqs.blackBody.uvY[0] )+" "+String.format("%.2f",rCqs.blackBody.uvY[1])+" "+String.format("%.2f",rCqs.blackBody.uvY[2])+"]",
               "[" +String.format("%.2f", rCqs.blackBody.rgb[0] )+" "+String.format("%.2f",rCqs.blackBody.rgb[1])+" "+String.format("%.2f",rCqs.blackBody.rgb[2])+"]"
        };
        
        dtmCordenadasCqs.addRow(newrow2);
        for(int i=0;i<14;i++){
               Object[] newrow={"Ref-VS"+(1+i),"[" +String.format("%.2f", rCqs.referMunsell[i].XYZ[0] )+" "+String.format("%.2f",rCqs.referMunsell[i].XYZ[1])+" "+String.format("%.2f",rCqs.referMunsell[i].XYZ[2])+"]",
               "[" +String.format("%.2f", rCqs.referMunsell[i].xyz[0] )+" "+String.format("%.2f",rCqs.referMunsell[i].xyz[1])+" "+String.format("%.2f",rCqs.referMunsell[i].xyz[2])+"]",
               "[" +String.format("%.2f", rCqs.referMunsell[i].uvY[0] )+" "+String.format("%.2f",rCqs.referMunsell[i].uvY[1])+" "+String.format("%.2f",rCqs.referMunsell[i].uvY[2])+"]",
               "[" +String.format("%.2f", rCqs.referMunsell[i].rgb[0] )+" "+String.format("%.2f",rCqs.referMunsell[i].rgb[1])+" "+String.format("%.2f",rCqs.referMunsell[i].rgb[2])+"]"
                };
               dtmCordenadasCqs.addRow(newrow);
        }
        
         Object[] newrow3={"Test","[" +String.format("%.2f", rCqs.lampaTest.XYZ[0] )+" "+String.format("%.2f",rCqs.lampaTest.XYZ[1])+" "+String.format("%.2f",rCqs.lampaTest.XYZ[2])+"]",
               "[" +String.format("%.2f", rCqs.lampaTest.xyz[0] )+" "+String.format("%.2f",rCqs.lampaTest.xyz[1])+" "+String.format("%.2f",rCqs.lampaTest.xyz[2])+"]",
               "[" +String.format("%.2f", rCqs.lampaTest.uvY[0] )+" "+String.format("%.2f",rCqs.lampaTest.uvY[1])+" "+String.format("%.2f",rCqs.lampaTest.uvY[2])+"]",
               "[" +String.format("%.2f", rCqs.lampaTest.rgb[0] )+" "+String.format("%.2f",rCqs.lampaTest.rgb[1])+" "+String.format("%.2f",rCqs.lampaTest.rgb[2])+"]"
        };
        dtmCordenadasCqs.addRow(newrow3);
        
        for(int i=0;i<14;i++){
               Object[] newrow={"Test-VS"+(1+i),"[" +String.format("%.2f", rCqs.referMunsell[i].XYZ[0] )+" "+String.format("%.2f",rCqs.referMunsell[i].XYZ[1])+" "+String.format("%.2f",rCqs.referMunsell[i].XYZ[2])+"]",
               "[" +String.format("%.2f", rCqs.testMunsell[i].xyz[0] )+" "+String.format("%.2f",rCqs.testMunsell[i].xyz[1])+" "+String.format("%.2f",rCqs.testMunsell[i].xyz[2])+"]",
               "[" +String.format("%.2f", rCqs.testMunsell[i].uvY[0] )+" "+String.format("%.2f",rCqs.testMunsell[i].uvY[1])+" "+String.format("%.2f",rCqs.testMunsell[i].uvY[2])+"]",
               "[" +String.format("%.2f", rCqs.testMunsell[i].rgb[0] )+" "+String.format("%.2f",rCqs.testMunsell[i].rgb[1])+" "+String.format("%.2f",rCqs.testMunsell[i].rgb[2])+"]"
               
        };
               dtmCordenadasCqs.addRow(newrow);
        }
        
        jTable3.getColumnModel().getColumn(0).setPreferredWidth(90);
        jTable3.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTable3.getColumnModel().getColumn(2).setPreferredWidth(150);
        jTable3.getColumnModel().getColumn(3).setPreferredWidth(150);
        jTable3.getColumnModel().getColumn(4).setPreferredWidth(150);
        
    }
    
    
    private void IngresarDatosCoordenadasCri(){
    
        while(dtmCordenadasCri.getRowCount()!=0)
           dtmCordenadasCri.removeRow(0);
        
        Object[] newrow2={"Referencia","[" +String.format("%.2f", rCri.blackBody.XYZ[0] )+" "+String.format("%.2f",rCri.blackBody.XYZ[1])+" "+String.format("%.2f",rCri.blackBody.XYZ[2])+"]",
               "[" +String.format("%.2f", rCri.blackBody.xyz[0] )+" "+String.format("%.2f",rCri.blackBody.xyz[1])+" "+String.format("%.2f",rCri.blackBody.xyz[2])+"]",
               "[" +String.format("%.2f", rCri.blackBody.uvY[0] )+" "+String.format("%.2f",rCri.blackBody.uvY[1])+" "+String.format("%.2f",rCri.blackBody.uvY[2])+"]",
               "[" +String.format("%.2f", rCri.blackBody.rgb[0] )+" "+String.format("%.2f",rCri.blackBody.rgb[1])+" "+String.format("%.2f",rCri.blackBody.rgb[2])+"]"
               
        };
        dtmCordenadasCri.addRow(newrow2);
        for(int i=0;i<14;i++){
               Object[] newrow={"Ref-TCS"+(1+i),"[" +String.format("%.2f", rCri.referMunsell[i].XYZ[0] )+" "+String.format("%.2f",rCri.referMunsell[i].XYZ[1])+" "+String.format("%.2f",rCri.referMunsell[i].XYZ[2])+"]",
               "[" +String.format("%.2f", rCri.referMunsell[i].xyz[0] )+" "+String.format("%.2f",rCri.referMunsell[i].xyz[1])+" "+String.format("%.2f",rCri.referMunsell[i].xyz[2])+"]",
               "[" +String.format("%.2f", rCri.referMunsell[i].uvY[0] )+" "+String.format("%.2f",rCri.referMunsell[i].uvY[1])+" "+String.format("%.2f",rCri.referMunsell[i].uvY[2])+"]",
               "[" +String.format("%.2f", rCri.referMunsell[i].rgb[0] )+" "+String.format("%.2f",rCri.referMunsell[i].rgb[1])+" "+String.format("%.2f",rCri.referMunsell[i].rgb[2])+"]"
               
        };
               dtmCordenadasCri.addRow(newrow);
        }
        
         Object[] newrow3={"Test","[" +String.format("%.2f", rCri.lampaTest.XYZ[0] )+" "+String.format("%.2f",rCri.lampaTest.XYZ[1])+" "+String.format("%.2f",rCri.lampaTest.XYZ[2])+"]",
               "[" +String.format("%.2f", rCri.lampaTest.xyz[0] )+" "+String.format("%.2f",rCri.lampaTest.xyz[1])+" "+String.format("%.2f",rCri.lampaTest.xyz[2])+"]",
               "[" +String.format("%.2f", rCri.lampaTest.uvY[0] )+" "+String.format("%.2f",rCri.lampaTest.uvY[1])+" "+String.format("%.2f",rCri.lampaTest.uvY[2])+"]",
               "[" +String.format("%.2f", rCri.lampaTest.rgb[0] )+" "+String.format("%.2f",rCri.lampaTest.rgb[1])+" "+String.format("%.2f",rCri.lampaTest.rgb[2])+"]"
               
        };
        dtmCordenadasCri.addRow(newrow3);
        
        for(int i=0;i<14;i++){
               Object[] newrow={"Test-TCS"+(1+i),"[" +String.format("%.2f", rCri.referMunsell[i].XYZ[0] )+" "+String.format("%.2f",rCri.referMunsell[i].XYZ[1])+" "+String.format("%.2f",rCri.referMunsell[i].XYZ[2])+"]",
               "[" +String.format("%.2f", rCri.testMunsell[i].xyz[0] )+" "+String.format("%.2f",rCri.testMunsell[i].xyz[1])+" "+String.format("%.2f",rCri.testMunsell[i].xyz[2])+"]",
               "[" +String.format("%.2f", rCri.testMunsell[i].uvY[0] )+" "+String.format("%.2f",rCri.testMunsell[i].uvY[1])+" "+String.format("%.2f",rCri.testMunsell[i].uvY[2])+"]",
               "[" +String.format("%.2f", rCri.testMunsell[i].rgb[0] )+" "+String.format("%.2f",rCri.testMunsell[i].rgb[1])+" "+String.format("%.2f",rCri.testMunsell[i].rgb[2])+"]"
               
        };
               dtmCordenadasCri.addRow(newrow);
        }
        
        jTable8.getColumnModel().getColumn(0).setPreferredWidth(90);
        jTable8.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTable8.getColumnModel().getColumn(2).setPreferredWidth(150);
        jTable8.getColumnModel().getColumn(3).setPreferredWidth(150);
        jTable8.getColumnModel().getColumn(4).setPreferredWidth(150);
    }
    
    private void ingresarDatosTablaCri(double Cri[]){
        
        while(dtm.getRowCount()!=0)
            dtm.removeRow(0);
  
        for(int i=0;i<Cri.length-1;i++){
               Object[] newrow={"CRI"+(1+i),Cri[i]};
               dtm.addRow(newrow);
            }
        Object[] newrow={"Ra",Cri[14]};
               dtm.addRow(newrow);
    }

    
    private void ingresarDatosTablaCqs(double Cqs[]){
        
        while(dtm2.getRowCount()!=0)
           dtm2.removeRow(0);
        
        for(int i=0;i<Cqs.length;i++){
            Object[] newrow={"Cqs"+(1+i),Cqs[i]};
            dtm2.addRow(newrow);
        }
        Object[] newRow={"Qa",rCqs.qA};
        dtm2.addRow(newRow);
    }
    
    
    private void CreateCri(double Cri[]){
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
        for(int i = 0 ; i < Cri.length-1 ; i++){
           dataset.addValue(Cri[i],"S1","TC"+(i+1));     
        }
        
        
            
         dataset.addValue(0,"S1","");   
         dataset.addValue(Cri[14],"S1","Ra");
         cri= ChartFactory.createBarChart(
        "Cri",
        // chart title
        "Category",
        // domain axis label
        "Value",
        // range axis label
        dataset,
        // data
        PlotOrientation.VERTICAL, // orientation
        false,
        // include legend
        true,
        // tooltips?
        false
        // URLs?
        );
    
        cri.setBackgroundPaint(Color.white);
        CategoryPlot plot = cri.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        Color colores[]=new Color [15];

        for (int i=0;i<colores.length-1;i++){

            colores[i]=new java.awt.Color((float)rCri.referMunsell[i].rgb[0],
                    (float)rCri.referMunsell[i].rgb[1],(float)rCri.referMunsell[i].rgb[2]);

        }



           final CategoryItemRenderer renderer = new CustomRenderer(colores);
 //   renderer.setItemLabelGenerator(new LabelGenerator(50.0));
        renderer.setItemLabelFont(new Font("Serif", Font.PLAIN, 20));
        renderer.setItemLabelsVisible(true);

        renderer.setItemLabelsVisible(true);
        final ItemLabelPosition p = new ItemLabelPosition(
            ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 45.0
        );
        renderer.setPositiveItemLabelPosition(p);
        plot.setRenderer(renderer);

        // change the margin at the top of the range axis...
        final ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setLowerMargin(0.15);
        rangeAxis.setUpperMargin(0.15);


        jPanel25.removeAll();
        jPanel25.setLayout(new java.awt.BorderLayout());
        jPanel25.add(new ChartPanel(cri));
        jPanel25.validate();
        
    
    }
    
  
    
    private void CreateCqs(double Cqs[]){
    
    
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
        for(int i = 0 ; i < Cqs.length ; i++){
           dataset.addValue(Cqs[i],"S1","VS"+(i+1));     
        }
            
          dataset.addValue(0,"S1",""); 
         dataset.addValue(rCqs.qA,"S1","Qa");
         cqs= ChartFactory.createBarChart(
        "CQS",
        // chart title
        "Category",
        // domain axis label
        "Value",
        // range axis label
        dataset,
        // data
        PlotOrientation.VERTICAL, // orientation
        false,
        // include legend
        true,
        // tooltips?
        false
        // URLs?
        );
    
    cqs.setBackgroundPaint(Color.white);
    CategoryPlot plot = cqs.getCategoryPlot();
    plot.setBackgroundPaint(Color.lightGray);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);
//    plot.setBackgroundImage(Toolkit.getDefaultToolkit().createImage(path+"/Archivos/SAM_2924.JPG"));
    
    Color colores[]=new Color [15];
   
    for (int i=0;i<colores.length;i++){
    
        colores[i]=new java.awt.Color((float)rCqs.referMunsell[i].rgb[0],
                (float)rCqs.referMunsell[i].rgb[1],(float)rCqs.referMunsell[i].rgb[2]);
    
    }
    
           final CategoryItemRenderer renderer = new CustomRenderer(colores
        );
           renderer.setItemLabelFont(new Font("Serif", Font.PLAIN, 20));
           renderer.setItemLabelsVisible(true);
           renderer.setItemLabelsVisible(true);
           final ItemLabelPosition p = new ItemLabelPosition(
            ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 45.0
        );
        renderer.setPositiveItemLabelPosition(p);
        plot.setRenderer(renderer);

        // change the margin at the top of the range axis...
        final ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setLowerMargin(0.15);
        rangeAxis.setUpperMargin(0.15);


        jPanel6.removeAll();
        jPanel6.setLayout(new java.awt.BorderLayout());
        jPanel6.add(new ChartPanel(cqs));
        jPanel6.validate();
    
    }
    
    
    private XYSeries  spdToXYSeries(ListData datos, double normal,String nombre){
    
     XYSeries psdSerie = new XYSeries(nombre);
        
        datos.resetNode();
        NodeData data=datos.getNodo();
      //  int i =0;
        while(data!=null){
   
          psdSerie.add(data.getLengthWave(),data.getAmplitude()/normal);
        //  psdSerie.add(i,i);
         // i++;
            data=datos.getNodo();
        }   
        
        return psdSerie;
 
    }
    
    private XYSeries spdToXYSeries(){
        
        ListTries.resetNode();
        NodeTries node=ListTries.getNodo();
        XYSeries psdSerie = new XYSeries("xy");        
        while(node.getLengthWave()!=505){
        
            psdSerie.add(node.amplX/(node.amplX+node.amplY+node.amplZ),node.amplY/(node.amplX+node.amplY+node.amplZ));
            node=ListTries.getNodo();
        }
        
        return psdSerie;
        
    }    
    
    public void updateGraph(XYSeries xy) {
        XYPlot plot = (XYPlot) spd.getPlot();
        dataset.removeAllSeries();
        dataset.addSeries(xy);
        plot.setDataset(dataset);
        lblTemperatureSpec.setText(selectedSpec.getTemperature());
    }
    
XYSeriesCollection dataset;

    public void graphic(XYSeries xy) {
        dataset = new XYSeriesCollection();
        //dataset.removeAllSeries();
        dataset.addSeries(xy);
// Generate the graph
        spd = ChartFactory.createXYLineChart(
                "", // Title
                "Wavelength", // x-axis Label
                "Amplitude", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        spd.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) spd.getPlot();

        plot.getRenderer().setSeriesPaint(0, Color.BLACK);
        plot.setBackgroundPaint(Color.white);

        plot.setDomainGridlinePaint(Color.CYAN);
        plot.setOutlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.CYAN);

        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        pnlGraph.removeAll();
        pnlGraph.setLayout(new java.awt.BorderLayout());
        pnlGraph.add(new ChartPanel(spd));
        pnlGraph.validate();
    }
   
    private void CreateSpd(ListData Datos){
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries psdSerie;
        XYSeries psdSerie2;
        XYSeries psdSerie1;
        XYSeries psdSerie3;
        if(jCheckBox1.isSelected()){
            psdSerie=spdToXYSeries(Datos,lampTest.max,lampTest.spd.getName());
        dataset.addSeries(psdSerie);}
        if(jCheckBox2.isSelected()){
            psdSerie1=spdToXYSeries(metric.photopic,metric.maxP,metric.photopic.getName());
            psdSerie2=spdToXYSeries(metric.mesopic,metric.maxM,metric.mesopicN.getName());
            psdSerie3=spdToXYSeries(metric.scotopic,metric.maxS,metric.scotopic.getName());
            dataset.addSeries(psdSerie1);
        dataset.addSeries(psdSerie2);
        dataset.addSeries(psdSerie3);
        
        }
       
        spd = ChartFactory.createXYLineChart(
        "SPD", // Title
        "Wav", // x-axis Label
        "Amplitud", // y-axis Label
        dataset, // Dataset
        PlotOrientation.VERTICAL, // Plot Orientation
        true, // Show Legend
        true, // Use tooltips
        false // Configure chart to generate URLs?
        );
        
        spd.setBackgroundPaint(Color.white);
        
        XYPlot plot = (XYPlot) spd.getPlot();
        
        plot.getRenderer().setSeriesPaint(0, Color.BLACK);
        plot.setBackgroundPaint(Color.white);
        
        plot.setDomainGridlinePaint(Color.CYAN);
        plot.setOutlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.CYAN);
        
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        
        pnlGraph.removeAll();
        pnlGraph.setLayout(new java.awt.BorderLayout());
        pnlGraph.add(new ChartPanel(spd));
        pnlGraph.validate();
        
        
    }
    
    
    private void crearDCromaticidadXY(){
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries psdSerieR=new XYSeries("Referencia");
        XYSeries psdSerieT=new XYSeries("Test");
        
        for(int i=0;i<14;i++){

            psdSerieT.add(rCri.testMunsell[i].xyz[0], rCri.testMunsell[i].xyz[1]);
        }
        
        for(int i=0;i<14;i++){
          //  psdSerie[i]=new XYSeries("TS"+i);
            psdSerieR.add(rCri.referMunsell[i].xyz[0], rCri.referMunsell[i].xyz[1]);
         
       // rCri.testMunsell[0].imprimir();
            
        }
        dataset.addSeries(psdSerieR);
         dataset.addSeries(psdSerieT);
        System.out.print("imrmim munsel");
       // rCri.testMunsell[0].imprimir();
        
        
        
        ListTries.resetNode();
        NodeTries node=ListTries.getNodo();
        XYSeries psdSerie5 = new XYSeries("xy");
        XYSeries psdSerie6 = new XYSeries("xy");
        while(node!=null){
            if(node.getLengthWave()<505)
                psdSerie5.add(node.amplX/(node.amplX+node.amplY+node.amplZ),node.amplY/(node.amplX+node.amplY+node.amplZ));
            else 
                psdSerie6.add(node.amplX/(node.amplX+node.amplY+node.amplZ),node.amplY/(node.amplX+node.amplY+node.amplZ));
            
            if(node.getLengthWave()==504)
                psdSerie6.add(node.amplX/(node.amplX+node.amplY+node.amplZ),node.amplY/(node.amplX+node.amplY+node.amplZ));
            node=ListTries.getNodo();
        }
        
        
        dataset.addSeries(psdSerie5);
        dataset.addSeries(psdSerie6);
        
        // Generate the graph
        spd = ChartFactory.createXYLineChart(
        "SPD", // Title
        "Wav", // x-axis Label
        "Amplitud", // y-axis Label
        dataset, // Dataset
        PlotOrientation.VERTICAL, // Plot Orientation
        true, // Show Legend
        true, // Use tooltips
        false // Configure chart to generate URLs?
        );
        
        spd.setBackgroundPaint(Color.white);
        
        XYPlot plot = (XYPlot) spd.getPlot();
        
        final XYLineAndShapeRenderer renderer=new XYLineAndShapeRenderer();
        XYToolTipGenerator xy=new StandardXYToolTipGenerator();
        
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(2, false);
        renderer.setSeriesShapesVisible(3, false);
        
        renderer.setSeriesToolTipGenerator(0, xy);
        renderer.setBaseItemLabelsVisible(true);      

           
        plot.setRenderer(renderer);
        plot.getRenderer().setSeriesPaint(2, Color.BLUE);
        plot.getRenderer().setSeriesPaint(3, Color.RED);
        plot.setBackgroundPaint(Color.white);
        
        plot.setDomainGridlinePaint(Color.CYAN);
        plot.setOutlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.CYAN);
        
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        
        
        jPanel25.removeAll();
        jPanel25.setLayout(new java.awt.BorderLayout());
        jPanel25.add(new ChartPanel(spd));
        jPanel25.validate();
        
        
    }
    
    
    private void crearDCromaticidadUV(){
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries psdSerieR=new XYSeries("Referencia");
        XYSeries psdSerieT=new XYSeries("Test");
        
        for(int i=0;i<14;i++){

            psdSerieT.add(rCri.testMunsell[i].uvY[0], rCri.testMunsell[i].uvY[1]);
        }
        
        for(int i=0;i<14;i++){
          //  psdSerie[i]=new XYSeries("TS"+i);
            psdSerieR.add(rCri.referMunsell[i].uvY[0], rCri.referMunsell[i].uvY[1]);
         
       // rCri.testMunsell[0].imprimir();
            
        }
        dataset.addSeries(psdSerieR);
         dataset.addSeries(psdSerieT);
        System.out.print("imrmim munsel");
       // rCri.testMunsell[0].imprimir();
        
        
        
        ListTries.resetNode();
        NodeTries node=ListTries.getNodo();
        XYSeries psdSerie5 = new XYSeries("xy");
        XYSeries psdSerie6 = new XYSeries("xy");
        while(node!=null){
            if(node.getLengthWave()<505)
                psdSerie5.add(4*node.amplX/(node.amplX+15*node.amplY+3*node.amplZ),9*node.amplY/(node.amplX+15*node.amplY+3*node.amplZ));
            else 
                psdSerie6.add(4*node.amplX/(node.amplX+15*node.amplY+3*node.amplZ),9*node.amplY/(node.amplX+15*node.amplY+3*node.amplZ));
            
            if(node.getLengthWave()==504)
                psdSerie6.add(4*node.amplX/(node.amplX+15*node.amplY+3*node.amplZ),9*node.amplY/(node.amplX+15*node.amplY+3*node.amplZ));
            node=ListTries.getNodo();
        }
        
        
        dataset.addSeries(psdSerie5);
        dataset.addSeries(psdSerie6);
        
        // Generate the graph
        spd = ChartFactory.createXYLineChart(
        "SPD", // Title
        "Wav", // x-axis Label
        "Amplitud", // y-axis Label
        dataset, // Dataset
        PlotOrientation.VERTICAL, // Plot Orientation
        true, // Show Legend
        true, // Use tooltips
        false // Configure chart to generate URLs?
        );
        
        spd.setBackgroundPaint(Color.white);
        
        XYPlot plot = (XYPlot) spd.getPlot();
                
        final XYLineAndShapeRenderer renderer=new XYLineAndShapeRenderer();
        XYToolTipGenerator xy=new StandardXYToolTipGenerator();
        
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(2, false);
        renderer.setSeriesShapesVisible(3, false);
        
        renderer.setSeriesToolTipGenerator(0, xy);
        renderer.setBaseItemLabelsVisible(true);      

           
        plot.setRenderer(renderer);
        plot.getRenderer().setSeriesPaint(2, Color.BLUE);
        plot.getRenderer().setSeriesPaint(3, Color.RED);
        plot.setBackgroundPaint(Color.white);
        
        plot.setDomainGridlinePaint(Color.CYAN);
        plot.setOutlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.CYAN);
        
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        
        
        jPanel25.removeAll();
        jPanel25.setLayout(new java.awt.BorderLayout());
        jPanel25.add(new ChartPanel(spd));
        jPanel25.validate();
        
        
    }
    
    
      private void crearDCromaticidadLab(){
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries psdSerieR=new XYSeries("Referencia");
        XYSeries psdSerieT=new XYSeries("Test");
        
        for(int i=0;i<14;i++){
            rCqs.testMunsell[i].imprimir();
            psdSerieT.add(rCqs.testMunsell[i].Lab[1], rCqs.testMunsell[i].Lab[2]);
        }
        
        for(int i=0;i<14;i++){
          //  psdSerie[i]=new XYSeries("TS"+i);
            psdSerieR.add(rCqs.referMunsell[i].Lab[1], rCqs.referMunsell[i].Lab[2]);
         rCqs.referMunsell[i].printSpd();
       // rCri.testMunsell[0].imprimir();
            
        }
        dataset.addSeries(psdSerieR);
         dataset.addSeries(psdSerieT);
        System.out.print("imrmim munsel");
          
        // Generate the graph
        spd = ChartFactory.createXYLineChart(
        "SPD", // Title
        "Wav", // x-axis Label
        "Amplitud", // y-axis Label
        dataset, // Dataset
        PlotOrientation.VERTICAL, // Plot Orientation
        true, // Show Legend
        true, // Use tooltips
        false // Configure chart to generate URLs?
        );
        
        spd.setBackgroundPaint(Color.white);
        
        XYPlot plot = (XYPlot) spd.getPlot();
        
        
        final XYLineAndShapeRenderer renderer=new XYLineAndShapeRenderer();
        XYToolTipGenerator xy=new StandardXYToolTipGenerator();
        
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesLinesVisible(1, false);

        
        renderer.setSeriesToolTipGenerator(0, xy);
        renderer.setSeriesToolTipGenerator(1, xy);
        renderer.setBaseItemLabelsVisible(true);      

           
        plot.setRenderer(renderer);
        plot.getRenderer().setSeriesPaint(2, Color.BLUE);
        plot.getRenderer().setSeriesPaint(3, Color.RED);
        plot.setBackgroundPaint(Color.white);
        
        plot.setDomainGridlinePaint(Color.CYAN);
        plot.setOutlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.CYAN);
        
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        
        
        jPanel6.removeAll();
        jPanel6.setLayout(new java.awt.BorderLayout());
        jPanel6.add(new ChartPanel(spd));
        jPanel6.validate();
        
        
    }
    
    
  
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel96 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        pnlSpecInfo = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        lblSerialNumber = new javax.swing.JLabel();
        lblChannels = new javax.swing.JLabel();
        lblTemperature = new javax.swing.JLabel();
        lblNameSpec = new javax.swing.JLabel();
        lblSerialNumberSpec = new javax.swing.JLabel();
        lblChannelsSpec = new javax.swing.JLabel();
        lblTemperatureSpec = new javax.swing.JLabel();
        lblFirmware = new javax.swing.JLabel();
        lblFirmwareSpec = new javax.swing.JLabel();
        btnSaveData = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        pnlChkFileSpec = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        pnlSpecControl = new javax.swing.JPanel();
        txtBoxcarWidth = new javax.swing.JTextField();
        lblBoxcarWidth = new javax.swing.JLabel();
        lblIntegrationTime = new javax.swing.JLabel();
        txtIntegrationTime = new javax.swing.JTextField();
        btnSetBoxcarWidth = new javax.swing.JButton();
        btnSetIntegrationTime = new javax.swing.JButton();
        jchkElecDarkCorr = new javax.swing.JCheckBox();
        txtScansToAverage = new javax.swing.JTextField();
        lblScansToAverage = new javax.swing.JLabel();
        btnSetScansToAverage = new javax.swing.JButton();
        pnlGraph = new JPanel();
        jPanel3 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel10 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel48 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jRadioButton4 = new javax.swing.JRadioButton();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(30, 0));
        jRadioButton5 = new javax.swing.JRadioButton();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(20, 0));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 10));
        jPanel6 = new JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel8 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jSplitPane5 = new javax.swing.JSplitPane();
        jPanel22 = new javax.swing.JPanel();
        jLabel97 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jLabel98 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jRadioButton7 = new javax.swing.JRadioButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(30, 0));
        jRadioButton1 = new javax.swing.JRadioButton();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(30, 0));
        jRadioButton8 = new javax.swing.JRadioButton();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(20, 0));
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 10));
        jPanel25 = new JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel26 = new javax.swing.JPanel();
        jLabel99 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        jLabel109 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel27 = new javax.swing.JPanel();
        jLabel114 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        jLabel116 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        jLabel119 = new javax.swing.JLabel();
        jLabel120 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        jLabel124 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        jLabel126 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        jLabel129 = new javax.swing.JLabel();
        jLabel130 = new javax.swing.JLabel();
        jLabel131 = new javax.swing.JLabel();
        jLabel132 = new javax.swing.JLabel();
        jLabel133 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        jLabel136 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        jLabel140 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        jLabel142 = new javax.swing.JLabel();
        jLabel143 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree2 = new TreeSpec();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ALux");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jSplitPane1.setDividerLocation(200);

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

        jSplitPane2.setBackground(new java.awt.Color(255, 255, 255));
        jSplitPane2.setDividerLocation(500);

        jPanel18.setMaximumSize(new java.awt.Dimension(20, 20));

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));

        jTable2.setModel(dtm3);
        jScrollPane3.setViewportView(jTable2);

        jLabel96.setText("Mesopica");

        jScrollPane7.setBackground(new java.awt.Color(255, 255, 255));

        jTable6.setModel(dtm4);
        jScrollPane7.setViewportView(jTable6);

        lblName.setText("Nombre:");

        lblSerialNumber.setText("Número de serie:");

        lblChannels.setText("Número de canales:");

        lblTemperature.setText("Temperatura:");

        lblFirmware.setText("Firmware:");

        btnSaveData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/capturarDatos.png"))); // NOI18N
        btnSaveData.setText("Capturar datos");
        btnSaveData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSpecInfoLayout = new javax.swing.GroupLayout(pnlSpecInfo);
        pnlSpecInfo.setLayout(pnlSpecInfoLayout);
        pnlSpecInfoLayout.setHorizontalGroup(
            pnlSpecInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSpecInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSpecInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSpecInfoLayout.createSequentialGroup()
                        .addComponent(lblName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNameSpec))
                    .addGroup(pnlSpecInfoLayout.createSequentialGroup()
                        .addComponent(lblSerialNumber)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSerialNumberSpec))
                    .addGroup(pnlSpecInfoLayout.createSequentialGroup()
                        .addComponent(lblChannels)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblChannelsSpec))
                    .addGroup(pnlSpecInfoLayout.createSequentialGroup()
                        .addComponent(lblFirmware)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFirmwareSpec))
                    .addGroup(pnlSpecInfoLayout.createSequentialGroup()
                        .addComponent(lblTemperature)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTemperatureSpec)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSpecInfoLayout.createSequentialGroup()
                .addContainerGap(127, Short.MAX_VALUE)
                .addComponent(btnSaveData, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
        );
        pnlSpecInfoLayout.setVerticalGroup(
            pnlSpecInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSpecInfoLayout.createSequentialGroup()
                .addGroup(pnlSpecInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(lblNameSpec))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSpecInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSerialNumber)
                    .addComponent(lblSerialNumberSpec))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSpecInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFirmware)
                    .addComponent(lblFirmwareSpec))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSpecInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblChannels)
                    .addComponent(lblChannelsSpec))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSpecInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTemperature)
                    .addComponent(lblTemperatureSpec))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSaveData)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel96)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(pnlSpecInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel96)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlSpecInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane2.setRightComponent(jPanel18);

        jPanel17.setBackground(new java.awt.Color(254, 254, 254));
        jPanel17.setAlignmentY(0.0F);
        jPanel17.setMaximumSize(new java.awt.Dimension(600, 400));
        jPanel17.setMinimumSize(new java.awt.Dimension(600, 100));
        jPanel17.setPreferredSize(new java.awt.Dimension(600, 100));
        jPanel17.setLayout(new javax.swing.BoxLayout(jPanel17, javax.swing.BoxLayout.Y_AXIS));

        pnlChkFileSpec.setBackground(new java.awt.Color(254, 254, 254));

        jCheckBox1.setText("SPD");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setLabel("Mesópica");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlChkFileSpecLayout = new javax.swing.GroupLayout(pnlChkFileSpec);
        pnlChkFileSpec.setLayout(pnlChkFileSpecLayout);
        pnlChkFileSpecLayout.setHorizontalGroup(
            pnlChkFileSpecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChkFileSpecLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jCheckBox1)
                .addGap(5, 5, 5)
                .addComponent(jCheckBox2)
                .addContainerGap(336, Short.MAX_VALUE))
        );
        pnlChkFileSpecLayout.setVerticalGroup(
            pnlChkFileSpecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChkFileSpecLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlChkFileSpecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel17.add(pnlChkFileSpec);

        pnlSpecControl.setBackground(new java.awt.Color(255, 255, 255));

        lblBoxcarWidth.setText("Boxcar width:");

        lblIntegrationTime.setText("Tiempo de integración(us):");

        btnSetBoxcarWidth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aceptarPeque.png"))); // NOI18N
        btnSetBoxcarWidth.setToolTipText("Aceptar");
        btnSetBoxcarWidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetBoxcarWidthActionPerformed(evt);
            }
        });

        btnSetIntegrationTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aceptarPeque.png"))); // NOI18N
        btnSetIntegrationTime.setToolTipText("Aceptar");
        btnSetIntegrationTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetIntegrationTimeActionPerformed(evt);
            }
        });

        jchkElecDarkCorr.setText("Electrical Dark Correction");
        jchkElecDarkCorr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchkElecDarkCorrActionPerformed(evt);
            }
        });

        lblScansToAverage.setText("Número de muestras a promediar:");

        btnSetScansToAverage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aceptarPeque.png"))); // NOI18N
        btnSetScansToAverage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetScansToAverageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSpecControlLayout = new javax.swing.GroupLayout(pnlSpecControl);
        pnlSpecControl.setLayout(pnlSpecControlLayout);
        pnlSpecControlLayout.setHorizontalGroup(
            pnlSpecControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSpecControlLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(pnlSpecControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlSpecControlLayout.createSequentialGroup()
                        .addComponent(lblScansToAverage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtScansToAverage, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSetScansToAverage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jchkElecDarkCorr))
                    .addGroup(pnlSpecControlLayout.createSequentialGroup()
                        .addComponent(lblBoxcarWidth)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBoxcarWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSetBoxcarWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblIntegrationTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIntegrationTime, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSetIntegrationTime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(168, 168, 168))
        );
        pnlSpecControlLayout.setVerticalGroup(
            pnlSpecControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSpecControlLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(pnlSpecControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSetBoxcarWidth, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlSpecControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtBoxcarWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBoxcarWidth)
                        .addComponent(lblIntegrationTime)
                        .addComponent(txtIntegrationTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSetIntegrationTime)))
                .addGap(18, 18, 18)
                .addGroup(pnlSpecControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSpecControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtScansToAverage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblScansToAverage))
                    .addComponent(btnSetScansToAverage, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jchkElecDarkCorr))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel17.add(pnlSpecControl);

        javax.swing.GroupLayout pnlGraphLayout = new javax.swing.GroupLayout(pnlGraph);
        pnlGraph.setLayout(pnlGraphLayout);
        pnlGraphLayout.setHorizontalGroup(
            pnlGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 499, Short.MAX_VALUE)
        );
        pnlGraphLayout.setVerticalGroup(
            pnlGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 294, Short.MAX_VALUE)
        );

        jPanel17.add(pnlGraph);

        jSplitPane2.setLeftComponent(jPanel17);

        jPanel2.add(jSplitPane2);

        jTabbedPane1.addTab("Principal", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.Y_AXIS));

        jLabel47.setText("CRI");
        jPanel10.add(jLabel47);

        jTable1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jTable1.setModel(dtm2);
        jTable1.setShowVerticalLines(false);
        jTable1.setSurrendersFocusOnKeystroke(true);
        jScrollPane1.setViewportView(jTable1);

        jPanel10.add(jScrollPane1);

        jLabel48.setText("Coordenadas");
        jPanel10.add(jLabel48);

        jScrollPane4.setToolTipText("");

        jTable3.setAutoCreateRowSorter(true);
        jTable3.setModel(dtmCordenadasCqs);
        jTable3.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane4.setViewportView(jTable3);

        jPanel10.add(jScrollPane4);

        jSplitPane3.setRightComponent(jPanel10);

        jPanel5.setBackground(new java.awt.Color(254, 254, 254));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));

        jPanel20.setBackground(new java.awt.Color(254, 254, 254));
        jPanel20.setLayout(new javax.swing.BoxLayout(jPanel20, javax.swing.BoxLayout.X_AXIS));

        buttonGroup3.add(jRadioButton4);
        jRadioButton4.setText("CQS");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });
        jPanel20.add(jRadioButton4);
        jPanel20.add(filler8);

        buttonGroup3.add(jRadioButton5);
        jRadioButton5.setText("Lab");
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });
        jPanel20.add(jRadioButton5);
        jPanel20.add(filler4);

        jPanel5.add(jPanel20);
        jPanel5.add(filler2);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 779, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 281, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel6);

        jSeparator2.setMaximumSize(new java.awt.Dimension(32767, 12));
        jSeparator2.setPreferredSize(new java.awt.Dimension(50, 12));
        jPanel5.add(jSeparator2);

        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel8.setLayout(new java.awt.GridLayout(1, 15, 1, 1));

        jLabel32.setBackground(new java.awt.Color(255, 255, 255));
        jLabel32.setForeground(new java.awt.Color(51, 51, 51));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Lamp");
        jLabel32.setOpaque(true);
        jPanel8.add(jLabel32);

        jLabel33.setBackground(new java.awt.Color(255, 255, 255));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("VS1");
        jLabel33.setToolTipText("");
        jLabel33.setOpaque(true);
        jPanel8.add(jLabel33);

        jLabel34.setBackground(new java.awt.Color(255, 255, 255));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("VS2");
        jLabel34.setOpaque(true);
        jPanel8.add(jLabel34);

        jLabel35.setBackground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("VS3");
        jLabel35.setOpaque(true);
        jPanel8.add(jLabel35);

        jLabel36.setBackground(new java.awt.Color(255, 255, 255));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("VS4");
        jLabel36.setOpaque(true);
        jPanel8.add(jLabel36);

        jLabel37.setBackground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("VS5");
        jLabel37.setOpaque(true);
        jPanel8.add(jLabel37);

        jLabel38.setBackground(new java.awt.Color(255, 255, 255));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("VS6");
        jLabel38.setOpaque(true);
        jPanel8.add(jLabel38);

        jLabel39.setBackground(new java.awt.Color(255, 255, 255));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("VS7");
        jLabel39.setOpaque(true);
        jPanel8.add(jLabel39);

        jLabel40.setBackground(new java.awt.Color(255, 255, 255));
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("VS8");
        jLabel40.setOpaque(true);
        jPanel8.add(jLabel40);

        jLabel41.setBackground(new java.awt.Color(255, 255, 255));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("VS9");
        jLabel41.setOpaque(true);
        jPanel8.add(jLabel41);

        jLabel42.setBackground(new java.awt.Color(255, 255, 255));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("VS10");
        jLabel42.setOpaque(true);
        jPanel8.add(jLabel42);

        jLabel43.setBackground(new java.awt.Color(255, 255, 255));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("VS11");
        jLabel43.setOpaque(true);
        jPanel8.add(jLabel43);

        jLabel44.setBackground(new java.awt.Color(255, 255, 255));
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("VS12");
        jLabel44.setOpaque(true);
        jPanel8.add(jLabel44);

        jLabel45.setBackground(new java.awt.Color(255, 255, 255));
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("VS13");
        jLabel45.setOpaque(true);
        jPanel8.add(jLabel45);

        jLabel46.setBackground(new java.awt.Color(255, 255, 255));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("VS14");
        jLabel46.setToolTipText("");
        jLabel46.setOpaque(true);
        jPanel8.add(jLabel46);

        jLabel51.setBackground(new java.awt.Color(255, 255, 255));
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setText("VS15");
        jLabel51.setToolTipText("");
        jLabel51.setOpaque(true);
        jPanel8.add(jLabel51);

        jPanel5.add(jPanel8);

        jSeparator1.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanel5.add(jSeparator1);

        jPanel7.setMaximumSize(new java.awt.Dimension(10000, 150));
        jPanel7.setMinimumSize(new java.awt.Dimension(779, 150));
        jPanel7.setPreferredSize(new java.awt.Dimension(100, 130));
        jPanel7.setLayout(new java.awt.GridLayout(2, 16, 1, 1));

        jLabel2.setBackground(new java.awt.Color(254, 252, 251));
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Refe.");
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel2.setOpaque(true);
        jPanel7.add(jLabel2);

        jLabel3.setBackground(new java.awt.Color(255, 0, 102));
        jLabel3.setToolTipText("");
        jLabel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel3.setMaximumSize(new java.awt.Dimension(4, 6));
        jLabel3.setMinimumSize(new java.awt.Dimension(4, 6));
        jLabel3.setOpaque(true);
        jLabel3.setPreferredSize(new java.awt.Dimension(4, 6));
        jPanel7.add(jLabel3);

        jLabel6.setBackground(new java.awt.Color(255, 0, 102));
        jLabel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel6.setOpaque(true);
        jPanel7.add(jLabel6);

        jLabel4.setBackground(new java.awt.Color(255, 0, 102));
        jLabel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel4.setOpaque(true);
        jPanel7.add(jLabel4);

        jLabel5.setBackground(new java.awt.Color(255, 0, 102));
        jLabel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel5.setOpaque(true);
        jPanel7.add(jLabel5);

        jLabel7.setBackground(new java.awt.Color(255, 0, 102));
        jLabel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel7.setOpaque(true);
        jPanel7.add(jLabel7);

        jLabel8.setBackground(new java.awt.Color(255, 0, 102));
        jLabel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel8.setOpaque(true);
        jPanel7.add(jLabel8);

        jLabel9.setBackground(new java.awt.Color(255, 0, 102));
        jLabel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel9.setOpaque(true);
        jPanel7.add(jLabel9);

        jLabel10.setBackground(new java.awt.Color(255, 0, 102));
        jLabel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel10.setOpaque(true);
        jPanel7.add(jLabel10);

        jLabel11.setBackground(new java.awt.Color(255, 0, 102));
        jLabel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel11.setOpaque(true);
        jPanel7.add(jLabel11);

        jLabel12.setBackground(new java.awt.Color(255, 0, 102));
        jLabel12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel12.setOpaque(true);
        jPanel7.add(jLabel12);

        jLabel13.setBackground(new java.awt.Color(255, 0, 102));
        jLabel13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel13.setOpaque(true);
        jPanel7.add(jLabel13);

        jLabel14.setBackground(new java.awt.Color(255, 0, 102));
        jLabel14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel14.setOpaque(true);
        jPanel7.add(jLabel14);

        jLabel15.setBackground(new java.awt.Color(255, 0, 102));
        jLabel15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel15.setOpaque(true);
        jPanel7.add(jLabel15);

        jLabel16.setBackground(new java.awt.Color(255, 0, 102));
        jLabel16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel16.setOpaque(true);
        jPanel7.add(jLabel16);

        jLabel49.setBackground(new java.awt.Color(255, 0, 102));
        jLabel49.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel49.setOpaque(true);
        jPanel7.add(jLabel49);

        jLabel17.setBackground(new java.awt.Color(250, 247, 249));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Test");
        jLabel17.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel17.setOpaque(true);
        jPanel7.add(jLabel17);

        jLabel18.setBackground(new java.awt.Color(255, 0, 102));
        jLabel18.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel18.setOpaque(true);
        jPanel7.add(jLabel18);

        jLabel19.setBackground(new java.awt.Color(255, 0, 102));
        jLabel19.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel19.setOpaque(true);
        jPanel7.add(jLabel19);

        jLabel20.setBackground(new java.awt.Color(255, 0, 102));
        jLabel20.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel20.setOpaque(true);
        jPanel7.add(jLabel20);

        jLabel21.setBackground(new java.awt.Color(255, 0, 102));
        jLabel21.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel21.setOpaque(true);
        jPanel7.add(jLabel21);

        jLabel22.setBackground(new java.awt.Color(255, 0, 102));
        jLabel22.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel22.setOpaque(true);
        jPanel7.add(jLabel22);

        jLabel23.setBackground(new java.awt.Color(255, 0, 102));
        jLabel23.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel23.setOpaque(true);
        jPanel7.add(jLabel23);

        jLabel24.setBackground(new java.awt.Color(255, 0, 102));
        jLabel24.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel24.setOpaque(true);
        jPanel7.add(jLabel24);

        jLabel25.setBackground(new java.awt.Color(255, 0, 102));
        jLabel25.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel25.setOpaque(true);
        jPanel7.add(jLabel25);

        jLabel26.setBackground(new java.awt.Color(255, 0, 102));
        jLabel26.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel26.setOpaque(true);
        jPanel7.add(jLabel26);

        jLabel27.setBackground(new java.awt.Color(255, 0, 102));
        jLabel27.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel27.setOpaque(true);
        jPanel7.add(jLabel27);

        jLabel28.setBackground(new java.awt.Color(255, 0, 102));
        jLabel28.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel28.setOpaque(true);
        jPanel7.add(jLabel28);

        jLabel29.setBackground(new java.awt.Color(255, 0, 102));
        jLabel29.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel29.setOpaque(true);
        jPanel7.add(jLabel29);

        jLabel30.setBackground(new java.awt.Color(255, 0, 102));
        jLabel30.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel30.setOpaque(true);
        jPanel7.add(jLabel30);

        jLabel31.setBackground(new java.awt.Color(255, 0, 102));
        jLabel31.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel31.setOpaque(true);
        jPanel7.add(jLabel31);

        jLabel50.setBackground(new java.awt.Color(255, 0, 102));
        jLabel50.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel50.setOpaque(true);
        jPanel7.add(jLabel50);

        jPanel5.add(jPanel7);

        jSplitPane3.setLeftComponent(jPanel5);

        jPanel3.add(jSplitPane3);

        jTabbedPane1.addTab("CQS", jPanel3);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setLayout(new javax.swing.BoxLayout(jPanel21, javax.swing.BoxLayout.Y_AXIS));

        jPanel22.setLayout(new javax.swing.BoxLayout(jPanel22, javax.swing.BoxLayout.Y_AXIS));

        jLabel97.setText("CRI");
        jPanel22.add(jLabel97);

        jTable7.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jTable7.setModel(dtm);
        jTable7.setShowVerticalLines(false);
        jTable7.setSurrendersFocusOnKeystroke(true);
        jScrollPane8.setViewportView(jTable7);

        jPanel22.add(jScrollPane8);

        jLabel98.setText("Coordenadas");
        jPanel22.add(jLabel98);

        jTable8.setModel(dtmCordenadasCri);
        jTable8.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane9.setViewportView(jTable8);

        jPanel22.add(jScrollPane9);

        jSplitPane5.setRightComponent(jPanel22);

        jPanel23.setBackground(new java.awt.Color(254, 254, 254));
        jPanel23.setLayout(new javax.swing.BoxLayout(jPanel23, javax.swing.BoxLayout.Y_AXIS));

        jPanel24.setBackground(new java.awt.Color(254, 254, 254));
        jPanel24.setLayout(new javax.swing.BoxLayout(jPanel24, javax.swing.BoxLayout.X_AXIS));

        buttonGroup2.add(jRadioButton7);
        jRadioButton7.setText("CRI");
        jRadioButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton7ActionPerformed(evt);
            }
        });
        jPanel24.add(jRadioButton7);
        jPanel24.add(filler3);

        buttonGroup2.add(jRadioButton1);
        jRadioButton1.setText("uv");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jPanel24.add(jRadioButton1);
        jPanel24.add(filler7);

        buttonGroup2.add(jRadioButton8);
        jRadioButton8.setText("xy");
        jRadioButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton8ActionPerformed(evt);
            }
        });
        jPanel24.add(jRadioButton8);
        jPanel24.add(filler5);

        jPanel23.add(jPanel24);
        jPanel23.add(filler6);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 779, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 281, Short.MAX_VALUE)
        );

        jPanel23.add(jPanel25);

        jSeparator5.setMaximumSize(new java.awt.Dimension(32767, 12));
        jSeparator5.setPreferredSize(new java.awt.Dimension(50, 12));
        jPanel23.add(jSeparator5);

        jPanel26.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel26.setLayout(new java.awt.GridLayout(1, 15, 1, 1));

        jLabel99.setBackground(new java.awt.Color(255, 255, 255));
        jLabel99.setForeground(new java.awt.Color(51, 51, 51));
        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel99.setText("Lamp");
        jLabel99.setOpaque(true);
        jPanel26.add(jLabel99);

        jLabel100.setBackground(new java.awt.Color(255, 255, 255));
        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel100.setText("TCS1");
        jLabel100.setToolTipText("");
        jLabel100.setOpaque(true);
        jPanel26.add(jLabel100);

        jLabel101.setBackground(new java.awt.Color(255, 255, 255));
        jLabel101.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel101.setText("TCS2");
        jLabel101.setOpaque(true);
        jPanel26.add(jLabel101);

        jLabel102.setBackground(new java.awt.Color(255, 255, 255));
        jLabel102.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel102.setText("TCS3");
        jLabel102.setOpaque(true);
        jPanel26.add(jLabel102);

        jLabel103.setBackground(new java.awt.Color(255, 255, 255));
        jLabel103.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel103.setText("TCS4");
        jLabel103.setOpaque(true);
        jPanel26.add(jLabel103);

        jLabel104.setBackground(new java.awt.Color(255, 255, 255));
        jLabel104.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel104.setText("TCS5");
        jLabel104.setOpaque(true);
        jPanel26.add(jLabel104);

        jLabel105.setBackground(new java.awt.Color(255, 255, 255));
        jLabel105.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel105.setText("TCS6");
        jLabel105.setOpaque(true);
        jPanel26.add(jLabel105);

        jLabel106.setBackground(new java.awt.Color(255, 255, 255));
        jLabel106.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel106.setText("TCS7");
        jLabel106.setOpaque(true);
        jPanel26.add(jLabel106);

        jLabel107.setBackground(new java.awt.Color(255, 255, 255));
        jLabel107.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel107.setText("TCS8");
        jLabel107.setOpaque(true);
        jPanel26.add(jLabel107);

        jLabel108.setBackground(new java.awt.Color(255, 255, 255));
        jLabel108.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel108.setText("TCS9");
        jLabel108.setOpaque(true);
        jPanel26.add(jLabel108);

        jLabel109.setBackground(new java.awt.Color(255, 255, 255));
        jLabel109.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel109.setText("TCS10");
        jLabel109.setOpaque(true);
        jPanel26.add(jLabel109);

        jLabel110.setBackground(new java.awt.Color(255, 255, 255));
        jLabel110.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel110.setText("TCS11");
        jLabel110.setOpaque(true);
        jPanel26.add(jLabel110);

        jLabel111.setBackground(new java.awt.Color(255, 255, 255));
        jLabel111.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel111.setText("TCS12");
        jLabel111.setOpaque(true);
        jPanel26.add(jLabel111);

        jLabel112.setBackground(new java.awt.Color(255, 255, 255));
        jLabel112.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel112.setText("TCS13");
        jLabel112.setOpaque(true);
        jPanel26.add(jLabel112);

        jLabel113.setBackground(new java.awt.Color(255, 255, 255));
        jLabel113.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel113.setText("TCS14");
        jLabel113.setToolTipText("");
        jLabel113.setOpaque(true);
        jPanel26.add(jLabel113);

        jPanel23.add(jPanel26);

        jSeparator6.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanel23.add(jSeparator6);

        jPanel27.setMaximumSize(new java.awt.Dimension(10000, 150));
        jPanel27.setMinimumSize(new java.awt.Dimension(779, 150));
        jPanel27.setPreferredSize(new java.awt.Dimension(100, 130));
        jPanel27.setLayout(new java.awt.GridLayout(2, 15, 1, 1));

        jLabel114.setBackground(new java.awt.Color(254, 252, 251));
        jLabel114.setForeground(new java.awt.Color(51, 51, 51));
        jLabel114.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel114.setText("Refe.");
        jLabel114.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel114.setOpaque(true);
        jPanel27.add(jLabel114);

        jLabel115.setBackground(new java.awt.Color(255, 0, 102));
        jLabel115.setToolTipText("");
        jLabel115.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel115.setMaximumSize(new java.awt.Dimension(4, 6));
        jLabel115.setMinimumSize(new java.awt.Dimension(4, 6));
        jLabel115.setOpaque(true);
        jLabel115.setPreferredSize(new java.awt.Dimension(4, 6));
        jPanel27.add(jLabel115);

        jLabel116.setBackground(new java.awt.Color(255, 0, 102));
        jLabel116.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel116.setOpaque(true);
        jPanel27.add(jLabel116);

        jLabel117.setBackground(new java.awt.Color(255, 0, 102));
        jLabel117.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel117.setOpaque(true);
        jPanel27.add(jLabel117);

        jLabel118.setBackground(new java.awt.Color(255, 0, 102));
        jLabel118.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel118.setOpaque(true);
        jPanel27.add(jLabel118);

        jLabel119.setBackground(new java.awt.Color(255, 0, 102));
        jLabel119.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel119.setOpaque(true);
        jPanel27.add(jLabel119);

        jLabel120.setBackground(new java.awt.Color(255, 0, 102));
        jLabel120.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel120.setOpaque(true);
        jPanel27.add(jLabel120);

        jLabel121.setBackground(new java.awt.Color(255, 0, 102));
        jLabel121.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel121.setOpaque(true);
        jPanel27.add(jLabel121);

        jLabel122.setBackground(new java.awt.Color(255, 0, 102));
        jLabel122.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel122.setOpaque(true);
        jPanel27.add(jLabel122);

        jLabel123.setBackground(new java.awt.Color(255, 0, 102));
        jLabel123.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel123.setOpaque(true);
        jPanel27.add(jLabel123);

        jLabel124.setBackground(new java.awt.Color(255, 0, 102));
        jLabel124.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel124.setOpaque(true);
        jPanel27.add(jLabel124);

        jLabel125.setBackground(new java.awt.Color(255, 0, 102));
        jLabel125.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel125.setOpaque(true);
        jPanel27.add(jLabel125);

        jLabel126.setBackground(new java.awt.Color(255, 0, 102));
        jLabel126.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel126.setOpaque(true);
        jPanel27.add(jLabel126);

        jLabel127.setBackground(new java.awt.Color(255, 0, 102));
        jLabel127.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel127.setOpaque(true);
        jPanel27.add(jLabel127);

        jLabel128.setBackground(new java.awt.Color(255, 0, 102));
        jLabel128.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel128.setOpaque(true);
        jPanel27.add(jLabel128);

        jLabel129.setBackground(new java.awt.Color(250, 247, 249));
        jLabel129.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel129.setText("REFER");
        jLabel129.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel129.setOpaque(true);
        jPanel27.add(jLabel129);

        jLabel130.setBackground(new java.awt.Color(255, 0, 102));
        jLabel130.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel130.setOpaque(true);
        jPanel27.add(jLabel130);

        jLabel131.setBackground(new java.awt.Color(255, 0, 102));
        jLabel131.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel131.setOpaque(true);
        jPanel27.add(jLabel131);

        jLabel132.setBackground(new java.awt.Color(255, 0, 102));
        jLabel132.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel132.setOpaque(true);
        jPanel27.add(jLabel132);

        jLabel133.setBackground(new java.awt.Color(255, 0, 102));
        jLabel133.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel133.setOpaque(true);
        jPanel27.add(jLabel133);

        jLabel134.setBackground(new java.awt.Color(255, 0, 102));
        jLabel134.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel134.setOpaque(true);
        jPanel27.add(jLabel134);

        jLabel135.setBackground(new java.awt.Color(255, 0, 102));
        jLabel135.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel135.setOpaque(true);
        jPanel27.add(jLabel135);

        jLabel136.setBackground(new java.awt.Color(255, 0, 102));
        jLabel136.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel136.setOpaque(true);
        jPanel27.add(jLabel136);

        jLabel137.setBackground(new java.awt.Color(255, 0, 102));
        jLabel137.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel137.setOpaque(true);
        jPanel27.add(jLabel137);

        jLabel138.setBackground(new java.awt.Color(255, 0, 102));
        jLabel138.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel138.setOpaque(true);
        jPanel27.add(jLabel138);

        jLabel139.setBackground(new java.awt.Color(255, 0, 102));
        jLabel139.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel139.setOpaque(true);
        jPanel27.add(jLabel139);

        jLabel140.setBackground(new java.awt.Color(255, 0, 102));
        jLabel140.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel140.setOpaque(true);
        jPanel27.add(jLabel140);

        jLabel141.setBackground(new java.awt.Color(255, 0, 102));
        jLabel141.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel141.setOpaque(true);
        jPanel27.add(jLabel141);

        jLabel142.setBackground(new java.awt.Color(255, 0, 102));
        jLabel142.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel142.setOpaque(true);
        jPanel27.add(jLabel142);

        jLabel143.setBackground(new java.awt.Color(255, 0, 102));
        jLabel143.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jLabel143.setOpaque(true);
        jPanel27.add(jLabel143);

        jPanel23.add(jPanel27);

        jSplitPane5.setLeftComponent(jPanel23);

        jPanel21.add(jSplitPane5);

        jTabbedPane1.addTab("CRI", jPanel21);

        jSplitPane1.setRightComponent(jTabbedPane1);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        ((TreeSpec)jTree2).register(this);
        jScrollPane2.setViewportView(jTree2);

        jPanel1.add(jScrollPane2);

        jSplitPane1.setLeftComponent(jPanel1);

        getContentPane().add(jSplitPane1);

        jMenu1.setText("Archivo");

        jMenuItem11.setText("Importar SPD");
        jMenu1.add(jMenuItem11);

        jMenuItem3.setText("Exportar");
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu5.setText("Calibración");

        jMenuItem12.setText("Calibración Total");
        jMenu5.add(jMenuItem12);

        jMenuItem1.setText("Calculadora Color");
        jMenu5.add(jMenuItem1);

        jMenuBar1.add(jMenu5);

        jMenu4.setText("Ayuda");

        jMenuItem8.setLabel("ALux");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jRadioButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton8ActionPerformed
        if(jRadioButton8.isSelected())
        crearDCromaticidadXY();
    }//GEN-LAST:event_jRadioButton8ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        if(jRadioButton1.isSelected())
        crearDCromaticidadUV();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton7ActionPerformed
        if(jRadioButton7.isSelected())
        CreateCri(rCri.getCri());
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton7ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        // TODO add your handling code here:
        if(jRadioButton5.isSelected())
        crearDCromaticidadLab();
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
        if(jRadioButton4.isSelected())
        CreateCqs(rCqs.getCqs());
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed

        if(!jCheckBox2.isSelected() & !jCheckBox1.isSelected() )
        jCheckBox1.setSelected(true);

        CreateSpd(lampTest.spd);

    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if(!jCheckBox2.isSelected() & !jCheckBox1.isSelected() )
        jCheckBox1.setSelected(true);

        //CreateSpd(lampTest.spd);
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void btnSetBoxcarWidthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetBoxcarWidthActionPerformed
        if(validIntValue(txtBoxcarWidth.getText())) {
            selectedSpec.setBoxCarWidth(Integer.parseInt(txtBoxcarWidth.getText()));
        }
    }//GEN-LAST:event_btnSetBoxcarWidthActionPerformed

    private void btnSetIntegrationTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetIntegrationTimeActionPerformed
        if(validIntValue(txtIntegrationTime.getText())) {
            selectedSpec.setIntegrationTime(Integer.parseInt(txtIntegrationTime.getText()));
        }
    }//GEN-LAST:event_btnSetIntegrationTimeActionPerformed

    private void jchkElecDarkCorrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchkElecDarkCorrActionPerformed
        selectedSpec.setElectricalDarkCorr(jchkElecDarkCorr.isSelected());
    }//GEN-LAST:event_jchkElecDarkCorrActionPerformed

    private void btnSetScansToAverageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetScansToAverageActionPerformed
        if(validIntValue(txtScansToAverage.getText())) {
            selectedSpec.setScansToAverage(Integer.parseInt(txtScansToAverage.getText()));
        }
    }//GEN-LAST:event_btnSetScansToAverageActionPerformed

    private void btnSaveDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveDataActionPerformed
        double [] spectrumArray = selectedSpec.getSpectrumArray();
        double [] wavelengthArray = selectedSpec.getWavelengthArray();
        String filePath = path + "/Lamparas/";
        String fileName = selectedSpec.getName() + " " + selectedSpec.getSerialNumber() + "-"
        + gCalendar.get(Calendar.HOUR_OF_DAY) + "_" + gCalendar.get(Calendar.MINUTE) + "_"
        + gCalendar.get(Calendar.SECOND)+ ".txt";
        if(saveFile(spectrumArray, wavelengthArray, filePath + fileName)) {
            ((TreeSpec)jTree2).addFile(fileName);
            JOptionPane.showMessageDialog(this, "Archivo de datos guardado.");
        }

    }//GEN-LAST:event_btnSaveDataActionPerformed

    private boolean saveFile(double[] spectrumArray, double[] wavelengthArray, String filePathName) {
        PrintWriter printWriter = null;
        try {
            File file = new File(filePathName);
            printWriter = new PrintWriter(new FileWriter(file));
            for(int i = 0; i < spectrumArray.length; i++) {
                printWriter.println( wavelengthArray[i] + "   " + spectrumArray[i]);
            }
            printWriter.flush();
            printWriter.close();
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error guardando el archivo" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            printWriter.close();
            return false;
        }
    }
    
    public boolean validIntValue(String data) {
        try {
            Integer.parseInt(data);
            return true;
        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Debe introducir un valor numérico entero.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /*
    public void cargarLamparasTree(){
    
    
        DefaultMutableTreeNode nroot = new DefaultMutableTreeNode("Recursos");
        DefaultTreeModel modelo = (DefaultTreeModel)jTree2.getModel();
        modelo.setRoot(nroot);
        DefaultMutableTreeNode USBSPec = new DefaultMutableTreeNode("Espectrómetros USB");
        DefaultMutableTreeNode EthSpec = new DefaultMutableTreeNode("Espectrómetros Ethernet");
        
        loadSpectrometers(modelo, USBSPec, EthSpec);
        
        DefaultMutableTreeNode nrootArch = new DefaultMutableTreeNode("Archivos de lámparas");
        modelo.insertNodeInto(USBSPec, nroot, 0);
        modelo.insertNodeInto(EthSpec, nroot, 1);
        modelo.insertNodeInto(nrootArch, nroot, 2);
        CargaEstructuraDirectorios(modelo, nrootArch, path+"/Lamparas/");

    }
    
    private void loadSpectrometers(DefaultTreeModel arbol, DefaultMutableTreeNode USBSPec, DefaultMutableTreeNode EthSpec) {
        DefaultMutableTreeNode aux = null;
        ArrayList<Spectrometer> spectrometers = spectrometer1.getArraySpec();
        for(int i = 0; i < spectrometers.size(); i++) {
            DefaultMutableTreeNode padre = spectrometers.get(i) instanceof USBSpectrometer ? USBSPec: EthSpec;
            aux = new DefaultMutableTreeNode(spectrometers.get(i));
            arbol.insertNodeInto(aux, padre, i);
        }
        
    }
    
     private void CargaEstructuraDirectorios(DefaultTreeModel arbol,
        DefaultMutableTreeNode padre, String ruta) {
        DefaultMutableTreeNode aux = null;
 
        File archivo = new File(ruta); // puntero al directorio de la ruta
        File[] archivos = archivo.listFiles(); // lista todos los archivos de la ruta
 
        // recorre lo que hay en la ruta
        if (archivos != null) {
            for (int i = 0; i < archivos.length; i++) {
 
                // creando un nodo con cada cosa del directorio
                aux = new DefaultMutableTreeNode(archivos[i].getName());
                // inserta el nodo hijo 
                arbol.insertNodeInto(aux, padre, i);
 
                // si encontramos un directorio volvemos a hacer lo mismo con sus hijos
                if (archivos[i].isDirectory()) {
                    try {
                         
                        // llamando recursivamente de nuevo a ésta misma función
                        CargaEstructuraDirectorios(arbol, aux,
                                archivos[i].getAbsolutePath() + "/");
                         
                    } catch (Exception e) {
                        System.out.println(e.getMessage()); // por si acaso le he puesto un try xD
                    }
                }
 
            }
 
        }
    }*/
    
    
    
    
    public static void escribirArchivo(ListData Datos,String ruta){
        
        Datos.actualNode=null;
        NodeData data=Datos.getNodo();
        
        
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(ruta);
       //     FileWriter fichero = new FileWriter("c:/prueba.txt",true);
            pw = new PrintWriter(fichero);
 
             while(data!=null){
             pw.println(data.getLengthWave()+"\t"+data.getAmplitude());
         //   psdSerie.add(data.getLengthWave(),data.getAmplitude());
            data=Datos.getNodo();
        
        }       
 
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
    
    
    private class ManejadorEventoMenu implements ActionListener{
    
    
    public void actionPerformed( ActionEvent evento ){
        
        File archivo=null;
         
        if(jMenuItem11 == evento.getSource()){

            try {
                archivo= obtenerSpd();
                copyFile(archivo,new File(path+"/Lamparas/"+archivo.getName()));
                //cargarLamparasTree();
            } catch (IOException ex) {
                JDialog eImport=new ErrorImportando(Lux.this,true);
                eImport.setLocationRelativeTo(Lux.this);
                eImport.setVisible(true);
            }catch(NullPointerException n){
                System.out.print("No se eligió ningun Archivo");
            }
            
        }
        else if(jMenuItem3 == evento.getSource()){    
            
     
        escribirArchivo(lampTest.spd,path+"/Lamparas/"+lampTest.spd.getName());
        }
       
        else if(jMenuItem8 == evento.getSource()){
        
        JDialog acerca=new SobreAlux(Lux.this,true);
        acerca.setResizable(false);
        acerca.setLocationRelativeTo(Lux.this);
        acerca.setVisible(true);
        }
        else if(jMenuItem12 == evento.getSource()){
            
        Dialogo1 espPatron=new Dialogo1(Lux.this,true);
            espPatron.setLocationRelativeTo(Lux.this);
            espPatron.setResizable(false);
            espPatron.setVisible(true);
        
       if(espPatron.getSpdMedido()!=null){
      /*         
               
        EspectroMedido capMedido=new EspectroMedido(Lux.this,true);
            capMedido.setResizable(false);
            capMedido.setLocationRelativeTo(Lux.this);
            capMedido.setVisible(true);
            if(capMedido.getSpdMedido()!=null){
            calibrar=new Calibration(((Vector)espPatron.dtm4.getDataVector().elementAt(0)).elementAt(0).toString()
                   ,((Vector)capMedido.dtm4.getDataVector().elementAt(0)).elementAt(0).toString());

            }*/
            }
       
        }
        else if(jMenuItem1 == evento.getSource()){
        
            CalculadoraColor colorCalculador=new CalculadoraColor(Lux.this,true);
            colorCalculador.setLocationRelativeTo(Lux.this);
            colorCalculador.setResizable(false);
            colorCalculador.setVisible(true);
            
        
        
        
        }
    }
          
    
    
}
    
    

    
    //private class ManejadorEventoGrafico implements ActionListener{
    
    //public void actionPerformed( ActionEvent evento ){
        
        //File archivo=null;
         
/*        if(jButton1 == evento.getSource()){
            spd.getPlot().zoom(20);
                    jPanel9.validate();
                    new ChartPanel(spd).createChartPrintJob();
            
        }*/
    //}   
//}
    
    
   

    
    
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
            java.util.logging.Logger.getLogger(Lux.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Lux.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Lux.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lux.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Lux LuxUn =new Lux();
                LuxUn.setExtendedState(MAXIMIZED_BOTH);
                LuxUn.setVisible(true);
                       
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSaveData;
    private javax.swing.JButton btnSetBoxcarWidth;
    private javax.swing.JButton btnSetIntegrationTime;
    private javax.swing.JButton btnSetScansToAverage;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTree jTree2;
    private javax.swing.JCheckBox jchkElecDarkCorr;
    private javax.swing.JLabel lblBoxcarWidth;
    private javax.swing.JLabel lblChannels;
    private javax.swing.JLabel lblChannelsSpec;
    private javax.swing.JLabel lblFirmware;
    private javax.swing.JLabel lblFirmwareSpec;
    private javax.swing.JLabel lblIntegrationTime;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblNameSpec;
    private javax.swing.JLabel lblScansToAverage;
    private javax.swing.JLabel lblSerialNumber;
    private javax.swing.JLabel lblSerialNumberSpec;
    private javax.swing.JLabel lblTemperature;
    private javax.swing.JLabel lblTemperatureSpec;
    private javax.swing.JPanel pnlChkFileSpec;
    private javax.swing.JPanel pnlGraph;
    private javax.swing.JPanel pnlSpecControl;
    private javax.swing.JPanel pnlSpecInfo;
    private javax.swing.JTextField txtBoxcarWidth;
    private javax.swing.JTextField txtIntegrationTime;
    private javax.swing.JTextField txtScansToAverage;
    // End of variables declaration//GEN-END:variables



   
    public  double flujo(ListData spd){
         
        double minor;
        double major;
        double flux=0;
        NodeMetric.photopic.resetNode();
        calibrar.getK1().resetNode();
        
        spd.resetNode();
         
        if(NodeMetric.photopic.getFirstNode().getLengthWave()<spd.getFirstNode().getLengthWave())
            minor=spd.getFirstNode().getLengthWave();
        else
            minor=NodeMetric.photopic.getFirstNode().getLengthWave();
         
        if(NodeMetric.photopic.getFinalNode().getLengthWave()>spd.getFinalNode().getLengthWave())
            major=spd.getFinalNode().getLengthWave();
        else
            major=NodeMetric.photopic.getFinalNode().getLengthWave();

        NodeData metric=NodeMetric.photopic.getNodo();
        NodeData spdNode=spd.getNodo();
        
        while(metric.getLengthWave()<minor){
                metric=NodeMetric.photopic.getNodo();                
            }
        while(spdNode.getLengthWave()<minor){
                spdNode=spd.getNodo();
            }
        
        NodeData cal=calibrar.getK1().getNodo();
        
        while(major!=spd.getNodoActual().getLengthWave() && major!=NodeMetric.photopic.getNodoActual().getLengthWave()){
            
            
            
            flux=flux+0.5*(metric.getAmplitude()*spdNode.getAmplitude()*cal.getAmplitude()*calibrar.getK2()+
                    metric.nextNodo.getAmplitude()*spdNode.nextNodo.getAmplitude()*cal.nextNodo.getAmplitude()*calibrar.getK2());
            
            metric=NodeMetric.photopic.getNodo(); 
            spdNode=spd.getNodo();
            cal=calibrar.getK1().getNodo();
            
            
        }
                
        return flux;
     }
     
    

}
