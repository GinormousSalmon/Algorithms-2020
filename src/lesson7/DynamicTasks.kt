@file:Suppress("UNUSED_PARAMETER")

package lesson7

import java.lang.System.currentTimeMillis
import kotlin.math.max

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
fun longestCommonSubSequence(first: String, second: String): String {
    TODO()
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */

// Трудоемкость O(log(N))
// Ресурсоемкость O(1)
fun binarySearch(element: Int, list: List<Int>): Int {
    var start = 0
    var end = list.size - 1
    while (start <= end) {
        val index = (start + end) / 2
        when {
            element > list[index] -> start = index + 1
            element < list[index] -> end = index - 1
            else -> return index
        }
    }
    return start
}

// Трудоемкость O(N + N*log(N))
// Ресурсоемкость O(N)
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    val n = list.size
    if (n == 0) return listOf()
    if (n == 1) return list

    val data = list.reversed().map { -it }
    val lasts = (0..n).map { Int.MAX_VALUE }.toMutableList()
    val positions = (0..n).map { 0 }.toMutableList()
    val previous = (0 until n).map { 0 }.toMutableList()
    var length = 0
    positions[0] = -1
    lasts[0] = Int.MIN_VALUE

    for (i in 0 until n) {
        val index = binarySearch(data[i], lasts)
        if (lasts[index - 1] < data[i] && data[i] < lasts[index]) {
            lasts[index] = data[i]
            positions[index] = i
            previous[i] = positions[index - 1]
            length = max(length, index)
        }
    }
    val answer = mutableListOf<Int>()
    var p = positions[length]
    while (p != -1) {
        answer.add(data[p])
        p = previous[p]
    }
    return answer.map { -it }
}


/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
fun shortestPathOnField(inputName: String): Int {
    TODO()
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5

