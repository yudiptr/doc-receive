package com.ddr.penerimaandocument.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ddr.penerimaandocument.dto.CreateMasterVendorRequestDTO;
import com.ddr.penerimaandocument.dto.DeleteVendorRequestDTO;
import com.ddr.penerimaandocument.dto.EditMasterVendorDTO;
import com.ddr.penerimaandocument.model.Vendor;
import java.util.List;
import com.ddr.penerimaandocument.repository.VendorRepository;

@Service
public class VendorService {
    
    @Autowired
    private VendorRepository vendorRepository;
    
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

	public void deleteVendor(DeleteVendorRequestDTO req){
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
