/**
 * 
 * Initial version of this code (c) 2009-2011 Media Tuners LLC with a full license to Pioneer Corporation.
 * 
 * Pioneer Corporation licenses this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 * 
 */


package net.zypr.gps;

import java.beans.PropertyChangeListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import net.zypr.gps.exceptions.GPSDeviceUnavailableException;
import net.zypr.gps.gpx.Gpx;
import net.zypr.gui.Configuration;
import net.zypr.gui.utils.Debug;

public class GPSDevice
{
  private static final GPSDevice INSTANCE = new GPSDevice();
  private final static byte GPS_MOUSE_MODE_ON[] =
  { (byte) 0xA0, (byte) 0xA2, (byte) 0x00, (byte) 0x02, (byte) 0xBC, (byte) 0x01, (byte) 0x00, (byte) 0xBD, (byte) 0xB0, (byte) 0xB3 };
  private final static byte GPS_MOUSE_MODE_OFF[] =
  { (byte) 0xA0, (byte) 0xA2, (byte) 0x00, (byte) 0x02, (byte) 0xBC, (byte) 0x00, (byte) 0x00, (byte) 0xBC, (byte) 0xB0, (byte) 0xB3 };
  private static final GPSData _gpsData = new GPSData();
  private BufferedReader _bufferedReader = null;
  private FileOutputStream _fileOutputStream = null;
  private static GPSParser _gpsParser = null;
  private static GPXThread _gpxThread = null;
  private List<Gpx.Trk.Trkseg.Trkpt> _gpxData = null;

  private GPSDevice()
  {
    super();
    if (Configuration.getInstance().getBooleanProperty("gps-file-override", false))
      {
        JAXBContext jaxbContext = null;
        Unmarshaller unmarshaller = null;
        try
          {
            jaxbContext = JAXBContext.newInstance("net.zypr.gps.gpx");
            unmarshaller = jaxbContext.createUnmarshaller();
            Gpx _gpx = (Gpx) unmarshaller.unmarshal(new File(Configuration.getInstance().getProperty("gps-file-location", "")));
            _gpxData = ((Gpx.Trk.Trkseg) _gpx.getTrk().get(0).getTrkseg().get(0)).getTrkpt();
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
            _gpxData = null;
          }
      }
    if (_gpxData == null)
      {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
          @Override
          public void run()
          {
            super.run();
            GPSDevice.getInstance().closeDevice();
          }
        });
      }
  }

  public static GPSDevice getInstance()
  {
    return (INSTANCE);
  }

  public void openDevice(String devicePath)
    throws GPSDeviceUnavailableException
  {
    if (_gpxData != null)
      {
        if (_gpxThread != null && _gpxThread.isRunning())
          return;
        _gpxThread = new GPXThread(_gpsData, _gpxData);
        _gpxThread.start();
      }
    else
      {
        if (_gpsParser != null && _gpsParser.isRunning())
          return;
        try
          {
            if (Runtime.getRuntime().exec(new String[]
                { "stty", "-F", devicePath, "-parenb", "cs8", "cread", "115200", "-cstopb" }).waitFor() != 0)
              throw new GPSDeviceUnavailableException("Unable to run STTY command.");
          }
        catch (IOException ioException)
          {
            throw new GPSDeviceUnavailableException(ioException);
          }
        catch (InterruptedException interruptedException)
          {
            throw new GPSDeviceUnavailableException(interruptedException);
          }
        try
          {
            _bufferedReader = new BufferedReader(new FileReader(devicePath));
            _fileOutputStream = new FileOutputStream(devicePath);
            _fileOutputStream.write(GPS_MOUSE_MODE_ON);
            _fileOutputStream.flush();
            _gpsParser = new GPSParser(_gpsData, _bufferedReader);
            _gpsParser.start();
          }
        catch (FileNotFoundException fileNotFoundException)
          {
            throw new GPSDeviceUnavailableException(fileNotFoundException);
          }
        catch (IOException ioException)
          {
            throw new GPSDeviceUnavailableException(ioException);
          }
      }
  }

  public void closeDevice()
  {
    if (_gpsParser != null)
      {
        _gpsParser.setRunning(false);
        _gpsParser = null;
      }
    if (_fileOutputStream != null)
      {
        try
          {
            _fileOutputStream.write(GPS_MOUSE_MODE_OFF);
            _fileOutputStream.flush();
            _fileOutputStream.close();
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        finally
          {
            _fileOutputStream = null;
          }
      }
    if (_bufferedReader != null)
      {
        try
          {
            _bufferedReader.close();
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        finally
          {
            _bufferedReader = null;
          }
      }
  }

  public boolean isAvailable()
  {
    return ((_gpsParser != null && _gpsParser.isRunning()) || (_gpxThread != null && _gpxThread.isRunning()));
  }

  public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    _gpsData.addPropertyChangeListener(propertyChangeListener);
  }

  public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    _gpsData.removePropertyChangeListener(propertyChangeListener);
  }

  public void removeAllPropertyChangeListener()
  {
    _gpsData.removeAllPropertyChangeListener();
  }

  public GPSData getGPSData()
  {
    return (_gpsData);
  }
}
