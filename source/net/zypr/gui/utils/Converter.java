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


package net.zypr.gui.utils;

import net.zypr.api.enums.DistanceUnit;
import net.zypr.api.enums.TemperatureScale;

public class Converter
{
  public static double celsius2Fahrenheit(double celsiusDegree)
  {
    return ((9.0 / 5.0) * celsiusDegree + 32);
  }

  public static double fahrenheit2Celsius(double fahrenheitDegree)
  {
    return ((5.0 / 9.0) * (fahrenheitDegree - 32));
  }

  public static double convertTemperatures(TemperatureScale fromTemperature, TemperatureScale toTemperature, double degree)
  {
    if (fromTemperature == TemperatureScale.CELSIUS && toTemperature == TemperatureScale.FAHRENHEIT)
      return (celsius2Fahrenheit(degree));
    else if (fromTemperature == TemperatureScale.FAHRENHEIT && toTemperature == TemperatureScale.CELSIUS)
      return (fahrenheit2Celsius(degree));
    else
      return (degree);
  }

  public static double mile2kilometer(double mile)
  {
    return (mile * 1.609);
  }

  public static double kilometer2mile(double kilometer)
  {
    return (kilometer / 1.609);
  }

  public static double convertDistances(DistanceUnit fromDistance, DistanceUnit toDistance, double distance)
  {
    if (fromDistance == DistanceUnit.MILE && toDistance == DistanceUnit.KILOMETER)
      return (mile2kilometer(distance));
    else if (fromDistance == DistanceUnit.KILOMETER && toDistance == DistanceUnit.MILE)
      return (kilometer2mile(distance));
    else
      return (distance);
  }
}
