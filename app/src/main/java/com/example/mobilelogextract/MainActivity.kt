package com.example.mobilelogextract

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewLogs)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LogAdapter(emptyList())
        recyclerView.adapter = adapter

        val button = findViewById<Button>(R.id.btnCaptureLogs)
        button.setOnClickListener {
            fetchAndDisplayLogs()
        }
    }

    private fun fetchAndDisplayLogs() {
        CoroutineScope(Dispatchers.Main).launch {
            val logs = readLogs()
            adapter = LogAdapter(logs)
            recyclerView.adapter = adapter
        }
    }

    private suspend fun readLogs(): List<LogEntry> = withContext(Dispatchers.IO) {
        val logEntries = mutableListOf<LogEntry>()
        try {
            val process = Runtime.getRuntime().exec("logcat -d")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                line?.let {
                    when {
                        it.contains("ERROR") -> logEntries.add(LogEntry("Error", it))
                        it.contains("WARN") -> logEntries.add(LogEntry("Warning", it))
                        else -> logEntries.add(LogEntry("Info", it))
                    }
                } ?: logEntries.add(LogEntry("Error", "Null log entry"))
            }
        } catch (e: Exception) {
            logEntries.add(LogEntry("Error", "Error reading logs: ${e.message}"))
        }
        logEntries
    }

}
