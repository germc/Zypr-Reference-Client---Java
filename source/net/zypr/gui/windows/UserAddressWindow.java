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

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.zypr.api.API;
import net.zypr.api.Session;
import net.zypr.api.enums.ItemDisplay;
import net.zypr.api.enums.ItemType;
import net.zypr.api.enums.VoiceCommandAction;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.exceptions.APIServerErrorException;
import net.zypr.api.vo.AddressVO;
import net.zypr.api.vo.GeoPositionVO;
import net.zypr.api.vo.InfoPOITypeVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.gui.Settings;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.panels.MapViewPanel;
import net.zypr.gui.renderers.TextListRenderer;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.StringUtils;
import net.zypr.mmp.mplayer.TTSPlayer;

public class UserAddressWindow
  extends ModalWindow
{
  private ScrollPane _scrollPaneAddressList = new ScrollPane();
  private JList _listAddressList = new JList(Settings.getInstance().getDTO().getUserAddresslist());
  private Label _labelPreviouslyEntered = new Label("Previously Entered Addresses");

  public UserAddressWindow()
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
    setTitle("Please state the address...");
    _labelPreviouslyEntered.setBounds(new Rectangle(15, 10, 530, 20));
    _scrollPaneAddressList.setBounds(new Rectangle(15, 35, 530, 201));
    _listAddressList.setOpaque(false);
    _listAddressList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _listAddressList.setCellRenderer(new TextListRenderer());
    _listAddressList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent listSelectionEvent)
      {
        _listAddressList_valueChanged(listSelectionEvent);
      }
    });
    _listAddressList.setVisibleRowCount(JLIST_VISIBLE_ROW_COUNT);
    _scrollPaneAddressList.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneAddressList_scrollUp(actionEvent);
      }
    });
    _scrollPaneAddressList.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneAddressList_scrollDown(actionEvent);
      }
    });
    _scrollPaneAddressList.addToViewport(_listAddressList);
    _panelContent.add(_labelPreviouslyEntered, null);
    _panelContent.add(_scrollPaneAddressList, null);
    if (Settings.getInstance().getDTO().isTTSCardHeader())
      new Thread(new Runnable()
      {
        public void run()
        {
          try
            {
              TTSPlayer.getInstance().loadPlayfile(API.getInstance().getVoice().tts(getTitle()).getAudioURI());
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
    setVoiceCommandState(VoiceCommandState.GET_NUMBER_STREET_CITY_STATE);
  }

  private void _listAddressList_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    if (!listSelectionEvent.getValueIsAdjusting())
      showAddress((AddressVO) _listAddressList.getSelectedValue());
  }

  private boolean showAddress(AddressVO addressVO)
  {
    Settings.getInstance().getDTO().addToUserAddresslist(addressVO);
    GeoPositionVO geoPosition;
    try
      {
        geoPosition = API.getInstance().getMap().geocode(addressVO);
        ItemVO itemVO = new ItemVO(ItemType.POI);
        itemVO.setGlobalItemID(StringUtils.toMD5Hash(addressVO.toAddressString() + "|" + Session.getInstance().getUsername()));
        itemVO.setInfo(new InfoPOITypeVO(addressVO, null, null, null));
        itemVO.setDisplay(ItemDisplay.GEO);
        itemVO.setIconURL(API.SERVER_URL + "_assets/gold_star.png");
        itemVO.setListPriority(1);
        itemVO.setName(addressVO.toAddressString());
        itemVO.setDescription("User Address");
        itemVO.setPosition(geoPosition);
        MapViewPanel mapViewPanel = (MapViewPanel) getParentPanel();
        mapViewPanel.addItemVOsPOI(itemVO);
        mapViewPanel.setMapCenter(geoPosition);
        _buttonWindowClose_actionPerformed(null);
        return (true);
      }
    catch (APICommunicationException apiCommunicationException)
      {
        Debug.displayStack(this, apiCommunicationException);
      }
    catch (APIServerErrorException apiServerErrorException)
      {
        Debug.displayStack(this, apiServerErrorException);
      }
    catch (APIProtocolException apiProtocolException)
      {
        Debug.displayStack(this, apiProtocolException);
      }
    return (false);
  }

  public boolean processVoiceCommand(VoiceCommandVO voiceCommandVO)
  {
    if (voiceCommandVO.getName() == VoiceCommandAction.FULL_ADDRESS)
      {
        String number = voiceCommandVO.getVariable("house_number") == null ? null : voiceCommandVO.getVariable("house_number").getValueAsString();
        String street = voiceCommandVO.getVariable("street") == null ? null : voiceCommandVO.getVariable("street").getValueAsString();
        String city = voiceCommandVO.getVariable("city") == null ? null : voiceCommandVO.getVariable("city").getValueAsString();
        String state = voiceCommandVO.getVariable("state") == null ? null : voiceCommandVO.getVariable("state").getValueAsString();
        String province = voiceCommandVO.getVariable("province") == null ? null : voiceCommandVO.getVariable("province").getValueAsString();
        String postal = voiceCommandVO.getVariable("postal") == null ? null : voiceCommandVO.getVariable("postal").getValueAsString();
        String country = voiceCommandVO.getVariable("country") == null ? null : voiceCommandVO.getVariable("country").getValueAsString();
        return (showAddress(new AddressVO("", number, street, city, state, province, postal, country)));
      }
    return (false);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    ((MapViewPanel) getParentPanel()).setUserAddress(null);
    getParentPanel().discardWindow();
  }

  private void _scrollPaneAddressList_scrollUp(ActionEvent actionEvent)
  {
    if (_listAddressList.getFirstVisibleIndex() - _listAddressList.getVisibleRowCount() >= 0)
      _listAddressList.ensureIndexIsVisible(_listAddressList.getFirstVisibleIndex() - _listAddressList.getVisibleRowCount());
    else
      _listAddressList.ensureIndexIsVisible(0);
  }

  private void _scrollPaneAddressList_scrollDown(ActionEvent actionEvent)
  {
    if (_listAddressList.getLastVisibleIndex() + _listAddressList.getVisibleRowCount() < _listAddressList.getModel().getSize())
      _listAddressList.ensureIndexIsVisible(_listAddressList.getLastVisibleIndex() + _listAddressList.getVisibleRowCount());
    else
      _listAddressList.ensureIndexIsVisible(_listAddressList.getModel().getSize());
  }
}
