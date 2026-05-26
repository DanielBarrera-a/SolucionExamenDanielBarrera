package domain;

import java.awt.*;

public class BlueSkinBehavior extends AbstractSkinBehavior {
    private static final long serialVersionUID = 1L;

    @Override
    public int getSpeed(Player player) { return 2; }

    @Override
    public int getDrawSize() { return 38; }

    @Override
    public int getDrawPadding() { return 1; }

    @Override
    public Color getColor(Player player) { return Color.BLUE; }

    @Override
    public Skin getSkinType() { return Skin.BLUE; }
}