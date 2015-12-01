package com.princetonsa.mundo.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.odontologia.InfoAntecedenteOdonto;
import util.odontologia.InfoOdontograma;
import util.odontologia.UtilidadOdontologia;

import java.math.BigDecimal;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.EvolucionOdontologicaDao;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoComponenteIndicePlaca;
import com.princetonsa.dto.odontologia.DtoEvolucionOdontologica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.servinte.axioma.hibernate.HibernateUtil;

public class EvolucionOdontologica
{
	/**
	 * Atributo para el registro de los errores
	 */
	private ActionErrors errores ;
	
	/**
	 * Atributo para el manejo del DAO
	 */
	private EvolucionOdontologicaDao evolucionDao = null;
	
	/**
	 * Atributo para el manejo del usuario
	 */
	private UsuarioBasico usuario;
	
	/**
	 * Atributo para el manejo del dto
	 */
	private DtoEvolucionOdontologica dtoEvolucion;
	
	
	/**
	 * DTO CITA
	 */
	private DtoCitaOdontologica dtoCita;
	
	
	
	/**
	 * Atributo para el manejo de los campos parametrizables
	 */
	private DtoPlantilla dtoPlantilla;
	
	/**
	 * Método para cargar la informacion de los antecedentes odontologicos
	 */
	private InfoAntecedenteOdonto infoAntecedenteOdonto;
	
	private DtoComponenteIndicePlaca indicePlaca;
	
	/**
	 * Atributo para el manejo del odontograma de Diagnostico
	 * */
	private InfoOdontograma infoOdontograma;
	
	public static Logger logger = Logger.getLogger(EvolucionOdontologica.class);
	
	public static ArrayList<DtoEvolucionOdontologica> consultarEvolucion(DtoEvolucionOdontologica dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEvolucionOdontologicaDao().consultarEvolucion(dto);
	}
	
