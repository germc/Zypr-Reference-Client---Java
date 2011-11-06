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
import net.zypr.api.enums.MessageType;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.vo.ItemVO;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.renderers.ContactListRenderer;
import net.zypr.gui.utils.Debug;

public class SendToContactListWindow
  extends ModalWindow
{
  private ScrollPane _scrollPaneContactList = new ScrollPane();
  private JList _listContactList = new JList(Session.getInstance().getFriendList());
  private ItemVO _itemVO;
  private MessageType _messageType;

  public SendToContactListWindow(ItemVO itemVO, MessageType messageType)
  {
    super();
    try
      {
        _itemVO = itemVO;
        _messageType = messageType;
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
    setTitle("Please select a contact to send");
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

  private void _listContactList_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    try
      {
        if (!listSelectionEvent.getValueIsAdjusting())
          {
            ItemVO contactItemVO = (ItemVO) _listContactList.getSelectedValue();
            if (API.getInstance().getSocial().canMessageSend(contactItemVO.getService()))
              {
                if (_messageType == MessageType.TEXT)
                  getParentPanel().discardAndShowWindow(new MessageSendWindow((ItemVO) _listContactList.getSelectedValue(), _itemVO, null));
                else if (_messageType == MessageType.VOICE)
                  getParentPanel().discardAndShowWindow(new VoiceNoteSendWindow((ItemVO) _listContactList.getSelectedValue(), _itemVO, null));
              }
            else
              {
                getParentPanel().showWindow(new WarningWindow("Cannot send message", "The service \"" + contactItemVO.getService() + "\" does not let you send private messages to \"" + contactItemVO.getName() + "\". Please select another contact.", 30, "send-message-contact-list-can-not-send.wav"));
                _listContactList.clearSelection();
              }
          }
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
