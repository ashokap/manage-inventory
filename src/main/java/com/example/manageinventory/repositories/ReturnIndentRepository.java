package com.example.manageinventory.repositories;

import com.example.manageinventory.models.Indent;
import com.example.manageinventory.models.ReturnIndent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ReturnIndentRepository extends JpaRepository<ReturnIndent, Integer> {
    ReturnIndent findReturnIndentById(int id);

    @Query("SELECT ri FROM return_indent ri WHERE ri.indent.id = ?1")
    Set<ReturnIndent> getAllReturnIndentsForIndent(int id);

    //@Query("SELECT p from product p WHERE p.quantity <= ?1")
}
