package com.tirexmurina.feature.common.offer.presentation

sealed interface OfferState {

    data object Initial : OfferState

    data object Content : OfferState

}