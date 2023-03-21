package Domain.DAL.Abstract;

public interface UpdateFunction<T,K> {
    public void update(T instance,K toUpdate);
}
