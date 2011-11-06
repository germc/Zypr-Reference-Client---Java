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

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingConstants;

import net.zypr.api.enums.AudioSourceType;
import net.zypr.gui.Settings;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.Separator;
import net.zypr.gui.components.TextField;
import net.zypr.gui.utils.Debug;

public class SettingsMediaSourceWindow
  extends ModalWindow
{
  private Separator _separatorTop = new Separator();
  private Separator _separatorBottom = new Separator();
  private Button _buttonCancel = new Button("button-media-source-settings-cancel-up.png", "button-media-source-settings-cancel-down.png");
  private Button _buttonOkay = new Button("button-media-source-settings-save-up.png", "button-media-source-settings-save-down.png");
  private Button _buttonPrevious = new Button("button-media-source-settings-previous-up.png", "button-media-source-settings-previous-down.png", "button-media-source-settings-previous-disabled.png");
  private Button _buttonNext = new Button("button-media-source-settings-next-up.png", "button-media-source-settings-next-down.png", "button-media-source-settings-next-disabled.png");
  private Label _labelLocalMediaFolder = new Label("Local Media Folder :");
  private Label _labelValue = new Label("", SwingConstants.CENTER);
  private int _selectedIndex = -1;
  private TextField _textFieldLocalMediaFolder = new TextField();
  private Button _buttonBrowse = new Button("button-media-source-settings-browse-up.png", "button-media-source-settings-browse-down.png", "button-media-source-settings-browse-disabled.png");

  public SettingsMediaSourceWindow()
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
    _separatorTop.setBounds(new Rectangle(10, 70, 375, 1));
    _separatorBottom.setBounds(new Rectangle(10, 150, 375, 1));
    setTitle("Voice Note Settings");
    this.setSize(new Dimension(400, 250));
    _buttonCancel.setLocation(115, 160);
    _buttonCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonCancel_actionPerformed(actionEvent);
      }
    });
    _buttonOkay.setLocation(230, 160);
    _buttonOkay.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonOkay_actionPerformed(actionEvent);
      }
    });
    _buttonPrevious.setLocation(65, 15);
    _buttonPrevious.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPrevious_actionPerformed(actionEvent);
      }
    });
    _buttonNext.setLocation(280, 15);
    _buttonNext.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonNext_actionPerformed(actionEvent);
      }
    });
    _buttonBrowse.setLocation(330, 85);
    _buttonBrowse.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        _buttonBrowse_actionPerformed(e);
      }
    });
    _labelLocalMediaFolder.setBounds(new Rectangle(15, 85, 300, 25));
    _labelValue.setBounds(new Rectangle(120, 15, 160, 45));
    _textFieldLocalMediaFolder.setBounds(new Rectangle(15, 110, 300, 20));
    _panelContent.add(_buttonBrowse, null);
    _panelContent.add(_textFieldLocalMediaFolder, null);
    _panelContent.add(_labelValue, null);
    _panelContent.add(_labelLocalMediaFolder, null);
    _panelContent.add(_buttonNext, null);
    _panelContent.add(_buttonPrevious, null);
    _panelContent.add(_separatorTop, null);
    _panelContent.add(_separatorBottom, null);
    _panelContent.add(_buttonOkay, null);
    _panelContent.add(_buttonCancel, null);
    AudioSourceType[] audioSourceTypes = AudioSourceType.values();
    AudioSourceType audioSourceType = Settings.getInstance().getDTO().getAudioSource();
    for (int index = 0; index < audioSourceTypes.length && _selectedIndex == -1; index++)
      if (audioSourceTypes[index] == audioSourceType)
        _selectedIndex = index;
    _selectedIndex = _selectedIndex == -1 ? 0 : _selectedIndex;
    _textFieldLocalMediaFolder.setText(Settings.getInstance().getDTO().getLocalAudioPath());
    setGUI();
  }

  protected void windowVisible()
  {
    setGUI();
  }

  private void setGUI()
  {
    AudioSourceType audioSourceType = AudioSourceType.values()[_selectedIndex];
    if (audioSourceType == AudioSourceType.LOCAL)
      {
        _labelLocalMediaFolder.setEnabled(true);
        _textFieldLocalMediaFolder.setEnabled(true);
        _buttonBrowse.setEnabled(true);
      }
    else
      {
        _labelLocalMediaFolder.setEnabled(false);
        _textFieldLocalMediaFolder.setEnabled(false);
        _buttonBrowse.setEnabled(false);
      }
    _labelValue.setText(audioSourceType.getName());
    if (_selectedIndex == 0)
      {
        _buttonPrevious.setEnabled(false);
      }
    else if (_selectedIndex == AudioSourceType.values().length - 1)
      {
        _buttonNext.setEnabled(false);
      }
    else
      {
        _buttonPrevious.setEnabled(true);
        _buttonNext.setEnabled(true);
      }
  }

  private void _buttonPrevious_actionPerformed(ActionEvent actionEvent)
  {
    _selectedIndex--;
    if (_selectedIndex < 0)
      _selectedIndex = 0;
    setGUI();
  }

  private void _buttonNext_actionPerformed(ActionEvent actionEvent)
  {
    _selectedIndex++;
    if (_selectedIndex >= AudioSourceType.values().length)
      _selectedIndex = AudioSourceType.values().length - 1;
    setGUI();
  }

  private void _buttonCancel_actionPerformed(ActionEvent actionEvent)
  {
    _buttonWindowClose_actionPerformed(null);
  }

  private void _buttonOkay_actionPerformed(ActionEvent actionEvent)
  {
    AudioSourceType audioSourceType = AudioSourceType.values()[_selectedIndex];
    if (audioSourceType == AudioSourceType.LOCAL)
      Settings.getInstance().getDTO().setLocalAudioPath(_textFieldLocalMediaFolder.getText());
    Settings.getInstance().getDTO().setAudioSource(audioSourceType);
    Settings.getInstance().save();
    _buttonWindowClose_actionPerformed(null);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }

  private void _buttonBrowse_actionPerformed(ActionEvent e)
  {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File("."));
    fileChooser.setDialogTitle("Please Select Local Media Folder");
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setAcceptAllFileFilterUsed(false);
    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
      _textFieldLocalMediaFolder.setText(fileChooser.getSelectedFile().toString());
  }
}
