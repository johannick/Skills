namespace Project.DesignPatterns.Creational;

public interface ICloneable<out TInstance>
{
    TInstance Clone();
}
