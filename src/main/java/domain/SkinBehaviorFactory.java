package domain;

/** Patrón Creador */
public class SkinBehaviorFactory {

    public static SkinBehavior create(Skin skin) {
        switch (skin) {
            case RED:   return new RedSkinBehavior();
            case BLUE:  return new BlueSkinBehavior();
            case GREEN: return new GreenSkinBehavior();
            default:    return new RedSkinBehavior();
        }
    }
}