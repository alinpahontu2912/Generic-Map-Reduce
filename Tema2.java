import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Tema2 {

	static long[] fibonacci = {0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181,
			6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040};


	public static void main(String[] args) throws IOException {
		if (args.length < 3) {
			System.err.println("Usage: Tema2 <workers> <in_file> <out_file>");
			return;
		}
		int numThreads = Integer.parseInt(args[0]);
		int bytesToRead;
		int numOfFiles;
		BufferedReader inputFile = new BufferedReader(new FileReader(args[1]));
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(args[2]));
		Map<String, List<MapResult>> partialResuts = new ConcurrentHashMap<>();//
		List<FileRes> solution = Collections.synchronizedList(new ArrayList<>());
		bytesToRead = Integer.parseInt(inputFile.readLine());
		numOfFiles = Integer.parseInt(inputFile.readLine());
		String filen;
		List<String> files = new ArrayList<>();
		MapPool mapPool = new MapPool(new ArrayList<>());
		List<MapWorker> mapWorkers = new ArrayList<>();
		ReducePool reducePool = new ReducePool(new ArrayList<>());
		List<ReduceWorker> reduceWorkers = new ArrayList<>();

		// creating the tasks
		for (int j = 0; j < numOfFiles; j++) {
			filen = inputFile.readLine().trim();
			files.add(filen);
			File file = new File(filen);
			partialResuts.put(filen, Collections.synchronizedList(new ArrayList<>()));
			int offset = 0;
			while (offset < file.length()) {
				int endOffset = (int) Math.min(offset + bytesToRead, file.length());
				MapTask mapTask = new MapTask(filen, offset, endOffset);
				mapPool.getList().add(mapTask);
				offset += bytesToRead;
			}
		}

		// starting the map workers

		for (int i = 0; i < numThreads; i++) {
			mapWorkers.add(new MapWorker(i, numThreads, mapPool.getList(), partialResuts));
			mapWorkers.get(i).start();
		}

		// waiting for the map workers to finish before jumping to the reduce workers

		for (int i = 0; i < numThreads; i++) {
			try {
				mapWorkers.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// creating the reduce tasks

		for (int i = 0; i < numOfFiles; i++) {
			reducePool.getList().add(new ReduceTask(files.get(i), partialResuts.get(files.get(i))));
		}

		// starting the reduce workers

		for (int i = 0; i < numThreads; i++) {
			reduceWorkers.add(new ReduceWorker(i, numThreads, partialResuts, solution, files));
			reduceWorkers.get(i).start();
		}

		for (int i = 0; i < numThreads; i++) {
			try {
				reduceWorkers.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// sorting the files based on rank and id
		Collections.sort(solution, Comparator.comparing(FileRes::getDoubleRank).reversed().thenComparing(FileRes::getId));

		for (int i = 0; i < numOfFiles; i++) {
			outputFile.write(solution.get(i).toString());
		}
		outputFile.flush();
		outputFile.close();
	}


}
