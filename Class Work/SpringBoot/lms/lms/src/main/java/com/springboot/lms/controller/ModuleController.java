package com.springboot.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.lms.exception.ResourceNotFoundException;
import com.springboot.lms.model.CModule;
import com.springboot.lms.service.ModuleService;

@RestController
@RequestMapping("/api/module")
public class ModuleController {
	@Autowired
	private ModuleService ms;
	
	
	
	
	
	
	/**AIM      : Add module to DB  
	 * PATH     :localhost:8080/api/module/add
	 * METHOD   : post
	 * RESPONSE : module
	 * INPUT    : module
	 * 
	 * @param courseId
	 * @param module
	 * @return
	 */
	@PostMapping("/add/{courseId}")
	public CModule addModule(@PathVariable int courseId,@RequestBody CModule module) {
		CModule mod = new CModule();
			try {
				mod= ms.addModule(courseId,module);
			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		return mod;
	}
	

}
