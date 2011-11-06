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

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import net.zypr.api.enums.DistanceUnit;
import net.zypr.gui.Settings;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.Separator;
import net.zypr.gui.utils.Debug;

public class SettingsVoiceNoteWindow
  extends ModalWindow
{
  private static final int MAX_VALUE = 999;
  private Separator _separator = new Separator();
  private Button _buttonCancel = new Button("button-voice-note-settings-cancel-up.png", "button-voice-note-settings-cancel-down.png");
  private Button _buttonOkay = new Button("button-voice-note-settings-save-up.png", "button-voice-note-settings-save-down.png");
  private Button _buttonPrevious = new Button("button-voice-note-settings-previous-up.png", "button-voice-note-settings-previous-down.png", "button-voice-note-settings-previous-disabled.png");
  private Button _buttonNext = new Button("button-voice-note-settings-next-up.png", "button-voice-note-settings-next-down.png", "button-voice-note-settings-next-disabled.png");
  private Label _labelReadAutomatically = new Label("Read voice notes automatically in", SwingConstants.CENTER);
  private Label _labelValue = new Label("", SwingConstants.CENTER);
  private int _voiceNoteReadRadius = 0;

  public SettingsVoiceNoteWindow()
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
    this.setSize(400, 220);
    setTitle("Voice Note Settings");
    _buttonCancel.setLocation(115, 130);
    _buttonCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonCancel_actionPerformed(actionEvent);
      }
    });
    _buttonOkay.setLocation(230, 130);
    _buttonOkay.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonOkay_actionPerformed(actionEvent);
      }
    });
    _buttonPrevious.setLocation(65, 55);
    _buttonPrevious.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPrevious_actionPerformed(actionEvent);
      }
    });
    _buttonNext.setLocation(280, 55);
    _buttonNext.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonNext_actionPerformed(actionEvent);
      }
    });
    _labelReadAutomatically.setBounds(new Rectangle(10, 15, 375, 25));
    _labelValue.setBounds(new Rectangle(120, 55, 160, 45));
    _separator.setBounds(10, 120, 375, 1);
    _panelContent.add(_labelValue, null);
    _panelContent.add(_labelReadAutomatically, null);
    _panelContent.add(_buttonNext, null);
    _panelContent.add(_buttonPrevious, null);
    _panelContent.add(_separator, null);
    _panelContent.add(_buttonOkay, null);
    _panelContent.add(_buttonCancel, null);
    _voiceNoteReadRadius = Settings.getInstance().getDTO().getVoiceNoteReadRadius();
    setGUI();
  }

  protected void windowVisible()
  {
    _voiceNoteReadRadius = Settings.getInstance().getDTO().getVoiceNoteReadRadius();
    setGUI();
  }

  private void setGUI()
  {
    DistanceUnit distanceUnit = Settings.getInstance().getDTO().getDistanceUnit();
    if (_voiceNoteReadRadius == 0)
      {
        _buttonNext.setEnabled(true);
        _buttonPrevious.setEnabled(false);
        _labelValue.setText("Off");
      }
    else if (_voiceNoteReadRadius > 0 && _voiceNoteReadRadius <= MAX_VALUE)
      {
        _buttonNext.setEnabled(true);
        _buttonPrevious.setEnabled(true);
        _labelValue.setText(_voiceNoteReadRadius + " " + (_voiceNoteReadRadius > 1 ? distanceUnit.getPluralName() : distanceUnit.getName()));
      }
    else
      {
        _buttonNext.setEnabled(false);
      }
  }

  private void _buttonPrevious_actionPerformed(ActionEvent actionEvent)
  {
    _voiceNoteReadRadius--;
    if (_voiceNoteReadRadius < 0)
      _voiceNoteReadRadius = 0;
    setGUI();
  }

  private void _buttonNext_actionPerformed(ActionEvent actionEvent)
  {
    _voiceNoteReadRadius++;
    if (_voiceNoteReadRadius > MAX_VALUE)
      _voiceNoteReadRadius = MAX_VALUE;
    setGUI();
  }

  private void _buttonCancel_actionPerformed(ActionEvent actionEvent)
  {
    _buttonWindowClose_actionPerformed(null);
  }

  private void _buttonOkay_actionPerformed(ActionEvent actionEvent)
  {
    Settings.getInstance().getDTO().setVoiceNoteReadRadius(_voiceNoteReadRadius);
    Settings.getInstance().save();
    _buttonWindowClose_actionPerformed(null);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }
}
