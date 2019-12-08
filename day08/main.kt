package day08

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Color {
  const val BLACK = 0
  const val WHITE = 1
  const val TRANSPARENT = 2
}

const val width = 25
const val height = 6
const val filename = "input.txt"

fun main() {
  val pixels = File(filename).readText().trim().chunked(1).map(String::toInt)
  val layers = pixels.chunked(width * height)

  val minLayer = layers.minBy { layer -> layer.count { it == 0 } }!!
  val checksum = minLayer.count { it == 1 } * minLayer.count { it == 2 }
  println("Checksum: $checksum")

  val imageData = layers.reduce { layer1, layer2 ->
    layer1.zip(layer2) { pixel1, pixel2 ->
      if (pixel1 != Color.TRANSPARENT) pixel1 else pixel2
    }
  }

  saveImage(imageData, "image.png")
}

private fun saveImage(imageData: List<Int>, imageName: String) {
  val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

  for (i in 0 until width) {
    for (j in 0 until height) {
      val color = when (imageData[i + j * width]) {
        Color.BLACK -> 0x000000
        Color.WHITE -> 0xffffff
        else -> 0xff00ff
      }
      image.setRGB(i, j, color)
    }
  }

  ImageIO.write(image, "png", File(imageName))
  println("Image saved to $imageName")
}
