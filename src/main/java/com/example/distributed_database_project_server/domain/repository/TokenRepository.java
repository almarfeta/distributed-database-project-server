package com.example.distributed_database_project_server.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.distributed_database_project_server.domain.entity.TokenEntity;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, UUID> {

    @Query(value = """
              select t from token t inner join account u on t.account.id = u.id
              where u.id = :userId and (t.expired = false or t.revoked = false)
            """)
    List<TokenEntity> findAllValidTokenByUser(@Param("userId") UUID userId);

    Optional<TokenEntity> findByToken(String tokenEntity);

    void deleteAllByAccountId(UUID accountId);
}
