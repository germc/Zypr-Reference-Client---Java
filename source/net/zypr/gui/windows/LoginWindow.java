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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import net.zypr.api.API;
import net.zypr.api.Session;
import net.zypr.api.enums.StatusCode;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.vo.InfoUserTypeVO;
import net.zypr.gui.Application;
import net.zypr.gui.Configuration;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.CheckBox;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.PasswordField;
import net.zypr.gui.components.Separator;
import net.zypr.gui.components.TextField;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.DeviceID;
import net.zypr.gui.utils.StringUtils;

public class LoginWindow
  extends ModalWindow
{
  private TextField _textFieldEMailAddress = new TextField(Configuration.getInstance().getProperty("login-email-address", ""));
  private PasswordField _passwordFieldPassword = new PasswordField(Configuration.getInstance().getEncryptedProperty("login-password", ""));
  private Label _labelEMailAddress = new Label("E-Mail Address :");
  private Label _labelPassword = new Label("Password :");
  private Label _labelWelcome = new Label("<html>Welcome to ZYPR...<br/>Please enter your E-Mail and password to sign in.");
  private Separator _separatorTop = new Separator();
  private Separator _separatorBottom = new Separator();
  private CheckBox _checkBoxRememberEMail = new CheckBox("Remember my e-mail");
  private CheckBox _checkBoxRememberPassword = new CheckBox("Remember my password");
  private CheckBox _checkBoxAutoSignIn = new CheckBox("Sign me in automatically");
  private CheckBox _checkBoxSignInInvis = new CheckBox("Sign me in invisible");
  private Button _buttonSignIn = new Button("button-sign-in-up.png", "button-sign-in-down.png");
  private Button _buttonSignUp = new Button("button-sign-up-up.png", "button-sign-up-down.png");

  public LoginWindow()
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
    this.setSize(620, 340);
    setTitle("Sign in to ZYPR");
    _textFieldEMailAddress.setBounds(25, 95, 260, 25);
    _textFieldEMailAddress.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent keyEvent)
      {
        _textFieldEMailAddress_keyPressed(keyEvent);
      }
    });
    _passwordFieldPassword.setBounds(335, 95, 260, 25);
    _passwordFieldPassword.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent keyEvent)
      {
        _passwordFieldPassword_keyPressed(keyEvent);
      }
    });
    _labelEMailAddress.setBounds(25, 75, 260, 15);
    _labelPassword.setBounds(335, 75, 260, 15);
    _labelWelcome.setBounds(25, 10, 570, 40);
    _separatorTop.setBounds(25, 60, 570, 1);
    _separatorBottom.setBounds(25, 215, 570, 1);
    _checkBoxRememberEMail.setLocation(20, 130);
    _checkBoxRememberEMail.setActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        setCheckBoxes();
      }
    });
    _checkBoxRememberPassword.setLocation(330, 130);
    _checkBoxRememberPassword.setActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        setCheckBoxes();
      }
    });
    _checkBoxAutoSignIn.setLocation(330, 170);
    _checkBoxSignInInvis.setLocation(20, 170);
    _buttonSignIn.setLocation(230, 240);
    _buttonSignIn.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonSignIn_actionPerformed(actionEvent);
      }
    });
    _buttonSignUp.setLocation(330, 240);
    _buttonSignUp.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonSignUp_actionPerformed(actionEvent);
      }
    });
    _panelContent.add(_buttonSignUp, null);
    _panelContent.add(_buttonSignIn, null);
    _panelContent.add(_checkBoxRememberEMail, null);
    _panelContent.add(_checkBoxRememberPassword, null);
    _panelContent.add(_checkBoxAutoSignIn, null);
    _panelContent.add(_checkBoxSignInInvis, null);
    _panelContent.add(_separatorTop, null);
    _panelContent.add(_separatorBottom, null);
    _panelContent.add(_labelWelcome, null);
    _panelContent.add(_labelPassword, null);
    _panelContent.add(_labelEMailAddress, null);
    _panelContent.add(_passwordFieldPassword, null);
    _panelContent.add(_textFieldEMailAddress, null);
    _checkBoxRememberEMail.setChecked(Configuration.getInstance().getBooleanProperty("login-remember-my-email", false));
    _checkBoxRememberPassword.setChecked(Configuration.getInstance().getBooleanProperty("login-remember-my-password", false));
    _checkBoxAutoSignIn.setChecked(Configuration.getInstance().getBooleanProperty("login-sign-me-in-automatically", false));
    setCheckBoxes();
    Application.splashFrame.setVisible(false);
  }

  private void setCheckBoxes()
  {
    _checkBoxRememberPassword.setEnabled(_checkBoxRememberEMail.isChecked());
    _checkBoxAutoSignIn.setEnabled(_checkBoxRememberPassword.isEnabled() && _checkBoxRememberPassword.isChecked());
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    super._buttonWindowClose_actionPerformed(actionEvent);
    System.exit(0);
  }

  public void _buttonSignIn_actionPerformed(ActionEvent actionEvent)
  {
    _textFieldEMailAddress.setText(_textFieldEMailAddress.getText().trim());
    if (StringUtils.isEmpty(new String(_passwordFieldPassword.getPassword())))
      {
        getParentPanel().showWindow(new WarningWindow("Please enter a password", "Password field is empty. Please enter your password.", 30, "sign-in-empty-password-warning.wav"));
        return;
      }
    else if ((new String(_passwordFieldPassword.getPassword())).length() < 4)
      {
        getParentPanel().showWindow(new WarningWindow("Invalid passwords", "Invalid passwords. Passwords must be at least four characters in length.", 30, "sign-in-invalid-password-length-warning.wav"));
        return;
      }
    try
      {
        if (API.getInstance().getAuth().login(_textFieldEMailAddress.getText(), new String(_passwordFieldPassword.getPassword()), DeviceID.load(_textFieldEMailAddress.getText())) != StatusCode.SUCCESSFUL)
          {
            getParentPanel().showWindow(new WarningWindow("Unable to sign in", "Unable to sign in. Please make sure you entered your e-mail address and password correct.", 30, "sign-in-unable-to-sign-in-warning.wav"));
            return;
          }
      }
    catch (APIProtocolException protocolException)
      {
        Debug.displayStack(this, protocolException);
        getParentPanel().showWindow(new WarningWindow("Protocol error", "Unable to sign in due to a protocol error! Please contact to admin.", 30, "sign-in-protocol-error-warning.wav"));
        return;
      }
    catch (APICommunicationException communicationException)
      {
        Debug.displayStack(this, communicationException);
        _checkBoxAutoSignIn.setChecked(false);
        setCheckBoxes();
        getParentPanel().showWindow(new WarningWindow("Communication error", "Unable to sign in due to communication error! Please check your internet connection.", 30, "sign-in-communication-error-warning.wav"));
        return;
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
        getParentPanel().showWindow(new WarningWindow("Error", exception.getMessage(), 30, "sign-in-unable-to-sign-in-warning.wav"));
        return;
      }
    DeviceID.save(_textFieldEMailAddress.getText(), Session.getInstance().getDeviceID());
    if (_checkBoxRememberEMail.isChecked())
      {
        Configuration.getInstance().setProperty("login-email-address", _textFieldEMailAddress.getText());
        if (_checkBoxRememberPassword.isChecked())
          {
            Configuration.getInstance().setEncryptedProperty("login-password", new String(_passwordFieldPassword.getPassword()));
          }
        else
          {
            Configuration.getInstance().remove("login-password");
          }
      }
    else
      {
        Configuration.getInstance().remove("login-email-address");
        Configuration.getInstance().remove("login-password");
      }
    Configuration.getInstance().setBooleanProperty("login-remember-my-email", _checkBoxRememberEMail.isChecked());
    Configuration.getInstance().setBooleanProperty("login-remember-my-password", _checkBoxRememberPassword.isChecked());
    Configuration.getInstance().setBooleanProperty("login-sign-me-in-automatically", _checkBoxAutoSignIn.isChecked());
    Configuration.getInstance().save();
    InfoUserTypeVO newInfoUserTypeVO = new InfoUserTypeVO();
    newInfoUserTypeVO.setLocationIsVisible(!_checkBoxSignInInvis.isChecked());
    try
      {
        API.getInstance().getUser().infoSet(Session.getInstance().getUserInfo(), newInfoUserTypeVO);
      }
    catch (Exception exception)
      {
        getParentPanel().showWindow(new WarningWindow("Unable to set", "Unable to set you invisable! Please contact to admin.", 30, "sign-in-unable-to-set-invisable.wav"));
        try
          {
            API.getInstance().getAuth().logout();
          }
        catch (Exception logoutException)
          {
            Debug.displayStack(this, logoutException);
          }
        return;
      }
    try
      {
        Session.getInstance().setUserInfo(API.getInstance().getUser().infoGet());
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    getParentPanel().getMainPanel().showMapViewPanel();
  }

  private void _buttonSignUp_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().showWindow(new SignUpWindow());
  }

  private void _checkBoxRememberPassword_actionPerformed(ActionEvent actionEvent)
  {
    _checkBoxAutoSignIn.setEnabled(!_checkBoxRememberPassword.isChecked());
    _checkBoxAutoSignIn.setChecked(false);
  }

  private void _checkBoxRememberEMail_actionPerformed(ActionEvent actionEvent)
  {
    _checkBoxRememberPassword.setEnabled(!_checkBoxRememberEMail.isChecked());
    _checkBoxAutoSignIn.setEnabled(!_checkBoxRememberEMail.isChecked());
    _checkBoxRememberPassword.setChecked(false);
    _checkBoxAutoSignIn.setChecked(false);
  }

  private void _textFieldEMailAddress_keyPressed(KeyEvent keyEvent)
  {
    if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
      _buttonSignIn_actionPerformed(null);
  }

  private void _passwordFieldPassword_keyPressed(KeyEvent keyEvent)
  {
    if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
      _buttonSignIn_actionPerformed(null);
  }

  protected void _this_componentShown(ComponentEvent componentEvent)
  {
    _textFieldEMailAddress.requestFocus();
  }
}
