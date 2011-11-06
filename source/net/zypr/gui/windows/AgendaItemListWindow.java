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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.Format;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.zypr.api.enums.VoiceCommandContextState;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.PressAndHoldButton;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.renderers.AgendaItemListRenderer;
import net.zypr.gui.utils.Debug;

public class AgendaItemListWindow
  extends ModalWindow
{
  private Label _labelDay = new Label(SwingConstants.CENTER);
  private Label _labelMonth = new Label(SwingConstants.CENTER);
  private Label _labelYear = new Label(SwingConstants.CENTER);
  private Button _buttonPreviousDay = new Button("button-agenda-previous-day-up.png", "button-agenda-previous-day-down.png");
  private Button _buttonNextDay = new Button("button-agenda-next-day-up.png", "button-agenda-next-day-down.png");
  private Button _buttonPreviousMonth = new Button("button-agenda-previous-month-up.png", "button-agenda-previous-month-down.png");
  private Button _buttonNextMonth = new Button("button-agenda-next-month-up.png", "button-agenda-next-month-down.png");
  private Button _buttonPreviousYear = new Button("button-agenda-previous-year-up.png", "button-agenda-previous-year-down.png");
  private Button _buttonNextYear = new Button("button-agenda-next-year-up.png", "button-agenda-next-year-down.png");
  private ScrollPane _scrollPaneAgendaItemList = new ScrollPane();
  private DefaultListModel _listModelAgendaItemList = new DefaultListModel();
  private JList _listAgendaItemList = new JList(_listModelAgendaItemList);
  private PressAndHoldButton _buttonAddToRoute = new PressAndHoldButton("button-agenda-add-all-to-route-up.png", "button-agenda-add-all-to-route-down.png");
  private Button _buttonToday = new Button("button-agenda-today-up.png", "button-agenda-today-down.png");
  private PressAndHoldButton _buttonDeleteAll = new PressAndHoldButton("button-agenda-delete-all-up.png", "button-agenda-delete-all-down.png");
  private Calendar _currentDate = Calendar.getInstance();

  public AgendaItemListWindow()
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
    this.setSize(620, 340);
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
    _listAgendaItemList.setOpaque(false);
    _listAgendaItemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _listAgendaItemList.setCellRenderer(new AgendaItemListRenderer());
    _listAgendaItemList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        _listAgendaItemList_valueChanged(e);
      }
    });
    _scrollPaneAgendaItemList.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _listAgendaItemList.ensureIndexIsVisible(_listAgendaItemList.getFirstVisibleIndex() - 1);
      }
    });
    _scrollPaneAgendaItemList.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _listAgendaItemList.ensureIndexIsVisible(_listAgendaItemList.getLastVisibleIndex() + 1);
      }
    });
    _scrollPaneAgendaItemList.setBounds(15, 55, 589, 190);
    _scrollPaneAgendaItemList.addToViewport(_listAgendaItemList);
    _buttonAddToRoute.setLocation(190, 251);
    _buttonAddToRoute.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonAddToRoute_actionPerformed(actionEvent);
      }
    });
    _buttonToday.setLocation(270, 251);
    _buttonToday.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonToday_actionPerformed(actionEvent);
      }
    });
    _buttonDeleteAll.setLocation(350, 251);
    _buttonDeleteAll.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonDeleteAll_actionPerformed(actionEvent);
      }
    });
    _labelMonth.setBounds(70, 10, 80, 35);
    _labelDay.setBounds(270, 10, 80, 35);
    _labelYear.setBounds(470, 10, 80, 35);
    _panelContent.add(_buttonNextYear, null);
    _panelContent.add(_labelYear, null);
    _panelContent.add(_labelDay, null);
    _panelContent.add(_labelMonth, null);
    _panelContent.add(_buttonNextDay, null);
    _panelContent.add(_buttonDeleteAll, null);
    _panelContent.add(_buttonAddToRoute, null);
    _panelContent.add(_buttonPreviousDay, null);
    _panelContent.add(_buttonNextMonth, null);
    _panelContent.add(_buttonPreviousMonth, null);
    _panelContent.add(_buttonPreviousYear, null);
    _panelContent.add(_buttonToday, null);
    _panelContent.add(_scrollPaneAgendaItemList, BorderLayout.CENTER);
    setVoiceCommandContextState(new VoiceCommandContextState[]
        { VoiceCommandContextState.MESSAGE_LIST, VoiceCommandContextState.PAGE_SELECTION, VoiceCommandContextState.APPOINTMENT_NAVIGATION });
    updateDateFields();
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
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

  private void _buttonAddToRoute_actionPerformed(ActionEvent actionEvent)
  {
    /*- Implement it -*/
    Debug.print(actionEvent);
  }

  private void _buttonToday_actionPerformed(ActionEvent actionEvent)
  {
    _currentDate = Calendar.getInstance();
    updateDateFields();
  }

  private void _listAgendaItemList_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    if (!listSelectionEvent.getValueIsAdjusting())
      {
        Debug.print(listSelectionEvent);
        hideWindow();
      }
  }

  private void _buttonDeleteAll_actionPerformed(ActionEvent actionEvent)
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
    formatter = new SimpleDateFormat("MMMM d, yyyy");
    setTitle("Agenda for " + formatter.format(date).toString());
    formatter = null;
  }
}
