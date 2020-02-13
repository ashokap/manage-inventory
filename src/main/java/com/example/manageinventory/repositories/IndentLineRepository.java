package com.example.manageinventory.repositories;

import com.example.manageinventory.models.Indent;
import com.example.manageinventory.models.IndentLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface IndentLineRepository extends JpaRepository<IndentLine, Integer> {
    IndentLine findIndentLineById(int id);

    //Get list of all indent lines for a given indent
    @Query("SELECT il from indent_line il where il.indent.id = ?1")
    Set<IndentLine> getAllIndentLinesForIndent(int indentId);

    //Get list of all indent lines for a given return indent
    @Query("SELECT il from indent_line il where il.returnIndent.id = ?1")
    Set<IndentLine> getAllIndentLinesForReturnIndent(int returnIndent_id);

}
