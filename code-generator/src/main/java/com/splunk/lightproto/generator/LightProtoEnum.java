/**
 * Copyright 2020 Splunk Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.splunk.lightproto.generator;

import io.protostuff.parser.EnumGroup;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class LightProtoEnum {
    private final EnumGroup eg;

    public LightProtoEnum(EnumGroup eg) {
        this.eg = eg;
    }

    public String getName() {
        return eg.getName();
    }

    public void generate(PrintWriter w) {
        w.format("    public enum %s {\n", eg.getName());
        eg.getSortedValues().forEach(v -> {
            w.format("        %s(%d),\n", v.getName(), v.getNumber());
        });
        w.println("        ;");
        w.println("        private final int value;");
        w.format("        private %s(int value) {\n", eg.getName());
        w.println("            this.value = value;");
        w.println("        }");
        w.println("        public int getValue() {");
        w.println("            return value;");
        w.println("        }");

        w.format("        public static %s valueOf(int n) {\n", eg.getName());
        w.format("            switch (n) {\n");
        eg.getSortedValues().forEach(v -> {
            w.format("                case %d: return %s;\n", v.getNumber(), v.getName());
        });
        w.println("                default: return null;\n");
        w.println("            }");
        w.println("        }");
        eg.getSortedValues().forEach(v -> {
            w.format("     public static final int %s_VALUE = %d;\n", v.getName(), v.getNumber());
        });
        w.println("    }");
        w.println();
    }
}
