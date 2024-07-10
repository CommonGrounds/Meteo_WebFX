package com.java4now.meteo_webfx.shared;

// Interface za  Java Script funkciju koja radi load ikonica bez flickeringa

public interface Loaded<T> {
    public void data(T data);
}
