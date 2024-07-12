// File managed by WebFX (DO NOT EDIT MANUALLY)
package dev.webfx.platform.resource.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import dev.webfx.platform.resource.spi.impl.gwt.GwtResourceBundleBase;

public interface GwtEmbedResourcesBundle extends ClientBundle {

    GwtEmbedResourcesBundle R = GWT.create(GwtEmbedResourcesBundle.class);
    @Source("dev/webfx/platform/meta/exe/exe.properties")
    TextResource r1();

    @Source("dev/webfx/stack/i18n/de.properties")
    TextResource r2();

    @Source("dev/webfx/stack/i18n/en.properties")
    TextResource r3();

    @Source("dev/webfx/stack/i18n/fr.properties")
    TextResource r4();

    @Source("dev/webfx/stack/i18n/rs.properties")
    TextResource r5();



    final class ProvidedGwtResourceBundle extends GwtResourceBundleBase {
        public ProvidedGwtResourceBundle() {
            registerResource("dev/webfx/platform/meta/exe/exe.properties", R.r1());
            registerResource("dev/webfx/stack/i18n/de.properties", R.r2());
            registerResource("dev/webfx/stack/i18n/en.properties", R.r3());
            registerResource("dev/webfx/stack/i18n/fr.properties", R.r4());
            registerResource("dev/webfx/stack/i18n/rs.properties", R.r5());

        }
    }
}
