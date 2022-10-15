public class MapTask extends Task {

	private final int endOffset;
	private int startOffset;

	public MapTask(String fileName, int startOffset, int endOffset) {
		this.fileName = fileName;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

	public String getFileName() {
		return fileName;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}

	@Override
	public String toString() {
		return "MapTask{" +
				"startOffset=" + startOffset +
				", endOffset=" + endOffset +
				", fileName='" + fileName + '\'' +
				'}';
	}
}
