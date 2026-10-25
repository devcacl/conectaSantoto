package com.example.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.SantotoNavy
import com.example.ui.theme.SantotoYellow
import com.example.viewmodel.SantotoViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun Explora360Screen(
    viewModel: SantotoViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val context = LocalContext.current
    val official360Url = "https://360.santototunja.edu.co/"

    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var loadingProgress by remember { mutableIntStateOf(0) }
    var isGyroscopeActive by remember { mutableStateOf(true) }
    var isPitchInverted by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }

    // Native Gyroscope / Motion Sensor Integration for 360 & VR
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager }
    val rotationSensor = remember {
        sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            ?: sensorManager?.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
            ?: sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION)
    }

    DisposableEffect(isGyroscopeActive, isPitchInverted, webViewInstance) {
        if (sensorManager == null || rotationSensor == null || !isGyroscopeActive) {
            onDispose { }
        } else {
            val sensorListener = object : SensorEventListener {
                private var lastDispatchTime = 0L

                override fun onSensorChanged(event: SensorEvent?) {
                    if (event == null) return
                    val now = System.currentTimeMillis()
                    // Throttle event dispatch to ~30 FPS (every 33ms) to avoid queueing
                    if (now - lastDispatchTime < 33) return
                    lastDispatchTime = now

                    val targetWebView = webViewInstance ?: return

                    if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR ||
                        event.sensor.type == Sensor.TYPE_GAME_ROTATION_VECTOR
                    ) {
                        val rotationMatrix = FloatArray(9)
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

                        val orientation = FloatArray(3)
                        SensorManager.getOrientation(rotationMatrix, orientation)

                        // Convert radians to degrees.
                        // CRITICAL: In Android, pitch (orientation[1]) is negative when tilted upright (-90°).
                        // In W3C DeviceOrientation spec, upright portrait beta is +90°.
                        // Negating pitch corrects the 180° upside-down tilt.
                        val rawAzimuth = Math.toDegrees(orientation[0].toDouble())
                        val rawPitch = Math.toDegrees(orientation[1].toDouble())
                        val rawRoll = Math.toDegrees(orientation[2].toDouble())

                        var beta = if (!isPitchInverted) -rawPitch else rawPitch
                        val gamma = -rawRoll
                        var alpha = (if (rawAzimuth < 0) rawAzimuth + 360.0 else rawAzimuth)

                        val js = "if(window.__bridgeSensor){window.__bridgeSensor($alpha, $beta, $gamma);}"
                        targetWebView.post {
                            targetWebView.evaluateJavascript(js, null)
                        }
                    } else if (event.sensor.type == Sensor.TYPE_ORIENTATION) {
                        val alpha = event.values[0].toDouble()
                        val rawPitch = event.values[1].toDouble()
                        val rawRoll = event.values[2].toDouble()
                        val beta = if (!isPitchInverted) -rawPitch else rawPitch
                        val gamma = -rawRoll

                        val js = "if(window.__bridgeSensor){window.__bridgeSensor($alpha, $beta, $gamma);}"
                        targetWebView.post {
                            targetWebView.evaluateJavascript(js, null)
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            sensorManager.registerListener(
                sensorListener,
                rotationSensor,
                SensorManager.SENSOR_DELAY_UI
            )

            onDispose {
                sensorManager.unregisterListener(sensorListener)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // TOP CONTROL HEADER - COMPACT TITLE AND GENEROUS BUTTON SPACING
        Surface(
            color = SantotoNavy,
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ViewInAr,
                        contentDescription = "Recorrido 360",
                        tint = SantotoYellow,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Tour 360°",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Sede Tunja",
                                fontSize = 9.sp,
                                color = SantotoYellow
                            )
                            if (isGyroscopeActive) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = SantotoYellow.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "GIROSCOPIO ON",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SantotoYellow,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Generously separated buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Toggle Gyroscope Sensor
                    IconButton(
                        onClick = {
                            isGyroscopeActive = !isGyroscopeActive
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isGyroscopeActive) SantotoYellow else Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ScreenRotation,
                            contentDescription = "Giroscopio",
                            tint = if (isGyroscopeActive) SantotoNavy else Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Toggle Invert Tilt (Vertical Flip)
                    IconButton(
                        onClick = {
                            isPitchInverted = !isPitchInverted
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isPitchInverted) SantotoYellow else Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Invertir Eje Vertical",
                            tint = if (isPitchInverted) SantotoNavy else Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Reload tour
                    IconButton(
                        onClick = {
                            hasError = false
                            isLoading = true
                            webViewInstance?.reload()
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Recargar recorrido",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Open in Browser
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(official360Url))
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SantotoYellow)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Launch,
                            contentDescription = "Abrir en navegador",
                            tint = SantotoNavy,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Loading Progress Bar
        if (isLoading) {
            LinearProgressIndicator(
                progress = { (loadingProgress / 100f).coerceIn(0.05f, 1f) },
                color = SantotoYellow,
                trackColor = SantotoNavy,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
            )
        }

        // Real Interactive Web Tour Container (Full Screen, No Bottom Tips/Labels)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("official_360_webview"),
                factory = { ctx ->
                    WebView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            databaseEnabled = true
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            cacheMode = WebSettings.LOAD_DEFAULT
                            mediaPlaybackRequiresUserGesture = false
                            allowFileAccess = true
                            allowContentAccess = true
                            javaScriptCanOpenWindowsAutomatically = true
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                loadingProgress = newProgress
                                if (newProgress >= 90) {
                                    isLoading = false
                                }
                            }

                            override fun onPermissionRequest(request: PermissionRequest?) {
                                request?.grant(request.resources)
                            }
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                isLoading = true
                                hasError = false
                                // Pre-inject DeviceOrientation API polyfill before 3DVista checks for sensors
                                val preInitJs = """
                                    (function() {
                                        if (typeof window.DeviceOrientationEvent === 'undefined') {
                                            window.DeviceOrientationEvent = function() {};
                                        }
                                        if (typeof window.DeviceMotionEvent === 'undefined') {
                                            window.DeviceMotionEvent = function() {};
                                        }
                                        window.DeviceOrientationEvent.requestPermission = function() { return Promise.resolve('granted'); };
                                        window.DeviceMotionEvent.requestPermission = function() { return Promise.resolve('granted'); };
                                    })();
                                """.trimIndent()
                                view?.evaluateJavascript(preInitJs, null)
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoading = false
                                // Sensor bridge without auto-motion and disabling unwanted auto-rotation
                                val injectionJs = """
                                    (function() {
                                        window.__bridgeSensor = function(alpha, beta, gamma) {
                                            try {
                                                var oEvent = new DeviceOrientationEvent('deviceorientation', {
                                                    alpha: alpha,
                                                    beta: beta,
                                                    gamma: gamma,
                                                    absolute: true,
                                                    bubbles: true,
                                                    cancelable: false
                                                });
                                                window.dispatchEvent(oEvent);
                                            } catch(e) {
                                                try {
                                                    var oEvent = document.createEvent('DeviceOrientationEvent');
                                                    if (oEvent.initDeviceOrientationEvent) {
                                                        oEvent.initDeviceOrientationEvent('deviceorientation', true, false, alpha, beta, gamma, true);
                                                        window.dispatchEvent(oEvent);
                                                    }
                                                } catch(err) {}
                                            }
                                        };

                                        // Stop unintended autorotation in 3DVista and activate sensor mode
                                        function haltAutoRotation() {
                                            try {
                                                if (window.TDV && window.TDV.Tour && window.TDV.Tour.Script) {
                                                    var script = window.TDV.Tour.Script;
                                                    var players = (typeof script.getCurrentPlayers === 'function') ? script.getCurrentPlayers() : [];
                                                    players.forEach(function(p) {
                                                        try {
                                                            p.set('gyroscopeEnabled', true);
                                                            var cam = p.get('camera');
                                                            if (cam) {
                                                                cam.set('automaticRotationSpeed', 0);
                                                                cam.set('timeToIdle', 99999999);
                                                                var s1 = cam.get('initialSequence');
                                                                if (s1 && typeof s1.pause === 'function') s1.pause();
                                                                var s2 = cam.get('idleSequence');
                                                                if (s2 && typeof s2.pause === 'function') s2.pause();
                                                            }
                                                        } catch(err) {}
                                                    });
                                                }
                                                if (window.tour && window.tour.player) {
                                                    var cam = (typeof window.tour.player.getCamera === 'function') ? window.tour.player.getCamera() : null;
                                                    if (cam && typeof cam.set === 'function') {
                                                        cam.set('automaticRotationSpeed', 0);
                                                        cam.set('timeToIdle', 99999999);
                                                    }
                                                }
                                                var viewer = document.getElementById('viewer');
                                                if (viewer) {
                                                    viewer.dispatchEvent(new MouseEvent('mousedown', { bubbles: true, cancelable: true, clientX: 200, clientY: 200 }));
                                                    viewer.dispatchEvent(new MouseEvent('mouseup', { bubbles: true, cancelable: true, clientX: 200, clientY: 200 }));
                                                }
                                            } catch(e) {}
                                        }

                                        setTimeout(haltAutoRotation, 200);
                                        setTimeout(haltAutoRotation, 600);
                                        setTimeout(haltAutoRotation, 1200);
                                        setTimeout(haltAutoRotation, 2500);
                                        setTimeout(haltAutoRotation, 4000);
                                    })();
                                """.trimIndent()
                                evaluateJavascript(injectionJs, null)
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                if (request?.isForMainFrame == true) {
                                    hasError = true
                                    isLoading = false
                                }
                            }
                        }

                        webViewInstance = this
                        loadUrl(official360Url)
                    }
                },
                update = { webView ->
                    webViewInstance = webView
                }
            )

            // Loading spinner overlay
            if (isLoading && !hasError) {
                Surface(
                    color = Color.Black.copy(alpha = 0.65f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = SantotoYellow,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Cargando recorrido 360°...",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Error Fallback Card if connection drops
            if (hasError) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SantotoNavy),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            tint = SantotoYellow,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Recorrido Virtual Santoto Tunja",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Explora los edificios, laboratorios y claustros históricos en 360°.",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    hasError = false
                                    isLoading = true
                                    webViewInstance?.loadUrl(official360Url)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f))
                            ) {
                                Text("Reintentar", color = Color.White, fontSize = 12.sp)
                            }
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(official360Url))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SantotoYellow)
                            ) {
                                Text("Abrir en Chrome", color = SantotoNavy, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
