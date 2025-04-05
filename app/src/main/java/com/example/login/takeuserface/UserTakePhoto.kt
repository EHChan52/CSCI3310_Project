package com.example.login.takeuserface

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.AspectRatio
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size as ComposeSize
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.mlkit.vision.common.InputImage
import okhttp3.Request
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors
import java.io.File

// Data class to store detected face information for the overlay
data class DetectedFace(
    val boundingBox: Rect,
    val id: Int? = null
)

@Composable
fun CameraScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Face detection state
    var faceDetected by remember { mutableStateOf(false) }

    // Store the detected faces for drawing the overlay
    var detectedFaces by remember { mutableStateOf<List<DetectedFace>>(emptyList()) }

    // For camera preview dimensions
    var previewWidth by remember { mutableStateOf(0) }
    var previewHeight by remember { mutableStateOf(0) }

    // For storing the ImageCapture instance with better quality settings
    val imageCaptureUseCase = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setTargetAspectRatio(AspectRatio.RATIO_4_3) // Using setTargetAspectRatio instead of ResolutionSelector
            .build()
    }

    // Check required permissions
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // For storage permission if needed (API < 29)
    var hasStoragePermission by remember {
        mutableStateOf(
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launchers
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> 
        hasCameraPermission = isGranted
    }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> 
        hasStoragePermission = isGranted
    }

    // Check and request storage permission if needed
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !hasStoragePermission) {
            storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 50.dp)
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // Text element
        Text(
            text = "Take a photo of your face",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Camera preview box with face detection status
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(if (faceDetected) Color(0xFFE0F7E0) else Color.LightGray) // Green tint when face detected
                .border(2.dp, if (faceDetected) Color(0xFF4CAF50) else Color.Black),
            contentAlignment = Alignment.Center
        ) {
            if (hasCameraPermission) {
                // Show camera preview with face detection
                CameraPreviewWithFaceDetection(
                    modifier = Modifier
                        .fillMaxWidth(),
                    lifecycleOwner = lifecycleOwner,
                    onFaceDetected = { faces, width, height -> 
                        faceDetected = faces.isNotEmpty()
                        detectedFaces = faces.map { face -> 
                            DetectedFace(face.boundingBox, face.trackingId)
                        }
                        // Update the preview dimensions for accurate overlay scaling
                        previewWidth = width
                        previewHeight = height
                    },
                    imageCaptureUseCase = imageCaptureUseCase
                )

                // Remove the face overlay that draws boxes around detected faces
                // Instead, you can optionally draw a fixed center box to indicate capture area
                if (previewWidth > 0 && previewHeight > 0) {
                    FixedCenterOverlay(
                        previewWidth = previewWidth,
                        previewHeight = previewHeight,
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(10f)
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Camera permission is required")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }) {
                        Text("Request permission")
                    }
                }
            }

            // Status text for face detection
            if (hasCameraPermission) {
                Text(
                    text = if (faceDetected) "Face detected! Center your face and take a photo" else "No face detected",
                    color = if (faceDetected) Color(0xFF4CAF50) else Color.Red,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                        .background(Color(0x88000000))
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Two buttons in a row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    if (faceDetected) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !hasStoragePermission) {
                            Toast.makeText(
                                context,
                                "Storage permission is required to save photos",
                                Toast.LENGTH_SHORT).show()
                            storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        } else {
                            // Create a fixed rectangle at the center of the preview
                            Log.d("CameraCapture", "Attempting to capture photo with face detected")
                            captureFixedAreaPhoto(
                                context,
                                imageCaptureUseCase,
                                previewWidth,
                                previewHeight,
                                navController  // Pass navController to handle navigation
                            )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                enabled = hasCameraPermission && faceDetected
            ) {
                Text("Take Photo", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

// Replace FaceOverlay with FixedCenterOverlay that shows a fixed center area
@Composable
private fun FixedCenterOverlay(
    previewWidth: Int,
    previewHeight: Int,
    modifier: Modifier = Modifier
) {
    // Draw a fixed box in the center (invisible by default - comment out if you want to show it)
    Canvas(modifier = modifier) {
        // This overlay is invisible - uncomment the drawRect to make it visible
        // Keeping this function in case you want a visual indicator in the future

        //Uncomment if you want to show the capture area
        val canvasWidth = size.width
        val canvasHeight = size.height
        
        // Define a fixed size for the center box (e.g., 70% of the preview width and height)
        val boxWidth = canvasWidth * 0.7f
        val boxHeight = canvasHeight * 0.7f
        
        // Calculate the center point
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2
        
        // Calculate the top-left point of the box
        val left = centerX - (boxWidth / 2)
        val top = centerY - (boxHeight / 2)
        
        // Draw a semi-transparent outline to indicate capture area
        drawRect(
            color = Color.White.copy(alpha = 0.5f),
            topLeft = Offset(left, top),
            size = ComposeSize(boxWidth, boxHeight),
            style = Stroke(width = 2f)
        )
        
    }
}

// New function to capture a fixed center area
private fun captureFixedAreaPhoto(
    context: Context,
    imageCapture: ImageCapture,
    previewWidth: Int,
    previewHeight: Int,
    navController: NavController  // Add NavController parameter
) {
    // Log the capture attempt
    Log.d("CameraCapture", "Starting center area photo capture process")

    try {
        // Create time-stamped output file to hold the image
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())
        val photoFileName = "FACE_$timeStamp.png"

        // Create a temporary file for full image before cropping
        val tempFileName = "TEMP_$timeStamp.png"
        val tempDir = File(context.cacheDir, "temp").apply {
            if (!exists()) mkdirs()
        }
        val tempFile = File(tempDir, tempFileName)

        // Create output options for the temporary file
        val tempOutputOptions = ImageCapture.OutputFileOptions.Builder(tempFile).build()

        // Capture the full image first to a temp file
        Log.d("CameraCapture", "Taking full picture to temporary file...")
        imageCapture.takePicture(
            tempOutputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    try {
                        // Process and crop the image
                        val bitmap = android.graphics.BitmapFactory.decodeFile(tempFile.absolutePath)

                        // Define the fixed center area (e.g., 70% of the bitmap dimensions)
                        val centerX = bitmap.width / 2
                        val centerY = bitmap.height / 2
                        val boxWidth = (bitmap.width * 0.7f).toInt()
                        val boxHeight = (bitmap.height * 0.7f).toInt()

                        // Calculate the crop rectangle
                        val cropRect = Rect(
                            centerX - (boxWidth / 2),
                            centerY - (boxHeight / 2),
                            centerX + (boxWidth / 2),
                            centerY + (boxHeight / 2)
                        )

                        // Ensure the rectangle is valid and within bounds
                        val validCropRect = Rect(
                            cropRect.left.coerceAtLeast(0),
                            cropRect.top.coerceAtLeast(0),
                            cropRect.right.coerceAtMost(bitmap.width),
                            cropRect.bottom.coerceAtMost(bitmap.height)
                        )

                        // Ensure the rectangle is valid
                        if (validCropRect.width() <= 0 || validCropRect.height() <= 0) {
                            throw Exception("Invalid crop rectangle dimensions")
                        }

                        // Crop the bitmap to the center area
                        val croppedBitmap = android.graphics.Bitmap.createBitmap(
                            bitmap,
                            validCropRect.left,
                            validCropRect.top,
                            validCropRect.width(),
                            validCropRect.height()
                        )
                        
                        // Rotate the cropped bitmap 90 degrees anticlockwise
                        val matrix = android.graphics.Matrix().apply {
                            postRotate(-90f) // Negative for anticlockwise rotation
                        }
                        
                        // Create a new bitmap with the rotation applied
                        // Note: We're switching width and height due to the 90-degree rotation
                        val rotatedBitmap = android.graphics.Bitmap.createBitmap(
                            croppedBitmap,
                            0, 0,
                            croppedBitmap.width, croppedBitmap.height,
                            matrix,
                            true
                        )
                        
                        // Log the rotation
                        Log.d("CameraCapture", "Rotated image 90 degrees anticlockwise: ${croppedBitmap.width}x${croppedBitmap.height} -> ${rotatedBitmap.width}x${rotatedBitmap.height}")

                        // Save the rotated bitmap to the final destination
                        val finalFile: File
                        val savedUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            // For Android 10 and above, use MediaStore
                            val contentValues = ContentValues().apply {
                                put(MediaStore.MediaColumns.DISPLAY_NAME, photoFileName)
                                put(MediaStore.MediaColumns.MIME_TYPE, "image/png") // Changed to PNG
                                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FaceAuth")
                            }

                            val uri = context.contentResolver.insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                contentValues
                            )

                            uri?.let {
                                context.contentResolver.openOutputStream(it)?.use { os -> 
                                    // Save rotated bitmap as PNG instead of JPEG
                                    rotatedBitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, os)
                                }
                            }
                            uri
                        } else {
                            // For older versions, use the external media directory
                            val storageDir = context.getExternalFilesDir("Pictures/FaceAuth")
                            if (storageDir == null) {
                                // Fallback to internal storage
                                val internalDir = File(context.filesDir, "Pictures/FaceAuth").apply {
                                    if (!exists()) mkdirs()
                                }
                                finalFile = File(internalDir, photoFileName)
                            } else {
                                storageDir.mkdirs() // Ensure directory exists
                                finalFile = File(storageDir, photoFileName)
                            }

                            // Save the rotated bitmap to file as PNG
                            finalFile.outputStream().use { os -> 
                                // Save rotated bitmap as PNG instead of JPEG
                                rotatedBitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, os)
                            }
                            android.net.Uri.fromFile(finalFile)
                        }

                        // Clean up temporary file
                        tempFile.delete()

                        // Recycle bitmaps to free memory
                        bitmap.recycle()
                        croppedBitmap.recycle()
                        rotatedBitmap.recycle() // Recycle the new rotated bitmap as well

                        // Notify success
                        val msg = "Photo captured, please review"
                        Log.d("CameraCapture", "$msg: $savedUri")
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        
                        // Navigate to the image review screen with the URI
                        // We need to encode the URI as it may contain special characters
                        val encodedUri = URLEncoder.encode(savedUri.toString(), StandardCharsets.UTF_8.toString())
                        navController.navigate("image_review_screen/$encodedUri")
                    } catch (e: Exception) {
                        Log.e("CameraCapture", "Error processing and cropping image", e)
                        Toast.makeText(context, "Failed to process face image: ${e.message}", Toast.LENGTH_SHORT).show()
                        tempFile.delete() // Clean up temp file on error
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    val errorMsg = "Photo capture failed: ${exception.message}"
                    Log.e("CameraCapture", errorMsg, exception)
                    Log.e("CameraCapture", "Error code: ${exception.imageCaptureError}")
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }
        )
    } catch (e: Exception) {
        Log.e("CameraCapture", "Setup failed: ${e.message}", e)
        Toast.makeText(context, "Failed to setup photo capture: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

// Add the missing CameraPreviewWithFaceDetection function
@Composable
private fun CameraPreviewWithFaceDetection(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner,
    onFaceDetected: (List<Face>, Int, Int) -> Unit, // Added width and height parameters
    imageCaptureUseCase: ImageCapture
) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    
    // Store the PreviewView reference for getting dimensions
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    
    // Setup ML Kit face detector with improved options for accuracy
    val faceDetectorOptions = remember {
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()
    }
    
    val faceDetector = remember { FaceDetection.getClient(faceDetectorOptions) }
    
    // Image analysis use case with compatible configuration
    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetAspectRatio(AspectRatio.RATIO_4_3) // Using setTargetAspectRatio instead
            .build()
    }
    
    // Set the analyzer separately
    DisposableEffect(imageAnalysis) {
        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy -> 
            // Pass the preview dimensions along with the detected faces
            processImageForFaceDetection(
                imageProxy, 
                faceDetector, 
                { faces -> 
                    val width = previewView?.width ?: 0
                    val height = previewView?.height ?: 0
                    onFaceDetected(faces, width, height)
                }
            )
        }
        
        onDispose {
            imageAnalysis.clearAnalyzer()
            cameraExecutor.shutdown()
        }
    }
    
    AndroidView(
        modifier = modifier,
        factory = { ctx -> 
            val view = PreviewView(ctx)
            previewView = view
            
            val executor = ContextCompat.getMainExecutor(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(view.surfaceProvider)
                }
                
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis,
                        imageCaptureUseCase // Add image capture use case
                    )
                } catch (e: Exception) {
                    Log.e("CameraPreview", "Use case binding failed", e)
                }
            }, executor)
            
            view
        },
        update = { view -> 
            // Store the preview view reference and dimensions
            previewView = view
        }
    )
}

