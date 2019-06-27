package com.demo.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

public class ParamParseUtil {

    private ParamParseUtil() {

    }

    public static String readJSONData(HttpServletRequest request) {
        StringBuffer json = new StringBuffer();
        String lineString = null;
        try {
            BufferedReader reader = request.getReader();
            while ((lineString = reader.readLine()) != null) {
                json.append(lineString);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return json.toString();
    }

}
