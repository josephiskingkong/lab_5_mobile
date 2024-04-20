package com.example.drawapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import yuku.ambilwarna.AmbilWarnaDialog

class MainActivity : Activity() {
    private lateinit var drawingView: DrawingView
    private lateinit var brushSizeSlider: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawing_view)
        brushSizeSlider = findViewById(R.id.brush_size_slider)

        findViewById<Button>(R.id.btn_color_picker).setOnClickListener {
            openColorPicker()
        }
        findViewById<Button>(R.id.btn_eraser).setOnClickListener {
            drawingView.useEraser()
        }
        findViewById<Button>(R.id.btn_add_photo).setOnClickListener {
            addPhoto()
        }

        brushSizeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                drawingView.changeBrushSize(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun openColorPicker() {
        val colorPicker = AmbilWarnaDialog(this, drawingView.getCurrentColor(), object : AmbilWarnaDialog.OnAmbilWarnaListener {
            override fun onCancel(dialog: AmbilWarnaDialog) {}

            override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                drawingView.changeColor(color)
            }
        })
        colorPicker.show()
    }

    private fun addPhoto() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                contentResolver.openInputStream(uri).use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    drawingView.setBackgroundImage(bitmap)
                }
            }
        }
    }

    companion object {
        private const val PICK_IMAGE = 1
    }
}
