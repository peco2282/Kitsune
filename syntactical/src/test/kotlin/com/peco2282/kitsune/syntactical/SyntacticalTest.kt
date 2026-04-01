package com.peco2282.kitsune.syntactical

import com.peco2282.kitsune.syntactical.Unit.CENTIMETERS
import com.peco2282.kitsune.syntactical.Unit.HOURS
import com.peco2282.kitsune.syntactical.Unit.KILOMETERS
import com.peco2282.kitsune.syntactical.Unit.METERS
import com.peco2282.kitsune.syntactical.Unit.MINUTES
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SyntacticalTest {

  @Test
  fun testUnitConversion() {
    // 距離の変換
    val distanceInMeters = (10.0 of KILOMETERS) inUnit METERS
    Assertions.assertEquals(10000.0, distanceInMeters)
//    println("10 kilometers is $distanceInMeters meters.") // 出力: 10 kilometers is 10000.0 meters.

    // 時間の変換
    val timeInHours = (90.0 of MINUTES) inUnit HOURS
    Assertions.assertEquals(1.5, timeInHours)
//    println("90 minutes is $timeInHours hours.") // 出力: 90 minutes is 1.5 hours.

    // 別の変換
    val lengthInCentimeters = (1.5 of METERS) inUnit CENTIMETERS
    Assertions.assertEquals(150.0, lengthInCentimeters)
//    println("1.5 meters is $lengthInCentimeters centimeters.") // 出力: 1.5 meters is 150.0 centimeters.
  }

}
