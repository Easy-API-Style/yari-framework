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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node;

public class TestObjectDeclaration {

    private void assertJavascript(TestInfo testInfo, String... code) {
        assertAst(testInfo, Node.objectDeclaration, code);
    }
    
    @Test
    public void test_01(TestInfo testInfo) {
        String code = "{ toto: 67, bibi :  'tutu' }";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "{ 'toto': 67, bibi :  'tutu' }";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "{ 'toto': 67, bibi :  tutu.tyty.jiji[gogo] }";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "{ 'toto': 67, bibi :  {lili:tutu.tyty.jiji[gogo],fifi:-2.3} }";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = "{ 'toto': 67, 20 :  'tttt' }";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = 
              "{"
            + "  get [expr]() { return 'bar'; }"
            + "}";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = 
            "{"
          + "  baz: 'bar',"
          + "  set [expr](v) { this.baz = v; }"
          + "}";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = 
              "{"
            + "  [Symbol.asyncIterator]() {"
            + "    let i = 0;"
            + "    return 'value';"
            + "  }"
            + "}";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = "{ [items]: \"Hello\" }";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code = 
                  "{"
                + "  coords: { x: 18, y: 30 },"
                + "  radius: 30,"
                + "}";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_11(TestInfo testInfo) {
        String code = "{callback:window[\"gapi_onload\"],"
                + "platform:\"backdrop blogger comments commentcount community donation family_creation follow hangout health page partnersbadge person playemm playreview plus plusone post ratingbadge savetoandroidpay savetodrive savetowallet sharetoclassroom shortlists signin2 surveyoptin visibility youtube ytsubscribe zoomableimage\".split(\" \"),\n"
                + "}";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_12(TestInfo testInfo) {
        String code = "{callback:window[\"gapi_onload\"],_c:{url:\"https://apis.google.com/js/api.js\",jsl:{ci:{\"oauth-flow\":{authUrl:\"https://accounts.google.com/o/oauth2/auth\",proxyUrl:\"https://accounts.google.com/o/oauth2/postmessageRelay\",disableOpt:!0,idpIframeUrl:\"https://accounts.google.com/o/oauth2/iframe\",usegapi:!1},debug:{reportExceptionRate:1,forceIm:!1,rethrowException:!0,host:\"https://apis.google.com\"},gen204logger:{ interval: 30000, rate: 0.01, batch: false },enableMultilogin:!0,\"googleapis.config\":{auth:{useFirstPartyAuthV2:!0},root:\"https://content.googleapis.com\",\"root-1p\":\"https://clients6.google.com\"},\n"
         + "inline:{css:1},disableRealtimeCallback:!1,drive_share:{skipInitCommand:!0},csi:{rate:.01},client:{cors:!1},signInDeprecation:{rate:0},include_granted_scopes:!0,llang:\"fr\",iframes:{youtube:{params:{location:[\"search\",\"hash\"]},url:\":socialhost:/:session_prefix:_/widget/render/youtube?usegapi=1\",methods:[\"scroll\",\"openwindow\"]},ytsubscribe:{url:\"https://www.youtube.com/subscribe_embed?usegapi=1\"},plus_circle:{params:{url:\"\"},url:\":socialhost:/:session_prefix::se:_/widget/plus/circle?usegapi=1\"},\n"
         + "plus_share:{params:{url:\"\"},url:\":socialhost:/:session_prefix::se:_/+1/sharebutton?plusShare=true&usegapi=1\"},rbr_s:{params:{url:\"\"},url:\":socialhost:/:session_prefix::se:_/widget/render/recobarsimplescroller\"},\":source:\":\"3p\",playemm:{url:\"https://play.google.com/work/embedded/search?usegapi=1&usegapi=1\"},savetoandroidpay:{url:\"https://pay.google.com/gp/v/widget/save\"},blogger:{params:{location:[\"search\",\"hash\"]},url:\":socialhost:/:session_prefix:_/widget/render/blogger?usegapi=1\",methods:[\"scroll\",\n"
         + "\"openwindow\"]},evwidget:{params:{url:\"\"},url:\":socialhost:/:session_prefix:_/events/widget?usegapi=1\"},partnersbadge:{url:\"https://www.gstatic.com/partners/badge/templates/badge.html?usegapi=1\"},dataconnector:{url:\"https://dataconnector.corp.google.com/:session_prefix:ui/widgetview?usegapi=1\"},surveyoptin:{url:\"https://www.google.com/shopping/customerreviews/optin?usegapi=1\"},\":socialhost:\":\"https://apis.google.com\",shortlists:{url:\"\"},hangout:{url:\"https://talkgadget.google.com/:session_prefix:talkgadget/_/widget\"},\n"
         + "plus_followers:{params:{url:\"\"},url:\":socialhost:/_/im/_/widget/render/plus/followers?usegapi=1\"},post:{params:{url:\"\"},url:\":socialhost:/:session_prefix::im_prefix:_/widget/render/post?usegapi=1\"},signin:{params:{url:\"\"},url:\":socialhost:/:session_prefix:_/widget/render/signin?usegapi=1\",methods:[\"onauth\"]},rbr_i:{params:{url:\"\"},url:\":socialhost:/:session_prefix::se:_/widget/render/recobarinvitation\"},share:{url:\":socialhost:/:session_prefix::im_prefix:_/widget/render/share?usegapi=1\"},plusone:{params:{count:\"\",\n"
         + "size:\"\",url:\"\"},url:\":socialhost:/:session_prefix::se:_/+1/fastbutton?usegapi=1\"},comments:{params:{location:[\"search\",\"hash\"]},url:\":socialhost:/:session_prefix:_/widget/render/comments?usegapi=1\",methods:[\"scroll\",\"openwindow\"]},\":im_socialhost:\":\"https://plus.googleapis.com\",backdrop:{url:\"https://clients3.google.com/cast/chromecast/home/widget/backdrop?usegapi=1\"},visibility:{params:{url:\"\"},url:\":socialhost:/:session_prefix:_/widget/render/visibility?usegapi=1\"},autocomplete:{params:{url:\"\"},url:\":socialhost:/:session_prefix:_/widget/render/autocomplete\"},\n"
         + "\":signuphost:\":\"https://plus.google.com\",ratingbadge:{url:\"https://www.google.com/shopping/merchantverse/?usegapi=1\"},appcirclepicker:{url:\":socialhost:/:session_prefix:_/widget/render/appcirclepicker\"},follow:{url:\":socialhost:/:session_prefix:_/widget/render/follow?usegapi=1\"},community:{url:\":ctx_socialhost:/:session_prefix::im_prefix:_/widget/render/community?usegapi=1\"},sharetoclassroom:{url:\"https://classroom.google.com/sharewidget?usegapi=1\"},ytshare:{params:{url:\"\"},url:\":socialhost:/:session_prefix:_/widget/render/ytshare?usegapi=1\"},\n"
         + "plus:{url:\":socialhost:/:session_prefix:_/widget/render/badge?usegapi=1\"},family_creation:{params:{url:\"\"},url:\"https://families.google.com/webcreation?usegapi=1&usegapi=1\"},commentcount:{url:\":socialhost:/:session_prefix:_/widget/render/commentcount?usegapi=1\"},configurator:{url:\":socialhost:/:session_prefix:_/plusbuttonconfigurator?usegapi=1\"},zoomableimage:{url:\"https://ssl.gstatic.com/microscope/embed/\"},appfinder:{url:\"https://workspace.google.com/:session_prefix:marketplace/appfinder?usegapi=1\"},savetowallet:{url:\"https://pay.google.com/gp/v/widget/save\"},\n"
         + "person:{url:\":socialhost:/:session_prefix:_/widget/render/person?usegapi=1\"},savetodrive:{url:\"https://drive.google.com/savetodrivebutton?usegapi=1\",methods:[\"save\"]},page:{url:\":socialhost:/:session_prefix:_/widget/render/page?usegapi=1\"},card:{url:\":socialhost:/:session_prefix:_/hovercard/card\"}}},h:\"m;/_/scs/abc-static/_/js/k=gapi.lb.fr.-iW_3wPUacA.O/d=1/rs=AHpOoo_Gv83aoaKZzLvOKu-TOqSvx36WMg/m=__features__\",u:\"https://apis.google.com/js/api.js\",hee:!0,dpo:!1,le:[\"scs\"]},platform:\"backdrop blogger comments commentcount community donation family_creation follow hangout health page partnersbadge person playemm playreview plus plusone post ratingbadge savetoandroidpay savetodrive savetowallet sharetoclassroom shortlists signin2 surveyoptin visibility youtube ytsubscribe zoomableimage\".split(\" \"),\n"
         + "annotation:[\"interactivepost\",\"recobar\",\"signin2\",\"autocomplete\"]}}";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        String code = "{value:a=>l+s,configurable:a}";
        assertJavascript(testInfo, code);
    }
    
}
