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
import java.beans.PropertyChangeSupport;

import java.util.Calendar;

import net.zypr.api.enums.CardinalDirection;
import net.zypr.api.vo.GeoPositionVO;
import net.zypr.gps.enums.DataStatus;
import net.zypr.gps.enums.FixMode;
import net.zypr.gps.enums.FixQuality;
import net.zypr.gps.enums.OperatingMode;

class GPSData
{
  private Calendar _utcDateTime;
  private DataStatus _dataStatus;
  private GeoPositionVO _position;
  private double _speedOverGroundInKnots;
  private double _trueCourseInDegrees;
  private double _magneticVariationInDegrees;
  private CardinalDirection _magneticVariationDirection;
  private FixQuality _fixQuality;
  private int _numberOfSatellitesBeingTracked;
  private double _horizontalDilutionOfPosition;
  private double _geoidalSeparation;
  private OperatingMode _operatingMode;
  private FixMode _fixMode;
  private double _positionDilutionOfPrecision;
  private double _horizontalDilutionOfPrecision;
  private double _verticalDilutionOfPrecision;
  private int[] _satellitesBeingTracked;
  private String _version1;
  private String _version2;
  private Throwable _parsingError;
  private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  public GPSData()
  {
    super();
  }

  public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
  }

  public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
  }

  public void removeAllPropertyChangeListener()
  {
    PropertyChangeListener[] propertyChangeListeners = propertyChangeSupport.getPropertyChangeListeners();
    for (int index = 0; index < propertyChangeListeners.length; index++)
      propertyChangeSupport.removePropertyChangeListener(propertyChangeListeners[index]);
  }

  public void setDataStatus(DataStatus dataStatus)
  {
    DataStatus oldDataStatus = _dataStatus;
    this._dataStatus = dataStatus;
    propertyChangeSupport.firePropertyChange("DataStatus", oldDataStatus, dataStatus);
  }

  public DataStatus getDataStatus()
  {
    return _dataStatus;
  }

  public void setPosition(GeoPositionVO position)
  {
    GeoPositionVO oldPosition = _position;
    this._position = position;
    propertyChangeSupport.firePropertyChange("Position", oldPosition, position);
  }

  public GeoPositionVO getPosition()
  {
    return _position;
  }

  public void setSpeedOverGroundInKnots(double speedOverGroundInKnots)
  {
    double oldSpeedOverGroundInKnots = _speedOverGroundInKnots;
    this._speedOverGroundInKnots = speedOverGroundInKnots;
    propertyChangeSupport.firePropertyChange("SpeedOverGroundInKnots", oldSpeedOverGroundInKnots, speedOverGroundInKnots);
  }

  public double getSpeedOverGroundInKnots()
  {
    return _speedOverGroundInKnots;
  }

  public void setTrueCourseInDegrees(double trueCourseInDegrees)
  {
    double oldTrueCourseInDegrees = _trueCourseInDegrees;
    this._trueCourseInDegrees = trueCourseInDegrees;
    propertyChangeSupport.firePropertyChange("TrueCourseInDegrees", oldTrueCourseInDegrees, trueCourseInDegrees);
  }

  public double getTrueCourseInDegrees()
  {
    return _trueCourseInDegrees;
  }

  public void setMagneticVariationInDegrees(double magneticVariationInDegrees)
  {
    double oldMagneticVariationInDegrees = _magneticVariationInDegrees;
    this._magneticVariationInDegrees = magneticVariationInDegrees;
    propertyChangeSupport.firePropertyChange("MagneticVariationInDegrees", oldMagneticVariationInDegrees, magneticVariationInDegrees);
  }

  public double getMagneticVariationInDegrees()
  {
    return _magneticVariationInDegrees;
  }

  public void setMagneticVariationDirection(CardinalDirection magneticVariationDirection)
  {
    CardinalDirection oldMagneticVariationDirection = _magneticVariationDirection;
    this._magneticVariationDirection = magneticVariationDirection;
    propertyChangeSupport.firePropertyChange("MagneticVariationDirection", oldMagneticVariationDirection, magneticVariationDirection);
  }

  public CardinalDirection getMagneticVariationDirection()
  {
    return _magneticVariationDirection;
  }

  public void setFixQuality(FixQuality fixQuality)
  {
    FixQuality oldFixQuality = _fixQuality;
    this._fixQuality = fixQuality;
    propertyChangeSupport.firePropertyChange("FixQuality", oldFixQuality, fixQuality);
  }

  public FixQuality getFixQuality()
  {
    return _fixQuality;
  }

  public void setNumberOfSatellitesBeingTracked(int numberOfSatellitesBeingTracked)
  {
    int oldNumberOfSatellitesBeingTracked = _numberOfSatellitesBeingTracked;
    this._numberOfSatellitesBeingTracked = numberOfSatellitesBeingTracked;
    propertyChangeSupport.firePropertyChange("NumberOfSatellitesBeingTracked", oldNumberOfSatellitesBeingTracked, numberOfSatellitesBeingTracked);
  }

  public int getNumberOfSatellitesBeingTracked()
  {
    return _numberOfSatellitesBeingTracked;
  }

  public void setHorizontalDilutionOfPosition(double horizontalDilutionOfPosition)
  {
    double oldHorizontalDilutionOfPosition = _horizontalDilutionOfPosition;
    this._horizontalDilutionOfPosition = horizontalDilutionOfPosition;
    propertyChangeSupport.firePropertyChange("HorizontalDilutionOfPosition", oldHorizontalDilutionOfPosition, horizontalDilutionOfPosition);
  }

  public double getHorizontalDilutionOfPosition()
  {
    return _horizontalDilutionOfPosition;
  }

  public void setGeoidalSeparation(double geoidalSeparation)
  {
    double oldGeoidalSeparation = _geoidalSeparation;
    this._geoidalSeparation = geoidalSeparation;
    propertyChangeSupport.firePropertyChange("GeoidalSeparation", oldGeoidalSeparation, geoidalSeparation);
  }

  public double getGeoidalSeparation()
  {
    return _geoidalSeparation;
  }

  public void setPositionDilutionOfPrecision(double positionDilutionOfPrecision)
  {
    double oldPositionDilutionOfPrecision = _positionDilutionOfPrecision;
    this._positionDilutionOfPrecision = positionDilutionOfPrecision;
    propertyChangeSupport.firePropertyChange("PositionDilutionOfPrecision", oldPositionDilutionOfPrecision, positionDilutionOfPrecision);
  }

  public double getPositionDilutionOfPrecision()
  {
    return _positionDilutionOfPrecision;
  }

  public void setHorizontalDilutionOfPrecision(double horizontalDilutionOfPrecision)
  {
    double oldHorizontalDilutionOfPrecision = _horizontalDilutionOfPrecision;
    this._horizontalDilutionOfPrecision = horizontalDilutionOfPrecision;
    propertyChangeSupport.firePropertyChange("HorizontalDilutionOfPrecision", oldHorizontalDilutionOfPrecision, horizontalDilutionOfPrecision);
  }

  public double getHorizontalDilutionOfPrecision()
  {
    return _horizontalDilutionOfPrecision;
  }

  public void setVerticalDilutionOfPrecision(double verticalDilutionOfPrecision)
  {
    double oldVerticalDilutionOfPrecision = _verticalDilutionOfPrecision;
    this._verticalDilutionOfPrecision = verticalDilutionOfPrecision;
    propertyChangeSupport.firePropertyChange("VerticalDilutionOfPrecision", oldVerticalDilutionOfPrecision, verticalDilutionOfPrecision);
  }

  public double getVerticalDilutionOfPrecision()
  {
    return _verticalDilutionOfPrecision;
  }

  public void setOperatingMode(OperatingMode operatingMode)
  {
    OperatingMode oldOperatingMode = _operatingMode;
    this._operatingMode = operatingMode;
    propertyChangeSupport.firePropertyChange("OperatingMode", oldOperatingMode, operatingMode);
  }

  public OperatingMode getOperatingMode()
  {
    return _operatingMode;
  }

  public void setFixMode(FixMode fixMode)
  {
    FixMode oldFixMode = _fixMode;
    this._fixMode = fixMode;
    propertyChangeSupport.firePropertyChange("FixMode", oldFixMode, fixMode);
  }

  public FixMode getFixMode()
  {
    return _fixMode;
  }

  public void setSatellitesBeingTracked(int[] satellitesBeingTracked)
  {
    int[] oldSatellitesBeingTracked = _satellitesBeingTracked;
    this._satellitesBeingTracked = satellitesBeingTracked;
    propertyChangeSupport.firePropertyChange("SatellitesBeingTracked", oldSatellitesBeingTracked, satellitesBeingTracked);
  }

  public int[] getSatellitesBeingTracked()
  {
    return _satellitesBeingTracked;
  }

  public void setUtcDateTime(Calendar utcDateTime)
  {
    Calendar oldUtcDateTime = _utcDateTime;
    this._utcDateTime = utcDateTime;
    propertyChangeSupport.firePropertyChange("UtcDateTime", oldUtcDateTime, utcDateTime);
  }

  public Calendar getUtcDateTime()
  {
    return _utcDateTime;
  }

  public void setVersion1(String version1)
  {
    String oldVersion1 = _version1;
    this._version1 = version1;
    propertyChangeSupport.firePropertyChange("Version1", oldVersion1, version1);
  }

  public String getVersion1()
  {
    return _version1;
  }

  public void setVersion2(String version2)
  {
    String oldVersion2 = _version2;
    this._version2 = version2;
    propertyChangeSupport.firePropertyChange("Version2", oldVersion2, version2);
  }

  public String getVersion2()
  {
    return _version2;
  }

  public void setParsingError(Throwable parsingError)
  {
    Throwable oldParsingError = _parsingError;
    this._parsingError = parsingError;
    propertyChangeSupport.firePropertyChange("ParsingError", oldParsingError, parsingError);
  }

  public Throwable getParsingError()
  {
    return _parsingError;
  }
}
