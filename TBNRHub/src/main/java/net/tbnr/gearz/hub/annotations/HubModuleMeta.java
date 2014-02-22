package net.tbnr.gearz.hub.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jake on 2/21/14.
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public class HubModuleMeta {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface HubItemMeta {

        public String key() default "null";

        public boolean enabled() default false;

        public boolean events() default false;

        public boolean commands() default false;

    }
}
