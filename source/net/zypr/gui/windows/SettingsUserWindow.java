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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import net.zypr.api.API;
import net.zypr.api.Session;
import net.zypr.api.vo.InfoUserTypeVO;
import net.zypr.gui.Configuration;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.PasswordField;
import net.zypr.gui.components.Separator;
import net.zypr.gui.components.TextField;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.StringUtils;

public class SettingsUserWindow
  extends ModalWindow
{
  private Label _labelWelcome = new Label("<html>Personal Settings...<br/>Please change the fields below.");
  private Label _labelEMailAddress = new Label("E-Mail Address :");
  private Label _labelPassword = new Label("Password :");
  private Label _labelPasswordConfirm = new Label("Password (Confirm) :");
  private Label _labelFirstName = new Label("Firstname :");
  private Label _labelLastName = new Label("Lastname :");
  private TextField _textFieldEMailAddress = new TextField();
  private TextField _textFieldFirstName = new TextField();
  private TextField _textFieldLastName = new TextField();
  private PasswordField _passwordFieldPassword = new PasswordField();
  private PasswordField _passwordFieldPasswordConfirm = new PasswordField();
  private Separator _separatorTop = new Separator();
  private Separator _separatorBottom = new Separator();
  private Button _buttonOkay = new Button("button-user-settings-okay-up.png", "button-user-settings-okay-down.png");
  private Button _buttonCancel = new Button("button-user-settings-cancel-up.png", "button-user-settings-cancel-down.png");

  public SettingsUserWindow()
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
    this.setSize(new Dimension(620, 340));
    setTitle("User Settings");
    _labelEMailAddress.setBounds(25, 75, 570, 15);
    _labelEMailAddress.setBounds(25, 75, 260, 15);
    _labelWelcome.setBounds(25, 10, 570, 40);
    _labelPasswordConfirm.setBounds(335, 125, 260, 14);
    _labelFirstName.setBounds(25, 175, 260, 14);
    _labelLastName.setBounds(335, 175, 260, 14);
    _labelPassword.setBounds(25, 125, 260, 14);
    _separatorTop.setBounds(25, 60, 570, 1);
    _separatorBottom.setBounds(25, 215, 570, 1);
    _textFieldEMailAddress.setBounds(25, 95, 570, 25);
    _textFieldEMailAddress.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent keyEvent)
      {
        _textFieldEMailAddress_keyPressed(keyEvent);
      }
    });
    _textFieldFirstName.setBounds(25, 195, 260, 25);
    _textFieldFirstName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent keyEvent)
      {
        _textFieldFirstName_keyPressed(keyEvent);
      }
    });
    _textFieldLastName.setBounds(335, 195, 260, 25);
    _textFieldLastName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent keyEvent)
      {
        _textFieldLastName_keyPressed(keyEvent);
      }
    });
    _passwordFieldPassword.setBounds(25, 145, 260, 25);
    _passwordFieldPassword.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent keyEvent)
      {
        _passwordFieldPassword_keyPressed(keyEvent);
      }
    });
    _passwordFieldPasswordConfirm.setBounds(335, 145, 260, 25);
    _passwordFieldPasswordConfirm.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent keyEvent)
      {
        _passwordFieldPasswordConfirm_keyPressed(keyEvent);
      }
    });
    _separatorBottom.setBounds(25, 235, 570, 1);
    _buttonOkay.setLocation(330, 245);
    _buttonOkay.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonOkay_actionPerformed(actionEvent);
      }
    });
    _buttonCancel.setLocation(230, 245);
    _buttonCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonCancel_actionPerformed(actionEvent);
      }
    });
    _panelContent.add(_buttonOkay, null);
    _panelContent.add(_buttonCancel, null);
    _panelContent.add(_labelLastName, null);
    _panelContent.add(_labelFirstName, null);
    _panelContent.add(_labelPasswordConfirm, null);
    _panelContent.add(_labelPassword, null);
    _panelContent.add(_labelEMailAddress, null);
    _panelContent.add(_labelWelcome, null);
    _panelContent.add(_passwordFieldPasswordConfirm, null);
    _panelContent.add(_passwordFieldPassword, null);
    _panelContent.add(_textFieldFirstName, null);
    _panelContent.add(_textFieldEMailAddress, null);
    _panelContent.add(_textFieldLastName, null);
    _panelContent.add(_separatorTop, null);
    _panelContent.add(_separatorBottom, null);
    _textFieldEMailAddress.setText(Session.getInstance().getUserInfo().getEmail());
    _textFieldFirstName.setText(Session.getInstance().getUserInfo().getFirstName());
    _textFieldLastName.setText(Session.getInstance().getUserInfo().getLastName());
  }

  private void _buttonOkay_actionPerformed(ActionEvent actionEvent)
  {
    _textFieldEMailAddress.setText(_textFieldEMailAddress.getText().trim());
    _textFieldFirstName.setText(_textFieldFirstName.getText().trim());
    _textFieldLastName.setText(_textFieldLastName.getText().trim());
    if (StringUtils.isEmpty(new String(_passwordFieldPassword.getPassword())) && !StringUtils.isEmpty(new String(_passwordFieldPasswordConfirm.getPassword())))
      {
        getParentPanel().showWindow(new WarningWindow("Please enter a password", "Password field is empty. Please enter a password.", 30, "user-settings-empty-password-warning.wav"));
        return;
      }
    else if (StringUtils.isEmpty(new String(_passwordFieldPasswordConfirm.getPassword())) && !StringUtils.isEmpty(new String(_passwordFieldPassword.getPassword())))
      {
        getParentPanel().showWindow(new WarningWindow("Please enter a password (Confirm)", "Password (Confirm) field is empty. Please enter a password.", 30, "user-settings-empty-password-confirm-warning.wav"));
        return;
      }
    else if (!(new String(_passwordFieldPasswordConfirm.getPassword())).equals((new String(_passwordFieldPassword.getPassword()))))
      {
        getParentPanel().showWindow(new WarningWindow("Passwords mismatch", "Passwords you entered, do not match. Please enter again.", 30, "user-settings-password-mismatch-warning.wav"));
        return;
      }
    else if ((new String(_passwordFieldPassword.getPassword())).length() < 4 && (new String(_passwordFieldPassword.getPassword())).length() != 0)
      {
        getParentPanel().showWindow(new WarningWindow("Invalid passwords", "Invalid passwords. Passwords must be at least four characters in length.", 30, "user-settings-invalid-password-length-warning.wav"));
        return;
      }
    else if (StringUtils.isEmpty(_textFieldFirstName.getText()))
      {
        getParentPanel().showWindow(new WarningWindow("Please enter your first name", "Firstname field is empty. Please enter your first name.", 30, "user-settings-empty-firstname-warning.wav"));
        return;
      }
    else if (StringUtils.isEmpty(_textFieldLastName.getText()))
      {
        getParentPanel().showWindow(new WarningWindow("Please enter your last name", "Lastname field is empty. Please enter your first name.", 30, "user-settings-empty-lastname-warning.wav"));
        return;
      }
    InfoUserTypeVO newInfoUserTypeVO = new InfoUserTypeVO();
    newInfoUserTypeVO.setFirstName(_textFieldFirstName.getText());
    newInfoUserTypeVO.setLastName(_textFieldLastName.getText());
    newInfoUserTypeVO.setEmail(_textFieldEMailAddress.getText());
    if (!(new String(_passwordFieldPassword.getPassword())).equals(""))
      {
        newInfoUserTypeVO.setCurrentPassword(Session.getInstance().getPassword());
        newInfoUserTypeVO.setNewPassword(new String(_passwordFieldPassword.getPassword()));
        newInfoUserTypeVO.setConfirmNewPassword(new String(_passwordFieldPasswordConfirm.getPassword()));
      }
    try
      {
        API.getInstance().getUser().infoSet(Session.getInstance().getUserInfo(), newInfoUserTypeVO);
      }
    catch (Exception exception)
      {
        getParentPanel().showWindow(new WarningWindow("Error", exception.getMessage(), 30, "user-settings-unable-to-save-warning.wav"));
        return;
      }
    try
      {
        Session.getInstance().setUserInfo(API.getInstance().getUser().infoGet(null));
        if (!(new String(_passwordFieldPassword.getPassword())).equals(""))
          Session.getInstance().setPassword((new String(_passwordFieldPassword.getPassword())));
        if (Configuration.getInstance().getBooleanProperty("login-remember-my-password", false))
          Configuration.getInstance().setEncryptedProperty("login-password", Session.getInstance().getPassword());
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    _buttonWindowClose_actionPerformed(actionEvent);
  }

  private void _buttonCancel_actionPerformed(ActionEvent actionEvent)
  {
    _buttonWindowClose_actionPerformed(actionEvent);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }

  private void _textFieldEMailAddress_keyPressed(KeyEvent keyEvent)
  {
    if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
      _buttonOkay_actionPerformed(null);
  }

  private void _textFieldFirstName_keyPressed(KeyEvent keyEvent)
  {
    if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
      _buttonOkay_actionPerformed(null);
  }

  private void _textFieldLastName_keyPressed(KeyEvent keyEvent)
  {
    if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
      _buttonOkay_actionPerformed(null);
  }

  private void _passwordFieldPassword_keyPressed(KeyEvent keyEvent)
  {
    if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
      _buttonOkay_actionPerformed(null);
  }

  private void _passwordFieldPasswordConfirm_keyPressed(KeyEvent keyEvent)
  {
    if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
      _buttonOkay_actionPerformed(null);
  }

  protected void _this_componentShown(ComponentEvent componentEvent)
  {
    _textFieldEMailAddress.requestFocus();
  }
}
