package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.ConstantesBD;
import util.Utilidades;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Parametros de entrada para generar la factura odontologica
 * 
 * @author Wilson Rios 
 *
 * Apr 28, 2010 - 9:17:52 AM
 */
public class DtoParametroFacturaAutomatica implements Serializable 
{
	/**
	 * serail
	 */
	private static final long serialVersionUID = 449078541764389086L;

	/**
	 * Coneccion de la BD, viene  transaccional desde la atencion de la cita
	 * 
	 */
	private Connection con; 
	
	/**
	 * lista de los cargos que se van ha facturar (FILTRADO)
	 */
	private ArrayList<BigDecimal> listaCargosPk;
	
	/**
	 * usuario que esta generando el proceso,
	 * tambien carga informacion fundamental de la
	 * institucion y el centro de atencion
	 */
	private UsuarioBasico usuario;
	
	/**
	 * Paciente al cual se esta facturando
	 */
	private PersonaBasica paciente;

	/**
	 * identificador unico x transacion realizada,
	 * usado en la tabla cuentas proceso facturacion 
	 */
	private String idSession;
	
	/**
	 * Codigo de la institución cargada en sesión.
	 */
	private int codigoInstitucion;
	
	
	
	/**
	 * 
	 * Constructor de la clase
	 * @param con
	 * @param listaCargosPk
	 * @param usuario
	 * @param paciente
	 */
	public DtoParametroFacturaAutomatica(	Connection con,
											ArrayList<BigDecimal> listaCargosPk, 
											UsuarioBasico usuario,
											PersonaBasica paciente,
											String idSession) 
	{
		super();
		this.con = con;
		this.listaCargosPk = listaCargosPk;
		this.usuario = usuario;
		this.paciente = paciente;
		this.idSession= idSession;
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @return the con
	 */
	public Connection getCon() {
		return con;
	}

	/**
	 * @param con the con to set
	 */
	public void setCon(Connection con) {
		this.con = con;
	}

	/**
	 * @return the listaCargosPk
	 */
	public ArrayList<BigDecimal> getListaCargosPk() {
		return listaCargosPk;
	}

	/**
	 * @param listaCargosPk the listaCargosPk to set
	 */
	public void setListaCargosPk(ArrayList<BigDecimal> listaCargosPk) {
		this.listaCargosPk = listaCargosPk;
	}

	/**
	 * @return the usuario
	 */
	public UsuarioBasico getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(UsuarioBasico usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the paciente
	 */
	public PersonaBasica getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(PersonaBasica paciente) {
		this.paciente = paciente;
	}

	/**
	 * @return the idSession
	 */
	public String getIdSession() {
		return idSession;
	}

	/**
	 * @param idSession the idSession to set
	 */
	public void setIdSession(String idSession) {
		this.idSession = idSession;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}
	
	
}
