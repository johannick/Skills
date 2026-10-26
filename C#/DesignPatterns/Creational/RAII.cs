namespace Project.DesignPatterns.Creational;

public interface ILogger
{
    void Debug(string Message);
}

public abstract class Disposable : IDisposable
{
    protected abstract void Dispose(bool disposing);

    public void Dispose()
    {
        Dispose(true);
        GC.SuppressFinalize(this);
    }
}

public class ScopedLogger(ILogger logger, Action action) : Disposable
{
    public ILogger Logger { get; } = logger;
    public Action Method { get; } = action;

    public void Run()
    {
        Logger.Debug("[START]");
        Method();
    }


    protected override void Dispose(bool disposing)
    {
        Logger.Debug("[END]");
    }
}
