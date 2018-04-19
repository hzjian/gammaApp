package com.cellinfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaLog;
import com.cellinfo.repository.TlGammaLogRepository;


@Service
public class SysLogService {

	@Autowired
    private TlGammaLogRepository tlGammaLogRepository;
	
	public TlGammaLog save(TlGammaLog sysLog) {
		return this.tlGammaLogRepository.save(sysLog);
	}
}
