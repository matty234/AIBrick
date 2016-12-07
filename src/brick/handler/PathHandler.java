package brick.handler;

public interface PathHandler {
	public void onFinishedPathSegment();
	public void onEndOfPath(int fare);
}