@OptIn(ExperimentalGetImage::class)
private fun processImageForFaceDetection(
    imageProxy: ImageProxy,
    faceDetector: FaceDetector,
    onFaceDetected: (List<Face>) -> Unit
) {
    // Add validation to ensure image is sufficient quality for processing
    if (imageProxy.width < 480 || imageProxy.height < 360) {
        Log.w("FaceDetection", "Image too small for reliable face detection: ${imageProxy.width}x${imageProxy.height}")
        onFaceDetected(emptyList())
        imageProxy.close()
        return
    }
    
    val mediaImage = imageProxy.image
    if (mediaImage == null) {
        Log.e("FaceDetection", "Image proxy had no image")
        imageProxy.close()
        onFaceDetected(emptyList())
        return
    }
    
    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
    
    // Create InputImage with proper rotation
    val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)
    
    // Track detection latency for performance monitoring
    val startTime = System.currentTimeMillis()
    
    faceDetector.process(image)
        .addOnSuccessListener { faces -> 
            val detectionTime = System.currentTimeMillis() - startTime
            Log.d("FaceDetection", "Detection completed in ${detectionTime}ms, found ${faces.size} faces")
            
            // Only report faces that are large enough for accurate detection
            val validFaces = faces.filter { face -> 
                val faceWidth = face.boundingBox.width()
                val faceHeight = face.boundingBox.height()
                
                // Ensure face is at least 100x100 pixels (or 200x200 if we were doing contour detection)
                val isLargeEnough = faceWidth >= 100 && faceHeight >= 100
                
                if (!isLargeEnough) {
                    Log.d("FaceDetection", "Found face too small for accurate detection: ${faceWidth}x${faceHeight}")
                }
                
                isLargeEnough
            }
            
            onFaceDetected(validFaces)
        }
        .addOnFailureListener { e -> 
            Log.e("FaceDetection", "Face detection failed", e)
            onFaceDetected(emptyList())
        }
        .addOnCompleteListener {
            imageProxy.close()
        }
}

