package dev.antirat.util;

import dev.antirat.AntiRat;

public class RatException extends RuntimeException {

    public RatException() {
        super();
        AntiRat.rats.add(FileUtil.getExecutorAndMethod() + " is a rat. Jar name: " + FileUtil.getJarName());
    }

}