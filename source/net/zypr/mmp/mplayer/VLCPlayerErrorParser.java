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

public class VLCPlayerErrorParser
  extends Thread
{
  private static final boolean DEBUG_PRINT = false;
  private BufferedReader _bufferedReader;
  private MPlayerProperties _properties;
  private boolean _running;
  private String mPlayerName = null;

  public VLCPlayerErrorParser(BufferedReader bufferedReader, MPlayerProperties properties, String name)
  {
    super();
    _bufferedReader = bufferedReader;
    _properties = properties;
    _running = false;
    mPlayerName = name;
  }

  @Override
  public void run()
  {
    super.run();
    _running = true;
    int msgNumber = 1;
    while (_running)
      {
        try
          {
            String stringRead = _bufferedReader.readLine();
            if (stringRead != null && _running)
              {
                if (DEBUG_PRINT)
                  Debug.print("VLCPlayerErrorParser:" + mPlayerName + " -  Message [" + msgNumber + "]:" + stringRead + "]");
                if (DEBUG_PRINT)
                  msgNumber++;
                /*
					Artist and Song Title
					[159]:[01386b94] access_http access debug: New Title=Led Zeppelin - Your Time is Gonna Come/Black Mountain Side]
					*/
                if (stringRead.contains("New Title="))
                  {
                    int idx = stringRead.indexOf("New Title=");
                    if (idx > 0)
                      {
                        _properties.setIcyStreamTitle(stringRead.substring(idx + 10));
                        if (DEBUG_PRINT)
                          Debug.print("VLCPlayerErrorParser:" + mPlayerName + " Set Artist/Title to [" + _properties.getName() + "]");
                      }
                    continue;
                  }
                /*
					 Station / Stream  Name
					 [85]:[01386b94] access_http access debug: Icy-Name: Radio Paradise - DJ-mixed modern & classic rock, electronica, world, and more - info: radioparadise.com]
					*/
                if (stringRead.contains("Icy-Name:"))
                  {
                    int idx = stringRead.indexOf("Icy-Name:");
                    if (idx > 0)
                      {
                        _properties.setName(stringRead.substring(idx + 9));
                        if (DEBUG_PRINT)
                          Debug.print("VLCPlayerErrorParser:" + mPlayerName + " Set Station to [" + _properties.getIcyStreamTitle() + "]");
                      }
                    continue;
                  }
                /*
					 Station  URL
					 [86]:[01386b94] access_http access debug: Meta-Info: icy-url: http://www.radioparadise.com]
					*/
                if (stringRead.contains("icy-url:"))
                  {
                    int idx = stringRead.indexOf("icy-url:");
                    if (idx > 0)
                      {
                        _properties.setIcyStreamUrl(stringRead.substring(idx + 8));
                        if (DEBUG_PRINT)
                          Debug.print("VLCPlayerErrorParser:" + mPlayerName + " Set URL to [" + _properties.getIcyStreamUrl() + "]");
                      }
                    continue;
                  }
                /*
					 Genre
					 access_http access debug: Icy-Genre: classical]
					*/
                if (stringRead.contains("Icy-Genre: "))
                  {
                    int idx = stringRead.indexOf("Icy-Genre: ");
                    if (idx > 0)
                      {
                        _properties.setGenre(stringRead.substring(idx + 11).split("\\s+"));
                        if (DEBUG_PRINT)
                          Debug.print("VLCPlayerErrorParser:" + mPlayerName + " Set Genre to [" + _properties.getGenre()[0] + "]");
                      }
                    continue;
                  }
                /*
					 Bit Rate
					 access_http access debug: Meta-Info: icy-br: 64]
					*/
                if (stringRead.contains("icy-br:"))
                  {
                    int idx = stringRead.indexOf("icy-br:");
                    if (idx > 0)
                      {
                        String bitRateStr = stringRead.substring(idx + 7, stringRead.length());
                        if (bitRateStr != null)
                          {
                            _properties.setStreamBitrate(Double.parseDouble(bitRateStr));
                            if (DEBUG_PRINT)
                              Debug.print("VLCPlayerErrorParser:" + mPlayerName + " Set Bitrate to [" + _properties.getStreamBitrate() + "]");
                          }
                      }
                    continue;
                  }
                /*
					 Playing Status
					 [013bc20c] main input debug: thread started]
					 [013bc20c] main input debug: thread ended]
          */
                if (stringRead.contains("thread started"))
                  {
                    _properties.setPlaybackStarted(true);
                    continue;
                  }
                if (stringRead.contains("thread ended"))
                  {
                    _properties.setPlaybackStarted(false);
                    continue;
                  }
              }
          }
        catch (IOException ioException)
          {
            ioException.printStackTrace();
            break;
          }
      }
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
