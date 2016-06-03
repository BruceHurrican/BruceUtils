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

package com.bruceutils.utils.logdetails.ldutils;


import com.bruceutils.utils.logdetails.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengwei on 16/4/19.
 */
public class Utils {

    // 分割线方位
    public static final int DIVIDER_TOP = 1;
    public static final int DIVIDER_BOTTOM = 2;
    public static final int DIVIDER_CENTER = 4;
    public static final int DIVIDER_NORMAL = 3;

    /**
     * 打印分割线
     *
     * @param dir
     * @return
     */
    public static String printDividingLine(int dir) {
        switch (dir) {
            case DIVIDER_TOP:
                return "╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════";
            case DIVIDER_BOTTOM:
                return "╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════";
            case DIVIDER_NORMAL:
                return "║ ";
            case DIVIDER_CENTER:
                return "╟───────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
            default:
                break;
        }
        return "";
    }


    /**
     * 长字符串转化为List
     *
     * @param msg
     * @return
     */
    public static List<String> largeStringToList(String msg) {
        List<String> stringList = new ArrayList<>();
        int index = 0;
        int maxLength = Constant.LINE_MAX;
        int countOfSub = msg.length() / maxLength;
        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                stringList.add(sub);
                index += maxLength;
            }
            stringList.add(msg.substring(index, msg.length()));
        } else {
            stringList.add(msg);
        }
        return stringList;
    }

    public static String shorten(String string, int count, int length) {
        if (string == null) return null;
        String resultString = string;
        if (Math.abs(length) < resultString.length()) {
            if (length > 0)
                resultString = string.substring(0, length);
            if (length < 0)
                resultString = string.substring(string.length() + length, string.length());
        }
        if (Math.abs(count) > resultString.length()) {
            return String.format("%" + count + "s", resultString);
        }
        return resultString;
    }

    public static String shortenClassName(String className, int count, int maxLength) throws Exception {
        className = shortenPackagesName(className, count);
        if (className == null) return null;
        if (maxLength == 0) return className;
        if (maxLength > className.length()) return className;
        if (maxLength < 0) {
            maxLength = -maxLength;
            StringBuilder builder = new StringBuilder();
            for (int index = className.length() - 1; index > 0; ) {
                int i = className.lastIndexOf('.', index);
                if (i == -1) {
                    if (builder.length() > 0
                            && builder.length() + index + 1 > maxLength) {
                        builder.insert(0, '*');
                        break;
                    }
                    builder.insert(0, className.substring(0, index + 1));
                } else {
                    if (builder.length() > 0
                            && builder.length() + (index + 1 - i) + 1 > maxLength) {
                        builder.insert(0, '*');
                        break;
                    }
                    builder.insert(0, className.substring(i, index + 1));
                }
                index = i - 1;
            }
            return builder.toString();
        } else {
            StringBuilder builder = new StringBuilder();
            for (int index = 0; index < className.length(); ) {
                int i = className.indexOf('.', index);
                if (i == -1) {
                    if (builder.length() > 0) {
                        builder.insert(builder.length(), '*');
                        break;
                    }
                    builder.insert(builder.length(), className.substring(index, className.length()));
                    break;
                } else {
                    if (builder.length() > 0
                            && i + 1 > maxLength) {
                        builder.insert(builder.length(), '*');
                        break;
                    }

                    builder.insert(builder.length(), className.substring(index, i + 1));
                }

                index = i + 1;
            }
            return builder.toString();
        }
    }

    // todo optimize it
    private static String shortenPackagesName(String className, int count) {
        if (className == null) return null;
        if (count == 0) return className;
        StringBuilder builder = new StringBuilder();
        if (count > 0) {
            int points = 1;
            for (int index = 0; index < className.length(); ) {
                int i = className.indexOf('.', index);
                if (i == -1) {
                    builder.insert(builder.length(), className.substring(index, className.length()));
                    break;
                } else {
                    if (points == count) {
                        builder.insert(builder.length(), className.substring(index, i));
                        break;
                    }
                    builder.insert(builder.length(), className.substring(index, i + 1));
                }
                index = i + 1;
                points++;
            }
        } else if (count < 0) {
            String exceptString = shortenPackagesName(className, -count);
            if (className.equals(exceptString)) {
                int from = className.lastIndexOf('.') + 1;
                int to = className.length();
                builder.insert(builder.length(), className.substring(from, to));
            } else
                return className.replaceFirst(exceptString + '.', "");
        }
        return builder.toString();
    }


}
