import java.util.List;
import java.util.Map;

public class FileRes extends Result<Integer, String> {

	private final String name;
	private final int id;
	private String rank;


	public FileRes(List<String> list, Map<Integer, Integer> map, int id, String name) {
		super(list, map);
		this.id = id;
		this.name = name.substring(12);
	}

	public Double getDoubleRank() {
		return Double.parseDouble(rank);
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return name + "," + rank + "," + list.get(0).length() + "," + map.get(list.get(0).length()) + "\n";
	}
}
