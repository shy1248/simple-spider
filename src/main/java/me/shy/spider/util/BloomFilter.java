/**
 * @Since: 2020-03-27 21:48:50
 * @Author: shy
 * @Email: yushuibo@ebupt.com / hengchen2005@gmail.com
 * @Version: v1.0
 * @Description: -
 */

package me.shy.spider.util;

import java.security.MessageDigest;
import java.util.BitSet;

public class BloomFilter{
	private BitSet bitset = null;
	private BloomHasher bloomHasher = null;

	public BloomFilter(int bitarrLength) {
		this.bloomHasher = new BloomHasher(2);
		this.bitset = new BitSet(bitarrLength);
	}

	public boolean contain(String str) {
		boolean result = true;
		for (int i = 0; i < bloomHasher.getHashers().length; i++) {
			result = result && (bitset.get(bloomHasher.getHashers()[i].hash(str)));
		}
		return result;
	}

	public void addElement(String str) {
		if(contain(str)) return;
		for (int i = 0; i < bloomHasher.getHashers().length; i++) {
			bitset.set(bloomHasher.getHashers()[i].hash(str), true);
		}
	}

	public class BloomHasher {
		private Hasher[] hashers;

		public Hasher[] getHashers() {
			return hashers;
		}

		public BloomHasher(int hashNum) {
			hashers = new Hasher[hashNum];
			for (int i = 0; i < hashNum; i++) {
				hashers[i] = buildHasher(i);
			}
		}

		private Hasher buildHasher(final int i){
			return new Hasher() {
				public int hash(String str) {
					try {
						MessageDigest md5 = MessageDigest
								.getInstance("MD5");
						md5.update(str.getBytes());
						byte[] bytes = md5.digest(str.getBytes());
						int result = bytes[i];
						return result < 0 ? -result : result;
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			};
		}
	}
}
