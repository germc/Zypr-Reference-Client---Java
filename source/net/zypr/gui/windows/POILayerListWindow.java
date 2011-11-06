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
import net.zypr.api.enums.ItemType;
import net.zypr.api.enums.VoiceCommandAction;
import net.zypr.api.enums.VoiceCommandContextState;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.api.vo.VoiceCommandVariableVO;
import net.zypr.gui.Settings;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.panels.MapViewPanel;
import net.zypr.gui.renderers.POIListRenderer;
import net.zypr.gui.utils.Debug;
import net.zypr.mmp.mplayer.TTSPlayer;

public class POILayerListWindow
  extends ModalWindow
{
  private ScrollPane _scrollPanePOIList = new ScrollPane();
  private DefaultListModel _listPOIModel = new DefaultListModel();
  private JList _listPOIList = new JList(_listPOIModel);

  public POILayerListWindow()
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
    this.setSize(new Dimension(560, 293));
    setTitle("Point Of Interest");
    _scrollPanePOIList.setBounds(15, 10, 530, 237);
    _listPOIList.setOpaque(false);
    _listPOIList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _listPOIList.setCellRenderer(new POIListRenderer());
    _listPOIList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent listSelectionEvent)
      {
        _listPOIList_valueChanged(listSelectionEvent);
      }
    });
    _listPOIList.setVisibleRowCount(JLIST_VISIBLE_ROW_COUNT);
    _scrollPanePOIList.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPanePOIList_scrollUp(actionEvent);
      }
    });
    _scrollPanePOIList.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPanePOIList_scrollDown(actionEvent);
      }
    });
    _scrollPanePOIList.addToViewport(_listPOIList);
    /*-
    for (int index = 0; index < _itemVOs.length; index++)
      _listPOIModel.addElement(_itemVOs[index]);
    -*/
    _panelContent.add(_scrollPanePOIList, null);
  }

  protected void windowVisible()
  {
    setVoiceCommandState(VoiceCommandState.CONFIRM_LIST);
    setVoiceCommandContextState(new VoiceCommandContextState[]
        { VoiceCommandContextState.PAGE_SELECTION });
    /*-
    for (int index = 0; index < _itemVOs.length; index++)
      addContextData(new ContextDataVO(_itemVOs[index].getName(), "" + (index + 1)));
    -*/
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

  private void _listPOIList_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    if (!listSelectionEvent.getValueIsAdjusting())
      {
        final ItemVO itemVO = (ItemVO) _listPOIList.getSelectedValue();
        if (itemVO != null && itemVO.getType() == ItemType.POI)
          {
            ((MapViewPanel) getParentPanel()).setMarkerSelected(itemVO.getGlobalItemID());
            getParentPanel().discardAndShowWindow(new POIDetailsWindow(itemVO, true));
          }
      }
  }

  public boolean removeFavorite(int index)
  {
    try
      {
        API.getInstance().getUser().favoriteDelete(((ItemVO) _listPOIModel.get(index)).getGlobalItemID());
        _listPOIList.remove(index);
        return (true);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    return (false);
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
            _scrollPanePOIList_scrollDown(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.PREVIOUS_PAGE)
          {
            _scrollPanePOIList_scrollUp(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.NBEST_REJECTION)
          {
            _buttonWindowClose_actionPerformed(null);
            return (true);
          }
        else
          return (false);
        _listPOIList.setSelectedIndex(Integer.parseInt(voiceCommandVariableVO.getValueAsString()) - 1);
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

  private void _scrollPanePOIList_scrollUp(ActionEvent actionEvent)
  {
    if (_listPOIList.getFirstVisibleIndex() - _listPOIList.getVisibleRowCount() >= 0)
      _listPOIList.ensureIndexIsVisible(_listPOIList.getFirstVisibleIndex() - _listPOIList.getVisibleRowCount());
    else
      _listPOIList.ensureIndexIsVisible(0);
  }

  private void _scrollPanePOIList_scrollDown(ActionEvent actionEvent)
  {
    if (_listPOIList.getLastVisibleIndex() + _listPOIList.getVisibleRowCount() < _listPOIList.getModel().getSize())
      _listPOIList.ensureIndexIsVisible(_listPOIList.getLastVisibleIndex() + _listPOIList.getVisibleRowCount());
    else
      _listPOIList.ensureIndexIsVisible(_listPOIList.getModel().getSize());
  }
}
