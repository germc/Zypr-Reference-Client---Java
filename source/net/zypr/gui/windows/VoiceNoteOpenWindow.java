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

import java.io.File;
import java.io.FileOutputStream;

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

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
import net.zypr.gui.audio.AudioPlayer;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.PressAndHoldButton;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.components.TextPane;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.FileUtils;

public class VoiceNoteOpenWindow
  extends ModalWindow
{
  private ImagePanel _imagePanelContactPicture = new ImagePanel("placeholder-picture-contact-96.png");
  private ImagePanel _imagePanelAttachmentPicture = new ImagePanel("placeholder-picture-attachment-96.png");
  private Button _buttonReply = new Button("button-message-reply-up.png", "button-message-reply-down.png");
  private Button _buttonContactDetails = new Button("button-message-contact-details-up.png", "button-message-contact-details-down.png");
  private PressAndHoldButton _buttonDelete = new PressAndHoldButton("button-message-delete-up.png", "button-message-delete-down.png");
  private Button _buttonOpenAttachment = new Button("button-message-open-attachment-up.png", "button-message-open-attachment-down.png", "button-message-open-attachment-disabled.png");
  private Button _buttonPlaybackStop = new Button("button-voice-note-send-play-up.png", "button-voice-note-send-play-down.png", "button-voice-note-send-play-disabled.png");
  private Button _buttonPrevious = new Button("button-message-previous-up.png", "button-message-previous-down.png", "button-message-previous-disabled.png");
  private Button _buttonNext = new Button("button-message-next-up.png", "button-message-next-down.png", "button-message-next-disabled.png");
  private Label _labelMessageFrom = new Label();
  private Label _labelMessageSentDate = new Label();
  private ScrollPane _scrollPaneMessageText = new ScrollPane();
  private TextPane _textPaneMessageText = new TextPane();
  private ItemVO _itemVO = null;
  private InfoMessageGetTypeVO _infoVO = null;
  private File _file = null;
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

  public VoiceNoteOpenWindow(ItemVO itemVO)
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
    this.setSize(new Dimension(620, 340));
    /*- Fix it
    setDataSourceLogo(Resources.getInstance().getImageIcon(Session.getInstance().getServiceDetails(_itemVO.getService().toLowerCase()).getServiceIconURI()));
    setDataSourceURL("http://www." + _itemVO.getService().toLowerCase() + ".com/");
    -*/
    setTitle("Voice Note");
    _imagePanelContactPicture.setLocation(15, 15);
    _imagePanelAttachmentPicture.setLocation(505, 15);
    _buttonReply.setLocation(140, 70);
    _buttonReply.setBounds(new Rectangle(205, 70, 54, 45));
    _buttonReply.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonReply_actionPerformed(actionEvent);
      }
    });
    _buttonContactDetails.setLocation(220, 70);
    _buttonContactDetails.setBounds(new Rectangle(280, 70, 54, 45));
    _buttonContactDetails.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonContactDetails_actionPerformed(actionEvent);
      }
    });
    _buttonDelete.setLocation(300, 70);
    _buttonDelete.setBounds(new Rectangle(355, 70, 54, 45));
    _buttonDelete.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonDelete_actionPerformed(actionEvent);
      }
    });
    _buttonOpenAttachment.setLocation(380, 70);
    _buttonOpenAttachment.setBounds(new Rectangle(430, 70, 54, 45));
    _buttonOpenAttachment.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonOpenAttachment_actionPerformed(actionEvent);
      }
    });
    _buttonPlaybackStop.setLocation(330, 70);
    _buttonPlaybackStop.setBounds(new Rectangle(130, 70, 54, 45));
    _buttonPlaybackStop.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPlaybackStop_actionPerformed(actionEvent);
      }
    });
    _buttonNext.setLocation(355, 245);
    _buttonNext.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonNext_actionPerformed(actionEvent);
      }
    });
    _buttonPrevious.setLocation(210, 245);
    _buttonPrevious.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonPrevious_actionPerformed(actionEvent);
      }
    });
    _labelMessageFrom.setBounds(140, 15, 455, 20);
    _labelMessageSentDate.setBounds(140, 40, 160, 20);
    _scrollPaneMessageText.setBounds(15, 130, 585, 105);
    _scrollPaneMessageText.addToViewport(_textPaneMessageText);
    _textPaneMessageText.setEditable(false);
    _panelContent.add(_labelMessageSentDate, null);
    _panelContent.add(_labelMessageFrom, null);
    _panelContent.add(_buttonOpenAttachment, null);
    _panelContent.add(_buttonDelete, null);
    _panelContent.add(_buttonContactDetails, null);
    _panelContent.add(_buttonPlaybackStop, null);
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
    if (_itemVO.getAttachments() != null && _itemVO.getAttachments().length > 1)
      {
        bufferedImage = ImageFetcher.getInstance().getImage(_itemVO.getAttachments()[0].getIconURL(), 96, 96, null);
        if (bufferedImage != null)
          _imagePanelAttachmentPicture.setImage(bufferedImage);
      }
    ItemVO attachmentItemVO = _itemVO.getAttachmentByType(ItemType.AUDIO);
    if (attachmentItemVO != null)
      {
        String attachmentURI = attachmentItemVO.getAction("play").getHandlerURIWithoutToken();
        _file = new File(FileUtils.SYSTEM_TEMP_PATH, "" + File.separator + attachmentItemVO.hashCode());
        _file.deleteOnExit();
        FileOutputStream fileOutputStream = new FileOutputStream(_file.toString());
        fileOutputStream.write(API.getInstance().getBytes(attachmentURI));
        fileOutputStream.close();
      }
    AudioPlayer.getInstance().addLineListener(_lineListener);
    AudioPlayer.getInstance().startPlayback(_file);
    _currentMessageIndex = Session.getInstance().getMessageIndex(_itemVO.getGlobalItemID());
    Session.getInstance().addPropertyChangeListener(_sessionPropertyChangeListener);
  }

  protected void windowVisible()
  {
    setVoiceCommandState(VoiceCommandState.MESSAGE_OPEN);
    if (_itemVO.getAttachments() != null && _itemVO.getAttachments().length > 1)
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
    if (AudioPlayer.getInstance().isPlaying())
      AudioPlayer.getInstance().stopPlayback();
    AudioPlayer.getInstance().removeLineListener(_lineListener);
    Session.getInstance().removePropertyChangeListener(_sessionPropertyChangeListener);
    getParentPanel().discardWindow();
  }

  private void _buttonReply_actionPerformed(ActionEvent actionEvent)
  {
    /*- Implement it -*/
    Debug.print(actionEvent);
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
            getParentPanel().showWindow(new RadioStationDetailsWindow(attachmentItemVO, false));
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

  private void _buttonPlaybackStop_actionPerformed(ActionEvent actionEvent)
  {
    if (AudioPlayer.getInstance().isPlaying())
      AudioPlayer.getInstance().stopPlayback();
    else
      AudioPlayer.getInstance().startPlayback(_file);
  }
}
