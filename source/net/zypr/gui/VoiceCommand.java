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


package net.zypr.gui;

import net.zypr.api.API;
import net.zypr.api.Session;
import net.zypr.api.enums.ItemDisplay;
import net.zypr.api.enums.ItemType;
import net.zypr.api.enums.MessageType;
import net.zypr.api.enums.VoiceCommandAction;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.vo.GeoBoundsItemsVO;
import net.zypr.api.vo.InfoContactTypeVO;
import net.zypr.api.vo.InfoMessageGetTypeVO;
import net.zypr.api.vo.InfoUserTypeVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.ServiceVO;
import net.zypr.api.vo.VoiceCommandResponseVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.api.vo.VoiceCommandVariableVO;
import net.zypr.gui.panels.MapViewPanel;
import net.zypr.gui.utils.AudioUtils;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.GeoUtil;
import net.zypr.gui.utils.WebBrowser;
import net.zypr.gui.windows.CompanyNameWindow;
import net.zypr.gui.windows.ContactDetailsWindow;
import net.zypr.gui.windows.FavoritesListWindow;
import net.zypr.gui.windows.MediaControllerWindow;
import net.zypr.gui.windows.MessageListWindow;
import net.zypr.gui.windows.MessageOpenWindow;
import net.zypr.gui.windows.MessageSendWindow;
import net.zypr.gui.windows.ModalWindow;
import net.zypr.gui.windows.NBestListWindow;
import net.zypr.gui.windows.RadioStationGenreListWindow;
import net.zypr.gui.windows.RadioStationListWindow;
import net.zypr.gui.windows.RouteListWindow;
import net.zypr.gui.windows.SendToContactListWindow;
import net.zypr.gui.windows.UserAddressWindow;
import net.zypr.gui.windows.VoiceNoteOpenWindow;
import net.zypr.gui.windows.VoiceNoteSendWindow;
import net.zypr.gui.windows.WarningWindow;
import net.zypr.gui.windows.WeatherCurrentWindow;
import net.zypr.gui.windows.WeatherForecastWindow;
import net.zypr.mmp.mplayer.MPlayer;
import net.zypr.mmp.mplayer.TTSPlayer;

public class VoiceCommand
{

