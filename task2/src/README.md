# Результаты

Массив из 80000000 элементов
* Работа с Collections.synchronizedMap(new HashMap<>())
    * Запись: 5
    * Чтение: 3
* Работа с ConcurrentHashMap<>()
    * Запись: 3
    * Чтение: 1

----

Массив из 800000 элементов
* Работа с Collections.synchronizedMap(new HashMap<>())
    * Запись: 4
    * Чтение: 24
* Работа с ConcurrentHashMap<>()
    * Запись: 3
    * Чтение: 3

----

Массив из 16000 элементов
* Работа с Collections.synchronizedMap(new HashMap<>())
    * Запись: 7
    * Чтение: 3
* Работа с ConcurrentHashMap<>()
    * Запись: 6
    * Чтение: 1

----

Массив из 800 элементов
* Работа с Collections.synchronizedMap(new HashMap<>())
    * Запись: 6
    * Чтение: 2
* Работа с ConcurrentHashMap<>()
    * Запись: 3
    * Чтение: 2

----

Collections.synchronizedMap(new HashMap<>()) работает медленней на больших объемах данных. 
Для небольшого количества данных прирост скорости на ConcurrentHashMap<>() будет незначителен.