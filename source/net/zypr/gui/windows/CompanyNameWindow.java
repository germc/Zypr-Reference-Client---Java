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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.zypr.api.API;
import net.zypr.api.enums.VoiceCommandAction;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.vo.AddressVO;
import net.zypr.api.vo.GeoBoundsItemsVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.api.vo.VoiceCommandVariableVO;
import net.zypr.gui.Configuration;
import net.zypr.gui.Settings;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.components.TextField;
import net.zypr.gui.panels.MapViewPanel;
import net.zypr.gui.renderers.TextListRenderer;
import net.zypr.gui.utils.AudioUtils;
import net.zypr.gui.utils.Debug;
import net.zypr.gui.utils.GeoUtil;
import net.zypr.mmp.mplayer.TTSPlayer;

public class CompanyNameWindow
  extends ModalWindow
{
  private ScrollPane _scrollPaneCompanyList = new ScrollPane();
  private JList _listCompanyList = new JList(Settings.getInstance().getDTO().getCompanyNamelist());
  private Label _labelPreviouslyEntered = new Label("Previously Entered Company Names");
  private TextField _textFieldCompanyName = new TextField();
  private Label _labelCompanyName = new Label("");
  private VoiceCommandAction _voiceCommandAction;
  private String _city = null;
  private String _state = null;

  public CompanyNameWindow(VoiceCommandAction voiceCommandAction, String city, String state)
  {
    super();
    _voiceCommandAction = voiceCommandAction;
    _city = city;
    _state = state;
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
    setTitle("State company name");
    _labelPreviouslyEntered.setBounds(new Rectangle(15, 81, 530, 20));
    _textFieldCompanyName.setBounds(new Rectangle(15, 35, 530, 30));
    _textFieldCompanyName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent keyEvent)
      {
        if (keyEvent.getKeyCode() == 10)
          _textFieldCompanyName_actionPerformed();
      }
    });
    _labelCompanyName.setBounds(new Rectangle(15, 10, 590, 20));
    if (_voiceCommandAction == VoiceCommandAction.BUSINESS_SEARCH_INTENT_4_NEARBY_LOCATION)
      _labelCompanyName.setText("Please State Business Name Nearby");
    else if (_voiceCommandAction == VoiceCommandAction.BUSINESS_SEARCH_INTENT_4_NEAR_DESTINATION)
      _labelCompanyName.setText("Please State Business Name Near Destination");
    else if (_voiceCommandAction == VoiceCommandAction.BUSINESS_SEARCH_INTENT_4_IN_A_LOCATION)
      _labelCompanyName.setText("Please State Business Name In " + _city + ", " + _state);
    _scrollPaneCompanyList.setBounds(new Rectangle(15, 106, 530, 130));
    _listCompanyList.setOpaque(false);
    _listCompanyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _listCompanyList.setCellRenderer(new TextListRenderer());
    _listCompanyList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent listSelectionEvent)
      {
        _listCompanyList_valueChanged(listSelectionEvent);
      }
    });
    _listCompanyList.setVisibleRowCount(JLIST_VISIBLE_ROW_COUNT - 1);
    _scrollPaneCompanyList.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneCompanyList_scrollUp(actionEvent);
      }
    });
    _scrollPaneCompanyList.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneCompanyList_scrollDown(actionEvent);
      }
    });
    _scrollPaneCompanyList.addToViewport(_listCompanyList);
    _panelContent.add(_labelCompanyName, null);
    _panelContent.add(_labelPreviouslyEntered, null);
    _panelContent.add(_scrollPaneCompanyList, null);
    _panelContent.add(_textFieldCompanyName, null);
    if (Settings.getInstance().getDTO().isTTSCardHeader())
      new Thread(new Runnable()
      {
        public void run()
        {
          try
            {
              TTSPlayer.getInstance().loadPlayfile(API.getInstance().getVoice().tts(_labelCompanyName.getText()).getAudioURI());
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
    if (_city != null || _state != null)
      ((MapViewPanel) getParentPanel()).setUserAddress(new AddressVO(_city, _state));
    setVoiceCommandState(VoiceCommandState.GET_BUSINESS_NAME);
  }

  private void _listCompanyList_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    if (!listSelectionEvent.getValueIsAdjusting())
      {
        _textFieldCompanyName.setText("" + _listCompanyList.getSelectedValue());
        _textFieldCompanyName_actionPerformed();
      }
  }

  public boolean processVoiceCommand(VoiceCommandVO voiceCommandVO)
  {
    try
      {
        VoiceCommandVariableVO voiceCommandVariableVO = null;
        if (voiceCommandVO.getName() == VoiceCommandAction.CATEGORY_NATIONAL_BRAND_OR_BUSINESS_IN_CITY)
          {
            voiceCommandVariableVO = voiceCommandVO.getVariable("business");
            if (voiceCommandVariableVO != null)
              {
                _textFieldCompanyName.setText(voiceCommandVariableVO.getValueAsString());
                _textFieldCompanyName_actionPerformed();
                return (true);
              }
          }
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    return (false);
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    ((MapViewPanel) getParentPanel()).setUserAddress(null);
    getParentPanel().discardWindow();
  }

  private void _scrollPaneCompanyList_scrollUp(ActionEvent actionEvent)
  {
    if (_listCompanyList.getFirstVisibleIndex() - _listCompanyList.getVisibleRowCount() >= 0)
      _listCompanyList.ensureIndexIsVisible(_listCompanyList.getFirstVisibleIndex() - _listCompanyList.getVisibleRowCount());
    else
      _listCompanyList.ensureIndexIsVisible(0);
  }

  private void _scrollPaneCompanyList_scrollDown(ActionEvent actionEvent)
  {
    if (_listCompanyList.getLastVisibleIndex() + _listCompanyList.getVisibleRowCount() < _listCompanyList.getModel().getSize())
      _listCompanyList.ensureIndexIsVisible(_listCompanyList.getLastVisibleIndex() + _listCompanyList.getVisibleRowCount());
    else
      _listCompanyList.ensureIndexIsVisible(_listCompanyList.getModel().getSize());
  }

  private void _textFieldCompanyName_actionPerformed()
  {
    _textFieldCompanyName.setText(_textFieldCompanyName.getText().trim());
    if (_textFieldCompanyName.getText().equals(""))
      return;
    MapViewPanel mapViewPanel = (MapViewPanel) getParentPanel();
    _listCompanyList.setEnabled(false);
    _textFieldCompanyName.setEditable(false);
    Settings.getInstance().getDTO().addToCompanyNamelist(_textFieldCompanyName.getText());
    if (_voiceCommandAction == VoiceCommandAction.BUSINESS_SEARCH_INTENT_4_NEAR_DESTINATION && mapViewPanel.getRouteVO() != null)
      {
        try
          {
            mapViewPanel.setMapCenter(mapViewPanel.getRouteVO().getManuvers()[mapViewPanel.getRouteVO().getManuvers().length - 1].getGeoPosition());
            mapViewPanel.setZoom(1);
            GeoBoundsItemsVO geoBoundsItemsVO = API.getInstance().getPOI().search(_textFieldCompanyName.getText(), Configuration.getInstance().getProperty("poi-provider-for-business-name-search", "all"), GeoUtil.getMapBounds(mapViewPanel), 0, 0);
            if (geoBoundsItemsVO.getItems().length > 0)
              {
                mapViewPanel.unsetLockToGPS();
                mapViewPanel.setItemVOsPOI(geoBoundsItemsVO.getItems(), true);
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
    else if (_voiceCommandAction == VoiceCommandAction.BUSINESS_SEARCH_INTENT_4_NEARBY_LOCATION)
      {
        try
          {
            GeoBoundsItemsVO geoBoundsItemsVO = API.getInstance().getPOI().search(_textFieldCompanyName.getText(), Configuration.getInstance().getProperty("poi-provider-for-business-name-search", "all"), GeoUtil.getMapBounds(mapViewPanel), 0, 0);
            if (geoBoundsItemsVO.getItems().length > 0)
              {
                mapViewPanel.unsetLockToGPS();
                mapViewPanel.setItemVOsPOI(geoBoundsItemsVO.getItems(), true);
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
    else if (_voiceCommandAction == VoiceCommandAction.BUSINESS_SEARCH_INTENT_4_IN_A_LOCATION)
      {
        try
          {
            GeoBoundsItemsVO geoBoundsItemsVO = API.getInstance().getPOI().search(_textFieldCompanyName.getText(), Configuration.getInstance().getProperty("poi-provider-for-business-name-search", "all"), _city + (_state != null ? "," + _state : ""), 0, 0);
            if (geoBoundsItemsVO.getItems().length > 0)
              {
                mapViewPanel.unsetLockToGPS();
                mapViewPanel.setZoomToBoundingBoxAndCenter(geoBoundsItemsVO.getGeoBounds());
                mapViewPanel.setItemVOsPOI(geoBoundsItemsVO.getItems(), true);
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
    else
      {
        _listCompanyList.setEnabled(true);
        _textFieldCompanyName.setEditable(true);
        return;
      }
  }
}
