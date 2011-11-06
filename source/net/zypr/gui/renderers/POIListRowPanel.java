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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.SwingConstants;

import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.Panel;
import net.zypr.gui.components.Separator;
import net.zypr.gui.utils.Debug;

public class POIListRowPanel
  extends Panel
{
  private ImagePanel _imagePanelPicture = new ImagePanel("placeholder-picture-unknown-32.png", false);
  private ImagePanel _imagePanelService = new ImagePanel("icon-network-zypr.png", false);
  private Label _labelLineNumber = new Label(SwingConstants.RIGHT);
  private Separator _separatorLine = new Separator();
  private Panel _panelContent = new Panel();
  private Label _labelName = new Label();

  public POIListRowPanel()
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
    this.setLayout(new BorderLayout());
    this.setBackground(Color.DARK_GRAY);
    this.setOpaque(false);
    _panelContent.setOpaque(false);
    _labelLineNumber.setText("0 -");
    _labelLineNumber.setFont(new Font("Dialog", 1, 16));
    _labelName.setFont(new Font("Dialog", 0, 24));
    _panelContent.setLayout(new FlowLayout(FlowLayout.LEFT));
    _panelContent.add(_imagePanelService, null);
    _panelContent.add(_imagePanelPicture, null);
    _panelContent.add(_labelLineNumber, null);
    this.add(_panelContent, BorderLayout.WEST);
    this.add(_labelName, BorderLayout.CENTER);
    this.add(_separatorLine, BorderLayout.SOUTH);
  }

  public void setLineNumber(int lineNumber)
  {
    _labelLineNumber.setText(lineNumber + " -");
  }

  public void setPicture(BufferedImage bufferedImage)
  {
    if (bufferedImage != null)
      _imagePanelPicture.setImage(bufferedImage);
  }

  public void setServiceIcon(BufferedImage bufferedImage)
  {
    if (bufferedImage != null)
      _imagePanelService.setImage(bufferedImage);
  }

  public void setName(String name)
  {
    _labelName.setText(name);
  }
}
