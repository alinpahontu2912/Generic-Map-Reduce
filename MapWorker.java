import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapWorker extends Worker<Integer, String> {

	private final Map<String, List<MapResult>> results;
	private final List<MapTask> tasks;


	public MapWorker(int id, int numThreads, List<MapTask> tasks, Map<String, List<MapResult>> results) {
		super(id, numThreads);
		this.tasks = tasks;
		this.results = results;
	}


	private boolean isLetter(byte d) {
		char c = (char) d;
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	@Override
	public void run() {
		int start = id * (int) (Math.ceil((double) tasks.size() / numThreads));
		int end = (int) Math.min(tasks.size(), (id + 1) * (Math.ceil((double) tasks.size() / numThreads)));
		for (int i = start; i < end; i++) {
			hashMap = new HashMap<>();
			list = new ArrayList<>();
			MapTask mapTask = tasks.get(i);
			StringBuilder stringBuilder = new StringBuilder();
			try {
				// opening file at needed offset
				RandomAccessFile randomAccessFile = new RandomAccessFile(mapTask.getFileName(), "r");
				randomAccessFile.seek(mapTask.getStartOffset());

				byte character = ' ';
				int count = 0;
				// checking if the fragment begins in the middle of a rod
				if (mapTask.getStartOffset() != 0) {
					randomAccessFile.seek(mapTask.getStartOffset() - 1);
					byte char1 = randomAccessFile.readByte();
					byte char2 = randomAccessFile.readByte();
					count++;
					if (isLetter(char1) && isLetter(char2)) {
						while (mapTask.getStartOffset() + count < mapTask.getEndOffset()
								&& isLetter(char2)
						) {
							char2 = randomAccessFile.readByte();
							mapTask.setStartOffset(mapTask.getStartOffset() + 1);
						}
					}
					if (mapTask.getStartOffset() + 1 < mapTask.getEndOffset()) {
						stringBuilder.append((char) char2);
					}
				}

				// reading character by character from file, until endoffset is met

				while (mapTask.getStartOffset() + count < mapTask.getEndOffset()) {
					character = randomAccessFile.readByte();
					stringBuilder.append((char) character);
					count++;
				}

				// if ending in middle of a word, keep reading until no letter is found

				while (isLetter(character) && mapTask.getStartOffset() + count < randomAccessFile.length()) {
					character = randomAccessFile.readByte();
					if (isLetter(character)) {
						stringBuilder.append((char) character);
						count++;
					}
				}

				randomAccessFile.close();

				// set up delimitors using regex and split the read fragment using it

				String[] words = stringBuilder.toString().split("[;:/?˜.,><‘\\[\\]{}()!@#$'%ˆ&\\- +’=*”|\\r\\n\\t]+");
				for (String string : words) {

					// create or update hashmap and list entries

					hashMap.merge(string.length(), 1, Integer::sum);
					if (list.size() == 0 || string.length() == list.get(0).length()) {
						list.add(string);
					}
					if (string.length() > list.get(0).length()) {
						list.clear();
						list.add(string);
					}

				}

				// adding the result to the map results list of the coordinator

				MapResult mapResult = new MapResult(mapTask.getFileName(), list, hashMap);
				results.get(mapTask.getFileName()).add(mapResult);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
