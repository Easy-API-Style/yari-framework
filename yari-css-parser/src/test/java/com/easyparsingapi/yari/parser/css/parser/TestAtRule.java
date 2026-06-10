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

public class TestAtRule {

    @Test
    public void test_01(TestInfo testInfo) {
        String code = """
           @charset "UTF-8";
           @charset "iso-8859-15";
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_02(TestInfo testInfo) {
        String code = """
          @color-profile --swop5c {
                src: url("https://example.org/SWOP2006_Coated5v2.icc");
          }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_03(TestInfo testInfo) {
        String code = """
            /* With a <size-query> */
            @container (width > 400px) {
              h2 {
                font-size: 1.5em;
              }
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_04(TestInfo testInfo) {
        String code = """
            /* With an optional <container-name> */
            @container tall (height > 30rem) {
              p {
                line-height: 1.6;
              }
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_05(TestInfo testInfo) {
        String code = """
            /* With a <scroll-state> */
            @container scroll-state(scrollable: top) {
              .back-to-top-link {
                visibility: visible;
              }
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_06(TestInfo testInfo) {
        String code = """
            /* With a <container-name> and a <scroll-state> */
            @container sticky-heading scroll-state(stuck: top) {
              h2 {
                background: purple;
                color: white;
              }
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_07(TestInfo testInfo) {
        String code = """
            /* Multiple queries in a single condition */
            @container (width > 400px) and style(--responsive: true) {
              h2 {
                font-size: 1.5em;
              }
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_08(TestInfo testInfo) {
        String code = """
            /* Condition list */
            @container card (width > 400px), style(--responsive: true), scroll-state(stuck: top) {
              h2 {
                font-size: 1.5em;
              }
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_09(TestInfo testInfo) {
        String code = """
            @container (width > 400px) and (height > 400px) {
              /* <stylesheet> */
            }
            
            @container (width > 400px) or (height > 400px) {
              /* <stylesheet> */
            }
            
            @container not (width < 400px) {
              /* <stylesheet> */
            }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code = """
            @container (min-width: 400px) {
              /* … */
            }
            @container (orientation: landscape) and (width > 400px) {
              /* … */
            }
            @container (15em <= block-size <= 30em) {
              /* … */
            }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_11(TestInfo testInfo) {
        String code = """
            @container scroll-state(scrollable: top) {
              /* … */
            }
            @container scroll-state(stuck: inline-end) {
              /* … */
            }
            @container scroll-state(snapped: both) {
              /* … */
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_12(TestInfo testInfo) {
        String code = """
            @container not scroll-state(scrollable: none) {
              /* … */
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_13(TestInfo testInfo) {
        String code = """
            @container scroll-state((stuck: top) and (stuck: left)) {
              /* … */
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_14(TestInfo testInfo) {
        String code = """
            @container summary (width > 400px) {
              @container (width > 800px) {
                /* <stylesheet> */
              }
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_15(TestInfo testInfo) {
        String code = """
            @container style(--themeBackground),
                not style(background-color: red),
                style(color: green) and style(background-color: transparent),
                style(--themeColor: blue) or style(--themeColor: purple) {
              /* <stylesheet> */
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_16(TestInfo testInfo) {
        String code = """
            @counter-style thumbs {
              system: cyclic;
              symbols: "\1F44D";
              suffix: " ";
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_17(TestInfo testInfo) {
        String code = """
            @document url("https://www.example.com/")
            {
              h1 {
                color: green;
              }
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_18(TestInfo testInfo) {
        String code = """
            @document url("http://www.w3.org/"),
                      url-prefix("http://www.w3.org/Style/"),
                      domain("mozilla.org"),
                      media-document("video"),
                      regexp("https:.*") {
              /* CSS rules here apply to:
                 - The page "http://www.w3.org/"
                 - Any page whose URL begins with "http://www.w3.org/Style/"
                 - Any page whose URL's host is "mozilla.org"
                   or ends with ".mozilla.org"
                 - Any standalone video
                 - Any page whose URL starts with "https:" */
            
              /* Make the above-mentioned pages really ugly */
              body {
                color: purple;
                background: yellow;
              }
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_19(TestInfo testInfo) {
        String code = """
            @font-face {
              font-family: "Trickster";
              src:
                local("Trickster"),
                url("trickster-COLRv1.otf") format("opentype") tech(color-COLRv1),
                url("trickster-outline.otf") format("opentype"),
                url("trickster-outline.woff") format("woff");
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_20(TestInfo testInfo) {
        String code = """
            @font-feature-values Font Name {
              font-display: swap;
              @styleset {
                nice-style: 12;
              }
              @swash {
                fancy: 2;
              }
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_21(TestInfo testInfo) {
        String code = """
            /* At-rule for "nice-style" in Font One */
            @font-feature-values Font One {
              @styleset {
                nice-style: 12;
              }
            }
            
            /* At-rule for "nice-style" in Font Two */
            @font-feature-values Font Two {
              @styleset {
                nice-style: 4;
              }
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_22(TestInfo testInfo) {
        String code = """
            @font-palette-values --identifier {
              font-family: "Bixa";
            }
            @font-palette-values --Alternate {
              font-family: "Bungee Spice";
              override-colors:
                0 #00ffbb,
                1 #007744;
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_23(TestInfo testInfo) {
        String code = """
            @function --anim-1s(--animation, --count) {
              --duration: 1s;
              --easing: linear;
              result: var(--animation) var(--duration) var(--count) var(--easing);
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_24(TestInfo testInfo) {
        String code = """
            @function --narrow-wide(--narrow, --wide) {
              result: var(--wide);
              @media (width < 700px) {
                result: var(--narrow);
              }
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_25(TestInfo testInfo) {
        String code = """
            @import url;
            @import url layer;
            @import url layer(layer-name);
            @import url layer(layer-name) supports(supports-condition);
            @import url layer(layer-name) supports(supports-condition) list-of-media-queries;
            @import url layer(layer-name) list-of-media-queries;
            @import url supports(supports-condition);
            @import url supports(supports-condition) list-of-media-queries;
            @import url list-of-media-queries;
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_26(TestInfo testInfo) {
        String code = """
            @import "fine-print.css" print;
            @import "bluish.css" print, screen;
            @import "common.css" screen;
            @import "landscape.css" screen and (orientation: landscape);
        """;
        assertAst(testInfo, code);
    }   

    @Test
    public void test_27(TestInfo testInfo) {
        String code = """
            @import "grid.css" supports(display: grid) screen and (width <= 400px);
            @import "flex.css" supports((not (display: grid)) and (display: flex)) screen
              and (width <= 400px);
        """;
        assertAst(testInfo, code);
    }   

    @Test
    public void test_28(TestInfo testInfo) {
        String code = """
            @import "whatever.css" supports((selector(h2 > p)) and (font-tech(color-COLRv1)));
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_29(TestInfo testInfo) {
        String code = """
            @keyframes slide-in {
              from {
                transform: translateX(0%);
              }
            
              to {
                transform: translateX(100%);
              }
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_30(TestInfo testInfo) {
        String code = """
            @keyframes identifier {
              0% {
                top: 0;
                left: 0;
              }
              30% {
                top: 50px;
              }
              68%,
              72% {
                left: 50px;
              }
              100% {
                top: 100px;
                left: 100%;
              }
            }
        """;
        assertAst(testInfo, code);
    }  
    
    @Test
    public void test_31(TestInfo testInfo) {
        String code = """
            @layer module, state;
        
            @layer state {
              .alert {
                background-color: brown;
              }
              p {
                border: medium solid limegreen;
              }
            }
        
            @layer module {
              .alert {
                border: medium solid violet;
                background-color: yellow;
                color: white;
              }
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_32(TestInfo testInfo) {
        String code = """
            @layer framework.layout {
              p {
                margin-block: 1rem;
              }
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_33(TestInfo testInfo) {
        String code = """
            @layer base, special;
            
            @layer special {
              .item {
                color: rebeccapurple;
              }
            }
            
            @layer base {
              .item {
                color: green;
                border: 5px solid green;
                font-size: 1.3em;
                padding: 0.5em;
              }
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_34(TestInfo testInfo) {
        String code = """
            abbr {
              color: #860304;
              font-weight: bold;
              transition: color 0.5s ease;
            }
            
            @media (hover: hover) {
              abbr:hover {
                color: #001ca8;
                transition-duration: 0.5s;
              }
            }
            
            @media not all and (hover: hover) {
              abbr::after {
                content: " (" attr(title) ")";
              }
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_35(TestInfo testInfo) {
        String code = """
            @media (400px <= width <= 700px) {
              body {
                line-height: 1.4;
              }
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_36(TestInfo testInfo) {
        String code = """
            @namespace svg url("http://www.w3.org/2000/svg");
            a {
              color: orangered;
              text-decoration: underline dashed;
              font-weight: bold;
            }
            svg|a {
              fill: blueviolet;
              text-decoration: underline solid;
              text-transform: uppercase;
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_37(TestInfo testInfo) {
        String code = """
            /* Targets all the pages */
            @page {
              size: 8.5in 9in;
              margin-top: 4in;
            }
            
            /* Targets all even-numbered pages */
            @page :left {
              margin-top: 4in;
            }
            
            /* Targets all odd-numbered pages */
            @page :right {
              size: 11in;
              margin-top: 4in;
            }
            
            /* Targets all selectors with `page: wide;` set */
            @page wide {
              size: a4 landscape;
            }
            
            @page {
              /* margin box at top right showing page number */
              @top-right {
                content: "Page " counter(pageNumber);
              }
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_38(TestInfo testInfo) {
        String code = """
            @position-try --custom-left {
              position-area: left;
              width: 100px;
              margin-right: 10px;
            }
            
            @position-try --custom-bottom {
              top: anchor(bottom);
              justify-self: anchor-center;
              margin-top: 10px;
              position-area: none;
            }
            
            @position-try --custom-right {
              left: calc(anchor(right) + 10px);
              align-self: anchor-center;
              width: 100px;
              position-area: none;
            }
            
            @position-try --custom-bottom-right {
              position-area: bottom right;
              margin: 10px 0 0 10px;
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_39(TestInfo testInfo) {
        String code = """
            @property --progress {
              syntax: "<percentage>";
              inherits: false;
              initial-value: 25%;
            }
            
            .bar {
              display: inline-block;
              --progress: 25%;
              width: 100%;
              height: 5px;
              background: linear-gradient(
                to right,
                #00d230 var(--progress),
                black var(--progress)
              );
              animation: progressAnimation 2.5s ease infinite;
            }
            
            @keyframes progressAnimation {
              to {
                --progress: 100%;
              }
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_40(TestInfo testInfo) {
        String code = """
            @scope (.article-body) to (figure) {
              img {
                border: 5px solid black;
                background-color: goldenrod;
              }
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_41(TestInfo testInfo) {
        String code = """
            @scope (.article-body) to (.feature :scope figure) {
              /* … */
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_42(TestInfo testInfo) {
        String code = """
            @scope (.article-body) to (.feature :scope figure) {
              /* … */
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_43(TestInfo testInfo) {
        String code = """
            @scope (.article-hero, .article-body) to (figure) {
                img {
                  border: 5px solid black;
                  background-color: goldenrod;
                }
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_44(TestInfo testInfo) {
        String code = """
            @starting-style {
              [popover]:popover-open {
                opacity: 0;
                transform: scaleX(0);
              }
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_45(TestInfo testInfo) {
        String code = """
            @supports (transform-origin: 5% 5%) {
            }
            @supports font-tech(color-COLRv1) {
            }
            @supports (display: flex) {
              .flex-container > * {
                text-shadow: 0 0 2px blue;
                float: none;
              }
            
              .flex-container {
                display: flex;
              }
            }
        """;
        assertAst(testInfo, code);
    }  
    
    @Test
    public void test_46(TestInfo testInfo) {
        String code = """
            @keyframes move-out {
              from {
                transform: translateY(0%);
              }
            
              to {
                transform: translateY(-100%);
              }
            }
        """;
        assertAst(testInfo, code);
    }  
    
    @Test
    public void test_47(TestInfo testInfo) {
        String code = """
              @supports (x: attr(x type(*))) {
                /* Browser has modern attr() support */
              }
        
              @supports not (x: attr(x type(*))) {
                /* Browser does not have modern attr() support */
              }
        """;
        assertAst(testInfo, code);
    } 

    @Test
    public void test_48(TestInfo testInfo) {
        String code = """
              @media not ((width > 1000px) and (color)), print and (color) {
                /* … */
              }
        """;
        assertAst(testInfo, code);
    } 

    @Test
    public void test_49(TestInfo testInfo) {
        String code = """
            @media (not (width > 1000px)) and (color), print and (color) {
              /* … */
            }
        """;
        assertAst(testInfo, code);
    } 

    @Test
    public void test_50(TestInfo testInfo) {
        String code = """
          @function --color-choice(--color1 type(red | green), --color2 blue) {
            result: var(--color1);
            @media (width < 700px) {
              result: var(--color2);
            }
          }
        """;
        assertAst(testInfo, code);
    } 

    @Test
    public void test_51(TestInfo testInfo) {
        String code = """
            @layer framework.layout.content {
              p {
                margin-block: 1rem;
              }
            }
        """;
        assertAst(testInfo, code);
    }  

    @Test
    public void test_52(TestInfo testInfo) {
        String code = """
            @supports not selector(:focus-visible) font-format(opentype) font-tech(color-COLRv1) {
            }
        """;
        assertAst(testInfo, code);
    }  
    
}
