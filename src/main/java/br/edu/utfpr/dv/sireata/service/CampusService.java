package br.edu.utfpr.dv.sireata.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.edu.utfpr.dv.sireata.bo.CampusBO;
import br.edu.utfpr.dv.sireata.bo.JsonBO;
import br.edu.utfpr.dv.sireata.model.Campus;

@Path("/campus")
public class CampusService {

	@GET
	@Path("/listar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listar() {
		try {
			List<Campus> list = new CampusBO().listarTodos(true);
			return Response.ok(new JsonBO().popularCampusJson(list)).build();
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);

			return Response.status(Status.INTERNAL_SERVER_ERROR.ordinal(), e.getMessage()).build();
		}
	}
	
}
