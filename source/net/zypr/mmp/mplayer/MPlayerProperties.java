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


package net.zypr.mmp.mplayer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

class MPlayerProperties
{
  private String _streamURL;
  private String _name;
  private String[] _genre;
  private String _website;
  private boolean _publicStream;
  private double _streamBitrate;
  private String _icyStreamTitle;
  private String _icyStreamUrl;
  private float _audioRate;
  private int _audioSizeInBits;
  private int _audioChannels;
  private boolean _audioSigned;
  private boolean _audioBigEndian;
  private double _audioBitrate;
  private String _audioCodec;
  private boolean _playbackStarted = false;
  private int _playbackVolume = 100;
  private boolean _playbackMuted = false;
  private double _cacheFillPercent;
  private int _cacheFillBytes;
  private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  public MPlayerProperties()
  {
    super();
  }

  public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener)
  {
    propertyChangeSupport.addPropertyChangeListener(propertyName, propertyChangeListener);
  }

  public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
  }

  public void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener)
  {
    propertyChangeSupport.removePropertyChangeListener(propertyName, propertyChangeListener);
  }

  public void removeAllPropertyChangeListener()
  {
    PropertyChangeListener[] propertyChangeListeners = propertyChangeSupport.getPropertyChangeListeners();
    for (int index = 0; index < propertyChangeListeners.length; index++)
      propertyChangeSupport.removePropertyChangeListener(propertyChangeListeners[index]);
  }

  public void setStreamURL(String streamURL)
  {
    String oldStreamURL = this._streamURL;
    this._streamURL = streamURL;
    propertyChangeSupport.firePropertyChange("StreamURL", oldStreamURL, streamURL);
  }

  public String getStreamURL()
  {
    return (_streamURL);
  }

  public void setName(String name)
  {
    String oldName = this._name;
    this._name = name;
    propertyChangeSupport.firePropertyChange("Name", oldName, name);
  }

  public String getName()
  {
    return (_name);
  }

  public void setGenre(String[] genre)
  {
    String[] oldGenre = this._genre;
    this._genre = genre;
    propertyChangeSupport.firePropertyChange("Genre", oldGenre, genre);
  }

  public String[] getGenre()
  {
    return (_genre);
  }

  public void setWebsite(String website)
  {
    String oldWebsite = this._website;
    this._website = website;
    propertyChangeSupport.firePropertyChange("Website", oldWebsite, website);
  }

  public String getWebsite()
  {
    return (_website);
  }

  public void setPublicStream(boolean publicStream)
  {
    boolean oldPublicStream = this._publicStream;
    this._publicStream = publicStream;
    propertyChangeSupport.firePropertyChange("PublicStream", oldPublicStream, publicStream);
  }

  public boolean isPublicStream()
  {
    return (_publicStream);
  }

  public void setStreamBitrate(double streamBitrate)
  {
    double oldStreamBitrate = this._streamBitrate;
    this._streamBitrate = streamBitrate;
    propertyChangeSupport.firePropertyChange("StreamBitrate", oldStreamBitrate, streamBitrate);
  }

  public double getStreamBitrate()
  {
    return (_streamBitrate);
  }

  public void setIcyStreamTitle(String icyStreamTitle)
  {
    String oldIcyStreamTitle = this._icyStreamTitle;
    this._icyStreamTitle = icyStreamTitle;
    propertyChangeSupport.firePropertyChange("IcyStreamTitle", oldIcyStreamTitle, icyStreamTitle);
  }

  public String getIcyStreamTitle()
  {
    return (_icyStreamTitle);
  }

  public void setIcyStreamUrl(String icyStreamUrl)
  {
    String oldIcyStreamUrl = this._icyStreamUrl;
    this._icyStreamUrl = icyStreamUrl;
    propertyChangeSupport.firePropertyChange("IcyStreamUrl", oldIcyStreamUrl, icyStreamUrl);
  }

  public String getIcyStreamUrl()
  {
    return (_icyStreamUrl);
  }

  public void setAudioRate(float audioRate)
  {
    float oldAudioRate = this._audioRate;
    this._audioRate = audioRate;
    propertyChangeSupport.firePropertyChange("AudioRate", oldAudioRate, audioRate);
  }

  public float getAudioRate()
  {
    return (_audioRate);
  }

  public void setAudioSizeInBits(int audioSizeInBits)
  {
    int oldAudioSizeInBits = this._audioSizeInBits;
    this._audioSizeInBits = audioSizeInBits;
    propertyChangeSupport.firePropertyChange("AudioSizeInBits", oldAudioSizeInBits, audioSizeInBits);
  }

  public int getAudioSizeInBits()
  {
    return (_audioSizeInBits);
  }

  public void setAudioChannels(int audioChannels)
  {
    int oldAudioChannels = this._audioChannels;
    this._audioChannels = audioChannels;
    propertyChangeSupport.firePropertyChange("AudioChannels", oldAudioChannels, audioChannels);
  }

  public int getAudioChannels()
  {
    return (_audioChannels);
  }

  public void setAudioSigned(boolean audioSigned)
  {
    boolean oldAudioSigned = this._audioSigned;
    this._audioSigned = audioSigned;
    propertyChangeSupport.firePropertyChange("AudioSigned", oldAudioSigned, audioSigned);
  }

  public boolean isAudioSigned()
  {
    return (_audioSigned);
  }

  public void setAudioBigEndian(boolean audioBigEndian)
  {
    boolean oldAudioBigEndian = this._audioBigEndian;
    this._audioBigEndian = audioBigEndian;
    propertyChangeSupport.firePropertyChange("AudioBigEndian", oldAudioBigEndian, audioBigEndian);
  }

  public boolean isAudioBigEndian()
  {
    return (_audioBigEndian);
  }

  public void setAudioBitrate(double audioBitrate)
  {
    double oldAudioBitrate = this._audioBitrate;
    this._audioBitrate = audioBitrate;
    propertyChangeSupport.firePropertyChange("AudioBitrate", oldAudioBitrate, audioBitrate);
  }

  public double getAudioBitrate()
  {
    return (_audioBitrate);
  }

  public void setAudioCodec(String audioCodec)
  {
    String oldAudioCodec = this._audioCodec;
    this._audioCodec = audioCodec;
    propertyChangeSupport.firePropertyChange("AudioCodec", oldAudioCodec, audioCodec);
  }

  public String getAudioCodec()
  {
    return (_audioCodec);
  }

  public void setPlaybackStarted(boolean playbackStarted)
  {
    boolean oldPlaybackStarted = this._playbackStarted;
    this._playbackStarted = playbackStarted;
    propertyChangeSupport.firePropertyChange("PlaybackStarted", oldPlaybackStarted, playbackStarted);
  }

  public boolean isPlaybackStarted()
  {
    return (_playbackStarted);
  }

  public void setPlaybackVolume(int playbackVolume)
  {
    int oldPlaybackVolume = this._playbackVolume;
    setPlaybackMuted(false);
    this._playbackVolume = playbackVolume;
    propertyChangeSupport.firePropertyChange("PlaybackVolume", oldPlaybackVolume, playbackVolume);
  }

  public int getPlaybackVolume()
  {
    return (_playbackVolume);
  }

  public void setPlaybackMuted(boolean playbackMuted)
  {
    boolean oldPlaybackMuted = this._playbackMuted;
    this._playbackMuted = playbackMuted;
    propertyChangeSupport.firePropertyChange("PlaybackMuted", oldPlaybackMuted, playbackMuted);
  }

  public boolean isPlaybackMuted()
  {
    return (_playbackMuted);
  }

  public void setCacheFillPercent(double cacheFillPercent)
  {
    double oldCacheFillPercent = _cacheFillPercent;
    this._cacheFillPercent = cacheFillPercent;
    propertyChangeSupport.firePropertyChange("CacheFillPercent", oldCacheFillPercent, cacheFillPercent);
  }

  public double getCacheFillPercent()
  {
    return (_cacheFillPercent);
  }

  public void setCacheFillBytes(int cacheFillBytes)
  {
    int oldCacheFillBytes = _cacheFillBytes;
    this._cacheFillBytes = cacheFillBytes;
    propertyChangeSupport.firePropertyChange("CacheFillBytes", oldCacheFillBytes, cacheFillBytes);
  }

  public int getCacheFillBytes()
  {
    return (_cacheFillBytes);
  }

  public void clear()
  {
    setAudioBigEndian(false);
    setAudioBitrate(0);
    setAudioChannels(0);
    setAudioCodec(null);
    setAudioRate(0);
    setAudioSigned(false);
    setAudioSizeInBits(0);
    setGenre(null);
    setIcyStreamTitle(null);
    setIcyStreamUrl(null);
    setName(null);
    setPlaybackMuted(false);
    setPlaybackStarted(false);
    setPublicStream(false);
    setStreamBitrate(0);
    setStreamURL(null);
    setWebsite(null);
    setCacheFillPercent(0);
    setCacheFillBytes(0);
  }
}
