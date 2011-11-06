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

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.zypr.api.vo.ManuverVO;
import net.zypr.gui.components.ScrollPane;
import net.zypr.gui.panels.MapViewPanel;
import net.zypr.gui.renderers.RouteListRenderer;
import net.zypr.gui.utils.Debug;

public class RouteListWindow
  extends ModalWindow
{
  private ScrollPane _scrollPaneRouteList = new ScrollPane();
  private JList _listRouteList = null;
  private ManuverVO[] _manuverVOs;

  public RouteListWindow(ManuverVO[] manuverVOs)
  {
    super();
    try
      {
        _manuverVOs = manuverVOs;
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
    setTitle("Routing Details");
    _listRouteList = new JList(_manuverVOs);
    _scrollPaneRouteList.setBounds(15, 10, 530, 237);
    _listRouteList.setOpaque(false);
    _listRouteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _listRouteList.setCellRenderer(new RouteListRenderer());
    _listRouteList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent listSelectionEvent)
      {
        _listRouteList_valueChanged(listSelectionEvent);
      }
    });
    _listRouteList.setVisibleRowCount(JLIST_VISIBLE_ROW_COUNT);
    _scrollPaneRouteList.addScrollUpButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollRouteList_scrollUp(actionEvent);
      }
    });
    _scrollPaneRouteList.addScrollDownButtonActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        _scrollRouteList_scrollDown(actionEvent);
      }
    });
    _scrollPaneRouteList.addToViewport(_listRouteList);
    _panelContent.add(_scrollPaneRouteList, null);
  }

  private void _listRouteList_valueChanged(ListSelectionEvent listSelectionEvent)
  {
    if (!listSelectionEvent.getValueIsAdjusting())
      {
        ManuverVO manuverVO = (ManuverVO) _listRouteList.getSelectedValue();
        ((MapViewPanel) getParentPanel()).setMapCenter(manuverVO.getGeoPosition());
        getParentPanel().discardAllWindows();
      }
  }

  protected void _buttonWindowClose_actionPerformed(ActionEvent actionEvent)
  {
    getParentPanel().discardWindow();
  }

  private void _scrollRouteList_scrollUp(ActionEvent actionEvent)
  {
    if (_listRouteList.getFirstVisibleIndex() - _listRouteList.getVisibleRowCount() >= 0)
      _listRouteList.ensureIndexIsVisible(_listRouteList.getFirstVisibleIndex() - _listRouteList.getVisibleRowCount());
    else
      _listRouteList.ensureIndexIsVisible(0);
  }

  private void _scrollRouteList_scrollDown(ActionEvent actionEvent)
  {
    if (_listRouteList.getLastVisibleIndex() + _listRouteList.getVisibleRowCount() < _listRouteList.getModel().getSize())
      _listRouteList.ensureIndexIsVisible(_listRouteList.getLastVisibleIndex() + _listRouteList.getVisibleRowCount());
    else
      _listRouteList.ensureIndexIsVisible(_listRouteList.getModel().getSize());
  }
}
