package Frontend.Sprite

import Frontend.Sprite.SpriteType.SpriteType

import java.awt.Color

class Sprite(val name: String, val spriteType: SpriteType, val size: Int, var pixels: Array[Array[Color]]) {
}
