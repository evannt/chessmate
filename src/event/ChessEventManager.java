package event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessEventManager {

	private Map<ChessEventType, List<ChessEventListener>> listeners;

	public ChessEventManager(ChessEventType... operations) {
		listeners = new HashMap<>();
		for (ChessEventType operation : operations) {
			listeners.put(operation, new ArrayList<>());
		}
	}

	public void subscribe(ChessEventListener listener, ChessEventType... operations) {
		for (ChessEventType eventType : operations) {
			listeners.get(eventType).add(listener);
		}
	}

	public void unsubscribe(ChessEventType eventType, ChessEventListener listener) {
		List<ChessEventListener> currentListeners = listeners.get(eventType);
		currentListeners.remove(listener);
	}

	public void notify(ChessEventType eventType, ChessEvent event) {
		List<ChessEventListener> currentListeners = listeners.get(eventType);
		for (ChessEventListener listener : currentListeners) {
			listener.update(eventType, event);
		}
	}

}
