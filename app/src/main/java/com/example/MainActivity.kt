package com.example

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        NexusBuilderScreen()
      }
    }
  }
}

@Composable
fun NexusBuilderScreen() {
  var buildLogs by remember { mutableStateOf(listOf(
    Pair("[INFO]", "Initializing APK build pipeline..."),
    Pair("[INFO]", "Target: Android 14 (API 34)"),
    Pair("[WARN]", "Optimization level: O3 enabled"),
    Pair("[DEBUG]", "Waiting for source input...")
  )) }
  
  val scope = rememberCoroutineScope()
  var progress by remember { mutableFloatStateOf(0f) }

  val filePicker = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    uri?.let {
      // Boilerplate for ZIP handling
      // Here you would use contentResolver.openInputStream(uri) and ZipInputStream
      buildLogs = buildLogs + Pair("[INFO]", "Selected file: ${it.lastPathSegment}")
      buildLogs = buildLogs + Pair("[INFO]", "Extracting to workspace...")
      
      // Simulate extraction
      scope.launch {
        for (i in 1..10) {
          delay(100)
          progress = i * 10f
        }
        buildLogs = buildLogs + Pair("[SUCCESS]", "Extraction complete. Ready to build.")
      }
    }
  }

  Box(modifier = Modifier.fillMaxSize().background(BgDark)) {
    // Ambient Background Gradients
    Box(
      modifier = Modifier
        .offset(x = 100.dp, y = (-100).dp)
        .size(300.dp)
        .background(Cyan600.copy(alpha = 0.2f), shape = CircleShape)
        .blur(80.dp)
    )
    Box(
      modifier = Modifier
        .offset(x = (-150).dp, y = 300.dp)
        .size(400.dp)
        .background(Blue900.copy(alpha = 0.15f), shape = CircleShape)
        .blur(100.dp)
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
    ) {
      // Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(
                brush = Brush.linearGradient(
                  colors = listOf(Cyan400, Blue600),
                  start = Offset(0f, 0f),
                  end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Terminal, contentDescription = "Terminal", tint = Color.White)
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "Nexus Builder",
              color = Slate100,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "V2.4.0 • API 34",
              color = Cyan400,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold,
              letterSpacing = 1.sp
            )
          }
        }
        
        IconButton(
          onClick = { /* Settings */ },
          modifier = Modifier
            .size(40.dp)
            .background(White5, CircleShape)
            .border(1.dp, White10, CircleShape)
        ) {
          Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Slate100)
        }
      }

      // Main Content
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 24.dp)
      ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        // App Configuration
        Box(modifier = Modifier.padding(top = 8.dp)) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .background(White5)
              .border(1.dp, White10, RoundedCornerShape(16.dp))
              .padding(16.dp)
          ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
              Column(modifier = Modifier.weight(1f)) {
                Text("APP NAME", fontSize = 10.sp, color = Slate400, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(White5)
                    .border(1.dp, White5, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                  Text("Nexus-Project-Alpha", fontSize = 14.sp, color = Slate100, fontWeight = FontWeight.Medium)
                }
              }
              Column(modifier = Modifier.weight(1f)) {
                Text("PACKAGE", fontSize = 10.sp, color = Slate400, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(White5)
                    .border(1.dp, White5, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                  Text("com.nexus.alpha", fontSize = 14.sp, color = Cyan400.copy(alpha = 0.7f), fontWeight = FontWeight.Medium)
                }
              }
            }
          }
          Text(
            text = "APP CONFIGURATION",
            fontSize = 10.sp,
            color = Cyan400,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
              .offset(x = 12.dp, y = (-8).dp)
              .background(BgDark)
              .padding(horizontal = 4.dp),
            letterSpacing = 1.sp
          )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Import Button
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
              brush = Brush.horizontalGradient(
                colors = listOf(Cyan600.copy(alpha = 0.4f), Blue900.copy(alpha = 0.4f))
              )
            )
            .border(1.dp, Cyan400.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .clickable { filePicker.launch("application/zip") }
            .padding(20.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .background(Cyan400.copy(alpha = 0.1f), CircleShape)
                .border(4.dp, Cyan400.copy(alpha = 0.05f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.UploadFile, contentDescription = "Import", tint = Cyan400, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Import Source ZIP", color = Slate100, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Scoped Storage Access Enabled", color = Slate400, fontSize = 11.sp)
          }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Build Log Section
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(6.dp).background(Cyan500, CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text("BUILD LOG", fontSize = 11.sp, color = Slate400, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
          }
          Text("${progress.toInt()}%", fontSize = 10.sp, color = Cyan400, fontFamily = FontFamily.Monospace)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.6f))
            .border(1.dp, White10, RoundedCornerShape(16.dp))
            .padding(16.dp)
        ) {
          Column {
            buildLogs.forEach { log ->
              val color = when (log.first) {
                "[INFO]" -> Emerald400
                "[DEBUG]" -> Cyan400
                "[WARN]" -> Color(0xFFFBBF24)
                "[SUCCESS]" -> Emerald400
                else -> Slate400
              }
              Row {
                Text(log.first, color = color, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.width(4.dp))
                Text(log.second, color = Slate100.copy(alpha = 0.8f), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
              }
              Spacer(modifier = Modifier.height(6.dp))
            }
            Text("_", color = Slate400, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
          }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
      }

      // Footer
      Column(modifier = Modifier.padding(24.dp)) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(CircleShape)
            .background(White5)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth(fraction = progress / 100f)
              .fillMaxHeight()
              .background(Brush.horizontalGradient(listOf(Cyan400, Blue600)))
          )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
          onClick = {
            scope.launch {
              buildLogs = buildLogs + Pair("[INFO]", "Starting build process...")
              for (i in (progress.toInt())..100 step 5) {
                delay(150)
                progress = i.toFloat()
                if (i == 30) buildLogs = buildLogs + Pair("[INFO]", "Compiling resources...")
                if (i == 60) buildLogs = buildLogs + Pair("[INFO]", "Dexing classes...")
                if (i == 90) buildLogs = buildLogs + Pair("[INFO]", "Signing APK (v2/v3)...")
              }
              buildLogs = buildLogs + Pair("[SUCCESS]", "APK Generated successfully!")
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(listOf(Cyan500, Blue600))),
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
          Icon(Icons.Default.RocketLaunch, contentDescription = "Generate", tint = Color.White)
          Spacer(modifier = Modifier.width(12.dp))
          Text("GENERATE APK", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(White5)
              .border(1.dp, White10, CircleShape)
              .padding(horizontal = 16.dp, vertical = 6.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.VerifiedUser, contentDescription = "Verified", tint = Emerald400, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("V2/V3 SIGNING READY", color = Slate400, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}
