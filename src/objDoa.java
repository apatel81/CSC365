import java.util.*;

public interface objDoa<T> {
   T getByKey();
   Set<T> getAll();
   Boolean insert(T obj);
   Boolean update(T obj);
   Boolean delete(T obj);

}
