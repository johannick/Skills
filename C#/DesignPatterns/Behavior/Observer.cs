namespace Project.DesignPatterns.Behavior;

public class IObserver<TParameter>
{
    event Action<TParameter>? Receive;

    public virtual void Send(TParameter parameter) => Receive += (e) => Console.WriteLine(e);
}

public class Observable<TParameter>(IObserver<TParameter> observer)
{
    public void Send(TParameter parameter) => observer.Send(parameter);
}
