package com.servicedata.servicelogs.controllers;

import com.servicedata.servicelogs.enums.AuthorityName;
import com.servicedata.servicelogs.forms.AuthorityForm;
import com.servicedata.servicelogs.forms.CompanyFilterData;
import com.servicedata.servicelogs.forms.SystemUserFilterData;
import com.servicedata.servicelogs.models.Authority;
import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.repositories.AuthorityRepository;
import com.servicedata.servicelogs.repositories.CompanyRepository;
import com.servicedata.servicelogs.repositories.SystemUserRepository;
import com.servicedata.servicelogs.services.CompanyService;
import com.servicedata.servicelogs.services.SystemUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController extends LoggedControllerSuper {

    private static final String REDIRECT_TO_COMPANY_LIST = "redirect:/admin/company/list";
    private static final String REDIRECT_TO_USER_LIST = "redirect:/admin/user/list";
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;
    private final SystemUserRepository systemUserRepository;
    private final SystemUserService systemUserService;
    private final AuthorityRepository authorityRepository;

    public AdminController(
            CompanyRepository companyRepository,
            CompanyService companyService,
            SystemUserRepository systemUserRepository,
            SystemUserService systemUserService,
            AuthorityRepository authorityRepository) {
        super(companyService);
        this.companyRepository = companyRepository;
        this.companyService = companyService;
        this.systemUserRepository = systemUserRepository;
        this.systemUserService = systemUserService;
        this.authorityRepository = authorityRepository;
    }

    @RequestMapping("/admin-menu")
    public String adminMenu() {
        return "admin/admin-menu";
    }

    @RequestMapping("/company/list")
    public String getCompanyList(Model model,
                                 @SortDefault("companyId") Pageable pageable,
                                 @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                 @ModelAttribute("filterData") CompanyFilterData filterData,
                                 Sort sort) {
        pageable = PageRequest.of(page - 1, 4, sort);
        Page<Company> companiesPaged = companyService.filteredCompany(pageable, filterData);
        int totalPages = companiesPaged.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("companiesPaged", companiesPaged);
        return "admin/company-list";
    }

    @RequestMapping("/company/edit/{companyId}")
    public String editCompanyForm(Model model,
                                  @PathVariable("companyId") Long companyId) {
        Company company = companyService.findCompanyById(companyId);
        model.addAttribute("company", company);
        return "admin/company-edit";
    }

    @RequestMapping("/company/addForm")
    public String addCompanyForm(Model model) {
        Company company = new Company();
        model.addAttribute(company);
        return "admin/company-add";
    }

    @RequestMapping("/company/save")
    public String saveCompany(@ModelAttribute Company company) {
        companyRepository.save(company);
        return REDIRECT_TO_COMPANY_LIST;
    }

    @RequestMapping("/company/delete/{companyId}")
    public String deleteCompany(Model model,
                                @PathVariable("companyId") Long companyId) {
        model.addAttribute("comment", "Company deleted");
        Company company = companyService.findCompanyById(companyId);
        companyRepository.delete(company);
        return REDIRECT_TO_COMPANY_LIST;
    }

    @RequestMapping("/user/list")
    public String getUserList(Model model,
                              @SortDefault("systemUserId") Pageable pageable,
                              @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                              @ModelAttribute("filterData") SystemUserFilterData filterData,
                              Sort sort) {
        pageable = PageRequest.of(page - 1, 4, sort);
        Page<SystemUser> systemUsersPaged = systemUserService.filteredSystemUser(pageable, filterData);
        int totalPages = systemUsersPaged.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("systemUsersPaged", systemUsersPaged);
        return "admin/user-list";
    }

    @RequestMapping("/user/edit/{systemUserId}")
    public String editUserForm(Model model,
                               @PathVariable("systemUserId") Long systemUserId
    ) {
        SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
        model.addAttribute("systemUser", systemUser);
        return "admin/user-edit";
    }

    @RequestMapping("/user/edit/save")
    public String saveUser(Model model,
                           @Valid SystemUser systemUser,
                           Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute(systemUser);
            return "admin/user-edit";
        }
        SystemUser systemUserToSave = systemUserService.findSystemUserById(systemUser.getSystemUserId());
        systemUserToSave.setUsername(systemUser.getUsername());
        systemUserToSave.setName(systemUser.getName());
        systemUserToSave.setSurname(systemUser.getSurname());
        systemUserRepository.save(systemUserToSave);
        return REDIRECT_TO_USER_LIST;
    }

    @RequestMapping("/user/password/{systemUserId}")
    public String passwordUserForm(Model model,
                                   @PathVariable("systemUserId") Long systemUserId) {
        SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
        model.addAttribute("systemUser", systemUser);
        return "admin/user-password";
    }

    @RequestMapping("/user/password/passwordSave")
    public String passwordUserForm(Model model,
                                   @Valid SystemUser systemUser,
                                   Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute(systemUser);
            return "admin/user-password";
        }
        systemUserService.saveSystemUser(systemUser);
        return REDIRECT_TO_USER_LIST;
    }

    @RequestMapping("/user/delete/{systemUserId}")
    public String deleteUser(Model model,
                             @PathVariable("systemUserId") Long systemUserId) {
        model.addAttribute("comment", "System user deleted");
        SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
        systemUserRepository.delete(systemUser);
        return REDIRECT_TO_USER_LIST;
    }

    @RequestMapping("/user/roles/{systemUserId}")
    public String rolesUserForm(Model model,
                                @PathVariable("systemUserId") Long systemUserId) {
        SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
        AuthorityForm authorityForm = new AuthorityForm();
        for (Authority authority : systemUser.getAuthorities()) {
            if (authority.getName() == AuthorityName.ROLE_COMPANYUSER) {
                authorityForm.setCompanyuser(true);
            } else if (authority.getName() == AuthorityName.ROLE_SERVICEMAN) {
                authorityForm.setServiceman(true);
            } else if (authority.getName() == AuthorityName.ROLE_ADMIN) {
                authorityForm.setAdmin(true);
            }
        }
        authorityForm.setSystemUserId(systemUserId);
        model.addAttribute("authorityForm", authorityForm);
        return "admin/user-roles";
    }

    @RequestMapping("/user/rolesSave")
    public String rolesUserSet(@ModelAttribute AuthorityForm authorityForm) {
        SystemUser systemUserToSave = systemUserService.findSystemUserById(authorityForm.getSystemUserId());
        HashSet<Authority> authorities = new HashSet<>();
        if (authorityForm.getCompanyuser() == true) {
            authorities.add(authorityRepository.findByName(AuthorityName.ROLE_COMPANYUSER));
        }
        if (authorityForm.getServiceman() == true) {
            authorities.add(authorityRepository.findByName(AuthorityName.ROLE_SERVICEMAN));
        }
        if (authorityForm.getAdmin() == true) {
            authorities.add(authorityRepository.findByName(AuthorityName.ROLE_ADMIN));
        }
        systemUserToSave.setAuthorities(authorities);
        systemUserRepository.save(systemUserToSave);
        return REDIRECT_TO_USER_LIST;
    }

    @RequestMapping("/user/company/{systemUserId}")
    public String companyUserList(Model model,
                                  @PathVariable("systemUserId") Long systemUserId,
                                  @SortDefault("companyId") Pageable pageable,
                                  @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                  @ModelAttribute("filterData") CompanyFilterData filterData,
                                  Sort sort) {
        SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
        model.addAttribute("systemUserId", systemUserId);
        pageable = PageRequest.of(page - 1, 4, sort);
        Page<Company> companiesPaged = companyService.filteredCompany(pageable, filterData);
        int totalPages = companiesPaged.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("companiesPaged", companiesPaged);
        List<Long> userCompaniesList = systemUser.getCompanies().stream().map(c -> c.getCompanyId()).collect(Collectors.toList());
        log.info("User companies: {}", userCompaniesList);
        model.addAttribute("userCompaniesList", userCompaniesList);
        return "admin/user-companies";
    }

    @RequestMapping("/user/company/add/{systemUserId}/{companyId}")
    public String companyUserSet(@PathVariable("systemUserId") Long systemUserId,
                                 @PathVariable("companyId") Long companyId) {
        SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
        Set<Company> userCompanies = systemUser.getCompanies();
        userCompanies.add(companyService.findCompanyById(companyId));
        systemUser.setCompanies(userCompanies);
        systemUserRepository.save(systemUser);
        return "redirect:/admin/user/company/" + systemUserId;
    }

    @RequestMapping("/user/company/delete/{systemUserId}/{companyId}")
    public String companyUserDelete(@PathVariable("systemUserId") Long systemUserId,
                                    @PathVariable("companyId") Long companyId) {
        SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
        Set<Company> userCompanies = systemUser.getCompanies();
        userCompanies.remove(companyService.findCompanyById(companyId));
        systemUser.setCompanies(userCompanies);
        systemUserRepository.save(systemUser);
        return "redirect:/admin/user/company/" + systemUserId;
    }

}