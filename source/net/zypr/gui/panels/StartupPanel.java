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


package net.zypr.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.zypr.gps.GPSDevice;
import net.zypr.gps.exceptions.GPSDeviceUnavailableException;
import net.zypr.gui.Application;
import net.zypr.gui.Configuration;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.windows.LoginWindow;
import net.zypr.gui.windows.WarningWindow;
import net.zypr.mmp.mplayer.MPlayer;
import net.zypr.mmp.mplayer.TTSPlayer;

public class StartupPanel
  extends GenericPanel
{
  public StartupPanel()
  {
    try
      {
        jbInit();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  private void jbInit()
    throws Exception
  {
  }

  public void startProcess()
  {
    checkGPSDevice();
  }

  private void checkGPSDevice()
  {
    try
      {
        GPSDevice.getInstance().openDevice(Configuration.getInstance().getProperty("gps-device-path", "/dev/ttyUSB0"));
        WarningWindow warningWindow = new WarningWindow("GPS device found", "GPS device found. GPS will be used to locate you on the map.", 30, "gps-device-found-warning.wav");
        warningWindow.setOkayButtonActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent actionEvent)
          {
            checkMPlayer();
          }
        });
        Application.splashFrame.setVisible(false);
        showWindow(warningWindow);
      }
    catch (GPSDeviceUnavailableException unableToOpenDeviceException)
      {
        checkMPlayer();
      }
  }

  private void checkMPlayer()
  {
    try
      {
        MPlayer.getInstance().execute();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
        WarningWindow warningWindow = new WarningWindow("Unable to execute Media Player", "Unable to execute Media Player. Please make sure it is installed and accessible by current OS user.", 30, "unable-to-run-mplayer-warning.wav");
        warningWindow.setOkayButtonActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent actionEvent)
          {
            startLoginProcess();
          }
        });
        Application.splashFrame.setVisible(false);
        showWindow(warningWindow);
      }
    finally
      {
        try
          {
            TTSPlayer.getInstance().execute();
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
    startLoginProcess();
  }

  private void startLoginProcess()
  {
    LoginWindow loginWindow = new LoginWindow();
    showWindow(loginWindow);
    if (!Configuration.getInstance().getProperty("login-email-address", "").equals("") && !Configuration.getInstance().getEncryptedProperty("login-password", "").equals("") && Configuration.getInstance().getBooleanProperty("login-remember-my-email", false) && Configuration.getInstance().getBooleanProperty("login-remember-my-password", false) && Configuration.getInstance().getBooleanProperty("login-sign-me-in-automatically", false))
      loginWindow._buttonSignIn_actionPerformed(null);
  }
}
