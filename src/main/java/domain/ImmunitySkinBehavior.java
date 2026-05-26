package domain;

import java.awt.*;

public class ImmunitySkinBehavior extends AbstractSkinBehavior {
    private static final long serialVersionUID = 1L;

    @Override
    public int getSpeed(Player player) { return 1; }

    @Override
    public int getDrawSize() { return 30; }

    @Override
    public int getDrawPadding() { return 5; }

    @Override
    public Color getColor(Player player) { return Color.CYAN; }

    // Sobrescribe la plantilla porque este skin es inmune
    @Override
    public boolean onEnemyHit(Player player) {
        return false; // inmunidad total, no muere
    }

    @Override
    public Skin getSkinType() { return Skin.IMMUNITY; }
}