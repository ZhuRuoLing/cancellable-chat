package icu.takeneko.cancellablechat;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
        id = "cancellable_chat",
        name = "CancellableChat",
        version = "1.0",
        authors = {"ZhuRuoLing"}
)
public class CancellableChat {

    static {
        InstrumentationAccess.init();
    }

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

    }
}
