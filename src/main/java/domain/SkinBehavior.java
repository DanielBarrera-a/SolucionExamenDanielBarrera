package domain;

import java.awt.*;
import java.io.Serializable;

/**
 * Interfaz que define el comportamiento de polimorfismo de cada skin
 */
public interface SkinBehavior extends Serializable {
    int getSpeed(Player player);
    int getDrawSize();
    int getDrawPadding();
    Color getColor(Player player);
    boolean hasShield();
    boolean onEnemyHit(Player player);
    void resetAfterRespawn(Player player);
    Skin getSkinType();
}