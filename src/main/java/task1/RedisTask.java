package task1;

import redis.clients.jedis.JedisPooled;

public class RedisTask {
    public static void main(String[] args) {
        JedisPooled jedis = new JedisPooled("localhost", 6379);
        int size = 18_758_328;
        for (int i = 0; i < size; i++) {
            Long number = Math.round(Math.random() * 1000000000000000L);
            String name = generateName(20);
            jedis.sadd(String.valueOf(number), name);

            if(i%10000==0) {
                System.out.println(i + " of " + 18_758_328);
            }
        }
    }

    private static String generateName(int length) {
        var alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя".toCharArray();
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * alphabet.length);
            name.append(alphabet[index]);
        }
        return name.toString();
    }
}
