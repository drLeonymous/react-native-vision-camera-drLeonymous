package com.mrousavy.camera.types

import com.mrousavy.camera.core.CameraDeviceDetails

enum class Orientation(override val unionValue: String) : JSUnionValue {
  LANDSCAPE_RIGHT("landscape-right"),
  PORTRAIT("portrait"),
  LANDSCAPE_LEFT("landscape-left"),
  PORTRAIT_UPSIDE_DOWN("portrait-upside-down");

  fun toDegrees(): Int =
    when (this) {
        LANDSCAPE_LEFT -> 0
        PORTRAIT -> 90
        LANDSCAPE_RIGHT -> 180
        PORTRAIT_UPSIDE_DOWN -> 270
    }

  fun toSensorRelativeOrientation(deviceDetails: CameraDeviceDetails): Orientation {
    // Convert target orientation to rotation degrees (0, 90, 180, 270)
    var rotationDegrees = this.toDegrees()

    // Reverse device orientation for front-facing cameras
    if (deviceDetails.lensFacing == LensFacing.FRONT) {
      rotationDegrees = -rotationDegrees
    }

    // Rotate sensor rotation by target rotation
    val newRotationDegrees = (deviceDetails.sensorOrientation.toDegrees() + rotationDegrees + 270) % 360

    return fromRotationDegrees(newRotationDegrees)
  }

  companion object : JSUnionValue.Companion<Orientation> {
    override fun fromUnionValue(unionValue: String?): Orientation =
      when (unionValue) {
        "landscape-left" -> LANDSCAPE_LEFT
        "portrait" -> PORTRAIT
        "landscape-right" -> LANDSCAPE_RIGHT
        "portrait-upside-down" -> PORTRAIT_UPSIDE_DOWN
        else -> PORTRAIT
      }

    fun fromRotationDegrees(rotationDegrees: Int): Orientation =
      when (rotationDegrees) {
          in 45..135 -> PORTRAIT
          in 135..225 -> LANDSCAPE_RIGHT
          in 225..315 -> PORTRAIT_UPSIDE_DOWN
          else -> LANDSCAPE_LEFT
      }
  }
}
