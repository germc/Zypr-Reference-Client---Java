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
import java.awt.image.BufferedImage;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import net.zypr.api.vo.ItemVO;
import net.zypr.gui.ImageFetcher;

public class FavoritesListRenderer
  extends FavoritesListRowPanel
  implements ListCellRenderer
{
  public FavoritesListRenderer()
  {
    super();
    setOpaque(false);
    setPreferredSize(new Dimension(40, 47));
  }

  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
  {
    setOpaque(isSelected);
    ItemVO itemVO = (ItemVO) value;
    setLineNumber(index + 1);
    setName(itemVO.getName());
    BufferedImage bufferedImage = ImageFetcher.getInstance().getImage(itemVO.getIconURL(), 32, 32, null);
    if (bufferedImage != null)
      setPicture(bufferedImage);
    return this;
  }
}
