
@Functional
public interface IProcessor<TResult, TParameter> {

    TResult process(TParameter parameter);

}