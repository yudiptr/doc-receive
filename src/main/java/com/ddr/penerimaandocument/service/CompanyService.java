package com.ddr.penerimaandocument.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ddr.penerimaandocument.dto.CreateMasterCompanyRequestDTO;
import com.ddr.penerimaandocument.dto.DeleteCompanyRequestDTO;
import com.ddr.penerimaandocument.dto.EditMasterCompanyDTO;
import com.ddr.penerimaandocument.model.Company;
import java.util.List;

import com.ddr.penerimaandocument.repository.CompanyRepository;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    
    public List<Company> getAllVendor(){
        return companyRepository.findAll();
    }

	public void addMasterCompany(CreateMasterCompanyRequestDTO req){
		Company data = new Company();
		data.setCompanyId(req.getCompanyId());
		data.setCompanyName(req.getCompanyName());
		data.setCompanyAddress(req.getCompanyAddress());
		data.setContactNumber(req.getContactNumber());
		data.setContactName(req.getContactName());
		companyRepository.save(data);
	}

	public String testGetData(){
		return companyRepository.findLastInserted();
	}

	public void deleteCompany(DeleteCompanyRequestDTO req){
		for (String id : req.getCompanyValue()){
			companyRepository.deleteById(id);
		}
	}

	public void editMasterCompany(EditMasterCompanyDTO req){
		Company data = companyRepository.getReferenceById(req.getCompanyId());
		data.setCompanyId(req.getCompanyId());
		data.setCompanyName(req.getCompanyName());
		data.setCompanyAddress(req.getCompanyAddress());
		data.setContactNumber(req.getContactNumber());
		data.setContactName(req.getContactName());
		companyRepository.save(data);
	}
}
