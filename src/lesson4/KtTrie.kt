package lesson4

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*

/**
 * Префиксное дерево для строк
 */
class KtTrie : AbstractMutableSet<String>(), MutableSet<String> {

    private class Node {
        val children: MutableMap<Char, Node> = linkedMapOf()

        override fun toString(): String {
            return this.children.toString()
        }
    }

    private var root = Node()

    override var size: Int = 0
        private set

    override fun clear() {
        root.children.clear()
        size = 0
    }

    private fun String.withZero() = this + 0.toChar()

    private fun findNode(element: String): Node? {
        var current = root
        for (char in element) {
            current = current.children[char] ?: return null
        }
        return current
    }

    override fun contains(element: String): Boolean =
        findNode(element.withZero()) != null

    override fun add(element: String): Boolean {
        var current = root
        var modified = false
        for (char in element.withZero()) {
            val child = current.children[char]
            if (child != null) {
                current = child
            } else {
                modified = true
                val newChild = Node()
                current.children[char] = newChild
                current = newChild
            }
        }
        if (modified) {
            size++
        }
        return modified
    }

    override fun remove(element: String): Boolean {
        val current = findNode(element) ?: return false
        if (current.children.remove(0.toChar()) != null) {
            size--
            return true
        }
        return false
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    override fun iterator(): MutableIterator<String> = TrieIterator()

    inner class TrieIterator : MutableIterator<String> {

        private val queue: Queue<String> = LinkedList()
        private var currentKey = ""
        private var removed = false

        init {
            queueFill(root, "")
        }

        // Трудоемкость O(1)
        // Ресурсоемкость O(1)
        // string queue pre-fill
        private fun queueFill(node: Node, prefix: String) {
            for ((key, value) in node.children) {
                if (key == 0.toChar())
                    queue.add(prefix)
                else
                    queueFill(value, prefix + key)
            }
        }

        // Трудоемкость O(1)
        // Ресурсоемкость O(1)
        override fun hasNext(): Boolean = queue.isNotEmpty()

        // Трудоемкость O(N)
        // Ресурсоемкость O(N)
        override fun next(): String {
            if (queue.isEmpty()) throw IllegalStateException()
            currentKey = queue.poll()
            removed = false
            return currentKey
        }

        // Трудоемкость O(N)
        // Ресурсоемкость O(N)
        override fun remove() {
            if (currentKey == "" || removed) throw IllegalStateException()
            remove(currentKey)
            removed = true
        }
    }

}
