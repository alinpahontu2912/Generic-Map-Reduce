import java.util.List;
import java.util.Map;

public class MapResult extends Result<Integer, String> {

	private final String fileName;

	public MapResult(String fileName, List<String> list, Map<Integer, Integer> map) {
		super(list, map);
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "MapResult{" +
				"fileName='" + fileName + '\'' +
				", list=" + list +
				", map=" + map +
				'}';
	}
}
