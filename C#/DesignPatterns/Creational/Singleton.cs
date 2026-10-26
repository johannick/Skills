namespace Project.DesignPatterns.Creational;

public static class Singleton<TInstance> where TInstance : new()
{
    public static readonly TInstance Instance = new();
}