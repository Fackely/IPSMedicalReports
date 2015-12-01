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
public class DtoHistoricoResultadoLaboratorios 
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
	private ArrayList<DtoResultadoLaboratorio> resultados;

	/**
	 * 
	 */
	public DtoHistoricoResultadoLaboratorios()
	{
		this.nombreUsuario="";
		this.fecha="";
		this.hora="";
		this.loginUsuario="";
		this.resultados=new ArrayList<DtoResultadoLaboratorio>();
		this.codigoHistoEnca=ConstantesBD.codigoNuncaValido;
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

	public ArrayList<DtoResultadoLaboratorio> getResultados() {
		return resultados;
	}

	public void setResultados(ArrayList<DtoResultadoLaboratorio> resultados) {
		this.resultados = resultados;
	}

	public int getCodigoHistoEnca() {
		return codigoHistoEnca;
	}

	public void setCodigoHistoEnca(int codigoHistoEnca) {
		this.codigoHistoEnca = codigoHistoEnca;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	
}
