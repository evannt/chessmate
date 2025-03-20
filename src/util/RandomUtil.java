package util;

public final class RandomUtil {

	private static int random_state = 1804289382;

	public static int getRandom32BitNumber() {
		int num = random_state;
		num ^= (num << 13);
		num ^= (num >> 17);
		num ^= (num << 5);
		random_state = num;
		return num;
	}

	public static long getRandom64BitNumber() {
		long num1, num2, num3, num4;
		num1 = getRandom32BitNumber() & 0xFFFF;
		num2 = getRandom32BitNumber() & 0xFFFF;
		num3 = getRandom32BitNumber() & 0xFFFF;
		num4 = getRandom32BitNumber() & 0xFFFF;
		return num1 | (num2 << 16) | (num3 << 32) | (num4 << 48);
	}

	public static long getMagicNumber() {
		return getRandom64BitNumber() & getRandom64BitNumber() & getRandom64BitNumber();
	}

}
