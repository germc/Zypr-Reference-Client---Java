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
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.Format;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import javax.swing.SwingConstants;
import javax.swing.Timer;

import net.zypr.api.enums.DateFormatPattern;
import net.zypr.api.enums.DistanceUnit;
import net.zypr.api.enums.TemperatureScale;
import net.zypr.api.enums.TimeFormatPattern;
import net.zypr.gui.Settings;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.Separator;
import net.zypr.gui.panels.MapViewPanel;
import net.zypr.gui.utils.Converter;

public class SettingsUnitsWindow
  extends ModalWindow
{
  private Separator _separatorTop = new Separator();
  private Separator _separatorBottom = new Separator();
  private Button _buttonCancel = new Button("button-media-source-settings-cancel-up.png", "button-media-source-settings-cancel-down.png");
  private Button _buttonOkay = new Button("button-media-source-settings-save-up.png", "button-media-source-settings-save-down.png");
  private Button _buttonTimeFormatPrevious = new Button("button-media-source-settings-previous-up.png", "button-media-source-settings-previous-down.png", "button-media-source-settings-previous-disabled.png");
  private Button _buttonTimeFormatNext = new Button("button-media-source-settings-next-up.png", "button-media-source-settings-next-down.png", "button-media-source-settings-next-disabled.png");
  private Button _buttonDateFormatPrevious = new Button("button-media-source-settings-previous-up.png", "button-media-source-settings-previous-down.png", "button-media-source-settings-previous-disabled.png");
  private Button _buttonDateFormatNext = new Button("button-media-source-settings-next-up.png", "button-media-source-settings-next-down.png", "button-media-source-settings-next-disabled.png");
  private Label _labelTimeFormatValue = new Label("", SwingConstants.CENTER);
  private Label _labelDateFormatValue = new Label("", SwingConstants.CENTER);
  private Button _buttonDistanceTypePrevious = new Button("button-media-source-settings-previous-up.png", "button-media-source-settings-previous-down.png", "button-media-source-settings-previous-disabled.png");
  private Button _buttonDistanceTypeNext = new Button("button-media-source-settings-next-up.png", "button-media-source-settings-next-down.png", "button-media-source-settings-next-disabled.png");
  private Button _buttonTemperatureTypePrevious = new Button("button-media-source-settings-previous-up.png", "button-media-source-settings-previous-down.png", "button-media-source-settings-previous-disabled.png");
  private Button _buttonTemperatureTypeNext = new Button("button-media-source-settings-next-up.png", "button-media-source-settings-next-down.png", "button-media-source-settings-next-disabled.png");
  private Label _labelDistanceTypeValue = new Label("", SwingConstants.CENTER);
  private Label _labelTemperatureTypeValue = new Label("", SwingConstants.CENTER);
  private Label _labelTimeFormat = new Label("Time Format", SwingConstants.CENTER);
  private Label _labelDateFormat = new Label("Date Format", SwingConstants.CENTER);
  private Label _labelTemperatureType = new Label("Temperature", SwingConstants.CENTER);
  private Label _labelDistanceType = new Label("Distance", SwingConstants.CENTER);
  private int _selectedTimeFormatIndex = -1;
  private int _selectedDateFormatIndex = -1;
  private int _selectedDistanceTypeIndex = -1;
  private int _selectedTemperatureTypeIndex = -1;
  private Timer _timerUpdater = null;

  public SettingsUnitsWindow()
  {
    super();
    try
      {
        jbInit();
      }
    catch (Exception exception)
      {
        exception.printStackTrace();
      }
  }

  private void jbInit()
    throws Exception
  {
    _separatorTop.setBounds(new Rectangle(15, 90, 490, 2));
    _separatorBottom.setBounds(new Rectangle(15, 170, 490, 2));
    setTitle("Units Settings");
    this.setSize(new Dimension(520, 270));
    _buttonCancel.setLocation(180, 180);
    _buttonCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonCancel_actionPerformed(actionEvent);
      }
    });
    _buttonOkay.setLocation(295, 180);
    _buttonOkay.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonOkay_actionPerformed(actionEvent);
      }
    });
    _buttonTimeFormatPrevious.setLocation(15, 35);
    _buttonTimeFormatPrevious.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonTimeFormatPrevious_actionPerformed(actionEvent);
      }
    });
    _buttonTimeFormatNext.setLocation(200, 35);
    _buttonTimeFormatNext.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonTimeFormatNext_actionPerformed(actionEvent);
      }
    });
    _labelTimeFormatValue.setBounds(new Rectangle(70, 35, 130, 45));
    _labelTimeFormatValue.setFont(new Font("Dialog", 1, 14));
    _buttonDateFormatPrevious.setLocation(265, 35);
    _buttonDateFormatPrevious.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonDateFormatPrevious_actionPerformed(actionEvent);
      }
    });
    _buttonDateFormatNext.setLocation(450, 35);
    _buttonDateFormatNext.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonDateFormatNext_actionPerformed(actionEvent);
      }
    });
    _labelDateFormatValue.setBounds(new Rectangle(320, 35, 130, 45));
    _labelDateFormatValue.setFont(new Font("Dialog", 1, 14));
    _buttonDistanceTypePrevious.setLocation(15, 120);
    _buttonDistanceTypePrevious.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonDistanceTypePrevious_actionPerformed(actionEvent);
      }
    });
    _buttonDistanceTypeNext.setLocation(200, 120);
    _buttonDistanceTypeNext.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonDistanceTypeNext_actionPerformed(actionEvent);
      }
    });
    _labelDistanceTypeValue.setBounds(new Rectangle(70, 120, 130, 45));
    _buttonTemperatureTypePrevious.setLocation(265, 120);
    _buttonTemperatureTypePrevious.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonTemperatureTypePrevious_actionPerformed(actionEvent);
      }
    });
    _buttonTemperatureTypeNext.setLocation(450, 120);
    _buttonTemperatureTypeNext.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonTemperatureTypeNext_actionPerformed(actionEvent);
      }
    });
    _labelTemperatureTypeValue.setBounds(new Rectangle(320, 120, 130, 45));
    _labelTimeFormat.setBounds(new Rectangle(20, 10, 230, 20));
    _labelDateFormat.setBounds(new Rectangle(270, 10, 230, 20));
    _labelTemperatureType.setBounds(new Rectangle(270, 95, 230, 20));
    _labelDistanceType.setBounds(new Rectangle(20, 95, 230, 20));
    _panelContent.add(_labelDistanceType, null);
    _panelContent.add(_labelTemperatureType, null);
    _panelContent.add(_labelDateFormat, null);
    _panelContent.add(_labelTimeFormat, null);
    _panelContent.add(_labelTemperatureTypeValue, null);
    _panelContent.add(_labelDistanceTypeValue, null);
    _panelContent.add(_buttonDistanceTypeNext, null);
    _panelContent.add(_buttonDistanceTypePrevious, null);
    _panelContent.add(_buttonTemperatureTypeNext, null);
    _panelContent.add(_buttonTemperatureTypePrevious, null);
    _panelContent.add(_labelDateFormatValue, null);
    _panelContent.add(_labelTimeFormatValue, null);
    _panelContent.add(_buttonTimeFormatNext, null);
    _panelContent.add(_buttonTimeFormatPrevious, null);
    _panelContent.add(_buttonDateFormatNext, null);
    _panelContent.add(_buttonDateFormatPrevious, null);
    _panelContent.add(_separatorTop, null);
    _panelContent.add(_separatorBottom, null);
    _panelContent.add(_buttonOkay, null);
    _panelContent.add(_buttonCancel, null);
    TimeFormatPattern[] timeFormatPatterns = TimeFormatPattern.values();
    TimeFormatPattern timeFormatPattern = Settings.getInstance().getDTO().getTimeFormatPattern();
    for (int index = 0; index < timeFormatPatterns.length && _selectedTimeFormatIndex == -1; index++)
      if (timeFormatPatterns[index] == timeFormatPattern)
        _selectedTimeFormatIndex = index;
    _selectedTimeFormatIndex = _selectedTimeFormatIndex == -1 ? 0 : _selectedTimeFormatIndex;
    DateFormatPattern[] dateFormatPatterns = DateFormatPattern.values();
    DateFormatPattern dateFormatPattern = Settings.getInstance().getDTO().getDateFormatPattern();
    for (int index = 0; index < dateFormatPatterns.length && _selectedDateFormatIndex == -1; index++)
      if (dateFormatPatterns[index] == dateFormatPattern)
        _selectedDateFormatIndex = index;
    _selectedDateFormatIndex = _selectedDateFormatIndex == -1 ? 0 : _selectedDateFormatIndex;
    DistanceUnit[] distanceUnits = DistanceUnit.values();
    DistanceUnit distanceUnit = Settings.getInstance().getDTO().getDistanceUnit();
    for (int index = 0; index < distanceUnits.length && _selectedDistanceTypeIndex == -1; index++)
      if (distanceUnits[index] == distanceUnit)
        _selectedDistanceTypeIndex = index;
    _selectedDistanceTypeIndex = _selectedDistanceTypeIndex == -1 ? 0 : _selectedDistanceTypeIndex;
    TemperatureScale[] temperatureScales = TemperatureScale.values();
    TemperatureScale temperatureScale = Settings.getInstance().getDTO().getTemperatureScale();
    for (int index = 0; index < temperatureScales.length && _selectedTemperatureTypeIndex == -1; index++)
      if (temperatureScales[index] == temperatureScale)
        _selectedTemperatureTypeIndex = index;
    _selectedTemperatureTypeIndex = _selectedTemperatureTypeIndex == -1 ? 0 : _selectedTemperatureTypeIndex;
    setGUI();
  }

  protected void windowVisible()
  {
    _timerUpdater = new Timer(1000, new ActionListener()
        {
          public void actionPerformed(ActionEvent actionEvent)
          {
            updateTimeDate();
          }
        });
    _timerUpdater.start();
    setGUI();
  }

  private void setGUI()
  {
    updateTimeDate();
    DistanceUnit distanceUnit = DistanceUnit.values()[_selectedDistanceTypeIndex];
    TemperatureScale temperatureScale = TemperatureScale.values()[_selectedTemperatureTypeIndex];
    _labelDistanceTypeValue.setText(distanceUnit.getPluralName());
    _labelTemperatureTypeValue.setText(temperatureScale.getName());
    if (_selectedTimeFormatIndex == 0)
      {
        _buttonTimeFormatPrevious.setEnabled(false);
        _buttonTimeFormatNext.setEnabled(true);
      }
    else if (_selectedTimeFormatIndex == TimeFormatPattern.values().length - 1)
      {
        _buttonTimeFormatNext.setEnabled(false);
        _buttonTimeFormatPrevious.setEnabled(true);
      }
    else
      {
        _buttonTimeFormatPrevious.setEnabled(true);
        _buttonTimeFormatNext.setEnabled(true);
      }
    if (_selectedDateFormatIndex == 0)
      {
        _buttonDateFormatPrevious.setEnabled(false);
        _buttonDateFormatNext.setEnabled(true);
      }
    else if (_selectedDateFormatIndex == DateFormatPattern.values().length - 1)
      {
        _buttonDateFormatNext.setEnabled(false);
        _buttonDateFormatPrevious.setEnabled(true);
      }
    else
      {
        _buttonDateFormatPrevious.setEnabled(true);
        _buttonDateFormatNext.setEnabled(true);
      }
    if (_selectedDistanceTypeIndex == 0)
      {
        _buttonDistanceTypePrevious.setEnabled(false);
        _buttonDistanceTypeNext.setEnabled(true);
      }
    else if (_selectedDistanceTypeIndex == DistanceUnit.values().length - 1)
      {
        _buttonDistanceTypePrevious.setEnabled(true);
        _buttonDistanceTypeNext.setEnabled(false);
      }
    else
      {
        _buttonDistanceTypePrevious.setEnabled(true);
        _buttonDistanceTypeNext.setEnabled(true);
      }
    if (_selectedTemperatureTypeIndex == 0)
      {
        _buttonTemperatureTypePrevious.setEnabled(false);
        _buttonTemperatureTypeNext.setEnabled(true);
      }
    else if (_selectedTemperatureTypeIndex == TemperatureScale.values().length - 1)
      {
        _buttonTemperatureTypePrevious.setEnabled(true);
        _buttonTemperatureTypeNext.setEnabled(false);
      }
    else
      {
        _buttonTemperatureTypePrevious.setEnabled(true);
        _buttonTemperatureTypeNext.setEnabled(true);
      }
  }

  private void updateTimeDate()
  {
    Date date = Calendar.getInstance().getTime();
    TimeFormatPattern timeFormatPattern = TimeFormatPattern.values()[_selectedTimeFormatIndex];
    Format formatter = new SimpleDateFormat(timeFormatPattern.getPattern());
    _labelTimeFormatValue.setText("<html><center>" + formatter.format(date).toString() + "<br/>(" + timeFormatPattern.getPattern() + ")</center></html>");
    formatter = null;
    DateFormatPattern dateFormatPattern = DateFormatPattern.values()[_selectedDateFormatIndex];
    formatter = new SimpleDateFormat(dateFormatPattern.getPattern());
    _labelDateFormatValue.setText("<html><center>" + formatter.format(date).toString() + "<br/>(" + dateFormatPattern.getPattern() + ")</center></html>");
    formatter = null;
  }

  private void _buttonTimeFormatPrevious_actionPerformed(ActionEvent actionEvent)
  {
    _selectedTimeFormatIndex--;
    if (_selectedTimeFormatIndex < 0)
      _selectedTimeFormatIndex = 0;
    setGUI();
  }

  private void _buttonTimeFormatNext_actionPerformed(ActionEvent actionEvent)
  {
    _selectedTimeFormatIndex++;
    if (_selectedTimeFormatIndex >= TimeFormatPattern.values().length)
      _selectedTimeFormatIndex = TimeFormatPattern.values().length - 1;
    setGUI();
  }

  private void _buttonDateFormatPrevious_actionPerformed(ActionEvent actionEvent)
  {
    _selectedDateFormatIndex--;
    if (_selectedDateFormatIndex < 0)
      _selectedDateFormatIndex = 0;
    setGUI();
  }

  private void _buttonDateFormatNext_actionPerformed(ActionEvent actionEvent)
  {
    _selectedDateFormatIndex++;
    if (_selectedDateFormatIndex >= DateFormatPattern.values().length)
      _selectedDateFormatIndex = DateFormatPattern.values().length - 1;
    setGUI();
  }

  private void _buttonDistanceTypePrevious_actionPerformed(ActionEvent actionEvent)
  {
    _selectedDistanceTypeIndex--;
    if (_selectedDistanceTypeIndex < 0)
      _selectedDistanceTypeIndex = 0;
    setGUI();
  }

  private void _buttonDistanceTypeNext_actionPerformed(ActionEvent actionEvent)
  {
    _selectedDistanceTypeIndex++;
    if (_selectedDistanceTypeIndex >= DistanceUnit.values().length)
      _selectedDistanceTypeIndex = DistanceUnit.values().length - 1;
    setGUI();
  }

  private void _buttonTemperatureTypePrevious_actionPerformed(ActionEvent actionEvent)
  {
    _selectedTemperatureTypeIndex--;
    if (_selectedTemperatureTypeIndex < 0)
      _selectedTemperatureTypeIndex = 0;
    setGUI();
  }

  private void _buttonTemperatureTypeNext_actionPerformed(ActionEvent actionEvent)
  {
    _selectedTemperatureTypeIndex++;
    if (_selectedTemperatureTypeIndex >= TemperatureScale.values().length)
      _selectedTemperatureTypeIndex = TemperatureScale.values().length - 1;
    setGUI();
  }

  private void _buttonCancel_actionPerformed(ActionEvent actionEvent)
  {
    _buttonWindowClose_actionPerformed(null);
  }

  private void _buttonOkay_actionPerformed(ActionEvent actionEvent)
  {
    Settings.getInstance().getDTO().setTimeFormatPattern(TimeFormatPattern.values()[_selectedTimeFormatIndex]);
    Settings.getInstance().getDTO().setDateFormatPattern(DateFormatPattern.values()[_selectedDateFormatIndex]);
    Settings.getInstance().getDTO().setVoiceNoteReadRadius((int) Converter.convertDistances(Settings.getInstance().getDTO().getDistanceUnit(), DistanceUnit.values()[_selectedDistanceTypeIndex], Settings.getInstance().getDTO().getVoiceNoteReadRadius()));
    Settings.getInstance().getDTO().setDistanceUnit(DistanceUnit.values()[_selectedDistanceTypeIndex]);
    Settings.getInstance().getDTO().setTemperatureScale(TemperatureScale.values()[_selectedTemperatureTypeIndex]);
    Settings.getInstance().save();
    ((MapViewPanel) getParentPanel()).updateCurrentWeather();
    _buttonWindowClose_actionPerformed(null);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    _timerUpdater.stop();
    _timerUpdater = null;
    getParentPanel().discardWindow();
  }
}
