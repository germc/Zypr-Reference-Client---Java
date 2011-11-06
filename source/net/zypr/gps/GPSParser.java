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

import java.io.BufferedReader;
import java.io.IOException;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.zypr.api.enums.CardinalDirection;
import net.zypr.api.vo.GeoPositionVO;
import net.zypr.gps.enums.DataStatus;
import net.zypr.gps.enums.FixMode;
import net.zypr.gps.enums.FixQuality;
import net.zypr.gps.enums.OperatingMode;
import net.zypr.gui.utils.Debug;

class GPSParser
  extends Thread
{
  private GPSData _gpsData;
  private BufferedReader _bufferedReader;
  private boolean _running = true;

  public GPSParser(GPSData gpsData, BufferedReader bufferedReader)
  {
    super();
    _gpsData = gpsData;
    _bufferedReader = bufferedReader;
  }

  @Override
  public void run()
  {
    super.run();
    try
      {
        String nmeaSentence;
        while ((nmeaSentence = _bufferedReader.readLine()) != null && _running)
          {
            nmeaSentence = nmeaSentence.replaceAll("[^\\p{Alnum}\\p{Punct}]", "");
            int start = nmeaSentence.indexOf("$GP");
            if (start > 0)
              nmeaSentence = nmeaSentence.substring(start);
            if (!nmeaSentence.equals(""))
              {
                String[] nmeaData = nmeaSentence.split(",|[*]");
                if (nmeaData[0].equals("$GPGGA"))
                  GPGGA(nmeaData);
                else if (nmeaData[0].equals("$GPGSA"))
                  GPGSA(nmeaData);
                else if (nmeaData[0].equals("$GPRMC"))
                  GPRMC(nmeaData);
                else if (nmeaData[0].equals("$GPGSV"))
                  GPGSV(nmeaData);
                else if (nmeaData[0].equals("$PSRFTXT"))
                  PSRFTXT(nmeaData);
                else
                  Debug.print("NOT IMPLEMENTED PARSER FOR NMEA DATA : " + nmeaSentence);
              }
          }
      }
    catch (IOException ioException)
      {
        _running = false;
        _gpsData.setParsingError(ioException);
        Debug.displayStack(this, ioException);
      }
    catch (Throwable throwable)
      {
        Debug.displayStack(this, throwable);
      }
  }

  private void GPGGA(String[] data)
  {
    try
      {
        int hour = Integer.parseInt(data[1].substring(0, 2));
        int minute = Integer.parseInt(data[1].substring(2, 4));
        int seconds = Integer.parseInt(data[1].substring(4, 6));
        Calendar calendar = _gpsData.getUtcDateTime() == null ? Calendar.getInstance() : _gpsData.getUtcDateTime();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        _gpsData.setUtcDateTime(new GregorianCalendar(year, month, day, hour, minute, seconds));
      }
    catch (Exception exception)
      {
        return;
      }
    try
      {
        _gpsData.setPosition(new GeoPositionVO(Double.parseDouble(data[2]), data[3].equals("N") ? CardinalDirection.N : CardinalDirection.S, Double.parseDouble(data[4]), data[5].equals("E") ? CardinalDirection.E : CardinalDirection.W, Double.parseDouble(data[9])));
      }
    catch (Exception exception)
      {
        return;
      }
    try
      {
        switch (Integer.parseInt(data[6]))
          {
            case 0:
              _gpsData.setFixQuality(FixQuality.INVALID);
              break;
            case 1:
              _gpsData.setFixQuality(FixQuality.GPS_FIX_SPS);
              break;
            case 2:
              _gpsData.setFixQuality(FixQuality.DGPS_FIX);
              break;
            case 3:
              _gpsData.setFixQuality(FixQuality.PPS_FIX);
              break;
            case 4:
              _gpsData.setFixQuality(FixQuality.REAL_TIME_KINEMATIC);
              break;
            case 5:
              _gpsData.setFixQuality(FixQuality.FLOAT_RTK);
              break;
            case 6:
              _gpsData.setFixQuality(FixQuality.ESTIMATED_DEAD_RECKONING);
              break;
            case 7:
              _gpsData.setFixQuality(FixQuality.MANUAL_INPUT_MODE);
              break;
            case 8:
              _gpsData.setFixQuality(FixQuality.SIMULATION_MODE);
              break;
          }
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        if (!data[7].equals(""))
          _gpsData.setNumberOfSatellitesBeingTracked(Integer.parseInt(data[7]));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        if (!data[8].equals(""))
          _gpsData.setHorizontalDilutionOfPosition(Double.parseDouble(data[8]));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        if (!data[11].equals(""))
          _gpsData.setGeoidalSeparation(Double.parseDouble(data[11]));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  private void GPGSA(String[] data)
  {
    if (data[1].equals("A"))
      _gpsData.setOperatingMode(OperatingMode.AUTOMATIC);
    else if (data[1].equals("M"))
      _gpsData.setOperatingMode(OperatingMode.MANUAL);
    try
      {
        switch (Integer.parseInt(data[2]))
          {
            case 0:
              _gpsData.setFixMode(FixMode.FIX_NOT_AVAILABLE);
              break;
            case 1:
              _gpsData.setFixMode(FixMode.FIX_2D);
              break;
            case 2:
              _gpsData.setFixMode(FixMode.FIX_3D);
              break;
          }
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    int[] satellitesBeingTracked = new int[12];
    for (int index = 0; index < 12; index++)
      {
        try
          {
            if (!data[3 + index].equals(""))
              satellitesBeingTracked[index] = Integer.parseInt(data[3 + index]);
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
    try
      {
        if (!data[15].equals(""))
          _gpsData.setPositionDilutionOfPrecision(Double.parseDouble(data[15]));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        if (!data[16].equals(""))
          _gpsData.setHorizontalDilutionOfPrecision(Double.parseDouble(data[16]));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        if (!data[17].equals(""))
          _gpsData.setVerticalDilutionOfPrecision(Double.parseDouble(data[17]));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  void GPRMC(String[] data)
  {
    try
      {
        int hour = Integer.parseInt(data[1].substring(0, 2));
        int minute = Integer.parseInt(data[1].substring(2, 4));
        int seconds = Integer.parseInt(data[1].substring(4, 6));
        int day = Integer.parseInt(data[9].substring(0, 2));
        int month = Integer.parseInt(data[9].substring(2, 4)) - 1;
        int year = Integer.parseInt(data[9].substring(4, 6)) + 2000;
        _gpsData.setUtcDateTime(new GregorianCalendar(year, month, day, hour, minute, seconds));
      }
    catch (Exception exception)
      {
        return;
      }
    if (data[2].equals("A"))
      _gpsData.setDataStatus(DataStatus.ACTIVE);
    else if (data[2].equals("V"))
      _gpsData.setDataStatus(DataStatus.VOID);
    try
      {
        GeoPositionVO position = _gpsData.getPosition();
        if (position == null)
          _gpsData.setPosition(new GeoPositionVO(Double.parseDouble(data[3]), data[4].equals("N") ? CardinalDirection.N : CardinalDirection.S, Double.parseDouble(data[5]), data[6].equals("E") ? CardinalDirection.E : CardinalDirection.W));
        else
          _gpsData.setPosition(new GeoPositionVO(Double.parseDouble(data[3]), data[4].equals("N") ? CardinalDirection.N : CardinalDirection.S, Double.parseDouble(data[5]), data[6].equals("E") ? CardinalDirection.E : CardinalDirection.W, position.getAltitude()));
      }
    catch (Exception exception)
      {
        return;
      }
    try
      {
        if (!data[7].equals(""))
          _gpsData.setSpeedOverGroundInKnots(Double.parseDouble(data[7]));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        if (!data[8].equals(""))
          _gpsData.setTrueCourseInDegrees(Double.parseDouble(data[8]));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        if (!data[10].equals(""))
          _gpsData.setMagneticVariationInDegrees(Double.parseDouble(data[10]));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    if (data[11].equals("E"))
      _gpsData.setMagneticVariationDirection(CardinalDirection.E);
    else if (data[11].equals("W"))
      _gpsData.setMagneticVariationDirection(CardinalDirection.W);
  }

  private void GPGSV(String[] data)
  {
  }

  private void PSRFTXT(String[] data)
  {
    if (data[1].startsWith("Version"))
      _gpsData.setVersion1(data[1].substring(8));
    else if (data[1].startsWith("Version2"))
      _gpsData.setVersion2(data[1].substring(9));
  }

  public void setRunning(boolean running)
  {
    this._running = running;
  }

  public boolean isRunning()
  {
    return (_running);
  }
}
