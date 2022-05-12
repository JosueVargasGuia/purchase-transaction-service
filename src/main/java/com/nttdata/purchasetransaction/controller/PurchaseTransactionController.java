package com.nttdata.purchasetransaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.purchasetransaction.entity.PurchaseTransaction;
import com.nttdata.purchasetransaction.service.PurchaseTransactionService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/api/v1/purchasetransaction")
public class PurchaseTransactionController {
	@Autowired
	PurchaseTransactionService transactionService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<PurchaseTransaction> findAll() {
		return transactionService.findAll();

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ResponseEntity<PurchaseTransaction>> save(@RequestBody PurchaseTransaction purchaseTransaction) {
		return transactionService.save(purchaseTransaction)
				.map(_purchaseTransaction -> ResponseEntity.ok().body(_purchaseTransaction)).onErrorResume(e -> {
					log.info("Error:" + e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				});
	}

	@GetMapping("/{idPurchaseTransaction}")
	public Mono<ResponseEntity<PurchaseTransaction>> findById(
			@PathVariable(name = "idPurchaseTransaction") Long idPurchaseTransaction) {
		return transactionService.findById(idPurchaseTransaction)
				.map(purchaseTransaction -> ResponseEntity.ok().body(purchaseTransaction)).onErrorResume(e -> {
					log.info("Error:" + e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@PutMapping
	public Mono<ResponseEntity<PurchaseTransaction>> update(@RequestBody PurchaseTransaction purchaseTransaction) {
		Mono<PurchaseTransaction> mono = transactionService.findById(purchaseTransaction.getIdPurchaseTransaction())
				.flatMap(objPurchaseTransaction -> {
					return transactionService.update(purchaseTransaction);
				});
		return mono.map(_purchaseTransaction -> {
			return ResponseEntity.ok().body(_purchaseTransaction);
		}).onErrorResume(e -> {
			log.info("Error:" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@DeleteMapping("/{idPurchaseTransaction}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable(name = "idPurchaseTransaction") Long idPurchaseTransaction) {
		Mono<PurchaseTransaction> _purchaseTransaction = transactionService.findById(idPurchaseTransaction);
		_purchaseTransaction.subscribe();
		PurchaseTransaction purchaseTransaction = _purchaseTransaction.toFuture().join();
		if (purchaseTransaction != null) {
			return transactionService.delete(idPurchaseTransaction).map(r -> ResponseEntity.ok().<Void>build());
		} else {
			return Mono.just(ResponseEntity.noContent().build());
		}

	}
}
