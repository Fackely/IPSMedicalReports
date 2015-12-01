/**
 * Juan David Ramírez 14/06/2006
 * Princeton S.A.
 */
package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.SubirPacienteDao;
import com.servinte.axioma.dto.capitacion.DtoUsuariosXConvenio;

/**
 * @author Juan David Ramírez
 *
 */
public class SubirPaciente
{
	SubirPacienteDao subirPacienteDao;
	
	/**
	 * Fecha inicial de cargue
	 */
	private String fechaInicial;

	/**
	 * Fecha final de cargue
	 */
	private String fechaFinal;

	/**
	 * Tipo de cargue
	 */
	private int tipoCargue;
	
	/**
	 * Usuario que realizó el cargue
	 */
	private String usuario;
	
	/**
	 * Inicializar la conexión con la BD
	 *
	 */
	public void init()
	{
		subirPacienteDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSubirPacienteDao();
	}
	
	/**
	 * Método para resetear la clase
	 */
	private void reset()
	{
		fechaInicial="";
		fechaFinal="";
		tipoCargue=0;
		usuario="";
	}
	
	/**
	 * 
	 */
	public SubirPaciente()
	{
		this.init();
		this.reset();
	}
	
	/**
	 * Método para consultar las opciones de selección de la funcionalidad
	 * @param con
	 * @param tipoConsulta
	 * @param institucion
	 * @param codigoConvenio @todo
	 * @return
	 */
	public static Collection consultarTipos(Connection con, int tipoConsulta, int institucion, int codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSubirPacienteDao().consultarTipos(con, tipoConsulta, institucion, codigoConvenio);
	}
	
