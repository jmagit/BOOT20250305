package com.example.controllers;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.example.dtos.CityDTO;
import com.example.exceptions.BadRequestException;
import com.example.exceptions.NotFoundException;
import com.example.models.entities.City;
import com.example.models.entities.Country;
import com.example.models.repositories.CityRepository;
import com.example.models.repositories.CountryRepository;

@Controller
@RequestMapping(path="/ciudades")
public class CiudadesController {
	@Autowired
	private CityRepository dao;
	@Autowired 
	private MessageSource ms;
	
	@GetMapping(path="")
	public String list(Model model, @PageableDefault(size=20, sort = {"city"})  Pageable page) {
		model.addAttribute("listado", dao.findAll(page));
		return "ciudades/list";
	}
	@GetMapping(path="/{id:\\d+}/**")
	public String view(@PathVariable Long id, Model model) {
		Optional<City> item = dao.findById(id);
		if(!item.isPresent())
			throw new NotFoundException();
		model.addAttribute("elemento", item.get());
		return "ciudades/view";
	}
	@GetMapping("/add")
	public String addGET(Model model) {
		model.addAttribute("modo", "add");
		model.addAttribute("action", "ciudades/add");
		model.addAttribute("elemento", new CityDTO());
		model.addAttribute("paises", getPaises());
		return "ciudades/form";
	}
	@PostMapping("/add")
	public ModelAndView addPOST(@ModelAttribute("elemento") @Valid CityDTO item, 
			BindingResult result, Locale locale) {
		ModelAndView mv = new ModelAndView();
		if(dao.findById(item.getCityId()).isPresent())
			result.addError(new FieldError("elemento", "cityId", ms.getMessage("errormsg.clave.duplicada", null, locale)));
		if(result.hasErrors()) {
			mv.addObject("modo", "add");
			mv.addObject("action", "ciudades/add");
			mv.addObject("elemento", item);
			mv.addObject("paises", getPaises());
			mv.setViewName("ciudades/form");
		} else {
			dao.save(CityDTO.form(item));
			mv.setViewName("redirect:/ciudades");
		}
		return mv;
	}

	@GetMapping("/{id:\\d+}/edit")
	public String editGET(@PathVariable Long id, Model model) {
		Optional<City> item = dao.findById(id);
		if(!item.isPresent())
			throw new NotFoundException();
		model.addAttribute("modo", "edit");
		model.addAttribute("action", "ciudades/" + id + "/edit");
		model.addAttribute("elemento", CityDTO.form(item.get()));
		model.addAttribute("paises", getPaises());
		return "ciudades/form";
	}
	@PostMapping("/{id:\\d+}/edit")
	public ModelAndView editPOST(@PathVariable Long id, @ModelAttribute("elemento") @Valid CityDTO item, 
			BindingResult result, Locale locale) {
		ModelAndView mv = new ModelAndView();
		item.setCityId(id);
		if(result.hasErrors()) {
			mv.addObject("modo", "edit");
			mv.addObject("action", "ciudades/" + id + "/edit");
			mv.addObject("elemento", item);
			mv.addObject("paises", getPaises());
			mv.setViewName("ciudades/form");
		} else {
			if(!dao.findById(item.getCityId()).isPresent())
				throw new NotFoundException();
			dao.save(CityDTO.form(item));
			mv.setViewName("redirect:/ciudades");
		}
		return mv;
	}
	
	@GetMapping("/{id:\\d+}/delete")
	public String editGET(@PathVariable Long id) {
//		try {
			dao.deleteById(id);
//		} catch (Exception e) {
//			throw new BadRequestException(e.getMessage(), e);
//		}
		return "redirect:/ciudades";
	}
	
	@Autowired
	private CountryRepository daoPaises;
	
	private List<Country> getPaises() {
		List<Country> rslt = daoPaises.findAll(Sort.by("country"));
		rslt.add(0, new Country(-1L, ""));
		return rslt;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ EmptyResultDataAccessException.class })
    public void badRequest(Exception e) {
    	throw new BadRequestException("nuevo: " + e.getMessage(), e);
    }
}
