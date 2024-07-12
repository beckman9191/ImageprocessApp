package com.example.imageprocessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.imageprocessapp.ui.theme.ImageprocessAppTheme
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Sets the content of the activity to use Compose UI
        setContent {
            ImageprocessAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ImageProcessor(
                        // Composable function that handles image loading and processing
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    companion object {
        init {
            // Loads the native library that contains C++ code
            System.loadLibrary("native-lib")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageProcessor(modifier: Modifier = Modifier) {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) } // State to hold the loaded image
    val context = LocalContext.current

    // Setup an activity result launcher to pick an image from the device
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri) // Opens an input stream to read the image data
            imageBitmap = BitmapFactory.decodeStream(inputStream) // Decodes the image data into a Bitmap
        }
    }

    // UI layout using Column to stack elements vertically.

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Image Process by Timmy") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(

                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = modifier
                .padding(70.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center, // Centers items vertically
            horizontalAlignment = Alignment.CenterHorizontally // Centers items horizontally


        ) {
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Load Image")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                imageBitmap?.let {
                    val processedImage = applyGrayscale(it)
                    imageBitmap = processedImage // Update the state with the processed image
                }
            }) {
                Text("Apply Grayscale")
            }

            Spacer(modifier = Modifier.height(8.dp))

            imageBitmap?.let {
                // Display the processed image
                Image(bitmap = it.asImageBitmap(), contentDescription = "Processed Image")
            }
        }
    }

}


fun applyGrayscale(originalBitmap: Bitmap): Bitmap {
    val width = originalBitmap.width // Get the width of the bitmap
    val height = originalBitmap.height // Get the height of the bitmap
    val grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) // Create a new bitmap for the grayscale image

    val pixels = IntArray(width * height) // Array to hold pixel data
    originalBitmap.getPixels(pixels, 0, width, 0, 0, width, height) // Extract pixels from the original bitmap
    nativeApplyGrayscale(pixels, width, height) // JNI function to modify pixels array
    grayscaleBitmap.setPixels(pixels, 0, width, 0, 0, width, height) // Set the modified pixels back to the bitmap

    return grayscaleBitmap
}

external fun nativeApplyGrayscale(pixels: IntArray, width: Int, height: Int)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ImageprocessAppTheme {
        ImageProcessor()
    }
}