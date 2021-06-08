package structural.flyweight

import org.amshove.kluent.`should be equal to`
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class FlyWeightSpec : Spek({

    beforeEachTest {
        TreeTypeFlyWeight.numberOfInstances = 0
        Tree.numberOfInstances = 0
    }

    describe("FlyWeight pattern implementation") {
        describe("getTreeType() method from FlyWeightFactory class") {
            context("when the requested TreeType doesn't exist on cache") {
                it("it create a new one, save and return it") {
                    val expected = TreeTypeFlyWeight("Sycamore Tree", "light green", "text_03.jpg")
                    TreeFactory.treeTypes = listOf(
                        TreeTypeFlyWeight("Maple Tree", "orange", "text_01.jpg"),
                        TreeTypeFlyWeight("Oak Tree", "green", "text_02.jpg")
                    )
                    TreeFactory.treeTypes.size `should be equal to` 2

                    val result = TreeFactory.getTreeType("Sycamore Tree", "light green", "text_03.jpg")

                    result `should be equal to` expected
                    TreeFactory.treeTypes.size `should be equal to` 3
                }
            }
            context("when the requested TreeType does exist on cache") {
                it("it just returns it without adding anything") {
                    val expected = TreeTypeFlyWeight("Pine Tree", "dark green", "text_03.jpg")
                    TreeFactory.treeTypes = listOf(
                        TreeTypeFlyWeight("Maple Tree", "orange", "text_01.jpg"),
                        TreeTypeFlyWeight("Oak Tree", "green", "text_02.jpg"),
                        TreeTypeFlyWeight("Pine Tree", "dark green", "text_03.jpg")
                    )
                    TreeFactory.treeTypes.size `should be equal to` 3

                    val result = TreeFactory.getTreeType("Pine Tree", "dark green", "text_03.jpg")

                    result `should be equal to` expected
                    TreeFactory.treeTypes.size `should be equal to` 3
                }
            }
        }


        describe("plantTree() and draw() method from Forest class") {
            context("when creating thousands of tree objects with common states that could be shared") {
                it("does save a considerable quantity of memory due to the fact that it only creates 3 instances to represent the universe of common types") {
                    val expectedNumberOfTreeInstances = 60000
                    val expectedNumberOfTreeTypes = 3
                    val forest = Forest().apply {
                        repeat(20000) {
                            plantTree(it +1 , it, "Pine Tree", "dark_green", "texture_01.jpg")
                            plantTree(it +1 , it, "Oak Tree", "light_green", "texture_02.jpg")
                            plantTree(it +1 , it, "Maple Tree", "orange", "texture_03.jpg")
                        }
                    }
                    forest.draw(Unit)

                    Tree.numberOfInstances `should be equal to` expectedNumberOfTreeInstances
                    TreeTypeFlyWeight.numberOfInstances `should be equal to` expectedNumberOfTreeTypes
                }
            }
        }
    }
})