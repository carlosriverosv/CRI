/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Carlos
 */
public class Resources {

    private int numberOfSpectrometersUSBFound;
    private String name, serialNumber, firmware;
    private int channels;
    private static ArrayList<Spectrometer> spectrometers = new ArrayList();
    private static Resources resources;

    public static ArrayList<Spectrometer> getArraySpec() {
        return spectrometers;
    }

    private Resources() {
        openUSBSpectrometers();
    }

    public static Resources getResources() {
        if (resources == null) {
            resources = new Resources();
        }
        return resources;
    }

    private void openUSBSpectrometers() {
        numberOfSpectrometersUSBFound = Spectrometer.getWrapper().openAllSpectrometers();

        if (numberOfSpectrometersUSBFound == -1) {
            System.out.println("Exception message: " + Spectrometer.getWrapper().getLastException());
            System.out.println("Stack trace:\n" + Spectrometer.getWrapper().getLastExceptionStackTrace());
            return;
        }
        if (numberOfSpectrometersUSBFound == 0) {
            System.out.println("No spectrometers were found. Exiting the application.");
            JOptionPane.showMessageDialog(null, "No se encontraron espectrómetros conectados", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int spectrometerIndex = 0; spectrometerIndex < numberOfSpectrometersUSBFound; ++spectrometerIndex) {
            name = Spectrometer.getWrapper().getName(spectrometerIndex);
            serialNumber = Spectrometer.getWrapper().getSerialNumber(spectrometerIndex);
            channels = Spectrometer.getWrapper().getWrapperExtensions().getNumberOfEnabledChannels(spectrometerIndex);
            firmware = Spectrometer.getWrapper().getFirmwareVersion(spectrometerIndex);
            USBSpectrometer usbSpec = new USBSpectrometer(spectrometerIndex, name, serialNumber, firmware, channels);
            spectrometers.add(usbSpec);
            for (int i = 1; i <= usbSpec.getChannels(); i++) {
                usbSpec = new USBSpectrometer(spectrometerIndex, name, serialNumber, firmware, i);
                spectrometers.add(usbSpec);
            }

        }
        TreeSpec.loadSpectrometers();
    }

    public void openEthSpectrometer(String ip) {
        int specEthFound = Spectrometer.getWrapper().openNetworkSpectrometer(ip);
        if (specEthFound == -1) {
            JOptionPane.showMessageDialog(null, "Exception message: " + Spectrometer.getWrapper().getLastException()
                    + "Stack trace:\n" + Spectrometer.getWrapper().getLastExceptionStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        name = Spectrometer.getWrapper().getName(specEthFound);
        serialNumber = Spectrometer.getWrapper().getSerialNumber(specEthFound);
        channels = Spectrometer.getWrapper().getWrapperExtensions().getNumberOfEnabledChannels(specEthFound);
        firmware = Spectrometer.getWrapper().getFirmwareVersion(specEthFound);
        EthSpectrometer ethSpec = new EthSpectrometer(specEthFound, name, serialNumber, firmware, channels, ip);
        spectrometers.add(ethSpec);
        for (int i = 1; i <= ethSpec.getChannels(); i++) {
            ethSpec = new EthSpectrometer(specEthFound, name, serialNumber, firmware, i, ip);
            spectrometers.add(ethSpec);
        }
        TreeSpec.loadSpectrometers();
    }
}
