package com.example.fullproject.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fullproject.model.Hit
import com.example.fullproject.repository.HitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: HitRepository) : ViewModel() {

    // Using mutableStateOf instead of LiveData
    var hits = mutableStateOf<List<Hit>>(emptyList())
        private set

    // Collect the Flow from the repository and update the state
    fun refreshPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPosts().collect { newHits ->
                hits.value = newHits // Update state when new hits are emitted
            }
        }
    }
}
