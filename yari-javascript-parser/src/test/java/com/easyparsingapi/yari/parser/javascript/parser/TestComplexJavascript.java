/*
 * Copyright (c) 2025 Easy API
 * Website : https://easyparsingapi.com/
 * GitHub  : https://github.com/Easy-API-Style/yari-framework
 * Contact : easy.api.contact@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easyparsingapi.yari.parser.javascript.parser;

import static com.easyparsingapi.yari.parser.javascript.parser.AssertUtil.assertAst;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class TestComplexJavascript {
    
  @Test
  public void test_01(TestInfo testInfo) throws IOException {
      String code = 
                "({1:"
              + "["
              + "function(e,o,t)"
              + "{"
              + "o.exports="
              + "{"
              + " CookieKey:\"_USERCOUNTRY6\","
              + " CookieValue:\"US\","
              + " SetCookie:function(e,o,t){var n=new Date,i=t;n.setTime(n.getTime()+24*i*60*60*1e3),document.cookie=e+\"=\"+escape(o)+\"; path=/\"+(null===n?\"\":\"; expires=\"+n.toGMTString())},"
              + " GetCookie:function(e){var o=e+\"=\";return document.cookie.length>0&&(offset=document.cookie.indexOf(o),-1!=offset)?(offset+=o.length,end=document.cookie.indexOf(\";\",offset),-1==end&&(end=document.cookie.length),unescape(document.cookie.substring(offset,end))):void 0},"
              + " AreCookiesEnabled:function(){return\"undefined\"!=typeof navigator.cookieEnabled||e||(document.cookie=\"testcookie\",e=-1!=document.cookie.indexOf(\"testcookie\")),e},"
              + " SetCountryFromServer:"
              + "   function(){"
              + "     var e,o=\"US\",t=\"\",n=!1,i=this;"
              + "     if(t=/.*pcmag\\.com$/.test(location.hostname)?\"//geo.pcmag.com/loc/country.php\":\"//geo.ziffdavis.com/loc/country.php\",this.SetCookie(this.CookieKey,o,1),window.XMLHttpRequest&&(e=new XMLHttpRequest,\"withCredentials\"in e))"
              + "     { "
              + "       n=!0,"
              + "       e.onreadystatechange="
              + "            function()"
              + "            {"
              + "                4==e.readyState"
              + "                   && 200==e.status"
              + "                   && 2==e.responseText.length"
              + "                   && "
              + "                      (iCookieExpire=7, "
              + "                        o=e.responseText.toUpperCase(), "
              + "                        i.SetCookie(i.CookieKey,o,iCookieExpire), "
              + "                        i.CookieValue=o)"
              + "            };"
              + "            try{e.open(\"GET\",t,!0),e.withCredentials=!0,e.send(null)}catch(r){console.log(\"cerror1\")}"
              + "     }"
              + "     if(n===!1&&window.XDomainRequest)"
              + "     {"
              + "       e=new XDomainRequest,"
              + "       e.onload=function()"
              + "                 {"
              + "                   2==e.responseText.length&&(iCookieExpire=7,o=e.responseText.toUpperCase(),i.SetCookie(i.CookieKey,i.sCountry,iCookieExpire),i.CookieValue=o)"
              + "                 };"
              + "        try{e.open(\"GET\",t),e.send(null)}catch(r){console.log(\"cerror2\")}"
              + "     }"
              + "  },"
              + " LoadCountry:function(){var e;this.AreCookiesEnabled()&&(e=this.GetCookie(this.CookieKey),\"undefined\"==typeof e?this.SetCountryFromServer():this.CookieValue=e)},"
              + " IsUSA:function(){return this.LoadCountry(),\"US\"===this.CookieValue},"
              + " IsIntl:function(){return this.IsUSA()===!1},"
              + " Country:function(){return this.LoadCountry(),this.CookieValue},"
              + " OnCountryRedirect:function(e,o){this.Country()===e.toUpperCase()&&window.location.replace(o)}"
              + "}"
              + "}"
              + "]"
              + ",2:"
              + "[function(e,o,t){(function(o){o.ZDXI=e(\"../lib/detectCountry.js\")}).call(this,\"undefined\"!=typeof self?self:\"undefined\"!=typeof window?window:{})},{\"../lib/detectCountry.js\":1}]"
              + "},"
              + "{},"
              + "[2]);";
      assertAst(testInfo, code);
   }
     
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "!function e(o,t,n){function i(u,s){if(!t[u]){if(!o[u]){var c=\"function\"==typeof require&&require;if(!s&&c)return c(u,!0);if(r)return r(u,!0);throw new Error(\"Cannot find module '\"+u+\"'\")}var a=t[u]={exports:{}};o[u][0].call(a.exports,function(e){var t=o[u][1][e];return i(t?t:e)},a,a.exports,e,o,t,n)}return t[u].exports}for(var r=\"function\"==typeof require&&require,u=0;u<n.length;u++)i(n[u]);return i}({1:[function(e,o,t){o.exports={CookieKey:\"_USERCOUNTRY6\",CookieValue:\"US\",SetCookie:function(e,o,t){var n=new Date,i=t;n.setTime(n.getTime()+24*i*60*60*1e3),document.cookie=e+\"=\"+escape(o)+\"; path=/\"+(null===n?\"\":\"; expires=\"+n.toGMTString())},GetCookie:function(e){var o=e+\"=\";return document.cookie.length>0&&(offset=document.cookie.indexOf(o),-1!=offset)?(offset+=o.length,end=document.cookie.indexOf(\";\",offset),-1==end&&(end=document.cookie.length),unescape(document.cookie.substring(offset,end))):void 0},AreCookiesEnabled:function(){var e=!!navigator.cookieEnabled;return\"undefined\"!=typeof navigator.cookieEnabled||e||(document.cookie=\"testcookie\",e=-1!=document.cookie.indexOf(\"testcookie\")),e},SetCountryFromServer:function(){var e,o=\"US\",t=\"\",n=!1,i=this;if(t=/.*pcmag\\.com$/.test(location.hostname)?\"//geo.pcmag.com/loc/country.php\":\"//geo.ziffdavis.com/loc/country.php\",this.SetCookie(this.CookieKey,o,1),window.XMLHttpRequest&&(e=new XMLHttpRequest,\"withCredentials\"in e)){n=!0,e.onreadystatechange=function(){4==e.readyState&&200==e.status&&2==e.responseText.length&&(iCookieExpire=7,o=e.responseText.toUpperCase(),i.SetCookie(i.CookieKey,o,iCookieExpire),i.CookieValue=o)};try{e.open(\"GET\",t,!0),e.withCredentials=!0,e.send(null)}catch(r){console.log(\"cerror1\")}}if(n===!1&&window.XDomainRequest){e=new XDomainRequest,e.onload=function(){2==e.responseText.length&&(iCookieExpire=7,o=e.responseText.toUpperCase(),i.SetCookie(i.CookieKey,i.sCountry,iCookieExpire),i.CookieValue=o)};try{e.open(\"GET\",t),e.send(null)}catch(r){console.log(\"cerror2\")}}},LoadCountry:function(){var e;this.AreCookiesEnabled()&&(e=this.GetCookie(this.CookieKey),\"undefined\"==typeof e?this.SetCountryFromServer():this.CookieValue=e)},IsUSA:function(){return this.LoadCountry(),\"US\"===this.CookieValue},IsIntl:function(){return this.IsUSA()===!1},Country:function(){return this.LoadCountry(),this.CookieValue},OnCountryRedirect:function(e,o){this.Country()===e.toUpperCase()&&window.location.replace(o)}}},{}],2:[function(e,o,t){(function(o){o.ZDXI=e(\"../lib/detectCountry.js\")}).call(this,\"undefined\"!=typeof self?self:\"undefined\"!=typeof window?window:{})},{\"../lib/detectCountry.js\":1}]},{},[2]);";
        assertAst(testInfo, code);
    }

    @Test
    public void test_03(TestInfo testInfo) {
        String code = "!function e(o,t,n){"
                + "    function i(u,s){"
                + "        if(!t[u]){"
                + "            if(!o[u]){"
                + "                var c=\"function\"==typeof require && require;"
                + "                if(!s&&c)return c(u,!0);"
                + "                if(r)return r(u,!0);"
                + "                throw new Error(\"Cannot find module '\"+u+\"'\")"
                + "            }"
                + "            var a=t[u]={exports:{}};"
                + "            o[u][0].call(a.exports,function(e){"
                + "                var t=o[u][1][e];"
                + "                return i(t?t:e)"
                + "            },a,a.exports,e,o,t,n)"
                + "        }"
                + "        return t[u].exports"
                + "    }"
                + "    for(var r=\"function\"==typeof require&&require,u=0;u<n.length;u++)i(n[u]);"
                + "    return i"
                + "}";
        assertAst(testInfo, code);
    }

    @Test
    public void test_04(TestInfo testInfo) {
        String code = 
                  "function(e,o,t){"
                + " var n=new Date,i=t;"
                + " n.setTime(n.getTime()+24*i*60*60*1e3),"
                + " document.cookie=e+\"=\"+escape(o)+\"; path=/\"+(null===n?\"\":\"; expires=\"+n.toGMTString())"
                + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = 
              "function(e){"
            + "var o=e+\"=\";"
            + "return "
            + "document.cookie.length>0 "
            + " && "
            + "   (offset=document.cookie.indexOf(o),-1!=offset)"
            + "    ? ("
            + "        offset+=o.length,"
            + "        end=document.cookie.indexOf(\";\",offset),"
            + "        -1==end&&(end=document.cookie.length),"
            + "        unescape(document.cookie.substring(offset,end))"
            + "     )"
            + "    : void 0"
            + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = " function(){" + "var e=!!navigator.cookieEnabled;"
                + "return \"undefined\"!= typeof navigator.cookieEnabled " 
                + " || e"
                + " || (document.cookie=\"testcookie\", e=-1!=document.cookie.indexOf(\"testcookie\")), e" 
                + "}";
        assertAst(testInfo, code);
    }

    @Test
    public void test_07(TestInfo testInfo) {
        String code = "function(){var e,o=\"US\",t=\"\",n=!1,i=this;if(t=/.*pcmag\\.com$/.test(location.hostname)?\"//geo.pcmag.com/loc/country.php\":\"//geo.ziffdavis.com/loc/country.php\",this.SetCookie(this.CookieKey,o,1),window.XMLHttpRequest&&(e=new XMLHttpRequest,\"withCredentials\"in e)){n=!0,e.onreadystatechange=function(){4==e.readyState&&200==e.status&&2==e.responseText.length&&(iCookieExpire=7,o=e.responseText.toUpperCase(),i.SetCookie(i.CookieKey,o,iCookieExpire),i.CookieValue=o)};try{e.open(\"GET\",t,!0),e.withCredentials=!0,e.send(null)}catch(r){console.log(\"cerror1\")}}if(n===!1&&window.XDomainRequest){e=new XDomainRequest,e.onload=function(){2==e.responseText.length&&(iCookieExpire=7,o=e.responseText.toUpperCase(),i.SetCookie(i.CookieKey,i.sCountry,iCookieExpire),i.CookieValue=o)};try{e.open(\"GET\",t),e.send(null)}catch(r){console.log(\"cerror2\")}}}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = "function(){"
                + "var e,o=\"US\",t=\"\",n=!1,i=this;"
                + "if("
                + "t=/.*pcmag\\.com$/.test(location.hostname)?\"//geo.pcmag.com/loc/country.php\":\"//geo.ziffdavis.com/loc/country.php\","
                + "this.SetCookie(this.CookieKey,o,1)"
                + ","
                + "window.XMLHttpRequest&&(e=new XMLHttpRequest,\"withCredentials\"in e)"
                + ")"
                + "{"
                + "n=!0,"
                + "e.onreadystatechange="
                + "function(){4==e.readyState&&200==e.status&&2==e.responseText.length&&(iCookieExpire=7,o=e.responseText.toUpperCase(),i.SetCookie(i.CookieKey,o,iCookieExpire),i.CookieValue=o)};"
                + "try{e.open(\"GET\",t,!0),e.withCredentials=!0,e.send(null)}"
                + "catch(r){console.log(\"cerror1\")}"
                + "}"
                + "if(n===!1&&window.XDomainRequest)"
                + "{"
                + "e=new XDomainRequest,"
                + "e.onload=function(){2==e.responseText.length&&(iCookieExpire=7,o=e.responseText.toUpperCase(),i.SetCookie(i.CookieKey,i.sCountry,iCookieExpire),i.CookieValue=o)};"
                + "try{e.open(\"GET\",t),e.send(null)}"
                + "catch(r){console.log(\"cerror2\")}"
                + "}"
                + "}";
        assertAst(testInfo, code);
    }

    @Test
    public void test_09(TestInfo testInfo) {
        String code = "window.XMLHttpRequest && (e=new XMLHttpRequest,\"withCredentials\"in e)";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code = "if ("
                + "t=/.*pcmag\\.com$/.test(location.hostname)?\"//geo.pcmag.com/loc/country.php\":\"//geo.ziffdavis.com/loc/country.php\""
                + ","
                + "this.SetCookie(this.CookieKey,o,1)"
                + ","
                + "window.XMLHttpRequest&&(e=new XMLHttpRequest,\"withCredentials\"in e)"
                + ")"
                + "{}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_11(TestInfo testInfo) {
        String code = "/.*pcmag\\.com$/.test(location.hostname)?\"//geo.pcmag.com/loc/country.php\":\"//geo.ziffdavis.com/loc/country.php\"";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_12(TestInfo testInfo) {
        String code = "t=/.*pcmag\\.com$/.test(location.hostname)?\"//geo.pcmag.com/loc/country.php\":\"//geo.ziffdavis.com/loc/country.php\"";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        String code = 
              "t=/.*pcmag\\.com$/.test(location.hostname)?\"//geo.pcmag.com/loc/country.php\":\"//geo.ziffdavis.com/loc/country.php\""
            + ","
            + "window.XMLHttpRequest && (e = new XMLHttpRequest, \"withCredentials\" in e)";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_14(TestInfo testInfo) {
        String code = 
              "(iCookieExpire=7, "
            + "o=e.responseText.toUpperCase(), "
            + "i.SetCookie(i.CookieKey,o,iCookieExpire), "
            + "i.CookieValue=o)";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_15(TestInfo testInfo) {
        String code = 
              "(Cookie_1=7, "
            + "o=e.responseText.Cookie_2(), "
            + "i.SetCookie(Cookie_3), "
            + "i.Cookie_4=o)";
        assertAst(testInfo, code);
    }

    @Test
    public void test_16(TestInfo testInfo) {
        String code = "function(){var e;this.AreCookiesEnabled()&&(e=this.GetCookie(this.CookieKey),\"undefined\"==typeof e?this.SetCountryFromServer():this.CookieValue=e)}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_17(TestInfo testInfo) {
        String code = "function(){return this.LoadCountry(),\"US\"===this.CookieValue}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_18(TestInfo testInfo) {
        String code = "function(){return this.IsUSA()===!1}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_19(TestInfo testInfo) {
        String code = "\"undefined\"!=typeof self ? self: \"undefined\"!=typeof window ? window: {}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_20(TestInfo testInfo) {
        String code = 
                  "v=["
                + "function(e,o,t){"
                + " (function(o){o.ZDXI=e(\"../lib/detectCountry.js\")})"
                + "        dd.call(this, \"undefined\"!=typeof self ? self: \"undefined\"!=typeof window ? window: {})"
                + "}"
                + ","
                + "{\"../lib/detectCountry.js\":1}"
                + "]";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_21(TestInfo testInfo) {
        String[] code = {
              "!function() {",
              "  var t,e,",
              "    n= {",
              "        696:function(t,e) {},",
              "        9128:function(t,e,n) {",
              "              \"use strict\";",
              "              function r(t,e,n){",
              "                this.off=function() {",
              "                   e.forEach(",
              "                      function(e) {",
              "                        var n=i[e];",
              "                        n && (t[e]=n, delete i[e])",
              "                      }",
              "                    )",
              "                }",
              "              }",
              "             }",
              "       }",
              "}"
      };
        assertAst(testInfo, code);
    }

    @Test
    public void test_22(TestInfo testInfo) {
        String code = "2 ** (3 ** 2) // 512\n; (2 ** 3) ** 2 // 64";
        assertAst(testInfo, code);
    }

    @Test
    public void test_23(TestInfo testInfo) {
        String code = "e = new XMLHttpRequest, \"withCredentials\" in e";
        assertAst(testInfo, code);
    }

}
