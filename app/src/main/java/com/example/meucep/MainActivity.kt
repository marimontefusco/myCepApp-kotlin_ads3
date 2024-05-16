package com.example.meucep

import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
import api.Api
import com.example.meucep.databinding.ActivityMainBinding
import model.Endereco
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

//import retrofit2.create

class MainActivity : AppCompatActivity() {

    // variável p viewBinding -> herda da activity MainBinding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Configurando o viewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#4CAF50")

        //Configurando o retrofit
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://viacep.com.br/ws/") // inserindo endereço da api ViaCep -> atenção: /ws/
            .build()
            .create(Api::class.java)

        // Configurando ação do btn -> Recuperar dados inseridos pelo usuário
        binding.btBuscarCEP.setOnClickListener {

            //variável q recebe dado do input -> editCEP
            val cep = binding.editCEP.text.toString()

            if (cep.isEmpty()) {
                Toast.makeText(this, "Preencha o campo CEP", Toast.LENGTH_SHORT).show()

            } else {
                retrofit.setEndereco(cep).enqueue(/* callback = */ object : Callback<Endereco> {  //.enqueue -> é um callback do model Endereco
                    override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {

                        // Verificação status da response
                        if (response.code() == 200){
                            val logradouro = response.body()?.logradouro.toString()
                            val bairro     = response.body()?.bairro.toString()
                            val localidade = response.body()?.localidade.toString()
                            val uf         = response.body()?.uf.toString()
                            setFormulario(logradouro, bairro, localidade, uf)

                        } else if (response.code() == 300){
                            Toast.makeText(applicationContext, "CEP inválido!", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(applicationContext, "CEP inexistente",Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Response em caso de falha
                    override fun onFailure(call: Call<Endereco>, t: Throwable) {
                        Toast.makeText(applicationContext, "Erro no servidor",Toast.LENGTH_SHORT).show()
                    }

                }
                )
            }

        }

    }

    // Método setFormulário -> chamado no if - linha 63
    private fun setFormulario(logradouro: String, bairro: String, localidade: String, uf:String) {
        binding.editLogradouro.setText(logradouro)
        binding.editBairro.setText(bairro)
        binding.editCidade.setText(localidade)
        binding.editEstado.setText(uf)
    }

}