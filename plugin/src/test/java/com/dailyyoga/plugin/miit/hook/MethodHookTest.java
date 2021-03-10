package com.dailyyoga.plugin.miit.hook;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 1/11/21 2:13 PM
 * @description:
 */
public class MethodHookTest {


    @Test
    public void methodCall() {
        String statement = "$_= com.dailyyoga.plugin.droidassist.LogTransform.d($where,$1,$2);";

        String result = statement.replaceAll("\\$where", "123");

        String result1 = statement.replaceAll(
                Pattern.quote("$where"), Matcher.quoteReplacement("123"));

        System.out.println("statement:" + statement + "--result:" + result + "--result1:" + result1);
    }

    public void checkSourceTarget(String node) {
    }


}