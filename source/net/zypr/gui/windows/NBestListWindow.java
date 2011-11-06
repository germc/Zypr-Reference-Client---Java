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

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.zypr.api.API;
import net.zypr.api.enums.VoiceCommandAction;
import net.zypr.api.enums.VoiceCommandContextState;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.vo.ContextDataVO;
import net.zypr.api.vo.VoiceCommandResponseVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.api.vo.VoiceCommandVariableVO;
import net.zypr.gui.Settings;
import net.zypr.gui.VoiceCommand;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.panels.MapViewPanel;
import net.zypr.gui.renderers.NBestListRenderer;
import net.zypr.gui.utils.Debug;
import net.zypr.mmp.mplayer.TTSPlayer;

public class NBestListWindow
  extends ModalWindow
{
  private ScrollPane _scrollPaneNBestList = new ScrollPane();
  private JList _listNBestList = null;
  private VoiceCommandResponseVO _voiceCommandResponseVO;

  public NBestListWindow(VoiceCommandResponseVO voiceCommandResponseVO)
  {
    super();
    _voiceCommandResponseVO = voiceCommandResponseVO;
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
    _listNBestList = new JList(_voiceCommandResponseVO.getVoiceCommandVOs());
    this.setSize(new Dimension(560, 293));
    setTitle(_voiceCommandResponseVO.getVariable("nbest_question_text").getValueAsString());
    _scrollPaneNBestList.setBounds(15, 10, 530, 237);
    _listNBestList.setOpaque(false);
    _listNBestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _listNBestList.setCellRenderer(new NBestListRenderer());
    _listNBestList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent listSelectionEvent)
      {
        _listNBestList_valueChanged(listSelectionEvent);
      }
    });
    _listNBestList.setVisibleRowCount(JLIST_VISIBLE_ROW_COUNT);
    _scrollPaneNBestList.addToViewport(_listNBestList);
    _scrollPaneNBestList.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneNBestList_scrollUp(actionEvent);
      }
    });
    _scrollPaneNBestList.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneNBestList_scrollDown(actionEvent);
      }
    });
    _panelContent.add(_scrollPaneNBestList, null);
  }

  protected void windowVisible()
  {
    setVoiceCommandState(VoiceCommandState.CONFIRM_LIST);
    setVoiceCommandContextState(new VoiceCommandContextState[]
        { VoiceCommandContextState.PAGE_SELECTION });
    for (int index = 0; index < _voiceCommandResponseVO.getVoiceCommandVOs().length; index++)
      addContextData(new ContextDataVO(_voiceCommandResponseVO.getVoiceCommandVOs()[index].getFollowupText(), "" + (index + 1)));
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

  private void _listNBestList_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    if (!listSelectionEvent.getValueIsAdjusting())
      {
        MapViewPanel mapViewPanel = (MapViewPanel) getParentPanel();
        mapViewPanel.discardWindow();
        VoiceCommandVO[] voiceCommandVOs = new VoiceCommandVO[1];
        voiceCommandVOs[0] = (VoiceCommandVO) _listNBestList.getSelectedValue();
        VoiceCommandResponseVO voiceCommandResponseVO = new VoiceCommandResponseVO(_voiceCommandResponseVO.getVoiceCommandVariableVOs(), voiceCommandVOs);
        try
          {
            VoiceCommand.Process(mapViewPanel, voiceCommandResponseVO);
          }
        catch (APICommunicationException apiCommunicationException)
          {
            Debug.displayStack(this, apiCommunicationException);
          }
        catch (APIProtocolException apiProtocolException)
          {
            Debug.displayStack(this, apiProtocolException);
          }
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
            _scrollPaneNBestList_scrollDown(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.PREVIOUS_PAGE)
          {
            _scrollPaneNBestList_scrollUp(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.NBEST_REJECTION)
          {
            _buttonWindowClose_actionPerformed(null);
            return (true);
          }
        else
          return (false);
        _listNBestList.setSelectedIndex(Integer.parseInt(voiceCommandVariableVO.getValueAsString()) - 1);
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

  private void _scrollPaneNBestList_scrollUp(ActionEvent actionEvent)
  {
    if (_listNBestList.getFirstVisibleIndex() - _listNBestList.getVisibleRowCount() >= 0)
      _listNBestList.ensureIndexIsVisible(_listNBestList.getFirstVisibleIndex() - _listNBestList.getVisibleRowCount());
    else
      _listNBestList.ensureIndexIsVisible(0);
  }

  private void _scrollPaneNBestList_scrollDown(ActionEvent actionEvent)
  {
    if (_listNBestList.getLastVisibleIndex() + _listNBestList.getVisibleRowCount() < _listNBestList.getModel().getSize())
      _listNBestList.ensureIndexIsVisible(_listNBestList.getLastVisibleIndex() + _listNBestList.getVisibleRowCount());
    else
      _listNBestList.ensureIndexIsVisible(_listNBestList.getModel().getSize());
  }
}
