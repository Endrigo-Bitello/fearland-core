package dev.fearland.cangasso.player.enums;

public enum FriendRequest {
    ATIVADO,
    DESATIVADO;

    public String getInkSack() {
        if (this == ATIVADO) {
            return "10";
        }

        return "8";
    }

    public String getName() {
        if (this == ATIVADO) {
            return "§aAtivado";
        }

        return "§cDesativado";
    }

    public FriendRequest next() {
        if (this == DESATIVADO) {
            return ATIVADO;
        }

        return DESATIVADO;
    }

    private static final FriendRequest[] VALUES = values();

    public static FriendRequest getByOrdinal(long ordinal) {
        if (ordinal < 2 && ordinal > -1) {
            return VALUES[(int) ordinal];
        }

        return null;
    }
}
