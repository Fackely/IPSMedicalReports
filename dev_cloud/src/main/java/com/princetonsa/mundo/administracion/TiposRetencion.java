package com.princetonsa.mundo.administracion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.administracion.EspecialidadesDao;
import com.princetonsa.dao.administracion.TiposRetencionDao;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseTiposRetencionDao;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionClaseInv;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionConceptoFV;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionGrupoSer;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.administracion.DtoLogTipoRetencion;
import com.princetonsa.dto.administracion.DtoTiposRetencion;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Víctor Hugo Gómez L.
 */

public class TiposRetencion 
{
	/**
	 * Manejador de mensajes
	 */
	private static Logger logger = Logger.getLogger(TiposRetencion.class);
	
	/**
	 * ArrayList Tipos de Retencion
	 */
	private static ArrayList<DtoTiposRetencion> dtoTiposRetencion = new ArrayList<DtoTiposRetencion>();
	
	/**
	 * instancia del DAO
	 * @return
	 */
	public static TiposRetencionDao getTiposRetencionDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTiposRetencionDao();
	}
	
	/**
	 * Constructor
	 */
	public TiposRetencion()
	{
		this.dtoTiposRetencion = new ArrayList<DtoTiposRetencion>();
	}
	
	/**
	 * Consulta Listado de Tipos de Retencion
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoTiposRetencion> cargarTiposRetencion(Connection con, int institucion)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion", institucion);
		return getTiposRetencionDao().cargarTiposRetencion(con, parametros);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTipoRetencion
	 * @return
	 */
	public static int insertarTipoRetencion(Connection con, DtoTiposRetencion dtoTipoRetencion)
	{
		return getTiposRetencionDao().insertarTipoRetencion(con, dtoTipoRetencion);
	}

	/**
	 * 
	 * @param con
	 * @param dtoTipoRetencion
	 * @return
	 */
	public static int updateTipoRetencion(Connection con, DtoTiposRetencion dtoTipoRetencion)
	{
		return getTiposRetencionDao().updateTipoRetencion(con, dtoTipoRetencion);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoLogTipoRetencion
	 * @return
	 */
	public static int insertarLogTipoRetencion(Connection con, DtoLogTipoRetencion dtoLogTipoRetencion)
	{
		return getTiposRetencionDao().insertarLogTipoRetencion(con, dtoLogTipoRetencion);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTipoRetencion
	 * @return
	 */
	public static int deleteTipoRetencion(Connection con, DtoTiposRetencion dtoTipoRetencion)
	{
		return getTiposRetencionDao().deleteTipoRetencion(con, dtoTipoRetencion);
	}
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @return
	 */
	public static int grabarCambiosTiposRetencion(Connection con, UsuarioBasico usuario)
	{
		UtilidadBD.iniciarTransaccion(con);
		boolean transacion = true;
		for(int i=0;i<dtoTiposRetencion.size();i++)
		{
			if(dtoTiposRetencion.get(i).getModificar().equals(ConstantesBD.acronimoSi)
					&& dtoTiposRetencion.get(i).getIngresar().equals(ConstantesBD.acronimoNo))
			{// se modifica el registro y se ingresa el viejo registro al log
				
				if(insertarLogTipoRetencion(con, dtoTiposRetencion.get(i).getDtoLogTipoRetencion())!=ConstantesBD.codigoNuncaValido)
				{
					if(updateTipoRetencion(con, dtoTiposRetencion.get(i))<=0)
					{
						transacion =false;
					}
				}else{
					transacion = false;
				}
			}else{
				if(dtoTiposRetencion.get(i).getIngresar().equals(ConstantesBD.acronimoSi)
						&& dtoTiposRetencion.get(i).getEliminar().equals(ConstantesBD.acronimoNo))
				{// se ingresa un uevo registo en tipos retencion
					dtoTiposRetencion.get(i).setCodigoInstitucion(usuario.getCodigoInstitucionInt());
					dtoTiposRetencion.get(i).setUsuarioModificacion(usuario.getLoginUsuario());
					dtoTiposRetencion.get(i).setActivo(ConstantesBD.acronimoSi);
					if(insertarTipoRetencion(con, dtoTiposRetencion.get(i))==ConstantesBD.codigoNuncaValido)
						transacion = false;
				}
			}
		}
		
		if(transacion){
			UtilidadBD.finalizarTransaccion(con);
			return 1;
		}else{
			UtilidadBD.abortarTransaccion(con);
			return ConstantesBD.codigoNuncaValido;
		}
	}

	/**
	 * 
	 * @param con
	 * @param usuario
	 * @return
	 */
	public static int inactivarTipoRetencion(Connection con, UsuarioBasico usuario, DtoTiposRetencion dto)
	{
		UtilidadBD.iniciarTransaccion(con);
		boolean transacion = true;
		int tipo_retencion = dto.getConsecutivo();
		if(verificacionConceptoRetencion(con, tipo_retencion))
		{
			if(verificacionGrupoSer(con, tipo_retencion))
			{
				if(verificacionClaseInv(con, tipo_retencion))
				{
					if(verificacionConcFacturaVaria(con, tipo_retencion))
					{
						dto.setUsuarioAnulacion(usuario.getLoginUsuario());
						if(deleteTipoRetencion(con, dto)==ConstantesBD.codigoNuncaValido)
							transacion = false;
					}else
						transacion = false;
				}else
					transacion = false;
			}else
				transacion = false;
		}else{
			transacion = false;
		}
		
		if(transacion){
			UtilidadBD.finalizarTransaccion(con);
			return 1;
		}else{
			UtilidadBD.abortarTransaccion(con);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * @return the dtoTiposRetencion
	 */
	public static ArrayList<DtoTiposRetencion> getDtoTiposRetencion() {
		return dtoTiposRetencion;
	}

	/**
	 * @param dtoTiposRetencion the dtoTiposRetencion to set
	 */
	public static void setDtoTiposRetencion(
			ArrayList<DtoTiposRetencion> dtoTiposRetencion) {
		TiposRetencion.dtoTiposRetencion = dtoTiposRetencion;
	}
	
	/**
	 * Verificacion Concepto retencion
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean verificacionConceptoRetencion(Connection con, int tipo_retencion)
	{
		HashMap parametros = new HashMap();
		parametros.put("tipo_retencion", tipo_retencion);
		return getTiposRetencionDao().verificacionConceptoRetencion(con, parametros);
	}
	
	/**
	 * Verificacion Tipo Retencion Clase Inventario
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean verificacionClaseInv(Connection con, int tipo_retencion)
	{
		HashMap parametros = new HashMap();
		parametros.put("tipo_retencion", tipo_retencion);
		return getTiposRetencionDao().verificacionClaseInv(con, parametros);
	}
	
	/**
	 * Verificacion Tipo Retencion Clase Inventario
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean verificacionGrupoSer(Connection con, int tipo_retencion)
	{
		HashMap parametros = new HashMap();
		parametros.put("tipo_retencion", tipo_retencion);
		return getTiposRetencionDao().verificacionGrupoSer(con, parametros);
	}
	
	/**
	 * Verificacion Tipo Retencion Clase Inventario
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean verificacionConcFacturaVaria(Connection con, int tipo_retencion)
	{
		HashMap parametros = new HashMap();
		parametros.put("tipo_retencion", tipo_retencion);
		return getTiposRetencionDao().verificacionConcFacturaVaria(con, parametros);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTRGrupoSer
	 * @return
	 */
	public static int insertarTRGrupoServicio(Connection con, DtoDetTiposRetencionGrupoSer dtoTRGrupoSer)
	{
		return getTiposRetencionDao().insertarTRGrupoServicio(con, dtoTRGrupoSer);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public static int insertarTRClaseInventario(Connection con, DtoDetTiposRetencionClaseInv dtoTRClaseInv)
	{
		return getTiposRetencionDao().insertarTRClaseInventario(con, dtoTRClaseInv);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public static int insertarTRConceptoFraVaria(Connection con, DtoDetTiposRetencionConceptoFV dtoTRConcFV)
	{
		return getTiposRetencionDao().insertarTRConceptoFraVaria(con, dtoTRConcFV);
	}
	
	/**
	 * Inactivacion de Tipo Retencion Grupo Servicio
	 * @param con
	 * @param dtoTRGrupoSer
	 * @return
	 */
	public static int inactivarTRGrupoServicio(Connection con, DtoDetTiposRetencionGrupoSer dtoTRGrupoSer)
	{
		return getTiposRetencionDao().inactivarTRGrupoServicio(con, dtoTRGrupoSer);
	}
		
	/**
	 * Inactivacion de Tipo Retencion Clase Inventario
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public static int inactivarTRClaseInventario(Connection con, DtoDetTiposRetencionClaseInv dtoTRClaseInv)
	{
		return getTiposRetencionDao().inactivarTRClaseInventario(con, dtoTRClaseInv);
	}
		
	/**
	 * Inactivacion de Tipo Retencion Clase Inventario
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public static int inactivarTRConceptoFraVarias(Connection con, DtoDetTiposRetencionConceptoFV dtoTRConceptoFV)
	{
		return getTiposRetencionDao().inactivarTRConceptoFraVarias(con, dtoTRConceptoFV); 
	}
}