	public static Collection consultarGruposEtareos(Connection con, int institucion, int codigoConvenio, String fechaInicial, String fechaFinal)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSubirPacienteDao().consultarGruposEtareos(con, institucion, codigoConvenio, fechaInicial, fechaFinal);
	}
	
	/**
	 * Ingresar el usuario por convenio
	 * @param con
	 * @param contrato
	 * @param codigoPersona
	 * @param tipoAfiliado 
	 * @return
	 */
	public int ingresarUsuarioXConvenio(Connection con, DtoUsuariosXConvenio datosCapitacion)
	{
		return subirPacienteDao.ingresarUsuarioXConvenio(con, datosCapitacion);
	}
	
	/**
	 * Método para ingresar los suarios capitados
	 * y el convenios para los mismos
	 * @param con
	 * @param contrato
	 * @param codigoPersona
	 * @param numeroIdentificacion
	 * @param tipoIdentificacion
	 * @param fechaNacimiento
	 * @param sexo
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @param direccion
	 * @param telefono
	 * @param email
	 * @param numeroFicha 
	 * @param clasificacionSE 
	 * @param convenio 
	 * @return
	 */
	public int insertarUsuarioCapitado(Connection con, String numeroIdentificacion, String tipoIdentificacion, String fechaNacimiento, int sexo, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String direccion, String telefono, String email, String numeroFicha,
									String tipoIdEmpleador,	String numIdEmpleador,String razonSociEmpleador,String tipoIdCotizante,String numIdCotizante,String nombresCotizante,String apellidosCotizante,int parentesco,int centroAtencion,String tipoAfiliado,String excepcionMonto,String pais,String departamento,
									String municipio,String localidad,String barrio)
	{
		return subirPacienteDao.insertarUsuarioCapitado(con, numeroIdentificacion, tipoIdentificacion, fechaNacimiento, sexo, primerNombre, segundoNombre, primerApellido, segundoApellido, direccion, telefono, email,numeroFicha,
				tipoIdEmpleador,numIdEmpleador,razonSociEmpleador,tipoIdCotizante,numIdCotizante,nombresCotizante,apellidosCotizante,parentesco,centroAtencion,tipoAfiliado,excepcionMonto,pais,departamento,municipio,localidad, barrio);
	}
	
	public int ingresarUsuarioCapitadoPeriodo(Connection con, int codUsuarioCapitado, int contrato, String convenio, String clasificacionSE, String codigoTipoAfiliado)
	{
		return subirPacienteDao.ingresarConvUsuarioCapitado(con, contrato, fechaInicial, fechaFinal, codUsuarioCapitado, tipoCargue, convenio, clasificacionSE,codigoTipoAfiliado);
	}
	
	public static int consultarCodigoUsuarioCapitado(Connection con, String tipoIdentificacion, String numeroIdentificacion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSubirPacienteDao().consultarCodigoUsuarioCapitado(con, tipoIdentificacion, numeroIdentificacion);
	}

	public static HashMap consultarUsuarioCapitado(Connection con, String tipoIdentificacion, String numeroIdentificacion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSubirPacienteDao().consultarUsuarioCapitado(con, tipoIdentificacion, numeroIdentificacion);
	}

	/**
	 * Metodo para ingresar el en la tabla capitacion.contrato_cargue
	 * @param con
	 * @param contrato
	 * @param totalPacientes
	 * @param upc
	 * @param tipoPago
	 * @return número Elementos ingresados
	 */
	public int ingresarContratoCargue(Connection con, int contrato, int totalPacientes, double upc, int tipoPago, double valorTotal) throws SQLException
	{
		int codigoContratoCargue = subirPacienteDao.consultarCodigoContratoCarguePeriodo(con, contrato, fechaInicial, fechaFinal); 
		if(codigoContratoCargue<=0)
			return subirPacienteDao.ingresarContratoCargue(con, contrato, totalPacientes, upc, fechaInicial, fechaFinal, tipoPago, valorTotal);
		else
		{
			subirPacienteDao.aumentarPacientesContratoCargue(con, codigoContratoCargue, totalPacientes, valorTotal);
			return codigoContratoCargue;
		}
	}

	/**
	 * Método para ingresar cargue grupo etareo
	 * @param con
	 * @param contratoCargue
	 * @param grupoEtareo
	 * @param totalPacientes
	 * @param upc
	 * @return
	 */
	public int guardarGrupoEtareoCargue(Connection con, int contratoCargue, int grupoEtareo, int totalPacientes, double upc)
	{
		int codigoCargueGrupoEtareo= subirPacienteDao.consultarCodigoCargueGrupoEtareo(con,contratoCargue, grupoEtareo); 
		if(codigoCargueGrupoEtareo==-1) // si no se habia ingresado ninguna información de cargue del grupo_etareo
		{
			return subirPacienteDao.ingresarGrupoEtareoCargue(con, contratoCargue, grupoEtareo, totalPacientes, upc);
		}
		else
			return subirPacienteDao.modificarGrupoEtareoCargue(con, codigoCargueGrupoEtareo, totalPacientes, upc);
	}

	/**
	 * Método para ingresar el log tipo BD del cargue
	 * @param con
	 * @param contrato
	 * @param registrosLeidos
	 * @param registrosGuardados
	 * @param loginUsuario
	 * @param archivoInconsistencias
	 */
	public boolean ingresarLogBD(Connection con, int contrato, int registrosLeidos, int registrosGuardados, String loginUsuario, String archivoInconsistencias)
	{
		return subirPacienteDao.ingresarLogBD(con, contrato, registrosLeidos, registrosGuardados, loginUsuario, archivoInconsistencias);
	}

	/**
	 * Método para consultar la existencia de usuarios capitados
	 * @param con
	 * @param codigoPersona
	 * @param contrato 
	 * @param convenio 
	 * @return
	 */
	public boolean existeUsuarioXConvenio(Connection con, int codigoPersona, int convenio, int contrato)
	{
		return subirPacienteDao.existeUsuarioXConvenio(con, codigoPersona, fechaInicial, fechaFinal,convenio,contrato);
	}
	
	/**
	 * Consulta si un usuario ya tiene cargue en el periodo indicado
	 * @param con
	 * @param codigoUsuarioCapitado
	 * @return
	 */
	public boolean existeUsuarioCapitadoPeriodo(Connection con, int codigoUsuarioCapitado, int convenio, int contrato)
	{
		return subirPacienteDao.existeUsuarioCapitadoPeriodo(con, codigoUsuarioCapitado, fechaInicial, fechaFinal,convenio,contrato);
	}
	
	/**
	 * Método que consulta un usuario capitado por tipo y número identificacion
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarUsuarioCapitado(Connection con,HashMap campos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSubirPacienteDao().consultarUsuarioCapitado(con,campos);
	}
	
	/**
	 * Método que elimina un usuario capitado 
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminarUsuarioCapitado(Connection con,HashMap campos)
	{
		return subirPacienteDao.eliminarUsuarioCapitado(con,campos);
	}
	
	/**
	 * Método implementado para consultar los datos del usuario x convenio
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public HashMap consultarUsuarioXConvenio(Connection con,String codigoPaciente)
	{
		return subirPacienteDao.consultarUsuarioXConvenio(con,codigoPaciente);
	}

	/**
	 * @return Retorna fechaFinal.
	 */
	public String getFechaFinal()
	{
		return fechaFinal;
	}

	/**
	 * @param fechaFinal Asigna fechaFinal.
	 */
	public void setFechaFinal(String fechaFinal)
	{
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return Retorna fechaInicial.
	 */
	public String getFechaInicial()
	{
		return fechaInicial;
	}

	/**
	 * @param fechaInicial Asigna fechaInicial.
	 */
	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return Retorna tipoCargue.
	 */
	public int getTipoCargue()
	{
		return tipoCargue;
	}

	/**
	 * @param tipoCargue Asigna tipoCargue.
	 */
	public void setTipoCargue(int tipoCargue)
	{
		this.tipoCargue = tipoCargue;
	}

	/**
	 * @return Retorna usuario.
	 */
	public String getUsuario()
	{
		return usuario;
	}

	/**
	 * @param usuario Asigna usuario.
	 */
	public void setUsuario(String usuario)
	{
		this.usuario = usuario;
	}

	/**
	 * 
	 * @param con
	 * @param mes
	 * @param anio
	 * @return
	 */
	public double obtenerValorUnidadPagoCapitacionMesAnio(Connection con, int mes, int anio)
	{
		return subirPacienteDao.obtenerValorUnidadPagoCapitacionMesAnio(con,mes,anio);
	}

	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static boolean existeConvenioCapitado(Connection con, String convenio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSubirPacienteDao().existeConvenioCapitado(con, convenio);

	}

	/**
	 * 
	 * @param con
	 * @param codigoConvenio 
	 * @param SE
	 * @return
	 */
	public static boolean existeClasificacionSocioEconomica(Connection con, String clasificacionSE, int codigoConvenio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSubirPacienteDao().existeClasificacionSocioEconomica(con, clasificacionSE,codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static int codigoEmpresaConvenio(Connection con, int convenio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSubirPacienteDao().codigoEmpresaConvenio(con, convenio);
	}
	
	
	/**
	 * Método implementado para remover las comillas de los datos del usuario capitado
	 * Key's del Mapa : primer_apellido_0,segundo_apellido_0,primer_nombre_0,segundo_nombre_0,direccion_0,email_0,telefono_0
	 * @param mapa 
	 * @return
	 */
	public static HashMap removerComillasDatosUsuarioCapitado(HashMap mapa) 
	{
		String aux = mapa.get("primer_apellido_0").toString();
		String nuevaAux = "";
		for(int i=0;i<aux.length();i++)
			if(aux.charAt(i)!='"')
				nuevaAux += aux.charAt(i);
		mapa.put("primer_apellido_0",nuevaAux);
		
		aux = mapa.get("segundo_apellido_0").toString();
		nuevaAux = "";
		for(int i=0;i<aux.length();i++)
			if(aux.charAt(i)!='"')
				nuevaAux += aux.charAt(i);
		mapa.put("segundo_apellido_0",nuevaAux);
		
		aux = mapa.get("primer_nombre_0").toString();
		nuevaAux = "";
		for(int i=0;i<aux.length();i++)
			if(aux.charAt(i)!='"')
				nuevaAux += aux.charAt(i);
		mapa.put("primer_nombre_0",nuevaAux);
		
		aux = mapa.get("segundo_nombre_0").toString();
		nuevaAux = "";
		for(int i=0;i<aux.length();i++)
			if(aux.charAt(i)!='"')
				nuevaAux += aux.charAt(i);
		mapa.put("segundo_nombre_0",nuevaAux);
		
		aux = mapa.get("direccion_0").toString();
		nuevaAux = "";
		for(int i=0;i<aux.length();i++)
			if(aux.charAt(i)!='"')
				nuevaAux += aux.charAt(i);
		mapa.put("direccion_0",nuevaAux);
		
		aux = mapa.get("email_0").toString();
		nuevaAux = "";
		for(int i=0;i<aux.length();i++)
			if(aux.charAt(i)!='"')
				nuevaAux += aux.charAt(i);
		mapa.put("email_0",nuevaAux);
		
		aux = mapa.get("telefono_0").toString();
		nuevaAux = "";
		for(int i=0;i<aux.length();i++)
			if(aux.charAt(i)!='"')
				nuevaAux += aux.charAt(i);
		mapa.put("telefono_0",nuevaAux);
		
		return mapa;
	}
	
	/**
	 * 
	 */
	public int obtenerCodigoPersona(Connection con, String tipoID,String numeroID) 
	{
		return subirPacienteDao.obtenerCodigoPersona(con,tipoID,numeroID);
	}
	
}