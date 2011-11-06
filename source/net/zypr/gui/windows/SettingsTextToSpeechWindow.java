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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import net.zypr.gui.Settings;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.CheckBox;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.Separator;
import net.zypr.gui.utils.Debug;

public class SettingsTextToSpeechWindow
  extends ModalWindow
{
  private Separator _separator = new Separator();
  private Button _buttonCancel = new Button("button-text-to-speech-settings-cancel-up.png", "button-text-to-speech-settings-cancel-down.png");
  private Button _buttonOkay = new Button("button-text-to-speech-settings-save-up.png", "button-text-to-speech-settings-save-down.png");
  private Label _labelReadAutomatically = new Label("Read following items automatically", SwingConstants.CENTER);
  private CheckBox _checkBoxMessageHeader = new CheckBox("Message Header");
  private CheckBox _checkBoxMessageText = new CheckBox("Message Text");
  private CheckBox _checkBoxContactFeed = new CheckBox("Contact Feed");
  private CheckBox _checkBoxFollowUpList = new CheckBox("Follow Up List");
  private CheckBox _checkBoxCardHeader = new CheckBox("Card Header");

  public SettingsTextToSpeechWindow()
  {
    super();
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
    setTitle("Text To Speech Settings");
    this.setSize(new Dimension(450, 260));
    _buttonCancel.setBounds(new Rectangle(140, 170, 54, 45));
    _buttonCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonCancel_actionPerformed(actionEvent);
      }
    });
    _buttonOkay.setBounds(new Rectangle(255, 170, 54, 45));
    _buttonOkay.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonOkay_actionPerformed(actionEvent);
      }
    });
    _labelReadAutomatically.setBounds(new Rectangle(10, 10, 425, 25));
    _checkBoxMessageHeader.setBounds(new Rectangle(10, 40, 200, 35));
    _checkBoxMessageText.setBounds(new Rectangle(230, 40, 200, 35));
    _checkBoxContactFeed.setBounds(new Rectangle(10, 80, 200, 35));
    _checkBoxCardHeader.setBounds(new Rectangle(10, 120, 200, 35));
    _checkBoxFollowUpList.setBounds(new Rectangle(230, 80, 200, 35));
    _separator.setBounds(10, 160, 425, 1);
    _panelContent.add(_checkBoxMessageHeader, null);
    _panelContent.add(_checkBoxMessageText, null);
    _panelContent.add(_checkBoxContactFeed, null);
    _panelContent.add(_checkBoxCardHeader, null);
    _panelContent.add(_checkBoxFollowUpList, null);
    _panelContent.add(_labelReadAutomatically, null);
    _panelContent.add(_separator, null);
    _panelContent.add(_buttonOkay, null);
    _panelContent.add(_buttonCancel, null);
    _checkBoxMessageHeader.setChecked(Settings.getInstance().getDTO().isTTSMessageHeader());
    _checkBoxMessageText.setChecked(Settings.getInstance().getDTO().isTTSMessageText());
    _checkBoxContactFeed.setChecked(Settings.getInstance().getDTO().isTTSContactFeed());
    _checkBoxFollowUpList.setChecked(Settings.getInstance().getDTO().isTTSFollowUpList());
    _checkBoxCardHeader.setChecked(Settings.getInstance().getDTO().isTTSCardHeader());
  }

  private void _buttonCancel_actionPerformed(ActionEvent actionEvent)
  {
    _buttonWindowClose_actionPerformed(null);
  }

  private void _buttonOkay_actionPerformed(ActionEvent actionEvent)
  {
    Settings.getInstance().getDTO().setTTSMessageHeader(_checkBoxMessageHeader.isChecked());
    Settings.getInstance().getDTO().setTTSMessageText(_checkBoxMessageText.isChecked());
    Settings.getInstance().getDTO().setTTSContactFeed(_checkBoxContactFeed.isChecked());
    Settings.getInstance().getDTO().setTTSFollowUpList(_checkBoxFollowUpList.isChecked());
    Settings.getInstance().getDTO().setTTSCardHeader(_checkBoxCardHeader.isChecked());
    Settings.getInstance().save();
    _buttonWindowClose_actionPerformed(null);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }
}
