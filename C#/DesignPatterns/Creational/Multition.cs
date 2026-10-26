namespace Project.DesignPatterns.Creational;

public static class Multiton<TInstance> where TInstance : new()
{
    private static readonly Dictionary<string, TInstance> Instances = [];

    public static TInstance GetInstance(string key) => Instances.GetValueOrDefault(key) ?? new TInstance();

}