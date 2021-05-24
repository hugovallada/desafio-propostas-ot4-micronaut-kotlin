package com.github.hugovallada.proposta

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("com.github.hugovallada.proposta")
		.start()
}

