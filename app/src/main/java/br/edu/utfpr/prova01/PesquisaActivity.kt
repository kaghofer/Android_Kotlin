package br.edu.utfpr.prova01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast

private lateinit var lvCombustiveis: ListView

class PesquisaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesquisa)

        lvCombustiveis = findViewById( R.id.lvCombustiveis )

        lvCombustiveis.setOnItemClickListener { adapterView, view, pos, l ->
            Toast.makeText( this, "Posicao: ${pos}", Toast.LENGTH_SHORT ).show()

            val cod = pos.inc().toString()

            Intent().apply {
                putExtra( "codRetorno", cod )
                setResult( RESULT_OK, this )
            }
            finish()
        }

    }


}