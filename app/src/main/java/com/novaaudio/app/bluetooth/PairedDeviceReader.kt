package com.novaaudio.app.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context

data class PairedDevice(
    val name: String,
    val address: String,
    val isConnected: Boolean
)

sealed class DeviceListState {
    object Idle : DeviceListState()
    object BluetoothUnavailable : DeviceListState()
    object BluetoothOff : DeviceListState()
    data class Ready(val devices: List<PairedDevice>) : DeviceListState()
    data class Failed(val message: String) : DeviceListState()
}

/**
 * READ-ONLY. Lists devices already paired with the phone and whether
 * they are connected for audio (A2DP). Sends nothing to any device.
 */
class PairedDeviceReader(private val context: Context) {

    @SuppressLint("MissingPermission")
    fun read(onResult: (DeviceListState) -> Unit) {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        val adapter: BluetoothAdapter? = manager?.adapter
        if (adapter == null) {
            onResult(DeviceListState.BluetoothUnavailable)
            return
        }
        if (!adapter.isEnabled) {
            onResult(DeviceListState.BluetoothOff)
            return
        }
        try {
            val bonded = adapter.bondedDevices.toList()
            val listener = object : BluetoothProfile.ServiceListener {
                override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
                    val connected = try {
                        proxy.connectedDevices.map { it.address }.toSet()
                    } catch (e: SecurityException) {
                        emptySet()
                    }
                    adapter.closeProfileProxy(profile, proxy)
                    onResult(buildState(bonded, connected))
                }

                override fun onServiceDisconnected(profile: Int) {}
            }
            val ok = adapter.getProfileProxy(context, listener, BluetoothProfile.A2DP)
            if (!ok) onResult(buildState(bonded, emptySet()))
        } catch (e: SecurityException) {
            onResult(DeviceListState.Failed("Permission missing: ${e.message}"))
        }
    }

    @SuppressLint("MissingPermission")
    private fun buildState(
        bonded: List<BluetoothDevice>,
        connected: Set<String>
    ): DeviceListState {
        return try {
            val list = bonded.map {
                PairedDevice(
                    name = it.name ?: "Unknown device",
                    address = it.address,
                    isConnected = it.address in connected
                )
            }.sortedByDescending { it.isConnected }
            DeviceListState.Ready(list)
        } catch (e: SecurityException) {
            DeviceListState.Failed("Permission missing: ${e.message}")
        }
    }
}
