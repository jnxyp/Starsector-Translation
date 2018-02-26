package data.scripts.weapons;

public class BasiliskAblationEffect extends BeamerAblationEffect
{
    @Override
    protected float ablationChance()
    {
        return 0.003f;
    }

    @Override
    protected float explosionSize()
    {
        return 60f;
    }
}
