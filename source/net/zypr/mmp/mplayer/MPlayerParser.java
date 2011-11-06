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

import java.io.BufferedReader;
import java.io.IOException;

import net.zypr.gui.utils.Debug;

public class MPlayerParser
  extends Thread
{
  private BufferedReader _bufferedReader;
  private MPlayerProperties _properties;
  private boolean _running;

  public MPlayerParser(BufferedReader bufferedReader, MPlayerProperties properties)
  {
    super();
    _bufferedReader = bufferedReader;
    _properties = properties;
    _running = false;
  }

  @Override
  public void run()
  {
    super.run();
    _running = true;
    try
      {
        String stringRead = _bufferedReader.readLine();
        while (stringRead != null && _running)
          {
            stringRead = stringRead.replaceAll("[^\\p{Alnum}\\p{Punct}\\s]", "").trim();
            try
              {
                if (stringRead.startsWith("Playing "))
                  {
                    _properties.setStreamURL(stringRead.substring(8, stringRead.length() - 1));
                  }
                else if (stringRead.startsWith("Name   : "))
                  {
                    _properties.setName(stringRead.substring(9));
                  }
                else if (stringRead.startsWith("Genre  : "))
                  {
                    _properties.setGenre(stringRead.substring(9).split("\\s+"));
                  }
                else if (stringRead.startsWith("Website: "))
                  {
                    _properties.setWebsite(stringRead.substring(9));
                  }
                else if (stringRead.startsWith("Public : "))
                  {
                    _properties.setPublicStream(stringRead.substring(9).startsWith("yes"));
                  }
                else if (stringRead.startsWith("Bitrate: "))
                  {
                    _properties.setStreamBitrate(Double.parseDouble(stringRead.substring(9, stringRead.length() - 6)));
                  }
                else if (stringRead.startsWith("ICY Info: "))
                  {
                    String[] icyInfo = stringRead.substring(10).split(";");
                    for (int index = 0; index < icyInfo.length; index++)
                      {
                        if (icyInfo[index].startsWith("StreamTitle='"))
                          {
                            _properties.setIcyStreamTitle(icyInfo[index].substring(13, icyInfo[index].length() - 1));
                          }
                        else if (icyInfo[index].startsWith("StreamUrl='"))
                          {
                            _properties.setIcyStreamUrl(icyInfo[index].substring(11, icyInfo[index].length() - 1));
                          }
                      }
                  }
                else if (stringRead.startsWith("AUDIO: "))
                  {
                    String[] audioInfo = stringRead.substring(7).split("\\s+");
                    _properties.setAudioRate(Float.parseFloat(audioInfo[0]));
                    _properties.setAudioChannels(Integer.parseInt(audioInfo[2]));
                    _properties.setAudioSigned(audioInfo[4].startsWith("s"));
                    _properties.setAudioSizeInBits(Integer.parseInt(audioInfo[4].substring(1, audioInfo[4].length() - 3)));
                    _properties.setAudioBigEndian(!audioInfo[4].endsWith("le"));
                    _properties.setAudioBitrate(Double.parseDouble(audioInfo[5]));
                  }
                else if (stringRead.startsWith("Selected audio codec: "))
                  {
                    _properties.setAudioCodec(stringRead.substring(22, stringRead.length() - 1));
                  }
                else if (stringRead.startsWith("Starting playback..."))
                  {
                    _properties.setPlaybackStarted(true);
                  }
                else if (stringRead.indexOf("Mute: enabled") != -1)
                  {
                    _properties.setPlaybackMuted(true);
                  }
                else if (stringRead.indexOf("Mute: disabled") != -1)
                  {
                    _properties.setPlaybackMuted(false);
                  }
                else if (stringRead.startsWith("Cache fill:"))
                  {
                    String[] cacheFill = stringRead.replaceAll("[^\\p{Alnum}\\s]", "").split("\\s+");
                    _properties.setCacheFillPercent(Double.parseDouble(cacheFill[2]));
                    _properties.setCacheFillBytes(Integer.parseInt(cacheFill[3]));
                  }
                else if (stringRead.indexOf("Volume: ") != -1)
                  {
                    String[] volumeInfo = stringRead.split("\\s+");
                    _properties.setPlaybackVolume(Integer.parseInt(volumeInfo[1]));
                  }
                else if (stringRead.startsWith("No stream found to handle url"))
                  {
                    _properties.setPlaybackStarted(false);
                  }
                else if (!stringRead.equals("") && !stringRead.equals("[A") && !stringRead.equals("[K"))
                  {
                    Debug.print("IGNORED MPLAYER MESSAGE FOR DEBUGGING : " + stringRead);
                  }
              }
            catch (Exception exception)
              {
                Debug.displayStack(this, exception);
              }
            stringRead = _bufferedReader.readLine();
          }
      }
    catch (IOException ioException)
      {
        Debug.displayStack(this, ioException, 1);
      }
    _running = false;
    MPlayer.getInstance().terminate();
  }

  public boolean isRunning()
  {
    return (_running);
  }

  public void setRunning(boolean running)
  {
    _running = running;
  }
}
