package io.holunda.camunda.bpm.example.common.domain.hotel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.util.UUID

class HotelServiceTest {

  private val hotelService = HotelService()

  @Test
  fun `should book hotel`() {
    val command = BookHotelCommand(
      guestName = "Bud Spencer",
      bookingReference = UUID.randomUUID().toString(),
      checkin = OffsetDateTime.now().plusDays(2),
      checkout = OffsetDateTime.now().plusDays(4)
    )
    val result = hotelService.bookHotel(command)

    assertThat(result.guestName).isEqualTo(command.guestName)
    assertThat(result.bookingReference).isEqualTo(command.bookingReference)
    assertThat(result.checkin).isEqualToIgnoringHours(command.checkin)
    assertThat(result.checkout).isEqualToIgnoringHours(command.checkout)


  }
}
