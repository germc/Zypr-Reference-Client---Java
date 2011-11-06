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
import net.zypr.api.Session;
import net.zypr.api.enums.ItemType;
import net.zypr.api.enums.VoiceCommandAction;
import net.zypr.api.enums.VoiceCommandContextState;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.vo.ContextDataVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.api.vo.VoiceCommandVariableVO;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.renderers.FavoritesListRenderer;
import net.zypr.gui.utils.Debug;

public class FavoritesListWindow
  extends ModalWindow
{
  private ScrollPane _scrollPaneFavoritesList = new ScrollPane();
  private DefaultListModel _listFavoritesModel = new DefaultListModel();
  private JList _listFavoritesList = new JList(_listFavoritesModel);

  public FavoritesListWindow()
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
    setTitle("Favorites");
    _scrollPaneFavoritesList.setBounds(15, 10, 530, 237);
    _listFavoritesList.setOpaque(false);
    _listFavoritesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _listFavoritesList.setCellRenderer(new FavoritesListRenderer());
    _listFavoritesList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent listSelectionEvent)
      {
        _listFavoritesList_valueChanged(listSelectionEvent);
      }
    });
    _listFavoritesList.setVisibleRowCount(JLIST_VISIBLE_ROW_COUNT);
    _scrollPaneFavoritesList.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneFavoritesList_scrollUp(actionEvent);
      }
    });
    _scrollPaneFavoritesList.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneFavoritesList_scrollDown(actionEvent);
      }
    });
    _scrollPaneFavoritesList.addToViewport(_listFavoritesList);
    ItemVO[] itemVOs = Session.getInstance().getFavorites();
    for (int index = 0; index < itemVOs.length; index++)
      _listFavoritesModel.addElement(itemVOs[index]);
    _panelContent.add(_scrollPaneFavoritesList, null);
  }

  protected void windowVisible()
  {
    ItemVO selecteItemVO = (ItemVO) _listFavoritesList.getSelectedValue();
    if (selecteItemVO != null)
      if (!Session.getInstance().isInFavorites(selecteItemVO))
        _listFavoritesModel.remove(_listFavoritesList.getSelectedIndex());
    _listFavoritesList.clearSelection();
    setVoiceCommandState(VoiceCommandState.CONFIRM_LIST);
    setVoiceCommandContextState(new VoiceCommandContextState[]
        { VoiceCommandContextState.PAGE_SELECTION });
    for (int index = 0; index < Session.getInstance().getFavorites().length; index++)
      addContextData(new ContextDataVO(Session.getInstance().getFavorites()[index].getName(), "" + (index + 1)));
  }

  private void _listFavoritesList_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    if (!listSelectionEvent.getValueIsAdjusting())
      {
        final ItemVO itemVO = (ItemVO) _listFavoritesList.getSelectedValue();
        if (itemVO != null)
          if (itemVO.getType() == ItemType.RADIO)
            {
              getParentPanel().showWindow(new RadioStationDetailsWindow(itemVO, true));
            }
          else if (itemVO.getType() == ItemType.POI)
            {
              getParentPanel().showWindow(new POIDetailsWindow(itemVO, true));
            }
      }
  }

  public boolean removeFavorite(int index)
  {
    try
      {
        API.getInstance().getUser().favoriteDelete(((ItemVO) _listFavoritesModel.get(index)).getGlobalItemID());
        _listFavoritesList.remove(index);
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
        else if (voiceCommandVO.getName() == VoiceCommandAction.DELETE_FAVORITE_4_ITEM_NUMBER)
          {
            voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_number");
            return (removeFavorite(Integer.parseInt(voiceCommandVariableVO.getValueAsString()) - 1));
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.DELETE_FAVORITE_4_ITEM_NAME)
          {
            voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_id");
            return (removeFavorite(Integer.parseInt(voiceCommandVariableVO.getValueAsString()) - 1));
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.NEXT_PAGE)
          {
            _scrollPaneFavoritesList_scrollDown(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.PREVIOUS_PAGE)
          {
            _scrollPaneFavoritesList_scrollUp(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.NBEST_REJECTION)
          {
            _buttonWindowClose_actionPerformed(null);
            return (true);
          }
        else
          return (false);
        _listFavoritesList.setSelectedIndex(Integer.parseInt(voiceCommandVariableVO.getValueAsString()) - 1);
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

  private void _scrollPaneFavoritesList_scrollUp(ActionEvent actionEvent)
  {
    if (_listFavoritesList.getFirstVisibleIndex() - _listFavoritesList.getVisibleRowCount() >= 0)
      _listFavoritesList.ensureIndexIsVisible(_listFavoritesList.getFirstVisibleIndex() - _listFavoritesList.getVisibleRowCount());
    else
      _listFavoritesList.ensureIndexIsVisible(0);
  }

  private void _scrollPaneFavoritesList_scrollDown(ActionEvent actionEvent)
  {
    if (_listFavoritesList.getLastVisibleIndex() + _listFavoritesList.getVisibleRowCount() < _listFavoritesList.getModel().getSize())
      _listFavoritesList.ensureIndexIsVisible(_listFavoritesList.getLastVisibleIndex() + _listFavoritesList.getVisibleRowCount());
    else
      _listFavoritesList.ensureIndexIsVisible(_listFavoritesList.getModel().getSize());
  }
}
