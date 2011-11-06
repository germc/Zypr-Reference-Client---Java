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


package net.zypr.gui.cache.image;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import java.util.Hashtable;

public class ImageObject
  extends BufferedImage
{
  public ImageObject(ColorModel colorModel, WritableRaster writableRaster, boolean isRasterPremultiplied, Hashtable<?, ?> properties)
  {
    super(colorModel, writableRaster, isRasterPremultiplied, properties);
  }

  public ImageObject(int width, int height, int imageType, IndexColorModel indexColorModel)
  {
    super(width, height, imageType, indexColorModel);
  }

  public ImageObject(int width, int height, int imageType)
  {
    super(width, height, imageType);
  }
}
