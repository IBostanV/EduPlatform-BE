package com.play.quiz.cosmetic;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnedItemRepository extends JpaRepository<OwnedItem, OwnedItem.Key> {

    List<OwnedItem> findByAccountId(Long accountId);

    boolean existsByAccountIdAndItemCode(Long accountId, String itemCode);
}
