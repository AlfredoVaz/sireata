package br.edu.utfpr.dv.sireata.dto;

import br.edu.utfpr.dv.sireata.model.Ata.TipoAta;

public class AtaJsonDTO {
	private TipoAta tipo;
	private int numero;
	private int ano;
	private String data;
	
	public AtaJsonDTO() {
		this.setTipo(TipoAta.ORDINARIA);
		this.setNumero(0);
		this.setAno(0);
		this.setData("");
	}
	
	public TipoAta getTipo() {
		return tipo;
	}
	public void setTipo(TipoAta tipo) {
		this.tipo = tipo;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}

