// File managed by WebFX (DO NOT EDIT MANUALLY)

module Meteo_WebFX.application.gluon {

    // Direct dependencies modules
    requires Meteo_WebFX.application;
    requires Service;
    requires javafx.graphics;
    requires javafx.web;
    requires webfx.extras.webtext.peers.openjfx;
    requires webfx.kit.javafxgraphics.openjfx;
    requires webfx.platform.audio.gluon;
    requires webfx.platform.blob.java;
    requires webfx.platform.boot.java;
    requires webfx.platform.console;
    requires webfx.platform.console.java;
    requires webfx.platform.fetch.java;
    requires webfx.platform.os.gluon;
    requires webfx.platform.resource.gluon;
    requires webfx.platform.scheduler.java;
    requires webfx.platform.shutdown.gluon;
    requires webfx.platform.storage.java;
    requires webfx.platform.storagelocation.gluon;
    requires webfx.platform.useragent.gluon;
    requires webfx.platform.util.time;
    requires webfx.platform.visibility.gluon;
    requires webfx.stack.i18n.ast;
    requires webfx.stack.ui.fxraiser.json;

    // Exported packages
    exports gluon.service;

    // Provided services
    provides service.ServiceProvider with gluon.service.GluonMyServiceProvider;

}