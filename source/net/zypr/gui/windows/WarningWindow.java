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


package net.zypr.gui.windows;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import net.zypr.gui.components.Button;
import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.Separator;
import net.zypr.gui.utils.AudioUtils;
import net.zypr.gui.utils.Debug;

public class WarningWindow
  extends ModalWindow
{
  private Button _buttonOkay = new Button("button-warning-okay-up.png", "button-warning-okay-down.png");
  private ImagePanel _imagePanelIcon = new ImagePanel("icon-dialog-warning.png", false);
  private Label _labelMessage = new Label("<html>...</html>");
  private Separator _separator = new Separator();
  private Timer _timerClose = null;
  private ActionListener _okayButtonActionListener = null;

  public WarningWindow(String title, String message, int seconds, String audioName)
  {
    super();
    try
      {
        if (seconds > 0)
          _timerClose = new Timer(seconds * 1000, new ActionListener()
              {
                public void actionPerformed(ActionEvent actionEvent)
                {
                  _buttonWindowClose_actionPerformed(actionEvent);
                }
              });
        setTitle(title);
        _labelMessage.setText("<html>" + message + "</html>");
        jbInit();
        if (audioName != null)
          AudioUtils.play(audioName);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  private void jbInit()
    throws Exception
  {
    this.setSize(400, 220);
    _buttonOkay.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonOkay_actionPerformed(actionEvent);
      }
    });
    _buttonOkay.setLocation(330, 130);
    _imagePanelIcon.setLocation(15, 15);
    _labelMessage.setBounds(new Rectangle(130, 10, 255, 95));
    _separator.setBounds(10, 120, 375, 1);
    _panelContent.add(_separator, null);
    _panelContent.add(_labelMessage, null);
    _panelContent.add(_imagePanelIcon, null);
    _panelContent.add(_buttonOkay, null);
    this.add(_panelContent, BorderLayout.CENTER);
    _timerClose.start();
  }

  public void setOkayButtonActionListener(ActionListener actionListener)
  {
    _okayButtonActionListener = actionListener;
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    _buttonOkay_actionPerformed(actionEvent);
  }

  private void _buttonOkay_actionPerformed(ActionEvent actionEvent)
  {
    if (_timerClose != null)
      _timerClose.stop();
    getParentPanel().discardWindow();
    if (_okayButtonActionListener != null)
      _okayButtonActionListener.actionPerformed(actionEvent);
  }
}
