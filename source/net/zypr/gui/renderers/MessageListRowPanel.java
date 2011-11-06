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

import java.text.Format;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.SwingConstants;

import net.zypr.api.enums.MessageType;
import net.zypr.gui.Resources;
import net.zypr.gui.Settings;
import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.Panel;
import net.zypr.gui.components.Separator;
import net.zypr.gui.utils.Debug;

public class MessageListRowPanel
  extends Panel
{
  private ImagePanel _imagePanelRead = new ImagePanel("icon-message-list-unread.png", false);
  private ImagePanel _imagePanelType = new ImagePanel("icon-message-list-text-message.png", false);
  private ImagePanel _imagePanelAttachment = new ImagePanel("icon-message-list-attachment.png", false);
  private Label _labelLineNumber = new Label(SwingConstants.RIGHT);
  private Label _labelTimestamp = new Label(SwingConstants.CENTER);
  private Separator _separatorLine = new Separator();
  private Panel _panelContent = new Panel();
  private Label _labelMessageFrom = new Label();

  public MessageListRowPanel()
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
    _labelTimestamp.setFont(new Font("Dialog", 1, 16));
    _labelTimestamp.setForeground(Color.GRAY);
    _labelMessageFrom.setFont(new Font("Dialog", 0, 24));
    _panelContent.setLayout(new FlowLayout(FlowLayout.LEFT));
    _panelContent.add(_labelLineNumber, null);
    _panelContent.add(_imagePanelRead, null);
    _panelContent.add(_imagePanelType, null);
    _panelContent.add(_imagePanelAttachment, null);
    this.add(_panelContent, BorderLayout.WEST);
    this.add(_labelMessageFrom, BorderLayout.CENTER);
    this.add(_separatorLine, BorderLayout.SOUTH);
    this.add(_labelTimestamp, BorderLayout.EAST);
  }

  public void setLineNumber(int lineNumber)
  {
    _labelLineNumber.setText(lineNumber + " -");
  }

  public void setMessageFrom(String from)
  {
    _labelMessageFrom.setText(from);
  }

  public void setTimestamp(Date date)
  {
    Format formatterDate = new SimpleDateFormat(Settings.getInstance().getDTO().getDateFormatPattern().getPattern());
    Format formatterTime = new SimpleDateFormat(Settings.getInstance().getDTO().getTimeFormatPattern().getPattern());
    _labelTimestamp.setText("<html><center>" + formatterDate.format(date).toString() + "<br/>" + formatterTime.format(date).toString() + "</center></html>");
  }

  public void setMessageType(MessageType messageType)
  {
    if (messageType == MessageType.TEXT)
      _imagePanelType.setImage(Resources.getInstance().getBufferedImage("icon-message-list-text-message.png"));
    else if (messageType == MessageType.VOICE)
      _imagePanelType.setImage(Resources.getInstance().getBufferedImage("icon-message-list-voice-message.png"));
  }

  public void setMessageRead(boolean messageRead)
  {
    _imagePanelRead.setImage(Resources.getInstance().getBufferedImage("icon-message-list-" + (messageRead ? "" : "un") + "read.png"));
  }

  public void setHasAttachment(boolean hasAttachment)
  {
    _imagePanelAttachment.setImage(hasAttachment ? Resources.getInstance().getBufferedImage("icon-message-list-attachment.png") : null);
  }
}
