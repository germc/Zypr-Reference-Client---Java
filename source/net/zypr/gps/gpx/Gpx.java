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
import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
    { "name", "desc", "author", "email", "url", "urlname", "time", "keywords", "bounds", "wpt", "rte", "trk", "any" })
@XmlRootElement(name = "gpx")
public class Gpx
{
  protected String name;
  protected String desc;
  protected String author;
  protected String email;
  @XmlSchemaType(name = "anyURI")
  protected String url;
  protected String urlname;
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar time;
  protected String keywords;
  protected BoundsType bounds;
  protected List<Gpx.Wpt> wpt;
  protected List<Gpx.Rte> rte;
  protected List<Gpx.Trk> trk;
  @XmlAnyElement(lax = true)
  protected List<Object> any;
  @XmlAttribute(required = true)
  protected String version;
  @XmlAttribute(required = true)
  protected String creator;

  public String getName()
  {
    return name;
  }

  public void setName(String value)
  {
    this.name = value;
  }

  public String getDesc()
  {
    return desc;
  }

  public void setDesc(String value)
  {
    this.desc = value;
  }

  public String getAuthor()
  {
    return author;
  }

  public void setAuthor(String value)
  {
    this.author = value;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String value)
  {
    this.email = value;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl(String value)
  {
    this.url = value;
  }

  public String getUrlname()
  {
    return urlname;
  }

  public void setUrlname(String value)
  {
    this.urlname = value;
  }

  public XMLGregorianCalendar getTime()
  {
    return time;
  }

  public void setTime(XMLGregorianCalendar value)
  {
    this.time = value;
  }

  public String getKeywords()
  {
    return keywords;
  }

  public void setKeywords(String value)
  {
    this.keywords = value;
  }

  public BoundsType getBounds()
  {
    return bounds;
  }

  public void setBounds(BoundsType value)
  {
    this.bounds = value;
  }

  public List<Gpx.Wpt> getWpt()
  {
    if (wpt == null)
      {
        wpt = new ArrayList<Gpx.Wpt>();
      }
    return this.wpt;
  }

  public List<Gpx.Rte> getRte()
  {
    if (rte == null)
      {
        rte = new ArrayList<Gpx.Rte>();
      }
    return this.rte;
  }

  public List<Gpx.Trk> getTrk()
  {
    if (trk == null)
      {
        trk = new ArrayList<Gpx.Trk>();
      }
    return this.trk;
  }

  public List<Object> getAny()
  {
    if (any == null)
      {
        any = new ArrayList<Object>();
      }
    return this.any;
  }

  public String getVersion()
  {
    if (version == null)
      {
        return "1.0";
      }
    else
      {
        return version;
      }
  }

  public void setVersion(String value)
  {
    this.version = value;
  }

  public String getCreator()
  {
    return creator;
  }

  public void setCreator(String value)
  {
    this.creator = value;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder =
      { "name", "cmt", "desc", "src", "url", "urlname", "number", "any", "rtept" })
  public static class Rte
  {
    protected String name;
    protected String cmt;
    protected String desc;
    protected String src;
    @XmlSchemaType(name = "anyURI")
    protected String url;
    protected String urlname;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger number;
    @XmlAnyElement(lax = true)
    protected List<Object> any;
    protected List<Gpx.Rte.Rtept> rtept;

    public String getName()
    {
      return name;
    }

    public void setName(String value)
    {
      this.name = value;
    }

    public String getCmt()
    {
      return cmt;
    }

    public void setCmt(String value)
    {
      this.cmt = value;
    }

    public String getDesc()
    {
      return desc;
    }

    public void setDesc(String value)
    {
      this.desc = value;
    }

    public String getSrc()
    {
      return src;
    }

    public void setSrc(String value)
    {
      this.src = value;
    }

    public String getUrl()
    {
      return url;
    }

    public void setUrl(String value)
    {
      this.url = value;
    }

    public String getUrlname()
    {
      return urlname;
    }

    public void setUrlname(String value)
    {
      this.urlname = value;
    }

    public BigInteger getNumber()
    {
      return number;
    }

    public void setNumber(BigInteger value)
    {
      this.number = value;
    }

    public List<Object> getAny()
    {
      if (any == null)
        {
          any = new ArrayList<Object>();
        }
      return this.any;
    }

    public List<Gpx.Rte.Rtept> getRtept()
    {
      if (rtept == null)
        {
          rtept = new ArrayList<Gpx.Rte.Rtept>();
        }
      return this.rtept;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder =
        { "ele", "time", "magvar", "geoidheight", "name", "cmt", "desc", "src", "url", "urlname", "sym", "type", "fix", "sat", "hdop", "vdop", "pdop", "ageofdgpsdata", "dgpsid", "any" })
    public static class Rtept
    {
      protected BigDecimal ele;
      @XmlSchemaType(name = "dateTime")
      protected XMLGregorianCalendar time;
      protected BigDecimal magvar;
      protected BigDecimal geoidheight;
      protected String name;
      protected String cmt;
      protected String desc;
      protected String src;
      @XmlSchemaType(name = "anyURI")
      protected String url;
      protected String urlname;
      protected String sym;
      protected String type;
      protected String fix;
      @XmlSchemaType(name = "nonNegativeInteger")
      protected BigInteger sat;
      protected BigDecimal hdop;
      protected BigDecimal vdop;
      protected BigDecimal pdop;
      protected BigDecimal ageofdgpsdata;
      protected Integer dgpsid;
      @XmlAnyElement(lax = true)
      protected List<Object> any;
      @XmlAttribute(required = true)
      protected BigDecimal lat;
      @XmlAttribute(required = true)
      protected BigDecimal lon;

      public BigDecimal getEle()
      {
        return ele;
      }

      public void setEle(BigDecimal value)
      {
        this.ele = value;
      }

      public XMLGregorianCalendar getTime()
      {
        return time;
      }

      public void setTime(XMLGregorianCalendar value)
      {
        this.time = value;
      }

      public BigDecimal getMagvar()
      {
        return magvar;
      }

      public void setMagvar(BigDecimal value)
      {
        this.magvar = value;
      }

      public BigDecimal getGeoidheight()
      {
        return geoidheight;
      }

      public void setGeoidheight(BigDecimal value)
      {
        this.geoidheight = value;
      }

      public String getName()
      {
        return name;
      }

      public void setName(String value)
      {
        this.name = value;
      }

      public String getCmt()
      {
        return cmt;
      }

      public void setCmt(String value)
      {
        this.cmt = value;
      }

      public String getDesc()
      {
        return desc;
      }

      public void setDesc(String value)
      {
        this.desc = value;
      }

      public String getSrc()
      {
        return src;
      }

      public void setSrc(String value)
      {
        this.src = value;
      }

      public String getUrl()
      {
        return url;
      }

      public void setUrl(String value)
      {
        this.url = value;
      }

      public String getUrlname()
      {
        return urlname;
      }

      public void setUrlname(String value)
      {
        this.urlname = value;
      }

      public String getSym()
      {
        return sym;
      }

      public void setSym(String value)
      {
        this.sym = value;
      }

      public String getType()
      {
        return type;
      }

      public void setType(String value)
      {
        this.type = value;
      }

      public String getFix()
      {
        return fix;
      }

      public void setFix(String value)
      {
        this.fix = value;
      }

      public BigInteger getSat()
      {
        return sat;
      }

      public void setSat(BigInteger value)
      {
        this.sat = value;
      }

      public BigDecimal getHdop()
      {
        return hdop;
      }

      public void setHdop(BigDecimal value)
      {
        this.hdop = value;
      }

      public BigDecimal getVdop()
      {
        return vdop;
      }

      public void setVdop(BigDecimal value)
      {
        this.vdop = value;
      }

      public BigDecimal getPdop()
      {
        return pdop;
      }

      public void setPdop(BigDecimal value)
      {
        this.pdop = value;
      }

      public BigDecimal getAgeofdgpsdata()
      {
        return ageofdgpsdata;
      }

      public void setAgeofdgpsdata(BigDecimal value)
      {
        this.ageofdgpsdata = value;
      }

      public Integer getDgpsid()
      {
        return dgpsid;
      }

      public void setDgpsid(Integer value)
      {
        this.dgpsid = value;
      }

      public List<Object> getAny()
      {
        if (any == null)
          {
            any = new ArrayList<Object>();
          }
        return this.any;
      }

      public BigDecimal getLat()
      {
        return lat;
      }

      public void setLat(BigDecimal value)
      {
        this.lat = value;
      }

      public BigDecimal getLon()
      {
        return lon;
      }

      public void setLon(BigDecimal value)
      {
        this.lon = value;
      }
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder =
      { "name", "cmt", "desc", "src", "url", "urlname", "number", "any", "trkseg" })
  public static class Trk
  {
    protected String name;
    protected String cmt;
    protected String desc;
    protected String src;
    @XmlSchemaType(name = "anyURI")
    protected String url;
    protected String urlname;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger number;
    @XmlAnyElement(lax = true)
    protected List<Object> any;
    protected List<Gpx.Trk.Trkseg> trkseg;

    public String getName()
    {
      return name;
    }

    public void setName(String value)
    {
      this.name = value;
    }

    public String getCmt()
    {
      return cmt;
    }

    public void setCmt(String value)
    {
      this.cmt = value;
    }

    public String getDesc()
    {
      return desc;
    }

    public void setDesc(String value)
    {
      this.desc = value;
    }

    public String getSrc()
    {
      return src;
    }

    public void setSrc(String value)
    {
      this.src = value;
    }

    public String getUrl()
    {
      return url;
    }

    public void setUrl(String value)
    {
      this.url = value;
    }

    public String getUrlname()
    {
      return urlname;
    }

    public void setUrlname(String value)
    {
      this.urlname = value;
    }

    public BigInteger getNumber()
    {
      return number;
    }

    public void setNumber(BigInteger value)
    {
      this.number = value;
    }

    public List<Object> getAny()
    {
      if (any == null)
        {
          any = new ArrayList<Object>();
        }
      return this.any;
    }

    public List<Gpx.Trk.Trkseg> getTrkseg()
    {
      if (trkseg == null)
        {
          trkseg = new ArrayList<Gpx.Trk.Trkseg>();
        }
      return this.trkseg;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder =
        { "trkpt" })
    public static class Trkseg
    {
      protected List<Gpx.Trk.Trkseg.Trkpt> trkpt;

      public List<Gpx.Trk.Trkseg.Trkpt> getTrkpt()
      {
        if (trkpt == null)
          {
            trkpt = new ArrayList<Gpx.Trk.Trkseg.Trkpt>();
          }
        return this.trkpt;
      }

      @XmlAccessorType(XmlAccessType.FIELD)
      @XmlType(name = "", propOrder =
          { "ele", "time", "course", "speed", "magvar", "geoidheight", "name", "cmt", "desc", "src", "url", "urlname", "sym", "type", "fix", "sat", "hdop", "vdop", "pdop", "ageofdgpsdata", "dgpsid", "any" })
      public static class Trkpt
      {
        protected BigDecimal ele;
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar time;
        protected BigDecimal course;
        protected BigDecimal speed;
        protected BigDecimal magvar;
        protected BigDecimal geoidheight;
        protected String name;
        protected String cmt;
        protected String desc;
        protected String src;
        @XmlSchemaType(name = "anyURI")
        protected String url;
        protected String urlname;
        protected String sym;
        protected String type;
        protected String fix;
        @XmlSchemaType(name = "nonNegativeInteger")
        protected BigInteger sat;
        protected BigDecimal hdop;
        protected BigDecimal vdop;
        protected BigDecimal pdop;
        protected BigDecimal ageofdgpsdata;
        protected Integer dgpsid;
        @XmlAnyElement(lax = true)
        protected List<Object> any;
        @XmlAttribute(required = true)
        protected BigDecimal lat;
        @XmlAttribute(required = true)
        protected BigDecimal lon;

        public BigDecimal getEle()
        {
          return ele;
        }

        public void setEle(BigDecimal value)
        {
          this.ele = value;
        }

        public XMLGregorianCalendar getTime()
        {
          return time;
        }

        public void setTime(XMLGregorianCalendar value)
        {
          this.time = value;
        }

        public BigDecimal getCourse()
        {
          return course;
        }

        public void setCourse(BigDecimal value)
        {
          this.course = value;
        }

        public BigDecimal getSpeed()
        {
          return speed;
        }

        public void setSpeed(BigDecimal value)
        {
          this.speed = value;
        }

        public BigDecimal getMagvar()
        {
          return magvar;
        }

        public void setMagvar(BigDecimal value)
        {
          this.magvar = value;
        }

        public BigDecimal getGeoidheight()
        {
          return geoidheight;
        }

        public void setGeoidheight(BigDecimal value)
        {
          this.geoidheight = value;
        }

        public String getName()
        {
          return name;
        }

        public void setName(String value)
        {
          this.name = value;
        }

        public String getCmt()
        {
          return cmt;
        }

        public void setCmt(String value)
        {
          this.cmt = value;
        }

        public String getDesc()
        {
          return desc;
        }

        public void setDesc(String value)
        {
          this.desc = value;
        }

        public String getSrc()
        {
          return src;
        }

        public void setSrc(String value)
        {
          this.src = value;
        }

        public String getUrl()
        {
          return url;
        }

        public void setUrl(String value)
        {
          this.url = value;
        }

        public String getUrlname()
        {
          return urlname;
        }

        public void setUrlname(String value)
        {
          this.urlname = value;
        }

        public String getSym()
        {
          return sym;
        }

        public void setSym(String value)
        {
          this.sym = value;
        }

        public String getType()
        {
          return type;
        }

        public void setType(String value)
        {
          this.type = value;
        }

        public String getFix()
        {
          return fix;
        }

        public void setFix(String value)
        {
          this.fix = value;
        }

        public BigInteger getSat()
        {
          return sat;
        }

        public void setSat(BigInteger value)
        {
          this.sat = value;
        }

        public BigDecimal getHdop()
        {
          return hdop;
        }

        public void setHdop(BigDecimal value)
        {
          this.hdop = value;
        }

        public BigDecimal getVdop()
        {
          return vdop;
        }

        public void setVdop(BigDecimal value)
        {
          this.vdop = value;
        }

        public BigDecimal getPdop()
        {
          return pdop;
        }

        public void setPdop(BigDecimal value)
        {
          this.pdop = value;
        }

        public BigDecimal getAgeofdgpsdata()
        {
          return ageofdgpsdata;
        }

        public void setAgeofdgpsdata(BigDecimal value)
        {
          this.ageofdgpsdata = value;
        }

        public Integer getDgpsid()
        {
          return dgpsid;
        }

        public void setDgpsid(Integer value)
        {
          this.dgpsid = value;
        }

        public List<Object> getAny()
        {
          if (any == null)
            {
              any = new ArrayList<Object>();
            }
          return this.any;
        }

        public BigDecimal getLat()
        {
          return lat;
        }

        public void setLat(BigDecimal value)
        {
          this.lat = value;
        }

        public BigDecimal getLon()
        {
          return lon;
        }

        public void setLon(BigDecimal value)
        {
          this.lon = value;
        }
      }
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder =
      { "ele", "time", "magvar", "geoidheight", "name", "cmt", "desc", "src", "url", "urlname", "sym", "type", "fix", "sat", "hdop", "vdop", "pdop", "ageofdgpsdata", "dgpsid", "any" })
  public static class Wpt
  {
    protected BigDecimal ele;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar time;
    protected BigDecimal magvar;
    protected BigDecimal geoidheight;
    protected String name;
    protected String cmt;
    protected String desc;
    protected String src;
    @XmlSchemaType(name = "anyURI")
    protected String url;
    protected String urlname;
    protected String sym;
    protected String type;
    protected String fix;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger sat;
    protected BigDecimal hdop;
    protected BigDecimal vdop;
    protected BigDecimal pdop;
    protected BigDecimal ageofdgpsdata;
    protected Integer dgpsid;
    @XmlAnyElement(lax = true)
    protected List<Object> any;
    @XmlAttribute(required = true)
    protected BigDecimal lat;
    @XmlAttribute(required = true)
    protected BigDecimal lon;

    public BigDecimal getEle()
    {
      return ele;
    }

    public void setEle(BigDecimal value)
    {
      this.ele = value;
    }

    public XMLGregorianCalendar getTime()
    {
      return time;
    }

    public void setTime(XMLGregorianCalendar value)
    {
      this.time = value;
    }

    public BigDecimal getMagvar()
    {
      return magvar;
    }

    public void setMagvar(BigDecimal value)
    {
      this.magvar = value;
    }

    public BigDecimal getGeoidheight()
    {
      return geoidheight;
    }

    public void setGeoidheight(BigDecimal value)
    {
      this.geoidheight = value;
    }

    public String getName()
    {
      return name;
    }

    public void setName(String value)
    {
      this.name = value;
    }

    public String getCmt()
    {
      return cmt;
    }

    public void setCmt(String value)
    {
      this.cmt = value;
    }

    public String getDesc()
    {
      return desc;
    }

    public void setDesc(String value)
    {
      this.desc = value;
    }

    public String getSrc()
    {
      return src;
    }

    public void setSrc(String value)
    {
      this.src = value;
    }

    public String getUrl()
    {
      return url;
    }

    public void setUrl(String value)
    {
      this.url = value;
    }

    public String getUrlname()
    {
      return urlname;
    }

    public void setUrlname(String value)
    {
      this.urlname = value;
    }

    public String getSym()
    {
      return sym;
    }

    public void setSym(String value)
    {
      this.sym = value;
    }

    public String getType()
    {
      return type;
    }

    public void setType(String value)
    {
      this.type = value;
    }

    public String getFix()
    {
      return fix;
    }

    public void setFix(String value)
    {
      this.fix = value;
    }

    public BigInteger getSat()
    {
      return sat;
    }

    public void setSat(BigInteger value)
    {
      this.sat = value;
    }

    public BigDecimal getHdop()
    {
      return hdop;
    }

    public void setHdop(BigDecimal value)
    {
      this.hdop = value;
    }

    public BigDecimal getVdop()
    {
      return vdop;
    }

    public void setVdop(BigDecimal value)
    {
      this.vdop = value;
    }

    public BigDecimal getPdop()
    {
      return pdop;
    }

    public void setPdop(BigDecimal value)
    {
      this.pdop = value;
    }

    public BigDecimal getAgeofdgpsdata()
    {
      return ageofdgpsdata;
    }

    public void setAgeofdgpsdata(BigDecimal value)
    {
      this.ageofdgpsdata = value;
    }

    public Integer getDgpsid()
    {
      return dgpsid;
    }

    public void setDgpsid(Integer value)
    {
      this.dgpsid = value;
    }

    public List<Object> getAny()
    {
      if (any == null)
        {
          any = new ArrayList<Object>();
        }
      return this.any;
    }

    public BigDecimal getLat()
    {
      return lat;
    }

    public void setLat(BigDecimal value)
    {
      this.lat = value;
    }

    public BigDecimal getLon()
    {
      return lon;
    }

    public void setLon(BigDecimal value)
    {
      this.lon = value;
    }
  }
}
