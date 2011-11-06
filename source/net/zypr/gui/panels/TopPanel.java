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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Timer;

import net.zypr.api.Session;
import net.zypr.gui.Configuration;
import net.zypr.gui.Resources;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.Panel;
import net.zypr.gui.utils.Debug;

public class TopPanel
  extends GenericPanel
{
  private BorderLayout borderLayout = new BorderLayout(5, 0);
  private Label _labelPioneerLogo = new Label();
  private Label _labelContacting = new Label();
  private Panel _panelFlipCard = new Panel();
  private NowPlayingPanel _nowPlayingPanel = new NowPlayingPanel();
  private MessagePanel _messagePanel = new MessagePanel();
  private Timer _timer = null;
  private Color _backgroundColor = Configuration.getInstance().getColorProperty("top-bar-background-color", new Color(0, 0, 0, 196));
  private Color _solidBackgroundColor = new Color(_backgroundColor.getRed(), _backgroundColor.getGreen(), _backgroundColor.getBlue(), 255);

  public TopPanel()
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
    setSize(0, 50);
    setLayout(borderLayout);
    this.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
    _labelPioneerLogo.setText("");
    _labelPioneerLogo.setIcon(Resources.getInstance().getImageIcon("logo-pioneer.png"));
    _labelPioneerLogo.addMouseListener(new MouseListener()
    {

      public void mouseClicked(MouseEvent mouseEvent)
      {
        MapViewPanel.gpsPaused = !MapViewPanel.gpsPaused;
        Debug.print("Processing of GPX file is " + (MapViewPanel.gpsPaused ? "paused." : "resumed."));
      }

      public void mouseEntered(MouseEvent mouseEvent)
      {
      }

      public void mouseExited(MouseEvent mouseEvent)
      {
      }

      public void mousePressed(MouseEvent mouseEvent)
      {
      }

      public void mouseReleased(MouseEvent e)
      {
      }
    });
    _labelPioneerLogo.setVisible(true);
    Dimension animatedIconSize = new Dimension(Resources.getInstance().getImageIcon("animated-icon-processing.gif").getIconWidth(), Resources.getInstance().getImageIcon("animated-icon-processing.gif").getIconHeight());
    _labelContacting.setText("");
    _labelContacting.setMinimumSize(animatedIconSize);
    _labelContacting.setMaximumSize(animatedIconSize);
    _labelContacting.setPreferredSize(animatedIconSize);
    _panelFlipCard.setOpaque(false);
    _panelFlipCard.setLayout(null);
    _panelFlipCard.addComponentListener(new ComponentAdapter()
    {
      public void componentResized(ComponentEvent e)
      {
        _panelFlipCard_componentResized(e);
      }
    });
    _nowPlayingPanel.setLocation(0, 0);
    _messagePanel.setLocation(0, 0);
    _messagePanel.setVisible(false);
    _panelFlipCard.add(_nowPlayingPanel, null);
    _panelFlipCard.add(_messagePanel, null);
    add(_labelPioneerLogo, BorderLayout.WEST);
    add(_labelContacting, BorderLayout.EAST);
    add(_panelFlipCard, BorderLayout.CENTER);
    Session.getInstance().addPropertyChangeListener(new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent propertyChangeEvent)
      {
        if (propertyChangeEvent.getPropertyName().equals("ActiveRequestCount"))
          {
            if (Session.getInstance().getActiveRequestCount() != 0)
              _labelContacting.setIcon(Resources.getInstance().getImageIcon("animated-icon-processing.gif"));
            else
              _labelContacting.setIcon(null);
          }
      }
    });
  }

  private void _panelFlipCard_componentResized(ComponentEvent e)
  {
    _nowPlayingPanel.setSize(_panelFlipCard.getSize());
  }

  public void setNotificationMessage(String message)
  {
    setNotificationMessage(message, true, null);
  }

  public void setNotificationMessage(final String message, boolean ttsEnabled, String sound)
  {
    _messagePanel.setNotificationMessage(message, ttsEnabled, sound);
    if (_timer != null && _timer.isRunning())
      {
        _timer.restart();
      }
    else
      {
        _nowPlayingPanel.hidePanel();
        _messagePanel.showPanel();
        _timer = new Timer(Configuration.getInstance().getIntegerProperty("notification-bar-timeout", 15000), new ActionListener()
            {
              public void actionPerformed(ActionEvent actionEvent)
              {
                _timer.stop();
                _timer = null;
                _messagePanel.hidePanel();
                _nowPlayingPanel.showPanel();
              }
            });
        _timer.start();
      }
  }

  @Override
  protected void paintComponent(Graphics graphics)
  {
    super.paintComponent(graphics);
    Graphics2D graphics2D = (Graphics2D) graphics;
    graphics2D.setPaint((Paint) _solidBackgroundColor);
    graphics2D.fillRect(0, 0, 180, this.getHeight());
    int alphaDifference = 255 - _backgroundColor.getAlpha();
    graphics2D.setPaint((Paint) _backgroundColor);
    graphics2D.fillRect(180 + alphaDifference * 2, 0, this.getWidth(), this.getHeight());
    for (int alpha = 0; alpha < alphaDifference; alpha++)
      {
        graphics2D.setPaint((Paint) new Color(_backgroundColor.getRed(), _backgroundColor.getGreen(), _backgroundColor.getBlue(), 255 - alpha));
        graphics2D.fillRect(180 + alpha * 2, 0, 2, this.getHeight());
      }
  }
}
