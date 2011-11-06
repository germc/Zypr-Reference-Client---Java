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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import net.zypr.api.vo.ItemVO;
import net.zypr.gui.Configuration;
import net.zypr.gui.utils.Debug;

public abstract class GenericPlayer
{
  public static boolean USE_MPLAYER = false;
  public static boolean USE_VLC = true;
  public MPlayerProperties _mPlayerProperties = new MPlayerProperties();
  public Process _process = null;
  public PrintStream _inputPrintStream = null;
  public BufferedReader _outputBufferedReader = null;
  public BufferedReader _errorBufferedReader = null;
  public MPlayerParser _outputListener = null;
  public MPlayerParser _errorListener = null;
  public VLCPlayer mVLCPlayerPtr = null;
  private ItemVO _currentRadioStationItem = null;

  public GenericPlayer()
  {
    if (Configuration.getInstance().getProperty("audio-player", "VLC").equals("MPlayer"))
      {
        USE_MPLAYER = true;
        USE_VLC = false;
      }
    else
      {
        USE_MPLAYER = false;
        USE_VLC = true;
      }
  }

  public void terminate()
  {
    if (USE_MPLAYER)
      {
        sendCommand("quit", new String[]
            { });
      }
    if (USE_VLC)
      {
        if (mVLCPlayerPtr != null)
          {
            try
              {
                mVLCPlayerPtr.close();
              }
            catch (IOException e)
              {
              }
          }
      }
  }

  public void setVLCPlayer(VLCPlayer obj)
  {
    mVLCPlayerPtr = obj;
  }

  public void setCurrentRadioStationItem(ItemVO currentRadioStationItem)
  {
    _currentRadioStationItem = currentRadioStationItem;
  }

  public ItemVO getCurrentRadioStationItem()
  {
    return (_currentRadioStationItem);
  }

  public boolean playRadioStationItem(ItemVO radioStationItem)
  {
    String newRadioStationURL = null;
    try
      {
        newRadioStationURL = radioStationItem.getAction("play").getHandlerURI();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
        return (false);
      }
    String currentRadioStationURL = null;
    try
      {
        if (getCurrentRadioStationItem() != null)
          currentRadioStationURL = getCurrentRadioStationItem().getAction("play").getHandlerURI();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    setCurrentRadioStationItem(radioStationItem);
    if (currentRadioStationURL == null || !currentRadioStationURL.equals(newRadioStationURL) || (currentRadioStationURL.equals(newRadioStationURL) && !MPlayer.getInstance().isPlaybackStarted()))
      {
        loadPlaylist(newRadioStationURL);
        return (true);
      }
    return (false);
  }

  public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    _mPlayerProperties.addPropertyChangeListener(propertyChangeListener);
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener)
  {
    _mPlayerProperties.addPropertyChangeListener(propertyName, propertyChangeListener);
  }

