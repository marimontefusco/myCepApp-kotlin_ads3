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

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#4CAF50")

        //Configuracoes do retrofit

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://viacep.com.br/ws/")
            .build()
            .create(Api::class.java)

        binding.btBuscarCEP.setOnClickListener{
            val cep = binding.editCEP.text.toString()
            if (cep.isEmpty()){
                Toast.makeText(this, "Preencha o campo CEP",Toast.LENGTH_SHORT).show()
            }else{

                retrofit.setEndereco(cep).enqueue(/* callback = */ object : Callback<Endereco>{
                    override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                        if (response.code() == 200){
                            val logradouro = response.body()?.logradouro.toString()
                            val bairro     = response.body()?.bairro.toString()
                            val localidade = response.body()?.localidade.toString()
                            val uf         = response.body()?.uf.toString()
                            setFormulario(logradouro,bairro,localidade,uf)
                        }
                        else{

                            Toast.makeText(applicationContext, "CEP Errado",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Endereco>, t: Throwable) {
                        Toast.makeText(applicationContext, "Erro no servidor",Toast.LENGTH_SHORT).show()
                    }

                }
                )
            }

        }
    }

        private fun setFormulario(logradouro: String,bairro: String,localidade: String,uf:String){

            binding.editLogradouro.setText(logradouro)
            binding.editBairro.setText(bairro)
            binding.editCidade.setText(localidade)
            binding.editEstado.setText(uf)

        }


}