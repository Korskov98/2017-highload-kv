### Ход тестирования

В ходе тестирования сначала запускались PUT.sh и PUT2.sh, после них запускались GET.sh и GET2.sh.

### GET без повторов
```
$ sh GET.sh
Average delay: 16.239 ms
Requests/sec: 61.5
```
### GET с повторами
```
$ sh GET2.sh
Average delay: 14.639 ms
Requests/sec: 68.3
```
### PUT без перезаписи
```
$ sh PUT.sh
Average delay: 18.932 ms
Requests/sec: 52.8
```
### PUT с перезаписью
```
$ sh PUT2.sh
Average delay: 26.916 ms
Requests/sec: 37.1
```

### После оптимизации

В рамках оптимизации были переписаны методы upsert и delete в классе MyFileDAO с использованием класса Files, который до оптимизации уже использовался в методе get того же класса. В класс MyService был использован класс Executor для разделения по потокам.

### GET без повторов
```
$ sh GET.sh
Average delay: 13.560 ms
Requests/sec: 73.7
```
### GET с повторами
```
$ sh GET2.sh
Average delay: 13.052 ms
Requests/sec: 76.6
```
### PUT без перезаписи
```
$ sh PUT.sh
Average delay: 13.461 ms
Requests/sec: 74.2
```
### PUT с перезаписью
```
$ sh PUT2.sh
Average delay: 15.653 ms
Requests/sec: 63.8
```

### Вывод

После оптимизации мы видим, что результаты во всех тестах изменились. Время задержки уменьшилось, а пропускная способность увеличилась. Особенно это касается PUT.