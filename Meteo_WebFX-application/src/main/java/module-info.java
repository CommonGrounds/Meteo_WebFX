// File managed by WebFX (DO NOT EDIT MANUALLY)

module Meteo_WebFX.application {

    // Direct dependencies modules
    requires Service;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;
    requires webfx.extras.webtext;
    requires webfx.platform.ast;
    requires webfx.platform.ast.factory.generic;
    requires webfx.platform.ast.json.plugin;
    requires webfx.platform.audio;
    requires webfx.platform.console;
    requires webfx.platform.fetch;
    requires webfx.platform.os;
    requires webfx.platform.resource;
    requires webfx.platform.shutdown;
    requires webfx.platform.storage;
    requires webfx.platform.useragent;
    requires webfx.platform.util;
    requires webfx.stack.i18n;

    // Exported packages
    exports com.java4now.meteo_webfx;
    exports com.java4now.meteo_webfx.shared;

    // Resources packages
    opens com.java4now.meteo_webfx;
    opens com.java4now.meteo_webfx.Arrows;
    opens com.java4now.meteo_webfx.Background;
    opens com.java4now.meteo_webfx.mm_api_symbols;
    opens com.java4now.meteo_webfx.sounds;
    opens dev.webfx.kit.css;

    // Provided services
    provides javafx.application.Application with com.java4now.meteo_webfx.Meteo_WebFX;

}