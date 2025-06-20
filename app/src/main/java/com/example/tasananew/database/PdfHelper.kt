package com.example.tasananew.database

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.IOException

fun generatePdfLaporan(
    context: Context,
    namaProject: String,
    model: String,
    deskripsi: String,
    os: String,
    cpu: String,
    ram: String,
    database: String,
    rencana: String,
    kategori: String,
    status: String,
    mulai: String,
    selesai: String,
    stakeholder: String,
    deskripsiTransaksi: String,
    namaFile: String,
    modelType: String,
    algorithm: String,
    hyperparam: String
) {
    val document = PdfDocument()
    val paint = Paint()
    val pageWidth = 595
    val pageHeight = 842
    val margin = 40
    var y = 50

    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    var page = document.startPage(pageInfo)
    var canvas = page.canvas

    fun checkEndOfPage(extraHeight: Int) {
        if (y + extraHeight > pageHeight - margin) {
            document.finishPage(page)
            val newPageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, document.pages.size + 1).create()
            page = document.startPage(newPageInfo)
            canvas = page.canvas
            y = margin
        }
    }

    fun drawCenteredTitle(title: String) {
        paint.textSize = 18f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val textWidth = paint.measureText(title)
        canvas.drawText(title, (pageWidth - textWidth) / 2, y.toFloat(), paint)
        y += 30
    }

    fun drawSectionTitle(title: String) {
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawLine(margin.toFloat(), y.toFloat(), (pageWidth - margin).toFloat(), y.toFloat(), paint)
        y += 20
        canvas.drawText(title, margin.toFloat(), y.toFloat(), paint)
        y += 20
    }

    fun drawTextLine(label: String, value: String) {
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("$label: $value", margin.toFloat(), y.toFloat(), paint)
        y += 18
    }

    fun drawTable(headers: List<String>, values: List<String>) {
        val rowHeight = 25
        val colWidth = (pageWidth - 2 * margin) / headers.size

        checkEndOfPage(rowHeight * 2 + 10)

        paint.style = Paint.Style.STROKE
        for (i in headers.indices) {
            val left = margin + i * colWidth
            canvas.drawRect(left.toFloat(), y.toFloat(), (left + colWidth).toFloat(), (y + rowHeight).toFloat(), paint)
        }

        paint.style = Paint.Style.FILL
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        for (i in headers.indices) {
            val left = margin + i * colWidth
            canvas.drawText(headers[i], (left + 5).toFloat(), (y + 17).toFloat(), paint)
        }

        y += rowHeight
        paint.style = Paint.Style.STROKE
        for (i in values.indices) {
            val left = margin + i * colWidth
            canvas.drawRect(left.toFloat(), y.toFloat(), (left + colWidth).toFloat(), (y + rowHeight).toFloat(), paint)
        }

        paint.style = Paint.Style.FILL
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        for (i in values.indices) {
            val left = margin + i * colWidth
            canvas.drawText(values[i], (left + 5).toFloat(), (y + 17).toFloat(), paint)
        }

        y += rowHeight + 10
    }

    drawCenteredTitle("Laporan Implementasi Sistem")

    drawSectionTitle("Detail Proyek")
    drawTextLine("Nama Proyek", namaProject)
    drawTextLine("Model", model)
    drawTextLine("Deskripsi", deskripsi)
    y += 10

    drawSectionTitle("Data Lingkungan")
    drawTable(listOf("OS", "CPU", "RAM", "Database"), listOf(os, cpu, ram, database))

    drawSectionTitle("Catatan Pemeliharaan")
    drawTable(
        listOf("Rencana Pekerjaan", "Kategori", "Status", "StartDate", "EndDate", "Stakeholder"),
        listOf(rencana, kategori, status, mulai, selesai, stakeholder)
    )

    drawSectionTitle("Data Transaksi")
    drawTable(listOf("Deskripsi", "Nama File"), listOf(deskripsiTransaksi, namaFile.substringAfterLast("/")))

    drawSectionTitle("Kinerja Model")
    drawTable(listOf("Model Performance", "Evaluation Metrics", "Hyperparameters"), listOf("-", "-", "-"))

    drawSectionTitle("Aktivitas Implementasi")
    drawTable(listOf("Model Type", "Algorithm Used", "Hyperparameters"), listOf(modelType, algorithm, hyperparam))

    document.finishPage(page)

    // Simpan PDF ke MediaStore (Downloads)
    val fileName = "Laporan_${namaProject.replace(" ", "_")}.pdf"
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    try {
        val uri = resolver.insert(MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), contentValues)
        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                document.writeTo(outputStream)
                Toast.makeText(context, "PDF berhasil disimpan di folder Downloads", Toast.LENGTH_LONG).show()
            }
        }
    } catch (e: IOException) {
        Toast.makeText(context, "Gagal menyimpan PDF: ${e.message}", Toast.LENGTH_SHORT).show()
    }

    document.close()
}