	public static double insertarEvolucionOdon(DtoEvolucionOdontologica dto, Connection con )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEvolucionOdontologicaDao().insertarEvolucionOdon(dto, con);
	}
	
	/**
	 * CONSTRUCTOR
	 */
	public EvolucionOdontologica()
	{
		this.reset();
	}
	
	/**
	 * Método que limpia los datos 
	 */
	public void reset()
	{
		this.evolucionDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEvolucionOdontologicaDao();
		this.errores = new ActionErrors();
		this.usuario = new UsuarioBasico();
		this.dtoEvolucion = new DtoEvolucionOdontologica();
		this.dtoPlantilla = new DtoPlantilla();
		this.infoAntecedenteOdonto = new InfoAntecedenteOdonto();
		this.indicePlaca = new DtoComponenteIndicePlaca();
		this.infoOdontograma = new InfoOdontograma();
		this.dtoCita = new DtoCitaOdontologica();
	}
	
	
	
	/**
	 * Metodo para ingresar la inofrmación de la plantilla parametrica de evolucion odontologica
	 * @param con
	 * @param paciente
	 * @param porConfirmar
	 * @return
	 */
	
	public double insertarEvolucion(Connection con, PersonaBasica paciente,String porConfirmar)
	{
		boolean porActualizar = false;
		logger.info("VALOR DEL CODIGOPK: "+this.dtoEvolucion.getCodigoPk());
		if(this.dtoEvolucion.getCodigoPk()>0)
		{
			porActualizar = true;
			logger.info("EL VALOR DE POR ACTUALIZAR------->"+porActualizar);
		}
		
		double evolucion=ConstantesBD.codigoNuncaValidoDouble;
		//boolean transaccionExitosa=false;
		
		try
		{
			
			
			UtilidadBD.iniciarTransaccion(con);
			
			//Hago al inserción de la evolucion y se la asign al codigo del dto de la evolucion
			logger.info("****************LLAMA A INSERTAR EVOLUCION*************");
			evolucion=EvolucionOdontologica.insertarEvolucionOdon(this.dtoEvolucion, con);
			logger.info("****************VUELVE DE INSERTAR EVOLUCION*************");
			dtoEvolucion.setCodigoPk(evolucion);
			
			//Si se ingresa correctamente la evolucion
			if (evolucion!=ConstantesBD.codigoNuncaValidoDouble)
			{
				logger.info("CODIGO DE LA EVOLUCION----->"+evolucion);
				this.dtoPlantilla.setCodigoEvolucionOdontologia(BigDecimal.valueOf(evolucion));
				//Inserto la plantilla de evolucion
				int codigoPkPlantilla=Plantillas.guardarEvolucionOdon(con, this.dtoPlantilla, paciente.getCodigoIngreso()+"", evolucion+"",this.dtoEvolucion.getCodigoPlantillaEvolucion().intValue()+"", UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), usuario);
				logger.info("codigoPkPlantilla "+codigoPkPlantilla);
				if(codigoPkPlantilla>0)
				{
					//**********************************INGRESO DE COMPONENTES*************************//
					this.dtoEvolucion.setCodigoPlantillaEvolucion(BigDecimal.valueOf(codigoPkPlantilla));
					//************REGISTRO DEL COMPONENTE ANTECEDENTE ODONTOLOGICO********************************
					if(this.dtoPlantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteAntecendentesOdontologicos))
					{
						UtilidadOdontologia.llenarDtoTratamientoInterno(this.infoAntecedenteOdonto);
						this.infoAntecedenteOdonto.getAntecedenteOdon().setEvolucion(Utilidades.convertirAEntero(Double.toString(this.dtoEvolucion.getCodigoPk())));
						
						// llenar datos parametros antecedentes odontologicos
						this.infoAntecedenteOdonto.getAntecedenteOdon().setCodigoPaciente(paciente.getCodigoPersona());
						this.infoAntecedenteOdonto.getAntecedenteOdon().setIngreso(paciente.getCodigoIngreso());
						logger.info("centro atencion: "+usuario.getCodigoCentroAtencion());
						this.infoAntecedenteOdonto.getAntecedenteOdon().setCentroAtencion(usuario.getCodigoCentroAtencion());
						this.infoAntecedenteOdonto.getAntecedenteOdon().setInstitucion(usuario.getCodigoInstitucionInt());
						this.infoAntecedenteOdonto.getAntecedenteOdon().setCodigoMedico(usuario.getCodigoPersona());
						this.infoAntecedenteOdonto.getAntecedenteOdon().setNombresMedico(usuario.getNombreyRMPersonalSalud());
						this.infoAntecedenteOdonto.getAntecedenteOdon().setEspecialidad(this.dtoPlantilla.getCodigoEspecialidad());
						this.infoAntecedenteOdonto.getAntecedenteOdon().setPorConfirmar(porConfirmar);
						this.infoAntecedenteOdonto.getAntecedenteOdon().setMostrarPor(this.infoAntecedenteOdonto.getMostrarPor());
						this.infoAntecedenteOdonto.getAntecedenteOdon().setUsuarioModifica(usuario.getLoginUsuario());
						
		
						if(!porActualizar)
						{
							if(UtilidadOdontologia.insertarAntcedenteOdontologico(con, this.infoAntecedenteOdonto.getAntecedenteOdon())==ConstantesBD.codigoNuncaValido)
							{
								this.errores.add("", new ActionMessage("errors.problemasGenericos","ingresando la informacion de antecedente odontológico para la valoración: "+this.dtoPlantilla.getNombre()));
							}	
						}
						else
						{
							logger.info("Va realizaar Actualizacion Antecedente Codigo PK Antecedente >> "+this.infoAntecedenteOdonto.getAntecedenteOdon().getCodigoPk() );
						 if(this.infoAntecedenteOdonto.getAntecedenteOdon().getCodigoPk()>0)
						 { 
							if(!UtilidadOdontologia.actualizacionAntOdon(con, this.infoAntecedenteOdonto.getAntecedenteOdon()))
							{
								this.errores.add("", new ActionMessage("errors.problemasGenericos","actualizando la informacion de antecedente odontológico para la valoración: "+this.dtoPlantilla.getNombre()));
							}
						 }
								
						}
					}
					//************FIN REGISTRO DEL COMPONENTE ANTECEDENTE ODONTOLOGICO****************************
					
					//************REGISTRO DEL COMPONENTE INDICE DE PLACA********************************
					if(this.dtoPlantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteIndicePlaca))
					{
						logger.info("\n\n\n");
						logger.info("CodigoPlantilla ingreso: "+this.dtoEvolucion.getCodigoPlantillaEvolucion().longValue());
						logger.info("\n\n\n");
						//this.getIndicePlaca().setPlantillaIngreso(Utilidades.convertirAEntero(this.dtoPlantillasIngreso.getCodigoPK()+""));
						this.getIndicePlaca().setPlantillaEvolucionOdo(this.dtoEvolucion.getCodigoPlantillaEvolucion().intValue());
						this.getIndicePlaca().setUsuarioModifica(this.usuario.getLoginUsuario());
						this.getIndicePlaca().setPorConfirmar(porConfirmar);
						if(!ComponenteIndicePlaca.accionIndicePlaca(con, this.getIndicePlaca()))
							this.errores.add("", new ActionMessage("errors.problemasGenericos","actualizando la informacion de índice de placa para la valoración: "+this.dtoPlantilla.getNombre()));
						
					}
					//************fin REGISTRO DEL COMPONENTE INDICE DE PLACA****************************
					
					
					//************REGISTRO DEL COMPONENTE ODONTOGRAMA DE EVOLUCION********************************
					
					/*
					 * TODO se elimina la condición que valida si la plantilla tiene odontograma, para que se
					 * pueda actualizar el estado del plan de tratamiento si todos los servicios asociados
					 * ya se encuentran realizados.
					 */
					
//					if(this.dtoPlantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaEvol))
//					{
					
					logger.info("guardar Componente Evolucion >> evolucion >> "+evolucion+" ingreso >> "+paciente.getCodigoIngreso()+" cita >> "+this.dtoEvolucion.getCita());
					this.infoOdontograma.setCodigoEvolucion(Utilidades.convertirAEntero(evolucion+""));
					this.infoOdontograma.setCodigoCita(Utilidades.convertirAEntero(this.dtoEvolucion.getCita()+""));
					this.infoOdontograma.setIdIngresoPaciente(paciente.getCodigoIngreso());
					this.infoOdontograma.setPorConfirmar(porConfirmar);
				
					/*
					 *EVOLUCIONAR EL PLAN DE TRATAMIENTO 
					 */
					this.infoOdontograma.setEspecialidad(this.getDtoCita().getAgendaOdon().getEspecialidadUniAgen());
					this.infoOdontograma.setCentroCosto(usuario.getCodigoCentroCosto());
	
					ComponenteOdontogramaEvo compOdonEvo = new ComponenteOdontogramaEvo();
					
					if(con==null){
						
						con = HibernateUtil.obtenerConexion();
					}
					
					compOdonEvo.setConInterna(con);
						
					compOdonEvo.centralAcciones(this.infoOdontograma,ComponenteOdontograma.codigoEstadoGuardarOdonto, paciente);
					
					logger.info("aqui entraaaaaaaaaaaaaaaaaa");
					if(!compOdonEvo.getErrores().isEmpty() && this.errores.isEmpty())
					{
						this.errores = compOdonEvo.getErrores();
					}
					
//					}
			
					//logger.info(this.errores.isEmpty());
					//************FIN REGISTRO DEL COMPONENTE ODONTOGRAMA DE EVOLUCION****************************
					
					//****************************** FIN INGRESO DE COMPONENTES**************************//
				}
				else
				{
					errores.add("", new ActionMessage("errors.problemasGenericos","guardando la informacion parametrizable de la evolución: "+this.dtoPlantilla.getNombre()));
					UtilidadBD.abortarTransaccion(con);
				}
			}
			else
			{
				errores.add("", new ActionMessage("errors.problemasGenericos","guardando la informacion de la evolución: "+this.dtoPlantilla.getNombre()));
				UtilidadBD.abortarTransaccion(con);
			}
			
			logger.info("¿Se desea dejar por confirmar la evolucion??" + porConfirmar);
			logger.info("ERRORES >> " + errores);
			
			// En cualquier caso debe modificar el parmámetro por confirmar de la cita
			// ya que en la cita original viene null.
			if(this.errores.isEmpty())
			{
				//Como la cita no esta confirmada entonces entra a estar POR CONFIRMAR en estado S
				
				//SI SE HACE BIEN LA INSERCIÓN REALIZO LA ACTUALIZACION DE LA CITA
				//*************************ACTUALIZO LA CITA*********************************
				logger.info("ESTOY ACTUALIZANDO EL ESTADO POR CONFIRMAR DE LA CITA------->"+this.dtoEvolucion.getCita());
				BigDecimal cita = new BigDecimal(this.dtoEvolucion.getCita());
				DtoCitaOdontologica citaLog = 	CitaOdontologica.obtenerCitaOdontologica(con, cita.intValue(),usuario.getCodigoInstitucionInt());
				          
				citaLog.setUsuarioModifica(usuario.getLoginUsuario());
				citaLog.setUsuarioRegistra(usuario);
				citaLog.setPorConfirmar(porConfirmar);
					           
				if(!CitaOdontologica.actualizarCitaOdontologica(con, citaLog))
				{
					this.errores.add("", new ActionMessage("", new ActionMessage("errors.problemasGenericos","activando la cita para dejarla marcada por confirmar")));
				}
				/*
				else
				{
					this.errores.add("", new ActionMessage("", new ActionMessage("errors.problemasGenericos","registrando el log de la cita")));
				}
				*/
				//***********************FIN ACTUALIZACION DE LA CITA*************************
				
			}
			
			if(this.errores.isEmpty())
			{
				UtilidadBD.finalizarTransaccion(con);
				
				//Se vuelve a cargar el plan de tratamiento
				if(this.dtoPlantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaEvol))
				{
					ComponenteOdontogramaEvo compOdonEvo = new ComponenteOdontogramaEvo();
					compOdonEvo.setConInterna(con);
					compOdonEvo.centralAcciones(this.infoOdontograma, "empezar",paciente);
				}
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return evolucion;
	}
	
	/**
	 * Método implementado para cargar la informacion de la evolucion
	 * @param con
	 * @param evolucionOdo
	 */
	public static void consultar(Connection con,DtoEvolucionOdontologica evolucionOdo)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEvolucionOdontologicaDao().consultar(con, evolucionOdo);
	}

	
	public ActionErrors getErrores() {
		return errores;
	}

	public void setErrores(ActionErrors errores) {
		this.errores = errores;
	}

	public EvolucionOdontologicaDao getEvolucionDao() {
		return evolucionDao;
	}

	public void setEvolucionDao(EvolucionOdontologicaDao evolucionDao) {
		this.evolucionDao = evolucionDao;
	}

	public UsuarioBasico getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioBasico usuario) {
		this.usuario = usuario;
	}

	public DtoEvolucionOdontologica getDtoEvolucion() {
		return dtoEvolucion;
	}

	public void setDtoEvolucion(DtoEvolucionOdontologica dtoEvolucion) {
		this.dtoEvolucion = dtoEvolucion;
	}

	public DtoPlantilla getDtoPlantilla() {
		return dtoPlantilla;
	}

	public void setDtoPlantilla(DtoPlantilla dtoPlantilla) {
		this.dtoPlantilla = dtoPlantilla;
	}

	public InfoAntecedenteOdonto getInfoAntecedenteOdonto() {
		return infoAntecedenteOdonto;
	}

	public void setInfoAntecedenteOdonto(InfoAntecedenteOdonto infoAntecedenteOdonto) {
		this.infoAntecedenteOdonto = infoAntecedenteOdonto;
	}

	public DtoComponenteIndicePlaca getIndicePlaca() {
		return indicePlaca;
	}

	public void setIndicePlaca(DtoComponenteIndicePlaca indicePlaca) {
		this.indicePlaca = indicePlaca;
	}

	public InfoOdontograma getInfoOdontograma() {
		return infoOdontograma;
	}

	public void setInfoOdontograma(InfoOdontograma infoOdontograma) {
		this.infoOdontograma = infoOdontograma;
	}

	public void setDtoCita(DtoCitaOdontologica dtoCita) {
		this.dtoCita = dtoCita;
	}

	public DtoCitaOdontologica getDtoCita() {
		return dtoCita;
	}
	
	
