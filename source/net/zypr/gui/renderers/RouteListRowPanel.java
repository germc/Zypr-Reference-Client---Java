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

public class RouteListRowPanel
  extends Panel
{
  private ImagePanel _imagePanelSignage = new ImagePanel("placeholder-picture-signage-32.png", false);
  private Label _labelLineNumber = new Label(SwingConstants.RIGHT);
  private Label _labelDistance = new Label(SwingConstants.CENTER);
  private Separator _separatorLine = new Separator();
  private Panel _panelContent = new Panel();
  private Label _labelDescription = new Label();

  public RouteListRowPanel()
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
    _labelDistance.setFont(new Font("Dialog", 1, 16));
    _labelDistance.setForeground(Color.GRAY);
    _labelDescription.setFont(new Font("Dialog", 0, 24));
    _panelContent.setLayout(new FlowLayout(FlowLayout.LEFT));
    _panelContent.add(_labelLineNumber, null);
    _panelContent.add(_imagePanelSignage, null);
    this.add(_panelContent, BorderLayout.WEST);
    this.add(_labelDescription, BorderLayout.CENTER);
    this.add(_separatorLine, BorderLayout.SOUTH);
    this.add(_labelDistance, BorderLayout.EAST);
  }

  public void setLineNumber(int lineNumber)
  {
    _labelLineNumber.setText(lineNumber + " -");
  }

  public void setSignageIcon(BufferedImage bufferedImage)
  {
    _imagePanelSignage.setImage(bufferedImage);
  }

  public void setDescription(String description)
  {
    _labelDescription.setText(description);
  }

  public void setDistance(String distance)
  {
    _labelDistance.setText(distance);
  }
}
