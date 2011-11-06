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

import net.zypr.api.vo.ServiceAuthStatusVO;
import net.zypr.api.vo.ServiceAuthVO;
import net.zypr.api.vo.ServiceStatusVO;
import net.zypr.gui.components.Label;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.renderers.ServiceAuthRenderer;
import net.zypr.gui.utils.WebBrowser;

public class SettingsServiceWindow
  extends ModalWindow
{
  private ScrollPane _scrollPaneAvailableServices = new ScrollPane();
  private ScrollPane _scrollPaneAuthorizedServices = new ScrollPane();
  private Label _labelAvailableServices = new Label("Available Services");
  private Label _labelAuthorizedServices = new Label("Authorized Services");
  private JList _listAvailableServices = null;
  private JList _listAuthorizedServices = null;
  private ServiceAuthStatusVO _serviceAuthStatusVO;

  public SettingsServiceWindow(ServiceAuthStatusVO serviceAuthStatusVO)
  {
    super();
    try
      {
        _serviceAuthStatusVO = serviceAuthStatusVO;
        jbInit();
      }
    catch (Exception exception)
      {
        exception.printStackTrace();
      }
  }

  private void jbInit()
    throws Exception
  {
    this.setSize(new Dimension(560, 300));
    this.setTitle("Service Authorizations");
    _listAvailableServices = new JList(_serviceAuthStatusVO.getServiceAuths());
    _listAvailableServices.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent listSelectionEvent)
      {
        _listAvailableServices_valueChanged(listSelectionEvent);
      }
    });
    _listAvailableServices.setOpaque(false);
    _listAvailableServices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _listAvailableServices.setCellRenderer(new ServiceAuthRenderer());
    _listAvailableServices.setVisibleRowCount(3);
    _scrollPaneAvailableServices.addToViewport(_listAvailableServices);
    _scrollPaneAvailableServices.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneAvailableServices_scrollUp(actionEvent);
      }
    });
    _scrollPaneAvailableServices.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneAvailableServices_scrollDown(actionEvent);
      }
    });
    _listAuthorizedServices = new JList(_serviceAuthStatusVO.getServiceStatuses());
    _listAuthorizedServices.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent listSelectionEvent)
      {
        _listAuthorizedServices_valueChanged(listSelectionEvent);
      }
    });
    _listAuthorizedServices.setOpaque(false);
    _listAuthorizedServices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _listAuthorizedServices.setCellRenderer(new ServiceAuthRenderer());
    _listAuthorizedServices.setVisibleRowCount(2);
    _scrollPaneAuthorizedServices.addToViewport(_listAuthorizedServices);
    _scrollPaneAuthorizedServices.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneAuthorizedServices_scrollUp(actionEvent);
      }
    });
    _scrollPaneAuthorizedServices.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollPaneAuthorizedServices_scrollDown(actionEvent);
      }
    });
    _panelContent.add(_labelAvailableServices, null);
    _panelContent.add(_labelAuthorizedServices, null);
    _panelContent.add(_scrollPaneAvailableServices, null);
    _panelContent.add(_scrollPaneAuthorizedServices, null);
    _scrollPaneAvailableServices.setBounds(new Rectangle(15, 25, 530, 122));
    _scrollPaneAuthorizedServices.setBounds(new Rectangle(15, 170, 530, 82));
    _labelAvailableServices.setBounds(new Rectangle(15, 5, 530, 15));
    _labelAuthorizedServices.setBounds(new Rectangle(15, 150, 530, 15));
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }

  private void _listAuthorizedServices_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    if (!listSelectionEvent.getValueIsAdjusting())
      {
        ServiceStatusVO serviceStatusVO = (ServiceStatusVO) _listAuthorizedServices.getSelectedValue();
        if (serviceStatusVO.isCurrentlyAuthenticated())
          WebBrowser.openURL(serviceStatusVO.getLogoutLinkForExistingConnection());
        else
          WebBrowser.openURL(serviceStatusVO.getAuthLinkForExistingConnection());
        _buttonWindowClose_actionPerformed(null);
      }
  }

  private void _listAvailableServices_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    if (!listSelectionEvent.getValueIsAdjusting())
      {
        ServiceAuthVO serviceAuthVO = (ServiceAuthVO) _listAvailableServices.getSelectedValue();
        WebBrowser.openURL(serviceAuthVO.getAuthLinkGeneric());
        _buttonWindowClose_actionPerformed(null);
      }
  }

  private void _scrollPaneAvailableServices_scrollUp(ActionEvent actionEvent)
  {
    if (_listAvailableServices.getFirstVisibleIndex() - _listAvailableServices.getVisibleRowCount() >= 0)
      _listAvailableServices.ensureIndexIsVisible(_listAvailableServices.getFirstVisibleIndex() - _listAvailableServices.getVisibleRowCount());
    else
      /*-
      _listAvailableServices.ensureIndexIsVisible(0);
      -*/
      _scrollPaneAvailableServices.scrollToStart();
  }

  private void _scrollPaneAvailableServices_scrollDown(ActionEvent actionEvent)
  {
    if (_listAvailableServices.getLastVisibleIndex() + _listAvailableServices.getVisibleRowCount() < _listAvailableServices.getModel().getSize())
      _listAvailableServices.ensureIndexIsVisible(_listAvailableServices.getLastVisibleIndex() + _listAvailableServices.getVisibleRowCount());
    else
      /*-
      _listAvailableServices.ensureIndexIsVisible(_listAvailableServices.getModel().getSize() -  _listAvailableServices.getVisibleRowCount());
      -*/
      _scrollPaneAvailableServices.scrollToEnd();
  }

  private void _scrollPaneAuthorizedServices_scrollUp(ActionEvent actionEvent)
  {
    if (_listAuthorizedServices.getFirstVisibleIndex() - _listAuthorizedServices.getVisibleRowCount() >= 0)
      _listAuthorizedServices.ensureIndexIsVisible(_listAuthorizedServices.getFirstVisibleIndex() - _listAuthorizedServices.getVisibleRowCount());
    else
      /*-
      _listAuthorizedServices.ensureIndexIsVisible(0);
      -*/
      _scrollPaneAuthorizedServices.scrollToStart();
  }

  private void _scrollPaneAuthorizedServices_scrollDown(ActionEvent actionEvent)
  {
    if (_listAuthorizedServices.getLastVisibleIndex() + _listAuthorizedServices.getVisibleRowCount() < _listAuthorizedServices.getModel().getSize())
      _listAuthorizedServices.ensureIndexIsVisible(_listAuthorizedServices.getLastVisibleIndex() + _listAuthorizedServices.getVisibleRowCount());
    else
      //			_listAuthorizedServices.ensureIndexIsVisible(_listAuthorizedServices.getModel().getSize());
      _scrollPaneAuthorizedServices.scrollToEnd();
  }
}
