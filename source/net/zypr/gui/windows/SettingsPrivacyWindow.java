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

import net.zypr.api.API;
import net.zypr.api.Session;
import net.zypr.api.vo.InfoUserTypeVO;
import net.zypr.gui.Settings;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.CheckBox;
import net.zypr.gui.components.Separator;
import net.zypr.gui.utils.Debug;

public class SettingsPrivacyWindow
  extends ModalWindow
{
  private Separator _separator = new Separator();
  private Button _buttonCancel = new Button("button-voice-note-settings-cancel-up.png", "button-privacy-settings-cancel-down.png");
  private Button _buttonOkay = new Button("button-voice-note-settings-save-up.png", "button-privacy-settings-save-down.png");
  private CheckBox _checkBoxInvisible = new CheckBox("Invisible to Everyone");
  private CheckBox _checkBoxUpdateLocation = new CheckBox("Update My Location");

  public SettingsPrivacyWindow()
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
    setTitle("Privacy Settings");
    this.setSize(new Dimension(480, 160));
    _separator.setBounds(new Rectangle(10, 60, 460, 1));
    _buttonCancel.setLocation(165, 70);
    _buttonCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonCancel_actionPerformed(actionEvent);
      }
    });
    _buttonOkay.setLocation(280, 70);
    _buttonOkay.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonOkay_actionPerformed(actionEvent);
      }
    });
    _checkBoxInvisible.setBounds(new Rectangle(10, 15, 230, 35));
    _checkBoxUpdateLocation.setBounds(new Rectangle(245, 15, 230, 35));
    _panelContent.add(_checkBoxInvisible, null);
    _panelContent.add(_checkBoxUpdateLocation, null);
    _panelContent.add(_separator, null);
    _panelContent.add(_buttonOkay, null);
    _panelContent.add(_buttonCancel, null);
    _checkBoxInvisible.setChecked(!Session.getInstance().getUserInfo().isLocationIsVisible());
    _checkBoxUpdateLocation.setChecked(Settings.getInstance().getDTO().isUpdateMyLocation());
  }

  private void _buttonCancel_actionPerformed(ActionEvent actionEvent)
  {
    _buttonWindowClose_actionPerformed(null);
  }

  private void _buttonOkay_actionPerformed(ActionEvent actionEvent)
  {
    InfoUserTypeVO newInfoUserTypeVO = new InfoUserTypeVO();
    newInfoUserTypeVO.setLocationIsVisible(!_checkBoxInvisible.isChecked());
    try
      {
        API.getInstance().getUser().infoSet(Session.getInstance().getUserInfo(), newInfoUserTypeVO);
      }
    catch (Exception exception)
      {
        getParentPanel().showWindow(new WarningWindow("Unable to save", "Unable to save privacy settings. Please try it later.", 30, "privacy-settings-unable-to-save-warning.wav"));
        return;
      }
    try
      {
        Session.getInstance().setUserInfo(API.getInstance().getUser().infoGet(null));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    Settings.getInstance().getDTO().setUpdateMyLocation(_checkBoxUpdateLocation.isChecked());
    Settings.getInstance().save();
    _buttonWindowClose_actionPerformed(null);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }
}
