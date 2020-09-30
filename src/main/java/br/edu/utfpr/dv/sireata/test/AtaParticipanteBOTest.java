package br.edu.utfpr.dv.sireata.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.edu.utfpr.dv.sireata.bo.AtaParticipanteBO;
import br.edu.utfpr.dv.sireata.model.Ata;
import br.edu.utfpr.dv.sireata.model.AtaParticipante;

@RunWith(MockitoJUnitRunner.class)
class AtaParticipanteBOTest {
	
	@Mock
	private AtaParticipanteBO participanteBOTeste;
	
	@Test
	void SalvarParticipante() {
		
		AtaParticipante participanteTeste = new AtaParticipante();
		
		participanteBOTeste = mock(AtaParticipanteBO.class);
		
		participanteTeste.setAta(new Ata());
		try {
			when(participanteBOTeste.salvar(participanteTeste)).thenReturn(1);
			int participanteResult = participanteBOTeste.salvar(participanteTeste);
			assertEquals(1, participanteResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void BuscarPorId() {
		AtaParticipante participanteTeste = new AtaParticipante();
		participanteTeste.setMotivo("Pesquisa");
		participanteTeste.setMembro(true);
		participanteTeste.setDesignacao("Orador");
		
		participanteBOTeste = mock(AtaParticipanteBO.class);
		try {
			when(participanteBOTeste.buscarPorId(1)).thenReturn(participanteTeste);
			AtaParticipante participanteResult = participanteBOTeste.buscarPorId(1);
			assertEquals("Pesquisa", participanteResult.getMotivo());
			assertEquals("Orador", participanteResult.getDesignacao());
			assertTrue(participanteResult.isMembro());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
