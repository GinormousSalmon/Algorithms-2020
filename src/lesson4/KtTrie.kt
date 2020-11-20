package lesson4

import java.lang.IllegalStateException

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

        private var prefix = ""
        private var hasNextElement = true
        private var saved = '0'
        private var nextElement = ""
        private var risen = false
        private var savedElement = ""
        private var alreadyRemoved = false

        init {
            if (root.children.isEmpty())
                hasNextElement = false
            else
                nextElement = getNextElement()
        }

        // Трудоемкость O(1)
        // Ресурсоемкость O(1)
        override fun hasNext(): Boolean = hasNextElement

        // Трудоемкость O(L * (L + C*logC))
        // Ресурсоемкость O(L * C)
        // L - average word length, C - average number of children nodes in every node, M - max word len
        override fun next(): String {
            if (!hasNextElement)
                throw IllegalStateException()
            alreadyRemoved = false
            val result = nextElement
            savedElement = nextElement
            nextElement = getNextElement()
            return result
        }

        // Трудоемкость O(N)
        // Ресурсоемкость O(N)
        override fun remove() {
            if (savedElement == "" || alreadyRemoved) throw IllegalStateException()
            remove(savedElement)
            alreadyRemoved = true
        }

        // Трудоемкость O(L * (L + C*logC))
        // Ресурсоемкость O(L * C)
        // L - average word length, C - average number of children nodes in every node, M - max word len
        private fun getNextElement(): String {
            var currentNode = root
            for (char in prefix)
                currentNode = currentNode.children[char]!!
            if (currentNode.children.isNotEmpty()) {
                val children = currentNode.children.toList().sortedBy { it.first }
                if (children[0].first == 0.toChar() && !risen) {
                    val result = prefix
                    when {
                        children.size > 1 -> prefix += children[1].first
                        prefix == "" -> hasNextElement = false
                        else -> levelUp()
                    }
                    return result
                } else {
                    if (risen) {
                        risen = false
                        val index = children.indexOfFirst { it.first == saved } + 1
                        if (children.size == index) {
                            if (prefix == "") {
                                hasNextElement = false
                                return ""
                            } else {
                                levelUp()
                            }
                        } else {
                            prefix += children[index].first
                        }
                    } else {
                        prefix += children[0].first
                    }
                }
            } else {
                levelUp()
            }
            return getNextElement()
        }

        private fun levelUp() {
            risen = true
            if (prefix == "")
                throw IllegalStateException()
            saved = prefix.last()
            prefix = prefix.substring(0, prefix.length - 1)
        }
    }
}


fun main() {
    val trie = KtTrie()
    trie.add("frffr")
    trie.add("qw")
    trie.add("qwe")
    trie.add("qwer")
    trie.add("qwerty")

    for (node in trie)
        println(node)
}