  public static void Process(final MapViewPanel mapViewPanel, VoiceCommandResponseVO voiceCommandResponseVO)
    throws APICommunicationException, APIProtocolException
  {
    VoiceCommandVO[] voiceCommandVOArray = voiceCommandResponseVO.getVoiceCommandVOs();
    VoiceCommandVariableVO[] voiceCommandVariableVOArray = voiceCommandResponseVO.getVoiceCommandVariableVOs();
    VoiceCommandVO voiceCommandVO = null;
    String out = "";
    out += "Voice Command Variables :\n";
    for (int i = 0; i < voiceCommandVariableVOArray.length; i++)
      out += voiceCommandVariableVOArray[i].toString() + "\n";
    out += "Voice Command Commands :\n";
    for (int i = 0; i < voiceCommandVOArray.length; i++)
      out += voiceCommandVOArray[i].toString() + "\n";
    Debug.displayInfo(mapViewPanel, out);
    if (voiceCommandVOArray == null || voiceCommandVOArray.length == 0)
      {
        try
          {
            TTSPlayer.getInstance().loadPlayfile(voiceCommandResponseVO.getVariable("no_command_found_TTS_uri").getValue().toString());
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
        return;
      }
    else if (voiceCommandVOArray.length == 1)
      {
        voiceCommandVO = voiceCommandVOArray[0];
      }
    else
      {
        if (mapViewPanel.getVoiceCommandState() == VoiceCommandState.CONFIRM_LIST)
          mapViewPanel.discardWindow();
        mapViewPanel.showWindow(new NBestListWindow(voiceCommandResponseVO));
        return;
      }
    Debug.displayInfo(mapViewPanel, "Selected Command To Process:\n" +
        voiceCommandVO);
    if (voiceCommandVO.getName() == VoiceCommandAction.SYSTEM_BACK || voiceCommandVO.getName() == VoiceCommandAction.SYSTEM_CANCEL)
      {
        mapViewPanel.discardWindow();
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.POST_SONG_TO_SOCIAL_NETWORK && MPlayer.getInstance().isPlaybackStarted())
      {
        VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("network");
        String url = null;
        /*-
        if (voiceCommandVariableVO.getValueAsString().equalsIgnoreCase("facebook"))
          url = SNShareURL.facebook(MPlayer.getInstance().getStreamURL().toString(), MPlayer.getInstance().getIcyStreamTitle());
        else if (voiceCommandVariableVO.getValueAsString().equalsIgnoreCase("twitter"))
          url = SNShareURL.twitter(MPlayer.getInstance().getStreamURL().toString(), MPlayer.getInstance().getIcyStreamTitle());
        else if (voiceCommandVariableVO.getValueAsString().equalsIgnoreCase("myspace"))
          url = SNShareURL.myspace(MPlayer.getInstance().getStreamURL().toString(), MPlayer.getInstance().getIcyStreamTitle());
        -*/
        if (url != null)
          WebBrowser.openURL(url);
        else
          noCommandFound(voiceCommandResponseVO);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.PLAY && !MPlayer.getInstance().isPlaybackStarted() && MPlayer.getInstance().getCurrentRadioStationItem() != null)
      {
        MPlayer.getInstance().playRadioStationItem(MPlayer.getInstance().getCurrentRadioStationItem());
      }
    else if ((voiceCommandVO.getName() == VoiceCommandAction.PAUSE || voiceCommandVO.getName() == VoiceCommandAction.STOP) && MPlayer.getInstance().isPlaybackStarted())
      {
        MPlayer.getInstance().stopPlayback();
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.MUTE_OFF && MPlayer.getInstance().isPlaybackMuted() && MPlayer.getInstance().isPlaybackStarted())
      {
        MPlayer.getInstance().setMute(false);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.MUTE_ON && !MPlayer.getInstance().isPlaybackMuted() && MPlayer.getInstance().isPlaybackStarted())
      {
        MPlayer.getInstance().setMute(true);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.VOLUME_UP && MPlayer.getInstance().isPlaybackStarted())
      {
        MPlayer.getInstance().setVolumeUp();
        if (MPlayer.getInstance().isPlaybackStarted())
          mapViewPanel.showTopBarNotification("Volume " + MPlayer.getInstance().getPlaybackVolume() + "%", false);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.VOLUME_DOWN && MPlayer.getInstance().isPlaybackStarted())
      {
        MPlayer.getInstance().setVolumeDown();
        mapViewPanel.showTopBarNotification("Volume " + MPlayer.getInstance().getPlaybackVolume() + "%", false);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.VOLUME_LEVEL && MPlayer.getInstance().isPlaybackStarted())
      {
        VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("Level");
        if (voiceCommandVariableVO != null)
          try
            {
              int volume = Integer.parseInt("" + voiceCommandVariableVO.getValue()) * 10;
              MPlayer.getInstance().setVolume(volume);
              mapViewPanel.showTopBarNotification("Volume " + MPlayer.getInstance().getPlaybackVolume() + "%", false);
            }
          catch (NumberFormatException numberFormatException)
            {
              Debug.displayStack(mapViewPanel, numberFormatException);
            }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.LIST_STATIONS_BY_GENRE)
      {
        try
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("genre");
            if (voiceCommandVariableVO == null)
              {
                noCommandFound(voiceCommandResponseVO);
                return;
              }
            mapViewPanel.showWindow(new RadioStationListWindow(voiceCommandVariableVO.getValueAsString()));
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.PLAY_STATION_CATCH)
      {
        try
          {
            mapViewPanel.showWindow(new RadioStationGenreListWindow());
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.PLAY_STATION_4_GLOBAL_STATION_NAME || voiceCommandVO.getName() == VoiceCommandAction.PLAY_STATION)
      {
        final VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("details_uri");
        new Thread(new Runnable()
        {
          public void run()
          {
            try
              {
                MPlayer.getInstance().playRadioStationItem(API.getInstance().getItemVO(voiceCommandVariableVO.getValueAsString()));
              }
            catch (Exception exception)
              {
                Debug.displayStack(this, exception);
              }
          }
        }).start();
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.PLAY_RANDOM_STATION)
      {
        try
          {
            GeoBoundsItemsVO geoBoundsItemsVO = API.getInstance().getMedia().search("random", Session.getInstance().getMediaSource(), 0, 0);
            if (geoBoundsItemsVO.getItems().length > 0)
              {
                try
                  {
                    if (MPlayer.getInstance().playRadioStationItem(geoBoundsItemsVO.getItems()[0]))
                      {
                        if (!(mapViewPanel.getLastInStack() instanceof MediaControllerWindow))
                          {
                            mapViewPanel.discardAllWindows();
                            mapViewPanel.showWindow(new MediaControllerWindow());
                          }
                      }
                  }
                catch (Exception exception)
                  {
                    Debug.displayStack(mapViewPanel, exception);
                  }
              }
            else
              {
                mapViewPanel.showTopBarNotification("No results found", true, "notification-media-no-search-result.wav");
              }
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.SHOW_STATIONS_IN_THE_PLACE_4_IN_A_LOCATION)
      {
        VoiceCommandVariableVO voiceCommandVariableVOCity = voiceCommandVO.getVariable("City");
        VoiceCommandVariableVO voiceCommandVariableVOState = voiceCommandVO.getVariable("State");
        if (voiceCommandVariableVOCity != null)
          {
            try
              {
                GeoBoundsItemsVO geoBoundsItemsVO = API.getInstance().getMedia().search("", Session.getInstance().getMediaSource(), voiceCommandVariableVOCity.getValue() + (voiceCommandVariableVOState != null ? "," + voiceCommandVariableVOState.getValue() : ""), 0, 0);
                if (geoBoundsItemsVO.getItems().length > 0)
                  {
                    mapViewPanel.setItemVOsISR(geoBoundsItemsVO.getItems());
                    mapViewPanel.unsetLockToGPS();
                    mapViewPanel.buildMarkers();
                    mapViewPanel.setZoomToBoundingBoxAndCenter(geoBoundsItemsVO.getGeoBounds());
                    AudioUtils.play("notification-media-successful-search-result.wav");
                  }
                else
                  {
                    mapViewPanel.showTopBarNotification("No results found", true, "notification-media-no-search-result.wav");
                  }
              }
            catch (Exception exception)
              {
                Debug.displayStack(mapViewPanel, exception);
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.SHOW_STATIONS_IN_THE_PLACE_4_NEARBY)
      {
        try
          {
            GeoBoundsItemsVO geoBoundsItemsVO = API.getInstance().getMedia().search("", Session.getInstance().getMediaSource(), GeoUtil.getMapBounds(mapViewPanel), 0, 0);
            if (geoBoundsItemsVO.getItems().length > 0)
              {
                mapViewPanel.setItemVOsISR(geoBoundsItemsVO.getItems());
                mapViewPanel.unsetLockToGPS();
                mapViewPanel.buildMarkers();
                AudioUtils.play("notification-media-successful-search-result.wav");
              }
            else
              {
                mapViewPanel.showTopBarNotification("No results found", true, "notification-media-no-search-result.wav");
              }
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.SEND_STATION_TO_CONTACT)
      {
        if (Session.getInstance().getFriendList() == null)
          {
            mapViewPanel.showWindow(new WarningWindow("Contact List Loading", "Contact list is still loading. Please try to open contact list later.", 30, "contact-list-still-loading-warning.wav"));
          }
        else if (Session.getInstance().getFriendList().length == 0)
          {
            mapViewPanel.showWindow(new WarningWindow("No Contacts", "You have no contacts.", 30, "contact-list-no-contacts-warning.wav"));
          }
        else
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("contact");
            ItemVO contactItemVO = null;
            for (int index = 0; index < Session.getInstance().getFriendList().length && contactItemVO == null; index++)
              if (Session.getInstance().getFriendList()[index].getName().equalsIgnoreCase(voiceCommandVariableVO.getValueAsString()))
                contactItemVO = Session.getInstance().getFriendList()[index];
            if (contactItemVO != null)
              {
                if (API.getInstance().getSocial().canMessageSend(contactItemVO.getService()))
                  mapViewPanel.showWindow(new MessageSendWindow(contactItemVO, MPlayer.getInstance().getCurrentRadioStationItem(), null));
                else
                  mapViewPanel.showWindow(new WarningWindow("Cannot send message", "The service \"" + contactItemVO.getService() + "\" does not let you send private messages to \"" + contactItemVO.getName() + "\". Please select another contact.", 30, "send-message-contact-list-can-not-send.wav"));
              }
            else
              {
                noCommandFound(voiceCommandResponseVO);
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.CATEGORY_OR_NATIONAL_BRAND_WITH_ACTION_IN_OPTIONAL_LOCATION_4_NEAR_DESTINATION && mapViewPanel.getRouteVO() != null)
      {
        VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("Category");
        if (voiceCommandVariableVO != null)
          {
            try
              {
                mapViewPanel.setMapCenter(mapViewPanel.getRouteVO().getManuvers()[mapViewPanel.getRouteVO().getManuvers().length - 1].getGeoPosition());
                mapViewPanel.setZoom(1);
                GeoBoundsItemsVO geoBoundsItemsVO = API.getInstance().getPOI().search(voiceCommandVariableVO.getValueAsString(), Configuration.getInstance().getProperty("poi-provider-for-category-search", "all"), GeoUtil.getMapBounds(mapViewPanel), 0, 0);
                if (geoBoundsItemsVO.getItems().length > 0)
                  {
                    mapViewPanel.setItemVOsPOI(geoBoundsItemsVO.getItems(), voiceCommandVariableVO.getValueAsString());
                    mapViewPanel.unsetLockToGPS();
                    mapViewPanel.buildMarkers();
                    AudioUtils.play("notification-poi-successful-search-result.wav");
                  }
                else
                  {
                    mapViewPanel.showTopBarNotification("No results found", true, "notification-poi-no-search-result.wav");
                  }
              }
            catch (Exception exception)
              {
                Debug.displayStack(mapViewPanel, exception);
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.CATEGORY_OR_NATIONAL_BRAND_WITH_ACTION_IN_OPTIONAL_LOCATION_4_NEARBY)
      {
        VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("Category");
        if (voiceCommandVariableVO != null)
          {
            try
              {
                GeoBoundsItemsVO geoBoundsItemsVO = API.getInstance().getPOI().search(voiceCommandVariableVO.getValueAsString(), Configuration.getInstance().getProperty("poi-provider-for-category-search", "all"), GeoUtil.getMapBounds(mapViewPanel), 0, 0);
                if (geoBoundsItemsVO.getItems().length > 0)
                  {
                    mapViewPanel.setItemVOsPOI(geoBoundsItemsVO.getItems(), voiceCommandVariableVO.getValueAsString());
                    mapViewPanel.unsetLockToGPS();
                    mapViewPanel.buildMarkers();
                    AudioUtils.play("notification-poi-successful-search-result.wav");
                  }
                else
                  {
                    mapViewPanel.showTopBarNotification("No results found", true, "notification-poi-no-search-result.wav");
                  }
              }
            catch (Exception exception)
              {
                Debug.displayStack(mapViewPanel, exception);
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.CATEGORY_OR_NATIONAL_BRAND_WITH_ACTION_IN_OPTIONAL_LOCATION_4_IN_A_LOCATION)
      {
        VoiceCommandVariableVO voiceCommandVariableVOName = voiceCommandVO.getVariable("Category");
        VoiceCommandVariableVO voiceCommandVariableVOCity = voiceCommandVO.getVariable("City");
        VoiceCommandVariableVO voiceCommandVariableVOState = voiceCommandVO.getVariable("State");
        if (voiceCommandVariableVOName != null && voiceCommandVariableVOCity != null)
          {
            try
              {
                GeoBoundsItemsVO geoBoundsItemsVO = API.getInstance().getPOI().search(voiceCommandVariableVOName.getValueAsString(), Configuration.getInstance().getProperty("poi-provider-for-category-search", "all"), voiceCommandVariableVOCity.getValue() + (voiceCommandVariableVOState != null ? "," + voiceCommandVariableVOState.getValue() : ""), 0, 0);
                if (geoBoundsItemsVO.getItems().length > 0)
                  {
                    mapViewPanel.setItemVOsPOI(geoBoundsItemsVO.getItems(), voiceCommandVariableVOName.getValueAsString());
                    mapViewPanel.unsetLockToGPS();
                    mapViewPanel.buildMarkers();
                    mapViewPanel.setZoomToBoundingBoxAndCenter(geoBoundsItemsVO.getGeoBounds());
                    AudioUtils.play("notification-poi-successful-search-result.wav");
                  }
                else
                  {
                    mapViewPanel.showTopBarNotification("No results found", true, "notification-poi-no-search-result.wav");
                  }
              }
            catch (Exception exception)
              {
                Debug.displayStack(mapViewPanel, exception);
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.SEND_PRIVATE_MESSAGE || voiceCommandVO.getName() == VoiceCommandAction.SEND_PRIVATE_MESSAGE_VIA_NETWORK || voiceCommandVO.getName() == VoiceCommandAction.SEND_PUBLIC_MESSAGE_VIA_NETWORK)
      {
        if (Session.getInstance().getFriendList() == null)
          {
            mapViewPanel.showWindow(new WarningWindow("Contact List Loading", "Contact list is still loading. Please try to open contact list later.", 30, "contact-list-still-loading-warning.wav"));
          }
        else if (Session.getInstance().getFriendList().length == 0)
          {
            mapViewPanel.showWindow(new WarningWindow("No Contacts", "You have no contacts.", 30, "contact-list-no-contacts-warning.wav"));
          }
        else
          {
            boolean isPublic = voiceCommandVO.getName() == VoiceCommandAction.SEND_PUBLIC_MESSAGE_VIA_NETWORK;
            VoiceCommandVariableVO voiceCommandVariableVOContact = voiceCommandVO.getVariable("contact");
            VoiceCommandVariableVO voiceCommandVariableVONetwork = voiceCommandVO.getVariable("network");
            ItemVO contactItemVO = null;
            for (int index = 0; index < Session.getInstance().getFriendList().length && contactItemVO == null; index++)
              if ((Session.getInstance().getFriendList()[index].getName().equalsIgnoreCase(voiceCommandVariableVOContact.getValueAsString()) && voiceCommandVariableVONetwork == null) || (Session.getInstance().getFriendList()[index].getName().equalsIgnoreCase(voiceCommandVariableVOContact.getValueAsString()) && voiceCommandVariableVONetwork != null && Session.getInstance().getFriendList()[index].getService().equalsIgnoreCase(voiceCommandVariableVONetwork.getValueAsString())))
                contactItemVO = Session.getInstance().getFriendList()[index];
            if (contactItemVO != null)
              {
                if (isPublic ? API.getInstance().getSocial().canMessageSendPublic(contactItemVO.getService()) : API.getInstance().getSocial().canMessageSend(contactItemVO.getService()))
                  mapViewPanel.showWindow(new MessageSendWindow(contactItemVO, null, voiceCommandVariableVONetwork == null ? null : voiceCommandVariableVONetwork.getValueAsString(), isPublic));
                else
                  mapViewPanel.showWindow(new WarningWindow("Cannot send message", "The service \"" + contactItemVO.getService() + "\" does not let you send public messages to \"" + contactItemVO.getName() + "\". Please select another contact.", 30, "send-message-contact-list-can-not-send.wav"));
              }
            else
              {
                noCommandFound(voiceCommandResponseVO);
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_AT_LIST_NUMBER)
      {
        if (Session.getInstance().getFriendList() == null)
          {
            mapViewPanel.showWindow(new WarningWindow("Contact List Loading", "Contact list is still loading. Please try to open contact list later.", 30, "contact-list-still-loading-warning.wav"));
          }
        else if (Session.getInstance().getFriendList().length == 0)
          {
            mapViewPanel.showWindow(new WarningWindow("No Contacts", "You have no contacts.", 30, "contact-list-no-contacts-warning.wav"));
          }
        else
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("contact");
            ItemVO contactItemVO = null;
            for (int index = 0; index < Session.getInstance().getFriendList().length && contactItemVO == null; index++)
              if (Session.getInstance().getFriendList()[index].getName().equalsIgnoreCase(voiceCommandVariableVO.getValueAsString()))
                contactItemVO = Session.getInstance().getFriendList()[index];
            if (contactItemVO != null)
              {
                if (API.getInstance().getSocial().canMessageSend(contactItemVO.getService()))
                  {
                    voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_number");
                    ItemVO itemVO = mapViewPanel.findMarkerByNumber(Integer.parseInt("" + voiceCommandVariableVO.getValue()));
                    if (itemVO == null)
                      {
                        noCommandFound(voiceCommandResponseVO);
                        return;
                      }
                    mapViewPanel.discardAndShowWindow(new VoiceNoteSendWindow(contactItemVO, itemVO, null));
                  }
                else
                  {
                    mapViewPanel.showWindow(new WarningWindow("Cannot send message", "The service \"" + contactItemVO.getService() + "\" does not let you send private messages to \"" + contactItemVO.getName() + "\". Please select another contact.", 30, "send-message-contact-list-can-not-send.wav"));
                  }
              }
            else
              {
                noCommandFound(voiceCommandResponseVO);
                return;
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_AT_LIST_ITEM)
      {
        if (Session.getInstance().getFriendList() == null)
          {
            mapViewPanel.showWindow(new WarningWindow("Contact List Loading", "Contact list is still loading. Please try to open contact list later.", 30, "contact-list-still-loading-warning.wav"));
          }
        else if (Session.getInstance().getFriendList().length == 0)
          {
            mapViewPanel.showWindow(new WarningWindow("No Contacts", "You have no contacts.", 30, "contact-list-no-contacts-warning.wav"));
          }
        else
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("contact");
            ItemVO contactItemVO = null;
            for (int index = 0; index < Session.getInstance().getFriendList().length && contactItemVO == null; index++)
              if (Session.getInstance().getFriendList()[index].getName().equalsIgnoreCase(voiceCommandVariableVO.getValueAsString()))
                contactItemVO = Session.getInstance().getFriendList()[index];
            if (contactItemVO != null)
              {
                if (API.getInstance().getSocial().canMessageSend(contactItemVO.getService()))
                  {
                    voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_name");
                    ItemVO itemVO = mapViewPanel.findMarkerByName(voiceCommandVariableVO.getValueAsString());
                    if (itemVO == null)
                      {
                        noCommandFound(voiceCommandResponseVO);
                        return;
                      }
                    mapViewPanel.discardAndShowWindow(new VoiceNoteSendWindow(contactItemVO, itemVO, null));
                  }
                else
                  {
                    mapViewPanel.showWindow(new WarningWindow("Cannot send message", "The service \"" + contactItemVO.getService() + "\" does not let you send private messages to \"" + contactItemVO.getName() + "\". Please select another contact.", 30, "send-message-contact-list-can-not-send.wav"));
                  }
              }
            else
              {
                noCommandFound(voiceCommandResponseVO);
                return;
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_CATCH)
      {
        if (Session.getInstance().getFriendList() == null)
          {
            mapViewPanel.showWindow(new WarningWindow("Contact List Loading", "Contact list is still loading. Please try to open contact list later.", 30, "contact-list-still-loading-warning.wav"));
          }
        else if (Session.getInstance().getFriendList().length == 0)
          {
            mapViewPanel.showWindow(new WarningWindow("No Contacts", "You have no contacts.", 30, "contact-list-no-contacts-warning.wav"));
          }
        else
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("contact");
            ItemVO contactItemVO = null;
            for (int index = 0; index < Session.getInstance().getFriendList().length && contactItemVO == null; index++)
              if (Session.getInstance().getFriendList()[index].getName().equalsIgnoreCase(voiceCommandVariableVO.getValueAsString()))
                contactItemVO = Session.getInstance().getFriendList()[index];
            if (contactItemVO != null)
              {
                if (API.getInstance().getSocial().canMessageSend(contactItemVO.getService()))
                  mapViewPanel.discardAndShowWindow(new VoiceNoteSendWindow(contactItemVO, null, null));
                else
                  mapViewPanel.showWindow(new WarningWindow("Cannot send message", "The service \"" + contactItemVO.getService() + "\" does not let you send private messages to \"" + contactItemVO.getName() + "\". Please select another contact.", 30, "send-message-contact-list-can-not-send.wav"));
              }
            else
              {
                noCommandFound(voiceCommandResponseVO);
                return;
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_AT_LOCATION_4_NEAR_DESTINATION && mapViewPanel.getRouteVO() != null)
      {
        if (Session.getInstance().getFriendList() == null)
          {
            mapViewPanel.showWindow(new WarningWindow("Contact List Loading", "Contact list is still loading. Please try to open contact list later.", 30, "contact-list-still-loading-warning.wav"));
          }
        else if (Session.getInstance().getFriendList().length == 0)
          {
            mapViewPanel.showWindow(new WarningWindow("No Contacts", "You have no contacts.", 30, "contact-list-no-contacts-warning.wav"));
          }
        else
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("contact");
            ItemVO contactItemVO = null;
            for (int index = 0; index < Session.getInstance().getFriendList().length && contactItemVO == null; index++)
              if (Session.getInstance().getFriendList()[index].getName().equalsIgnoreCase(voiceCommandVariableVO.getValueAsString()))
                contactItemVO = Session.getInstance().getFriendList()[index];
            if (contactItemVO != null)
              {
                if (API.getInstance().getSocial().canMessageSend(contactItemVO.getService()))
                  {
                    mapViewPanel.setMapCenter(mapViewPanel.getRouteVO().getManuvers()[mapViewPanel.getRouteVO().getManuvers().length - 1].getGeoPosition());
                    mapViewPanel.discardAndShowWindow(new VoiceNoteSendWindow(contactItemVO, null, mapViewPanel.getMapCenterAsPosition()));
                  }
                else
                  {
                    mapViewPanel.showWindow(new WarningWindow("Cannot send message", "The service \"" + contactItemVO.getService() + "\" does not let you send private messages to \"" + contactItemVO.getName() + "\". Please select another contact.", 30, "send-message-contact-list-can-not-send.wav"));
                  }
              }
            else
              {
                noCommandFound(voiceCommandResponseVO);
                return;
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_AT_LOCATION_4_NEARBY)
      {
        if (Session.getInstance().getFriendList() == null)
          {
            mapViewPanel.showWindow(new WarningWindow("Contact List Loading", "Contact list is still loading. Please try to open contact list later.", 30, "contact-list-still-loading-warning.wav"));
          }
        else if (Session.getInstance().getFriendList().length == 0)
          {
            mapViewPanel.showWindow(new WarningWindow("No Contacts", "You have no contacts.", 30, "contact-list-no-contacts-warning.wav"));
          }
        else
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("contact");
            ItemVO contactItemVO = null;
            for (int index = 0; index < Session.getInstance().getFriendList().length && contactItemVO == null; index++)
              if (Session.getInstance().getFriendList()[index].getName().equalsIgnoreCase(voiceCommandVariableVO.getValueAsString()))
                contactItemVO = Session.getInstance().getFriendList()[index];
            if (contactItemVO != null)
              {
                if (API.getInstance().getSocial().canMessageSend(contactItemVO.getService()))
                  mapViewPanel.discardAndShowWindow(new VoiceNoteSendWindow(contactItemVO, null, mapViewPanel.getMapCenterAsPosition()));
                else
                  mapViewPanel.showWindow(new WarningWindow("Cannot send message", "The service \"" + contactItemVO.getService() + "\" does not let you send private messages to \"" + contactItemVO.getName() + "\". Please select another contact.", 30, "send-message-contact-list-can-not-send.wav"));
              }
            else
              {
                noCommandFound(voiceCommandResponseVO);
                return;
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_AT_LOCATION_4_IN_A_LOCATION)
      {
        if (Session.getInstance().getFriendList() == null)
          {
            mapViewPanel.showWindow(new WarningWindow("Contact List Loading", "Contact list is still loading. Please try to open contact list later.", 30, "contact-list-still-loading-warning.wav"));
          }
        else if (Session.getInstance().getFriendList().length == 0)
          {
            mapViewPanel.showWindow(new WarningWindow("No Contacts", "You have no contacts.", 30, "contact-list-no-contacts-warning.wav"));
          }
        else
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("contact");
            ItemVO contactItemVO = null;
            for (int index = 0; index < Session.getInstance().getFriendList().length && contactItemVO == null; index++)
              if (Session.getInstance().getFriendList()[index].getName().equalsIgnoreCase(voiceCommandVariableVO.getValueAsString()))
                contactItemVO = Session.getInstance().getFriendList()[index];
            if (contactItemVO != null)
              {
                if (API.getInstance().getSocial().canMessageSend(contactItemVO.getService()))
                  {
                    VoiceCommandVariableVO voiceCommandVariableVOCity = voiceCommandVO.getVariable("City");
                    VoiceCommandVariableVO voiceCommandVariableVOState = voiceCommandVO.getVariable("State");
                    try
                      {
                        mapViewPanel.setMapCenter(API.getInstance().getMap().geocode(voiceCommandVariableVOCity.getValueAsString(), voiceCommandVariableVOState.getValueAsString()));
                      }
                    catch (Exception exception)
                      {
                        Debug.displayStack(mapViewPanel, exception);
                      }
                    mapViewPanel.discardAndShowWindow(new VoiceNoteSendWindow(contactItemVO, null, mapViewPanel.getMapCenterAsPosition()));
                  }
                else
                  {
                    mapViewPanel.showWindow(new WarningWindow("Cannot send message", "The service \"" + contactItemVO.getService() + "\" does not let you send private messages to \"" + contactItemVO.getName() + "\". Please select another contact.", 30, "send-message-contact-list-can-not-send.wav"));
                  }
              }
            else
              {
                noCommandFound(voiceCommandResponseVO);
                return;
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.SEND_LIST_NUMBER_AS_PRIVATE_MESSAGE)
      {
        if (Session.getInstance().getFriendList() == null)
          {
            mapViewPanel.showWindow(new WarningWindow("Contact List Loading", "Contact list is still loading. Please try to open contact list later.", 30, "contact-list-still-loading-warning.wav"));
          }
        else if (Session.getInstance().getFriendList().length == 0)
          {
            mapViewPanel.showWindow(new WarningWindow("No Contacts", "You have no contacts.", 30, "contact-list-no-contacts-warning.wav"));
          }
        else
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("contact");
            ItemVO contactItemVO = null;
            for (int index = 0; index < Session.getInstance().getFriendList().length && contactItemVO == null; index++)
              if (Session.getInstance().getFriendList()[index].getName().equalsIgnoreCase(voiceCommandVariableVO.getValueAsString()))
                contactItemVO = Session.getInstance().getFriendList()[index];
            if (contactItemVO != null)
              {
                if (API.getInstance().getSocial().canMessageSend(contactItemVO.getService()))
                  {
                    voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_number");
                    ItemVO itemVO = mapViewPanel.findMarkerByNumber(Integer.parseInt("" + voiceCommandVariableVO.getValue()));
                    if (itemVO == null)
                      {
                        noCommandFound(voiceCommandResponseVO);
                        return;
                      }
                    mapViewPanel.discardAndShowWindow(new MessageSendWindow(contactItemVO, itemVO, null));
                  }
                else
                  {
                    mapViewPanel.showWindow(new WarningWindow("Cannot send message", "The service \"" + contactItemVO.getService() + "\" does not let you send private messages to \"" + contactItemVO.getName() + "\". Please select another contact.", 30, "send-message-contact-list-can-not-send.wav"));
                  }
              }
            else
              {
                noCommandFound(voiceCommandResponseVO);
                return;
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.SEND_LIST_ITEM_AS_PRIVATE_MESSAGE)
      {
        if (Session.getInstance().getFriendList() == null)
          {
            mapViewPanel.showWindow(new WarningWindow("Contact List Loading", "Contact list is still loading. Please try to open contact list later.", 30, "contact-list-still-loading-warning.wav"));
          }
        else if (Session.getInstance().getFriendList().length == 0)
          {
            mapViewPanel.showWindow(new WarningWindow("No Contacts", "You have no contacts.", 30, "contact-list-no-contacts-warning.wav"));
          }
        else
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("contact");
            ItemVO contactItemVO = null;
            for (int index = 0; index < Session.getInstance().getFriendList().length && contactItemVO == null; index++)
              if (Session.getInstance().getFriendList()[index].getName().equalsIgnoreCase(voiceCommandVariableVO.getValueAsString()))
                contactItemVO = Session.getInstance().getFriendList()[index];
            if (contactItemVO != null)
              {
                if (API.getInstance().getSocial().canMessageSend(contactItemVO.getService()))
                  {
                    voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_name");
                    ItemVO itemVO = mapViewPanel.findMarkerByName(voiceCommandVariableVO.getValueAsString());
                    if (itemVO == null)
                      {
                        noCommandFound(voiceCommandResponseVO);
                        return;
                      }
                    mapViewPanel.discardAndShowWindow(new MessageSendWindow(contactItemVO, itemVO, null));
                  }
                else
                  {
                    mapViewPanel.showWindow(new WarningWindow("Cannot send message", "The service \"" + contactItemVO.getService() + "\" does not let you send private messages to \"" + contactItemVO.getName() + "\". Please select another contact.", 30, "send-message-contact-list-can-not-send.wav"));
                  }
              }
            else
              {
                noCommandFound(voiceCommandResponseVO);
                return;
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.FIND_CONTACT || voiceCommandVO.getName() == VoiceCommandAction.VIEW_STATUS_UPDATES_FOR_CONTACT_VIA_NETWORK)
      {
        if (Session.getInstance().getFriendList() == null)
          {
            mapViewPanel.showWindow(new WarningWindow("Contact List Loading", "Contact list is still loading. Please try to open contact list later.", 30, "contact-list-still-loading-warning.wav"));
          }
        else if (Session.getInstance().getFriendList().length == 0)
          {
            mapViewPanel.showWindow(new WarningWindow("No Contacts", "You have no contacts.", 30, "contact-list-no-contacts-warning.wav"));
          }
        else
          {
            VoiceCommandVariableVO voiceCommandVariableContactVO = voiceCommandVO.getVariable("contact");
            VoiceCommandVariableVO voiceCommandVariableNetworkVO = voiceCommandVO.getVariable("network");
            ItemVO contactItemVO = null;
            for (int index = 0; index < Session.getInstance().getFriendList().length && contactItemVO == null; index++)
              if (Session.getInstance().getFriendList()[index].getName().equalsIgnoreCase(voiceCommandVariableContactVO.getValueAsString()) || (voiceCommandVariableNetworkVO != null && Session.getInstance().getFriendList()[index].getName().equalsIgnoreCase(voiceCommandVariableNetworkVO.getValueAsString())))
                contactItemVO = Session.getInstance().getFriendList()[index];
            if (contactItemVO != null)
              {
                if (contactItemVO.getDisplay() == ItemDisplay.GEO)
                  {
                    if (Settings.getInstance().getDTO().isHiddenContact(contactItemVO.getServiceItemID(), contactItemVO.getService()))
                      {
                        Settings.getInstance().getDTO().showContact(contactItemVO.getServiceItemID(), contactItemVO.getService());
                        mapViewPanel.buildMarkers();
                      }
                    mapViewPanel.setMarkerSelected(contactItemVO.getGlobalItemID());
                    mapViewPanel.unsetLockToGPS();
                    mapViewPanel.setMapCenter(contactItemVO.getPosition());
                    mapViewPanel.setZoom(2);
                  }
                else if (contactItemVO.getDisplay() == ItemDisplay.LIST)
                  {
                    final ItemVO itemVO = contactItemVO;
                    new Thread(new Runnable()
                    {
                      public void run()
                      {
                        try
                          {
                            mapViewPanel.showWindow(new ContactDetailsWindow(itemVO, (InfoContactTypeVO) API.getInstance().getInfoVO(itemVO.getAction("details").getHandlerURI()), true));
                          }
                        catch (Exception exception)
                          {
                            Debug.displayStack(this, exception);
                          }
                      }
                    }).start();
                  }
              }
            else
              {
                noCommandFound(voiceCommandResponseVO);
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.ROUTE_TO_LIST_NUMBER || voiceCommandVO.getName() == VoiceCommandAction.ROUTE_TO_LIST_ITEM || voiceCommandVO.getName() == VoiceCommandAction.ADD_DESTINATION_LIST_NUMBER || voiceCommandVO.getName() == VoiceCommandAction.ADD_DESTINATION_LIST_NUMBER)
      {
        try
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_number");
            if (voiceCommandVariableVO == null)
              voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_id");
            ModalWindow currentModalWindow = mapViewPanel.getLastInStack();
            ItemVO item = null;
            if (currentModalWindow instanceof FavoritesListWindow)
              item = Session.getInstance().getFavorites()[Integer.parseInt("" + voiceCommandVariableVO.getValue()) - 1];
            else
              item = mapViewPanel.findMarkerByNumber(Integer.parseInt("" + voiceCommandVariableVO.getValue()));
            if (item == null)
              {
                noCommandFound(voiceCommandResponseVO);
                return;
              }
            mapViewPanel.addRoute(item);
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.FIND_LIST_ITEM || voiceCommandVO.getName() == VoiceCommandAction.FIND_LIST_NUMBER)
      {
        try
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_number");
            if (voiceCommandVariableVO == null)
              voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_id");
            ItemVO item = mapViewPanel.findMarkerByNumber(((Long) voiceCommandVariableVO.getValue()).intValue());
            if (item == null)
              {
                noCommandFound(voiceCommandResponseVO);
                return;
              }
            mapViewPanel.setMarkerSelected(item.getGlobalItemID());
            mapViewPanel.unsetLockToGPS();
            mapViewPanel.setMapCenter(item.getPosition());
            mapViewPanel.setZoom(2);
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.SAVE_FAVORITE_4_ITEM_NUMBER)
      {
        try
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_number");
            ItemVO item = mapViewPanel.findMarkerByNumber(Integer.parseInt("" + voiceCommandVariableVO.getValue()));
            if (item == null)
              {
                noCommandFound(voiceCommandResponseVO);
                return;
              }
            if (item.getType() == ItemType.POI || item.getType() == ItemType.RADIO)
              {
                API.getInstance().getUser().favoriteSet(item);
                mapViewPanel.discardAllWindows();
                mapViewPanel.showWindow(new FavoritesListWindow());
              }
            else
              {
                mapViewPanel.showWindow(new WarningWindow("Cannot Save As Favorite", "You cannot save \"" + item.getName() + "\" as a favorite", 30, "favorite-save-cannot-save-warning.wav"));
              }
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.SAVE_FAVORITE_4_ITEM_NAME)
      {
        try
          {
            VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_name");
            ItemVO item = mapViewPanel.findMarkerByName((String) voiceCommandVariableVO.getValue());
            if (item == null)
              {
                noCommandFound(voiceCommandResponseVO);
                return;
              }
            if (item.getType() == ItemType.POI || item.getType() == ItemType.RADIO)
              {
                API.getInstance().getUser().favoriteSet(item);
                mapViewPanel.discardAllWindows();
                mapViewPanel.showWindow(new FavoritesListWindow());
              }
            else
              {
                mapViewPanel.showWindow(new WarningWindow("Cannot Save As Favorite", "You cannot save \"" + item.getName() + "\" as a favorite", 30, "favorite-save-cannot-save-warning.wav"));
              }
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.SEND_MESSAGE_CATCH)
      {
        mapViewPanel.discardAllWindows();
        mapViewPanel.showWindow(new SendToContactListWindow(null, MessageType.TEXT));
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.VOICE_MEMO_CATCH)
      {
        mapViewPanel.discardAllWindows();
        mapViewPanel.showWindow(new SendToContactListWindow(null, MessageType.VOICE));
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.SHOW_MESSAGES_CATCH)
      {
        if (Session.getInstance().messageCount() == -1)
          {
            mapViewPanel.showWindow(new WarningWindow("Message List Loading", "Message list is still loading. Please try to open message list later.", 30, "message-list-still-loading-warning.wav"));
          }
        else if (Session.getInstance().messageCount() == 0)
          {
            mapViewPanel.showWindow(new WarningWindow("No Messages", "You have no messages.", 30, "message-list-no-messages-warning.wav"));
          }
        else
          {
            mapViewPanel.discardAllWindows();
            mapViewPanel.showWindow(new MessageListWindow());
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.OPEN_MESSAGE_FROM_CONTACT_NAME || voiceCommandVO.getName() == VoiceCommandAction.OPEN_MESSAGE_FROM_CONTACT_NUMBER)
      {
        String contactName = voiceCommandVO.getVariable("contact").getValueAsString();
        int offset = 1;
        if (voiceCommandVO.getName() == VoiceCommandAction.OPEN_MESSAGE_FROM_CONTACT_NUMBER)
          offset = Integer.parseInt(voiceCommandVO.getVariable("list_item_number").getValueAsString());
        int count = 0;
        ItemVO[] messages = Session.getInstance().getMessages();
        for (int index = 0; index < messages.length; index++)
          {
            ItemVO itemVO = messages[index];
            InfoMessageGetTypeVO infoVO = (InfoMessageGetTypeVO) itemVO.getInfo();
            if (infoVO.getFrom().equalsIgnoreCase(contactName))
              count++;
            if (count == offset)
              {
                if (infoVO.getMessageType() == MessageType.TEXT)
                  mapViewPanel.showWindow(new MessageOpenWindow(itemVO));
                else if (infoVO.getMessageType() == MessageType.VOICE)
                  mapViewPanel.showWindow(new VoiceNoteOpenWindow(itemVO));
                return;
              }
          }
        mapViewPanel.showTopBarNotification("There is no messages from " + contactName, true, "notification-message-no-from-contact.wav");
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.GET_FAVORITES)
      {
        if (!Session.getInstance().hasFavorites())
          {
            mapViewPanel.showWindow(new WarningWindow("No Favorites", "You do not have favorites yet. Please add one.", 30, "favorites-list-no-favorites-warning.wav"));
          }
        else
          {
            mapViewPanel.discardAllWindows();
            mapViewPanel.showWindow(new FavoritesListWindow());
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.FOLLOW_GPS)
      {
        mapViewPanel.setLockToGPS(true);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.SET_CURRENT_LOCATION)
      {
        mapViewPanel.setLocationToMapCenter();
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.STATUS_UPDATE_VIA_NETWORK)
      {
        VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("network");
        if (voiceCommandVariableVO != null)
          {
            String service = voiceCommandVariableVO.getValueAsString().toLowerCase();
            if (API.getInstance().getSocial().canMessageSend(service))
              mapViewPanel.showWindow(new MessageSendWindow(null, null, service));
            else
              mapViewPanel.showWindow(new WarningWindow("Cannot update your status", "The service \"" + service + "\" does not let you to update your status. Please select another service.", 30, "send-message-contact-list-can-not-send.wav"));
          }
        else
          {
            noCommandFound(voiceCommandResponseVO);
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.STATUS_UPDATE_VIA_NETWORK_WITH_LIST_NUMBER)
      {
        VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("network");
        if (voiceCommandVariableVO != null)
          {
            String service = voiceCommandVariableVO.getValueAsString().toLowerCase();
            if (API.getInstance().getSocial().canMessageSend(service))
              {
                voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_number");
                ItemVO itemVO = mapViewPanel.findMarkerByNumber(Integer.parseInt("" + voiceCommandVariableVO.getValue()));
                if (itemVO == null)
                  {
                    noCommandFound(voiceCommandResponseVO);
                    return;
                  }
                mapViewPanel.discardAndShowWindow(new MessageSendWindow(null, itemVO, service));
              }
            else
              {
                mapViewPanel.showWindow(new WarningWindow("Cannot update your status", "The service \"" + service + "\" does not let you to update your status. Please select another service.", 30, "send-message-contact-list-can-not-send.wav"));
              }
          }
        else
          {
            noCommandFound(voiceCommandResponseVO);
            return;
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.STATUS_UPDATE_VIA_NETWORK_WITH_LIST_ITEM)
      {
        VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("network");
        if (voiceCommandVariableVO != null)
          {
            String service = voiceCommandVariableVO.getValueAsString().toLowerCase();
            if (API.getInstance().getSocial().canMessageSend(service))
              {
                voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_name");
                ItemVO itemVO = mapViewPanel.findMarkerByName(voiceCommandVariableVO.getValueAsString());
                if (itemVO == null)
                  {
                    noCommandFound(voiceCommandResponseVO);
                    return;
                  }
                mapViewPanel.discardAndShowWindow(new MessageSendWindow(null, itemVO, service));
              }
            else
              {
                mapViewPanel.showWindow(new WarningWindow("Cannot update your status", "The service \"" + service + "\" does not let you to update your status. Please select another service.", 30, "send-message-contact-list-can-not-send.wav"));
              }
          }
        else
          {
            noCommandFound(voiceCommandResponseVO);
            return;
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.ENTER_ADDRESS)
      {
        mapViewPanel.discardAllWindows();
        mapViewPanel.showWindow(new UserAddressWindow());
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.BUSINESS_SEARCH_INTENT || voiceCommandVO.getName() == VoiceCommandAction.BUSINESS_SEARCH_INTENT_4_NEARBY_LOCATION || voiceCommandVO.getName() == VoiceCommandAction.BUSINESS_SEARCH_INTENT_4_IN_A_LOCATION || (voiceCommandVO.getName() == VoiceCommandAction.BUSINESS_SEARCH_INTENT_4_NEAR_DESTINATION && mapViewPanel.getRouteVO() != null))
      {
        VoiceCommandVariableVO voiceCommandVariableVOCity = voiceCommandVO.getVariable("City");
        VoiceCommandVariableVO voiceCommandVariableVOState = voiceCommandVO.getVariable("State");
        mapViewPanel.discardAllWindows();
        mapViewPanel.showWindow(new CompanyNameWindow(voiceCommandVO.getName(), (voiceCommandVariableVOCity != null ? voiceCommandVO.getVariable("City").getValueAsString() : null), (voiceCommandVariableVOState != null ? voiceCommandVO.getVariable("State").getValueAsString() : null)));
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.DELETE_FAVORITE_4_ITEM_NAME)
      {
        VoiceCommandVariableVO voiceCommandVariableVO = voiceCommandVO.getVariable("list_item_id");
        try
          {
            API.getInstance().getUser().favoriteDelete(voiceCommandVariableVO.getValueAsString());
            mapViewPanel.showTopBarNotification("Favorite deleted successfully.", true, "notification-favorite-deleted-successfully.wav");
          }
        catch (Exception exception)
          {
            noCommandFound(voiceCommandResponseVO);
            return;
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.CANCEL_ROUTING && mapViewPanel.getRouteVO() != null)
      {
        mapViewPanel.clearRoute();
        ModalWindow currentModalWindow = mapViewPanel.getLastInStack();
        if (currentModalWindow instanceof RouteListWindow)
          mapViewPanel.discardWindow();
        else
          mapViewPanel.repaint();
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.CLEAR_NAMED_LAYER)
      {
        VoiceCommandVariableVO voiceCommandVariableVOLayerName = voiceCommandVO.getVariable("layer_name");
        if (voiceCommandVariableVOLayerName == null)
          {
            noCommandFound(voiceCommandResponseVO);
            return;
          }
        else if (voiceCommandVariableVOLayerName.getValueAsString().equalsIgnoreCase("route") && mapViewPanel.getRouteVO() != null)
          {
            mapViewPanel.clearRoute();
            ModalWindow currentModalWindow = mapViewPanel.getLastInStack();
            if (currentModalWindow instanceof RouteListWindow)
              mapViewPanel.discardWindow();
            else
              mapViewPanel.repaint();
          }
        else
          {
            noCommandFound(voiceCommandResponseVO);
            return;
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.CHANGE_SOURCE_NAMED)
      {
        VoiceCommandVariableVO voiceCommandVariableVOSource = voiceCommandVO.getVariable("source");
        if (voiceCommandVariableVOSource == null)
          {
            noCommandFound(voiceCommandResponseVO);
            return;
          }
        else
          {
            ServiceVO serviceVO = API.getInstance().getMedia().getService(voiceCommandVariableVOSource.getValueAsString());
            if (serviceVO == null)
              {
                mapViewPanel.showTopBarNotification("Unable to set media source to " + voiceCommandVariableVOSource.getValueAsString(), true, "notification-media-set-failed.wav");
              }
            else
              {
                Session.getInstance().setMediaSource(voiceCommandVariableVOSource.getValueAsString());
                mapViewPanel.showTopBarNotification("Media source set to " + voiceCommandVariableVOSource.getValueAsString(), true, "notification-favorite-deleted-successfully.wav");
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.GET_WEATHER_4_NEAR_DESTINATION && mapViewPanel.getRouteVO() != null)
      {
        try
          {
            mapViewPanel.showWindow(new WeatherCurrentWindow(API.getInstance().getWeather().current("default", mapViewPanel.getRouteVO().getManuvers()[mapViewPanel.getRouteVO().getManuvers().length - 1].getGeoPosition()), ""));
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.GET_WEATHER_4_NEARBY)
      {
        try
          {
            mapViewPanel.showWindow(new WeatherCurrentWindow(API.getInstance().getWeather().current("default", mapViewPanel.getMapCenterAsPosition()), ""));
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.GET_WEATHER_4_IN_A_LOCATION)
      {
        VoiceCommandVariableVO voiceCommandVariableVOCity = voiceCommandVO.getVariable("City");
        VoiceCommandVariableVO voiceCommandVariableVOState = voiceCommandVO.getVariable("State");
        if (voiceCommandVariableVOCity != null)
          {
            try
              {
                mapViewPanel.showWindow(new WeatherCurrentWindow(API.getInstance().getWeather().current("default", voiceCommandVariableVOCity.getValue() + (voiceCommandVariableVOState != null ? "," + voiceCommandVariableVOState.getValue() : "")), ""));
              }
            catch (Exception exception)
              {
                Debug.displayStack(mapViewPanel, exception);
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.GET_FORECAST_4_NEAR_DESTINATION && mapViewPanel.getRouteVO() != null)
      {
        try
          {
            mapViewPanel.showWindow(new WeatherForecastWindow(API.getInstance().getWeather().forecast("default", mapViewPanel.getRouteVO().getManuvers()[mapViewPanel.getRouteVO().getManuvers().length - 1].getGeoPosition()), ""));
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.GET_FORECAST_4_NEARBY)
      {
        try
          {
            mapViewPanel.showWindow(new WeatherForecastWindow(API.getInstance().getWeather().forecast("default", mapViewPanel.getMapCenterAsPosition()), ""));
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.GET_FORECAST_4_IN_A_LOCATION)
      {
        VoiceCommandVariableVO voiceCommandVariableVOCity = voiceCommandVO.getVariable("City");
        VoiceCommandVariableVO voiceCommandVariableVOState = voiceCommandVO.getVariable("State");
        if (voiceCommandVariableVOCity != null)
          {
            try
              {
                mapViewPanel.showWindow(new WeatherForecastWindow(API.getInstance().getWeather().forecast("default", voiceCommandVariableVOCity.getValue() + (voiceCommandVariableVOState != null ? "," + voiceCommandVariableVOState.getValue() : "")), ""));
              }
            catch (Exception exception)
              {
                Debug.displayStack(mapViewPanel, exception);
              }
          }
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.MAP_ZOOM_4_IN)
      {
        mapViewPanel.zoomIn();
        mapViewPanel.showTopBarNotification("Map zoom is set to " + mapViewPanel.getZoomAsPercent() + "%", true);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.MAP_ZOOM_4_OUT)
      {
        mapViewPanel.zoomOut();
        mapViewPanel.showTopBarNotification("Map zoom is set to " + mapViewPanel.getZoomAsPercent() + "%", true);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.MAP_ZOOM_4_MAX)
      {
        mapViewPanel.setZoomAsPercent(0);
        mapViewPanel.showTopBarNotification("Map zoom is set to " + mapViewPanel.getZoomAsPercent() + "%", true);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.MAP_ZOOM_4_MIN)
      {
        mapViewPanel.setZoomAsPercent(100);
        mapViewPanel.showTopBarNotification("Map zoom is set to " + mapViewPanel.getZoomAsPercent() + "%", true);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.MAP_ZOOM_4_1)
      {
        mapViewPanel.setZoomAsPercent(20);
        mapViewPanel.showTopBarNotification("Map zoom is set to 1", true);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.MAP_ZOOM_4_2)
      {
        mapViewPanel.setZoomAsPercent(40);
        mapViewPanel.showTopBarNotification("Map zoom is set to 2", true);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.MAP_ZOOM_4_3)
      {
        mapViewPanel.setZoomAsPercent(60);
        mapViewPanel.showTopBarNotification("Map zoom is set to 3", true);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.MAP_ZOOM_4_4)
      {
        mapViewPanel.setZoomAsPercent(80);
        mapViewPanel.showTopBarNotification("Map zoom is set to 4", true);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.MAP_ZOOM_4_5)
      {
        mapViewPanel.setZoomAsPercent(100);
        mapViewPanel.showTopBarNotification("Map zoom is set to 5", true);
      }
    else if ((voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_SOUTH) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_SOUTH_FAST) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_DOWN) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_DOWN_FAST))
      {
        mapViewPanel.panMapDown();
      }
    else if ((voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_NORTH) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_NORTH_FAST) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_UP) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_UP_FAST))
      {
        mapViewPanel.panMapUp();
      }
    else if ((voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_EAST) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_EAST_FAST) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_LEFT) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_LEFT_FAST))
      {
        mapViewPanel.panMapLeft();
      }
    else if ((voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_WEST) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_WEST_FAST) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_RIGHT) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_RIGHT_FAST))
      {
        mapViewPanel.panMapRight();
      }
    else if ((voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_SOUTH) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_SOUTH_SLOW) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_DOWN) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_DOWN_SLOW))
      {
        mapViewPanel.panMapDown(false);
      }
    else if ((voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_NORTH) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_NORTH_SLOW) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_UP) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_UP_SLOW))
      {
        mapViewPanel.panMapUp(false);
      }
    else if ((voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_EAST) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_EAST_SLOW) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_LEFT) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_LEFT_SLOW))
      {
        mapViewPanel.panMapLeft(false);
      }
    else if ((voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_WEST) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_WEST_SLOW) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_RIGHT) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_RIGHT_SLOW))
      {
        mapViewPanel.panMapRight(false);
      }
    else if ((voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_WEST) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_WEST_SLOW) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_RIGHT) || (voiceCommandVO.getName() == VoiceCommandAction.MAP_PAN_4_RIGHT_SLOW))
      {
        mapViewPanel.panMapRight(false);
      }
    else if (voiceCommandVO.getName() == VoiceCommandAction.LOCATION_HIDE || voiceCommandVO.getName() == VoiceCommandAction.LOCATION_SHOW)
      {
        InfoUserTypeVO newInfoUserTypeVO = null;
        try
          {
            newInfoUserTypeVO = Session.getInstance().getUserInfo().clone();
          }
        catch (CloneNotSupportedException cloneNotSupportedException)
          {
            newInfoUserTypeVO = new InfoUserTypeVO();
          }
        try
          {
            newInfoUserTypeVO.setLocationIsVisible(voiceCommandVO.getName() == VoiceCommandAction.LOCATION_SHOW);
            API.getInstance().getUser().infoSet(Session.getInstance().getUserInfo(), newInfoUserTypeVO);
            Session.getInstance().setUserInfo(newInfoUserTypeVO);
            mapViewPanel.showTopBarNotification("Location sharing is " + (voiceCommandVO.getName() == VoiceCommandAction.LOCATION_SHOW ? "en" : "dis") + "abled.", true);
          }
        catch (Exception exception)
          {
            Debug.displayStack(mapViewPanel, exception);
          }
      }
    else
      {
        ModalWindow currentModalWindow = mapViewPanel.getLastInStack();
        if (currentModalWindow == null || !currentModalWindow.processVoiceCommand(voiceCommandVO))
          {
            noCommandFound(voiceCommandResponseVO);
            return;
          }
      }
  }

  private static void noCommandFound(VoiceCommandResponseVO voiceCommandResponseVO)
  {
    try
      {
        TTSPlayer.getInstance().loadPlayfile(voiceCommandResponseVO.getVariable("no_command_found_TTS_uri").getValue().toString());
      }
    catch (Exception exception)
      {
        Debug.displayStack(null, exception);
      }
  }
}
