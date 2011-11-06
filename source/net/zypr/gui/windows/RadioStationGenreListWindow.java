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
import net.zypr.gui.renderers.RadioStationGenreListRenderer;
import net.zypr.gui.utils.Debug;
import net.zypr.mmp.mplayer.TTSPlayer;

public class RadioStationGenreListWindow
  extends ModalWindow
{
  private ScrollPane _scrollPaneRadioStationGenreList = new ScrollPane();
  private DefaultListModel _listRadioStationGenreModel = new DefaultListModel();
  private JList _listRadioStationGenreList = new JList(_listRadioStationGenreModel);
  private ItemVO[] _itemVOs;
  private String _category = null;

  public RadioStationGenreListWindow()
  {
    this(null, null);
  }

  public RadioStationGenreListWindow(String category)
  {
    this(category, null);
  }

  public RadioStationGenreListWindow(String category, String url)
  {
    super();
    _category = category;
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
    setTitle((_category != null ? _category + " : " : "") + "Please select a genre.");
    _scrollPaneRadioStationGenreList.setBounds(15, 10, 530, 237);
    _listRadioStationGenreList.setOpaque(false);
    _listRadioStationGenreList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _listRadioStationGenreList.setCellRenderer(new RadioStationGenreListRenderer());
    _listRadioStationGenreList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent listSelectionEvent)
      {
        _listRadioStationGenreList_valueChanged(listSelectionEvent);
      }
    });
    _listRadioStationGenreList.setVisibleRowCount(JLIST_VISIBLE_ROW_COUNT);
    _scrollPaneRadioStationGenreList.addToViewport(_listRadioStationGenreList);
    _scrollPaneRadioStationGenreList.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollRadioStationGenreList_scrollUp(actionEvent);
      }
    });
    _scrollPaneRadioStationGenreList.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollRadioStationGenreList_scrollDown(actionEvent);
      }
    });
    _panelContent.add(_scrollPaneRadioStationGenreList, null);
    try
      {
        _itemVOs = API.getInstance().getMedia().list(_category);
        for (int index = 0; index < _itemVOs.length; index++)
          _listRadioStationGenreModel.addElement(_itemVOs[index]);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  protected void windowVisible()
  {
    setVoiceCommandState(VoiceCommandState.CONFIRM_GENRE_LIST);
    setVoiceCommandContextState(new VoiceCommandContextState[]
        { VoiceCommandContextState.PAGE_SELECTION });
    if (_itemVOs != null)
      for (int index = 0; index < _itemVOs.length; index++)
        addContextData(new ContextDataVO(_itemVOs[index].getName(), "" + (index + 1)));
    _listRadioStationGenreList.clearSelection();
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

  private void _listRadioStationGenreList_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    if (!listSelectionEvent.getValueIsAdjusting())
      {
        ((MapViewPanel) getParent()).showWindow(new RadioStationListWindow(((ItemVO) _listRadioStationGenreList.getSelectedValue()).getName()));
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
            _scrollRadioStationGenreList_scrollDown(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.PREVIOUS_PAGE)
          {
            _scrollRadioStationGenreList_scrollUp(null);
            return (true);
          }
        else
          return (false);
        _listRadioStationGenreList.setSelectedIndex(Integer.parseInt(voiceCommandVariableVO.getValueAsString()) - 1);
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

  private void _scrollRadioStationGenreList_scrollUp(ActionEvent actionEvent)
  {
    if (_listRadioStationGenreList.getFirstVisibleIndex() - _listRadioStationGenreList.getVisibleRowCount() >= 0)
      _listRadioStationGenreList.ensureIndexIsVisible(_listRadioStationGenreList.getFirstVisibleIndex() - _listRadioStationGenreList.getVisibleRowCount());
    else
      _listRadioStationGenreList.ensureIndexIsVisible(0);
  }

  private void _scrollRadioStationGenreList_scrollDown(ActionEvent actionEvent)
  {
    if (_listRadioStationGenreList.getLastVisibleIndex() + _listRadioStationGenreList.getVisibleRowCount() < _listRadioStationGenreList.getModel().getSize())
      _listRadioStationGenreList.ensureIndexIsVisible(_listRadioStationGenreList.getLastVisibleIndex() + _listRadioStationGenreList.getVisibleRowCount());
    else
      _listRadioStationGenreList.ensureIndexIsVisible(_listRadioStationGenreList.getModel().getSize());
  }
}
