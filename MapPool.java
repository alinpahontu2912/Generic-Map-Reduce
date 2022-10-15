import java.util.List;

public class MapPool {

	private final List<MapTask> list;

	public MapPool(List<MapTask> list) {
		this.list = list;
	}

	public List<MapTask> getList() {
		return list;
	}
}
