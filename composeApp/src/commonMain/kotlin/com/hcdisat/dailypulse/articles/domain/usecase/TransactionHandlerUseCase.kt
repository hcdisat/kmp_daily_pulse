package com.hcdisat.dailypulse.articles.domain.usecase

import com.hcdisat.dailypulse.articles.domain.ArticleRepository
import com.hcdisat.dailypulse.articles.domain.DatabaseTransactionResult
import com.hcdisat.dailypulse.articles.domain.UseCaseResult
import com.hcdisat.dailypulse.core.dataaccess.network.logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionHandlerUseCase(private val repository: ArticleRepository) {
    operator fun invoke(): Flow<UseCaseResult<Unit>> = repository.transactionResult.map { transactionResult ->
        when (transactionResult) {
            is DatabaseTransactionResult.Commited -> {
                logger().d("Transaction commited")
                UseCaseResult.Success(Unit)
            }
            is DatabaseTransactionResult.RolledBack -> {
                logger().e("Transaction rolled back")
                UseCaseResult.Error("Transaction rolled back", Exception())
            }
        }
    }
}