package org.jnbt;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public final class CompoundTag extends Tag {
    private final Map<String, Tag> value;

    public CompoundTag(String name, Map<String, Tag> value) {
        super(name);
        this.value = Collections.unmodifiableMap(value);
    }

    public Map<String, Tag> getValue() {
        return this.value;
    }

    public String toString() {
        String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }

        StringBuilder bldr = new StringBuilder();
        bldr.append("TAG_Compound" + append + ": " + this.value.size() + " entries\r\n{\r\n");
        Iterator var5 = this.value.entrySet().iterator();

        while(var5.hasNext()) {
            Map.Entry<String, Tag> entry = (Map.Entry)var5.next();
            bldr.append("   " + ((Tag)entry.getValue()).toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
        }

        bldr.append("}");
        return bldr.toString();
    }
}

