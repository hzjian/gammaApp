package com.cellinfo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.enums.ResultEnum;
import com.cellinfo.exception.NetException;
import com.cellinfo.repository.TlGammaUserRepository;


@Service
public class SysUserService implements UserDetailsService {

	private static final String ROLE_USER = "ROLE_USER";
	
    @Autowired
    private TlGammaUserRepository tlGammaUserRepository;

    
    
    //@Transactional
   

    public void getEnabled(String userId) throws Exception{
    	TlGammaUser user = tlGammaUserRepository.findOne(userId);
        boolean isEnable = user.isEnabled();
        if (isEnable) {
            throw new NetException(ResultEnum.INVALIDUSER);
        }else  {
            throw new NetException(ResultEnum.VALIDUSER);
        }
    }

    /**
     * 通过Id查询
     * @param id
     * @return
     */
    public TlGammaUser findOne(String userId) {
        return tlGammaUserRepository.findOne(userId);
    }
    
    public Page<TlGammaUser> getAll(PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.tlGammaUserRepository.findAll(pageInfo);
	}

	public List<TlGammaUser> findByUserCnname(String cnname) {
		// TODO Auto-generated method stub
		return this.tlGammaUserRepository.findByUserCnname(cnname);
	}

	public void delete(String id) {
		this.tlGammaUserRepository.delete(id);
		
	}

	public TlGammaUser save(TlGammaUser user) {
		// TODO Auto-generated method stub
		return this.tlGammaUserRepository.save(user);
	}


	public TlGammaUser loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<TlGammaUser> user = tlGammaUserRepository.findByUserName(username);

        final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
        user.ifPresent(detailsChecker::check);
        return user.orElseThrow(() -> new UsernameNotFoundException("user not found."));
    }

	public void delete(TlGammaUser user) {
		// TODO Auto-generated method stub
		this.tlGammaUserRepository.delete(user);
		
	}

}