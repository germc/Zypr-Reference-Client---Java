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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import net.zypr.api.API;
import net.zypr.api.Session;
import net.zypr.gps.GPSDevice;
import net.zypr.gui.Configuration;
import net.zypr.gui.Settings;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.Separator;
import net.zypr.gui.utils.Debug;
import net.zypr.mmp.mplayer.MPlayer;

public class ContextMenuWindow
  extends ModalWindow
{
  /*-
  set my location
  leave voice not
  create a marker
  user hide-show
  poi hide-show
  radio hide-show
  -*/
  private Button _buttonVoiceNote = new Button("button-settings-voice-note-up.png", "button-settings-voice-note-down.png");
  private Button _buttonTextToSpeech = new Button("button-settings-text-to-speech-up.png", "button-settings-text-to-speech-down.png");
  private Button _buttonMediaSource = new Button("button-settings-media-source-up.png", "button-settings-media-source-down.png");
  private Button _buttonPrivacy = new Button("button-settings-privacy-up.png", "button-settings-privacy-down.png");
  private Button _buttonServices = new Button("button-settings-services-up.png", "button-settings-services-down.png");
  private Button _buttonUnits = new Button("button-settings-units-up.png", "button-settings-units-down.png");
  private Button _buttonUser = new Button("button-settings-user-up.png", "button-settings-user-down.png");
  private Button _buttonSignOut = new Button("button-sign-out-up.png", "button-sign-out-down.png");
  private Button _buttonExit = new Button("button-settings-exit-up.png", "button-settings-exit-down.png");
  private Label _labelVoiceNote = new Label("Voice Note", SwingConstants.CENTER);
  private Label _labelTextToSpeech = new Label("Text To Speech", SwingConstants.CENTER);
  private Label _labelMediaSource = new Label("Media Source", SwingConstants.CENTER);
  private Label _labelPrivacy = new Label("Privacy", SwingConstants.CENTER);
  private Label _labelServices = new Label("Services", SwingConstants.CENTER);
  private Label _labelUnits = new Label("Units", SwingConstants.CENTER);
  private Label _labelUser = new Label("User", SwingConstants.CENTER);
  private Label _labelSignOut = new Label("Sign Out", SwingConstants.CENTER);
  private Label _labelExit = new Label("Exit", SwingConstants.CENTER);
  private Separator _separatorHoriontal1 = new Separator(SwingConstants.HORIZONTAL);
  private Separator _separatorHoriontal2 = new Separator(SwingConstants.HORIZONTAL);
  private Separator _separatorHoriontal3 = new Separator(SwingConstants.HORIZONTAL);
  private Separator _separatorHoriontal4 = new Separator(SwingConstants.HORIZONTAL);
  private Separator _separatorVertical1 = new Separator(SwingConstants.VERTICAL);
  private Separator _separatorVertical2 = new Separator(SwingConstants.VERTICAL);
  private Separator _separatorVertical3 = new Separator(SwingConstants.VERTICAL);
  private Separator _separatorVertical4 = new Separator(SwingConstants.VERTICAL);

  public ContextMenuWindow()
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
    this.setSize(new Dimension(522, 340));
    this.setTitle("Settings");
    _buttonVoiceNote.setLocation(73, 20);
    _buttonVoiceNote.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonVoiceNote_actionPerformed(actionEvent);
      }
    });
    _buttonTextToSpeech.setLocation(233, 20);
    _buttonTextToSpeech.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonTextToSpeech_actionPerformed(actionEvent);
      }
    });
    _buttonMediaSource.setLocation(393, 20);
    _buttonMediaSource.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonMediaSource_actionPerformed(actionEvent);
      }
    });
    _buttonPrivacy.setLocation(73, 115);
    _buttonPrivacy.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPrivacy_actionPerformed(actionEvent);
      }
    });
    _buttonServices.setLocation(233, 115);
    _buttonServices.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonServices_actionPerformed(actionEvent);
      }
    });
    _buttonUnits.setLocation(393, 115);
    _buttonUnits.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonUnits_actionPerformed(actionEvent);
      }
    });
    _buttonUser.setLocation(73, 210);
    _buttonUser.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonUser_actionPerformed(actionEvent);
      }
    });
    _buttonSignOut.setLocation(233, 210);
    _buttonSignOut.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonSignOut_actionPerformed(actionEvent);
      }
    });
    _buttonExit.setLocation(393, 210);
    _buttonExit.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonExit_actionPerformed(actionEvent);
      }
    });
    _labelVoiceNote.setBounds(20, 80, 160, 15);
    _labelTextToSpeech.setBounds(180, 80, 160, 15);
    _labelMediaSource.setBounds(340, 80, 160, 15);
    _labelPrivacy.setBounds(20, 175, 160, 15);
    _labelServices.setBounds(180, 175, 160, 15);
    _labelUnits.setBounds(340, 175, 160, 15);
    _labelUser.setBounds(20, 270, 160, 15);
    _labelSignOut.setBounds(180, 270, 160, 15);
    _labelExit.setBounds(340, 270, 160, 15);
    _separatorHoriontal1.setBounds(20, 10, 1, 285);
    _separatorHoriontal2.setBounds(180, 10, 1, 285);
    _separatorHoriontal3.setBounds(340, 10, 1, 285);
    _separatorHoriontal4.setBounds(500, 10, 1, 285);
    _separatorVertical1.setBounds(20, 10, 480, 1);
    _separatorVertical2.setBounds(20, 105, 480, 1);
    _separatorVertical3.setBounds(20, 200, 480, 1);
    _separatorVertical4.setBounds(20, 295, 480, 1);
    _panelContent.add(_buttonVoiceNote, null);
    _panelContent.add(_buttonTextToSpeech, null);
    _panelContent.add(_buttonMediaSource, null);
    _panelContent.add(_buttonPrivacy, null);
    _panelContent.add(_buttonServices, null);
    _panelContent.add(_buttonUnits, null);
    _panelContent.add(_buttonUser, null);
    _panelContent.add(_buttonSignOut, null);
    _panelContent.add(_buttonExit, null);
    _panelContent.add(_labelVoiceNote, null);
    _panelContent.add(_labelTextToSpeech, null);
    _panelContent.add(_labelMediaSource, null);
    _panelContent.add(_labelPrivacy, null);
    _panelContent.add(_labelServices, null);
    _panelContent.add(_labelUnits, null);
    _panelContent.add(_labelUser, null);
    _panelContent.add(_labelSignOut, null);
    _panelContent.add(_labelExit, null);
    _panelContent.add(_separatorHoriontal1, null);
    _panelContent.add(_separatorHoriontal2, null);
    _panelContent.add(_separatorHoriontal3, null);
    _panelContent.add(_separatorHoriontal4, null);
    _panelContent.add(_separatorVertical1, null);
    _panelContent.add(_separatorVertical2, null);
    _panelContent.add(_separatorVertical3, null);
    _panelContent.add(_separatorVertical4, null);
    this.add(_panelContent, BorderLayout.CENTER);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }

  private void _buttonVoiceNote_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().showWindow(new SettingsVoiceNoteWindow());
  }

  private void _buttonTextToSpeech_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().showWindow(new SettingsTextToSpeechWindow());
  }

  private void _buttonMediaSource_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().showWindow(new SettingsMediaSourceWindow());
  }

  private void _buttonPrivacy_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().showWindow(new SettingsPrivacyWindow());
  }

  private void _buttonServices_actionPerformed(ActionEvent actionEvent)
  {
    _buttonServices.setEnabled(false);
    new Thread(new Runnable()
    {
      public void run()
      {
        try
          {
            getParentPanel().showWindow(new SettingsServiceWindow(API.getInstance().getAuth().serviceAuthStatus()));
          }
        catch (Exception exception)
          {
            _buttonServices.setEnabled(true);
            Debug.displayStack(this, exception);
          }
      }
    }).start();
  }

  private void _buttonUnits_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().showWindow(new SettingsUnitsWindow());
  }

  private void _buttonUser_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().showWindow(new SettingsUserWindow());
  }

  private void _buttonSignOut_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardAllWindows();
    Configuration.getInstance().save();
    Settings.getInstance().save();
    Settings.getInstance().clear();
    if (MPlayer.getInstance().isPlaybackStarted())
      MPlayer.getInstance().stopPlayback();
    MPlayer.getInstance().removeAllPropertyChangeListener();
    GPSDevice.getInstance().removeAllPropertyChangeListener();
    try
      {
        API.getInstance().getAuth().logout();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    Session.getInstance().loggedOut();
    Configuration.getInstance().setBooleanProperty("login-sign-me-in-automatically", false);
    getParentPanel().getMainPanel().showStartupPanel();
  }

  private void _buttonExit_actionPerformed(ActionEvent actionEvent)
  {
    Configuration.getInstance().save();
    Settings.getInstance().save();
    try
      {
        API.getInstance().getAuth().logout();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    System.exit(0);
  }
}
