/**
 * 
 */
package com.princetonsa.mundo.historiaClinica;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.UtilidadFecha;

import com.princetonsa.dto.historiaClinica.DtoMotivosNoConsentimientoInformado;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.MotivosNoConsentimientoInfo;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.odontologia.MotivosNoConsentimientoInfoDelegate;

/**
 * @author armando
 *
 */
public class MotivosNoConsentimientoInfoMundo 
{
	/**
	 * @param todos
	 * @return
	 */
	public static ArrayList<DtoMotivosNoConsentimientoInformado> cosultarMotivos(boolean todos) 
	{
		try
		{
			HibernateUtil.beginTransaction();
			MotivosNoConsentimientoInfoDelegate dao=new MotivosNoConsentimientoInfoDelegate();
			ArrayList<MotivosNoConsentimientoInfo> motivo=dao.consultarTodos(todos);
			ArrayList<DtoMotivosNoConsentimientoInformado> resultado=new ArrayList<DtoMotivosNoConsentimientoInformado>();
			for(MotivosNoConsentimientoInfo mot:motivo)
			{
				DtoMotivosNoConsentimientoInformado dto=new DtoMotivosNoConsentimientoInformado();
				dto.setActivo(mot.getActivo());
				dto.setCodigoPk(mot.getCodigoPk());
				dto.setDescripcion(mot.getDescripcion());
				dto.setFechaModifica(mot.getFechaModifica());
				dto.setHoraModifica(mot.getHoraModifica());
				dto.setInstitucion(mot.getInstituciones().getCodigo());
				dto.setUsuarioModifica(mot.getUsuarios().getLogin());
				if(mot.getConsentimientoInfoOdontos().size()>0)
					dto.setPuedoEliminar(false);
				else
					dto.setPuedoEliminar(true);
				resultado.add(dto);
			}
			return resultado;
		}
		catch (Exception e)
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.endTransaction();
		}
		return null;
	}

	public static boolean guardarMotivos(ArrayList<DtoMotivosNoConsentimientoInformado> motivos,ArrayList<DtoMotivosNoConsentimientoInformado> motivosEliminados, UsuarioBasico usuario) 
	{
		try
		{
			HibernateUtil.beginTransaction();
			MotivosNoConsentimientoInfoDelegate dao=new MotivosNoConsentimientoInfoDelegate();
			//eliminar los anteriores
			for(DtoMotivosNoConsentimientoInformado motivo:motivosEliminados)
			{
				MotivosNoConsentimientoInfo dto=dao.findById(motivo.getCodigoPk());
				dao.delete(dto);
			}
			
			//insertar y modificar los nuevos
			for(DtoMotivosNoConsentimientoInformado motivo:motivos)
			{
				MotivosNoConsentimientoInfo dto=new MotivosNoConsentimientoInfo();
				if(motivo.getCodigoPk()>0)
				{
					dto=dao.findById(motivo.getCodigoPk());
				}
				dto.setActivo(motivo.getActivo());
				dto.setDescripcion(motivo.getDescripcion());
				dto.setFechaModifica(UtilidadFecha.getFechaActual());
				dto.setHoraModifica(UtilidadFecha.getHoraActual());
				Instituciones institucion=new Instituciones();
				institucion.setCodigo(usuario.getCodigoInstitucionInt());
				dto.setInstituciones(institucion);
				Usuarios usu=new Usuarios();
				usu.setLogin(usuario.getLoginUsuario());
				dto.setUsuarios(usu);
				dao.persist(dto);
			}
			return true;
		}
		catch (Exception e)
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.endTransaction();
		}
		return false;
	}
}
