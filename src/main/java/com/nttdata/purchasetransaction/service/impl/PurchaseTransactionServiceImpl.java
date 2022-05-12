package com.nttdata.purchasetransaction.service.impl;

import java.util.Calendar;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.purchasetransaction.entity.PurchaseTransaction;
import com.nttdata.purchasetransaction.repository.PurchaseTransactionRepository;
import com.nttdata.purchasetransaction.service.PurchaseTransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PurchaseTransactionServiceImpl implements PurchaseTransactionService {
	@Autowired
	PurchaseTransactionRepository transactionRepository;

	@Override
	public Flux<PurchaseTransaction> findAll() {
		return transactionRepository.findAll()
				.sort((objA, objB) -> objA.getIdPurchaseTransaction().compareTo(objB.getIdPurchaseTransaction()));
	}

	@Override
	public Mono<PurchaseTransaction> findById(Long idPurchaseTransaction) {
		return transactionRepository.findById(idPurchaseTransaction);

	}

	@Override
	public Mono<PurchaseTransaction> save(PurchaseTransaction purchaseTransaction) {
		Long count = this.findAll().collect(Collectors.counting()).blockOptional().get();
		Long idPurchaseTransaction;
		if (count != null) {
			if (count <= 0) {
				idPurchaseTransaction = Long.valueOf(0);
			} else {
				idPurchaseTransaction = this.findAll()
						.collect(Collectors.maxBy(Comparator.comparing(PurchaseTransaction::getIdPurchaseTransaction)))
						.blockOptional().get().get().getIdPurchaseTransaction();
			}
		} else {
			idPurchaseTransaction = Long.valueOf(0);

		}
		purchaseTransaction.setIdPurchaseTransaction(idPurchaseTransaction + 1);
		purchaseTransaction.setCreationDate(Calendar.getInstance().getTime());
		return transactionRepository.insert(purchaseTransaction);
	}

	@Override
	public Mono<PurchaseTransaction> update(PurchaseTransaction purchaseTransaction) {
		return transactionRepository.save(purchaseTransaction);
	}

	@Override
	public Mono<Void> delete(Long idPurchaseTransaction) {

		return transactionRepository.deleteById(idPurchaseTransaction);
	}
}
