package data.scripts.weapons;

public class SupportBeamerAblationEffect extends BeamerAblationEffect
{
    @Override
    protected float ablationChance()
    {
        return 0.002f;
    }

    @Override
    protected float explosionSize()
    {
        return 40f;
    }
}
