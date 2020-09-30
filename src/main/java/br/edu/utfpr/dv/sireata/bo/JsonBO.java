package br.edu.utfpr.dv.sireata.bo;

import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.sireata.dto.AtaJsonDTO;
import br.edu.utfpr.dv.sireata.dto.CampusJsonDTO;
import br.edu.utfpr.dv.sireata.dto.DepartamentoJsonDTO;
import br.edu.utfpr.dv.sireata.dto.OrgaoJsonDTO;
import br.edu.utfpr.dv.sireata.model.Ata;
import br.edu.utfpr.dv.sireata.model.Campus;
import br.edu.utfpr.dv.sireata.model.Departamento;
import br.edu.utfpr.dv.sireata.model.Orgao;
import br.edu.utfpr.dv.sireata.util.DateUtils;

public class JsonBO {

	public List<AtaJsonDTO> popularAtaJson(List<Ata> list){
		List<AtaJsonDTO> ret = new ArrayList<AtaJsonDTO>();
		for(Ata a : list) {
			AtaJsonDTO ata = new AtaJsonDTO();
			
			ata.setTipo(a.getTipo());
			ata.setNumero(a.getNumero());
			ata.setAno(DateUtils.getYear(a.getData()));
			ata.setData(DateUtils.format(a.getData(), "dd/MM/yyyy"));
			
			ret.add(ata);
		}
		return ret;
	}
	
	public List<CampusJsonDTO> popularCampusJson(List<Campus> list){
		List<CampusJsonDTO> ret = new ArrayList<CampusJsonDTO>();
		
		for(Campus c : list) {
			CampusJsonDTO campus = new CampusJsonDTO();
			
			campus.setCodigo(c.getIdCampus());
			campus.setNome(c.getNome());
			
			ret.add(campus);
		}
		return ret;
	}
	
	public List<DepartamentoJsonDTO> popularDepartamentoJson(List<Departamento> list){
		List<DepartamentoJsonDTO> ret = new ArrayList<DepartamentoJsonDTO>();
		for(Departamento d : list) {
			DepartamentoJsonDTO departamento = new DepartamentoJsonDTO();
			
			departamento.setCodigo(d.getIdDepartamento());
			departamento.setNome(d.getNome());
			
			ret.add(departamento);
		}
		return ret;
	}
	
	public List<OrgaoJsonDTO> popularOrgaoJson(List<Orgao> list){
		List<OrgaoJsonDTO> ret = new ArrayList<OrgaoJsonDTO>();
		
		for(Orgao o : list) {
			OrgaoJsonDTO orgao = new OrgaoJsonDTO();
			
			orgao.setCodigo(o.getIdOrgao());
			orgao.setNome(o.getNome());
			
			ret.add(orgao);
		}
		return ret;
	}
	
}
