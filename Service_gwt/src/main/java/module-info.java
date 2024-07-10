// File managed by WebFX (DO NOT EDIT MANUALLY)

module Service_gwt {

    // Direct dependencies modules
    requires Meteo_WebFX.application;
    requires Service;
    requires elemental2.dom;
    requires gwt.user;
    requires javafx.graphics;
    requires webfx.kit.javafxgraphics.peers.gwt.j2cl;
    requires webfx.platform.console;

    // Exported packages
    exports gwt_service;

    // Provided services
    provides service.ServiceProvider with gwt_service.GWT_MyServiceProvider;

}