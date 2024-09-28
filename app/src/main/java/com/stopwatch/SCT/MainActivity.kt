package com.stopwatch.SCT

import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
    }
}

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, InfoFragment())
            .commit()
    }
}

class InfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.information, container, false)
    }
}


class MainActivity : AppCompatActivity() {
    private fun isVibrationEnabled(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreferences.getBoolean("vibrate_val", true)
    }

    private fun isResetConfirmationEnabled(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreferences.getBoolean("reset_confirm", true)
    }

    private lateinit var timerTextView: TextView
    private lateinit var stopResetButton: Button
    private lateinit var lapResumeButton: Button
    private lateinit var uploadButton: Button
    private lateinit var startButton: Button
    private lateinit var lapTimesListView: ListView
    private val handler = Handler(Looper.getMainLooper())
    private var startTime: Long = 0
    private var timeInMilliseconds: Long = 0
    private var isRunning = false
    private val lapTimes = ArrayList<String>()
    private lateinit var adapter: LapTimesAdapter
    private lateinit var enableBluetoothLauncher: ActivityResultLauncher<Intent>

    companion object {
        private const val REQUEST_PERMISSIONS = 2
        private const val TAG = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableBluetoothLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->

            }
        timerTextView = findViewById(R.id.timer)
        stopResetButton = findViewById(R.id.stop_reset_button)
        lapResumeButton = findViewById(R.id.lap_resume_button)
        startButton = findViewById(R.id.start_button)
        uploadButton = findViewById(R.id.upload_button)
        lapTimesListView = findViewById(R.id.lap_times)
        adapter = LapTimesAdapter(this, lapTimes)
        lapTimesListView.adapter = adapter
        stopResetButton.visibility = View.GONE
        lapResumeButton.visibility = View.GONE
        lapResumeButton.text = getString(R.string.lap)
        timerTextView.text = getString(R.string._00_00_0)
        lapResumeButton.setOnClickListener {
            if (isRunning) {
                recordLap()
            } else {
                startTimer()
            }
        }
        startButton.setOnClickListener {
            startTimer()
        }
        stopResetButton.setOnClickListener {
            if (isRunning) {
                stopTimer()
            } else {
                resetTimer()
            }
        }
        uploadButton.setOnClickListener { uploadResults() }
        checkAndRequestPermissions()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_info -> {
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkAndRequestPermissions() {
        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.BLUETOOTH_SCAN,
                android.Manifest.permission.BLUETOOTH_CONNECT,
            )
        } else {
            arrayOf(
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS
            )
        } else {
            enableBluetooth()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Log.d(TAG, "All permissions granted")
                enableBluetooth()
            } else {
                Log.d(TAG, "Permissions denied")
                Toast.makeText(
                    this,
                    "Permissions are required for Bluetooth operations",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun enableBluetooth() {
        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.BLUETOOTH_SCAN,
                android.Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN
            )
        }
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS
            )
        } else {
            try {
                val bluetoothManager =
                    getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                val bluetoothAdapter = bluetoothManager.adapter ?: return
                if (!bluetoothAdapter.isEnabled) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    enableBluetoothLauncher.launch(enableBtIntent)
                }
            } catch (e: SecurityException) {
                Log.e(TAG, "SecurityException: ${e.message}")
                Toast.makeText(this, "Bluetooth permissions are required", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("timeInMilliseconds", timeInMilliseconds)
        outState.putLong("elapsedTime", System.currentTimeMillis() - startTime)
        outState.putBoolean("isRunning", isRunning)
        outState.putStringArrayList("lapTimes", ArrayList(lapTimes))
        outState.putInt("stopResetButtonVisibility", stopResetButton.visibility)
        outState.putString("stopResetButtonText", stopResetButton.text.toString())
        outState.putBoolean("uploadButtonEnabled", uploadButton.isEnabled)
        outState.putString("lapResumeButtonText", lapResumeButton.text.toString())
        outState.putInt("lapResumeButtonVisibility", lapResumeButton.visibility)
        outState.putInt("startButtonVisibility", startButton.visibility)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        timeInMilliseconds = savedInstanceState.getLong("timeInMilliseconds")
        val elapsedTime = savedInstanceState.getLong("elapsedTime")
        isRunning = savedInstanceState.getBoolean("isRunning")
        lapTimes.clear()
        lapTimes.addAll(savedInstanceState.getStringArrayList("lapTimes") ?: arrayListOf())
        stopResetButton.visibility = savedInstanceState.getInt("stopResetButtonVisibility")
        stopResetButton.text = savedInstanceState.getString("stopResetButtonText")
        uploadButton.isEnabled = savedInstanceState.getBoolean("uploadButtonEnabled")
        lapResumeButton.text = savedInstanceState.getString("lapResumeButtonText")
        lapResumeButton.visibility = savedInstanceState.getInt("lapResumeButtonVisibility")
        startButton.visibility = savedInstanceState.getInt("startButtonVisibility")
        if (isRunning) {
            startTime = System.currentTimeMillis() - elapsedTime
            handler.postDelayed(updateTimerThread, 0)
        } else {
            updateTimer()
        }
    }

    private fun updateTimer() {
        if (isRunning) {
            val updatedTime = System.currentTimeMillis() - startTime + timeInMilliseconds
            timerTextView.text = formatTime(updatedTime)
        } else {
            timerTextView.text = formatTime(timeInMilliseconds)
        }
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        stopResetButton.isEnabled = enabled
        lapResumeButton.isEnabled = enabled
    }


    private fun startTimer() {
        setButtonsEnabled(false)
        startTime = System.currentTimeMillis()
        handler.postDelayed(updateTimerThread, 0)
        isRunning = true
        startButton.visibility = View.GONE
        stopResetButton.text = getString(R.string.stop)
        stopResetButton.visibility = View.VISIBLE
        lapResumeButton.text = getString(R.string.lap)
        lapResumeButton.visibility = View.VISIBLE
        uploadButton.isEnabled = false

        if (isVibrationEnabled()) {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        setButtonsEnabled(true)
    }

    private fun stopTimer() {
        setButtonsEnabled(false)
        timeInMilliseconds += System.currentTimeMillis() - startTime
        handler.removeCallbacks(updateTimerThread)
        isRunning = false
        stopResetButton.text = getString(R.string.reset)
        lapResumeButton.text = getString(R.string.resume)
        uploadButton.isEnabled = true
        if (isVibrationEnabled()) {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        setButtonsEnabled(true)
    }

    private fun resetTimer() {
        setButtonsEnabled(false)
        uploadButton.isEnabled = false
        if (isResetConfirmationEnabled()) {
            AlertDialog.Builder(this)
                .setTitle("Reset Timer")
                .setMessage("Are you sure you want to reset the timer?")
                .setPositiveButton("yes") { _, _ ->
                    resetTimerActions()
                }
                .setNegativeButton("no") { _, _ ->
                    setButtonsEnabled(true)
                    uploadButton.isEnabled = true
                }
                .setOnCancelListener {
                    setButtonsEnabled(true)
                    uploadButton.isEnabled = true
                }
                .show()
        } else {
            resetTimerActions()
        }

    }

    private fun resetTimerActions() {
        timeInMilliseconds = 0
        updateTimer()
        lapTimes.clear()
        adapter.notifyDataSetChanged()
        stopResetButton.visibility = View.GONE
        lapResumeButton.visibility = View.GONE
        lapResumeButton.text = getString(R.string.lap)
        startButton.visibility = View.VISIBLE
        uploadButton.isEnabled = false
    }


    private fun recordLap() {
        setButtonsEnabled(false)
        val lapTime = System.currentTimeMillis() - startTime + timeInMilliseconds
        lapTimes.add("Lap ${lapTimes.size + 1} | ${formatTime(lapTime)}")
        adapter.notifyDataSetChanged()
        if (isVibrationEnabled()) {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(100)
        }
        setButtonsEnabled(true)
    }

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            val updatedTime = System.currentTimeMillis() - startTime + timeInMilliseconds
            timerTextView.text = formatTime(updatedTime)
            handler.postDelayed(this, 0)
        }
    }

    private fun formatTime(time: Long): String {
        val millis = (time % 1000) / 100
        val secs = (time / 1000).toInt()
        val mins = secs / 60
        val seconds = secs % 60
        val minutes = mins % 60
        return String.format(getString(R.string._02d_02d_01d), minutes, seconds, millis)
    }

    private fun showDistanceDialog(onDistanceSelected: (String) -> Unit) {
        setButtonsEnabled(false)
        uploadButton.isEnabled = false
        val distances = arrayOf("750m", "1250m", "2000m", "2500m", "Custom")
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_distance, null)
        val spinner: Spinner = view.findViewById(R.id.distance_spinner)
        val customDistanceEditText: EditText = view.findViewById(R.id.custom_distance_edit_text)
        customDistanceEditText.visibility = View.GONE
        spinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, distances)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                customDistanceEditText.visibility =
                    if (distances[position] == "Custom") View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                customDistanceEditText.visibility = View.GONE
            }
        }
        builder.setView(view)
            .setTitle("Select Race Distance")
            .setPositiveButton("OK") { dialog, _ ->
                val selectedDistance = if (spinner.selectedItem == "Custom") {
                    customDistanceEditText.text.toString()
                } else {
                    spinner.selectedItem.toString()
                }
                if (selectedDistance.isNotBlank()) {
                    onDistanceSelected(selectedDistance)
                }
                setButtonsEnabled(true)
                uploadButton.isEnabled = true
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                setButtonsEnabled(true)
                uploadButton.isEnabled = true
                dialog.cancel()
            }
        val dialog: AlertDialog = builder.create()

        dialog.setOnDismissListener {
            setButtonsEnabled(true)
            uploadButton.isEnabled = true
        }
        dialog.show()
    }

    private fun uploadResults() {
        showDistanceDialog { selectedDistance ->
            val date = java.text.SimpleDateFormat("ddMMM", java.util.Locale.getDefault())
                .format(java.util.Date())
            val baseFileName = "${selectedDistance}-${date}"
            val fileName = "$baseFileName.xlsx"
            val resolver = contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(
                    MediaStore.MediaColumns.MIME_TYPE,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    val downloadsDirectory =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = "xlsx".createUniqueFile(downloadsDirectory, baseFileName)
                    put(MediaStore.MediaColumns.DATA, file.absolutePath)
                } else {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
            }

            val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    val workbook = XSSFWorkbook()
                    val sheet = workbook.createSheet("Lap Times")
                    val createHelper = workbook.creationHelper
                    val cellStyle = workbook.createCellStyle()
                    cellStyle.dataFormat = createHelper.createDataFormat().getFormat("mm:ss.0")

                    for ((index, lap) in lapTimes.withIndex()) {
                        val row = sheet.createRow(index)
                        val lapData = lap.split("| ")
                        val cell1 = row.createCell(0)
                        cell1.setCellValue(lapData[0])
                        val cell2 = row.createCell(1)
                        cell2.cellStyle = cellStyle
                        cell2.setCellValue(convertTimeToExcelFormat(lapData[1]))
                    }
                    workbook.write(outputStream)
                    workbook.close()
                }


                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)
                    type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(Intent.createChooser(shareIntent, "Share File"))
            }
        }
    }

    private fun convertTimeToExcelFormat(timeString: String): Double {
        val parts = timeString.split(":")
        val minutes = parts[0].toDouble()
        val secondsMillis = parts[1].split(".")
        val seconds = secondsMillis[0].toDouble()
        val millis = secondsMillis[1].toDouble() / 10
        return (minutes * 60 + seconds + millis) / (24 * 60 * 60)
    }
}

private fun String.createUniqueFile(directory: File, baseFileName: String): File {
    var file = File(directory, "$baseFileName.${this}")
    var count = 1
    while (file.exists()) {
        file = File(directory, "$baseFileName($count).${this}")
        count++
    }
    return file
}
