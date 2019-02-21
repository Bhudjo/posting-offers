package com.buggin.offers

final case class Price(amount: Int, currency: String)
final case class Offer(
  id: Int,
  price: Price,
  description: String,
  merchant: MerchantID
)
final case class Offers(Offers: Seq[Offer])
final case class OfferRequest(
  price: Price,
  description: String,
  merchant: MerchantID
)
final case class OfferResponse(id: Int)
final case class MerchantID(id: String)
