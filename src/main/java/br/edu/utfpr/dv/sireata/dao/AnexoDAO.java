package br.edu.utfpr.dv.sireata.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.edu.utfpr.dv.sireata.model.Anexo;

public class AnexoDAO {
	
	public Anexo buscarPorId(Integer id) throws SQLException{
		String query = "SELECT anexos.* FROM anexos WHERE idAnexo = ?";
		try(Connection conn = ConnectionDAO.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query);){
		
			stmt.setInt(1, id);
			try(ResultSet rs = stmt.executeQuery();) {
				if(rs.next()){
					return this.carregarObjeto(rs);
				}else{
					return null;
				}
			}
		}
	}
	
	public List<Anexo> listarPorAta(Integer idAta) throws SQLException{
		String query = "SELECT anexos.* FROM anexos WHERE idAta=" + String.valueOf(idAta) + " ORDER BY anexos.ordem";
		try(Connection conn = ConnectionDAO.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query);){
			
			try(ResultSet rs = stmt.getResultSet();){
				List<Anexo> list = new ArrayList<Anexo>();
				
				while(rs.next()){
					list.add(this.carregarObjeto(rs));
				}
				return list;
			}
		}
	}
	
	public int salvar(Anexo anexo) throws SQLException{
		Optional<Integer> insertOptional = Optional.of(anexo.getIdAnexo());
		if(insertOptional.isPresent()){
			return criar(anexo, insertOptional);
		}else{
			return atualizar(anexo, insertOptional);
		}
	}
	
	public int criar(Anexo anexo, Optional<Integer> insertOptional) throws SQLException{
		try(Connection conn = ConnectionDAO.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO anexos(idAta, ordem, descricao, arquivo) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);){
			popularQuery(anexo, insertOptional, stmt);
			stmt.execute();
			return extrairResultado(anexo, insertOptional, stmt);
		}
		
	}

	public int atualizar(Anexo anexo, Optional<Integer> insertOptional) throws SQLException{
		try(Connection conn = ConnectionDAO.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE anexos SET idAta=?, ordem=?, descricao=?, arquivo=? WHERE idAnexo=?")){
			popularQuery(anexo, insertOptional, stmt);
			stmt.execute();
			return extrairResultado(anexo, insertOptional, stmt);
		}
	}
	
	private void popularQuery(Anexo anexo, Optional<Integer> insertOptional, PreparedStatement stmt) throws SQLException {
		stmt.setInt(1, anexo.getAta().getIdAta());
		stmt.setInt(2, anexo.getOrdem());
		stmt.setString(3, anexo.getDescricao());
		stmt.setBytes(4, anexo.getArquivo());
		
		if(!insertOptional.isPresent()){
			stmt.setInt(5, anexo.getIdAnexo());
		}
	}
	
	private int extrairResultado(Anexo anexo, Optional<Integer> insertOptional, PreparedStatement stmt) throws SQLException {
		if(insertOptional.isPresent()){
			try(ResultSet rs = stmt.getGeneratedKeys();){
				if(rs.next()){
					anexo.setIdAnexo(rs.getInt(1));
				}
			}
		}
		return anexo.getIdAnexo();
	}
	
	public void excluir(int id) throws SQLException{
		
		try( Connection conn = ConnectionDAO.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM anexos WHERE idanexo=" + String.valueOf(id));){		
		}
	}
	
	private Anexo carregarObjeto(ResultSet rs) throws SQLException{
		Anexo anexo = new Anexo();
		
		anexo.setIdAnexo(rs.getInt("idAnexo"));
		anexo.getAta().setIdAta(rs.getInt("idAta"));
		anexo.setDescricao(rs.getString("descricao"));
		anexo.setOrdem(rs.getInt("ordem"));
		anexo.setArquivo(rs.getBytes("arquivo"));
		
		return anexo;
	}

}
