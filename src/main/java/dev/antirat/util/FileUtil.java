package dev.antirat.util;

import java.net.URL;
import java.net.URLDecoder;

public class FileUtil {
    
    public static String getExecutor(final int line) {
        try {
            final String className = new Exception().getStackTrace()[line].getClassName();
            if (className.equals("sun.reflect.NativeMethodAccessorImpl")) {
                return new Exception().getStackTrace()[line + 4].getClassName();
            }
            return className;
        } catch (Exception e) {
            return "";
        }
    }
    
    public static String getExecutorAndMethod() {
        try {
            final StackTraceElement[] elements = new Exception().getStackTrace();
            StackTraceElement element = elements[4];
            if (element.getClassName().equals("sun.reflect.NativeMethodAccessorImpl")) {
                element = elements[8];
            }
            return element.getClassName() + "." + element.getMethodName() + "()";
        } catch (Exception e) {
            return "";
        }
    }
    
    public static String getJarName() {
        try {
            String className = new Exception().getStackTrace()[4].getClassName();
            if (className.equals("sun.reflect.NativeMethodAccessorImpl")) {
                className = new Exception().getStackTrace()[8].getClassName();
            }
            final URL location = Class.forName(className).getProtectionDomain().getCodeSource().getLocation();
            final String[] message = location.getFile().split("!")[0].split("/");
            return URLDecoder.decode(message[message.length - 1], "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }
    
    public static String getFileLocation(final String executor) {
        try {
            return Class.forName(executor).getProtectionDomain().getCodeSource().getLocation().toString().split("!")[0];
        } catch (Exception e) {
            return "";
        }
    }

}