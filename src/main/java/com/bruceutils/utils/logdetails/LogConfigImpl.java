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


import android.text.TextUtils;

import com.bruceutils.utils.logdetails.pattern.LogPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengwei on 16/3/4.
 * Log config
 */
class LogConfigImpl implements LogConfig {

    private boolean enable = true;
    private String tagPrefix;
    private boolean showBorder = false;
    @LogLevel.LogLevelType
    private int logLevel = LogLevel.TYPE_VERBOSE;
    private List<Parser> parseList;
    private String formatTag;

    private static LogConfigImpl singleton;

    private LogConfigImpl() {
        parseList = new ArrayList<>();
    }

    static LogConfigImpl getInstance() {
        if (singleton == null) {
            synchronized (LogConfigImpl.class) {
                if (singleton == null) {
                    singleton = new LogConfigImpl();
                }
            }
        }
        return singleton;
    }

    @Override
    public LogConfig configAllowLog(boolean allowLog) {
        this.enable = allowLog;
        return this;
    }

    @Override
    public LogConfig configTagPrefix(String prefix) {
        this.tagPrefix = prefix;
        return this;
    }

    @Override
    public LogConfig configFormatTag(String formatTag) {
        this.formatTag = formatTag;
        return this;
    }

    public String getFormatTag(StackTraceElement caller) {
        if (TextUtils.isEmpty(formatTag)) {
            return null;
        }
        return LogPattern.compile(formatTag).apply(caller);
    }

    @Override
    public LogConfig configShowBorders(boolean showBorder) {
        this.showBorder = showBorder;
        return this;
    }

    @Override
    public LogConfig configLevel(@LogLevel.LogLevelType int logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    @Override
    public LogConfig addParserClass(Class<? extends Parser>... classes) {
        // TODO: 16/3/12 判断解析类的继承关系
        for (Class<? extends Parser> cla : classes) {
            try {
                parseList.add(0, cla.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getTagPrefix() {
        if (TextUtils.isEmpty(tagPrefix)) {
            return "LogDetails-";
        }

        return tagPrefix;
    }

    public boolean isShowBorder() {
        return showBorder;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public List<Parser> getParseList() {
        return parseList;
    }
}
