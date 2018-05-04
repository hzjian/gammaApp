package com.cellinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaFile;
import com.cellinfo.repository.TlGammaFileRepository;


@Service
public class SysRelateFileService {

	
	@Autowired
	private TlGammaFileRepository tlGammaFileRepository ;
	
	public List<TlGammaFile> findByRelateid(String rid)
	{
		return this.tlGammaFileRepository.findByRelateid(rid);
	}
	
	public TlGammaFile saveRelateFileEntity(TlGammaFile entity)
	{
		return this.tlGammaFileRepository.save(entity);
	}

	public String getFileNameById(String fileId) {
		// TODO Auto-generated method stub
		TlGammaFile tmp = this.tlGammaFileRepository.findOne(fileId);
		return tmp.getFname();
	}

	public void deleteFile(String fileid) {
		// TODO Auto-generated method stub
		this.tlGammaFileRepository.delete(fileid);
		
	}
	
}
