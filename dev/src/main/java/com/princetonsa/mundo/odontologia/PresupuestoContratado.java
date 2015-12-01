/**
 * 
 */
package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoFirmasContratoOtrosiInst;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoPresuContratoOdoImp;
import com.princetonsa.dto.odontologia.DtoPresupuestoContratado;
import com.princetonsa.dto.odontologia.DtoPresupuestoCuotasEspecialidad;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.CentroAtencion;
import com.servinte.axioma.mundo.fabrica.odontologia.contrato.ContratoFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IContratoOdontologicoMundo;
import com.servinte.axioma.orm.ContratoOdontologico;
import com.servinte.axioma.orm.FirmasContratoOtrosiInst;
import com.servinte.axioma.orm.Instituciones;


/**
 * 
 * @author Wilson Rios 
 *
 * Jun 6, 2010 - 8:59:42 AM
 */
public class PresupuestoContratado 
{
	/**
	 * 
	 * Metodo para insertar
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static BigDecimal insertar(Connection con, DtoPresupuestoContratado dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoContratadoDao().insertar(con, dto);
	}
	
	/**
	 * 
	 * Metodo para eliminar 
	 * @param con
	 * @param codigoPk
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean eliminar(Connection con, DtoPresupuestoContratado dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoContratadoDao().eliminar(con, dto);
	}
	
	/**
	 * 
	 * Metodo para cargar 
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<DtoPresupuestoContratado> cargar(Connection con, DtoPresupuestoContratado dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoContratadoDao().cargar(con, dto);
	}

	
	
	
	/**
	 * 
	 * Metodo para hacer la insercion de presupuesto_contratado
	 * @param con
	 * @param codigoPK
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean insertarPresupuestoContratadoPrecontratadoYCuotas(Connection con, BigDecimal codigoPKPresupuesto, UsuarioBasico usuario , boolean preContratar) 
	{
		
		
		//1. VERIFICAMOS QUE NO EXISTA PREVIAMENTE, ESTO PUEDE SUCEDER CUANDO HAGO VARIAS SOLICITUDES DE DCTO Y QUEDA PRECONTRATADO
		DtoPresupuestoContratado dto= new DtoPresupuestoContratado();
		boolean retorna= false;
		dto.setCodigoPkPresupuesto(codigoPKPresupuesto);
		
		boolean existe= cargar(con, dto).size()>0;
		if(existe)
		{
			//debemos eliminarlo y volver a insertar con las nuevas condiciones
			retorna=eliminarCascadaCuotasYPresupuesto(con, codigoPKPresupuesto);
			if(!retorna)
			{
				Log4JManager.error("NO ELIMINA CUOTAS Y PRESUPUESTO");
				return retorna;
			}
		}
	
		
		String consecutivoAInsertar=ConstantesBD.codigoNuncaValido+"";
		
		if(!preContratar)
		{
			/*
			 * CONTRATA
			 * CARGAR EL CONSECUTIVO A INSERTAR 
			 */
			consecutivoAInsertar=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoContratoPresupustoOdontologico,usuario.getCodigoInstitucionInt());
	 	  
			if(! UtilidadTexto.isEmpty(consecutivoAInsertar) ) 
			{
				// ADICIONAMOS EL CONSECUTIVO 
				dto.setConsecutivo(new BigDecimal(consecutivoAInsertar));
			}
			else
			{
				//ERROR 
				Log4JManager.info(" \n\n\n\n\n   Error NO EXISTE UN CONSECUTIVO   ");
				Log4JManager.error(" \n\n\n\n\n   Error NO EXISTE UN CONSECUTIVO   ");
				
				/*
				 * NOTA SI NO ENCUENTRA CONSECUTIVO DEBE CANCELAR EL PROCESO
				 * TODO MODIFICAR ESTA PARTE VALIDAR AL INICION QUE EXISTA UN CONSECUTIVO 
				 */
				return retorna=Boolean.FALSE;
			}
		}
		
		else
		{
			/**
			 * PRECONTRATA
			 */
			dto.setConsecutivo(new BigDecimal(ConstantesBD.codigoNuncaValido));
			dto.setPiePaginaPresupuesto(CentroAtencion.obtenerPiePaginaPresupuesto(usuario.getCodigoCentroAtencion()));
		}
		
		
		
		/*
		 *GUARDAR CONTRATO
		 */
		BigDecimal tmpCodigoPresupuesto=insertar(con, dto);
		retorna= tmpCodigoPresupuesto.doubleValue()>0;
		
		
		
		/*
		 * VALIDACION DEL CONSECUTIVO
		 */

		if(!preContratar)
		{	
			if(retorna)
			{
				/*
				 *CAMBIA ESTADO CONSECUTIVO 
				 */
				boolean insertoExitoso= UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoContratoPresupustoOdontologico, usuario.getCodigoInstitucionInt(), consecutivoAInsertar, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
				/*
				 *GUARDA INFORMACION DE LA CLAUSULA 
				 */
				accionGuardarContratoClausula(con, usuario, tmpCodigoPresupuesto);
			}
			else
			{
				/*
				 *CAMBIAR INFOMACION DEL CONSECUTIVO 
				 */
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoContratoPresupustoOdontologico, usuario.getCodigoInstitucionInt(), consecutivoAInsertar, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			}
		}
		
		/*
		 * LA CUOTA SIEMPRE SE GUARDA 
		 * PROCESO DE GUARDAR CUOTAS POR ESPECIALIDA  
		 */
		accionGuardarEspecialidadCuota(con, codigoPKPresupuesto, usuario);	
		
		return retorna;
	}

	
	
	
	
	/**
	 * METODO QUE GUARDA AL ESPECIALIDA CUOTAS Y LOS DETALLES
	 * 1.CARGA LA ESPECIALIDAD POR CUOTAS 
	 * 2.LA INSERTA EN EL PRESUPUESTO
	 * @author Wilson 
	 * @param con
	 * @param codigoPKPresupuesto
	 * @param usuario
	 */
	private static void accionGuardarEspecialidadCuota(Connection con,
														BigDecimal codigoPKPresupuesto, 
														UsuarioBasico usuario) 
	
	{
		
		
		//ahora insertamos los cuotas especialidad
		//primero cargamos todas las especialidades de los programas contratados
		
		ArrayList<Integer> especialidades= PresupuestoCuotasEspecialidad.cargarEspecialidadesProgramaPresupuesto(con, codigoPKPresupuesto);
		
		for(Integer especialidad: especialidades)
		{	
			DtoPresupuestoCuotasEspecialidad dtoCuota= PresupuestoCuotasEspecialidad.proponerCargar(con, especialidad, usuario.getCodigoInstitucionInt());
			if(dtoCuota.getTipoValor()!=null)
			{	
				dtoCuota.setPresupuestoContratado(codigoPKPresupuesto);
				dtoCuota.setFHU(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
				PresupuestoCuotasEspecialidad.insertar(con, dtoCuota);
			}	
		}
	}

	
	
	
	
	
	/**
	 * METODO QUE BUSCA LAS CLAUSULA DE AL INSTITUCION Y LA INSERTA EN EL PRESUPUESTO
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param con
	 * @param usuario
	 * @param tmpCodigoPresupuesto
	 */
	private static void accionGuardarContratoClausula(Connection con,
														UsuarioBasico usuario,
														BigDecimal tmpCodigoPresupuesto) 
	{
	
		/*
		 *INTERFAZ MUNDO CONTRATO FABRICA
		 */
		IContratoOdontologicoMundo mundoContratoOdonto = ContratoFabricaMundo.crearContratoOdontologicoMundo();
		
		
		/*
		 *CONTRATO
		 *ARMAR EL DTO CONTRATO PARA BUSCAR LA INFORMACION DE LA CLAUSULA 
		 */
		ContratoOdontologico dtoContrato = new ContratoOdontologico();
		dtoContrato.setInstituciones(new Instituciones());
		dtoContrato.getInstituciones().setCodigo(usuario.getCodigoInstitucionInt());
		dtoContrato=mundoContratoOdonto.consultarAvanzadaContratoOdon(dtoContrato);
		
		
		/*
		 * VALIDACION 
		 */
		if(dtoContrato !=null)
		{
			
			/*
			 * SE ARMA EL DTO PRESUPUESTO CONTRATO CON LA INFORMACION PARAMETRIZADA EN LA INSTITUCION 
			 * INSTANCIA DTO PRESUPUESTO CONTRATO 
			 */
			DtoPresuContratoOdoImp dtoPresuContra = new DtoPresuContratoOdoImp();
			
			dtoPresuContra.setCodigoPresuContratoOdo(tmpCodigoPresupuesto.longValue());
			dtoPresuContra.setClausulas(dtoContrato.getClausulas());
			dtoPresuContra.setPiePagina(dtoContrato.getPiePagina());
			dtoPresuContra.setFechaModifica(UtilidadFecha.conversionFormatoFechaABD( UtilidadFecha.getFechaActual()));
			dtoPresuContra.setUsuarioModifica(usuario.getLoginUsuario());
			dtoPresuContra.setHoraModifica(UtilidadFecha.getHoraActual());
			
			dtoPresuContra.setListaFirmasContrato(new ArrayList<DtoFirmasContratoOtrosiInst>());
			
			
			/*
			 * ITERACION DETALLE DEL CONTRATO ODONTOLOGICO
			 */
			  if( dtoContrato.getFirmasContratoOtrosiInsts() !=null)
			  {
				 Iterator it= dtoContrato.getFirmasContratoOtrosiInsts().iterator();
			
				 
				 while(it.hasNext())
				 {
					 /*
					  */
					 DtoFirmasContratoOtrosiInst dtoFirmasPresu= new DtoFirmasContratoOtrosiInst();
					 FirmasContratoOtrosiInst entidadFirmasContratos =  (FirmasContratoOtrosiInst)it.next();
					 dtoFirmasPresu.setAdjuntoFirma( entidadFirmasContratos.getAdjuntoFirma());
					 dtoFirmasPresu.setEmpresaInstitucion(util.ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()));  
					 dtoFirmasPresu.setFechaModifica(UtilidadFecha.conversionFormatoFechaABD( UtilidadFecha.getFechaActual()));
					 dtoFirmasPresu.setFirmaDigital(entidadFirmasContratos.getFirmaDigital());
					 dtoFirmasPresu.setLabelDebajoFirma(entidadFirmasContratos.getLabelDebajoFirma());
					 dtoFirmasPresu.setNumero(entidadFirmasContratos.getNumero());
										 
					 dtoPresuContra.getListaFirmasContrato().add(dtoFirmasPresu);
				 }
				 
				 
			  }
				
			  /*
			   *GUARDAR INFORMACION DE LA CLAUSULA  
			   */
			 PresupuestoOdontologico.guardarContratoPresupuestoClausula(con, dtoPresuContra);
		}
	}
	
	

	/**
	 * 
	 * Metodo para eliminar la informacion previa de las cuotas y el presupuesto contratado precontrada
	 * @param con
	 * @param codigoPKPresupuesto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static boolean eliminarCascadaCuotasYPresupuesto(Connection con,BigDecimal codigoPKPresupuesto) 
	{
		//eliminamos las cuotas  x especialidad
		PresupuestoCuotasEspecialidad.eliminar(con, codigoPKPresupuesto);
		//eliminamos el presupuesto_contrato
		DtoPresupuestoContratado dto= new DtoPresupuestoContratado();
		dto.setCodigoPkPresupuesto(codigoPKPresupuesto);
		
		return PresupuestoContratado.eliminar(con, dto);
	}


}
