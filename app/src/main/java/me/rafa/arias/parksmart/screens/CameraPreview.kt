package me.rafa.arias.parksmart.screens

import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.awaitCancellation
import java.util.concurrent.atomic.AtomicBoolean

private val PLATE_REGEX = Regex("[A-Z]{3}\\d{2,3}")

@Composable
fun CameraPreview(
    scanKey: Int,
    onPlateDetected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val detected = remember(scanKey) { AtomicBoolean(false) }

    LaunchedEffect(lifecycleOwner, scanKey) {
        val cameraProvider = ProcessCameraProvider.awaitInstance(context)

        val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }

        val analysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { ia ->
                ia.setAnalyzer(ContextCompat.getMainExecutor(context), PlateAnalyzer { plate ->
                    if (detected.compareAndSet(false, true)) {
                        onPlateDetected(plate)
                    }
                })
            }

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            analysis
        )

        try {
            awaitCancellation()
        } finally {
            cameraProvider.unbindAll()
        }
    }

    AndroidView(factory = { previewView }, modifier = modifier)
}

private class PlateAnalyzer(private val onPlateFound: (String) -> Unit) : ImageAnalysis.Analyzer {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run { imageProxy.close(); return }
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        recognizer.process(image)
            .addOnSuccessListener { result ->
                for (block in result.textBlocks) {
                    val clean = block.text.uppercase().replace("[^A-Z0-9]".toRegex(), "")
                    val match = PLATE_REGEX.find(clean) ?: continue
                    val raw = match.value
                    onPlateFound("${raw.take(3)}-${raw.drop(3)}")
                    break
                }
            }
            .addOnCompleteListener { imageProxy.close() }
    }
}
