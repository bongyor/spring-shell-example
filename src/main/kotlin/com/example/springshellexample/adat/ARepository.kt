package com.example.springshellexample.adat

import org.springframework.data.jpa.repository.JpaRepository

interface ARepository : JpaRepository<AEntity, Int>
