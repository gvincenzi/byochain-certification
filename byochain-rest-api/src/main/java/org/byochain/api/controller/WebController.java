package org.byochain.api.controller;

import org.byochain.model.entity.Block;
import org.byochain.services.exception.ByoChainServiceException;
import org.byochain.services.service.ICertificationBlockService;
import org.byochain.services.service.impl.CertificationBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web")
public class WebController {
	@Autowired
	@Qualifier("certificationBlockService")
	private ICertificationBlockService blockService;
	
    @GetMapping("/public/check")
    public String check(@RequestParam(name="hash", required=true) String hash, Model model) {
    	try {
			Block block = ((CertificationBlockService)blockService).getBlockByHash(hash);
			if(block == null){
				model.addAttribute("valid", Boolean.FALSE);
				model.addAttribute("error", "This hash code does not exist");
				return "check";
			}
			model.addAttribute("hash", block.getHash());
			model.addAttribute("previoushash", block.getPreviousHash());
			model.addAttribute("logo", block.getData().getLogo());
			model.addAttribute("data", block.getData().getData());
			model.addAttribute("expirationdate", block.getData().getExpirationDate().getTime());
			model.addAttribute("valid", Boolean.TRUE);
			model.addAttribute("validated", block.getValidated());
		} catch (ByoChainServiceException e) {
			model.addAttribute("valid", Boolean.FALSE);
			model.addAttribute("error", e.getMessage());
		}
        
        return "check";
    }

}
