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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.Format;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import net.zypr.api.vo.ItemVO;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.Label;
import net.zypr.gui.utils.Debug;

public class AgendaEditWindow
  extends ModalWindow
{
  private Button _buttonPreviousDay = new Button("button-agenda-previous-day-up.png", "button-agenda-previous-day-down.png");
  private Button _buttonNextDay = new Button("button-agenda-next-day-up.png", "button-agenda-next-day-down.png");
  private Button _buttonPreviousMonth = new Button("button-agenda-previous-month-up.png", "button-agenda-previous-month-down.png");
  private Button _buttonNextMonth = new Button("button-agenda-next-month-up.png", "button-agenda-next-month-down.png");
  private Button _buttonPreviousYear = new Button("button-agenda-previous-year-up.png", "button-agenda-previous-year-down.png");
  private Button _buttonNextYear = new Button("button-agenda-next-year-up.png", "button-agenda-next-year-down.png");
  private Button _buttonPreviousHour = new Button("button-agenda-previous-hour-up.png", "button-agenda-previous-hour-down.png");
  private Button _buttonNextHour = new Button("button-agenda-next-hour-up.png", "button-agenda-next-hour-down.png");
  private Button _buttonPreviousMinute = new Button("button-agenda-previous-minute-up.png", "button-agenda-previous-minute-down.png");
  private Button _buttonNextMinute = new Button("button-agenda-next-minute-up.png", "button-agenda-next-minute-down.png");
  private Button _buttonAM = new Button("button-agenda-am-up.png", "button-agenda-am-down.png");
  private Button _buttonPM = new Button("button-agenda-pm-up.png", "button-agenda-pm-down.png");
  private Button _buttonCancel = new Button("button-agenda-cancel-up.png", "button-agenda-cancel-down.png");
  private Button _buttonSave = new Button("button-agenda-save-up.png", "button-agenda-save-down.png");
  private Button _buttonAddAttachment = new Button("button-agenda-add-attachment-up.png", "button-agenda-add-attachment-down.png");
  private JTextPane _textPaneAgendaNote = new JTextPane();
  private JScrollPane _scrollPaneAgendaNote = new JScrollPane();
  private Button _buttonScrollDown = new Button("button-page-down-window-up.png", "button-page-down-window-down.png");
  private Button _buttonScrollUp = new Button("button-page-up-window-up.png", "button-page-up-window-down.png");
  private Label _labelMonth = new Label(SwingConstants.CENTER);
  private Label _labelDay = new Label(SwingConstants.CENTER);
  private Label _labelYear = new Label(SwingConstants.CENTER);
  private Label _labelHour = new Label(SwingConstants.CENTER);
  private Label _labelMinute = new Label(SwingConstants.CENTER);
  private Label _labelAMPM = new Label(SwingConstants.CENTER);
  private ItemVO _itemVO = null;
  private Calendar _currentDate = Calendar.getInstance();

  public AgendaEditWindow(ItemVO itemVO)
  {
    super();
    try
      {
        _itemVO = itemVO;
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
    _buttonPreviousDay.setLocation(215, 5);
    _buttonPreviousDay.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPreviousDay_actionPerformed(actionEvent);
      }
    });
    _buttonNextDay.setLocation(350, 5);
    _buttonNextDay.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonNextDay_actionPerformed(actionEvent);
      }
    });
    _buttonPreviousMonth.setLocation(15, 5);
    _buttonPreviousMonth.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPreviousMonth_actionPerformed(actionEvent);
      }
    });
    _buttonNextMonth.setLocation(150, 5);
    _buttonNextMonth.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonNextMonth_actionPerformed(actionEvent);
      }
    });
    _buttonPreviousYear.setLocation(415, 5);
    _buttonPreviousYear.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPreviousYear_actionPerformed(actionEvent);
      }
    });
    _buttonNextYear.setLocation(550, 5);
    _buttonNextYear.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonNextYear_actionPerformed(actionEvent);
      }
    });
    _buttonPreviousHour.setLocation(15, 60);
    _buttonPreviousHour.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPreviousHour_actionPerformed(actionEvent);
      }
    });
    _buttonNextHour.setLocation(150, 60);
    _buttonNextHour.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonNextHour_actionPerformed(actionEvent);
      }
    });
    _buttonPreviousMinute.setLocation(215, 60);
    _buttonPreviousMinute.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPreviousMinute_actionPerformed(actionEvent);
      }
    });
    _buttonNextMinute.setLocation(350, 60);
    _buttonNextMinute.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonNextMinute_actionPerformed(actionEvent);
      }
    });
    _buttonAM.setLocation(415, 60);
    _buttonAM.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonAM_actionPerformed(actionEvent);
      }
    });
    _buttonPM.setLocation(550, 60);
    _buttonPM.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPM_actionPerformed(actionEvent);
      }
    });
    _buttonCancel.setLocation(148, 250);
    _buttonCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonCancel_actionPerformed(actionEvent);
      }
    });
    _buttonSave.setLocation(283, 250);
    _buttonSave.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonSave_actionPerformed(actionEvent);
      }
    });
    _buttonAddAttachment.setLocation(418, 250);
    _buttonAddAttachment.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonAddAttachment_actionPerformed(actionEvent);
      }
    });
    _textPaneAgendaNote.setOpaque(false);
    _textPaneAgendaNote.setEditable(true);
    _textPaneAgendaNote.setForeground(Color.WHITE);
    _textPaneAgendaNote.setContentType("text/plain");
    _textPaneAgendaNote.setFont(new Font("Dialog", 1, 16));
    _scrollPaneAgendaNote.setBounds(15, 115, 565, 125);
    _scrollPaneAgendaNote.setOpaque(false);
    _scrollPaneAgendaNote.getViewport().setOpaque(false);
    _scrollPaneAgendaNote.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    _scrollPaneAgendaNote.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    _scrollPaneAgendaNote.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    _scrollPaneAgendaNote.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    _scrollPaneAgendaNote.getViewport().add(_textPaneAgendaNote, null);
    _buttonScrollDown.setLocation(580, 210);
    _buttonScrollDown.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonScrollDown_actionPerformed(actionEvent);
      }
    });
    _buttonScrollUp.setLocation(580, 115);
    _buttonScrollUp.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonScrollUp_actionPerformed(actionEvent);
      }
    });
    _labelMonth.setBounds(70, 10, 80, 35);
    _labelDay.setBounds(270, 10, 80, 35);
    _labelYear.setBounds(470, 10, 80, 35);
    _labelHour.setBounds(70, 65, 80, 35);
    _labelMinute.setBounds(270, 65, 80, 35);
    _labelAMPM.setBounds(470, 65, 80, 35);
    _panelContent.add(_buttonPreviousHour, null);
    _panelContent.add(_buttonNextHour, null);
    _panelContent.add(_buttonPreviousMinute, null);
    _panelContent.add(_buttonNextMinute, null);
    _panelContent.add(_buttonAM, null);
    _panelContent.add(_buttonPM, null);
    _panelContent.add(_labelHour, null);
    _panelContent.add(_labelMinute, null);
    _panelContent.add(_labelAMPM, null);
    _panelContent.add(_labelMonth, null);
    _panelContent.add(_labelDay, null);
    _panelContent.add(_labelYear, null);
    _panelContent.add(_buttonNextDay, null);
    _panelContent.add(_buttonPreviousDay, null);
    _panelContent.add(_buttonNextMonth, null);
    _panelContent.add(_buttonPreviousMonth, null);
    _panelContent.add(_buttonNextYear, null);
    _panelContent.add(_buttonPreviousYear, null);
    _panelContent.add(_buttonScrollUp, null);
    _panelContent.add(_buttonScrollDown, null);
    _panelContent.add(_scrollPaneAgendaNote, null);
    _panelContent.add(_buttonCancel, null);
    _panelContent.add(_buttonSave, null);
    _panelContent.add(_buttonAddAttachment, null);
    updateDateFields();
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    super._buttonWindowClose_actionPerformed(actionEvent);
    /*- Implement it -*/
    Debug.print(actionEvent);
  }

  private void _buttonScrollUp_actionPerformed(ActionEvent actionEvent)
  {
    _scrollPaneAgendaNote.getVerticalScrollBar().setValue(_scrollPaneAgendaNote.getVerticalScrollBar().getValue() - 10);
  }

  private void _buttonScrollDown_actionPerformed(ActionEvent actionEvent)
  {
    _scrollPaneAgendaNote.getVerticalScrollBar().setValue(_scrollPaneAgendaNote.getVerticalScrollBar().getValue() + 10);
  }

  private void _buttonPreviousMonth_actionPerformed(ActionEvent actionEvent)
  {
    _currentDate.add(Calendar.MONTH, -1);
    updateDateFields();
  }

  private void _buttonNextMonth_actionPerformed(ActionEvent actionEvent)
  {
    _currentDate.add(Calendar.MONTH, 1);
    updateDateFields();
  }

  private void _buttonPreviousDay_actionPerformed(ActionEvent actionEvent)
  {
    _currentDate.add(Calendar.DAY_OF_MONTH, -1);
    updateDateFields();
  }

  private void _buttonNextDay_actionPerformed(ActionEvent actionEvent)
  {
    _currentDate.add(Calendar.DAY_OF_MONTH, 1);
    updateDateFields();
  }

  private void _buttonPreviousYear_actionPerformed(ActionEvent actionEvent)
  {
    _currentDate.add(Calendar.YEAR, -1);
    updateDateFields();
  }

  private void _buttonNextYear_actionPerformed(ActionEvent actionEvent)
  {
    _currentDate.add(Calendar.YEAR, 1);
    updateDateFields();
  }

  private void _buttonPreviousHour_actionPerformed(ActionEvent actionEvent)
  {
    _currentDate.add(Calendar.HOUR_OF_DAY, -1);
    updateDateFields();
  }

  private void _buttonNextHour_actionPerformed(ActionEvent actionEvent)
  {
    _currentDate.add(Calendar.HOUR_OF_DAY, 1);
    updateDateFields();
  }

  private void _buttonPreviousMinute_actionPerformed(ActionEvent actionEvent)
  {
    _currentDate.add(Calendar.MINUTE, -1);
    updateDateFields();
  }

  private void _buttonNextMinute_actionPerformed(ActionEvent actionEvent)
  {
    _currentDate.add(Calendar.MINUTE, 1);
    updateDateFields();
  }

  private void _buttonAM_actionPerformed(ActionEvent actionEvent)
  {
    _currentDate.set(Calendar.AM_PM, Calendar.AM);
    updateDateFields();
  }

  private void _buttonPM_actionPerformed(ActionEvent actionEvent)
  {
    _currentDate.set(Calendar.AM_PM, Calendar.PM);
    updateDateFields();
  }

  private void _buttonCancel_actionPerformed(ActionEvent actionEvent)
  {
    _buttonWindowClose_actionPerformed(actionEvent);
  }

  private void _buttonSave_actionPerformed(ActionEvent actionEvent)
  {
    /*- Implement it -*/
    Debug.print(actionEvent);
  }

  private void _buttonAddAttachment_actionPerformed(ActionEvent actionEvent)
  {
    /*- Implement it -*/
    Debug.print(actionEvent);
  }

  private void updateDateFields()
  {
    Date date = _currentDate.getTime();
    Format formatter = new SimpleDateFormat("MMM");
    _labelMonth.setText(formatter.format(date).toString());
    formatter = null;
    formatter = new SimpleDateFormat("d");
    _labelDay.setText(formatter.format(date).toString());
    formatter = null;
    formatter = new SimpleDateFormat("yyyy");
    _labelYear.setText(formatter.format(date).toString());
    formatter = null;
    formatter = new SimpleDateFormat("KK");
    _labelHour.setText(formatter.format(date).toString());
    formatter = null;
    formatter = new SimpleDateFormat("m");
    _labelMinute.setText(formatter.format(date).toString());
    formatter = null;
    formatter = new SimpleDateFormat("a");
    _labelAMPM.setText(formatter.format(date).toString());
    formatter = null;
    formatter = new SimpleDateFormat("MMMM d, yyyy KK:mm a");
    setTitle("Agenda item for " + formatter.format(date).toString());
    formatter = null;
  }
}
