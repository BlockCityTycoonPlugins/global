package org.jnbt;

public final class BooleanTag extends Tag {
    private final boolean value;

    public BooleanTag(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public Boolean getValue() {
        return this.value;
    }

    public String toString() {
        String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }

        return "TAG_Boolean" + append + ": " + this.value;
    }
}
