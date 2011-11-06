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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import java.net.InetAddress;
import java.net.Socket;

import net.zypr.gui.Configuration;
import net.zypr.gui.utils.Debug;
/* **********************************************
   VLC   RC  Command List
*************************************************
 add XYZ  . . . . . . . . . . add XYZ to playlist
 playlist . . .  show items currently in playlist
 play . . . . . . . . . . . . . . . . play stream
 stop . . . . . . . . . . . . . . . . stop stream
 next . . . . . . . . . . . .  next playlist item
 prev . . . . . . . . . .  previous playlist item
 goto . . . . . . . . . . . .  goto item at index
 clear . . . . . . . . . . .   clear the playlist
 status . . . . . . . . . current playlist status
 title [X]  . . . . set/get title in current item
 title_n  . . . . . .  next title in current item
 title_p  . . . .  previous title in current item
 chapter [X]  . . set/get chapter in current item
 chapter_n  . . . .  next chapter in current item
 chapter_p  . .  previous chapter in current item

 seek X . seek in seconds, for instance `seek 12'
 pause  . . . . . . . . . . . . . .  toggle pause
 fastforward  . . . . . .  .  set to maximum rate
 rewind  . . . . . . . . . .  set to minimum rate
 faster . . . . . . . .  faster playing of stream
 slower . . . . . . . .  slower playing of stream
 normal . . . . . . . .  normal playing of stream
 f [on/off] . . . . . . . . . . toggle fullscreen
 info . . .  information about the current stream

 volume [X] . . . . . . . .  set/get audio volume
 volup [X]  . . . . .  raise audio volume X steps
 voldown [X]  . . . .  lower audio volume X steps
 adev [X] . . . . . . . . .  set/get audio device
 achan [X]. . . . . . . .  set/get audio channels
 menu [on/off/up/down/left/right/select] use menu

 marq-marquee STRING  . . overlay STRING in video
 marq-x X . . . . . . . . . . . .offset from left
 marq-y Y . . . . . . . . . . . . offset from top
 marq-position #. . .  .relative position control
 marq-color # . . . . . . . . . . font color, RGB
 marq-opacity # . . . . . . . . . . . . . opacity
 marq-timeout T. . . . . . . . . . timeout, in ms
 marq-size # . . . . . . . . font size, in pixels

 time-format STRING . . . overlay STRING in video
 time-x X . . . . . . . . . . . .offset from left
 time-y Y . . . . . . . . . . . . offset from top
 time-position #. . . . . . . . relative position
 time-color # . . . . . . . . . . font color, RGB
 time-opacity # . . . . . . . . . . . . . opacity
 time-size # . . . . . . . . font size, in pixels

 logo-file STRING . . . the overlay file path/name
 logo-x X . . . . . . . . . . . .offset from left
 logo-y Y . . . . . . . . . . . . offset from top
 logo-position #. . . . . . . . relative position
 logo-transparency #. . . . . . . . .transparency

 mosaic-alpha # . . . . . . . . . . . . . . alpha
 mosaic-height #. . . . . . . . . . . . . .height
 mosaic-width # . . . . . . . . . . . . . . width
 mosaic-xoffset # . . . .top left corner position
 mosaic-yoffset # . . . .top left corner position
 mosaic-align 0..2,4..6,8..10. . .mosaic alignment
 mosaic-vborder # . . . . . . . . vertical border
 mosaic-hborder # . . . . . . . horizontal border
 mosaic-position {0=auto,1=fixed} . . . .position
 mosaic-rows #. . . . . . . . . . .number of rows
 mosaic-cols #. . . . . . . . . . .number of cols
 mosaic-keep-aspect-ratio {0,1} . . .aspect ratio

 help . . . . . . . . . . . . . this help message
 longhelp . . . . . . . . . a longer help message
 logout . . . . .  exit (if in socket connection)
 quit . . . . . . . . . . . . . . . . .  quit vlc

  http:/wiki.videolan.org/Documentation:Modules/rc
  TO get this list "vlc --list"
************************************************** */
public class VLCPlayer
{
  private static final boolean DEBUG_PRINT = false;
  private Process mVLCProcess = null;
  private PrintStream mInputPrintStream = null;
  private VLCPlayerParser mVLCPLayerParser = null;
  private BufferedReader mInputBufferedReader = null;
  private InputStreamReader mInputStreamReader = null;
  private VLCPlayerErrorParser mVLCPLayerErrorParser = null;
  private BufferedReader mInputErrorBufferedReader = null;
  private InputStreamReader mInputErrorStreamReader = null;
  private MPlayerProperties mPlayerProperties = null;
  private String mVLCPath = null;
  private String mSocketPort = null;
  private String mVLCRCVersion = null;
  private String mTTYMode = null;
  private String mPlayerName = null;
  private Socket mVLCSocket = null;
  private int mCurrentVolume = 500;
  private int mVolumeStep = 100;
  private int mTTSVolume = 1000;
  private int mSFXVolume = 750;
  private int mInternetRadioVolume = 500;
  private int mMaxAudioVolume = 1000;

