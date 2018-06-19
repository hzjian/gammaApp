package com.cellinfo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaFile;
import com.cellinfo.repository.TlGammaFileRepository;


@Service
public class SysRelateFileService {

	
	@Autowired
	private TlGammaFileRepository tlGammaFileRepository ;
	
	public List<TlGammaFile> findByRelateid(String rid)
	{
		return this.tlGammaFileRepository.findByRelateId(rid);
	}
	
	public TlGammaFile saveRelateFileEntity(TlGammaFile entity)
	{
		return this.tlGammaFileRepository.save(entity);
	}

	public String getFileNameById(String fileId) {
		// TODO Auto-generated method stub
		TlGammaFile tmp = this.tlGammaFileRepository.findById(fileId).get();
		return tmp.getFileName();
	}

	public void deleteFile(String fileid) {
		// TODO Auto-generated method stub
		this.tlGammaFileRepository.deleteById(fileid);
		
	}

	public Page<TlGammaFile> findByRelateId(String skey, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.tlGammaFileRepository.findByRelateId(skey,pageable);
	}

	public Optional<TlGammaFile> getFileById(String fileId) {
		// TODO Auto-generated method stub
		return this.tlGammaFileRepository.findById(fileId);
	}
	
}
