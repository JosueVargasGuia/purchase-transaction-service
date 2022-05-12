package com.nttdata.purchasetransaction.service;

 
import com.nttdata.purchasetransaction.entity.PurchaseTransaction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PurchaseTransactionService {
	Flux<PurchaseTransaction> findAll();

	Mono<PurchaseTransaction> findById(Long idPurchaseTransaction);

	Mono<PurchaseTransaction> save(PurchaseTransaction purchaseTransaction);

	Mono<PurchaseTransaction> update(PurchaseTransaction purchaseTransaction);

	Mono<Void> delete(Long idPurchaseTransaction);
}