  private enum OperatingSystem
  {
    None,
    Windows,
    MAC,
    Linux;
  }
  private OperatingSystem mOS = OperatingSystem.None;
  private static final String[] VLC_LINUXPATHS =
  { "/bin/vlc", "/usr/bin/cvlc" };
  private static final String[] VLC_MACPATHS =
  { "/Applications/VLC.app/Contents/MacOS/VLC", };
  private final String VLC_KEY = "Software\\Classes\\VLC.3g2\\DefaultIcon";

  public VLCPlayer()
  {
    super();
  }

  public synchronized void Init(int socket, MPlayerProperties playerProp, String name)
    throws IOException
  {
    GetSystemInfo();
    if ((mVLCPath == null) || (mOS == null))
      {
        if (DEBUG_PRINT)
          Debug.print("VLCPlayer: ERROR - Unable to find VLC");
        throw (new IOException());
      }
    mPlayerName = name;
    if (DEBUG_PRINT)
      Debug.print("VLCPlayer: Name=" + mPlayerName + " Socket=" + socket + " OS=" + mOS + " RC=" + mVLCRCVersion + " Path=" + mVLCPath);
    mSocketPort = String.valueOf(socket);
    mPlayerProperties = playerProp;
    if (mVLCProcess != null)
      return;
    String cmdParm[] = null;
    switch (mOS)
      {
        case Windows:
          cmdParm = new String[]
              { mVLCPath, "--intf=" + mVLCRCVersion, "--rc-host=localhost:" + mSocketPort, "-vvv", mTTYMode };
          if (DEBUG_PRINT)
            Debug.print("VLC:" + mPlayerName + " - Starting VLC on Windows");
          break;
        case MAC:
          cmdParm = new String[]
              { mVLCPath, "--intf=" + mVLCRCVersion, "--rc-host", "localhost:" + mSocketPort, "-vvv", mTTYMode };
          if (DEBUG_PRINT)
            Debug.print("VLC:" + mPlayerName + " - Starting VLC on MAC");
          break;
        case Linux:
          cmdParm = new String[]
              { mVLCPath, "--rc-fake-tty", "--rc-host", "localhost:" + mSocketPort, "--extraintf", "oldrc" };
          if (DEBUG_PRINT)
            Debug.print("VLC:" + mPlayerName + " - Starting VLC on Linux");
          break;
      }
    boolean isLaunched = false;
    int launches = 10;
    do
      {
        launches--;
        try
          {
            if (!isLaunched)
              mVLCProcess = Runtime.getRuntime().exec(cmdParm);
            Thread.sleep(2500);
            mInputErrorStreamReader = new InputStreamReader(mVLCProcess.getErrorStream());
            mInputErrorBufferedReader = new BufferedReader(mInputErrorStreamReader);
            mVLCPLayerErrorParser = new VLCPlayerErrorParser(mInputErrorBufferedReader, mPlayerProperties, mPlayerName);
            mVLCPLayerErrorParser.start();
            if (DEBUG_PRINT)
              Debug.print("VLC:" + mPlayerName + " - Getting VLC inet address.");
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            if (DEBUG_PRINT)
              Debug.print("VLC:" + mPlayerName + " - Trying to opening Socket to VLC");
            mVLCSocket = new Socket(addr, Integer.parseInt(mSocketPort));
            mInputPrintStream = new PrintStream(mVLCSocket.getOutputStream());
            if (DEBUG_PRINT)
              Debug.print("VLC:" + mPlayerName + " - Successful opening Socket to VLC");
            if (DEBUG_PRINT)
              Debug.print("VLC:" + mPlayerName + " - Opening input Stream to VLC");
            mInputStreamReader = new InputStreamReader(mVLCSocket.getInputStream());
            mInputBufferedReader = new BufferedReader(mInputStreamReader);
            mVLCPLayerParser = new VLCPlayerParser(mInputBufferedReader, mPlayerProperties, mPlayerName);
            mVLCPLayerParser.start();
            setConfig();
            setCurrentVolume();
            setVolume(VolumeAsPercent(mCurrentVolume));
            launches = 0;
          }
        catch (Exception exception)
          {
            isLaunched = true;
            closeParsers();
            closeSocket();
            if (DEBUG_PRINT)
              Debug.print("Unable to successfully start vlc media player" + " Trying " + launches + " more times.");
          }
      }
    while (launches > 0);
  }

