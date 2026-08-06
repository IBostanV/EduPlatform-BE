package com.play.quiz.repository;

import com.play.quiz.domain.GlossaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GlossaryTypeRepository extends JpaRepository<GlossaryType, Long> {

    // Glossaries still using the type: Q_GLOSSARY.TYPE_ID blocks deleting it.
    @Query("SELECT COUNT(g) FROM Glossary g WHERE g.type.id = :typeId")
    long countGlossariesUsing(Long typeId);
}
