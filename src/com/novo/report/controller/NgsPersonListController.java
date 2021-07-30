package com.novo.report.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.Person;
import com.novo.report.beans.SampleFile;
import com.novo.report.beans.User;
import com.novo.report.service.NgsIntegratedMutationFileService;
import com.novo.report.service.NgsPersonListService;
import com.novo.report.service.SampleFileService;
import com.novo.report.utils.DateUtil;

@Controller
@RequestMapping("person")
public class NgsPersonListController {
	
	@Autowired
	private NgsPersonListService ngsPersonListService;
	
	@Autowired
	NgsIntegratedMutationFileService ngsIntegratedMutationFileService; //ngs结果查询的service
	
	@Autowired
	private SampleFileService sampleFileService;
	
	@RequestMapping(value = "personList", produces = "application/json; charset=utf-8")
	public String personList(HttpServletRequest request,Model model) {
		User userSession=(User)request.getSession().getAttribute("user");
		try {
			String person_name= request.getParameter("person_name");
			String gender= request.getParameter("gender");
			String birthday= request.getParameter("birthday");
			String remark= request.getParameter("remark");
			
			Integer person_id = ngsPersonListService.isExistPerson(person_name,gender);
			Person person = new Person();
			if(person_id == null ){
				person.setPerson_name(person_name);
				person.setGender(gender);
				person.setBirthday(birthday);
				person.setRemark(remark);
				person.setCreated_by(userSession.getUser_account());
				person.setCreated_date(DateUtil.getSystemTime());
				ngsPersonListService.savePerson(person);
				person_id = person.getPerson_id();
			}
			Integer existPersion = sampleFileService.isExistPerson_id(person_id);
			if(existPersion == null || existPersion == 0){
				//System.out.println(person_id);
				//person_id给sample_file persion_id 赋值
				SampleFile sampleFile = new SampleFile();
				sampleFile.setPerson_name(person_name);
				sampleFile.setGender(gender);
				List<Person> personList = ngsPersonListService.getAllPersonByPage(sampleFile);
				for (Person person_val : personList) {
					ngsPersonListService.updatePersonId(person_val.getSample_id(),person_id);
				}
			}
			model.addAttribute("person_id", person_id);
			model.addAttribute("person_name", person_name);
			model.addAttribute("gender", gender);
			model.addAttribute("birthday", birthday);
			model.addAttribute("remark", remark);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "ngs/personList";
	}
	
	@RequestMapping("getAllPersonByPage")
	@ResponseBody
	public Object getAllPersonByPage(SampleFile sampleFile){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("dataList", ngsPersonListService.getAllPersonByPage(sampleFile));
		return map;
		
	}
	
	//添加数据
	@RequestMapping("updatePersonId")
	@ResponseBody
	public Object updatePersonId(Integer sample_id,Integer person_id){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try{
			ngsPersonListService.updatePersonId(sample_id,person_id);
			jsonMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			jsonMap.put("success", false);
		}
		return jsonMap;
	}
	
	@RequestMapping("historyList")
	public String historyList(Integer person_id,Model model) {
		List<Person> personList=ngsPersonListService.getSampleIdAndSubbarcode(person_id);
		int length = personList.size();
		/*Integer[] sample_ids= new Integer[length];
		String[] subbarcodes= new String[length];*/
		String sample_ids= "";
		String subbarcodes= "";
		for(int i=0; i<length; i++){
			Person person=personList.get(i);
			if(i==length-1){
				sample_ids += person.getSample_id();
				subbarcodes +=person.getSubbarcode();
			}else{
				sample_ids += person.getSample_id()+",";
				subbarcodes +=person.getSubbarcode()+",";
			}
		}
		model.addAttribute("sample_ids", sample_ids);
		model.addAttribute("subbarcodes", subbarcodes);
		return "ngs/historyList";
	}
	
}
