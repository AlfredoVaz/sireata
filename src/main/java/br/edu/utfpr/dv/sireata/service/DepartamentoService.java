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

import br.edu.utfpr.dv.sireata.bo.DepartamentoBO;
import br.edu.utfpr.dv.sireata.bo.JsonBO;
import br.edu.utfpr.dv.sireata.model.Departamento;

@Path("/departamento")
public class DepartamentoService {
	
	@GET
	@Path("/listar/{campus}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listar(@PathParam("campus") int idCampus) {
		try {
			List<Departamento> list = new DepartamentoBO().listarPorCampus(idCampus, true);
			return Response.ok(new JsonBO().popularDepartamentoJson(list)).build();
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);

			return Response.status(Status.INTERNAL_SERVER_ERROR.ordinal(), e.getMessage()).build();
		}
	}
}
