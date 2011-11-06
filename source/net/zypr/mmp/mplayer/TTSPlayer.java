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
import java.io.InputStreamReader;
import java.io.PrintStream;

import net.zypr.mmp.mplayer.exceptions.MPlayerUnavailableException;

public class TTSPlayer
  extends GenericPlayer
{
  private static final TTSPlayer INSTANCE = new TTSPlayer();
  private static final int VLCSocket = 5123;
  private VLCPlayer mVLCPlayer = null;

  private TTSPlayer()
  {
    super();
    Runtime.getRuntime().addShutdownHook(new Thread()
    {
      @Override
      public void run()
      {
        super.run();
        TTSPlayer.getInstance().terminate();
      }
    });
  }

  public synchronized static TTSPlayer getInstance()
  {
    return (INSTANCE);
  }

  public synchronized void execute()
    throws MPlayerUnavailableException
  {
    try
      {
        if (USE_MPLAYER)
          {
            _process = Runtime.getRuntime().exec(new String[]
                  { "mplayer", "-slave", "-idle", "-quiet", "-noconsolecontrols" });
            _inputPrintStream = new PrintStream(_process.getOutputStream());
            _outputBufferedReader = new BufferedReader(new InputStreamReader(_process.getInputStream()));
            _errorBufferedReader = new BufferedReader(new InputStreamReader(_process.getErrorStream()));
            _outputListener = new MPlayerParser(_outputBufferedReader, _mPlayerProperties);
            _outputListener.start();
            _errorListener = new MPlayerParser(_errorBufferedReader, _mPlayerProperties);
            _errorListener.start();
          }
        if (USE_VLC)
          {
            mVLCPlayer = new VLCPlayer();
            setVLCPlayer(mVLCPlayer);
            mVLCPlayer.Init(VLCSocket, _mPlayerProperties, "TTSPlayer");
          }
      }
    catch (Exception exception)
      {
        terminate();
        throw new MPlayerUnavailableException(exception.getMessage());
      }
  }
}
