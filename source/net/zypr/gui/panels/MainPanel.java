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

import java.awt.CardLayout;
import java.awt.Dimension;

import net.zypr.gui.Configuration;
import net.zypr.gui.Resources;
import net.zypr.gui.utils.Debug;

import org.jdesktop.swingx.painter.ImagePainter;

public class MainPanel
  extends GenericPanel
{
  private final ImagePainter _backgroundImagePainter = new ImagePainter(Resources.getInstance().getBufferedImage("background.jpg"));
  private CardLayout _cardLayout = new CardLayout();
  private StartupPanel _startupPanel = null;
  private MapViewPanel _mapViewPanel = null;

  public MainPanel()
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
    this.setBackgroundPainter(_backgroundImagePainter);
    this.setLayout(_cardLayout);
    showStartupPanel();
  }

  public void showStartupPanel()
  {
    if (_mapViewPanel != null)
      {
        this.remove(_mapViewPanel);
        _mapViewPanel = null;
      }
    if (_startupPanel == null)
      {
        _startupPanel = new StartupPanel();
        this.add(_startupPanel, "startup");
      }
    _startupPanel.setSize(getSize());
    _cardLayout.show(this, "startup");
    discardAllWindows();
    _startupPanel.startProcess();
  }

  public void showMapViewPanel()
  {
    discardAllWindows();
    if (_mapViewPanel == null)
      {
        _mapViewPanel = new MapViewPanel();
        this.add(_mapViewPanel, "mapview");
      }
    _mapViewPanel.setSize(getSize());
    _cardLayout.show(this, "mapview");
  }

  public MapViewPanel getMapViewPanel()
  {
    return (_mapViewPanel);
  }

  public StartupPanel getStartupPanel()
  {
    return (_startupPanel);
  }
}
