package com.princetonsa.mundo.interfaz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.ParamInterfazSistema1EDao;
import com.princetonsa.dto.interfaz.DtoConceptosParam1E;
import com.princetonsa.dto.interfaz.DtoEventosParam1E;
import com.princetonsa.dto.interfaz.DtoInterfazParamContaS1E;
import com.princetonsa.dto.interfaz.DtoLogParamGenerales1E;
import com.princetonsa.dto.interfaz.DtoTiposInterfazDocumentosParam1E;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ConsultarImprimirGlosas;
import com.servinte.axioma.dao.impl.administracion.CentroCostosDAO;
import com.servinte.axioma.dao.interfaz.administracion.ICentroCostosDAO;
import com.servinte.axioma.hibernate.HibernateUtil;

public class ParamInterfazSistema1E{
	
	//---------------------ATRIBUTOS
	/**
	 * Para manejo de Logs
	 */
	private static Logger logger = Logger.getLogger(ConsultarImprimirGlosas.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ParamInterfazSistema1EDao paramInterfazSistema1EDao;
	
	/**
	 * Codigo de la institucion
	 */
	private int institucion;
	
	
	// ----------------	METODOS
	
	/**
	 * Reset
	 */
	private void reset() 
	{
		this.institucion = ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			paramInterfazSistema1EDao = myFactory.getParamInterfazSistema1EDao();
			wasInited = (paramInterfazSistema1EDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @return
	 */
	private static ParamInterfazSistema1EDao getParamInterfazSistema1EDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getParamInterfazSistema1EDao();
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static boolean ingresarParamGenerales(Connection con, DtoInterfazParamContaS1E dtoInterfazSis1E)
	{
		return getParamInterfazSistema1EDao().ingresarParamGenerales(con, dtoInterfazSis1E);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static DtoInterfazParamContaS1E consultarParamGenerales(Connection con)
	{
		HashMap parametros = new HashMap();
		return getParamInterfazSistema1EDao().consultarParamGenerales(con,parametros);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static DtoInterfazParamContaS1E consultarParamGenerales(Connection con,String tipoDto)
	{
		HashMap parametros = new HashMap();
		parametros.put("tipoDto", tipoDto);
		return getParamInterfazSistema1EDao().consultarParamGenerales(con,parametros);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static boolean actualizarParamGenerales(Connection con, DtoInterfazParamContaS1E dtoInterfazSis1E)
	{
		return getParamInterfazSistema1EDao().actualizarParamGenerales(con, dtoInterfazSis1E);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static boolean ingresarTiposDoc(Connection con, DtoTiposInterfazDocumentosParam1E dto)
	{
		return getParamInterfazSistema1EDao().ingresarTiposDoc(con, dto);
	}
	
	public static ArrayList<DtoTiposInterfazDocumentosParam1E> consultarTiposDocs(Connection con)
	{
		return getParamInterfazSistema1EDao().consultarTiposDocs(con);
	}
	
	public static ArrayList consultarTiposDoc1E(Connection con)
	{
		return getParamInterfazSistema1EDao().consultarTiposDoc1E(con);
	}
	
	public static boolean eliminarTiposDoc(Connection con,String indice)
	{
		return getParamInterfazSistema1EDao().eliminarTiposDoc(con, indice);
	}
	
	public static DtoTiposInterfazDocumentosParam1E consultarInfoTipoDocUnitario(Connection con, String indice)
	{
		return getParamInterfazSistema1EDao().consultarInfoTipoDocUnitario(con,indice);
	}
	
	public static boolean actualizarDocParam(Connection con, String indice, DtoTiposInterfazDocumentosParam1E dto)
	{
		return getParamInterfazSistema1EDao().actualizarDocParam(con,indice,dto);
	}
	
	public static boolean insertarLog(Connection con, DtoLogParamGenerales1E dto)
	{
		return getParamInterfazSistema1EDao().insertarLog(con,dto);
	}
	
	/**
	 * Consulta Listado Concepto Param 1E 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoConceptosParam1E> cargarConceptosparam1E(Connection con, int param_generales_1e)
	{
		HashMap parametros = new HashMap();
		parametros.put("param_generales_1e", param_generales_1e);
		return getParamInterfazSistema1EDao().cargarConceptosparam1E(con, parametros);
	}
	
	/**
	 * Guarda los Concepto Param 1E
	 * @param con
	 * @param array
	 * @return
	 */
	public static int guardarConceptosParam1E(Connection con, UsuarioBasico usuario, ArrayList<DtoConceptosParam1E> array, int param_general_1e, String seccion)
	{
		boolean transacion = true;
		UtilidadBD.iniciarTransaccion(con);
		String[] aux;
		for(int i=0;i<array.size();i++)
		{
			DtoConceptosParam1E dto = (DtoConceptosParam1E) array.get(i);
			if(dto.getIngresar().equals(ConstantesBD.acronimoSi) 
					&& dto.getEliminar().equals(ConstantesBD.acronimoNo))
			{
				dto.setCodigoParamGeneral1E(param_general_1e);
				dto.setSeccion(seccion);
				dto.setUsuarioModifica(usuario.getLoginUsuario());
				dto.setActivo(ConstantesBD.acronimoSi);
				if(getParamInterfazSistema1EDao().insertarConceptoParam1E(con, dto)<=0)
					transacion = false;
			}
			
		}
		if(transacion)
		{
			UtilidadBD.finalizarTransaccion(con);
			return 0;
		}else{
			UtilidadBD.abortarTransaccion(con);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * Inactivacion Concepto Param 1E
	 * @param con
	 * @param DtoConceptosParam1E
	 * @return
	 */
	public static int inactivarConceptoParam1E(Connection con, DtoConceptosParam1E dto)
	{
		return getParamInterfazSistema1EDao().inactivarConceptoParam1E(con, dto);
	}
	
	public static ArrayList consultarEventos(Connection con)
	{
		return getParamInterfazSistema1EDao().consultarEventos(con);
	}
	
	public static boolean insertarEventosParam1E(Connection con, DtoEventosParam1E dto)
	{
		return getParamInterfazSistema1EDao().insertarEventosParam1E(con,dto);
	}
	
	public static ArrayList<DtoEventosParam1E> consultarEventosParam1E(Connection con,String consecutivo)
	{
		return getParamInterfazSistema1EDao().consultarEventosParam1E(con,consecutivo);
	}
	
	public static boolean inactivarEvento(Connection con, DtoEventosParam1E dto)
	{
		return getParamInterfazSistema1EDao().inactivarEvento(con,dto);
	}
	
	public static boolean validarEventoUnico(Connection con, DtoEventosParam1E dto)
	{
		return getParamInterfazSistema1EDao().validarEventoUnico(con,dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap<String, Object> getTiposConseTipDoc(Connection con, String tipo_doc)
	{
		HashMap parametros = new HashMap(); 
		parametros.put("tipo_doc", tipo_doc);
		return getParamInterfazSistema1EDao().getTiposConseTipDoc(con, parametros);
	}
	
	/**
	 * M&eacute;todo que permite realizar la consulta de los centros de costo por tipo de área y estado activo
	 * @param tipoArea
	 * @param estado
	 * @return ArrayList
	 */	
	public static ArrayList<DtoCentroCosto> listaCentroCostoTipoArea(int tipoArea, boolean estado) {
		HibernateUtil.beginTransaction();
		ICentroCostosDAO centroCostoDAO = new CentroCostosDAO();
		ArrayList<DtoCentroCosto> listaCentrosCosto = centroCostoDAO.listaCentroCostoTipoArea(tipoArea, estado);
		HibernateUtil.endTransaction();
		return listaCentrosCosto;
	}
}