package data.scripts.weapons;

public class TacBeamerAblationEffect extends BeamerAblationEffect
{
    @Override
    protected float ablationChance()
    {
        return 0.001f;
    }

    @Override
    protected float explosionSize()
    {
        return 20f;
    }
}
