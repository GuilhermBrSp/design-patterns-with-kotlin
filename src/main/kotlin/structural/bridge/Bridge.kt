package structural.bridge

// Bridge is a structural design pattern that lets you split a large class or a set of closely related classes into
// two separate hierarchies - abstraction and implementation - which can be developed independently of each other.

// This example illustrates how the Bridge pattern can help divide the monolithic code of an app that manages devices
// and their remote controls. The Device classes act as the implementation, whereas the Remotes act as abstraction.

// The "abstraction" defines the interface for the "control" part of the two class hierarchies. It maintains a reference to an
// object of the "implementation" hierarchy and delegates all of the real work to this object.
abstract class Remote(val device: Device) {
    fun togglePower() = device.apply { if (isEnabled()) disable() else enable() }
    fun volumeDown() = device.apply { setVolume(getVolume() - 1) }
    fun volumeUp() = device.apply { setVolume(getVolume() + 1) }
    fun channelDown() = device.apply { setChannel(getChannel() - 1) }
    fun channelUp() = device.apply { setChannel(getChannel() + 1) }
}

// The "implementation" interface declares methods common to all concrete implementation classes. It doesnt have to match the
// abstraction interface.
interface Device {
    fun isEnabled(): Boolean
    fun enable()
    fun disable()
    fun getVolume(): Int
    fun setVolume(volume: Int)
    fun getChannel(): Double
    fun setChannel(channel: Double)
}

// We can extend classes from the abstraction hierarchy independently from device classes
class AdvancedRemoteControl(device: Device) : Remote(device) {
    fun mute() = device.setVolume(0)

    fun digitChannel(channel: Double) = device.setChannel(channel)
}

class BasicRemoteControl(device: Device) : Remote(device)

class Radio(var batteryPercentage: Int = 100) : Device {
    private var radioState = "OFF"
    private var radioVolume = 50
    private var radioChannel = 90.0

    override fun isEnabled(): Boolean = radioState == "ON"

    override fun enable() {
        radioState = "ON"
    }

    override fun disable() {
        radioState = "OFF"
    }

    override fun getVolume(): Int = radioVolume

    override fun setVolume(volume: Int) {
        radioVolume = volume
    }

    override fun getChannel(): Double = radioChannel

    override fun setChannel(channel: Double) {
        radioChannel = channel
    }
}

class Tv : Device {
    private var tvState = "OFF"
    private var tvVolume = 50
    private var tvChannel = 0.0

    override fun isEnabled(): Boolean = tvState == "ON"

    override fun enable() {
        tvState = "ON"
    }

    override fun disable() {
        tvState = "OFF"
    }

    override fun getVolume(): Int = tvVolume

    override fun setVolume(volume: Int) {
        tvVolume = volume
    }

    override fun getChannel(): Double = tvChannel

    override fun setChannel(channel: Double) {
        tvChannel = channel
    }
}

