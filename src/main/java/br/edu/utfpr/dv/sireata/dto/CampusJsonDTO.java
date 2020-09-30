package br.edu.utfpr.dv.sireata.dto;

public class CampusJsonDTO {
	
	private int codigo;
	private String nome;
	
	public CampusJsonDTO() {
		this.setCodigo(0);
		this.setNome("");
	}
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
