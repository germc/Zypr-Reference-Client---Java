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


package net.zypr.gui.markers;

import java.awt.geom.Rectangle2D;

import net.zypr.api.vo.ItemVO;

public class Marker
{
  private ItemVO _item;
  private boolean _selected;
  private int _index;
  private Rectangle2D _clickableRectangle;

  public Marker(ItemVO item, int index)
  {
    super();
    _item = item;
    _index = index;
  }

  public Marker(ItemVO item, int index, boolean selected)
  {
    super();
    _item = item;
    _index = index;
    _selected = selected;
  }

  public void setItem(ItemVO item)
  {
    _item = item;
  }

  public ItemVO getItem()
  {
    return _item;
  }

  public void setSelected(boolean selected)
  {
    _selected = selected;
  }

  public boolean isSelected()
  {
    return _selected;
  }

  public void setIndex(int index)
  {
    _index = index;
  }

  public int getIndex()
  {
    return _index;
  }

  public String getName()
  {
    return (_item.getName());
  }

  public void setClickableRectangle(Rectangle2D clickableRectangle)
  {
    _clickableRectangle = clickableRectangle;
  }

  public Rectangle2D getClickableRectangle()
  {
    return _clickableRectangle;
  }
}
