package com.buggin.offers

final case class Price(amount: Int, currency: String)
final case class Offer(id: Int, price: Price, description: String)
final case class Offers(Offers: Seq[Offer])
