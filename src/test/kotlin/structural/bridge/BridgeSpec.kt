package structural.bridge

import org.amshove.kluent.`should be equal to`
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class BridgeSpec : Spek({
    describe("Bridge pattern implementation") {
        describe("We can create objects combining the two class hierarchies") {
            context("When creating a basic remote control for a tv") {
                it("should enable us to perform its operations with success") {
                    val remote = BasicRemoteControl(Tv())

                    remote.run {
                        togglePower()
                        repeat(10) { volumeUp() }
                        repeat(5) { volumeDown() }
                        repeat(5) { channelUp() }
                        repeat(2) { channelDown() }
                    }

                    remote.device.getChannel() `should be equal to` 3.0
                    remote.device.getVolume() `should be equal to` 55
                    remote.device.isEnabled() `should be equal to` true
                }
            }
            context("When creating a basic remote control for a radio") {
                it("should enable us to perform its operations with success") {
                    val remote = BasicRemoteControl(Radio())

                    remote.run {
                        togglePower()
                        repeat(10) { volumeUp() }
                        repeat(5) { volumeDown() }
                        repeat(5) { channelUp() }
                        repeat(2) { channelDown() }
                    }

                    remote.device.getChannel() `should be equal to` 93.0
                    remote.device.getVolume() `should be equal to` 55
                    remote.device.isEnabled() `should be equal to` true
                }
            }
            context("When creating a advanced remote control for a tv") {
                it("should enable us to perform its operations with success") {
                    val remote = AdvancedRemoteControl(Tv())

                    remote.run {
                        togglePower()
                        repeat(10) { volumeUp() }
                        repeat(5) { channelUp() }
                        repeat(2) { channelDown() }
                        mute()
                        digitChannel(99.0)
                    }

                    remote.device.getChannel() `should be equal to` 99.0
                    remote.device.getVolume() `should be equal to` 0
                    remote.device.isEnabled() `should be equal to` true
                }
            }
        }
    }
})