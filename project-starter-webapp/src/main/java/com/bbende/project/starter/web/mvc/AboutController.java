package com.bbende.project.starter.web.mvc;

import com.bbende.project.starter.component.about.AboutInfoDto;
import com.bbende.project.starter.component.about.AboutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AboutController {

    private final AboutService aboutService;

    public AboutController(final AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @GetMapping("/about")
    public ModelAndView getAbout() {
        final AboutInfoDto aboutInfo = aboutService.getAboutInfo();
        final ModelMap modelMap = new ModelMap("aboutInfo", aboutInfo);
        return new ModelAndView("about", modelMap);
    }
}
