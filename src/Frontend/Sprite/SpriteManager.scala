package Frontend.Sprite

import Frontend.Sprite.SpriteType.SpriteType

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object SpriteManager {
  val SPRITE_SIZE = 32
  private val IMAGES_DIR = "res/"

  private var sprites: Array[Sprite] = Array.empty[Sprite]

  def loadSprites() : Unit = {
    var allSprites = Array.empty[Sprite]

    for(spriteType <- SpriteType.values){
      val typePath = IMAGES_DIR + spriteType.toString.toLowerCase + "/"
      val directory = new File(typePath)

      if (directory.exists && directory.isDirectory) {
        val files = directory.listFiles
        val validImages = files.filter(f => f.isFile && (f.getName.endsWith(".png") || f.getName.endsWith(".jpg")))
        val newSprites: Array[Sprite] = validImages.map(image => {
          try {
            val name = image.getName.substring(0, image.getName.lastIndexOf('.'))
            loadSprite(image.getPath, name, spriteType)
          } catch {
            case e: Exception =>
              throw e
          }
        })

        allSprites = Array.concat(allSprites, newSprites)
      } else {
        println(s"Error with images directory at location: ${directory.getPath}")
      }

    }

    sprites = allSprites

    println("All sprites loaded")
  }

  def getSprite(name: String): Sprite = {
    for(s <- sprites){
      if(s.name.toLowerCase() == name.toLowerCase()){
        return s
      }
    }

    throw new Exception(s"Sprite: $name not found")
  }

  def getSpritesByType(spriteType: SpriteType.SpriteType): Array[Sprite] = {
    return sprites.filter(_.spriteType == spriteType)
  }

  def getAllSprites(): Array[Sprite] = {
    return sprites
  }

  private def loadSprite(filePath: String, name: String, spriteType: SpriteType): Sprite = {
    var imgBuffer: BufferedImage = null
    var w = 0
    var h = 0

    try {
      imgBuffer = ImageIO.read(new File(filePath))
      w = imgBuffer.getWidth
      h = imgBuffer.getHeight
    } catch {
      case e: Exception =>
        throw new Exception(s"Could not load image at location: $filePath")
    }

    if(w != SPRITE_SIZE || h != SPRITE_SIZE){
      throw new Exception(s"The image at $filePath has incorrect size: Image is ${w}x${h}")
    }

    val pixels: Array[Array[Color]] = getPixelsColor(imgBuffer)
    val sprite: Sprite = new Sprite(name, spriteType, SPRITE_SIZE, pixels)

    return sprite
  }

  private def getPixelsColor(imgBuffer: BufferedImage): Array[Array[Color]] = {
    val values = Array.ofDim[Color](SPRITE_SIZE, SPRITE_SIZE)
    for (i <- 0 until SPRITE_SIZE) {
      for (j <- 0 until SPRITE_SIZE) {
        val color = new Color(imgBuffer.getRGB(i, j), true)
        values(i)(j) = if(color.getAlpha < 255) Color.BLACK else color
      }
    }
    return values
  }
}
