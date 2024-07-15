package dev.webfx.platform.boot.gwt;

import com.google.gwt.core.client.EntryPoint;
import dev.webfx.platform.boot.ApplicationBooter;
import dev.webfx.platform.boot.spi.ApplicationBooterProvider;

import static dev.webfx.platform.service.gwtj2cl.ServiceRegistry.*;

public final class GwtEntryPoint implements ApplicationBooterProvider, EntryPoint {

    @Override
    public void onModuleLoad() {
        registerArrayConstructors();
        registerServiceProviders();
        ApplicationBooter.start(this, null);
    }

    public static void registerArrayConstructors() {

    }

    public static void registerServiceProviders() {
        register(dev.webfx.kit.launcher.spi.WebFxKitLauncherProvider.class, dev.webfx.kit.launcher.spi.impl.gwtj2cl.GwtJ2clWebFxKitLauncherProvider::new);
        register(dev.webfx.kit.mapper.peers.javafxmedia.spi.WebFxKitMediaMapperProvider.class, dev.webfx.kit.mapper.peers.javafxmedia.spi.gwtj2cl.GwtJ2clWebFxKitMediaMapperProvider::new);
        register(dev.webfx.kit.mapper.spi.WebFxKitMapperProvider.class, dev.webfx.kit.mapper.spi.impl.gwtj2cl.GwtJ2clWebFxKitHtmlMapperProvider::new);
        register(dev.webfx.platform.ast.spi.factory.AstFactoryProvider.class, dev.webfx.platform.ast.spi.factory.impl.gwt.GwtAstFactoryProvider::new);
        register(dev.webfx.platform.ast.spi.formatter.AstFormatterProvider.class, dev.webfx.platform.ast.json.formatter.JsonFormatterProvider::new);
        register(dev.webfx.platform.ast.spi.parser.AstParserProvider.class, dev.webfx.platform.ast.json.parser.JsonParserProvider::new);
        register(dev.webfx.platform.audio.spi.AudioServiceProvider.class, dev.webfx.kit.platform.audio.spi.impl.openjfxgwtj2cl.OpenJFXGwtJ2clAudioServiceProvider::new);
        register(dev.webfx.platform.blob.spi.BlobProvider.class, dev.webfx.platform.blob.spi.impl.gwtj2cl.GwtJ2clBlobProvider::new);
        register(dev.webfx.platform.boot.spi.ApplicationModuleBooter.class, dev.webfx.kit.launcher.WebFxKitLauncherModuleBooter::new, dev.webfx.platform.boot.spi.impl.ApplicationJobsInitializer::new, dev.webfx.platform.boot.spi.impl.ApplicationJobsStarter::new, dev.webfx.platform.resource.spi.impl.gwt.GwtResourceModuleBooter::new, dev.webfx.stack.ui.fxraiser.json.JsonFXRaiserModuleBooter::new);
        register(dev.webfx.platform.console.spi.ConsoleProvider.class, dev.webfx.platform.console.spi.impl.gwtj2cl.GwtJ2clConsoleProvider::new);
        register(dev.webfx.platform.fetch.spi.FetchProvider.class, dev.webfx.platform.fetch.spi.impl.gwtj2cl.GwtJ2clFetchProvider::new);
        register(dev.webfx.platform.os.spi.OperatingSystemProvider.class, dev.webfx.platform.os.spi.impl.gwtj2cl.GwtJ2clOperatingSystemProvider::new);
        register(dev.webfx.platform.resource.spi.ResourceProvider.class, dev.webfx.platform.resource.spi.impl.gwt.GwtResourceProvider::new);
        register(dev.webfx.platform.resource.spi.impl.gwt.GwtResourceBundle.class, dev.webfx.platform.resource.gwt.GwtEmbedResourcesBundle.ProvidedGwtResourceBundle::new);
        register(dev.webfx.platform.scheduler.spi.SchedulerProvider.class, dev.webfx.platform.uischeduler.spi.impl.gwtj2cl.GwtJ2clUiSchedulerProvider::new);
        register(dev.webfx.platform.shutdown.spi.ShutdownProvider.class, dev.webfx.platform.shutdown.spi.impl.gwtj2cl.GwtJ2clShutdownProvider::new);
        register(dev.webfx.platform.storage.spi.LocalStorageProvider.class, dev.webfx.platform.storage.spi.impl.gwtj2cl.GwtJ2clLocalStorageProvider::new);
        register(dev.webfx.platform.storage.spi.SessionStorageProvider.class, dev.webfx.platform.storage.spi.impl.gwtj2cl.GwtJ2clSessionStorageProvider::new);
        register(dev.webfx.platform.uischeduler.spi.UiSchedulerProvider.class, dev.webfx.platform.uischeduler.spi.impl.gwtj2cl.GwtJ2clUiSchedulerProvider::new);
        register(dev.webfx.platform.useragent.spi.UserAgentProvider.class, dev.webfx.platform.useragent.spi.impl.gwtj2cl.GwtJ2clUserAgentProvider::new);
        register(dev.webfx.stack.i18n.spi.I18nProvider.class, dev.webfx.stack.i18n.spi.impl.ast.AstI18nProvider::new);
        register(javafx.application.Application.class, com.java4now.meteo_webfx.Meteo_WebFX::new);
        register(service.ServiceProvider.class, gwt_service.GWT_MyServiceProvider::new);
    }
}