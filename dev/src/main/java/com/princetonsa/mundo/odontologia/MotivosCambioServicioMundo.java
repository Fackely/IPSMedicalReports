/**
 * 
 */
package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;


import com.princetonsa.actionform.odontologia.MotivosCambioServicioForm;
import com.princetonsa.dto.odontologia.DtoMotivosCambioServicio;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.MotivosCambiosServicios;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.odontologia.MotivosCambioServicioDelegate;

/**
 * @author armando
 *
 */
public class MotivosCambioServicioMundo 
{

	public static boolean existeMotivoCambioServicio(String codigo,int codigoInstitucion) 
	{
		MotivosCambioServicioDelegate dao=new MotivosCambioServicioDelegate();
		boolean resultado=dao.existePaqueteOdontologico(codigo,codigoInstitucion);
		HibernateUtil.endTransaction();
		return resultado;
		
	}

	public static ArrayList<DtoMotivosCambioServicio> consultarMotivosCambioServicios(int codigoInstitucion, String tipoMotivo) 
	{
		MotivosCambioServicioDelegate dao=new MotivosCambioServicioDelegate();
		ArrayList<MotivosCambiosServicios> motivos=dao.consultarMotivosCambioServicios(codigoInstitucion,tipoMotivo);
		ArrayList<DtoMotivosCambioServicio> resultado=new ArrayList<DtoMotivosCambioServicio>();
		for(MotivosCambiosServicios motivo:motivos)
		{
			DtoMotivosCambioServicio dto=new DtoMotivosCambioServicio();
			dto.setCodigo(motivo.getCodigo());
			dto.setCodigoPk(motivo.getCodigoPk());
			dto.setDescripcion(motivo.getDescripcion());
			dto.setFechaModifica(motivo.getFechaModifica());
			dto.setHoraModifica(motivo.getHoraModifica());
			dto.setTipo(motivo.getTipo());
			resultado.add(dto);
			//esto no es necesario cargarlo por ahora.
			//dto.setInstitucion(motivo.getInstituciones().getCodigo());
			//dto.setUsuarioModifica(motivo.getUsuarios().getLogin());
		}
		return resultado;
	}

	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public static boolean eliminarRegistro(int codigoPk) 
	{
		try
		{
			MotivosCambioServicioDelegate dao=new MotivosCambioServicioDelegate();
			MotivosCambiosServicios dto=new MotivosCambiosServicios();
			dto=dao.findById(codigoPk);
			dao.delete(dto);
			HibernateUtil.endTransaction();
			return true;
		}
		catch(Exception e)
		{
			HibernateUtil.abortTransaction();
			return false;
		}
	}

	public static boolean guardar(DtoMotivosCambioServicio dto) 
	{
		try
		{
			MotivosCambioServicioDelegate dao=new MotivosCambioServicioDelegate();
			MotivosCambiosServicios motivo=new MotivosCambiosServicios();
			if(dto.getCodigoPk()>0)
			{
				motivo=dao.findById(dto.getCodigoPk());
				
			}
			else
			{
				Instituciones institucion=new Instituciones();
				institucion.setCodigo(dto.getInstitucion());
				
				Usuarios usuario=new Usuarios();
				usuario.setLogin(dto.getUsuarioModifica());
				
				motivo.setUsuarios(usuario);
				motivo.setInstituciones(institucion);
				
			}
			motivo.setCodigo(dto.getCodigo());
			motivo.setDescripcion(dto.getDescripcion());
			motivo.setFechaModifica(dto.getFechaModifica());
			motivo.setHoraModifica(dto.getHoraModifica());
			motivo.setTipo(dto.getTipo());
			dao.persist(motivo);
			if(motivo.getCodigoPk()>0)
			{
				HibernateUtil.endTransaction();
				return true;
			}
			else
			{
				HibernateUtil.abortTransaction();
				return false;
			}
		}
		catch(Exception e)
		{
			HibernateUtil.abortTransaction();
			return false;
		}  
	}

}
