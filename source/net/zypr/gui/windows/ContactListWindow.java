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
import net.zypr.api.Session;
import net.zypr.api.enums.VoiceCommandAction;
import net.zypr.api.enums.VoiceCommandContextState;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.vo.ContextDataVO;
import net.zypr.api.vo.InfoContactTypeVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.api.vo.VoiceCommandVariableVO;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.renderers.ContactListRenderer;
import net.zypr.gui.utils.Debug;

public class ContactListWindow
  extends ModalWindow
{
  private ScrollPane _scrollPaneContactList = new ScrollPane();
  private JList _listContactList = new JList(Session.getInstance().getFriendList());

  public ContactListWindow()
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
    setTitle("Contacts");
    _scrollPaneContactList.setBounds(15, 10, 530, 237);
    _listContactList.setOpaque(false);
    _listContactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _listContactList.setCellRenderer(new ContactListRenderer());
    _listContactList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent listSelectionEvent)
      {
        _listContactList_valueChanged(listSelectionEvent);
      }
    });
    _listContactList.setVisibleRowCount(JLIST_VISIBLE_ROW_COUNT);
    _scrollPaneContactList.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneContactList_scrollUp(actionEvent);
      }
    });
    _scrollPaneContactList.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneContactList_scrollDown(actionEvent);
      }
    });
    _scrollPaneContactList.addToViewport(_listContactList);
    _panelContent.add(_scrollPaneContactList, null);
  }

  protected void windowVisible()
  {
    setVoiceCommandState(VoiceCommandState.CONFIRM_LIST);
    setVoiceCommandContextState(new VoiceCommandContextState[]
        { VoiceCommandContextState.PAGE_SELECTION });
    for (int index = 0; index < Session.getInstance().getFriendList().length; index++)
      addContextData(new ContextDataVO(Session.getInstance().getFriendList()[index].getName(), "" + (index + 1)));
  }

  private void _listContactList_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    if (!listSelectionEvent.getValueIsAdjusting())
      {
        _listContactList.setEnabled(false);
        final ItemVO itemVO = (ItemVO) _listContactList.getSelectedValue();
        new Thread(new Runnable()
        {
          public void run()
          {
            try
              {
                getParentPanel().showWindow(new ContactDetailsWindow(itemVO, (InfoContactTypeVO) API.getInstance().getInfoVO(itemVO.getAction("details").getHandlerURI()), true));
              }
            catch (Exception exception)
              {
                Debug.displayStack(this, exception);
              }
            finally
              {
                _listContactList.setEnabled(true);
                _listContactList.clearSelection();
              }
          }
        }).start();
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
            _scrollPaneContactList_scrollDown(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.PREVIOUS_PAGE)
          {
            _scrollPaneContactList_scrollUp(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.NBEST_REJECTION)
          {
            _buttonWindowClose_actionPerformed(null);
            return (true);
          }
        else
          return (false);
        _listContactList.setSelectedIndex(Integer.parseInt(voiceCommandVariableVO.getValueAsString()) - 1);
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

  private void _scrollPaneContactList_scrollUp(ActionEvent actionEvent)
  {
    if (_listContactList.getFirstVisibleIndex() - _listContactList.getVisibleRowCount() >= 0)
      _listContactList.ensureIndexIsVisible(_listContactList.getFirstVisibleIndex() - _listContactList.getVisibleRowCount());
    else
      _listContactList.ensureIndexIsVisible(0);
  }

  private void _scrollPaneContactList_scrollDown(ActionEvent actionEvent)
  {
    if (_listContactList.getLastVisibleIndex() + _listContactList.getVisibleRowCount() < _listContactList.getModel().getSize())
      _listContactList.ensureIndexIsVisible(_listContactList.getLastVisibleIndex() + _listContactList.getVisibleRowCount());
    else
      _listContactList.ensureIndexIsVisible(_listContactList.getModel().getSize());
  }
}
