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
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.MissingFormatArgumentException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import static com.bruceutils.utils.logdetails.ldutils.ObjectUtil.objectToString;
import static com.bruceutils.utils.logdetails.ldutils.Utils.*;
import static com.bruceutils.utils.logdetails.LogLevel.*;

/**
 * Created by pengwei08 on 2015/7/20.
 */
// TODO: 16/3/22 泛型支持
class Logger implements Printer {

    private LogConfigImpl mLogConfig;
    private final ThreadLocal<String> localTags = new ThreadLocal<>();

    protected Logger() {
        mLogConfig = LogConfigImpl.getInstance();
        mLogConfig.addParserClass(Constant.DEFAULT_PARSE_CLASS);
    }

    /**
     * 设置临时tag
     *
     * @param tag
     * @return
     */
    public Printer setTag(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            localTags.set(tag);
        }
        return this;
    }

    /**
     * 打印字符串
     *
     * @param type
     * @param msg
     * @param args
     */
    private synchronized void logString(@LogLevelType int type, String msg, Object... args) {
        logString(type, msg, false, args);
    }

    private void logString(@LogLevelType int type, String msg, boolean isPart, Object... args) {
        if (!mLogConfig.isEnable()) {
            return;
        }
        if (type < mLogConfig.getLogLevel()) {
            return;
        }
        String tag = generateTag();
        if (msg.length() > Constant.LINE_MAX) {
            if (mLogConfig.isShowBorder()) {
                printLog(type, tag, printDividingLine(DIVIDER_TOP));
                printLog(type, tag, printDividingLine(DIVIDER_NORMAL) + getTopStackInfo());
                printLog(type, tag, printDividingLine(DIVIDER_CENTER));
            }
            for (String subMsg : largeStringToList(msg)) {
                logString(type, subMsg, true, args);
            }
            if (mLogConfig.isShowBorder()) {
                printLog(type, tag, printDividingLine(DIVIDER_BOTTOM));
            }
            return;
        }
        if (args.length > 0) {
            try {
                msg = String.format(msg, args);
            } catch (MissingFormatArgumentException e) {

            }
        }
        if (mLogConfig.isShowBorder()) {
            if (isPart) {
                for (String sub : msg.split(Constant.BR)) {
                    printLog(type, tag, printDividingLine(DIVIDER_NORMAL) + sub);
                }
            } else {
                printLog(type, tag, printDividingLine(DIVIDER_TOP));
                printLog(type, tag, printDividingLine(DIVIDER_NORMAL) + getTopStackInfo());
                printLog(type, tag, printDividingLine(DIVIDER_CENTER));
                for (String sub : msg.split(Constant.BR)) {
                    printLog(type, tag, printDividingLine(DIVIDER_NORMAL) + sub);
                }
                printLog(type, tag, printDividingLine(DIVIDER_BOTTOM));
            }
        } else {
            printLog(type, tag, msg);
        }
    }


    /**
     * 打印对象
     *
     * @param type
     * @param object
     */
    private void logObject(@LogLevelType int type, Object object) {
        logString(type, objectToString(object));
    }

    /**
     * 自动生成tag
     *
     * @return
     */
    private String generateTag() {
        String tempTag = localTags.get();
        if (!TextUtils.isEmpty(tempTag)) {
            localTags.remove();
            if (mLogConfig.isShowBorder()) {
                return tempTag;
            }
            return tempTag + "/" + getTopStackInfo();
        }
        if (!mLogConfig.isShowBorder()) {
            return mLogConfig.getTagPrefix() + "/" + getTopStackInfo();
        }
        return mLogConfig.getTagPrefix();
    }

    /**
     * 获取当前activity栈信息
     *
     * @return
     */
    private StackTraceElement getCurrentStackTrace() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset = getStackOffset(trace, LogDetails.class);
        if (stackOffset == -1) {
            stackOffset = getStackOffset(trace, Logger.class);
            if (stackOffset == -1) {
                return null;
            }
        }
        StackTraceElement caller = trace[stackOffset];
        return caller;
    }

    /**
     * 获取最顶部stack信息
     *
     * @return
     */
    private String getTopStackInfo() {
        String customTag = mLogConfig.getFormatTag(getCurrentStackTrace());
        if (customTag != null) {
            return customTag;
        }
        StackTraceElement caller = getCurrentStackTrace();
        String stackTrace = caller.toString();
        stackTrace = stackTrace.substring(stackTrace.lastIndexOf('('), stackTrace.length());
        String tag = "%s.%s%s";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), stackTrace);
        return tag;
    }

    private int getStackOffset(StackTraceElement[] trace, Class cla) {
        for (int i = Constant.MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (cla.equals(Logger.class) && i < trace.length - 1 && trace[i + 1].getClassName()
                    .equals(Logger.class.getName())) {
                continue;
            }
            if (name.equals(cla.getName())) {
                return ++i;
            }
        }
        return -1;
    }

    @Override
    public void d(String message, Object... args) {
        logString(TYPE_DEBUG, message, args);
    }

    @Override
    public void d(Object object) {
        logObject(TYPE_DEBUG, object);
    }

    @Override
    public void e(String message, Object... args) {
        logString(TYPE_ERROR, message, args);
    }

    @Override
    public void e(Object object) {
        logObject(TYPE_ERROR, object);
    }

    @Override
    public void w(String message, Object... args) {
        logString(TYPE_WARM, message, args);
    }

    @Override
    public void w(Object object) {
        logObject(TYPE_WARM, object);
    }

    @Override
    public void i(String message, Object... args) {
        logString(TYPE_INFO, message, args);
    }

    @Override
    public void i(Object object) {
        logObject(TYPE_INFO, object);
    }

    @Override
    public void v(String message, Object... args) {
        logString(TYPE_VERBOSE, message, args);
    }

    @Override
    public void v(Object object) {
        logObject(TYPE_VERBOSE, object);
    }

    @Override
    public void wtf(String message, Object... args) {
        logString(TYPE_WTF, message, args);
    }

    @Override
    public void wtf(Object object) {
        logObject(TYPE_WTF, object);
    }

    /**
     * 采用orhanobut/logger的json解析方案
     * source:https://github.com/orhanobut/logger/blob/master/logger/src/main/java/com/orhanobut/logger/LoggerPrinter.java#L152
     *
     * @param json
     */
    @Override
    public void json(String json) {
        int indent = 4;
        if (TextUtils.isEmpty(json)) {
            d("JSON{json is empty}");
            return;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String msg = jsonObject.toString(indent);
                d(msg);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String msg = jsonArray.toString(indent);
                d(msg);
            }
        } catch (JSONException e) {
            e(e.toString() + "\n\njson = " + json);
        }
    }

    /**
     * 采用orhanobut/logger的xml解析方案
     * source:https://github.com/orhanobut/logger/blob/master/logger/src/main/java/com/orhanobut/logger/LoggerPrinter.java#L180
     *
     * @param xml
     */
    @Override
    public void xml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            d("XML{xml is empty}");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e(e.toString() + "\n\nxml = " + xml);
        }
    }


    /**
     * 打印日志
     *
     * @param type
     * @param tag
     * @param msg
     */
    private void printLog(@LogLevelType int type, String tag, String msg) {
        switch (type) {
            case TYPE_VERBOSE:
                Log.v(tag, msg);
                break;
            case TYPE_DEBUG:
                Log.d(tag, msg);
                break;
            case TYPE_INFO:
                Log.i(tag, msg);
                break;
            case TYPE_WARM:
                Log.w(tag, msg);
                break;
            case TYPE_ERROR:
                Log.e(tag, msg);
                break;
            case TYPE_WTF:
                Log.wtf(tag, msg);
                break;
            default:
                break;
        }
    }

}
