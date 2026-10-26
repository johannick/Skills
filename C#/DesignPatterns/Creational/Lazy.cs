
namespace Project.DesignPatterns.Creational;

/// <summary>
/// Lazy loading is part of .NET
/// </summary>

public class LazyLoading(StreamReader Reader)
{
    public Lazy<string> Content { get; } = new Lazy<string>(() => Reader?.ReadToEnd() ?? string.Empty);
}