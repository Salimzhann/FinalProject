package com.example.finalproject.domain.model

data class UiState(
    val premieresState: ScreenState<List<MovieItem>> = ScreenState.Initial,
    val popularState: ScreenState<List<MovieItem>> = ScreenState.Initial,
    val seriesState: ScreenState<List<MovieItem>> = ScreenState.Initial,
    val filmDetailState: ScreenState<FilmDetail> = ScreenState.Initial,
    val staffMembers: ScreenState<List<StaffMember>> = ScreenState.Initial,
    val actorDetailsState: ScreenState<ActorDetail?> = ScreenState.Initial,
    val filmImagesState: ScreenState<List<ImageItem>> = ScreenState.Initial,
    val isLoadingImages: Boolean = false,
    val allDataLoaded: Boolean = false
)