  public synchronized void close()
    throws IOException
  {
    if (mVLCProcess != null)
      {
        closePlayer();
        closeParsers();
        closeSocket();
        closeVLCProcess();
      }
    if (DEBUG_PRINT)
      Debug.print("close() - Player Name = " + mPlayerName + "  mCurrentVolume = " + mCurrentVolume);
  }

  private void closeParsers()
  {
    if (mVLCPLayerParser != null)
      {
        mVLCPLayerParser.setRunning(false);
        mVLCPLayerParser = null;
      }
    if (mVLCPLayerErrorParser != null)
      {
        mVLCPLayerErrorParser.setRunning(false);
        mVLCPLayerErrorParser = null;
      }
  }

  private void closeSocket()
  {
    if (mVLCSocket != null)
      {
        try
          {
            mVLCSocket.close();
          }
        catch (IOException e)
          {
            if (DEBUG_PRINT)
              Debug.print("Unable to close socket");
          }
      }
  }

  private void closeVLCProcess()
  {
    if (mVLCProcess != null)
      mVLCProcess.destroy();
    mVLCProcess = null;
  }

  private void closePlayer()
  {
    stopPlayback();
    sendCommand("quit");
  }

  public boolean isAvailable()
  {
    return (mVLCSocket.isConnected());
  }

  public boolean isPlaybackMuted()
  {
    return mPlayerProperties.isPlaybackMuted();
  }

  public synchronized void loadPlaylist(String url, boolean clearList)
  {
    if (DEBUG_PRINT)
      Debug.print("VLC:" + mPlayerName + " - Load Playlist: " + url);
    if (clearList)
      sendCommand("clear");
    sendCommand("add", new String[]
        { url });
    // TEMP
    mPlayerProperties.setPlaybackStarted(true);
  }

  public synchronized void loadPlayfile(String url, boolean clearList)
  {
    if (DEBUG_PRINT)
      Debug.print("VLC:" + mPlayerName + " - Load Play file: " + url);
    if (clearList)
      sendCommand("clear");
    sendCommand("add", new String[]
        { url });
    /* TEMP */
    mPlayerProperties.setPlaybackStarted(true);
    /* TEMP */
  }

  public synchronized void setVolumeDown()
  {
    mCurrentVolume -= mVolumeStep;
    if (mCurrentVolume < 0)
      mCurrentVolume = 0;
    sendCommand("volume", new String[]
        { "" + mCurrentVolume });
    mPlayerProperties.setPlaybackVolume(VolumeAsPercent(mCurrentVolume));
    if (mPlayerName.equals("MediaPlayer"))
      Configuration.getInstance().setIntegerProperty("internet-radio-mix-max", mCurrentVolume);
    if (mPlayerName.equals("TTSPlayer"))
      Configuration.getInstance().setIntegerProperty("tts-max-mix", mCurrentVolume);
  }

  public synchronized void setVolumeUp()
  {
    mCurrentVolume += mVolumeStep;
    if (mCurrentVolume > mMaxAudioVolume)
      mCurrentVolume = mMaxAudioVolume;
    sendCommand("volume", new String[]
        { "" + mCurrentVolume });
    mPlayerProperties.setPlaybackVolume(VolumeAsPercent(mCurrentVolume));
    if (mPlayerName.equals("MediaPlayer"))
      Configuration.getInstance().setIntegerProperty("internet-radio-mix-max", mCurrentVolume);
    if (mPlayerName.equals("TTSPlayer"))
      Configuration.getInstance().setIntegerProperty("tts-max-mix", mCurrentVolume);
  }

  public synchronized void setVolume(int value)
  {
    if ((value >= 0) && (value <= 100))
      {
        mCurrentVolume = VolumeFromPercent(value);
        sendCommand("volume", new String[]
            { "" + mCurrentVolume });
        mPlayerProperties.setPlaybackVolume(value);
      }
  }

  public synchronized void setConfig()
  {
    mMaxAudioVolume = Configuration.getInstance().getIntegerProperty("audio-max-volume", mMaxAudioVolume);
    mVolumeStep = Configuration.getInstance().getIntegerProperty("volume-step", mVolumeStep);
    mInternetRadioVolume = Configuration.getInstance().getIntegerProperty("internet-radio-mix-max", mInternetRadioVolume);
    mTTSVolume = Configuration.getInstance().getIntegerProperty("tts-mix-max", mTTSVolume);
    mSFXVolume = Configuration.getInstance().getIntegerProperty("sfx-mix-max", mSFXVolume);
  }

