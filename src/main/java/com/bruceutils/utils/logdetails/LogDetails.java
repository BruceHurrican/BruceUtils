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


/**
 * Created by pengwei08 on 2015/7/16.
 * 日志管理器
 */
public final class LogDetails {

    private static Logger printer = new Logger();
    private static LogConfigImpl logConfig = LogConfigImpl.getInstance();

    /**
     * 选项配置
     *
     * @return
     */
    public static LogConfig getLogConfig() {
        return logConfig;
    }

    public static Printer tag(String tag) {
        return printer.setTag(tag);
    }

    /**
     * verbose输出
     *
     * @param msg
     * @param args
     */
    public static void v(String msg, Object... args) {
        printer.v(msg, args);
    }

    public static void v(Object object) {
        printer.v(object);
    }


    /**
     * debug输出
     *
     * @param msg
     * @param args
     */
    public static void d(String msg, Object... args) {
        printer.d(msg, args);
    }

    public static void d(Object object) {
        printer.d(object);
    }

    /**
     * info输出
     *
     * @param msg
     * @param args
     */
    public static void i(String msg, Object... args) {
        printer.i(msg, args);
    }

    public static void i(Object object) {
        printer.i(object);
    }

    /**
     * warn输出
     *
     * @param msg
     * @param args
     */
    public static void w(String msg, Object... args) {
        printer.w(msg, args);
    }

    public static void w(Object object) {
        printer.w(object);
    }

    /**
     * error输出
     *
     * @param msg
     * @param args
     */
    public static void e(String msg, Object... args) {
        printer.e(msg, args);
    }

    public static void e(Object object) {
        printer.e(object);
    }

    /**
     * assert输出
     *
     * @param msg
     * @param args
     */
    public static void wtf(String msg, Object... args) {
        printer.wtf(msg, args);
    }

    public static void wtf(Object object) {
        printer.wtf(object);
    }

    /**
     * 打印json
     *
     * @param json
     */
    public static void json(String json) {
        printer.json(json);
    }

    /**
     * 输出xml
     * @param xml
     */
    public static void xml(String xml) {
        printer.xml(xml);
    }
}
