package com.github.hugovallada.proposta.proposta

import com.github.hugovallada.proposta.proposta.endereco.StatusProposta
import java.math.BigDecimal
import java.util.*

class PropostaResponse(
    val nome: String,
    val id: UUID?,
    val email: String,
    val salario: BigDecimal,
    val situacao: StatusProposta?,
    //TODO: Criar um cartão response
    val cartaoId: String?
) {
    //TODO: O segundo construtor é necessário para que o JAckson consiga desserializar apenas o necessário
    constructor(proposta: Proposta) : this(
        proposta.nome,
        proposta.id,
        proposta.email,
        proposta.salario,
        proposta.situacao,
        proposta.cartao?.id
    )
}

