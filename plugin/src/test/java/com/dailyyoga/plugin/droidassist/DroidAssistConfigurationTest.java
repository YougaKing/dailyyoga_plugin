package com.dailyyoga.plugin.droidassist;

import com.dailyyoga.plugin.droidassist.transform.Transformer;
import com.dailyyoga.plugin.droidassist.transform.replace.MethodCallReplaceTransformer;
import com.dailyyoga.plugin.droidassist.util.XmlErrorHandler;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import groovy.util.Node;
import groovy.util.NodeList;
import groovy.util.XmlParser;

import static org.junit.Assert.*;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 3/10/21 5:37 PM
 * @description:
 */
public class DroidAssistConfigurationTest {

    private File file = new File("../app/droidAssist.xml");

    public void attribute() {
        try {
            XmlParser xmlParser = new XmlParser(true, true, true);
            xmlParser.setErrorHandler(new XmlErrorHandler());
            Node configs = xmlParser.parse(file);

            NodeList nodeList = (NodeList) configs.get("Replace");
            NodeList methodCallList = nodeList.getAt("MethodCall");
            Node methodCall = (Node) methodCallList.get(0);

            System.out.println(methodCall.attribute("journal"));


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseTest() {
        replaceAll();
    }


    public List<Transformer> parse() {
        DroidAssistConfiguration configuration = new DroidAssistConfiguration(null);
        return configuration.parse(file);
    }

    public void replaceAll(){
        String testSTr = "                byte[]\n" +
                "                java.net.NetworkInterface.getHardwareAddress()";
        //去掉收尾空格并压缩中间的空格到只剩下一个
        System.out.println(testSTr.replaceAll("\n", "").replaceAll(" +", " "));
    }
}