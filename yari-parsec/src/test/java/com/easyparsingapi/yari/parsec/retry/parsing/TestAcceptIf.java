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
package com.easyparsingapi.yari.parsec.retry.parsing;

import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.parse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Terminals;
import com.easyparsingapi.yari.parsec.util.ast.AssertUtil.Result;

public class TestAcceptIf {
    
    
    @Test
    public void test_01(TestInfo testInfo) {
        record ResultList(List<String> a, List<String> b) {}
        Result<ResultList> result = 
            parse(Parsers.sequence(Terminals.identifier().acceptIf(v -> v.contains("a")).many(), 
                                   Terminals.identifier().many(),
                                   (a, b) -> new ResultList(a, b)),
                "a ab ac yy bb");
       System.err.println();
    }

}
