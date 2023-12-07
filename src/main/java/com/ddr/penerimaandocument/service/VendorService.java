package com.ddr.penerimaandocument.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ddr.penerimaandocument.dto.CreateMasterVendorRequestDTO;
import com.ddr.penerimaandocument.dto.DeleteVendorRequestDTO;
import com.ddr.penerimaandocument.dto.EditMasterCompanyDTO;
import com.ddr.penerimaandocument.dto.EditMasterVendorDTO;
import com.ddr.penerimaandocument.model.Company;
import com.ddr.penerimaandocument.model.Vendor;
import java.util.List;
import com.ddr.penerimaandocument.repository.VendorRepository;

@Service
public class VendorService {
    
    private final RestTemplate restTemplate;

    @Autowired
    private VendorRepository vendorRepository;
	
	@Value("${host.and.port.hris}")
	private String HOST_AND_PORT_HRIS;
		
	@Value("${openendpoint.getauth}")
	private String GET_AUTH;
	
	@Value("${openendpoint.getallcompany}")
	private String GET_ALL_COMPANY;

	@Autowired
	public VendorService(@Qualifier("restTemplateHttp")RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
    
    public List<Vendor> getAllVendor(){
        return vendorRepository.findAll();
    }

	public void addMasterVendor(CreateMasterVendorRequestDTO req){
		Vendor data = new Vendor();
		data.setVendorId(req.getVendorId());
		data.setVendorName(req.getVendorName());
		data.setVendorAddress(req.getVendorAddress());
		data.setContactNumber(req.getContactNumber());
		data.setContactName(req.getContactName());
		vendorRepository.save(data);
	}

	public String testGetData(){
		return vendorRepository.findLastInserted();
	}

	public void deleteCompany(DeleteVendorRequestDTO req){
		for (String id : req.getVendorValue()){
			vendorRepository.deleteById(id);
		}
	}

	public void editMasterVendor(EditMasterVendorDTO req){
		Vendor data = vendorRepository.getReferenceById(req.getVendorId());
		data.setVendorId(req.getVendorId());
		data.setVendorName(req.getVendorName());
		data.setVendorAddress(req.getVendorAddress());
		data.setContactNumber(req.getContactNumber());
		data.setContactName(req.getContactName());
		vendorRepository.save(data);
	}
}
