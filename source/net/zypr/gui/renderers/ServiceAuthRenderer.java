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

import net.zypr.api.vo.ServiceAuthVO;
import net.zypr.api.vo.ServiceStatusVO;
import net.zypr.gui.ImageFetcher;
import net.zypr.gui.Resources;

public class ServiceAuthRenderer
  extends ServiceAuthRowPanel
  implements ListCellRenderer
{
  public ServiceAuthRenderer()
  {
    super();
    setOpaque(false);
    setPreferredSize(new Dimension(40, 40));
  }

  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
  {
    setOpaque(isSelected);
    if (value instanceof ServiceAuthVO)
      {
        ServiceAuthVO serviceAuthVO = (ServiceAuthVO) value;
        setDetailMessage("Add your \"" + serviceAuthVO.getServiceName() + "\" account.");
        setServiceLogo(ImageFetcher.getInstance().getImage(serviceAuthVO.getServiceIconURI(), 32, 32, Resources.getInstance().getBufferedImage("icon-network-zypr.png")));
        setEnabled(true);
      }
    else if (value instanceof ServiceStatusVO)
      {
        ServiceStatusVO serviceStatusVO = (ServiceStatusVO) value;
        setServiceLogo(ImageFetcher.getInstance().getImage(serviceStatusVO.getServiceIconURI(), 32, 32, Resources.getInstance().getBufferedImage("icon-network-zypr.png")));
        setStatusImage("icon-social-network-" + (serviceStatusVO.isCurrentlyAuthenticated() ? "" : "not-") + "authorized.png");
        setDetailMessage((serviceStatusVO.isCurrentlyAuthenticated() ? "Remove" : "Re-authenticate") + " your \"" + serviceStatusVO.getUserName() + "\" account.");
      }
    return this;
  }
}
