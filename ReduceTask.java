import java.util.List;

public class ReduceTask extends Task {

	private final List<MapResult> list;

	public ReduceTask(String fileName, List<MapResult> list) {
		this.fileName = fileName;
		this.list = list;
	}

	@Override
	public String toString() {
		return "ReduceTask{" +
				"list=" + list +
				", fileName='" + fileName + '\'' +
				'}';
	}
}
