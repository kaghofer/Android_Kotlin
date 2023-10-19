package br.edu.utfpr.prova01

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


private lateinit var etCod : TextView
private lateinit var etQtd : TextView
private lateinit var etCidade : TextView
private lateinit var banco: SQLiteDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCod = findViewById( R.id.etCodigo )
        etQtd = findViewById( R.id.etQtdLitros )
        etCidade = findViewById( R.id.etCidade )

        banco = SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath("dbfile.sqlite"), null)
        //banco.execSQL("drop table posto")
        banco.execSQL("CREATE TABLE IF NOT EXISTS posto(_id INTEGER PRIMARY KEY AUTOINCREMENT,combustivel int, cidade TEXT, litros Double)")
    }

    fun btIncluirRegistro(view: View)
    {
        val combustivelDesejado: Double
        val cidade: String
        val valorAtual: Double

        // Validação dos campos
        try {
            combustivelDesejado = etCod.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            etCod.error = "Digite um código válido"
            return
        }

        cidade = etCidade.text.toString()
        if(cidade.isEmpty()){
            etCidade.error = "Digite um valor válido"
        }

        val valorAtualText = etQtd.text.toString()
        if (valorAtualText.isEmpty()) {
            etQtd.error = "Digite um valor válido"
            return
        }

        valorAtual = valorAtualText.toDouble()

        // Consulta SQL para calcular a soma dos litros
        val sql = "SELECT SUM(litros) AS total_litros FROM posto WHERE combustivel = '$combustivelDesejado'"

        val cursor = banco.rawQuery(sql, null)

        val totalLitros: Double = if (cursor.moveToFirst()) {
            cursor.getDouble(0)
        } else {
            0.0
        }
        cursor.close()

        val total = valorAtual + totalLitros

        // Inserir os dados na tabela "posto"
        val insertSQL = "INSERT INTO posto (combustivel, cidade, litros) VALUES ('$combustivelDesejado', '$cidade', $total)"
        banco.execSQL(insertSQL)
        limparCampos()
    }

    fun limparCampos() {
        etCod.setText("")
        etQtd.setText("")
        etCidade.setText("")
    }

    fun btSomatoria(view: View) {
        var combustivelDesejado: Double

        try {
            combustivelDesejado = etCod.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            // Se a conversão para Int falhar, exiba um Toast de erro
            Toast.makeText(this, "Digite um código válido.", Toast.LENGTH_SHORT).show()
            return
        }

        val sql = "SELECT SUM(litros) AS total_litros FROM posto WHERE combustivel = '$combustivelDesejado'"

        val cursor = banco.rawQuery(sql, null)

        val totalLitros: Double = if (cursor.moveToFirst()) {
            cursor.getDouble(0)
        } else {
            0.0
        }
        cursor.close()

        // Exibir um Toast com a soma dos litros
        val mensagem = "Soma dos litros: $totalLitros"
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }

    fun btListarLancamentoOnClick(view: View) {
        val intent = Intent( this, PesquisaActivity::class.java ).let {
            register.launch( it )
        }
    }

    val register = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult() ){
            result : ActivityResult ->

        if ( result.resultCode == RESULT_OK ) {
            result.data?.let {
                if ( it.hasExtra( "codRetorno" ) ) {
                    val codRetorno = it.getStringExtra( "codRetorno" )
                    etCod.setText( codRetorno )
                }
            }
        }
    }

}