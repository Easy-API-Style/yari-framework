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
package com.easyparsingapi.yari.parser.css.parser;

import static com.easyparsingapi.yari.parser.css.parser.AssertUtil.assertAst;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class TestProperty {
    
    @Test
    public void test_01(TestInfo testInfo) {
        String code = """
            .post {
              container: sidebar / inline-size;
            }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = """
            selector {
              property: value; /* normal declaration */
              property: value !important; /* important declaration (preferred) */
              property: value ! important; /* important declaration (not preferred) */
            }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = """
            .item {
                width: fit-content;
                background-color: #8ca0ff;
                padding: 5px;
                margin-bottom: 1em;
              }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_04(TestInfo testInfo) {
        String code = """
            .item {
                color: rgb(34 12 64 / 60%);
                /* Valeurs absolues */
                color: rgb(255 255 255);
                color: rgb(255 255 255 / 50%);
                
                /* Valeurs relatives */
                color: rgb(from green r g b / 0.5);
                color: rgb(from #123456 calc(r + 40) calc(g + 40) b);
                color: rgb(from hwb(120deg 10% 20%) r g calc(b + 200));
                
                /* Alias 'rgba()' hérité */
                color: rgba(0 255 255);
                
                /* Format hérité */
                color: rgb(0, 255, 255);
                color: rgb(0, 255, 255, 50%);
              }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = """
            div {
              --deg: -45deg;
              background-image: linear-gradient(abs(var(--deg)), blue, red);
              width: abs(20% - 100px)
            }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = """
            div {
                /* Valeurs numériques */
                /* Type <number> */
                transform: rotate(acos(-0.2));
                transform: rotate(acos(2 * 0.125));
            
                /* Autres valeurs */
                transform: rotate(acos(pi / 5));
                transform: rotate(acos(e / 3));
            }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = """
            div {
                /* sizing relative to anchor side */
                width: anchor-size(width);
                block-size: anchor-size(block);
                height: calc(anchor-size(self-inline) + 2em);
                
                /* sizing relative to named anchor's side */
                width: anchor-size(--my-anchor width);
                block-size: anchor-size(--my-anchor block);
                
                /* sizing relative to named anchor's side with fallback */
                width: anchor-size(--my-anchor width, 50%);
                block-size: anchor-size(--my-anchor block, 200px);
                
                /* positioning relative to anchor side */
                left: anchor-size(width);
                inset-inline-end: anchor-size(--my-anchor height, 100px);
                
                /* setting margin relative to anchor side */
                margin-left: calc(anchor-size(width) / 4);
                margin-block-start: anchor-size(--my-anchor self-block, 20px);
            }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = """
            div {
                /* Single <number> values */
                transform: rotate(asin(-0.2));
                transform: rotate(asin(2 * 0.125));
                
                /* Other values */
                transform: rotate(asin(pi / 5));
                transform: rotate(asin(e / 3));
            }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = """
            div {
                transform: attr(x type(*));
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_10(TestInfo testInfo) {
        String code = """
            div {
                /* calc(expression) */
                transform: calc(100% - 80px);
                /* Expression with a CSS function */
                transform: calc(100px * sin(pi / 2));
                /* Expression containing a variable */
                transform: calc(var(--hue) + 180);
                /* Expression with color channels in relative colors */
                transform: lch(from aquamarine l c calc(h + 180));
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_11(TestInfo testInfo) {
        String code = """
            div {
                /* Utilisation simple */
                content: url(https://example.com/images/monImg.jpg);
                content: url(data:image/png;base64,iRxVB0…);
                content: url(maPolice.woff);
                content: url(#IDdeCheminSVG);
                
                /* Propriétés utilisables */
                background-image: url("star.gif");
                list-style-image: url('../images/bullet.jpg');
                content: url("pdficon.jpg");
                cursor: url(moncurseur.cur);
                border-image-source: url(/media/diamonds.png);
                src: url('superpolice.woff');
                offset-path: url(#path);
                mask-image: url("masques.svg#masque1");
                
                /* Propriétés avec valeurs de recours */
                cursor: url(pointer.cur), pointer;
                
                /* Propriétés raccourcies associées */
                background: url('star.gif') bottom right repeat-x blue;
                border-image: url("/media/diamonds.png") 30 fill / 30px / 30px space;
                
                /* Utilisation comme paramètre d'une fonction CSS */
                background-image: cross-fade(20% url(first.png), url(second.png));
                mask-image: image(url(mask.png), skyblue, linear-gradient(rgba(0, 0, 0, 1.0), transparent));
                
                /* Utilisation avec plusieurs valeurs */
                content: url(star.svg) url(star.svg) url(star.svg) url(star.svg) url(star.svg);
                
                /* Règles @ / at-rules */
                @document url("https://www.example.com/") {  } /* expérimental */
                @import url("https://www.example.com/style.css");
                @namespace url(http://www.w3.org/1999/xhtml);
            }
        """;
        assertAst(testInfo, code);
    }
    
}
