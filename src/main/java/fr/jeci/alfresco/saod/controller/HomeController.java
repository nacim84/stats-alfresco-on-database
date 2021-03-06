package fr.jeci.alfresco.saod.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.jeci.alfresco.saod.SaodException;
import fr.jeci.alfresco.saod.service.SaodService;

@Controller
public class HomeController {
	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

	@Value("${description}")
	private String title = "Jeci SAOD";

	@Value("${version}")
	private String version = "0.0.0";

	@Autowired
	private SaodService saodService;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("time", new Date());
		model.addAttribute("title", this.title);
		model.addAttribute("version", this.version);
		//
		// try {
		// model.addAttribute("selectDirLocalSize", alfrescoDao.selectDirLocalSize());
		// } catch (SaodException e) {
		// model.addAttribute("error", e.getLocalizedMessage());
		// LOG.error(e.getMessage(), e);
		// }

		return "home";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@RequestMapping("/access")
	public String access(Model model) {
		return "access";
	}

	@RequestMapping("/init")
	@Secured("ROLE_ADMIN")
	public String init(Model model) {
		model.addAttribute("time", new Date());
		model.addAttribute("title", this.title);
		model.addAttribute("version", this.version);

		try {
			long start = System.currentTimeMillis();
			saodService.loadDataFromAlfrescoDB();
			LOG.info("Duration : " + (System.currentTimeMillis() - start));
		} catch (SaodException e) {
			model.addAttribute("error", e.getLocalizedMessage());
			LOG.error(e.getMessage(), e);
		}
		return "home";
	}

	@RequestMapping("/print")
	@Secured("ROLE_USER")
	public String print(@RequestParam(value = "nodeid", required = false, defaultValue = "") String nodeid,
			Model model) {
		model.addAttribute("time", new Date());
		model.addAttribute("version", this.version);

		try {
			long start = System.currentTimeMillis();

			if (StringUtils.hasText(nodeid) && Integer.decode(nodeid) > 0) {
				model.addAttribute("dir", this.saodService.loadPrintNode(nodeid));
				model.addAttribute("title", String.format("%s", nodeid));
				model.addAttribute("nodes", this.saodService.getSubFolders(nodeid));

				String path = this.saodService.computePath(nodeid);
				model.addAttribute("path", path);
			} else {
				model.addAttribute("title", String.format("Roots", nodeid));
				model.addAttribute("nodes", this.saodService.getRoots());
			}
			LOG.info("Duration : " + (System.currentTimeMillis() - start));
		} catch (SaodException e) {
			model.addAttribute("error", e.getLocalizedMessage());
			LOG.error(e.getMessage(), e);
		}

		return "print";
	}

}
