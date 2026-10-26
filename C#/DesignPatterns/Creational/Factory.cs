/// <summary>
/// Patterns de creation
/// </summary>
/// <typeparam name="TInstance"></typeparam>

namespace Project.DesignPatterns.Creational;

public class Factory<TInstance>
{
    public TInstance Create()
    {
        return Activator.CreateInstance<TInstance>();   
    }
}

/// <summary>
/// Pattern de creation avec héritage pour limiter la factory a un certain type
/// </summary>
/// <typeparam name="TInstance"></typeparam>
/// <typeparam name="TInterface"></typeparam>
public class Factory<TInstance, TInterface> where TInstance : TInterface
{
    public TInstance Create()
    {
        return Activator.CreateInstance<TInstance>();   
    }
}