@Composable
fun ImageReviewScreen(navController: NavController, imageUriString: String) {
    val context = LocalContext.current
    val decodedUri = Uri.parse(java.net.URLDecoder.decode(imageUriString, StandardCharsets.UTF_8.toString()))
    
    // Create coroutine scope at the Composable level where it's valid
    val coroutineScope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Photo Review",
            fontSize = 24.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        // Image display box
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.LightGray)
                .border(2.dp, Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(decodedUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Captured face photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.popBackStack() }, // Go back to camera
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Retake Photo")
            }
            
            Button(
                onClick = {
                    Toast.makeText(context, "Processing photo...", Toast.LENGTH_SHORT).show()
                    
                    coroutineScope.launch(Dispatchers.IO) {
                        try {
                            // Use ContentResolver to get an InputStream from the URI
                            val inputStream = context.contentResolver.openInputStream(decodedUri)
                            if (inputStream == null) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Failed to read image data", Toast.LENGTH_SHORT).show()
                                }
                                return@launch
                            }
                            
                            // Read the image data into a ByteArray
                            val imageBytes = inputStream.use { it.readBytes() }
                            
                            // Convert image to base64 for Segmind API
                            val base64Image = android.util.Base64.encodeToString(
                                imageBytes, 
                                android.util.Base64.NO_WRAP
                            )
                            
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Removing background...", Toast.LENGTH_SHORT).show()
                            }
                            
                            // Create OkHttpClient with timeout configuration
                            val client = OkHttpClient.Builder()
                                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                                .build()
                            
                            // Segmind API setup 
                            val apiKey = "SG_b78e15ed8491c323"
                            val segmindUrl = "https://api.segmind.com/v1/background-eraser"
                            
                            // Create JSON payload matching the Python example
                            val jsonPayload = """
                                {
                                  "image": "$base64Image",
                                  "return_mask": false,
                                  "invert_mask": false,
                                  "grow_mask": 0,
                                  "base64": false
                                }
                            """.trimIndent()
                            
                            val jsonMediaType = "application/json; charset=utf-8".toMediaType()
                            val segmindRequestBody = jsonPayload.toRequestBody(jsonMediaType)
                            
                            val segmindRequest = Request.Builder()
                                .url(segmindUrl)
                                .header("x-api-key", apiKey)
                                .post(segmindRequestBody)
                                .build()
                            
                            try {
                                // Execute the API call
                                val segmindResponse = client.newCall(segmindRequest).execute()
                                
                                if (segmindResponse.isSuccessful) {
                                    // Get the response as binary data (not base64)
                                    val processedImageBytes = segmindResponse.body?.bytes()
                                    
                                    if (processedImageBytes != null) {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(context, "Background removed successfully", Toast.LENGTH_SHORT).show()
                                        }
                                        
                                        // Save the image as "user-face.png"
                                        val processedFileName = "user-face.png"
                                        
                                        // File to store processed image
                                        val storageDir = context.getExternalFilesDir("Pictures/FaceAuth")
                                        storageDir?.mkdirs()
                                        val outputFile = File(storageDir, processedFileName)
                                        
                                        // Write binary data directly to file
                                        outputFile.writeBytes(processedImageBytes)
                                        
                                        Log.d("ImageReview", "Saved background-removed image to ${outputFile.absolutePath}")
                                        
                                        // Save a copy to MediaStore for gallery access
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            val contentValues = ContentValues().apply {
                                                put(MediaStore.MediaColumns.DISPLAY_NAME, processedFileName)
                                                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                                                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FaceAuth")
                                            }
                                            
                                            context.contentResolver.insert(
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                contentValues
                                            )?.let { newUri ->
                                                context.contentResolver.openOutputStream(newUri)?.use { os ->
                                                    os.write(processedImageBytes)
                                                }
                                            }
                                        }
                                        
                                        // Navigate directly to login screen after saving the image
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(context, "Face image saved successfully", Toast.LENGTH_SHORT).show()
                                            navController.navigate("login_screen") {
                                                popUpTo("login_screen") { inclusive = true }
                                            }
                                        }
                                    } else {
                                        throw Exception("Received empty response from background removal API")
                                    }
                                } else {
                                    throw Exception("Background removal failed: ${segmindResponse.message}")
                                }
                            } catch (e: Exception) {
                                Log.e("ImageReview", "Error during background removal: ${e.message}", e)
                            }
                        } catch (e: Exception) {
                            Log.e("ImageReview", "Error processing image: ${e.message}", e)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text("Use This Photo", color = Color.White)
            }
        }
    }
}
