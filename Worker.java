import java.util.List;
import java.util.Map;

public class Worker<T, K> extends Thread {

	protected Map<T, T> hashMap;
	protected List<K> list;
	protected int id;
	protected int numThreads;

	public Worker(int id, int numThreads) {
		this.id = id;
		this.numThreads = numThreads;
	}

	@Override
	public void run() {
		super.run();
	}
}
