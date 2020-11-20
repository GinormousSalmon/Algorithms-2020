package lesson5


/**
 * Множество(таблица) с открытой адресацией на 2^bits элементов без возможности роста.
 */
class KtOpenAddressingSet<T : Any>(private val bits: Int) : AbstractMutableSet<T>() {
    init {
        require(bits in 2..31)
    }

    private val capacity = 1 shl bits

    private val storage = Array<Any?>(capacity) { null }

    override var size: Int = 0

    /**
     * Индекс в таблице, начиная с которого следует искать данный элемент
     */
    private fun T.startingIndex(): Int {
        return hashCode() and (0x7FFFFFFF shr (31 - bits))
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    override fun contains(element: T): Boolean {
        var index = element.startingIndex()
        var current = storage[index]
        while (current != null) {
            if (current == element) {
                return true
            }
            index = (index + 1) % capacity
            current = storage[index]
        }
        return false
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    override fun add(element: T): Boolean {
        val startingIndex = element.startingIndex()
        var index = startingIndex
        var current = storage[index]
        while (current != null && current != EMPTY) {
            if (current == element) {
                return false
            }
            index = (index + 1) % capacity
            check(index != startingIndex) { "Table is full" }
            current = storage[index]
        }
        storage[index] = element
        size++
        return true
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблице, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: [java.util.Set.remove] (Ctrl+Click по remove)
     *
     * Средняя
     */

    private object EMPTY

    // Трудоемкость от O(1) до O(N)
    // в лучшем случае индекс удаляемого элемента равен startingIndex, и выполняется только одна итерация цикла
    // в худшем случае startingIndex = 0 и элемента в storage нет, таким образом в цикле просматривается весь storage до конца, N итераций
    // Ресурсоемкость O(1)
    override fun remove(element: T): Boolean {
        val startingIndex = element.startingIndex()
        var index = startingIndex
        var current = storage[index]
        while (current != null) {
            if (current == element) {
                storage[index] = EMPTY
                size--
                return true
            }
            index = (index + 1) % capacity
            if (index == startingIndex) return false
            current = storage[index]
        }
        return false
    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    override fun iterator(): MutableIterator<T> = KtOpenAddressingSetIterator()

    inner class KtOpenAddressingSetIterator : MutableIterator<T> {

        private var index = 0
        private var current = 0
        private var removed = true

        init {
            calcNextIndex() // finds index of first existing element
        }

        // Трудоемкость от O(1) до O(N)
        // в лучшем случае следующий элемент существует, т.е. находится первой итерацией цикла
        // в худшем случае просмотр storage начинается с нулевого индекса и все элементы удалены (возможно, кроме последнего), т.е. просматривается весь массив
        // Ресурсоемкость O(1)
        private fun calcNextIndex() {   // finds index of next existing element
            while (index < capacity && (storage[index] == null || storage[index] == EMPTY))
                index++
        }

        // Трудоемкость O(1)
        // Ресурсоемкость O(1)
        override fun hasNext(): Boolean {
            return index < capacity
        }

        // Трудоемкость от O(1) до O(N)
        // Ресурсоемкость O(1)
        override fun next(): T {
            check(hasNext())
            @Suppress("UNCHECKED_CAST")
            val result = storage[index] as T
            current = index
            index++
            calcNextIndex()
            removed = false // allows removing of current element
            return result
        }

        // Трудоемкость O(1)
        // Ресурсоемкость O(1)
        override fun remove() {
            check(!removed)    // double remove of same element prevention
            removed = true
            storage[current] = EMPTY
            size--
            //remove(current)
        }
    }
}


