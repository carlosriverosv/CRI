/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import static Controlador.Spectrometer.LUX;
import com.oceanoptics.omnidriver.api.wrapper.Wrapper;
import com.oceanoptics.omnidriver.features.boardtemperature.BoardTemperature;
import cri.Lamp;
import cri.Lux;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.Timer;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author Carlos
 */
public abstract class Spectrometer implements ActionListener {

    protected String name, serialNumber, firmware;
    protected int channels;
    protected int index;
    protected XYSeries xy = new XYSeries("");
    protected double[] spectrumArray, wavelengthArray;
    protected static Lux LUX;
    protected Timer timer;
    protected int refreshTime = 100;
    protected int boxCarWidth;
    protected int scansToAverage;
    protected boolean electricalDarkCorr, supportedBoardTemperature;
    protected int integrationTime;
    protected BoardTemperature boardTemperature;
    protected double temperatureCelsius;
    private static Wrapper wrapper;
    
    
    public static Wrapper getWrapper() {
        if(wrapper == null) {
            wrapper = new Wrapper();
        }
        return wrapper;
    }

    public Spectrometer(int index, String name, String serialNumber, String firmware, int channels) {
        this.index = index;
        this.name = name;
        this.serialNumber = serialNumber;
        this.firmware = firmware;
        this.channels = channels;
        supportedBoardTemperature = supportedBoardTemperature();
        this.integrationTime = Spectrometer.getWrapper().getMinimumIntegrationTime(index);
    }

    public boolean supportedBoardTemperature() {
        if (Spectrometer.getWrapper().isFeatureSupportedBoardTemperature(index)) {
            boardTemperature = Spectrometer.getWrapper().getFeatureControllerBoardTemperature(index);
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getFirmware() {
        return firmware;
    }

    public int getChannels() {
        return channels;
    }

    public int getBoxCarWidth() {
        return boxCarWidth;
    }

    public void setBoxCarWidth(int boxCarWidth) {
        this.boxCarWidth = boxCarWidth;
        Spectrometer.getWrapper().setBoxcarWidth(index, boxCarWidth);
    }

    public int getScansToAverage() {
        return scansToAverage;
    }

    public void setScansToAverage(int scansToAverage) {
        this.scansToAverage = scansToAverage;
        Spectrometer.getWrapper().setScansToAverage(index, scansToAverage);
    }

    public boolean isElectricalDarkCorr() {
        return electricalDarkCorr;
    }

    public void setElectricalDarkCorr(boolean electricalDarkCorr) {
        this.electricalDarkCorr = electricalDarkCorr;
        int sel = electricalDarkCorr ? 1 : 0;
        Spectrometer.getWrapper().setCorrectForElectricalDark(index, sel);
    }

    public int getIntegrationTime() {
        return integrationTime;
    }

    public void setIntegrationTime(int integrationTime) {
        this.integrationTime = integrationTime;
        Spectrometer.getWrapper().setIntegrationTime(index, integrationTime);
    }

    @Override
    public String toString() {
        return this.getName() + " - " + this.getSerialNumber() + "- Ch: " + channels;
    }

    public static void setLux(Lux lux) {
        Spectrometer.LUX = lux;
    }

    
    Lamp lampTest;
    public XYSeries getValues() {
        if (!xy.isEmpty()) {
            xy.clear();
        }
        
        //lampTest = new Lamp();
        spectrumArray = getSpectrumArray();
        wavelengthArray = getWavelengthArray();
        for (int i = 0; i < spectrumArray.length; i++) {
            xy.add(wavelengthArray[i], spectrumArray[i]);
            //lampTest.spd.setSpd(wavelengthArray[i], spectrumArray[i]);
            //System.out.println(wavelengthArray[i] + "   "+ spectrumArray[i]);
        }
          //LUX.cargarDatosLampara(lampTest);
        return xy;
    }
    
    public double[] getSpectrumArray () {
        return Spectrometer.getWrapper().getSpectrum(index, channels - 1);
    }
    
    public double[] getWavelengthArray () {
        return Spectrometer.getWrapper().getWavelengths(index, channels - 1);
    }

    public void start() {
        LUX.graphic(getValues());
        //LUX.cargarDatosLampara(lampTest);
        timer = new Timer(refreshTime, this);
        timer.setInitialDelay(refreshTime);
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        LUX.updateGraph(getValues());
    }

    public String getTemperature() {
        if (supportedBoardTemperature) {
            try {
                temperatureCelsius = boardTemperature.getBoardTemperatureCelsius();
            } catch (IOException ioException) {
                System.out.println(ioException);
            }
            return String.valueOf(temperatureCelsius) + "°C";
        } else {
            return "Característica no soportada por este espectrómetro.";
        }
    }

}
