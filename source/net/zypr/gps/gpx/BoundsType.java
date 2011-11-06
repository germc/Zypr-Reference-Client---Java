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


package net.zypr.gps.gpx;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "boundsType")
public class BoundsType
{
  @XmlAttribute(required = true)
  protected BigDecimal minlat;
  @XmlAttribute(required = true)
  protected BigDecimal minlon;
  @XmlAttribute(required = true)
  protected BigDecimal maxlat;
  @XmlAttribute(required = true)
  protected BigDecimal maxlon;

  public BigDecimal getMinlat()
  {
    return minlat;
  }

  public void setMinlat(BigDecimal value)
  {
    this.minlat = value;
  }

  public BigDecimal getMinlon()
  {
    return minlon;
  }

  public void setMinlon(BigDecimal value)
  {
    this.minlon = value;
  }

  public BigDecimal getMaxlat()
  {
    return maxlat;
  }

  public void setMaxlat(BigDecimal value)
  {
    this.maxlat = value;
  }

  public BigDecimal getMaxlon()
  {
    return maxlon;
  }

  public void setMaxlon(BigDecimal value)
  {
    this.maxlon = value;
  }
}
