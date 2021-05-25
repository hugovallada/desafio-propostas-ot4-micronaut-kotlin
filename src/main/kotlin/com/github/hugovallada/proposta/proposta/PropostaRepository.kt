package com.github.hugovallada.proposta.proposta

import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface PropostaRepository : JpaRepository<Proposta, UUID> {

    fun existsByDocumento(documento: String) : Boolean

    @Query("Select * from proposta where situacao = 'ELEGIVEL' and cartao_id is null", nativeQuery = true)
    fun buscarPropostasElegiveisSemCartao(): List<Proposta>

}