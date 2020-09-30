package br.edu.utfpr.dv.sireata.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.sireata.model.Ata;
import br.edu.utfpr.dv.sireata.model.Ata.TipoAta;
import br.edu.utfpr.dv.sireata.util.DateUtils;

public class AtaDAO {
	
	public Ata buscarPorId(Integer id) throws SQLException{
		String query ="SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"WHERE idAta = ?";
		try(Connection conn = ConnectionDAO.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query);){
			
			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery();) {
				if(rs.next()){
					return this.carregarObjeto(rs);
				}else{
					return null;
				}
			}
		}
	}
	
	public Ata buscarPorNumero(Integer idOrgao, TipoAta tipo, Integer numero, Integer ano) throws SQLException{
		String query = "SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"WHERE atas.publicada = 1 AND atas.idOrgao = ? AND atas.tipo = ? AND atas.numero = ? AND YEAR(atas.data) = ?";
		try(Connection conn = ConnectionDAO.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query);){
			
			stmt.setInt(1, idOrgao);
			stmt.setInt(2, tipo.getValue());
			stmt.setInt(3, numero);
			stmt.setInt(4, ano);
			try(ResultSet rs = stmt.executeQuery();) {
				if(rs.next()){
					return this.carregarObjeto(rs);
				}else{
					return null;
				}
			}
		}
	}
	
	public Ata buscarPorPauta(Integer idPauta) throws SQLException{
		
		String query = "SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"INNER JOIN pautas ON pautas.idAta=atas.idAta " +
				"WHERE pautas.idPauta = ?";
		try(Connection conn = ConnectionDAO.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query);){
			
			stmt.setInt(1, idPauta);
			
			try(ResultSet rs = stmt.executeQuery();) {
				if(rs.next()){
					return this.carregarObjeto(rs);
				}else{
					return null;
				}
			}
		}
	}
	
	public Integer buscarProximoNumeroAta(Integer idOrgao, Integer ano, TipoAta tipo) throws SQLException{
		
		String query = "SELECT MAX(numero) AS numero FROM atas WHERE idOrgao = ? AND YEAR(data) = ? AND tipo = ?";
		try(Connection conn = ConnectionDAO.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query);){
			
			stmt.setInt(1, idOrgao);
			stmt.setInt(2, ano);
			stmt.setInt(3, tipo.getValue());
			
			try(ResultSet rs = stmt.executeQuery();) {
				if(rs.next()){
					return rs.getInt("numero") + 1;
				}else{
					return 1;
				}
			}
		}
	}
	
	public List<Ata> listar(Integer idUsuario, Integer idCampus, Integer idDepartamento, Integer idOrgao, Boolean publicadas) throws SQLException{
		
		String query = "SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN ataparticipantes ON ataparticipantes.idAta=atas.idAta " +
				"INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"WHERE ataparticipantes.idUsuario = " + String.valueOf(idUsuario) +
				" AND atas.publicada = " + (publicadas ? "1 " : "0 ") +
				(idCampus > 0 ? " AND departamentos.idCampus = " + String.valueOf(idCampus) : "") +
				(idDepartamento > 0 ? " AND departamentos.idDepartamento = " + String.valueOf(idDepartamento) : "") +
				(idOrgao > 0 ? " AND atas.idOrgao = " + String.valueOf(idOrgao) : "") +
				"ORDER BY atas.data DESC";
		
		try(Connection conn = ConnectionDAO.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery();){
			
			List<Ata> list = new ArrayList<Ata>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			return list;
		}
	}
	
	public List<Ata> listarPublicadas() throws SQLException{
		
		String query = "SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"WHERE atas.publicada=1 ORDER BY atas.data DESC";
		try(Connection conn = ConnectionDAO.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery();){
			
			List<Ata> list = new ArrayList<Ata>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			
			return list;
		}
	}
	
	public List<Ata> listarPorOrgao(Integer idOrgao) throws SQLException{
		
		String query = "SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"WHERE atas.publicada=1 AND atas.idOrgao=" + String.valueOf(idOrgao) + " ORDER BY atas.data DESC";
		try(Connection conn = ConnectionDAO.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery();){
		
			List<Ata> list = new ArrayList<Ata>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			
			return list;
		}
	}
	
	public List<Ata> listarPorDepartamento(Integer idDepartamento) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"WHERE atas.publicada=1 AND Orgaos.idDepartamento=" + String.valueOf(idDepartamento) + " ORDER BY atas.data DESC");
		
			List<Ata> list = new ArrayList<Ata>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			
			return list;
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public List<Ata> listarPorCampus(Integer idCampus) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"WHERE atas.publicada=1 AND departamentos.idCampus=" + String.valueOf(idCampus) + " ORDER BY atas.data DESC");
		
			List<Ata> list = new ArrayList<Ata>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			
			return list;
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public List<Ata> listarNaoPublicadas(Integer idUsuario) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"INNER JOIN ataparticipantes ON ataparticipantes.idAta=atas.idAta " +
				"WHERE atas.publicada=0 AND ataparticipantes.idUsuario=" + String.valueOf(idUsuario) +" ORDER BY atas.data DESC");
		
			List<Ata> list = new ArrayList<Ata>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			
			return list;
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public List<Ata> listarPorOrgao(Integer idOrgao, Integer idUsuario) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"INNER JOIN ataparticipantes ON ataparticipantes.idAta=atas.idAta " +
				"WHERE atas.publicada=0 AND ataparticipantes.idUsuario=" + String.valueOf(idUsuario) + " AND atas.idOrgao=" + String.valueOf(idOrgao) + " ORDER BY atas.data DESC");
		
			List<Ata> list = new ArrayList<Ata>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			
			return list;
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public List<Ata> listarPorDepartamento(Integer idDepartamento, Integer idUsuario) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"INNER JOIN ataparticipantes ON ataparticipantes.idAta=atas.idAta " +
				"WHERE atas.publicada=0 AND ataparticipantes.idUsuario=" + String.valueOf(idUsuario) + " AND Orgaos.idDepartamento=" + String.valueOf(idDepartamento) + " ORDER BY atas.data DESC");
			
			List<Ata> list = new ArrayList<Ata>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			
			return list;
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public List<Ata> listarPorCampus(Integer idCampus, Integer idUsuario) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"INNER JOIN ataparticipantes ON ataparticipantes.idAta=atas.idAta " +
				"WHERE atas.publicada=0 AND ataparticipantes.idUsuario=" + String.valueOf(idUsuario) + " AND departamentos.idCampus=" + String.valueOf(idCampus) + " ORDER BY atas.data DESC");
		
			List<Ata> list = new ArrayList<Ata>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			
			return list;
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public Integer salvar(Ata ata) throws SQLException{
		Boolean insert = (ata.getIdAta() == 0);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			
			if(insert){
				stmt = conn.prepareStatement("INSERT INTO atas(idOrgao, idPresidente, idSecretario, tipo, numero, data, local, localCompleto, dataLimiteComentarios, consideracoesIniciais, audio, documento, publicada, dataPublicacao, aceitarComentarios) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, 0, NULL, 0)", Statement.RETURN_GENERATED_KEYS);
			}else{
				stmt = conn.prepareStatement("UPDATE atas SET idOrgao=?, idPresidente=?, idSecretario=?, tipo=?, numero=?, data=?, local=?, localCompleto=?, dataLimiteComentarios=?, consideracoesIniciais=?, audio=? WHERE idAta=?");
			}
			
			stmt.setInt(1, ata.getOrgao().getIdOrgao());
			stmt.setInt(2, ata.getPresidente().getIdUsuario());
			stmt.setInt(3, ata.getSecretario().getIdUsuario());
			stmt.setInt(4, ata.getTipo().getValue());
			stmt.setInt(5, ata.getNumero());
			stmt.setTimestamp(6, new java.sql.Timestamp(ata.getData().getTime()));
			stmt.setString(7, ata.getLocal());
			stmt.setString(8, ata.getLocalCompleto());
			stmt.setDate(9, new java.sql.Date(ata.getDataLimiteComentarios().getTime()));
			stmt.setString(10, ata.getConsideracoesIniciais());
			if(ata.getAudio() == null){
				stmt.setNull(11, Types.BINARY);
			}else{
				stmt.setBytes(11, ata.getAudio());	
			}
			
			if(!insert){
				stmt.setInt(12, ata.getIdAta());
			}
			
			stmt.execute();
			
			if(insert){
				rs = stmt.getGeneratedKeys();
				
				if(rs.next()){
					ata.setIdAta(rs.getInt(1));
				}
			}
			
			return ata.getIdAta();
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public void publicar(Integer idAta, byte[] documento) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.prepareStatement("UPDATE atas SET documento=?, dataPublicacao=?, publicada=1, aceitarComentarios=0 WHERE publicada=0 AND idAta=?");
		
			stmt.setBytes(1, documento);
			stmt.setTimestamp(2, new java.sql.Timestamp(DateUtils.getNow().getTime().getTime()));
			stmt.setInt(3, idAta);
			
			stmt.execute();
		}finally{
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public void liberarComentarios(Integer idAta) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			stmt.execute("UPDATE atas SET aceitarComentarios=1 WHERE publicada=0 AND idAta=" + String.valueOf(idAta));
		}finally{
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public void bloquearComentarios(Integer idAta) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			stmt.execute("UPDATE atas SET aceitarComentarios=0 WHERE idAta=" + String.valueOf(idAta));
		}finally{
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	private Ata carregarObjeto(ResultSet rs) throws SQLException{
		Ata ata = new Ata();
		
		ata.setIdAta(rs.getInt("idAta"));
		ata.getOrgao().setIdOrgao(rs.getInt("idOrgao"));
		ata.getOrgao().setNome(rs.getString("orgao"));
		ata.getPresidente().setIdUsuario(rs.getInt("idPresidente"));
		ata.getPresidente().setNome(rs.getString("presidente"));
		ata.getSecretario().setIdUsuario(rs.getInt("idSecretario"));
		ata.getSecretario().setNome(rs.getString("secretario"));
		ata.setTipo(TipoAta.valueOf(rs.getInt("tipo")));
		ata.setNumero(rs.getInt("numero"));
		ata.setData(rs.getTimestamp("data"));
		ata.setLocal(rs.getString("local"));
		ata.setLocalCompleto(rs.getString("localCompleto"));
		ata.setDataLimiteComentarios(rs.getDate("dataLimiteComentarios"));
		ata.setConsideracoesIniciais(rs.getString("consideracoesIniciais"));
		ata.setAudio(rs.getBytes("audio"));
		ata.setPublicada(rs.getInt("publicada") == 1);
		ata.setAceitarComentarios(rs.getInt("aceitarComentarios") == 1);
		ata.setDataPublicacao(rs.getTimestamp("dataPublicacao"));
		ata.setDocumento(rs.getBytes("documento"));
		
		return ata;
	}
	
	public Boolean temComentarios(Integer idAta) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT COUNT(comentarios.idComentario) AS qtde FROM comentarios " +
				"INNER JOIN pautas ON pautas.idPauta=comentarios.idPauta " + 
				"WHERE pautas.idAta=" + String.valueOf(idAta));
		
			if(rs.next()){
				return (rs.getInt("qtde") > 0);
			}else{
				return false;
			}
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public Boolean isPresidenteOuSecretario(Integer idUsuario, Integer idAta) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT atas.idAta FROM atas " +
				"WHERE idAta=" + String.valueOf(idAta) + " AND (idPresidente=" + String.valueOf(idUsuario) + " OR idSecretario=" + String.valueOf(idUsuario) + ")");
		
			return rs.next();
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public Boolean isPresidente(Integer idUsuario, Integer idAta) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT atas.idAta FROM atas " +
				"WHERE idAta=" + String.valueOf(idAta) + " AND idPresidente=" + String.valueOf(idUsuario));
		
			return rs.next();
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public Boolean isPublicada(int idAta) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT atas.publicada FROM atas " +
				"WHERE idAta=" + String.valueOf(idAta));
		
			if(rs.next()) {
				return rs.getInt("publicada") == 1;
			} else {
				return false;
			}
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public Boolean excluir(Integer idAta) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			
			stmt.execute("DELETE FROM comentarios WHERE idPauta IN (SELECT idPauta FROM pautas WHERE idAta=" + String.valueOf(idAta) + ")");
			stmt.execute("DELETE FROM pautas WHERE idAta=" + String.valueOf(idAta));
			stmt.execute("DELETE FROM ataparticipantes WHERE idAta=" + String.valueOf(idAta));
			stmt.execute("DELETE FROM anexos WHERE idAta=" + String.valueOf(idAta));
			Boolean ret = stmt.execute("DELETE FROM atas WHERE idAta=" + String.valueOf(idAta));
			
			conn.commit();
			
			return ret;
		}catch(SQLException ex) {
			conn.rollback();
			throw ex;
		}finally{
			conn.setAutoCommit(true);
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}

}
