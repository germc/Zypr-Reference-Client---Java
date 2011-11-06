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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.utils.ImageUtils;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.MattePainter;

public class SplashFrame
  extends JFrame
{
  public SplashFrame()
  {
    try
      {
        jbInit();
      }
    catch (Exception e)
      {
        e.printStackTrace();
      }
  }

  private void jbInit()
    throws Exception
  {
    if (Configuration.getInstance().getBooleanProperty("show-splash-screen", true))
      {
        this.setSize(new Dimension(Configuration.getInstance().getIntegerProperty("window-width", 800), Configuration.getInstance().getIntegerProperty("window-height", 480)));
        this.setTitle("Pioneer ZYPR");
        this.setResizable(false);
        this.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height)
          frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
          frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        JXPanel contentPanel = new JXPanel();
        contentPanel.setLayout(null);
        this.setContentPane(contentPanel);
        File screenshotFile = new File(System.getProperty("user.home") + File.separator + ".zypr" + File.separator + "screenshot.jpg");
        if (screenshotFile.exists())
          {
            this.setBackground(Color.BLACK);
            contentPanel.setBackgroundPainter(new ImagePainter(ImageUtils.getBufferedImage((new ImageIcon(screenshotFile.getCanonicalPath()).getImage()))));
          }
        else
          {
            contentPanel.setBackgroundPainter(new MattePainter(new Color(0, 0, 0, 255)));
            ImagePanel imagePanel = new ImagePanel("splash-screen.png", false);
            imagePanel.setLocation(100, 200);
            this.add(imagePanel);
          }
        if (Configuration.getInstance().getBooleanProperty("display-spinner-on-splashscreen", true))
          {
            Label labelSpinner = new Label();
            labelSpinner.setOpaque(false);
            labelSpinner.setBounds(376, 300, 48, 48);
            labelSpinner.setIcon(Resources.getInstance().getImageIcon("animated-icon-processing.gif"));
            this.add(labelSpinner);
          }
        this.setVisible(true);
      }
  }
}
