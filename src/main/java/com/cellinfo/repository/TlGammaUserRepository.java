package com.cellinfo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaUser;

public interface TlGammaUserRepository extends PagingAndSortingRepository<TlGammaUser, String> {

    public List<TlGammaUser> findByUserCnname(String  cnname);

	public Optional<TlGammaUser> findByUserName(String username);


}
