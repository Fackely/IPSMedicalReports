package com.princetonsa.mundo;

import java.io.Serializable;
import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.Answer;
import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/*
* Tipo Modificacion: Segun incidencia 7055
* usuario: jesrioro
* Fecha: 30/05/2013
* Descripcion: Se  implementa  serializable  para  poder generar  el  reporte  de  HC  en  el  contexto de  reportes            
*/
public class CuentasPaciente implements Serializable
{	
	//Atributos 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8750946687189436352L;

	/**
	 * String codigo via Ingreso
	 * */	
	private String codigoViaIngreso;
	
	/**
	 * String descripcion Via Ingreso
	 * */
	private String descripcionViaIngreso;
	
	/**
	 * String codigo tipo paciente
	 * */
	private String codigoTipoPaciente;
	
	/**
	 * String descripcion tipo paciente
	 * */
	private String descripcionTipoPaciente;
	
	/**
	 * String codigo cuenta
	 * */
	private String codigoCuenta;
	
	/**
	 * String estado de la cuenta
	 * */
	private String estadoCuenta;
	
	/**
	 * String descripcion cuenta
	 * */
	private String descripcionEstadoCuenta;
	
	/**
	 * String codigoIngreso
	 * */
	private String codigoIngreso;
	
	
	private String codigoInstitucion="";
	private String [] idsCuenta=null;
	private int codigoPaciente=0;
	
	
	//Metodos
	//*****************************************************************************************
	public CuentasPaciente ()
	{
		this.codigoCuenta = ConstantesBD.codigoNuncaValido + "";
		this.estadoCuenta = ConstantesBD.codigoNuncaValido + "";
		this.descripcionEstadoCuenta = "";
		this.codigoTipoPaciente = "";
		this.descripcionTipoPaciente = "";
		this.codigoIngreso = ConstantesBD.codigoNuncaValido + "";
		this.codigoViaIngreso = ConstantesBD.codigoNuncaValido + "";
		this.descripcionViaIngreso = "";
	}
	
	//*****************************************************************************************	
	public CuentasPaciente (int codigoPaciente,String codigoInstitucion)
	{
		this.codigoPaciente=codigoPaciente;
		this.codigoInstitucion=codigoInstitucion; 
	}	
	//*****************************************************************************************
	/**
	 * Carga la informacion de las cuentas del paciente
	 * @param Connection con
	 * @param String tipoBD
	 * */
	public String[] cargarCuentasPaciente(Connection con, String tipoBD) throws SQLException
	{

		TagDao tagDao;
					
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		tagDao = myFactory.getTagDao();
		//Consulta si la institución pudiera ver las cuentas de todas las instituciones que le han dado
		//permiso
		//"SELECT id from cuentas where id_ingreso IN (SELECT id from ingresos where institucion IN (SELECT codigo_institucion_duena from pacientes_instituciones2 where codigo_institucion_permitida =" + codigoInstitucion + " and tipo_identificacion_paciente='" + codigoTipoIdentificacionPaciente + "' and numero_identificacion_paciente = '" + numeroIdentificacionPaciente + "') and tipo_identificacion_paciente='CC' and numero_identificacion_paciente = '" + numeroIdentificacionPaciente + "' )"
		//Como solo se pueden ver los que hayan sido creados en esta institución para este paciente, la consulta es:
		String cargarCuentasPaciente="SELECT id from cuentas where id_ingreso IN (SELECT id from ingresos where codigo_paciente =" + codigoPaciente + " and institucion=" + codigoInstitucion + ")";
		
		Answer a=tagDao.resultadoConsulta(con, cargarCuentasPaciente);
		ResultSetDecorator rs=a.getResultSet();
		String temporal="";
		boolean primeraVez=true;
		
		int numResultados=0;
		
		while (rs.next())
		{
			if (primeraVez)
			{
				primeraVez=false;
				temporal=rs.getString("id");
			}
			else
			{
				temporal=temporal + "-" +rs.getString("id");
			}
			numResultados++;
		}

		if (numResultados==0)
			return null;
		else if (numResultados==1)
		{
			String res[]=new String[1];
			res[0]=temporal;
		}
		else
		{
			return UtilidadTexto.separarNombresDeCodigos(temporal, (numResultados-1));
		}
		
		return temporal.split("-");
	}


	/**
	 * @return the codigoCuenta
	 */
	public String getCodigoCuenta() {
		return codigoCuenta;
	}

	/**
	 * @param codigoCuenta the codigoCuenta to set
	 */
	public void setCodigoCuenta(String codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}

	/**
	 * @return the codigoIngreso
	 */
	public String getCodigoIngreso() {
		return codigoIngreso;
	}

	/**
	 * @param codigoIngreso the codigoIngreso to set
	 */
	public void setCodigoIngreso(String codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
	}

	/**
	 * @return the codigoTipoPaciente
	 */
	public String getCodigoTipoPaciente() {
		return codigoTipoPaciente;
	}

	/**
	 * @param codigoTipoPaciente the codigoTipoPaciente to set
	 */
	public void setCodigoTipoPaciente(String codigoTipoPaciente) {
		this.codigoTipoPaciente = codigoTipoPaciente;
	}

	/**
	 * @return the codigoViaIngreso
	 */
	public String getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(String codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	/**
	 * @return the descripcionTipoPaciente
	 */
	public String getDescripcionTipoPaciente() {
		return descripcionTipoPaciente;
	}

	/**
	 * @param descripcionTipoPaciente the descripcionTipoPaciente to set
	 */
	public void setDescripcionTipoPaciente(String descripcionTipoPaciente) {
		this.descripcionTipoPaciente = descripcionTipoPaciente;
	}

	/**
	 * @return the descripcionViaIngreso
	 */
	public String getDescripcionViaIngreso() {
		return descripcionViaIngreso;
	}

	/**
	 * @param descripcionViaIngreso the descripcionViaIngreso to set
	 */
	public void setDescripcionViaIngreso(String descripcionViaIngreso) {
		this.descripcionViaIngreso = descripcionViaIngreso;
	}

	/**
	 * @return the estadoCuenta
	 */
	public String getEstadoCuenta() {
		return estadoCuenta;
	}

	/**
	 * @param estadoCuenta the estadoCuenta to set
	 */
	public void setEstadoCuenta(String estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}	

	/**
	 * @return the descripcionEstadoCuenta
	 */
	public String getDescripcionEstadoCuenta() {
		return descripcionEstadoCuenta;
	}

	/**
	 * @param descripcionEstadoCuenta the descripcionEstadoCuenta to set
	 */
	public void setDescripcionEstadoCuenta(String descripcionEstadoCuenta) {
		this.descripcionEstadoCuenta = descripcionEstadoCuenta;
	}	
}