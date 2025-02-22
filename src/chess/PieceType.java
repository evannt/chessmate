package chess;

import java.util.HashMap;
import java.util.Map;

public enum PieceType {

	NONE(".", 0),
	WPAWN("P", 1), WKNIGHT("N", 2), WBISHOP("B", 3), WROOK("R", 4), WQUEEN("Q", 5), WKING("K", 6),
	BPAWN("p", 7), BKNIGHT("n", 8), BBISHOP("b", 9), BROOK("r", 10), BQUEEN("q", 11), BKING("k", 12);

	private static final Map<String, PieceType> BY_ID = new HashMap<>();

	static {
		for (PieceType e : values()) {
			BY_ID.put(e.id, e);
		}
	}

	private static final Map<Integer, PieceType> BY_KEY = new HashMap<>();

	static {
		for (PieceType e : values()) {
			BY_KEY.put(e.key, e);
		}
	}

	private String id;
	private int key;

	private PieceType(String id, int key) {
		this.id = id;
		this.key = key;
	}

	public boolean isWhite() {
		return this.key < BPAWN.key;
	}

	public String getId() {
		return id;
	}

	public int getKey() {
		return key;
	}

	public static PieceType valueOfId(String id) {
		return BY_ID.get(id);
	}

	public static int getKeyById(String id) {
		return valueOfId(id).key;
	}

	public static PieceType valueOfKey(int key) {
		return BY_KEY.get(key);
	}

	public static String getIdByKey(int key) {
		return valueOfKey(key).id;
	}

}
