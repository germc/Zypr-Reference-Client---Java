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

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import net.zypr.api.API;
import net.zypr.api.Session;
import net.zypr.gui.panels.MainPanel;
import net.zypr.gui.utils.Debug;

public class ApplicationFrame
  extends JFrame
{
  private MainPanel _mainPanel = new MainPanel();

  public ApplicationFrame()
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
    this.setSize(new Dimension(Configuration.getInstance().getIntegerProperty("window-width", 800), Configuration.getInstance().getIntegerProperty("window-height", 480)));
    this.setTitle("Pioneer ZYPR");
    this.setResizable(false);
    this.setUndecorated(!Configuration.getInstance().getBooleanProperty("window-show-frame", true));
    setContentPane(_mainPanel);
    this.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent windowEvent)
      {
        this_windowClosing(windowEvent);
      }
    });
  }

  private void this_windowClosing(WindowEvent windowEvent)
  {
    if (Session.getInstance().isLoggedIn())
      {
        _mainPanel.getMapViewPanel().saveAsImage();
        Configuration.getInstance().save();
        Settings.getInstance().save();
        try
          {
            API.getInstance().getAuth().logout();
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
  }
}
