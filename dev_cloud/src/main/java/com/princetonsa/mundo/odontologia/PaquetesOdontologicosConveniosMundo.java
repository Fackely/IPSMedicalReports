/**
 * 
 */
package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;
import java.util.Set;

import org.hibernate.criterion.Expression;

import com.princetonsa.dto.odontologia.DtoDetallePaquetesOdontologicosConvenios;
import com.princetonsa.dto.odontologia.DtoPaquetesOdontologicos;
import com.princetonsa.dto.odontologia.DtoPaquetesOdontologicosConvenio;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.DetPaqOdontConvenio;
import com.servinte.axioma.orm.EsquemasTarifarios;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.PaqOdontConvenio;
import com.servinte.axioma.orm.PaquetesOdontologicos;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.odontologia.DetPaqOdontConvenioDelegate;
import com.servinte.axioma.orm.delegate.odontologia.PaqOdontConvenioDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * @author armando
 *
 */
public class PaquetesOdontologicosConveniosMundo 
{
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static DtoPaquetesOdontologicosConvenio consultarPaquetesOdontologicosConvenioContrato(int codigoConvenio,int codigoContrato)
	{
		UtilidadTransaccion.getTransaccion().begin();
		PaqOdontConvenioDelegate dao=new PaqOdontConvenioDelegate();
		PaqOdontConvenio paquete=dao.consultarPaquetesOdontologicosConvenioContrato(codigoConvenio, codigoContrato);
		
		DtoPaquetesOdontologicosConvenio paqueteConvenio=new DtoPaquetesOdontologicosConvenio();
		if(paquete!=null)
		{
			paqueteConvenio.setCodigoPk(paquete.getCodigoPk());
			paqueteConvenio.setContrato(paquete.getContratos().getCodigo());
			paqueteConvenio.setContrato(paquete.getConvenios().getCodigo());
			paqueteConvenio.setInstitucion(paquete.getInstituciones().getCodigo());
			paqueteConvenio.setUsuarioModifica(paquete.getUsuarios().getLogin());
			paqueteConvenio.setFechaModifica(paquete.getFechaModifica());
			paqueteConvenio.setHoraModifica(paquete.getHoraModifica());
			paqueteConvenio.setDetallePaquete(cargarDetallePaquete(paquete.getDetPaqOdontConvenios()));
		}
		UtilidadTransaccion.getTransaccion().commit();
		return paqueteConvenio;
		
	}

	private static ArrayList<DtoDetallePaquetesOdontologicosConvenios> cargarDetallePaquete(Set<DetPaqOdontConvenio> detPaqOdontConvenios) 
	{
		ArrayList<DtoDetallePaquetesOdontologicosConvenios>  detallePaquete=new ArrayList<DtoDetallePaquetesOdontologicosConvenios>();
		for(DetPaqOdontConvenio temporal:detPaqOdontConvenios)
		{
			DtoDetallePaquetesOdontologicosConvenios detalle=new DtoDetallePaquetesOdontologicosConvenios();
			detalle.setCodigoPk(temporal.getCodigoPk());
			detalle.setCodigoPkPaqueteConvenio(temporal.getPaqOdontConvenio().getCodigoPk());
			DtoPaquetesOdontologicos paquete=new DtoPaquetesOdontologicos();
			paquete.setCodigoPk(temporal.getPaquetesOdontologicos().getCodigoPk());
			paquete.setDescripcion(temporal.getPaquetesOdontologicos().getDescripcion());
			detalle.setPaquete(paquete);
			detalle.setActivo(temporal.getActivo());
			detalle.setEsUsado(temporal.getPresupuestoPaqueteses().size()>0);
			detalle.setEsquemaTarifario(temporal.getEsquemasTarifarios().getCodigo());
			detalle.setDescripcionEsquemaTarifario(temporal.getEsquemasTarifarios().getNombre());
			detalle.setFechaIncial(temporal.getFechaInicial());
			detalle.setFechaFinal(temporal.getFechaFinal());
			detallePaquete.add(detalle);
		}
		return detallePaquete;
	}

	public static boolean insetarModificar(DtoPaquetesOdontologicosConvenio paqueteConvenio, DtoDetallePaquetesOdontologicosConvenios dtoDetalleConvenio,boolean isModificacion) 
	{
		try
		{
			PaqOdontConvenioDelegate dao=new PaqOdontConvenioDelegate();
			PaqOdontConvenio dto=new PaqOdontConvenio();
			
			//modificacion.
			if(paqueteConvenio.getCodigoPk()>0)
			{
				dto=dao.findById(paqueteConvenio.getCodigoPk());
			}
			else
			{
				Contratos contrato=new Contratos();
				Convenios convenio=new Convenios();
				convenio.setCodigo(paqueteConvenio.getConvenio());
				contrato.setCodigo(paqueteConvenio.getContrato());
				dto.setContratos(contrato);
				dto.setConvenios(convenio);
				Instituciones institucion=new Instituciones();
				institucion.setCodigo(paqueteConvenio.getInstitucion());
				dto.setInstituciones(institucion);
			}
			dto.setFechaModifica(paqueteConvenio.getFechaModifica());
			dto.setHoraModifica(paqueteConvenio.getHoraModifica());
			Usuarios usuario=new Usuarios();
			usuario.setLogin(paqueteConvenio.getUsuarioModifica());
			dto.setUsuarios(usuario);
			dao.persist(dto);
			
			//guardoBien.
			if(dto.getCodigoPk()>0)
			{
				DetPaqOdontConvenioDelegate daoDetalle=new DetPaqOdontConvenioDelegate();
				DetPaqOdontConvenio dtoDetalle=new DetPaqOdontConvenio();
				if(isModificacion)
				{
					dtoDetalle=daoDetalle.findById(dtoDetalleConvenio.getCodigoPk());
				}
				PaquetesOdontologicos paqueteOdon=new PaquetesOdontologicos();
				paqueteOdon.setCodigoPk(dtoDetalleConvenio.getPaquete().getCodigoPk());
				dtoDetalle.setPaquetesOdontologicos(paqueteOdon);
				//ACTIVO
				dtoDetalle.setActivo(dtoDetalleConvenio.getActivo());
				dtoDetalle.setPaqOdontConvenio(dto);
				dtoDetalle.setFechaInicial(dtoDetalleConvenio.getFechaIncial());
				dtoDetalle.setFechaFinal(dtoDetalleConvenio.getFechaFinal());
				EsquemasTarifarios esquema=new EsquemasTarifarios();
				esquema.setCodigo(dtoDetalleConvenio.getEsquemaTarifario());
				dtoDetalle.setEsquemasTarifarios(esquema);
				daoDetalle.persist(dtoDetalle);
				if(dtoDetalle.getCodigoPk()<=0)
				{
					HibernateUtil.abortTransaction();
					return false;
				}
			
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
			e.printStackTrace();
			return false;
		}
	}

	public static boolean eliminarDetallePaquete(int codigoPk) 
	{
		try
		{
			DetPaqOdontConvenioDelegate dao=new DetPaqOdontConvenioDelegate();
			DetPaqOdontConvenio dto=new DetPaqOdontConvenio();
			dto=dao.findById(codigoPk);
			dao.delete(dto);

			HibernateUtil.endTransaction();
			return true;
		}
		catch(Exception e)
		{
			HibernateUtil.abortTransaction();
			e.printStackTrace();
		}
		return false;
	}

}
