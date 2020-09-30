package br.edu.utfpr.dv.sireata.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.edu.utfpr.dv.sireata.bo.JsonBO;
import br.edu.utfpr.dv.sireata.bo.OrgaoBO;
import br.edu.utfpr.dv.sireata.model.Orgao;

@Path("/orgao")
public class OrgaoService {
	
	@GET
	@Path("/listar/{departamento}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listar(@PathParam("departamento") int idDepartamento) {
		try {
			List<Orgao> list = new OrgaoBO().listarPorDepartamento(idDepartamento);
			return Response.ok(new JsonBO().popularOrgaoJson(list)).build();
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);

			return Response.status(Status.INTERNAL_SERVER_ERROR.ordinal(), e.getMessage()).build();
		}
	}
	
}
