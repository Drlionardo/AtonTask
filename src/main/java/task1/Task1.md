## Расчитаем примерные затраты RAM при храннение данных
### Определим вид данных для хранения
Требуется хранить 18_758_328 записей. 
Каждая запись представляет пару key/value
* key - номер телефона (E164 -  формат номера до 15 символов)
* value - имя пользователя 20 символов (Unicode)
### Выбор инструмента
Для доступа к данным за О(1) можно хранить данные в словаре, используя в качестве ключа  номер телефона.
Можно было написать собственные сервис на Java, хранящий в себе данные в HashMap, но для этого пришлось бы 
есть более удобные инструменты, в которых решены многие проблемы
Выбор пал на Redis, как на одно из самых популярных key/value решений.
* Хранение данных в RAM обеспечивает быстрый доступ
* Возможность создания отказоустойчивого кластера в разных зонах доступности
* Репликация данных на диск
* Удобыне клиенты для различный ЯП
## Развернем локальный instance Redis, сгенерируем тестовые данные и измерим затраты памяти
### Генерация данных
Сгенерируем нужное кол-во записей при помощи клента Jedis на Java
```
import redis.clients.jedis.JedisPooled;

public class task1.RedisTask {
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
```

### После генерации данных проверяем кол-во записей и объем памяти в кластере
```
> redis-cli INFO Memory | grep used_memory
used_memory:4741730240
127.0.0.1:6379> DBSIZE
(integer) 18758328
```
### Вывод:
Получаем примерную оценку в 4.42 GB. 
К этому стоит добавить необходимые накладные рассходы при деплое Redis кластера, в зависимости от платформы.
К примеру Yandex Managed Service for Redis максимальный объем данных равен 75% доступной памяти.
В таком случаем получаем
> TotalMemory= 4.42 Gb /0.75 = 5.89 ≈ 6 GB