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
package com.easyparsingapi.yari.parser.xml.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.parser.xml.parser.XmlConfig;

public class TestXmlParser_02 {
    
    private static final XmlConfig XML_CONFIG_ACCEPT = 
        XmlConfig.builder()
                 .acceptUnclosedTag(true)
                 .build();
    
    private static final XmlConfig XML_CONFIG_NOT_ACCEPT  =
        XmlConfig.builder()
                 .acceptUnclosedTag(false)
                 .build();
    
    @Test
    public void test_01(TestInfo testInfo) {
        String code = """
           <test> 
             blabla
           </test>    
        """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_02(TestInfo testInfo) {
        String code = """
            <test att_1 att_2> 
              blabla
            </test>    
         """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_03(TestInfo testInfo) {
        String code = """
                <test att_1='11' att_2> 
                  blabla
                </test>    
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_04(TestInfo testInfo) {
        String code = """
                <test att_1="11" att_2> 
                  blabla
                </test>     
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_05(TestInfo testInfo) {
        String code = """
                <test att_1="11" att_2='22'> 
                  blabla
                </test>    
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_06(TestInfo testInfo) {
        String code = """
                <test att_1 att_2='22'> 
                  blabla
                </test>     
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_07(TestInfo testInfo) {
        String code = """
                <test att_1="11" att_2 att_3='33'> 
                  blabla
                </test>     
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_08(TestInfo testInfo) {
        String code = """
                <test> 
                  <div>
                     blabla
                  </div>
                </test>     
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = """
                <test> 
                  <title/>
                  <div>
                     blabla
                  </div>
                </test>     
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_10(TestInfo testInfo) {
        String code = """
                <test> 
                  <title />
                  <div>
                     blabla
                  </div>
                </test>     
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_11(TestInfo testInfo) {
        String code = """
                <test> 
                  <title att_1="11" att_2 att_3='33'/>
                  <div>
                     blabla
                  </div>
                </test>     
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }
    
    @Test
    public void test_12(TestInfo testInfo) {
        String code = """
                <test> 
                  <title att_1="11" att_2 att_3='33'/>
                  <div_1>
                     aaaaa
                     <div_2>
                        bbbb
                         <div_3>
                           dddd
                         </div_3>
                     </div_2>
                     cccc
                  </div_1>
                </test>     
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        String code = """
                <test> 
                  <title att_1="11" att_2 att_3='33'/>
                  <div>
                     aaaaa
                     bbbbb
                     cccc
                     
                  </div>
                </test>     
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }
    
    @Test
    public void test_14(TestInfo testInfo) {
        String code = """
                <?xml version="1.0" encoding="UTF-8"?>
                <?xml-stylesheet href="tei2html.xsl" type="text/xsl"?>
                <?xml-model
                    href="tei.rng"
                    type="application/xml"
                    schematypens="http://relaxng.org/ns/structure/1.0"?
                >
                <?erreur?>
                <TEI xmlns="http://www.tei-c.org/ns/1.0">
                    <!-- … -->
                </TEI> 
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_15(TestInfo testInfo) {
        String code = """
                <script><![CDATA[
                if (chars > 140 && mode == tweet) {
                    div.innerHTML = '<b>Attention !</b>, le {{140e}} caractère est dépassé !';
                }
                ]]></script>
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_16(TestInfo testInfo) {
        String code = """
            <?xml version="1.0" encoding="UTF-8"?>
            <?xml-stylesheet href="transformation.xsl" type="text/xsl"?>
            <?mode ecran?>
            <?instruction pour le traitement?>
            <!-- Commentaire -->
            <ex:collection
                xml:lang="fr"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
                xmlns="http://www.w3.org/1999/xhtml"
                xmlns:ex="http://exemple.org"
            >
                <élément>Texte</élément>
                <dc:title>Astérix le Gaulois</dc:title>
                <ex:livre attribut="valeur" type="BD">
                    <dc:title>Astérix chez les Belges</dc:title>
                    <!-- élément répété -->
                    <dc:creator>René Goscinny</dc:creator>
                    <dc:creator>Albert Uderzo</dc:creator>
                    <dc:description>
                        <b>Astérix chez les Belges</b> est un album de 
            <a href="http://fr.wikipedia.org/wiki/Bande_dessinée">bande dessinée</a> de la
            série Astérix le Gaulois créée par René Goscinny et Albert Uderzo.
            <br /><!-- élément vide -->
                        Cet album publié en 1979 est le dernier de la série écrite par René 
            Goscinny.
                    </dc:description>
                </ex:livre>
            </ex:collection>
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }
    
    @Test
    public void test_17(TestInfo testInfo) {
        String code = """
                <test> 
                  <title att_1="11" att_2 att_3='33'/>
                  <div>
                     aaaaa
                  </div>
                  </div>
                </test>     
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_18(TestInfo testInfo) {
        String code = """
                <test> 
                  <title att_1="11" att_2 att_3='33'/>
                  <div>
                     aaaaa
                  </div>
                  </div>
                </test>     
             """;
        AssertUtil.assertXmlAstError(testInfo, XML_CONFIG_NOT_ACCEPT, code);
    }

    @Test
    public void test_19(TestInfo testInfo) {
        String code = """
                <test> 
                  <title att_1="11" att_2 att_3='33'/>
                  <div>
                     aaaaa
                  </div>
                </test>     
             """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_20(TestInfo testInfo) {
        String code = """
                <test> 
                  <title att_1="11" att_2 att_3='33'>
                  <div>
                     aaaaa
                  </div>
                  </div>
                </test>     
             """;
        AssertUtil.assertXmlAstError(testInfo, XML_CONFIG_NOT_ACCEPT, code);
    }
    
    @Test
    public void test_21(TestInfo testInfo) {
        String code = """
           <pc:test> 
             blabla
           </pc:test>    
        """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }
    
    @Test
    public void test_22(TestInfo testInfo) {
        String code = """
           <pc:test> 
             <title oo:att_1="11">
             blabla
           </pc:test>    
        """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }
    
    @Test
    public void test_23(TestInfo testInfo) {
        String code = """
           <pc:test<> 
             blabla
           </pc:test>    
        """;
        AssertUtil.assertXmlAstError(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_24(TestInfo testInfo) {
        String code = """
           <pc:test attr_1 attr_2 = 'fff' <> 
             blabla
           </pc:test>    
        """;
        AssertUtil.assertXmlAstError(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_25(TestInfo testInfo) {
        String code = """
           <div>
             <div<
               blabla
             </div>    
           </div>    
        """;
        AssertUtil.assertXmlAstError(testInfo, XML_CONFIG_ACCEPT, code);
    }
    
    @Test
    public void test_26(TestInfo testInfo) {
        String code = """
           <div>
            <div>
             <div>
               blabla
             </div>    
           </div>    
        """;
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG_ACCEPT, code);
    }

    @Test
    public void test_27(TestInfo testInfo) {
        XmlConfig config = XmlConfig.builder()
                                    .acceptUnclosedTag(true)
                                    .tagAsPlainText("script")
                                    .build();
        String code = """
                <test> 
                    <script> 
                       function hello() {
                          return 'hello'
                       }
                       <div>ahh</div>
                       
                       function hi() {
                          return 'hi'
                       }
                       
                    </script>
                    <div> 
                      aaaa
                    </div>  
                </test>    
             """;
        AssertUtil.assertXmlAst(testInfo, config, code);
    }

    @Test
    public void test_28(TestInfo testInfo) {
        XmlConfig config = XmlConfig.builder()
                                    .acceptUnclosedTag(true)
                                    .build();
        String code = """
                <test> 
                    <script> 
                       function hello() {
                          return 'hello'
                       }
                       <div>ahh</div>
                       
                       function hi() {
                          return 'hi'
                       }
                       
                    </script>
                    <div> 
                      aaaa
                    </div>  
                </test>    
             """;
        AssertUtil.assertXmlAst(testInfo, config, code);
    }
    
}
