package com.peco2282.kitsune.syntactical

sealed class Unit(
  val baseValue: Double
) {


  object METERS : Unit(1.0)

  object KILOMETERS : Unit(1000.0)

  object CENTIMETERS : Unit(0.01)

  object SECONDS : Unit(1.0)

  object MINUTES : Unit(60.0)

  object HOURS : Unit(3600.0)
}


class Quantity(val value: Double, val unit: Unit) : Comparable<Quantity> {
  override operator fun compareTo(other: Quantity): Int {
    val itemInBase = value * unit.baseValue
    val otherInBase = other.value * other.unit.baseValue
    return itemInBase.compareTo(otherInBase)
  }

  operator fun rangeTo(other: Quantity) = this to other
}

class QuantityRange(
  override val start: Quantity,
  override val endInclusive: Quantity
) : ClosedRange<Quantity> {
  override operator fun contains(value: Quantity): Boolean {
    val itemInBase = value.value * value.unit.baseValue
    val startInBase = start.value * start.unit.baseValue
    val endInBase = endInclusive.value * endInclusive.unit.baseValue
    return itemInBase in startInBase..endInBase
  }
}

infix fun Quantity.to(other: Quantity) = QuantityRange(this, other)

infix fun Double.of(unit: Unit) = Quantity(this, unit)


infix fun Quantity.inUnit(targetUnit: Unit) = this.value * this.unit.baseValue / targetUnit.baseValue
