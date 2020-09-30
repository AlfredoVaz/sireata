package br.edu.utfpr.dv.sireata.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.edu.utfpr.dv.sireata.bo.AtaBO;
import br.edu.utfpr.dv.sireata.bo.JsonBO;
import br.edu.utfpr.dv.sireata.model.Ata;
import br.edu.utfpr.dv.sireata.model.Ata.TipoAta;

@Path("/ata")
public class AtaService {
	
	@GET
	@Path("/listar/{orgao}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listar(@PathParam("orgao") int idOrgao) {
		try {
			List<Ata> list = new AtaBO().listarPorOrgao(idOrgao);
			return Response.ok(new JsonBO().popularAtaJson(list)).build();
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);

			return Response.status(Status.INTERNAL_SERVER_ERROR.ordinal(), e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/visualizar/{orgao}/{tipo}/{numero}/{ano}")
	@Produces("application/pdf")
	public Response baixar(@PathParam("orgao") int idOrgao, @PathParam("tipo") int tipo, @PathParam("numero") int numero, @PathParam("ano") int ano) {
		try {
			Optional<Ata> ata = Optional.ofNullable(new AtaBO().buscarPorNumero(idOrgao, TipoAta.valueOf(tipo), numero, ano));
			if(ata.isPresent()) {
				return Response.ok().type("application/pdf").entity(ata.get().getDocumento()).build();
			} else {
				return Response.status(Status.NOT_FOUND).build();
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);

			return Response.status(Status.INTERNAL_SERVER_ERROR.ordinal(), e.getMessage()).build();
		}
	}
}
