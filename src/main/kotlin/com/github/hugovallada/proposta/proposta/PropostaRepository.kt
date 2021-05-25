package com.github.hugovallada.proposta.proposta

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface PropostaRepository : JpaRepository<Proposta, UUID> {
}