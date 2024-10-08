import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL
import java.io.IOException

data class Endereco(
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val complemento: String,
    val localidade: String,
    val uf: String,
) {
    override fun toString(): String {
        return """
            CEP: $cep
            Logradouro: $logradouro
            Bairro: $bairro
            Cidade: $localidade
            Estado: $uf
        """
    }
}

fun buscarEndereco(cep: String): Endereco? {
    val url = URL("https://viacep.com.br/ws/$cep/json/")

    return try {
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream.bufferedReader().use {
                    val response = it.readText()
                    return Gson().fromJson(response, Endereco::class.java)
                }
            } else {
                println("Erro na requisição: Código de resposta $responseCode")
                null
            }
        }
    } catch (e: IOException) {
        println("Erro de conexão: ${e.message}")
        null
    }
}

fun main() {
    println("Digite o CEP para consulta:")
    val cep = readlnOrNull() ?: ""

    if (cep.isNotEmpty()) {
        val endereco = buscarEndereco(cep)

        if (endereco != null) {
            println(endereco)
        } else {
            println("CEP não encontrado ou erro na consulta.")
        }
    } else {
        println("CEP inválido.")
    }
}
