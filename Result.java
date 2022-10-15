import java.util.List;
import java.util.Map;

public class Result<T, K> {

	protected List<K> list;
	protected Map<T, T> map;

	public Result(List<K> list, Map<T, T> map) {
		this.list = list;
		this.map = map;
	}

	public List<K> getList() {
		return list;
	}

	public void setList(List<K> list) {
		this.list = list;
	}

	public Map<T, T> getMap() {
		return map;
	}

}
