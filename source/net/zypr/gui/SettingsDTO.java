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


package net.zypr.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import net.zypr.api.enums.AudioSourceType;
import net.zypr.api.enums.DateFormatPattern;
import net.zypr.api.enums.DistanceUnit;
import net.zypr.api.enums.TemperatureScale;
import net.zypr.api.enums.TimeFormatPattern;
import net.zypr.api.vo.AddressVO;

public class SettingsDTO
{
  private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
  private int _voiceNoteReadRadius = 3;
  private AudioSourceType _audioSource = AudioSourceType.LOCAL;
  private String _localAudioPath = "./";
  private boolean _ttsMessageHeader = true;
  private boolean _ttsMessageText = true;
  private boolean _ttsContactFeed = true;
  private boolean _ttsFollowUpList = true;
  private boolean _updateMyLocation = true;
  private boolean _ttsCardHeader = true;
  private TimeFormatPattern _timeFormatPattern = TimeFormatPattern.HOUR12COLON;
  private DateFormatPattern _dateFormatPattern = DateFormatPattern.MMDDYYYYSLASH;
  private DistanceUnit _distanceUnit = DistanceUnit.MILE;
  private TemperatureScale _temperatureScale = TemperatureScale.FAHRENHEIT;
  private Set _hiddenContacts = new HashSet();
  private Vector<String> _companyNamelist = new Vector<String>();
  private Vector<AddressVO> _userAddresslist = new Vector<AddressVO>();

  public SettingsDTO()
  {
    super();
  }

