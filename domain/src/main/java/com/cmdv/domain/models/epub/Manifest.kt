package com.cmdv.domain.models.epub

class Manifest {

    val items: ArrayList<ManifestItem>
    private val idIndex: HashMap<String, ManifestItem>

    init {
        items = ArrayList()
        idIndex = HashMap()
    }
    fun add(item: ManifestItem) {
        items.add(item)
        idIndex[item.iD] = item
    }

    fun clear() {
        items.clear()
    }

    fun findById(id: String): ManifestItem? {
        return idIndex[id]
    }

    fun findByResourceName(resourceName: String): ManifestItem? {
        for (i in 0 until items.size) {
            val item = items[i]
            if (resourceName == item.href) {
                return item
            }
        }
        return null
    }
}
