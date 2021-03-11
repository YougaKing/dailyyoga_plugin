package com.dailyyoga.plugin.droidassist;

import com.dailyyoga.plugin.droidassist.transform.Transformer;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 3/10/21 5:37 PM
 * @description:
 */
public class DroidAssistConfigurationTest {

    @Test
    public List<Transformer> parse() {
        DroidAssistConfiguration configuration = new DroidAssistConfiguration(null);

        File file = new File("../app/droidAssist.xml");

        return configuration.parse(file);
    }
}