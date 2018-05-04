package com.cellinfo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaUser;

public interface TlGammaUserRepository extends PagingAndSortingRepository<TlGammaUser, String> {

    public List<TlGammaUser> findByUserCnname(String  cnname);

	public Optional<TlGammaUser> findByUserName(String username);

	//@Query("select u from TlGammaUser u where u.roleId <> 'ROLE_ADMIN'")
	public Page<TlGammaUser> findByGroupGuid(String groupGuid ,Pageable pageable);

	public List<TlGammaUser> findByGroupGuid(String groupGuid);

}
