package ru.spbau.mit.characters.mobs;

import ru.spbau.mit.characters.Stats;

public class Boss extends Mob {
    private static final char DEFAULT_ICON = '☠';
    private static final Stats DEFAULT_STATS = new Stats(50, 20, 5);
    private static final double ARMOR_POWER_DECREASE = 0.3;

    public Boss() {
        super(DEFAULT_ICON, new Stats(DEFAULT_STATS));
    }

    @Override
    public int getFightPower() {
        return Math.max(0, (int) Math.round(getStats().getStamina() - getStats().getArmor() * ARMOR_POWER_DECREASE));
    }

}
