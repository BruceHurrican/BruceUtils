/*
 * BruceHurrican
 *    Copyright (c) 2016.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *    This document is Bruce's individual learning the android demo, wherein the use of the code from the Internet, only to use as a learning exchanges.
 *    And where any person can download and use, but not for commercial purposes.
 *    Author does not assume the resulting corresponding disputes.
 *    If you have good suggestions for the code, you can contact BurrceHurrican@foxmail.com
 *    本文件为Bruce's个人学习android的demo, 其中所用到的代码来源于互联网，仅作为学习交流使用。
 *    任和何人可以下载并使用, 但是不能用于商业用途。
 *    作者不承担由此带来的相应纠纷。
 *    如果对本代码有好的建议，可以联系BurrceHurrican@foxmail.com
 */

package com.bruceutils.utils.logdetails;

import com.bruceutils.utils.logdetails.parsers.BundleParse;
import com.bruceutils.utils.logdetails.parsers.CollectionParse;
import com.bruceutils.utils.logdetails.parsers.IntentParse;
import com.bruceutils.utils.logdetails.parsers.MapParse;
import com.bruceutils.utils.logdetails.parsers.ReferenceParse;
import com.bruceutils.utils.logdetails.parsers.ThrowableParse;

import java.util.List;

/**
 * Created by pengwei on 16/4/18.
 */
public class Constant {

    public static final String STRING_OBJECT_NULL = "Object[object is null]";

    // 每行最大日志长度
    public static final int LINE_MAX = 1024 * 3;

    // 解析属性最大层级
    public static final int MAX_CHILD_LEVEL = 2;

    public static final int MIN_STACK_OFFSET = 5;

    // 换行符
    public static final String BR = System.getProperty("line.separator");

    // 空格
    public static final String SPACE = "\t";

    // 默认支持解析库
    public static final Class<? extends Parser>[] DEFAULT_PARSE_CLASS = new Class[]{
            BundleParse.class, IntentParse.class, CollectionParse.class,
            MapParse.class, ThrowableParse.class, ReferenceParse.class
    };


    /**
     * 获取默认解析类
     *
     * @return
     */
    public static final List<Parser> getParsers() {
        return LogConfigImpl.getInstance().getParseList();
    }
}
