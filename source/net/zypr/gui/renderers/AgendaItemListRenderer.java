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


package net.zypr.gui.renderers;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import net.zypr.api.vo.ItemVO;
import net.zypr.gui.Resources;

public class AgendaItemListRenderer
  extends ContactListRowPanel
  implements ListCellRenderer
{
  public AgendaItemListRenderer()
  {
    super();
    setOpaque(false);
    setPreferredSize(new Dimension(40, 40));
  }

  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
  {
    ItemVO itemVO = (ItemVO) value;
    setLineNumber(index + 1);
    setContactName("<html>" + itemVO.getName() + "<font color=\"gray\"> : " + itemVO.getDescription() + "</font></html>");
    setSocialNetworkIcon(Resources.getInstance().getBufferedImage("icon-network-" + itemVO.getService() + ".png"));
    return (this);
  }
}
