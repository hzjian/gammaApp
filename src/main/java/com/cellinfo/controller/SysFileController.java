package com.cellinfo.controller;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.controller.entity.FileParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaFile;
import com.cellinfo.service.SysRelateFileService;
import com.cellinfo.utils.ResultUtil;
import com.cellinfo.utils.ReturnDesc;
/**
 * 1.任务相关联的文档管理
 * 2.
 * @author zhangjian
 */ 
@ServiceLog(moduleName = "文档管理模块")
@RestController
@RequestMapping("/service/file")
public class SysFileController {

	private String fileLocation = "d:/files";
	
	@Autowired
	private SysRelateFileService sysRelateFileService;
 
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
	
	private ConcurrentHashMap<String, File> fileMap = new ConcurrentHashMap<String, File>();

	public void dirExists(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }
	
	@PostConstruct
	public void init() {
		
		File dir = new File(fileLocation);
		this.dirExists(dir);
		fileMap.clear();
		fileMap.putAll(Arrays.asList(dir.listFiles()).stream()
				.collect(Collectors.toMap((f) -> {
					String name = ((File) f).getName();
					return name;
				}, (f) -> (File) f)));
	}

	
	
	@PostMapping( value = "/download")
	public StreamingResponseBody stream(HttpServletResponse resp,@RequestBody FileParameter para)
			throws FileNotFoundException {
		
		if(para.getFileId()!=null)
		{
			File sFile = fileMap.get(para.getFileId());
			if(sFile!=null)
			{
				Optional<TlGammaFile>  optionalfile =this.sysRelateFileService.getFileById(para.getFileId());
				if(optionalfile.isPresent())
				{
					resp.setHeader("content-type", "application/octet-stream");
					resp.setContentType("application/octet-stream");
					resp.setHeader("Content-Disposition", "attachment;filename=" + optionalfile.get().getFileName());
					
					byte[] buff = new byte[2048];
					BufferedInputStream bis = null;
					OutputStream os = null;
					try {
							os = resp.getOutputStream();
							bis = new BufferedInputStream(new FileInputStream(sFile));
							int i = bis.read(buff);
							while (i != -1) 
							{
									os.write(buff, 0, buff.length);
									os.flush();
									i = bis.read(buff);
							}
						} catch(IOException e)
						{
							e.printStackTrace();
						}finally 
						{
							if (bis != null) 
							{
								try {
									bis.close();
								} catch (IOException e) 
								{
									e.printStackTrace();
								}
							}
						}
				}
			}
		}
		return null;
	}
	
	@PostMapping(value="/task/upload")
	//@ResponseStatus(HttpStatus.NO_CONTENT) //立即返回
	public Result<Map<String,String>> uploadTaskFile(@RequestParam("file") MultipartFile file,FileParameter para) throws IOException {
		Map<String,String> fileProps = new HashMap<String ,String>();
		
		if(file== null || file.getSize()>52428800)
		{
			ResultUtil.error(400,ReturnDesc.FILE_EXCEED_MAX_SIZE);
		}
		
		TlGammaFile entity =new TlGammaFile();
		entity.setFileGuid(UUID.randomUUID().toString());
		entity.setFileName(file.getOriginalFilename());
		entity.setRelateType("TASK");
		entity.setRelateId(para.getId());
		entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		String fName = file.getOriginalFilename();
		int index = fName.lastIndexOf(".");
		if(index>0)
		{
			String fType = fName.substring(index+1);
			if("PPT/PPTX/DOC/DOCX/XLS/XLSX/CSV/TXT/PDF/BMP/JPG/JPEG/GIF/PNG/RAR/ZIP".indexOf(fType.toUpperCase())>=0)
			{
				TlGammaFile saveresult;
				try {
					entity.setFileType(fType);
					saveresult = this.sysRelateFileService.saveRelateFileEntity(entity);
					
					OutputStream os = new FileOutputStream(new File(fileLocation, saveresult.getFileGuid()));
					InputStream in = file.getInputStream();
					byte[] data = new byte[2048];
					int read = 0;
					while ((read = in.read(data)) > 0) {
						os.write(data, 0, read);
					}
					os.flush();
					os.close();
					init();
					fileProps.put("fileName", saveresult.getFileName());
					fileProps.put("fileId",saveresult.getFileGuid());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return ResultUtil.success(fileProps);
			}
			else
			{
				return ResultUtil.error(400,ReturnDesc.FILE_FORMART_IS_NOT_SUPPORT);
			} 
		}
		return ResultUtil.error(400,ReturnDesc.UNKNOW_ERROR);
	}

	@PostMapping(value = "/filelist")
	public Result<Page<TlGammaFile>> roleList(HttpServletRequest request,@RequestBody RequestParameter para) {

		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();
		Sort sort = null; 
		String sortField = "createTime";
		if (para.getSortDirection()!=null && para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}
		PageRequest pageInfo =new  PageRequest(pageNumber, pageSize, sort);
		Page<TlGammaFile> mList = this.sysRelateFileService.findByRelateId(para.getSkey(),pageInfo);
		List<Map<String,Object>> fileList  = new LinkedList<Map<String,Object>>();
		if(mList!=null)
		{
			fileList = mList.getContent().stream().map(item -> {
				Map<String,Object> tmp = new HashMap<String,Object>();
				tmp.put("fileId", item.getFileGuid());
				tmp.put("fileName", item.getFileName());
				tmp.put("fileDesc", item.getFileDesc());
				if(item.getCreateTime()!=null)
					tmp.put("createTime", df.format(item.getCreateTime()));
				tmp.put("fileType", item.getFileType());
				return tmp;
			}).collect(Collectors.toList());
			
			Page<Map<String,Object>> resPage = new PageImpl<Map<String,Object>>(fileList,pageInfo,mList.getTotalElements());
			return ResultUtil.success(resPage);
		}
		return ResultUtil.success(new PageImpl<Map<String,Object>>(fileList,pageInfo,0));
	}

	
	@PostMapping(value = "/deletefile")
	public Result<String> deletefile(HttpServletRequest request,@RequestBody @Valid FileParameter para) {

		try {
			File videoFile = fileMap.get(para.getFileId());
			videoFile.delete();
			fileMap.remove(para.getFileId());
			this.sysRelateFileService.deleteFile(para.getFileId());
		} catch (Exception e) {

			return ResultUtil.success("FAILURE");
		}
		return ResultUtil.success("SUCCESS");
	}

	@PostMapping
	public Set<String> list() {
		return fileMap.keySet();
	}
	
	private void readAndWrite(final InputStream is, OutputStream os)
			throws IOException {
		byte[] data = new byte[2048];
		int read = 0;
		while ((read = is.read(data)) > 0) {
			os.write(data, 0, read);
		}
		os.flush();
	}

}