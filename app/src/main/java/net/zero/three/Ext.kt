package net.zero.three

import android.R.attr
import android.content.Context
import android.location.Location
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.R.attr.label

import android.content.ClipData
import android.content.ClipboardManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService


fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun View.showKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun readFromAssets(context: Context, filename: String): String {
    val reader = BufferedReader(InputStreamReader(context.assets.open(filename)))
    val sb = StringBuilder()
    var mLine = reader.readLine()
    while (mLine != null) {
        sb.append(mLine)
        mLine = reader.readLine()
    }
    reader.close()
    return sb.toString()
}

fun String.isNumeric(): Boolean {
    return this.matches("-?\\d+(\\.\\d+)?".toRegex())
}

fun String.isEmail(): Boolean {
    return this.matches(("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").toRegex())
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Double.toCurrency(code: String): String {
    var run = ""
    if (this < 0.0) {
        val harga = this.toString().replace("-", "").toDouble()
        run = StringBuilder("-" + code + "" + String.format("%,.0f", harga)).toString()
            .replace(",", ".")
    } else {
        run = StringBuilder(code + "" + String.format("%,.0f", this)).toString().replace(",", ".")
    }

    return run
}

fun Double.distanceInKm(): Double {
//    val distanceInKm = this * 0.001
    val number3digits: Double = Math.round(this * 1000.0) / 1000.0
    val number2digits: Double = Math.round(number3digits * 100.0) / 100.0
    val solution = Math.round(number2digits * 10.0) / 10.0

    return solution
}

fun getDataDistance(lat1: String, long1: String, lat2: String, long2: String): Double {
    var solution: Double = 0.0

    val loc1 = Location("")
    loc1.latitude = lat1.toDouble()
    loc1.longitude = long1.toDouble()

    val loc2 = Location("")
    loc2.latitude = lat2.toDouble()
    loc2.longitude = long2.toDouble()

    val distanceInMeters = loc1.distanceTo(loc2)

    val distanceInKm = distanceInMeters * 0.001
    val number3digits: Double = Math.round(distanceInKm * 1000.0) / 1000.0
    val number2digits: Double = Math.round(number3digits * 100.0) / 100.0
    solution = Math.round(number2digits * 10.0) / 10.0

    return solution
}

fun String.convertDate(
    startFormat: String,
    endFormat: String
): String {
    val inputFormat = SimpleDateFormat(startFormat)
    val outputFormat = SimpleDateFormat(endFormat)
    val parsed: Date?
    var outputText = ""

    try {
        parsed = inputFormat.parse(this)
        outputText = outputFormat.format(parsed!!)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return outputText
}

fun copyClipBoard(context: Context, text: String) {
    val clipboard: ClipboardManager? =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText("Copied text", text)
    clipboard?.setPrimaryClip(clip)
    Toast.makeText(context, "Copied text", Toast.LENGTH_SHORT).show()
}

enum class Status(val id: Int) {
    REGISTER(0),
    ACTIVE(1),
    CANCEL(2)
}

enum class Withdrawal(val id: Int) {
    REQUEST(0),
    ACCEPTED(1),
    REJECTED(2)
}

enum class Payment(val id: Int) {
    REQUEST(0),
    PAID(3),
    FAILED(2)
}

enum class Progress(val id: Int) {
    ORDER(0),
    APPROVE(1),
    PROGRESS(2),
    FINISH(3),
    CANCEL(4)
}

enum class PaymentMethod() {
    NANTI,
    BRIVA,
    BCAVA
}