package com.nttdata.purchasetransaction.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.purchasetransaction.entity.PurchaseTransaction;
@Repository
public interface PurchaseTransactionRepository extends ReactiveMongoRepository<PurchaseTransaction, Long> {

}
