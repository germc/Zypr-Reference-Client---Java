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


package net.zypr.gui.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.Panel;
import net.zypr.gui.components.Separator;
import net.zypr.gui.utils.Debug;

public class ServiceAuthRowPanel
  extends Panel
{
  private BorderLayout _borderLayout = new BorderLayout();
  private Label _labelDetailMessage = new Label();
  private Panel _panel = new Panel();
  private GridLayout _gridLayout = new GridLayout();
  private ImagePanel _imagePanelServiceStatus = new ImagePanel("icon-social-network-add.png", false);
  private ImagePanel _imagePanelServiceLogo = new ImagePanel("icon-network-zypr.png", false);
  private Separator separator = new Separator();

  public ServiceAuthRowPanel()
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
    this.setBackground(Color.DARK_GRAY);
    _panel.setOpaque(false);
    this.setLayout(_borderLayout);
    _labelDetailMessage.setFont(new Font("Dialog", 0, 20));
    _labelDetailMessage.addMouseListener(new MouseAdapter()
    {
      public void mouseEntered(MouseEvent e)
      {
        _labelDetailMessage_mouseEntered(e);
      }
    });
    _gridLayout.setColumns(1);
    _panel.setLayout(_gridLayout);
    _panel.add(_imagePanelServiceStatus, null);
    _panel.add(_imagePanelServiceLogo, null);
    this.add(_labelDetailMessage, BorderLayout.CENTER);
    this.add(_panel, BorderLayout.WEST);
    this.add(separator, BorderLayout.SOUTH);
  }

  public void setDetailMessage(String detailMessage)
  {
    _labelDetailMessage.setText(detailMessage);
  }

  public void setStatusImage(String imageFilename)
  {
    _imagePanelServiceStatus.setImage(imageFilename);
  }

  public void setServiceLogo(BufferedImage bufferedImage)
  {
    _imagePanelServiceLogo.setImage(bufferedImage);
  }

  private void _labelDetailMessage_mouseEntered(MouseEvent e)
  {
    Debug.print(e);
  }
}
