package edu.quinnipiac.ser210.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {
    lateinit var stopwatch: Chronometer //The stopwatch
    var running = false  //Is the stopwatch running?
    var offset: Long = 0 //The base offset for the stopwatch

    val OFFSET_KEY = "offset!"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base!"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stopwatch = findViewById<Chronometer>(R.id.stopwatch)

        //Restore the previous state
        if (savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else setBaseTime()
        }

        //The start button starts the stopwatch if it's not running
        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            if (!running) {
                setBaseTime() // make sude the stopwatch starts from the correct time then start it
                stopwatch.start()
                running = true
            }
        }

        //The pause button pauses the stopwatch if it's running
        val pauseButton = findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener {
            if (running) {
                saveOffset() // save the time on the stopwatch then stop it running
                stopwatch.stop()
                running = false
            }
        }
        //The reset button sets the offset and stopwatch to 0
        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            offset = 0
            setBaseTime() // sets the stop watch back to 0
        }
    }

    override fun onSaveInstanceState(savedInstancestate: Bundle) {
        savedInstancestate.putLong(OFFSET_KEY, offset)
        savedInstancestate.putBoolean(RUNNING_KEY, running)
        savedInstancestate.putLong(BASE_KEY, stopwatch.base)
        super.onSaveInstanceState(savedInstancestate)
    }

    override fun onPause() {
        super.onPause()
        if (running) {
            saveOffset()
            stopwatch.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (running) {
            setBaseTime()
            stopwatch.start()
            offset = 0
        }
    }

    //Record the offset
    fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }

    //Update the stopwatch.base time, allowing for any offset
    fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime()- offset
    }
}