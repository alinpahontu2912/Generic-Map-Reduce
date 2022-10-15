import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReduceWorker extends Worker<Integer, String> {

	private final Map<String, List<MapResult>> map;
	private final List<FileRes> results;
	private final List<String> files;

	public ReduceWorker(int id, int numThreads, Map<String, List<MapResult>> map, List<FileRes> results, List<String> files) {
		super(id, numThreads);
		this.map = map;
		this.results = results;
		this.files = files;
	}

	@Override
	public void run() {
		int start = id * (int) (Math.ceil((double) files.size() / numThreads));
		int end = (int) Math.min(files.size(), (id + 1) * (Math.ceil((double) files.size() / numThreads)));
		for (int i = start; i < end; i++) {
			String currFile = files.get(i);
			hashMap = new HashMap<>();
			list = new ArrayList<>();
			FileRes fileRes = new FileRes(list, hashMap, i, files.get(i));
			List<MapResult> mapResultList = map.get(currFile);

			// combination step

			for (MapResult mapResult : mapResultList) {
				if (mapResult.getList().size() != 0 && mapResult.getMap().size() != 0) {
					for (Map.Entry<Integer, Integer> entry : mapResult.getMap().entrySet()) {
						fileRes.getMap().merge(entry.getKey(), entry.getValue(), Integer::sum);
					}
					for (String word : mapResult.getList()) {
						if (fileRes.getList().size() == 0 || (word.length() == fileRes.getList().get(0).length())) {
							fileRes.getList().add(word);
						} else if (word.length() > fileRes.getList().get(0).length()) {
							fileRes.getList().clear();
							fileRes.getList().add(word);
						}
					}
				}
			}

			// processing step

			double sum = 0;
			double count = 0;
			for (Map.Entry<Integer, Integer> entry : fileRes.getMap().entrySet()) {
				if (entry.getKey() != 0) {
					sum += Tema2.fibonacci[entry.getKey() + 1] * entry.getValue();
					count += entry.getValue();
				}
			}
			fileRes.setRank(String.format("%.2f", sum / count));

			// adding the result to the result list of the coordinator thread

			results.add(fileRes);
		}
	}
}
