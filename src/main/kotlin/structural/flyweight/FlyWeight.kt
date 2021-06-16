package structural.flyweight

// Flyweight (AKA Cache) is a structural design pattern that lets you fit more objects into the available amount of RAM
// by sharing commom parts of state between multiple objects instead of keeping all of the data in each object.

// In this example, the Flyweight pattern helps to reduce memory usage when rendering milions of tree objects on a Canvas
// The pattern extracts the repeating intrinsic state from main Tree class and moves it into the flyweight class TreeType.


// The "FlyWeight" class contains the portion of the original object's state that can be shared between multiple methods
// the state stored inside a flyweight is called intrinsic.
data class TreeTypeFlyWeight(
    val name: String,
    val color: String,
    val texture: String
) {
    init { numberOfInstances++ }

    // Just Sample method
    fun draw(canvas: Any, x: Int, y: Int): Unit = Unit

    companion object {
        var numberOfInstances = 0
    }
}

// The "Context" class contains the extrinsic state, unique across all original objects.
data class Tree(
    val x: Int,
    val y: Int,
    val type: TreeTypeFlyWeight
) {
    init { numberOfInstances++ }

    fun draw(canvas: Any) = type.draw(canvas, x, y)

    companion object {
        var numberOfInstances = 0
    }
}

// The Flyweight factory containing our cache of objects that represent common states composition
class TreeFactory {
    companion object {

        // Our "cache" of flyweight objects
        var treeTypes: List<TreeTypeFlyWeight> = emptyList()

        fun getTreeType(name: String, color: String, texture: String): TreeTypeFlyWeight =
            treeTypes.find { it.name == name && it.color == color && it.texture == texture }
                ?: let {
                    val newType = TreeTypeFlyWeight(name, color, texture)
                    treeTypes = treeTypes.plus(newType)
                    newType
                }
    }
}

class Forest(var trees: List<Tree> = emptyList()) {

    fun plantTree(x: Int, y: Int, name: String, color: String, texture: String) = apply {
        val treeType = TreeFactory.getTreeType(name, color, texture)
        trees = trees.plus(Tree(x, y, treeType))
    }

    fun draw(canvas: Any) = trees.forEach { it.draw(canvas) }
}