  public synchronized void setCurrentVolume()
  {
    if (mPlayerName == "MediaPlayer")
      mCurrentVolume = mInternetRadioVolume;
    if (mPlayerName == "TTSPlayer")
      mCurrentVolume = mTTSVolume;
  }

  public synchronized void setMute(boolean value)
  {
    if (value)
      {
        if (!mPlayerProperties.isPlaybackMuted())
          {
            sendCommand("volume", new String[]
                { "" + 0 });
            mPlayerProperties.setPlaybackMuted(true);
          }
      }
    else
      {
        if (mPlayerProperties.isPlaybackMuted())
          {
            setVolume(VolumeAsPercent(mCurrentVolume));
            mPlayerProperties.setPlaybackMuted(false);
          }
      }
  }

  public synchronized void stopPlayback()
  {
    sendCommand("stop", new String[]
        { });
    /* TEMP */
    mPlayerProperties.setPlaybackStarted(false);
    /* TEMP */
  }

  public synchronized void sendCommand(String command, String[] parameters)
  {
    if (mInputPrintStream != null)
      {
        String theVLCCommand = command + " ";
        for (int index = 0; index < parameters.length; index++)
          theVLCCommand += " " + parameters[index];
        try
          {
            if (DEBUG_PRINT)
              Debug.print("VLC:" + mPlayerName + " - Sending VLC command:" + theVLCCommand);
            mInputPrintStream.println(theVLCCommand);
          }
        catch (Exception exception)
          {
            exception.printStackTrace();
          }
      }
  }

  public synchronized void sendCommand(String command, String parameter)
  {
    if ((mInputPrintStream != null) && (command != null) && (parameter != null))
      {
        String[] sArray = new String[]
          { parameter };
        sendCommand(command, sArray);
      }
  }

  public synchronized void sendCommand(String command)
  {
    if ((mInputPrintStream != null) && (command != null))
      {
        String[] sArray = new String[]
          { "" };
        sendCommand(command, sArray);
      }
  }

  private void GetSystemInfo()
  {
    String osName = System.getProperty("os.name");
    if (osName.startsWith("Mac OS"))
      {
        mOS = OperatingSystem.MAC;
      }
    else if (osName.startsWith("Windows"))
      {
        mOS = OperatingSystem.Windows;
      }
    else if (osName.startsWith("Linux"))
      {
        mOS = OperatingSystem.Linux;
      }
    File test;
    int i;
    switch (mOS)
      {
        case Windows:
          mVLCRCVersion = "oldrc";
          mTTYMode = "--rc-quiet";
          String path;
          path = DiscoverVLCWinLocation();
          test = new File(path);
          if (test.exists())
            {
              mVLCPath = path;
              Configuration.getInstance().setStringProperty("vlc-path", mVLCPath);
            }
          break;
        case MAC:
          mVLCRCVersion = "oldrc";
          mTTYMode = "--rc-fake-tty";
          for (i = 0; i < VLC_MACPATHS.length; i++)
            {
              test = new File(VLC_MACPATHS[i]);
              if (test.exists())
                {
                  mVLCPath = VLC_MACPATHS[i];
                  break;
                }
            }
          break;
        case Linux:
          mVLCRCVersion = "rc";
          mTTYMode = "--rc-fake-tty";
          for (i = 0; i < VLC_LINUXPATHS.length; i++)
            {
              test = new File(VLC_LINUXPATHS[i]);
              if (test.exists())
                {
                  mVLCPath = VLC_LINUXPATHS[i];
                  break;
                }
            }
          break;
      }
  }

  private int VolumeAsPercent(int value)
  {
    return (value * 100) / mMaxAudioVolume;
  }

  private int VolumeFromPercent(int value)
  {
    return (value * mMaxAudioVolume) / 100;
  }

  private String DiscoverVLCWinLocation()
  {
    String line = null;
    String value = null;
    String command = System.getenv("windir") + "\\System32\\REG QUERY " + "HKEY_LOCAL_MACHINE" + "\\" + VLC_KEY + " /ve ";
    try
      {
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        line = input.readLine();
        do
          {
            line = input.readLine();
            if (!line.isEmpty())
              value = line;
          }
        while (!line.isEmpty());
        input.close();
      }
    catch (Exception exception)
      {
        exception.printStackTrace();
      }
    return (GetVLCWinPath(value));
  }

  private String GetVLCWinPath(String vlcRegString)
  {
    int left = vlcRegString.indexOf('"');
    int right = vlcRegString.indexOf(',');
    String path = vlcRegString.substring(left + 1, right - 1);
    return path;
  }
}
