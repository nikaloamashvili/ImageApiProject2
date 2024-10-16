package com.example.fullproject

import com.example.fullproject.worker.MyWorker
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import coil.compose.rememberAsyncImagePainter
import com.example.fullproject.model.Hit
import com.example.fullproject.ui.theme.FullProjectTheme
import com.example.fullproject.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Refresh posts when the activity is created
        viewModel.refreshPosts()
        scheduleDailyWork(baseContext)
        enableEdgeToEdge()

        setContent {
            FullProjectTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Image Api Project") },
                        )
                    }
                ) { paddingValues ->
                    // LazyColumn content with padding to avoid overlap with the top bar
                    val hits = viewModel.hits.value
                    LazyColumnExample(hits, Modifier.padding(paddingValues))
                }
            }
        }
    }



@Composable
fun LazyColumnExample(hits: List<Hit>,modifier: Modifier) {
    // LazyColumn to display the list
    LazyColumn(
        modifier = modifier.fillMaxSize()


    ) {
        items(hits) { item ->
            ListItem(item)
        }
    }
}

@Composable
fun ListItem(item: Hit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ImageLoader(item)
    }
}



@Composable
fun ImageLoader(hit: Hit) {
    Box(
        modifier = Modifier
            .fillMaxWidth() // Set Box width to match the screen width
            .height(200.dp) // Set Box height to match the image height
    ) {
        val imageUrl = hit.previewUrl

        // Coil's rememberAsyncImagePainter
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "Loaded Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Align the Row at the bottom of the Box
                .padding(10.dp)
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(end = 10.dp) // Add margin between buttons
            ) {
                Text("Likes: "+hit.likes.toString())
            }

            Button(
                onClick = { /*TODO*/ }
            ) {
                Text("Comments: "+hit.comments.toString())
            }
        }
    }
}

fun scheduleDailyWork(applicationContext: Context) {
    val currentTime = Calendar.getInstance()
    val targetTime = Calendar.getInstance()

    // Set the target time to 2 PM
    targetTime.set(Calendar.HOUR_OF_DAY, 14)
    targetTime.set(Calendar.MINUTE, 0)
    targetTime.set(Calendar.SECOND, 0)

    // If the target time is before the current time, move it to the next day
    if (targetTime.before(currentTime)) {
        targetTime.add(Calendar.DAY_OF_MONTH, 1)
    }

    // Calculate the delay in milliseconds
    val delayInMillis = targetTime.timeInMillis - currentTime.timeInMillis
    val delayInMinutes = TimeUnit.MILLISECONDS.toMinutes(delayInMillis)

    // Create a one-time work request to start the worker at 2 PM
    val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
        .setInitialDelay(delayInMinutes, TimeUnit.MINUTES)
        .build()

    // Enqueue the one-time request
    WorkManager.getInstance(applicationContext).enqueue(oneTimeWorkRequest)

    // Create a periodic work request to run the worker every 24 hours
    val periodicWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 24, TimeUnit.HOURS)
        .build()

    // Once the one-time work completes, schedule the periodic work
    WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
        "DailyWorker",
        ExistingPeriodicWorkPolicy.REPLACE,  // Replace if already exists
        periodicWorkRequest
    )
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FullProjectTheme {
        Greeting("Android")
    }
}}