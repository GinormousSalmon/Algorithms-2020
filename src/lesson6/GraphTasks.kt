@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson6

import java.io.File
import java.util.*
import kotlin.math.min


/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 */
fun Graph.findEulerLoop(): List<Graph.Edge> {
    TODO()
}

/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 */
fun Graph.minimumSpanningTree(): Graph {
    TODO()
}

/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Если на входе граф с циклами, бросить IllegalArgumentException
 *
 * Эта задача может быть зачтена за пятый и шестой урок одновременно
 */
fun Graph.largestIndependentVertexSet(): Set<Graph.Vertex> {
    TODO()
}

/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 */

// Трудоемкость O(N^2)
// Ресурсоемкость от O(N) до O(N * K)
// Лучший случай - у каждой вершины нет соседей, худший - все соединены со всеми
// N - количество вершин, K - среднее количество соседей у каждой вершины
fun Graph.longestSimplePath(): Path {
    if (vertices.isEmpty())
        return Path()
    val paths: Queue<Path> = LinkedList(vertices.map { Path(it) })
    var longestPath = Path()
    while (paths.size != 1) {
        longestPath = paths.poll()
        for (neighbor in getNeighbors(longestPath.vertices.last()))
            if (!longestPath.contains(neighbor))
                paths.add(Path(longestPath, this, neighbor))
    }
    return longestPath
}

/**
 * Балда
 * Сложная
 *
 * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
 * поэтому задача присутствует в этом разделе
 *
 * В файле с именем inputName задана матрица из букв в следующем формате
 * (отдельные буквы в ряду разделены пробелами):
 *
 * И Т Ы Н
 * К Р А Н
 * А К В А
 *
 * В аргументе words содержится множество слов для поиска, например,
 * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
 *
 * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
 * и вернуть множество найденных слов. В данном случае:
 * ТРАВА, КРАН, АКВА, НАРТЫ
 *
 * И т Ы Н     И т ы Н
 * К р а Н     К р а н
 * А К в а     А К В А
 *
 * Все слова и буквы -- русские или английские, прописные.
 * В файле буквы разделены пробелами, строки -- переносами строк.
 * Остальные символы ни в файле, ни в словах не допускаются.
 */

// Трудоемкость O(W * N * L)
// Ресурсоемкость O(W + N + L)
// W - number of words, N - number of letters in the matrix, L - average word length
fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {
    val data = File(inputName).readLines().map { it.split(' ').map { letter -> letter.first() } }
    val offsets = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))
    val result = mutableSetOf<String>()
    for (word in words) {
        for (lineNumber in data.indices)
            for (letterNumber in data[lineNumber].indices) {
                val checked = mutableListOf<Pair<Int, Int>>()

                // Трудоемкость O(L)
                // Ресурсоемкость O(1)
                fun findNext(y: Int, x: Int, index: Int): Boolean {
                    for ((dy, dx) in offsets) {
                        val newY = y + dy
                        val newX = x + dx
                        val char = word[index]
                        if (newY in data.indices && newX in data[0].indices && !checked.contains(Pair(newY, newX))) {
                            if (data[newY][newX] == char) {
                                checked.add(Pair(newY, newX))
                                if (index == (word.length - 1))
                                    return true
                                if (findNext(newY, newX, index + 1))
                                    return true
                                else
                                    checked.remove(Pair(newY, newX))
                            }
                        }
                    }
                    return false
                }
                if (data[lineNumber][letterNumber] == word.first()) {
                    checked.add(Pair(lineNumber, letterNumber))
                    if (word.length == 1 || findNext(lineNumber, letterNumber, 1))
                        result.add(word)
                }
            }
    }
    return result
}

