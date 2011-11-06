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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.zypr.api.Session;
import net.zypr.api.enums.MessageType;
import net.zypr.api.enums.VoiceCommandAction;
import net.zypr.api.enums.VoiceCommandContextState;
import net.zypr.api.vo.ContextDataVO;
import net.zypr.api.vo.InfoMessageGetTypeVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.api.vo.VoiceCommandVariableVO;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.renderers.MessageListRenderer;
import net.zypr.gui.utils.Debug;

public class MessageListWindow
  extends ModalWindow
{
  private ScrollPane _scrollPaneMessageList = new ScrollPane();
  private DefaultListModel _listMessageModel = new DefaultListModel();
  private JList _listMessageList = new JList(_listMessageModel);
  private PropertyChangeListener _sessionPropertyChangeListener = new PropertyChangeListener()
  {
    public void propertyChange(PropertyChangeEvent propertyChangeEvent)
    {
      if (propertyChangeEvent.getPropertyName().equals("Messages"))
        {
          ItemVO[] newMessagesItemVO = (ItemVO[]) propertyChangeEvent.getNewValue();
          for (int index = 0; index < newMessagesItemVO.length; index++)
            _listMessageModel.add(0, newMessagesItemVO[index]);
          windowVisible();
        }
    }
  };

  public MessageListWindow()
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
    setTitle("Messages");
    _scrollPaneMessageList.setBounds(15, 10, 530, 237);
    _listMessageList.setOpaque(false);
    _listMessageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _listMessageList.setCellRenderer(new MessageListRenderer());
    _listMessageList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent listSelectionEvent)
      {
        _listMessageList_valueChanged(listSelectionEvent);
      }
    });
    _listMessageList.setVisibleRowCount(JLIST_VISIBLE_ROW_COUNT);
    _scrollPaneMessageList.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneMessageList_scrollUp(actionEvent);
      }
    });
    _scrollPaneMessageList.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneMessageList_scrollDown(actionEvent);
      }
    });
    _scrollPaneMessageList.addToViewport(_listMessageList);
    ItemVO[] itemVOs = Session.getInstance().getMessages();
    for (int index = 0; index < itemVOs.length; index++)
      _listMessageModel.addElement(itemVOs[index]);
    _panelContent.add(_scrollPaneMessageList, null);
    Session.getInstance().addPropertyChangeListener(_sessionPropertyChangeListener);
  }

  protected void windowVisible()
  {
    int selectedIndex = _listMessageList.getSelectedIndex();
    if (selectedIndex != -1)
      {
        ItemVO selecteItemVO = (ItemVO) _listMessageList.getSelectedValue();
        ItemVO messageItemVO = Session.getInstance().getMessage(selectedIndex);
        if (!messageItemVO.getGlobalItemID().equals(selecteItemVO.getGlobalItemID()))
          _listMessageModel.remove(selectedIndex);
      }
    _listMessageList.clearSelection();
    setVoiceCommandContextState(new VoiceCommandContextState[]
        { VoiceCommandContextState.MESSAGE_LIST, VoiceCommandContextState.PAGE_SELECTION });
    for (int index = 0; index < Session.getInstance().getMessages().length; index++)
      addContextData(new ContextDataVO(Session.getInstance().getMessages()[index].getName(), "" + (index + 1)));
  }

  private void _listMessageList_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    if (!listSelectionEvent.getValueIsAdjusting())
      {
        final ItemVO itemVO = (ItemVO) _listMessageList.getSelectedValue();
        if (itemVO != null)
          {
            InfoMessageGetTypeVO infoVO = (InfoMessageGetTypeVO) itemVO.getInfo();
            if (infoVO.getMessageType() == MessageType.TEXT)
              getParentPanel().showWindow(new MessageOpenWindow(itemVO));
            else if (infoVO.getMessageType() == MessageType.VOICE)
              getParentPanel().showWindow(new VoiceNoteOpenWindow(itemVO));
          }
      }
  }

  public boolean processVoiceCommand(VoiceCommandVO voiceCommandVO)
  {
    try
      {
        VoiceCommandVariableVO voiceCommandVariableVO = null;
        if (voiceCommandVO.getName() == VoiceCommandAction.ITEM_SELECTED_4_ITEM_NUMBER || voiceCommandVO.getName() == VoiceCommandAction.OPEN_MESSAGE_BY_NUMBER)
          {
            voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_number");
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.ITEM_SELECTED_4_ITEM_NAME || voiceCommandVO.getName() == VoiceCommandAction.OPEN_MESSAGE_BY_NUMBER)
          {
            voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_id");
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.NEXT_PAGE)
          {
            _scrollPaneMessageList_scrollDown(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.PREVIOUS_PAGE)
          {
            _scrollPaneMessageList_scrollUp(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.NBEST_REJECTION)
          {
            _buttonWindowClose_actionPerformed(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.OPEN_MESSAGE_FROM_CONTACT_NAME || voiceCommandVO.getName() == VoiceCommandAction.OPEN_MESSAGE_FROM_CONTACT_NUMBER)
          {
            String contactName = voiceCommandVO.getVariable("contact").getValueAsString();
            int offset = 1;
            if (voiceCommandVO.getName() == VoiceCommandAction.OPEN_MESSAGE_FROM_CONTACT_NUMBER)
              offset = Integer.parseInt(voiceCommandVO.getVariable("list_item_number").getValueAsString());
            int count = 0;
            for (int index = 0; index < _listMessageList.getModel().getSize(); index++)
              {
                ItemVO itemVO = (ItemVO) _listMessageModel.get(index);
                InfoMessageGetTypeVO infoVO = (InfoMessageGetTypeVO) itemVO.getInfo();
                if (infoVO.getFrom().equalsIgnoreCase(contactName))
                  count++;
                if (count == offset)
                  {
                    _listMessageList.setSelectedIndex(index);
                    return (true);
                  }
              }
            return (false);
          }
        else
          return (false);
        _listMessageList.setSelectedIndex(Integer.parseInt(voiceCommandVariableVO.getValueAsString()) - 1);
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
    Session.getInstance().removePropertyChangeListener(_sessionPropertyChangeListener);
    getParentPanel().discardWindow();
  }

  private void _scrollPaneMessageList_scrollUp(ActionEvent actionEvent)
  {
    if (_listMessageList.getFirstVisibleIndex() - _listMessageList.getVisibleRowCount() >= 0)
      _listMessageList.ensureIndexIsVisible(_listMessageList.getFirstVisibleIndex() - _listMessageList.getVisibleRowCount());
    else
      _listMessageList.ensureIndexIsVisible(0);
  }

  private void _scrollPaneMessageList_scrollDown(ActionEvent actionEvent)
  {
    if (_listMessageList.getLastVisibleIndex() + _listMessageList.getVisibleRowCount() < _listMessageList.getModel().getSize())
      _listMessageList.ensureIndexIsVisible(_listMessageList.getLastVisibleIndex() + _listMessageList.getVisibleRowCount());
    else
      _listMessageList.ensureIndexIsVisible(_listMessageList.getModel().getSize());
  }
}
