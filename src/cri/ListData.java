/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cri;

import dialog.ErrorArchivo;
import dialog.ErrorLectura;
import dialog.ErrorOpen;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JDialog;

/**
 *
 * @author BALUM ETB3
 */
public class ListData {

    private String nameData;
    private NodeData firstNode;
    private NodeData finalNode;
    public NodeData actualNode;
    public double max;

    public ListData(String nameData) {

        this.nameData = nameData;

    }

    ListData(File ruta) {

        this.nameData = ruta.getName();

        try {
            Scanner salida = new Scanner(ruta);

            try // lee registros del archivo, usando el objeto Scanner
            {
                double lamb1 = 0;
                String inter;
                double ampl1 = 0;
                double lambR;
                double lamb2;
                double ampl2;
                if (salida.hasNext()) {// read the first values

                    lamb1 = Double.parseDouble(salida.next().replace(',', '.'));
                    ampl1 = Double.parseDouble(salida.next().replace(',', '.'));
                }

                if (Math.ceil(lamb1) == Math.floor(lamb1)) {
                    setSpd(lamb1, ampl1);//ingresa el primer valor si es entero
                    lambR = lamb1 + 1;       //aumenta la sigueinte requerida                 
                } else {
                    lambR = Math.ceil(lamb1);// Si no es entero calcula el techo del valor actual
                }
                lamb2 = Double.parseDouble(salida.next().replace(',', '.'));// the next Lamp
                ampl2 = Double.parseDouble(salida.next().replace(',', '.'));// the next Ampl

                while (salida.hasNext()) {

                    if (lambR > lamb2) {
                        lamb1 = lamb2;
                        ampl1 = ampl2;
                        lamb2 = Double.parseDouble(salida.next().replace(',', '.'));
                        ampl2 = Double.parseDouble(salida.next().replace(',', '.'));
                    }
                    if (lamb2 == lambR) {// if the next value is equal to value requerided, then save
                        setSpd(lamb2, ampl2);
                        lambR = lambR + 1;
                    } else if (lamb2 > lambR) {// if the next value is major to value requerided, then calculate and save
                        setSpd(lambR, (ampl2 - ampl1) / (lamb2 - lamb1) * (lambR - lamb1) + ampl1);
                        lambR = lambR + 1;
                    }

                } // fin de while

            } // fin de try
            catch (NoSuchElementException elementException) {
                JDialog error = new ErrorArchivo(Lux.getFrames()[0], true);
                error.setLocationRelativeTo(Lux.getFrames()[0]);
                error.setVisible(true);
                //     salida.close();
                //     System.exit( 1 );
            } // fin de catch
            catch (IllegalStateException stateException) {
                JDialog error = new ErrorLectura(Lux.getFrames()[0], true);
                error.setLocationRelativeTo(Lux.getFrames()[0]);
                error.setVisible(true);
            } // fin de catch*/
        } // fin de try
        catch (FileNotFoundException fileNotFoundException) {
            JDialog error = new ErrorOpen(Lux.getFrames()[0], true);
            error.setLocationRelativeTo(Lux.getFrames()[0]);
            error.setVisible(true);
        } // fin de catch

    }

    public void setSpd(double lamb, double ampl) {
        insertNode(ampl, lamb);
        if (ampl > max) {
            max = ampl;
        }
    }

    public void insertNode(double amplitude, double lengthWave) {
        if (firstNode == null) {
            firstNode = finalNode = new NodeData(amplitude, lengthWave);
        } else {
            finalNode = finalNode.nextNodo = new NodeData(amplitude, lengthWave);
        }
    }

    public NodeData getNodo() {

        if (actualNode == finalNode) {
            actualNode = null;
        } else if (actualNode == null) {
            actualNode = firstNode;
        } else {
            actualNode = actualNode.nextNodo;
        }
        return actualNode;
    }

    public NodeData getNodoActual() {
        return actualNode;
    }

    public NodeData getFirstNode() {
        return firstNode;
    }

    public NodeData getFinalNode() {
        return finalNode;
    }

    public NodeData getNextNodeA() {
        return actualNode.nextNodo;
    }

    public void resetNode() {
        actualNode = null;
    }

    public String getName() {
        return nameData;
    }

}