//	public static boolean insertarEvolucion(Connection con,DtoEvolucionOdontologica dtoEvolucionOdo, DtoPlantilla plantilla, UsuarioBasico usuario)
//	{
//		boolean transaccionExitosa=false;
//		try
//		{	
//			double evolucion=ConstantesBD.codigoNuncaValidoDouble;
//			
//			UtilidadBD.iniciarTransaccion(con);
//			
//			evolucion=EvolucionOdontologica.insertarEvolucionOdon(dtoEvolucionOdo);
//
//			//Si se ingresa correctamente la evolucion
//			if (evolucion!=ConstantesBD.codigoNuncaValidoDouble)
//			{
//				logger.info("CODIGO DE LA EVOLUCION----->"+evolucion);
//				//Inserto la plantilla de evolucion
//				if (Plantillas.guardarEvolucionOdon(con, plantilla, "", dtoEvolucionOdo.getPlantilla()+"" ,evolucion+"", UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), usuario).isTrue())
//				{
//					
//					transaccionExitosa=true;
//					UtilidadBD.finalizarTransaccion(con);
//					UtilidadBD.cerrarConexion(con);
//					logger.info("\n\n\n***************SE INSERTO CORRECTAMENTE LA EVOLUCION!!!!!!!\n\n\n***********");
//				}
//				else
//					UtilidadBD.abortarTransaccion(con);
//			}
//			else
//				UtilidadBD.abortarTransaccion(con);
//		}
//		catch (Exception e) {
//			
//			e.printStackTrace();
//		}
//		return transaccionExitosa;
//	}
	
	
}