package com.cellinfo.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaFile;
import com.cellinfo.service.SysRelateFileService;
import com.cellinfo.utils.ResultUtil;
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

	
	
	@RequestMapping(method = RequestMethod.GET, value = "/{fileId:.+}")
	public StreamingResponseBody stream(@PathVariable String fileId)
			throws FileNotFoundException {
		String trueName = this.sysRelateFileService.getFileNameById(fileId);
		
		File sFile = fileMap.get(fileId);

		final InputStream videoFileStream = new FileInputStream(sFile);

		return (os) -> {
			readAndWrite(videoFileStream, os);
		};
	}
	
	@RequestMapping(method = RequestMethod.POST,value="/task/{gid}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Result<Map<String,String>> uploadTaskFile(@RequestParam("file") MultipartFile multifile,
			@PathVariable("gid") String gid) throws IOException {
		Map<String,String> fileProps = new HashMap<String ,String>();
		
		TlGammaFile entity =new TlGammaFile();
		entity.setFname(multifile.getOriginalFilename());
		entity.setPtype("TASK");
		entity.setRelateid(gid);
		entity.setFtype(multifile.getContentType());
		TlGammaFile saveresult =this.sysRelateFileService.saveRelateFileEntity(entity);
		
		OutputStream os = new FileOutputStream(new File(fileLocation, saveresult.getFileid()));
		readAndWrite(multifile.getInputStream(), os);
		init();
		
		fileProps.put("fname", saveresult.getFname());
		fileProps.put("fileid",saveresult.getFileid());
		
		return ResultUtil.success(fileProps);
	}
	
	@RequestMapping(method = RequestMethod.POST,value="/kernel/{gid}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void uploadKernelFile(@RequestParam("file") MultipartFile multifile,
			@PathVariable("gid") String gid) throws IOException {

		TlGammaFile entity =new TlGammaFile();
		entity.setFname(multifile.getOriginalFilename());
		entity.setPtype("KERNEL");
		entity.setRelateid(gid);
		entity.setFtype(multifile.getContentType());
		TlGammaFile saveresult =this.sysRelateFileService.saveRelateFileEntity(entity);
		
		OutputStream os = new FileOutputStream(new File(fileLocation, saveresult.getFileid()));
		readAndWrite(multifile.getInputStream(), os);
		init();
	}

	@GetMapping(value = "/filelist")
	public Result<Page<TlGammaFile>> roleList(HttpServletRequest request) {

		String gid = request.getParameter("gid");
		String layerId = request.getParameter("layerId");

		List<TlGammaFile> mList = this.sysRelateFileService.findByRelateid(gid);

		return ResultUtil.success(mList);
	}

	
	@GetMapping(value = "/deletefile")
	public Result<Page<TlGammaFile>> deletefile(HttpServletRequest request) {

		try {
			String fileid = request.getParameter("fileid");
			File videoFile = fileMap.get(fileid);
			videoFile.delete();
			fileMap.remove(fileid);
			this.sysRelateFileService.deleteFile(fileid);
		} catch (Exception e) {

			return ResultUtil.success("FAILURE");
		}
		return ResultUtil.success("SUCCESS");
	}

	@RequestMapping(method = RequestMethod.GET)
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