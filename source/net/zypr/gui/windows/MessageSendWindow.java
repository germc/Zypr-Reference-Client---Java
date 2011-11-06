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
import java.awt.image.BufferedImage;

import javax.swing.event.HyperlinkEvent;

import net.zypr.api.API;
import net.zypr.api.enums.ItemType;
import net.zypr.api.enums.VoiceCommandAction;
import net.zypr.api.enums.VoiceCommandContextState;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.vo.InfoContactTypeVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.api.vo.VoiceCommandVariableVO;
import net.zypr.gui.ImageFetcher;
import net.zypr.gui.Settings;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.components.TextPane;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.WebBrowser;
import net.zypr.mmp.mplayer.TTSPlayer;

public class MessageSendWindow
  extends ModalWindow
{
  private ImagePanel _imagePanelContactPicture = new ImagePanel("placeholder-picture-contact-96.png");
  private ImagePanel _imagePanelAttachmentPicture = new ImagePanel("placeholder-picture-attachment-96.png");
  private Button _buttonContactDetails = new Button("button-message-send-contact-details-up.png", "button-message-send-contact-details-down.png");
  private Button _buttonOpenAttachment = new Button("button-message-send-open-attachment-up.png", "button-message-send-open-attachment-down.png");
  private Button _buttonSend = new Button("button-message-send-send-up.png", "button-message-send-send-down.png");
  private Button _buttonCancel = new Button("button-message-send-cancel-up.png", "button-message-send-cancel-down.png");
  private Label _labelMessageTo = new Label();
  private Label _labelMessageAttachment = new Label();
  private ScrollPane _scrollPaneMessageText = new ScrollPane();
  private TextPane _textPaneMessageText = new TextPane();
  private ItemVO _contactItemVO = null;
  private ItemVO _attachmentItemVO = null;
  private String _service = null;
  private boolean _publicMessage = false;

  public MessageSendWindow(ItemVO contactItemVO, ItemVO attachmentItemVO, String service)
  {
    super();
    try
      {
        _contactItemVO = contactItemVO;
        _attachmentItemVO = attachmentItemVO;
        _service = service;
        jbInit();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  public MessageSendWindow(ItemVO contactItemVO, ItemVO attachmentItemVO, String service, boolean publicMessage)
  {
    this(contactItemVO, attachmentItemVO, service);
    _publicMessage = publicMessage;
  }

  private void jbInit()
    throws Exception
  {
    this.setSize(new Dimension(560, 293));
    /*- Fix it
    setDataSourceLogo(Resources.getInstance().getImageIcon("icon-network-" + (_service != null ? _service : _contactItemVO.getService()) + ".png"));
    setDataSourceURL("http://www. + (_service != null ? _service: _contactItemVO.getService()) + .com/");
    -*/
    _imagePanelContactPicture.setLocation(15, 15);
    _imagePanelAttachmentPicture.setLocation(445, 15);
    _buttonContactDetails.setLocation(140, 70);
    _buttonContactDetails.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonContactDetails_actionPerformed(actionEvent);
      }
    });
    _buttonOpenAttachment.setLocation(365, 70);
    _buttonOpenAttachment.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonOpenAttachment_actionPerformed(actionEvent);
      }
    });
    _buttonSend.setLocation(325, 198);
    _buttonSend.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonSend_actionPerformed(actionEvent);
      }
    });
    _buttonCancel.setLocation(180, 198);
    _buttonCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonCancel_actionPerformed(actionEvent);
      }
    });
    _labelMessageTo.setBounds(125, 15, 370, 20);
    _labelMessageAttachment.setBounds(125, 40, 370, 20);
    _scrollPaneMessageText.setBounds(15, 130, 525, 58);
    _scrollPaneMessageText.addToViewport(_textPaneMessageText);
    _textPaneMessageText.setEditable(true);
    if (_contactItemVO == null)
      {
        _imagePanelContactPicture.setVisible(false);
        _buttonContactDetails.setVisible(false);
        _labelMessageTo.setVisible(false);
      }
    else
      {
        BufferedImage bufferedImage = ImageFetcher.getInstance().getImage(_contactItemVO.getIconURL(), 96, 96, null);
        if (bufferedImage != null)
          {
            _labelMessageTo.setText("To : " + _contactItemVO.getName());
            _imagePanelContactPicture.setImage(bufferedImage);
          }
      }
    if (_attachmentItemVO != null)
      {
        BufferedImage bufferedImage = ImageFetcher.getInstance().getImage(_attachmentItemVO.getIconURL(), 96, 96, null);
        if (bufferedImage != null)
          _imagePanelAttachmentPicture.setImage(bufferedImage);
        _labelMessageAttachment.setText("Attachment:" + _attachmentItemVO.getName());
        setTitle("Send Message : Do you want to add text?");
        textToSpeech("Do you want to add text");
        setVoiceCommandState(VoiceCommandState.CONFIRM_ADD_TEXT);
        setVoiceCommandContextState(new VoiceCommandContextState[]
            { VoiceCommandContextState.ATTACHMENT_AVAILABLE });
      }
    else
      {
        _labelMessageAttachment.setText("");
        _buttonOpenAttachment.setVisible(false);
        _imagePanelAttachmentPicture.setVisible(false);
        setTitle("Send Message : Please state message text...");
        textToSpeech("Please state message text");
        setVoiceCommandState(VoiceCommandState.GET_MESSAGE_TEXT);
      }
    _panelContent.add(_labelMessageAttachment, null);
    _panelContent.add(_labelMessageTo, null);
    _panelContent.add(_buttonOpenAttachment, null);
    _panelContent.add(_buttonContactDetails, null);
    _panelContent.add(_buttonSend, null);
    _panelContent.add(_buttonCancel, null);
    _panelContent.add(_imagePanelContactPicture, null);
    _panelContent.add(_imagePanelAttachmentPicture, null);
    _panelContent.add(_scrollPaneMessageText, null);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }

  private void _buttonContactDetails_actionPerformed(ActionEvent actionEvent)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        try
          {
            getParentPanel().showWindow(new ContactDetailsWindow(_contactItemVO, (InfoContactTypeVO) API.getInstance().getInfoVO(_contactItemVO.getAction("details").getHandlerURI()), false));
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
    }).start();
  }

  private void _buttonOpenAttachment_actionPerformed(ActionEvent actionEvent)
  {
    if (_attachmentItemVO != null)
      {
        if (_attachmentItemVO.getType() == ItemType.USER)
          {
            _buttonOpenAttachment.setEnabled(false);
            new Thread(new Runnable()
            {
              public void run()
              {
                try
                  {
                    getParentPanel().showWindow(new ContactDetailsWindow(_attachmentItemVO, (InfoContactTypeVO) API.getInstance().getInfoVO(_attachmentItemVO.getAction("details").getHandlerURI()), false));
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
        else if (_attachmentItemVO.getType() == ItemType.RADIO)
          {
            getParentPanel().showWindow(new RadioStationDetailsWindow(_attachmentItemVO, false));
          }
        else if (_attachmentItemVO.getType() == ItemType.POI)
          {
            getParentPanel().showWindow(new POIDetailsWindow(_attachmentItemVO, false));
          }
        else
          {
            getParentPanel().showWindow(new WarningWindow("Unable to open attachment", "Unable to open attachment. Unknown attachment type", 30, "send-message-unknown-attachment-type.wav"));
          }
      }
  }

  private void _textPaneMessageText_hyperlinkUpdate(HyperlinkEvent hyperlinkEvent)
  {
    try
      {
        if (hyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
          {
            WebBrowser.openURL(hyperlinkEvent.getURL().toString());
          }
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  private void _buttonCancel_actionPerformed(ActionEvent actionEvent)
  {
    _buttonWindowClose_actionPerformed(actionEvent);
  }

  private void _buttonSend_actionPerformed(ActionEvent actionEvent)
  {
    /*-
    try
      {
        _textPaneMessageText.setText(_textPaneMessageText.getText().trim());
        if (_publicMessage)
          {
            if (API.getInstance().getSocial().messageSendPublic(_service != null ? _service : API.getInstance().getSocial().getDefaultService(), (_contactItemVO == null ? "" : _contactItemVO.getName()), (_service != null ? _service : _contactItemVO.getService()), (_contactItemVO == null ? "" : _contactItemVO.getServiceItemID()), (_contactItemVO == null ? "public" : "private"), _textPaneMessageText.getText(), (_attachmentItemVO != null ? _attachmentItemVO.getRawJSONString() : null)) == StatusCode.SUCCESSFUL)
              {
                ((MapViewPanel) getParentPanel()).showTopBarNotification("Public message sent", true, "notification-message-sent-successfully.wav");
                getParentPanel().discardWindow();
              }
            else
              {
                getParentPanel().showWindow(new WarningWindow("Unable to send public message", "Unable to send public message. Please try again later.", 30, "send-message-unable-to-send-warning.wav"));
              }
          }
        else
          {
            if (API.getInstance().getSocial().messageSend(API.getInstance().getSocial().getDefaultService(), (_contactItemVO == null ? "" : _contactItemVO.getName()), (_service != null ? _service : _contactItemVO.getService()), (_contactItemVO == null ? "" : _contactItemVO.getServiceItemID()), (_contactItemVO == null ? "public" : "private"), _textPaneMessageText.getText(), (_attachmentItemVO != null ? _attachmentItemVO.getRawJSONString() : null)) == StatusCode.SUCCESSFUL)
              {
                ((MapViewPanel) getParentPanel()).showTopBarNotification("Private message sent", true, "notification-message-sent-successfully.wav");
                getParentPanel().discardWindow();
              }
            else
              {
                getParentPanel().showWindow(new WarningWindow("Unable to send private message", "Unable to send private message. Please try again later.", 30, "send-message-unable-to-send-warning.wav"));
              }
          }
      }
    catch (APIProtocolException protocolException)
      {
        Debug.displayStack(this, protocolException);
        getParentPanel().showWindow(new WarningWindow("Protocol error", "Unable to send message due to a protocol error! Please contact to admin.", 30, "send-message-protocol-error-warning.wav"));
      }
    catch (APICommunicationException communicationException)
      {
        Debug.displayStack(this, communicationException);
        getParentPanel().showWindow(new WarningWindow("Communication error", "Unable to send message due to communication error! Please check your internet connection.", 30, "send-message-communication-error-warning.wav"));
      }
    catch (APIServerErrorException serverErrorException)
      {
        Debug.displayStack(this, serverErrorException);
        getParentPanel().showWindow(new WarningWindow("Server error", "Unable to send message due to a protocol error! Please contact to admin.", 30, "send-message-unable-to-send-warning.wav"));
      }
  -*/
  }

  public boolean processVoiceCommand(VoiceCommandVO voiceCommandVO)
  {
    try
      {
        if (voiceCommandVO.getName() == VoiceCommandAction.CONFIRMATION_NO && getVoiceCommandState() == VoiceCommandState.CONFIRM_ADD_TEXT)
          {
            _buttonSend_actionPerformed(null);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.CONFIRMATION_YES && getVoiceCommandState() == VoiceCommandState.CONFIRM_ADD_TEXT)
          {
            setTitle("Send Message : Please state message text...");
            textToSpeech("Please state message text");
            setVoiceCommandState(VoiceCommandState.GET_MESSAGE_TEXT);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.GET_TEXT)
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("user_text");
            _textPaneMessageText.setText(voiceCommandVariableVO.getValueAsString());
            setTitle("Send Message : Is this correct?");
            textToSpeech("Is this correct?");
            setVoiceCommandState(VoiceCommandState.CONFIRM_MESSAGE);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.CONFIRMATION_YES && getVoiceCommandState() == VoiceCommandState.CONFIRM_MESSAGE)
          {
            _buttonSend_actionPerformed(null);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.CONFIRMATION_NO && getVoiceCommandState() == VoiceCommandState.CONFIRM_MESSAGE)
          {
            setTitle("Send Message : Please state message text...");
            textToSpeech("Please state message text");
            setVoiceCommandState(VoiceCommandState.GET_MESSAGE_TEXT);
            _textPaneMessageText.setText("");
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.SHOW_ATTACHMENT)
          {
            _buttonOpenAttachment_actionPerformed(null);
          }
        else
          {
            return (false);
          }
        return (true);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    return (false);
  }

  private void textToSpeech(final String text)
  {
    if (Settings.getInstance().getDTO().isTTSCardHeader())
      new Thread(new Runnable()
      {
        public void run()
        {
          try
            {
              TTSPlayer.getInstance().loadPlayfile(API.getInstance().getVoice().tts(text).getAudioURI());
            }
          catch (Exception exception)
            {
              Debug.displayStack(this, exception);
            }
        }
      }).start();
  }
}
