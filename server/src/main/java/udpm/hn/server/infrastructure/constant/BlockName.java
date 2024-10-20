package udpm.hn.server.infrastructure.constant;

public enum BlockName {
    BLOCK_1,
    BLOCK_2;

    public static String toString(BlockName value) {
        if (value != null) {
            for (BlockName b : BlockName.values()) {
                if (b.name().equalsIgnoreCase(value.name())) {
                    String name = b.name();
                    name = name.toLowerCase().replace("_", " ");
                    return Character.toUpperCase(name.charAt(0)) + name.substring(1);
                }
            }
        }
        return null;
    }
}
