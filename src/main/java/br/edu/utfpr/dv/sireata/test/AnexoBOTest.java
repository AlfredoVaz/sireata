package br.edu.utfpr.dv.sireata.test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import br.edu.utfpr.dv.sireata.bo.AnexoBO;
import br.edu.utfpr.dv.sireata.model.Anexo;
import br.edu.utfpr.dv.sireata.model.Comentario;
import br.edu.utfpr.dv.sireata.model.Comentario.SituacaoComentario;

public class AnexoBOTest {
	
	@Test
	void testarMetodoValidarDadosNull() {
		Anexo anexoTeste = new Anexo();
		AnexoBO anexoBOTest = new AnexoBO();
		anexoTeste.setArquivo(null);
		try {
			anexoBOTest.validarDados(anexoTeste);
		} catch (Exception e) {
			assertEquals("Efetue o envio do arquivo.", e.getMessage());
		}
		
	}
	
	@Test
	void testarMetodoValidarDadosVazio() {
		Anexo anexoTeste = new Anexo();
		AnexoBO anexoBOTest = new AnexoBO();
		anexoTeste.setDescricao("");
		try {
			anexoBOTest.validarDados(anexoTeste);
		} catch (Exception e) {
			assertEquals("Informe a descrição do anexo.", e.getMessage());
		}
		
	}
}
