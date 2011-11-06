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

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.swing.SwingConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.zypr.api.API;
import net.zypr.api.enums.ItemDisplay;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.vo.InfoContactTypeVO;
import net.zypr.api.vo.InfoMessageGetTypeVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.gui.ImageFetcher;
import net.zypr.gui.Settings;
import net.zypr.gui.components.Button;
import net.zypr.gui.components.HTMLPane;
import net.zypr.gui.components.ImagePanel;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.panels.MapViewPanel;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.WebBrowser;
import net.zypr.mmp.mplayer.TTSPlayer;

import org.apache.commons.codec.binary.Base64;

public class ContactDetailsWindow
  extends ModalWindow
{
  private ImagePanel _imagePanelContactPicture = new ImagePanel("placeholder-picture-contact-96.png");
  private Button _buttonSendMessage = new Button("button-contact-send-message-up.png", "button-contact-send-message-down.png", "button-contact-send-message-disabled.png");
  /*-
  private Button _buttonSendVoiceNote = new Button("button-contact-leave-voice-note-up.png", "button-contact-leave-voice-note-down.png");
  -*/
  private Button _buttonLocateOnMap = new Button("button-contact-locate-on-the-map-up.png", "button-contact-locate-on-the-map-down.png", "button-contact-locate-on-the-map-disabled.png");
  private Button _buttonAddRemoveToRoute = new Button("button-contact-add-to-route-up.png", "button-contact-add-to-route-down.png", "button-contact-add-to-route-disabled.png");
  private Button _buttonShowHideOnMap = new Button("button-contact-hide-on-the-map-up.png", "button-contact-hide-on-the-map-down.png", "button-contact-hide-on-the-map-disabled.png");
  /*-
  private Button _buttonGotoSocialNetworkPage = new Button("button-contact-social-network-page-up.png", "button-contact-social-network-page-down.png");
  -*/
  private ScrollPane _scrollPaneUserFeed = new ScrollPane();
  private HTMLPane _htmlPaneUserFeed = new HTMLPane();
  private Label _labelDescription = new Label();
  private ItemVO _itemVO;
  private InfoContactTypeVO _infoVO;
  private boolean _allowSendMessage = true;

  public ContactDetailsWindow(ItemVO itemVO, InfoContactTypeVO infoVO, boolean allowSendMessage)
  {
    super();
    try
      {
        _allowSendMessage = allowSendMessage;
        _itemVO = itemVO;
        _infoVO = infoVO;
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
    _buttonSendMessage.setLocation(130, 70);
    _buttonSendMessage.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonSendMessage_actionPerformed(actionEvent);
      }
    });
    /*-
    _buttonSendVoiceNote.setLocation(200, 70);
    _buttonSendVoiceNote.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonSendVoiceNote_actionPerformed(actionEvent);
      }
    });
    -*/
    _buttonLocateOnMap.setLocation(200, 70);
    _buttonLocateOnMap.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonLocateOnMap_actionPerformed(actionEvent);
      }
    });
    _buttonAddRemoveToRoute.setLocation(270, 70);
    _buttonAddRemoveToRoute.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonAddRemoveToRoute_actionPerformed(actionEvent);
      }
    });
    _buttonShowHideOnMap.setLocation(340, 70);
    _buttonShowHideOnMap.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonShowHideOnMap_actionPerformed(actionEvent);
      }
    });
    /*-
    _buttonGotoSocialNetworkPage.setLocation(480, 70);
    _buttonGotoSocialNetworkPage.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _buttonGotoSocialNetworkPage_actionPerformed(actionEvent);
      }
    });
    -*/
    _scrollPaneUserFeed.setBounds(20, 135, 515, 108);
    _scrollPaneUserFeed.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneUserFeed.scrollDown(_scrollPaneUserFeed.getHeight() / 4);
      }
    });
    _scrollPaneUserFeed.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneUserFeed.scrollUp(_scrollPaneUserFeed.getHeight() / 4);
      }
    });
    _htmlPaneUserFeed.addHyperlinkListener(new HyperlinkListener()
    {
      public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent)
      {
        _htmlPaneUserFeed_hyperlinkUpdate(hyperlinkEvent);
      }
    });
    _scrollPaneUserFeed.addToViewport(_htmlPaneUserFeed);
    _labelDescription.setBounds(new Rectangle(140, 15, 455, 40));
    _labelDescription.setVerticalAlignment(SwingConstants.TOP);
    _labelDescription.setVerticalTextPosition(SwingConstants.TOP);
    setTitle("Contact details of " + _itemVO.getName());
    /*- Fix it
    setDataSourceLogo(Resources.getInstance().getImageIcon(Session.getInstance().getServiceDetails(_itemVO.getService().toLowerCase()).getServiceIconURI()));
    setDataSourceURL("http://www." + _itemVO.getService().toLowerCase() + ".com/");
    -*/
    _imagePanelContactPicture.setLocation(20, 15);
    _imagePanelContactPicture.setOpaque(false);
    BufferedImage bufferedImage = ImageFetcher.getInstance().getImage(_itemVO.getIconURL(), 96, 96, null);
    if (bufferedImage != null)
      _imagePanelContactPicture.setImage(bufferedImage);
    _labelDescription.setText("<html>" + _itemVO.getDescription().trim() + "</html>");
    _panelContent.add(_labelDescription, null);
    _panelContent.add(_scrollPaneUserFeed, null);
    /*-
    _panelContent.add(_buttonGotoSocialNetworkPage, null);
    -*/
    _panelContent.add(_buttonShowHideOnMap, null);
    _panelContent.add(_buttonAddRemoveToRoute, null);
    _panelContent.add(_buttonLocateOnMap, null);
    /*-
    _panelContent.add(_buttonSendVoiceNote, null);
    -*/
    _panelContent.add(_buttonSendMessage, null);
    _panelContent.add(_imagePanelContactPicture, null);
    ItemVO[] feedItems = API.getInstance().getSocial().feedGet(_itemVO.getService(), _itemVO.getServiceItemID());
    StringBuffer stringBuffer = new StringBuffer("<html><body alink=\"#FF0000\" link=\"#EEFFEE\" text=\"#FFFFFF\" vlink=\"#EEFFEE\"><p>");
    if (!_itemVO.getDescription().trim().equals(""))
      {
        stringBuffer.append("<b>About :</b>");
        stringBuffer.append(_itemVO.getDescription().trim().replaceAll("(http[s]?:\\/\\/[^\\s]+)", "<a style=\"color: #3BB9FF;\" href=\"$1\">$1</a>"));
        stringBuffer.append("</p><hr/>");
      }
    for (int index = 0; index < feedItems.length; index++)
      {
        stringBuffer.append("<b>");
        InfoMessageGetTypeVO infoVO = (InfoMessageGetTypeVO) feedItems[index].getInfo();
        Format formatterDate = new SimpleDateFormat(Settings.getInstance().getDTO().getDateFormatPattern().getPattern());
        Format formatterTime = new SimpleDateFormat(Settings.getInstance().getDTO().getTimeFormatPattern().getPattern());
        stringBuffer.append(formatterDate.format(infoVO.getDateSent()).toString());
        stringBuffer.append(" ");
        stringBuffer.append(formatterTime.format(infoVO.getDateSent()).toString());
        stringBuffer.append(" : </b>");
        stringBuffer.append(feedItems[index].getDescription().trim().replaceAll("(http[s]?:\\/\\/[^\\s]+)", "<a style=\"color: #3BB9FF;\" href=\"$1\">$1</a>"));
        stringBuffer.append("</p>");
        if (index != feedItems.length - 1)
          stringBuffer.append("<hr/>");
      }
    stringBuffer.append("</body></html>");
    _htmlPaneUserFeed.setText(stringBuffer.toString());
    if (Settings.getInstance().getDTO().isTTSContactFeed())
      new Thread(new Runnable()
      {
        public void run()
        {
          try
            {
              TTSPlayer.getInstance().loadPlayfile(API.getInstance().getVoice().tts((Settings.getInstance().getDTO().isTTSCardHeader() ? _itemVO.getName() + ", " : "") + _infoVO.getStatusMessage().trim()).getAudioURI());
            }
          catch (Exception exception)
            {
              Debug.displayStack(this, exception);
            }
        }
      }).start();
  }

  protected void windowVisible()
  {
    if (Settings.getInstance().getDTO().isHiddenContact(_itemVO.getServiceItemID(), _itemVO.getService()))
      _buttonShowHideOnMap.setIcons("button-contact-show-on-the-map-up.png", "button-contact-show-on-the-map-down.png", "button-contact-show-on-the-map-disabled.png");
    else
      _buttonShowHideOnMap.setIcons("button-contact-hide-on-the-map-up.png", "button-contact-hide-on-the-map-down.png", "button-contact-hide-on-the-map-disabled.png");
    _buttonLocateOnMap.setEnabled(_itemVO.getDisplay() == ItemDisplay.GEO);
    _buttonAddRemoveToRoute.setEnabled(_itemVO.getDisplay() == ItemDisplay.GEO);
    _buttonShowHideOnMap.setEnabled(_itemVO.getDisplay() == ItemDisplay.GEO);
    _buttonSendMessage.setEnabled(_allowSendMessage);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }

  private void _buttonSendMessage_actionPerformed(ActionEvent actionEvent)
  {
    try
      {
        if (API.getInstance().getSocial().canMessageSend(_itemVO.getService()))
          getParentPanel().showWindow(new MessageSendWindow(_itemVO, null, null));
        else
          getParentPanel().showWindow(new WarningWindow("Cannot send message", "The service \"" + _itemVO.getService() + "\" does not let you send private messages to \"" + _itemVO.getName() + "\". Please select another contact.", 30, "send-message-contact-list-can-not-send.wav"));
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

  private void _buttonSendVoiceNote_actionPerformed(ActionEvent actionEvent)
  {
    try
      {
        if (API.getInstance().getSocial().canMessageSend(_itemVO.getService()))
          getParentPanel().showWindow(new VoiceNoteSendWindow(_itemVO, null, null));
        else
          getParentPanel().showWindow(new WarningWindow("Cannot send message", "The service \"" + _itemVO.getService() + "\" does not let you send private messages to \"" + _itemVO.getName() + "\". Please select another contact.", 30, "send-message-contact-list-can-not-send.wav"));
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

  private void _buttonLocateOnMap_actionPerformed(ActionEvent actionEvent)
  {
    if (Settings.getInstance().getDTO().isHiddenContact(_itemVO.getServiceItemID(), _itemVO.getService()))
      {
        Settings.getInstance().getDTO().showContact(_itemVO.getServiceItemID(), _itemVO.getService());
        ((MapViewPanel) getParentPanel()).buildMarkers();
      }
    ((MapViewPanel) getParentPanel()).setMapCenter(_itemVO.getPosition());
    getParentPanel().discardAllWindows();
  }

  private void _buttonAddRemoveToRoute_actionPerformed(ActionEvent actionEvent)
  {
    if (Settings.getInstance().getDTO().isHiddenContact(_itemVO.getServiceItemID(), _itemVO.getService()))
      {
        Settings.getInstance().getDTO().showContact(_itemVO.getServiceItemID(), _itemVO.getService());
        ((MapViewPanel) getParentPanel()).buildMarkers();
      }
    _buttonAddRemoveToRoute.setEnabled(false);
    ((MapViewPanel) getParentPanel()).addRoute(_itemVO);
  }

  private void _buttonShowHideOnMap_actionPerformed(ActionEvent actionEvent)
  {
    if (Settings.getInstance().getDTO().isHiddenContact(_itemVO.getServiceItemID(), _itemVO.getService()))
      Settings.getInstance().getDTO().showContact(_itemVO.getServiceItemID(), _itemVO.getService());
    else
      Settings.getInstance().getDTO().hideContact(_itemVO.getServiceItemID(), _itemVO.getService());
    ((MapViewPanel) getParentPanel()).buildMarkers();
    windowVisible();
  }

  private void _buttonGotoSocialNetworkPage_actionPerformed(ActionEvent actionEvent)
  {
    WebBrowser.openURL("data:text/html;base64," + (new Base64()).encodeBase64URLSafeString(("<h1>Going to " + _itemVO.getService().toUpperCase() + " page of " + _itemVO.getName() + "...</h1>").getBytes()));
  }

  private void _htmlPaneUserFeed_hyperlinkUpdate(HyperlinkEvent hyperlinkEvent)
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

  public boolean processVoiceCommand(VoiceCommandVO voiceCommandVO)
  {
    return (false);
  }
}
