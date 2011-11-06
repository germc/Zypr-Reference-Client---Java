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

public class VLCPlayerParser
  extends Thread
{
  private static final boolean DEBUG_PRINT = false;
  private BufferedReader _bufferedReader;
  private MPlayerProperties _properties;
  private boolean _running;
  private String mPlayerName = null;

  public VLCPlayerParser(BufferedReader bufferedReader, MPlayerProperties properties, String name)
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
                  Debug.print("VLCPlayerParser:" + mPlayerName + " - Message [" + msgNumber + "]:" + stringRead + "]");
                //*********************************************************
                //  Mute
                //*********************************************************
                // access status change: ( audio volume:
                if (stringRead.contains("status change: ( audio volume:"))
                  {
                    int idx = stringRead.indexOf("volume:");
                    if (idx > 0)
                      {
                        String strTemp = stringRead.substring(idx + 8);
                        String strVol = strTemp.substring(0, strTemp.length() - 1);
                        int volume = Integer.parseInt(strVol.trim());
                        if (DEBUG_PRINT)
                          Debug.print("Volume= " + volume);
                        boolean playBackMuted = (volume == 0) ? true : false;
                        _properties.setPlaybackMuted(playBackMuted);
                      }
                  }
                if (DEBUG_PRINT)
                  msgNumber++;
              }
          }
        catch (IOException ioException)
          {
            if (_running && (ioException.getMessage().compareToIgnoreCase("socket closed") != 0))
              ioException.printStackTrace();
            break;
          }
      }
    _running = false;
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
