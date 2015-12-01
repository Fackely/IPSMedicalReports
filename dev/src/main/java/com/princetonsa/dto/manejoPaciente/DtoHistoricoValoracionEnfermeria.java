/**
 * 
 */
package com.princetonsa.dto.manejoPaciente;

import java.util.ArrayList;

import util.ConstantesBD;

/**
 * @author axioma
 *
 */
public class DtoHistoricoValoracionEnfermeria 
{
	/**
	 * 
	 */
	private int codigoHistoEnca;
	
	/**
	 * 
	 */
	private String fecha;
	
	/**
	 * 
	 */
	private String hora;
	
	/**
	 * 
	 */
	private String loginUsuario;
	
	/**
	 * 
	 */
	private String nombreUsuario;

	/**
	 * 
	 */
	private ArrayList<DtoValoracionEnfermeria> resultados;
	
	/**
	 * 
	 */
	public DtoHistoricoValoracionEnfermeria()
	{
		this.codigoHistoEnca=ConstantesBD.codigoNuncaValido;
		this.fecha="";
		this.hora="";
		this.loginUsuario="";
		this.nombreUsuario="";
		this.resultados=new ArrayList<DtoValoracionEnfermeria>();
	}
	
	
	

	public int getCodigoHistoEnca() {
		return codigoHistoEnca;
	}

	public void setCodigoHistoEnca(int codigoHistoEnca) {
		this.codigoHistoEnca = codigoHistoEnca;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public ArrayList<DtoValoracionEnfermeria> getResultados() {
		return resultados;
	}

	public void setResultados(ArrayList<DtoValoracionEnfermeria> resultados) {
		this.resultados = resultados;
	}

}
