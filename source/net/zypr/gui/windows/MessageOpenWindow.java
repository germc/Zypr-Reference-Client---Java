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
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.text.Format;
import java.text.SimpleDateFormat;

import net.zypr.api.API;
import net.zypr.api.Session;
import net.zypr.api.enums.ItemType;
import net.zypr.api.enums.MessageType;
import net.zypr.api.enums.VoiceCommandAction;
import net.zypr.api.enums.VoiceCommandContextState;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.vo.InfoContactTypeVO;
import net.zypr.api.vo.InfoMessageGetTypeVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.gui.ImageFetcher;
import net.zypr.gui.Settings;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.PressAndHoldButton;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.components.TextPane;
import net.zypr.gui.utils.Debug;
import net.zypr.mmp.mplayer.MPlayer;
import net.zypr.mmp.mplayer.TTSPlayer;

public class MessageOpenWindow
  extends ModalWindow
{
  private ImagePanel _imagePanelContactPicture = new ImagePanel("placeholder-picture-contact-96.png");
  private ImagePanel _imagePanelAttachmentPicture = new ImagePanel("placeholder-picture-attachment-96.png");
  private Button _buttonReply = new Button("button-message-reply-up.png", "button-message-reply-down.png");
  private Button _buttonContactDetails = new Button("button-message-contact-details-up.png", "button-message-contact-details-down.png");
  private PressAndHoldButton _buttonDelete = new PressAndHoldButton("button-message-delete-up.png", "button-message-delete-down.png");
  private Button _buttonOpenAttachment = new Button("button-message-open-attachment-up.png", "button-message-open-attachment-down.png", "button-message-open-attachment-disabled.png");
  private Button _buttonPrevious = new Button("button-message-previous-up.png", "button-message-previous-down.png", "button-message-previous-disabled.png");
  private Button _buttonNext = new Button("button-message-next-up.png", "button-message-next-down.png", "button-message-next-disabled.png");
  private Label _labelMessageFrom = new Label();
  private Label _labelMessageSentDate = new Label();
  private ScrollPane _scrollPaneMessageText = new ScrollPane();
  private TextPane _textPaneMessageText = new TextPane();
  private ItemVO _itemVO = null;
  private InfoMessageGetTypeVO _infoVO = null;
  private int _currentMessageIndex = -1;
  private PropertyChangeListener _sessionPropertyChangeListener = new PropertyChangeListener()
  {
    public void propertyChange(PropertyChangeEvent propertyChangeEvent)
    {
      if (propertyChangeEvent.getPropertyName().equals("Messages"))
        {
          _buttonNext.setEnabled(_currentMessageIndex + 1 < Session.getInstance().messageCount());
        }
    }
  };

  public MessageOpenWindow(ItemVO itemVO)
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
    this.setSize(new Dimension(560, 293));
    /*- Fix it
    setDataSourceLogo(Resources.getInstance().getImageIcon(Session.getInstance().getServiceDetails(_itemVO.getService().toLowerCase()).getServiceIconURI()));
    setDataSourceURL("http://www." + _itemVO.getService().toLowerCase() + ".com/");
    -*/
    setTitle("Text Message");
    _imagePanelContactPicture.setLocation(15, 15);
    _imagePanelAttachmentPicture.setLocation(445, 15);
    _buttonReply.setLocation(140, 70);
    _buttonReply.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonReply_actionPerformed(actionEvent);
      }
    });
    _buttonContactDetails.setLocation(220, 70);
    _buttonContactDetails.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonContactDetails_actionPerformed(actionEvent);
      }
    });
    _buttonDelete.setLocation(300, 70);
    _buttonDelete.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonDelete_actionPerformed(actionEvent);
      }
    });
    _buttonOpenAttachment.setLocation(380, 70);
    _buttonOpenAttachment.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonOpenAttachment_actionPerformed(actionEvent);
      }
    });
    _buttonNext.setLocation(325, 198);
    _buttonNext.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonNext_actionPerformed(actionEvent);
      }
    });
    _buttonPrevious.setLocation(180, 198);
    _buttonPrevious.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPrevious_actionPerformed(actionEvent);
      }
    });
    _labelMessageFrom.setBounds(140, 15, 455, 20);
    _labelMessageSentDate.setBounds(140, 40, 160, 20);
    _scrollPaneMessageText.setBounds(15, 130, 525, 58);
    _scrollPaneMessageText.addToViewport(_textPaneMessageText);
    _textPaneMessageText.setEditable(false);
    _panelContent.add(_labelMessageSentDate, null);
    _panelContent.add(_labelMessageFrom, null);
    _panelContent.add(_buttonOpenAttachment, null);
    _panelContent.add(_buttonDelete, null);
    _panelContent.add(_buttonContactDetails, null);
    _panelContent.add(_buttonReply, null);
    _panelContent.add(_buttonNext, null);
    _panelContent.add(_buttonPrevious, null);
    _panelContent.add(_imagePanelContactPicture, null);
    _panelContent.add(_imagePanelAttachmentPicture, null);
    _panelContent.add(_scrollPaneMessageText, null);
    _labelMessageFrom.setText(_itemVO.getName());
    BufferedImage bufferedImage = ImageFetcher.getInstance().getImage(_itemVO.getIconURL(), 96, 96, null);
    if (bufferedImage != null)
      _imagePanelContactPicture.setImage(bufferedImage);
    _infoVO = (InfoMessageGetTypeVO) _itemVO.getInfo();
    Format formatterDate = new SimpleDateFormat(Settings.getInstance().getDTO().getDateFormatPattern().getPattern());
    Format formatterTime = new SimpleDateFormat(Settings.getInstance().getDTO().getTimeFormatPattern().getPattern());
    _labelMessageSentDate.setText(formatterDate.format(_infoVO.getDateSent()).toString() + " " + formatterTime.format(_infoVO.getDateSent()).toString());
    _labelMessageSentDate.setBounds(new Rectangle(140, 40, 455, 20));
    _textPaneMessageText.setText(_itemVO.getDescription().trim());
    if (_itemVO.getAttachments() != null && _itemVO.getAttachments().length != 0)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_itemVO.getAttachments()[0].getIconURL(), 96, 96, null);
        if (bufferedImage != null)
          _imagePanelAttachmentPicture.setImage(bufferedImage);
      }
    if (Settings.getInstance().getDTO().isTTSMessageText())
      new Thread(new Runnable()
      {
        public void run()
        {
          try
            {
              TTSPlayer.getInstance().loadPlayfile(API.getInstance().getVoice().tts(_itemVO.getDescription().trim()).getAudioURI());
            }
          catch (Exception exception)
            {
              Debug.displayStack(this, exception);
            }
        }
      }).start();
    _currentMessageIndex = Session.getInstance().getMessageIndex(_itemVO.getGlobalItemID());
    Session.getInstance().addPropertyChangeListener(_sessionPropertyChangeListener);
  }

  protected void windowVisible()
  {
    setVoiceCommandState(VoiceCommandState.MESSAGE_OPEN);
    if (_itemVO.getAttachments() != null && _itemVO.getAttachments().length != 0)
      {
        setVoiceCommandContextState(new VoiceCommandContextState[]
            { VoiceCommandContextState.ATTACHMENT_AVAILABLE });
        _buttonOpenAttachment.setEnabled(true);
      }
    else
      {
        _buttonOpenAttachment.setEnabled(false);
      }
    _buttonPrevious.setEnabled(_currentMessageIndex > 0);
    _buttonNext.setEnabled(_currentMessageIndex + 1 < Session.getInstance().messageCount());
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    Session.getInstance().removePropertyChangeListener(_sessionPropertyChangeListener);
    getParentPanel().discardWindow();
  }

  private void _buttonReply_actionPerformed(ActionEvent actionEvent)
  {
    /*-
    ((MapViewPanel)getParentPanel()).showWindow(new MessageSendWindow(_itemVO, null, null));
    -*/
  }

  private void _buttonContactDetails_actionPerformed(ActionEvent actionEvent)
  {
    /*- Implement it -*/
    Debug.print(actionEvent);
  }

  private void _buttonDelete_actionPerformed(ActionEvent actionEvent)
  {
    /*- Implement it -*/
    Debug.print(actionEvent);
  }

  private void _buttonOpenAttachment_actionPerformed(ActionEvent actionEvent)
  {
    final ItemVO attachmentItemVO = _itemVO.getAttachments()[0];
    if (attachmentItemVO != null)
      {
        if (attachmentItemVO.getType() == ItemType.USER)
          {
            _buttonOpenAttachment.setEnabled(false);
            new Thread(new Runnable()
            {
              public void run()
              {
                try
                  {
                    getParentPanel().showWindow(new ContactDetailsWindow(attachmentItemVO, (InfoContactTypeVO) API.getInstance().getInfoVO(attachmentItemVO.getAction("details").getHandlerURI()), false));
                  }
                catch (Exception exception)
                  {
                    Debug.displayStack(this, exception);
                  }
                finally
                  {
                    _buttonOpenAttachment.setEnabled(true);
                  }
              }
            }).start();
          }
        else if (attachmentItemVO.getType() == ItemType.RADIO)
          {
            MPlayer.getInstance().playRadioStationItem(attachmentItemVO);
            /*-
            getParentPanel().showWindow(new RadioStationDetailsWindow(attachmentItemVO, false));
            -*/
          }
        else if (attachmentItemVO.getType() == ItemType.POI)
          {
            getParentPanel().showWindow(new POIDetailsWindow(attachmentItemVO, false));
          }
        else
          {
            getParentPanel().showWindow(new WarningWindow("Unable to open attachment", "Unable to open attachment. Unknown attachment type", 30, "send-message-unknown-attachment-type.wav"));
          }
      }
  }

  public boolean processVoiceCommand(VoiceCommandVO voiceCommandVO)
  {
    try
      {
        if (voiceCommandVO.getName() == VoiceCommandAction.SHOW_ATTACHMENT && _buttonOpenAttachment.isEnabled())
          {
            _buttonOpenAttachment_actionPerformed(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.NEXT_MESSAGE)
          {
            _buttonNext_actionPerformed(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.PREVIOUS_MESSAGE)
          {
            _buttonPrevious_actionPerformed(null);
            return (true);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.REPLY_TO_MESSAGE)
          {
            _buttonReply_actionPerformed(null);
            return (true);
          }
        return (false);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    return (false);
  }

  private void _buttonPrevious_actionPerformed(ActionEvent actionEvent)
  {
    showPreviousNextMessage(Session.getInstance().getPreviousMessage(_itemVO.getGlobalItemID()));
  }

  private void _buttonNext_actionPerformed(ActionEvent actionEvent)
  {
    showPreviousNextMessage(Session.getInstance().getNextMessage(_itemVO.getGlobalItemID()));
  }

  private void showPreviousNextMessage(ItemVO itemVO)
  {
    if (itemVO != null)
      {
        InfoMessageGetTypeVO infoVO = (InfoMessageGetTypeVO) itemVO.getInfo();
        if (infoVO.getMessageType() == MessageType.TEXT)
          getParentPanel().discardAndShowWindow(new MessageOpenWindow(itemVO));
        else if (infoVO.getMessageType() == MessageType.VOICE)
          getParentPanel().discardAndShowWindow(new VoiceNoteOpenWindow(itemVO));
      }
  }
}