  public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
  }

  public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
  }

  public synchronized void setDistanceUnit(DistanceUnit distanceUnit)
  {
    DistanceUnit oldDistanceUnit = _distanceUnit;
    this._distanceUnit = distanceUnit;
    propertyChangeSupport.firePropertyChange("DistanceUnit", oldDistanceUnit, distanceUnit);
  }

  public synchronized DistanceUnit getDistanceUnit()
  {
    return _distanceUnit;
  }

  public synchronized void setTemperatureScale(TemperatureScale temperatureScale)
  {
    TemperatureScale oldTemperatureScale = _temperatureScale;
    this._temperatureScale = temperatureScale;
    propertyChangeSupport.firePropertyChange("TemperatureScale", oldTemperatureScale, temperatureScale);
  }

  public synchronized TemperatureScale getTemperatureScale()
  {
    return _temperatureScale;
  }

  public synchronized void setVoiceNoteReadRadius(int voiceNoteReadRadius)
  {
    int oldVoiceNoteReadRadius = _voiceNoteReadRadius;
    this._voiceNoteReadRadius = voiceNoteReadRadius;
    propertyChangeSupport.firePropertyChange("VoiceNoteReadRadius", oldVoiceNoteReadRadius, voiceNoteReadRadius);
  }

  public synchronized int getVoiceNoteReadRadius()
  {
    return _voiceNoteReadRadius;
  }

  public synchronized void setAudioSource(AudioSourceType audioSource)
  {
    AudioSourceType oldAudioSource = _audioSource;
    this._audioSource = audioSource;
    propertyChangeSupport.firePropertyChange("AudioSource", oldAudioSource, audioSource);
  }

  public synchronized AudioSourceType getAudioSource()
  {
    return _audioSource;
  }

  public synchronized void setLocalAudioPath(String localAudioPath)
  {
    String oldLocalAudioPath = _localAudioPath;
    this._localAudioPath = localAudioPath;
    propertyChangeSupport.firePropertyChange("LocalAudioPath", oldLocalAudioPath, localAudioPath);
  }

  public synchronized String getLocalAudioPath()
  {
    return _localAudioPath;
  }

  public synchronized void setTTSMessageHeader(boolean ttsMessageHeader)
  {
    boolean oldTTSMessageHeader = _ttsMessageHeader;
    this._ttsMessageHeader = ttsMessageHeader;
    propertyChangeSupport.firePropertyChange("TTSMessageHeader", oldTTSMessageHeader, ttsMessageHeader);
  }

  public synchronized boolean isTTSMessageHeader()
  {
    return (_ttsMessageHeader);
  }

  public synchronized void setTTSMessageText(boolean ttsMessageText)
  {
    boolean oldTTSMessageText = _ttsMessageText;
    this._ttsMessageText = ttsMessageText;
    propertyChangeSupport.firePropertyChange("TTSMessageText", oldTTSMessageText, ttsMessageText);
  }

  public synchronized boolean isTTSMessageText()
  {
    return (_ttsMessageText);
  }

  public synchronized void setTTSContactFeed(boolean ttsContactFeed)
  {
    boolean oldTTSContactFeed = _ttsContactFeed;
    this._ttsContactFeed = ttsContactFeed;
    propertyChangeSupport.firePropertyChange("TTSContactFeed", oldTTSContactFeed, ttsContactFeed);
  }

  public synchronized boolean isTTSContactFeed()
  {
    return (_ttsContactFeed);
  }

  public synchronized void setTTSFollowUpList(boolean ttsFollowUpList)
  {
    boolean oldTTSFollowUpList = _ttsFollowUpList;
    this._ttsFollowUpList = ttsFollowUpList;
    propertyChangeSupport.firePropertyChange("TTSFollowUpList", oldTTSFollowUpList, ttsFollowUpList);
  }

  public synchronized boolean isTTSFollowUpList()
  {
    return (_ttsFollowUpList);
  }

  public synchronized void setUpdateMyLocation(boolean updateMyLocation)
  {
    boolean oldUpdateMyLocation = _updateMyLocation;
    this._updateMyLocation = updateMyLocation;
    propertyChangeSupport.firePropertyChange("UpdateMyLocation", oldUpdateMyLocation, updateMyLocation);
  }

  public synchronized boolean isUpdateMyLocation()
  {
    return (_updateMyLocation);
  }

  public synchronized void setTimeFormatPattern(TimeFormatPattern timeFormatPattern)
  {
    TimeFormatPattern oldTimeFormatPattern = _timeFormatPattern;
    this._timeFormatPattern = timeFormatPattern;
    propertyChangeSupport.firePropertyChange("TimeFormatPattern", oldTimeFormatPattern, timeFormatPattern);
  }

  public synchronized TimeFormatPattern getTimeFormatPattern()
  {
    return (_timeFormatPattern);
  }

  public synchronized void setDateFormatPattern(DateFormatPattern dateFormatPattern)
  {
    DateFormatPattern oldDateFormatPattern = _dateFormatPattern;
    this._dateFormatPattern = dateFormatPattern;
    propertyChangeSupport.firePropertyChange("DateFormatPattern", oldDateFormatPattern, dateFormatPattern);
  }

  public synchronized DateFormatPattern getDateFormatPattern()
  {
    return (_dateFormatPattern);
  }

  public synchronized void hideContact(String contactID, String service)
  {
    hideContact(contactID + "@" + service);
  }

  public synchronized void hideContact(String id)
  {
    _hiddenContacts.add(id);
  }

  public synchronized void showContact(String contactID, String service)
  {
    showContact(contactID + "@" + service);
  }

  public synchronized void showContact(String id)
  {
    _hiddenContacts.remove(id);
  }

  public synchronized boolean isHiddenContact(String contactID, String service)
  {
    return (isHiddenContact(contactID + "@" + service));
  }

  public synchronized boolean isHiddenContact(String id)
  {
    return (_hiddenContacts.contains(id));
  }

  public synchronized void setHiddenContacts(Set hiddenContacts)
  {
    Set oldHiddenContacts = _hiddenContacts;
    this._hiddenContacts = hiddenContacts;
    propertyChangeSupport.firePropertyChange("HiddenContacts", oldHiddenContacts, hiddenContacts);
  }

  public synchronized Set getHiddenContacts()
  {
    return (_hiddenContacts);
  }

  public synchronized void setTTSCardHeader(boolean ttsCardHeader)
  {
    boolean oldTTSCardHeader = _ttsCardHeader;
    this._ttsCardHeader = ttsCardHeader;
    propertyChangeSupport.firePropertyChange("TTSCardHeader", oldTTSCardHeader, ttsCardHeader);
  }

  public synchronized boolean isTTSCardHeader()
  {
    return (_ttsCardHeader);
  }

  public synchronized void setCompanyNamelist(Vector<String> companyNamelist)
  {
    Vector<String> oldCompanyNamelist = _companyNamelist;
    this._companyNamelist = companyNamelist;
    propertyChangeSupport.firePropertyChange("CompanyNamelist", oldCompanyNamelist, companyNamelist);
  }

  public synchronized Vector<String> getCompanyNamelist()
  {
    return (_companyNamelist);
  }

  public synchronized void addToCompanyNamelist(String companyName)
  {
    int index = _companyNamelist.indexOf(companyName);
    if (index != -1)
      _companyNamelist.remove(index);
    _companyNamelist.add(0, companyName);
    while (_companyNamelist.size() >= 20)
      _companyNamelist.remove(_companyNamelist.size() - 1);
  }

  public synchronized void setUserAddresslist(Vector<AddressVO> userAddresslist)
  {
    Vector<AddressVO> oldUserAddresslist = _userAddresslist;
    this._userAddresslist = userAddresslist;
    propertyChangeSupport.firePropertyChange("UserAddresslist", oldUserAddresslist, userAddresslist);
  }

  public synchronized Vector<AddressVO> getUserAddresslist()
  {
    return (_userAddresslist);
  }

  public synchronized void addToUserAddresslist(AddressVO address)
  {
    int index = _userAddresslist.indexOf(address);
    if (index != -1)
      _userAddresslist.remove(index);
    _userAddresslist.add(0, address);
    while (_userAddresslist.size() >= 20)
      _userAddresslist.remove(_userAddresslist.size() - 1);
  }
}
