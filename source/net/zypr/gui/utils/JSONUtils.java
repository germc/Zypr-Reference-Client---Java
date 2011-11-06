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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class JSONUtils
{
  public static String getJSONValue(String query, Object jsonObject)
    throws ParseException
  {
    String value = "";
    try
      {
        String[] pieces = query.split("\\.");
        String currentKey = pieces[0];
        String[] arrayParans = currentKey.split("(\\[|\\])");
        if (arrayParans.length > 1)
          {
            jsonObject = ((JSONObject) jsonObject).get(arrayParans[0]);
            jsonObject = ((JSONArray) jsonObject).get(Integer.parseInt(arrayParans[1]));
          }
        else
          {
            jsonObject = ((JSONObject) jsonObject).get(currentKey);
          }
        pieces = StringUtils.subarray(pieces, 1);
        query = StringUtils.arrayToString(pieces, ".");
        if (pieces.length > 0)
          value = getJSONValue(query, jsonObject);
        else
          value = jsonObject.toString();
      }
    catch (Exception exception)
      {
        throw new ParseException(0);
      }
    return (value);
  }
}
