package com.echoist.linkedout.viewModels

import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.ExampleItems
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchingViewModel @Inject constructor(
    essayApi: EssayApi,
    exampleItems: ExampleItems
) : CommunityViewModel(essayApi, exampleItems)