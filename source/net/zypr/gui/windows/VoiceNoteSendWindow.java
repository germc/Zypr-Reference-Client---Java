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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.io.File;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import javax.swing.event.HyperlinkEvent;

import net.zypr.api.API;
import net.zypr.api.enums.ItemType;
import net.zypr.api.enums.VoiceCommandAction;
import net.zypr.api.enums.VoiceCommandContextState;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.vo.GeoPositionVO;
import net.zypr.api.vo.InfoContactTypeVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.api.vo.VoiceCommandVariableVO;
import net.zypr.gui.ImageFetcher;
import net.zypr.gui.audio.AudioPlayer;
import net.zypr.gui.audio.AudioRecorder;
import net.zypr.gui.audio.AudioRecorderException;
import net.zypr.gui.audio.WAVERecorder;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.components.TextPane;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.FileUtils;
import net.zypr.gui.utils.WebBrowser;
import net.zypr.mmp.mplayer.MPlayer;

public class VoiceNoteSendWindow
  extends ModalWindow
{
  private ImagePanel _imagePanelContactPicture = new ImagePanel("placeholder-picture-contact-96.png");
  private ImagePanel _imagePanelAttachmentPicture = new ImagePanel("placeholder-picture-attachment-96.png");
  private Button _buttonContactDetails = new Button("button-voice-note-send-contact-details-up.png", "button-voice-note-send-contact-details-down.png");
  private Button _buttonOpenAttachment = new Button("button-voice-note-send-open-attachment-up.png", "button-voice-note-send-open-attachment-down.png");
  private Button _buttonRecord = new Button("button-voice-note-send-record-up.png", "button-voice-note-send-record-down.png", "button-voice-note-send-record-disabled.png");
  private Button _buttonPlaybackStop = new Button("button-voice-note-send-play-up.png", "button-voice-note-send-play-down.png", "button-voice-note-send-play-disabled.png");
  private Button _buttonSend = new Button("button-voice-note-send-send-up.png", "button-voice-note-send-send-down.png", "button-voice-note-send-send-disabled.png");
  private Button _buttonCancel = new Button("button-voice-note-send-cancel-up.png", "button-voice-note-send-cancel-down.png");
  private Label _labelMessageTo = new Label();
  private Label _labelMessageAttachment = new Label();
  private ScrollPane _scrollPaneMessageText = new ScrollPane();
  private TextPane _textPaneMessageText = new TextPane();
  private ItemVO _contactItemVO = null;
  private ItemVO _attachmentItemVO = null;
  private GeoPositionVO _geoPositionVO = null;
  private int _speechVolume = 0;
  private AudioRecorder _audioRecorder = null;
  private boolean _recorded = false;
  private LineListener _lineListener = new LineListener()
  {
    public void update(LineEvent event)
    {
      if (event.getType() == LineEvent.Type.START)
        _buttonPlaybackStop.setIcons("button-voice-note-send-stop-up.png", "button-voice-note-send-stop-down.png", "button-voice-note-send-stop-disabled.png");
      else if (event.getType() == LineEvent.Type.STOP)
        _buttonPlaybackStop.setIcons("button-voice-note-send-play-up.png", "button-voice-note-send-play-down.png", "button-voice-note-send-play-disabled.png");
    }
  };

  public VoiceNoteSendWindow(ItemVO contactItemVO, ItemVO attachmentItemVO, GeoPositionVO geoPositionVO)
  {
    super();
    try
      {
        _contactItemVO = contactItemVO;
        _attachmentItemVO = attachmentItemVO;
        _geoPositionVO = geoPositionVO;
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
    this.setSize(new Dimension(620, 340));
    /*- Fix it
    setDataSourceLogo(Resources.getInstance().getImageIcon("icon-network-" + _contactItemVO.getService() + ".png"));
    setDataSourceURL("http://www. + _contactItemVO.getService() + .com/");
    -*/
    _imagePanelContactPicture.setLocation(15, 15);
    _imagePanelAttachmentPicture.setLocation(505, 15);
    _buttonContactDetails.setLocation(140, 70);
    _buttonContactDetails.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonContactDetails_actionPerformed(actionEvent);
      }
    });
    _buttonOpenAttachment.setLocation(425, 70);
    _buttonOpenAttachment.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonOpenAttachment_actionPerformed(actionEvent);
      }
    });
    _buttonRecord.setBounds(new Rectangle(235, 70, 54, 45));
    _buttonRecord.addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MouseEvent mouseEvent)
      {
        _buttonRecord_mousePressed(mouseEvent);
      }

      public void mouseReleased(MouseEvent mouseEvent)
      {
        _buttonRecord_mouseReleased(mouseEvent);
      }
    });
    _buttonPlaybackStop.setEnabled(false);
    _buttonPlaybackStop.setBounds(new Rectangle(330, 70, 54, 45));
    _buttonPlaybackStop.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPlaybackStop_actionPerformed(actionEvent);
      }
    });
    _buttonSend.setEnabled(false);
    _buttonSend.setLocation(355, 245);
    _buttonSend.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonSend_actionPerformed(actionEvent);
      }
    });
    _buttonCancel.setLocation(210, 245);
    _buttonCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonCancel_actionPerformed(actionEvent);
      }
    });
    _labelMessageTo.setBounds(125, 15, 370, 20);
    _labelMessageAttachment.setBounds(125, 40, 370, 20);
    _scrollPaneMessageText.setBounds(15, 130, 590, 105);
    _scrollPaneMessageText.addToViewport(_textPaneMessageText);
    _textPaneMessageText.setEditable(true);
    BufferedImage bufferedImage = ImageFetcher.getInstance().getImage(_contactItemVO.getIconURL(), 96, 96, null);
    if (bufferedImage != null)
      _imagePanelContactPicture.setImage(bufferedImage);
    if (_attachmentItemVO != null)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_attachmentItemVO.getIconURL(), 96, 96, null);
        if (bufferedImage != null)
          _imagePanelAttachmentPicture.setImage(bufferedImage);
        _labelMessageAttachment.setText("Attachment:" + _attachmentItemVO.getName());
        setTitle("Do you want to add text?");
        setVoiceCommandState(VoiceCommandState.CONFIRM_ADD_TEXT);
        setVoiceCommandContextState(new VoiceCommandContextState[]
            { VoiceCommandContextState.ATTACHMENT_AVAILABLE });
      }
    else
      {
        _labelMessageAttachment.setText("");
        _buttonOpenAttachment.setVisible(false);
        _imagePanelAttachmentPicture.setVisible(false);
        setTitle("Please state message text...");
        setVoiceCommandState(VoiceCommandState.GET_MESSAGE_TEXT);
      }
    _labelMessageTo.setText("To : " + _contactItemVO.getName());
    _panelContent.add(_labelMessageAttachment, null);
    _panelContent.add(_labelMessageTo, null);
    _panelContent.add(_buttonRecord, null);
    _panelContent.add(_buttonPlaybackStop, null);
    _panelContent.add(_buttonOpenAttachment, null);
    _panelContent.add(_buttonContactDetails, null);
    _panelContent.add(_buttonSend, null);
    _panelContent.add(_buttonCancel, null);
    _panelContent.add(_imagePanelContactPicture, null);
    _panelContent.add(_imagePanelAttachmentPicture, null);
    _panelContent.add(_scrollPaneMessageText, null);
    AudioPlayer.getInstance().addLineListener(_lineListener);
    if (_attachmentItemVO != null)
      _geoPositionVO = _attachmentItemVO.getPosition();
  }

  protected void windowVisible()
  {
    _buttonPlaybackStop.setEnabled(_recorded);
    _buttonSend.setEnabled(_recorded);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    AudioPlayer.getInstance().removeLineListener(_lineListener);
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
            getParentPanel().showWindow(new WarningWindow("Unable to open attachment", "Unable to open attachment. Unknown attachment type", 30, "send-voice-note-unknown-attachment-type.wav"));
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
    /*- Fix it
    try
      {
        _textPaneMessageText.setText(_textPaneMessageText.getText().trim());
        if (API.getInstance().getSocial().voiceNoteSend(API.getInstance().getSocial().getDefaultService(), _audioRecorder.getFile(), "audio/wav", _contactItemVO.getName(), _contactItemVO.getService(), _contactItemVO.getServiceItemID(), "private", _geoPositionVO, _textPaneMessageText.getText(), (_attachmentItemVO != null ? _attachmentItemVO.getRawJSONString() : null)) == StatusCode.SUCCESSFUL)
          {
            ((MapViewPanel) getParentPanel()).showTopBarNotification("Voice message sent", true, "notification-voice-message-sent-successfully.wav");
            getParentPanel().discardWindow();
          }
        else
          {
            getParentPanel().showWindow(new WarningWindow("Unable to send voice note", "Unable to send voice note. Please try again later.", 30, "send-voice-note-unable-to-send-warning.wav"));
          }
      }
    catch (APIProtocolException protocolException)
      {
        Debug.displayStack(this, protocolException);
        getParentPanel().showWindow(new WarningWindow("Protocol error", "Unable to send message due to a protocol error! Please contact to admin.", 30, "send-voice-note-protocol-error-warning.wav"));
      }
    catch (APICommunicationException communicationException)
      {
        Debug.displayStack(this, communicationException);
        getParentPanel().showWindow(new WarningWindow("Communication error", "Unable to send voice note due to communication error! Please check your internet connection.", 30, "send-voice-note-communication-error-warning.wav"));
      }
    catch (APIServerErrorException serverErrorException)
      {
        Debug.displayStack(this, serverErrorException);
        getParentPanel().showWindow(new WarningWindow("Server error", "Unable to send voice note due to a protocol error! Please contact to admin.", 30, "send-voice-note-unable-to-send-warning.wav"));
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
            setTitle("Please state message text...");
            setVoiceCommandState(VoiceCommandState.GET_MESSAGE_TEXT);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.GET_TEXT)
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("user_text");
            _textPaneMessageText.setText(voiceCommandVariableVO.getValueAsString());
            setTitle("It is correct?");
            setVoiceCommandState(VoiceCommandState.CONFIRM_MESSAGE);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.CONFIRMATION_YES && getVoiceCommandState() == VoiceCommandState.CONFIRM_MESSAGE)
          {
            _buttonSend_actionPerformed(null);
          }
        else if (voiceCommandVO.getName() == VoiceCommandAction.CONFIRMATION_NO && getVoiceCommandState() == VoiceCommandState.CONFIRM_MESSAGE)
          {
            setTitle("Please state message text...");
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

  private void _buttonRecord_mousePressed(MouseEvent mouseEvent)
  {
    _speechVolume = MPlayer.getInstance().getPlaybackVolume();
    if (MPlayer.getInstance().isPlaybackStarted() && !MPlayer.getInstance().isPlaybackMuted() && MPlayer.getInstance().getPlaybackVolume() > 25)
      MPlayer.getInstance().setVolume(25);
    if (_audioRecorder == null)
      {
        _audioRecorder = new WAVERecorder();
        _audioRecorder.setFilename(FileUtils.SYSTEM_TEMP_PATH.toString() + File.separator + System.currentTimeMillis());
      }
    try
      {
        _buttonPlaybackStop.setEnabled(false);
        _audioRecorder.startRecording();
      }
    catch (AudioRecorderException audioRecorderException)
      {
        Debug.displayStack(this, audioRecorderException);
      }
  }

  private void _buttonRecord_mouseReleased(MouseEvent mouseEvent)
  {
    _audioRecorder.stopRecording();
    _buttonPlaybackStop.setEnabled(true);
    _buttonSend.setEnabled(true);
    _recorded = true;
  }

  private void _buttonPlaybackStop_actionPerformed(ActionEvent actionEvent)
  {
    if (AudioPlayer.getInstance().isPlaying())
      AudioPlayer.getInstance().stopPlayback();
    else
      AudioPlayer.getInstance().startPlayback(_audioRecorder.getFile());
  }
}
