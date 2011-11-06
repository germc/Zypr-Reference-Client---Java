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

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.zypr.api.API;
import net.zypr.api.enums.VoiceCommandAction;
import net.zypr.api.enums.VoiceCommandContextState;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.vo.ContextDataVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.api.vo.VoiceCommandVariableVO;
import net.zypr.gui.Settings;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.panels.MapViewPanel;
import net.zypr.gui.renderers.RadioStationListRenderer;
import net.zypr.gui.utils.Debug;
import net.zypr.mmp.mplayer.MPlayer;
import net.zypr.mmp.mplayer.TTSPlayer;

public class RadioStationListWindow
  extends ModalWindow
{
  private ScrollPane _scrollPaneRadioStationList = new ScrollPane();
  private DefaultListModel _listRadioStationModel = new DefaultListModel();
  private JList _listRadioStationList = new JList(_listRadioStationModel);
  private ItemVO[] _itemVOs;
  private String _term = null;

  public RadioStationListWindow(String term)
  {
    super();
    _term = term;
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
    this.setSize(new Dimension(560, 293));
    setTitle((_term != null ? _term + " " : "") + "Please Select Station by Name or Number...");
    _scrollPaneRadioStationList.setBounds(15, 10, 530, 237);
    _listRadioStationList.setOpaque(false);
    _listRadioStationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _listRadioStationList.setCellRenderer(new RadioStationListRenderer());
    _listRadioStationList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent listSelectionEvent)
      {
        _listRadioStationList_valueChanged(listSelectionEvent);
      }
    });
    _listRadioStationList.setVisibleRowCount(JLIST_VISIBLE_ROW_COUNT);
    _scrollPaneRadioStationList.addToViewport(_listRadioStationList);
    _scrollPaneRadioStationList.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollRadioStationList_scrollUp(actionEvent);
      }
    });
    _scrollPaneRadioStationList.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollRadioStationList_scrollDown(actionEvent);
      }
    });
    _panelContent.add(_scrollPaneRadioStationList, null);
    try
      {
        _itemVOs = API.getInstance().getMedia().search(_term).getItems();
        for (int index = 0; index < _itemVOs.length; index++)
          _listRadioStationModel.addElement(_itemVOs[index]);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  protected void windowVisible()
  {
    setVoiceCommandState(VoiceCommandState.CONFIRM_STATION_LIST);
    setVoiceCommandContextState(new VoiceCommandContextState[]
        { VoiceCommandContextState.PAGE_SELECTION });
    if (_itemVOs != null)
      for (int index = 0; index < _itemVOs.length; index++)
        addContextData(new ContextDataVO(_itemVOs[index].getName(), "" + (index + 1)));
    new Thread(new Runnable()
    {
      public void run()
      {
        try
          {
            if (Settings.getInstance().getDTO().isTTSFollowUpList())
              TTSPlayer.getInstance().loadPlaylist(API.getInstance().getVoice().ttsPLS("voicebox2", (Settings.getInstance().getDTO().isTTSCardHeader() ? getTitle() : null), getContextData()));
            else if (Settings.getInstance().getDTO().isTTSCardHeader())
              TTSPlayer.getInstance().loadPlaylist(API.getInstance().getVoice().tts("voicebox2", getTitle()).getAudioURI());
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
    }).start();
  }

  private void _listRadioStationList_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    if (!listSelectionEvent.getValueIsAdjusting())
      {
        ((MapViewPanel) getParent()).showWindow(new RadioStationDetailsWindow((ItemVO) _listRadioStationList.getSelectedValue(), true));
      }
  }

  public boolean processVoiceCommand(VoiceCommandVO voiceCommandVO)
  {
    try
      {
        VoiceCommandVariableVO voiceCommandVariableVO = null;
        if (voiceCommandVO.getName() == VoiceCommandAction.ITEM_SELECTED_4_ITEM_NUMBER)
          {
            voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_number");
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.ITEM_SELECTED_4_ITEM_NAME)
          {
            voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_id");
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.NEXT_PAGE)
          {
            _scrollRadioStationList_scrollDown(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.PREVIOUS_PAGE)
          {
            _scrollRadioStationList_scrollUp(null);
            return (true);
          }
        else
          return (false);
        MPlayer.getInstance().playRadioStationItem(_itemVOs[Integer.parseInt(voiceCommandVariableVO.getValueAsString()) - 1]);
        ((MapViewPanel) getParent()).discardAllWindows();
        return (true);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    return (false);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }

  private void _scrollRadioStationList_scrollUp(ActionEvent actionEvent)
  {
    if (_listRadioStationList.getFirstVisibleIndex() - _listRadioStationList.getVisibleRowCount() >= 0)
      _listRadioStationList.ensureIndexIsVisible(_listRadioStationList.getFirstVisibleIndex() - _listRadioStationList.getVisibleRowCount());
    else
      _listRadioStationList.ensureIndexIsVisible(0);
  }

  private void _scrollRadioStationList_scrollDown(ActionEvent actionEvent)
  {
    if (_listRadioStationList.getLastVisibleIndex() + _listRadioStationList.getVisibleRowCount() < _listRadioStationList.getModel().getSize())
      _listRadioStationList.ensureIndexIsVisible(_listRadioStationList.getLastVisibleIndex() + _listRadioStationList.getVisibleRowCount());
    else
      _listRadioStationList.ensureIndexIsVisible(_listRadioStationList.getModel().getSize());
  }
}