  public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    _mPlayerProperties.removePropertyChangeListener(propertyChangeListener);
  }

  public void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener)
  {
    _mPlayerProperties.removePropertyChangeListener(propertyName, propertyChangeListener);
  }

  public void removeAllPropertyChangeListener()
  {
    _mPlayerProperties.removeAllPropertyChangeListener();
  }

  public boolean isAvailable()
  {
    if (USE_MPLAYER)
      {
        return (_outputListener != null && _outputListener.isRunning() && _errorListener != null && _errorListener.isRunning());
      }
    if (USE_VLC)
      {
        if (mVLCPlayerPtr != null)
          return mVLCPlayerPtr.isAvailable();
        else
          return false;
      }
    return false;
  }

  public String getStreamURL()
  {
    return (_mPlayerProperties.getStreamURL());
  }

  public String getName()
  {
    return (_mPlayerProperties.getName());
  }

  public String[] getGenre()
  {
    return (_mPlayerProperties.getGenre());
  }

  public String getWebsite()
  {
    return (_mPlayerProperties.getWebsite());
  }

  public boolean isPublicStream()
  {
    return (_mPlayerProperties.isPublicStream());
  }

  public double getStreamBitrate()
  {
    return (_mPlayerProperties.getStreamBitrate());
  }

  public String getIcyStreamTitle()
  {
    return (_mPlayerProperties.getIcyStreamTitle());
  }

  public String getIcyStreamUrl()
  {
    return (_mPlayerProperties.getIcyStreamUrl());
  }

  public float getAudioRate()
  {
    return (_mPlayerProperties.getAudioRate());
  }

  public int getAudioSizeInBits()
  {
    return (_mPlayerProperties.getAudioSizeInBits());
  }

  public int getAudioChannels()
  {
    return (_mPlayerProperties.getAudioChannels());
  }

  public boolean isAudioSigned()
  {
    return (_mPlayerProperties.isAudioSigned());
  }

  public boolean isAudioBigEndian()
  {
    return (_mPlayerProperties.isAudioBigEndian());
  }

  public double getAudioBitrate()
  {
    return (_mPlayerProperties.getAudioBitrate());
  }

  public String getAudioCodec()
  {
    return (_mPlayerProperties.getAudioCodec());
  }

  public boolean isPlaybackStarted()
  {
    return (_mPlayerProperties.isPlaybackStarted());
  }

  public int getPlaybackVolume()
  {
    return (_mPlayerProperties.getPlaybackVolume());
  }

  public boolean isPlaybackMuted()
  {
    return (_mPlayerProperties.isPlaybackMuted());
  }

  public synchronized void loadPlaylist(String url, boolean clearList)
  {
    if (USE_MPLAYER)
      {
        if (clearList)
          _mPlayerProperties.clear();
        sendCommand("loadlist", new String[]
            { url });
      }
    if (USE_VLC)
      {
        if (clearList)
          _mPlayerProperties.clear();
        if (mVLCPlayerPtr != null)
          {
            mVLCPlayerPtr.loadPlaylist(url, clearList);
          }
      }
  }

  public synchronized void loadPlaylist(String url)
  {
    loadPlaylist(url, true);
  }

  public synchronized void loadPlayfile(String url, boolean clearList)
  {
    if (USE_VLC)
      {
        _mPlayerProperties.clear();
        if (mVLCPlayerPtr != null)
          {
            mVLCPlayerPtr.loadPlayfile(url, clearList);
          }
      }
  }

  public synchronized void loadPlayfile(String url)
  {
    loadPlayfile(url, true);
  }

  public synchronized void setVolumeDown()
  {
    if (USE_MPLAYER)
      {
        sendCommand("volume", new String[]
            { "" + (getPlaybackVolume() - 10), "1" });
      }
    if (USE_VLC)
      {
        if (mVLCPlayerPtr != null)
          {
            mVLCPlayerPtr.setVolumeDown();
          }
      }
  }

  public synchronized void setVolumeUp()
  {
    if (USE_MPLAYER)
      {
        sendCommand("volume", new String[]
            { "" + (getPlaybackVolume() + 10), "1" });
      }
    if (USE_VLC)
      {
        if (mVLCPlayerPtr != null)
          {
            mVLCPlayerPtr.setVolumeUp();
          }
      }
  }

  public synchronized void setVolume(int value)
  {
    if (USE_MPLAYER)
      {
        sendCommand("volume", new String[]
            { "" + value, "1" });
      }
    if (USE_VLC)
      {
        if (mVLCPlayerPtr != null)
          {
            mVLCPlayerPtr.setVolume(value);
          }
      }
  }

  public synchronized void setMute(boolean value)
  {
    if (USE_MPLAYER)
      {
        sendCommand("mute", new String[]
            { (value ? "1" : "0") });
      }
    if (USE_VLC)
      {
        if (mVLCPlayerPtr != null)
          {
            mVLCPlayerPtr.setMute(value);
          }
      }
  }

  public synchronized void stopPlayback()
  {
    if (USE_MPLAYER)
      {
        sendCommand("stop", new String[]
            { });
        _mPlayerProperties.setPlaybackStarted(false);
      }
    if (USE_VLC)
      {
        _mPlayerProperties.setPlaybackStarted(false);
        if (mVLCPlayerPtr != null)
          {
            mVLCPlayerPtr.stopPlayback();
          }
      }
  }

  private synchronized void sendCommand(String command, String[] parameters)
  {
    if (USE_MPLAYER)
      {
        if (_inputPrintStream != null)
          {
            try
              {
                _inputPrintStream.print(command);
                for (int index = 0; index < parameters.length; index++)
                  {
                    _inputPrintStream.print(" ");
                    _inputPrintStream.print(parameters[index]);
                  }
                _inputPrintStream.print("\n");
                _inputPrintStream.flush();
              }
            catch (Exception exception)
              {
                Debug.displayStack(this, exception);
              }
          }
      }
  }
}
