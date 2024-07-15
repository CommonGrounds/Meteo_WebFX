// File managed by WebFX (DO NOT EDIT MANUALLY)

module Meteo_WebFX.application.openjfx {

    // Direct dependencies modules
    requires Meteo_WebFX.application;
    requires Service;
    requires javafx.graphics;
    requires javafx.web;
    requires webfx.extras.webtext.peers.openjfx;
    requires webfx.kit.javafxgraphics.openjfx;
    requires webfx.kit.platform.audio.openjfx.gwt.j2cl;
    requires webfx.kit.platform.visibility.openjfx;
    requires webfx.platform.blob.java;
    requires webfx.platform.boot.java;
    requires webfx.platform.console;
    requires webfx.platform.console.java;
    requires webfx.platform.fetch.java;
    requires webfx.platform.os.java;
    requires webfx.platform.resource.java;
    requires webfx.platform.scheduler.java;
    requires webfx.platform.shutdown.java;
    requires webfx.platform.storage.java;
    requires webfx.platform.storagelocation.java;
    requires webfx.platform.useragent.java.client;
    requires webfx.platform.util.time;
    requires webfx.stack.i18n.ast;
    requires webfx.stack.ui.fxraiser.json;

    // Exported packages
    exports openjfx.service;

    // Provided services
    provides service.ServiceProvider with openjfx.service.OpenJFXMyServiceProvider;

}