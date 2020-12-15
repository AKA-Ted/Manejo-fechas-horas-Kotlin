package com.example.fechas

import android.os.Build
import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import java.lang.Exception
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class ScrollingActivity : AppCompatActivity() {
    private val LOCALE = Locale("es","MX")
    private val fmtFecha = SimpleDateFormat("dd/MM/yyyy", LOCALE)
    private val fmtHora = SimpleDateFormat("HH:mm", LOCALE)
    private val fmtFechaYHora = SimpleDateFormat("dd/MM/yyyy HH:mm", LOCALE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(findViewById(R.id.toolbar))
        try {
            datePicker.minDate = fmtFecha.parse("3/12/2014").getTime()
            datePicker.maxDate = fmtFecha.parse("3/12/2020").getTime()
        }catch (e: Exception){
            Logger.getLogger(javaClass.name).log(Level.WARNING, "Limite de fecha incorrecto")
        }
        fab.setOnClickListener {procesar()}
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.action_procesar){
            procesar()
            return true
        }
        if(id == R.id.action_limpiar){
            limpiar()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun procesar(){
        val ahora = Date()
        lee(txtHora, salidaHora, fmtHora, "Hora Incorrecta")
        val fechaPasado = lee(txtFechaPasado, salidaFechaPasado, fmtFecha,"Fecha Incorrecta")
        if(fechaPasado != null && fechaPasado.compareTo(ahora) >= 0){
            txtFechaPasado.setError("Debe estar en el pasado")
        }
        val fechaHoraFuturo =  lee(txtFechaHoraFuturo, salidaFechaHoraFuturo, fmtFechaYHora, "Fecha y Hora Incorrecta")
        if(fechaHoraFuturo != null && fechaHoraFuturo.compareTo(ahora) <= 0){
            txtFechaHoraFuturo.setError("Debe estar en el futuro")
        }

        val tPicker = Calendar.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tPicker.set(Calendar.HOUR, timePicker.hour)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tPicker.set(Calendar.MINUTE, timePicker.minute)
        }
        salidaTimePicker.text = fmtHora.format(tPicker.time)

        val dPicker = Calendar.getInstance()
        val dia = datePicker.dayOfMonth
        val mes = datePicker.month
        val año = datePicker.year
        dPicker.set(Calendar.DAY_OF_MONTH, dia)
        dPicker.set(Calendar.MONTH, mes)
        dPicker.set(Calendar.YEAR, año)
        salidaDatePicker.text = fmtFecha.format(dPicker.time)

        val analClock = Calendar.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            analClock.set(Calendar.HOUR, timePicker.hour)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            analClock.set(Calendar.MINUTE, timePicker.minute)
        }
        salidaAnalogClock.text = fmtHora.format(analClock.time)
    }

    private fun limpiar(){
        salidaHora.text = ""
        salidaFechaPasado.text = ""
        salidaFechaHoraFuturo.text = ""
        salidaTimePicker.text = ""
        salidaDatePicker.text = ""
        salidaAnalogClock.text = ""
    }


    private fun lee(editText: EditText, salida: TextView, fmt: SimpleDateFormat, mensajeDeError: String?):Date? {
        return try {
            val fecha = leeDate(editText, fmt, mensajeDeError)
            salida.text = fmt.format(fecha)
            fecha
        }catch (e:ParseException){
            salida.text = ""
            null
        }
    }

    private fun leeDate(editText: EditText, fmt: DateFormat, mensajeError:String?): Date {
        return try {
            val valor = editText.text.toString().trim()
            fmt.parse(valor)
        }catch (e: ParseException){
            editText.error = mensajeError
            throw e
        }
    }
}