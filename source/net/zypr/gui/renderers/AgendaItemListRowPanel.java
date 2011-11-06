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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import net.zypr.gui.Resources;
import net.zypr.gui.components.Panel;
import net.zypr.gui.utils.Debug;

public class AgendaItemListRowPanel
  extends Panel
{
  private JLabel _labelLineNumber = new JLabel();
  private JLabel _labelSocialNetworkIcon = new JLabel();
  private JLabel _labelContactName = new JLabel();
  private JLabel _labelContactPicture = new JLabel();
  private JSeparator _separatorLine = new JSeparator();

  public AgendaItemListRowPanel()
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
    this.setLayout(null);
    this.setOpaque(false);
    _labelLineNumber.setOpaque(false);
    _labelLineNumber.setText("0 -");
    _labelLineNumber.setFont(new Font("Dialog", 0, 14));
    _labelLineNumber.setHorizontalAlignment(SwingConstants.RIGHT);
    _labelLineNumber.setHorizontalTextPosition(SwingConstants.RIGHT);
    _labelLineNumber.setMaximumSize(new Dimension(30, 25));
    _labelLineNumber.setMinimumSize(new Dimension(30, 25));
    _labelLineNumber.setPreferredSize(new Dimension(30, 25));
    _labelLineNumber.setBounds(75, 10, 30, 25);
    _labelLineNumber.setForeground(Color.white);
    _labelSocialNetworkIcon.setOpaque(false);
    _labelSocialNetworkIcon.setMaximumSize(new Dimension(32, 32));
    _labelSocialNetworkIcon.setMinimumSize(new Dimension(32, 32));
    _labelSocialNetworkIcon.setPreferredSize(new Dimension(32, 32));
    _labelSocialNetworkIcon.setHorizontalAlignment(SwingConstants.CENTER);
    _labelSocialNetworkIcon.setHorizontalTextPosition(SwingConstants.CENTER);
    _labelSocialNetworkIcon.setBounds(5, 5, 30, 30);
    _labelSocialNetworkIcon.setIcon(Resources.getInstance().getImageIcon("icon-network-zypr.png"));
    _labelContactName.setOpaque(false);
    _labelContactName.setSize(new Dimension(40, 14));
    _labelContactName.setFont(new Font("Dialog", 1, 16));
    _labelContactName.setBounds(109, 10, 285, 20);
    _labelContactName.setForeground(Color.white);
    _labelContactPicture.setOpaque(false);
    _labelContactPicture.setMaximumSize(new Dimension(32, 32));
    _labelContactPicture.setMinimumSize(new Dimension(32, 32));
    _labelContactPicture.setPreferredSize(new Dimension(32, 32));
    _labelContactPicture.setHorizontalAlignment(SwingConstants.CENTER);
    _labelContactPicture.setHorizontalTextPosition(SwingConstants.CENTER);
    _labelContactPicture.setBounds(40, 5, 35, 30);
    _labelContactPicture.setIcon(Resources.getInstance().getImageIcon("placeholder-picture-contact-32.png"));
    _separatorLine.setOpaque(false);
    _separatorLine.setBounds(0, 39, 320, 1);
    _separatorLine.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    _separatorLine.setSize(new Dimension(375, 1));
    this.add(_separatorLine, null);
    this.add(_labelSocialNetworkIcon, null);
    this.add(_labelContactPicture, null);
    this.add(_labelLineNumber, null);
    this.add(_labelContactName, null);
  }

  public void setLineNumber(int lineNumber)
  {
    _labelLineNumber.setText(lineNumber + " -");
  }

  public void setSocialNetworkIcon(ImageIcon socialNetworkIcon)
  {
    _labelSocialNetworkIcon.setIcon(socialNetworkIcon);
  }

  public void setContactPicture(ImageIcon contactPicture)
  {
    _labelContactPicture.setIcon(contactPicture);
  }

  public void setContactName(String contactName)
  {
    _labelContactName.setText(contactName);
  }

  public void setWidth(int width)
  {
    _separatorLine.setSize(width, _separatorLine.getHeight());
    _labelContactName.setSize(width - _labelContactName.getX(), _labelContactName.getHeight());
  }
}
