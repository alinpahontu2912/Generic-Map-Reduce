import java.util.List;

public class ReducePool {

	private final List<ReduceTask> list;

	public ReducePool(List<ReduceTask> list) {
		this.list = list;
	}

	public List<ReduceTask> getList() {
		return list;
	}

}
