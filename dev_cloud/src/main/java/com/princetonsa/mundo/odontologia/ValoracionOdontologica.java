package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.Utilidades;
import util.odontologia.InfoAntecedenteOdonto;
import util.odontologia.InfoOdontograma;
import util.odontologia.UtilidadOdontologia;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.ValoracionOdontologicaDao;
import com.princetonsa.dto.historiaClinica.DtoPlantillasIngresos;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoComponenteIndicePlaca;
import com.princetonsa.dto.odontologia.DtoValDiagnosticosOdo;
import com.princetonsa.dto.odontologia.DtoValoracionesOdonto;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.historiaClinica.Plantillas;

/**
 * 
 * @author axioma
 *
 */
public class ValoracionOdontologica
{
	
	/**
	 * Atributo para el registro de los errores
	 */
	private ActionErrors errores ;
	
	/**
	 * Atributo para el manejo del DAO
	 */
	private ValoracionOdontologicaDao valoracionDao = null;
	
	/**
	 * Atributo para el manejo del usuario
	 */
	private UsuarioBasico usuario;
	
	/**
	 * Atributo para el manejo del dto
	 */
	private DtoValoracionesOdonto dtoValoracion;
	
	/**
	 * Atributo para el manejo del registro de los campos parametrizables
	 */
	private DtoPlantillasIngresos dtoPlantillasIngreso;
	
	/**
	 * Atributo para el manejo de los campos parametrizables
	 */
	private DtoPlantilla dtoPlantilla;
	
	/**
	 * Atributo para el manejo de los diagnósticos
	 */
	private ArrayList<Diagnostico> diagnosticos;
	
	/**
	 * Atributo para el manejo de los diagnósticos relacionados
	 */
	private HashMap<String, Object> diagnosticosRelacionados;
	
	/**
	 * Método para cargar la informacion de los antecedentes odontologicos
	 */
	private InfoAntecedenteOdonto infoAntecedenteOdonto;
	
	
	/**
	 * Atributo para el manejo del odontograma de Diagnostico
	 * */
	private InfoOdontograma infoOdontograma;
	
	private DtoComponenteIndicePlaca indicePlaca;
	
	Logger logger = Logger.getLogger(ValoracionOdontologica.class);
	




	/**
	 * DtoCita
	 *
	 */
	private DtoCitaOdontologica dtoCita;

	
	/**
	 * CONSTRUCTOR
	 */
	public ValoracionOdontologica()
	{
		this.reset();
	}
	
	/**
	 * Método que limpia los datos 
	 */
	public void reset()
	{
		this.valoracionDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValoracionOdontologicaDao();
		this.errores = new ActionErrors();
		this.usuario = new UsuarioBasico();
		this.dtoValoracion = new DtoValoracionesOdonto();
		this.dtoPlantillasIngreso = new DtoPlantillasIngresos();
		this.dtoPlantilla = new DtoPlantilla();
		this.diagnosticos = new ArrayList<Diagnostico>();
		this.diagnosticosRelacionados = new HashMap<String, Object>();
		this.infoAntecedenteOdonto = new InfoAntecedenteOdonto();
		this.indicePlaca = new DtoComponenteIndicePlaca();
		this.infoOdontograma = new InfoOdontograma();
		this.dtoCita = new DtoCitaOdontologica();
	}
	
	/**
	 * Método implementado para registrar la valoracion odontologica
	 * @param con
	 * @param paciente
	 * @param porConfirmar
	 */
	public double guardar(Connection con, PersonaBasica paciente,String porConfirmar)
	{

		infoOdontograma.setPorConfirmar(porConfirmar);
		boolean porActualizar = false;
		logger.info("VALOR DEL CODIGO PK >> "+this.dtoValoracion.getCodigoPk());
		if(this.dtoValoracion.getCodigoPk()>0)
		{
			porActualizar = true;
			logger.info("EL VALOR DE POR ACTUALIZAR------->"+porActualizar);
		}
		
		//Se ingresa la valoracion
		double codigoPkValoracion = this.valoracionDao.ingresarValoracionesOdonto(con, this.dtoValoracion);
		double codigoPkPlantillaIngreso = ConstantesBD.codigoNuncaValidoDouble;
		
		
		if(codigoPkValoracion>0)
		{
			//************SE REGISTRA LA INFORMACION PARAMETRIZABLE********************************
			codigoPkPlantillaIngreso = Plantillas.guardarCamposParametrizablesIngresoOdontologia(
					con, 
					this.dtoPlantilla,
					paciente.getCodigoIngreso(), 
					paciente.getCodigoPersona(), 
					this.dtoValoracion.getFechaConsulta(), 
					this.dtoValoracion.getHoraConsulta(), 
					this.dtoValoracion.getUsuarioModifica(),
					codigoPkValoracion,
					this.dtoPlantillasIngreso.getCodigoPK());
			logger.info("valor codigo pk plantilla ingreso después de: "+codigoPkPlantillaIngreso);
			
			/*if(codigoPkPlantillaIngreso==ConstantesBD.codigoNuncaValido)
			{
				errores.add("", new ActionMessage("errors.problemasGenericos","guardando la informacion parametrizable de la valoracion: "+this.dtoPlantilla.getNombre()));
			}*/
			//*********************************************************************************************
			
			if(this.errores.isEmpty())
			{
				//************REGISTRO DE LOS DIAGNOSTICOS QUE SE PARAMETRICEN********************************
				//Agrego el login del usuario
				this.diagnosticosRelacionados.put("loginUsuario", usuario.getLoginUsuario());
				logger.info("ENTRE A GUARDAR LOS DIAGNOSTICOS");
				//Si no se ingresan diagnosticos no se inserta nada
				if (!this.diagnosticos.get(0).getAcronimo().equals(""))
				{
					if(ingresarDiagnosticos(con,this.diagnosticos,this.diagnosticosRelacionados,codigoPkValoracion))
						logger.info("INSERTE BIEN LOS DIAGNOSTICOS!!!!!!!!!");
					else
						this.errores.add("", new ActionMessage("errors.problemasGenericos","guardando los diagnóstico de la valoración: "+this.dtoPlantilla.getNombre()));
				}
				
				
				
				//**********************************INGRESO DE COMPONENTES*************************//
				
				//************REGISTRO DEL COMPONENTE ANTECEDENTE ODONTOLOGICO********************************
				if(this.errores.isEmpty()&&this.dtoPlantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteAntecendentesOdontologicos))
				{
					UtilidadOdontologia.llenarDtoTratamientoInterno(this.infoAntecedenteOdonto);
					this.infoAntecedenteOdonto.getAntecedenteOdon().setValoracion(Utilidades.convertirAEntero(Double.toString(codigoPkValoracion)));
					
					// llenar datos parametros antecedentes odontologicos
					this.infoAntecedenteOdonto.getAntecedenteOdon().setCodigoPaciente(paciente.getCodigoPersona());
					this.infoAntecedenteOdonto.getAntecedenteOdon().setIngreso(paciente.getCodigoIngreso());
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
						logger.info("insertar ANTECEDENTE ODONTOLOGICO: ");
						if(UtilidadOdontologia.insertarAntcedenteOdontologico(con, this.infoAntecedenteOdonto.getAntecedenteOdon())==ConstantesBD.codigoNuncaValido)
						{
							this.errores.add("", new ActionMessage("errors.problemasGenericos","ingresando la informacion de antecedente odontológico para la valoración: "+this.dtoPlantilla.getNombre()));
						}	
					}
					else
					{
						logger.info("Actualizar ANTECEDENTE ODONTOLOGICO");
						if(!UtilidadOdontologia.actualizacionAntOdon(con, this.infoAntecedenteOdonto.getAntecedenteOdon()))
						{
							this.errores.add("", new ActionMessage("errors.problemasGenericos","actualizando la informacion de antecedente odontológico para la valoración: "+this.dtoPlantilla.getNombre()));
						}
							
					}
				}
				//************FIN REGISTRO DEL COMPONENTE ANTECEDENTE ODONTOLOGICO****************************
				
				
				//************REGISTRO DEL COMPONENTE ODONTOGRAMA DE DIAGNOSTICO********************************
				if(this.errores.isEmpty()&&this.dtoPlantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaDiag))
				{
					logger.info("guardar Componente Diagnostico >> valoracion >> "+codigoPkValoracion+" ingreso >> "+paciente.getCodigoIngreso()+" cita >> "+this.getDtoValoracion().getCita());
					ComponenteOdontograma compOdonDx = new ComponenteOdontograma();
					compOdonDx.setConInterna(con);
					compOdonDx.setPorConfirmar(porConfirmar);
					
					this.infoOdontograma.setCodigoValoracion(Utilidades.convertirAEntero(codigoPkValoracion+""));
					this.infoOdontograma.setCodigoCita(Utilidades.convertirAEntero(this.getDtoValoracion().getCita()+""));
					this.infoOdontograma.setIdIngresoPaciente(paciente.getCodigoIngreso());

					infoOdontograma.getInfoPlanTrata().setPorConfirmar(porConfirmar);
					
					
					/*
					 * REALIZAR VARIAS OPERACION
					 */
					
					
					/*
					 * paso 3
					 * 
					 */
					this.infoOdontograma.setEspecialidad(this.getDtoCita().getAgendaOdon().getEspecialidadUniAgen());
					BigDecimal nuevoCodigoPkPlanTrat  = compOdonDx.centralAcciones(this.infoOdontograma,ComponenteOdontograma.codigoEstadoGuardarOdonto, new BigDecimal(codigoPkValoracion)).getIndicadorAuxBd();
					
					if(!compOdonDx.getErrores().isEmpty() && this.errores.isEmpty())
					{
						this.infoOdontograma.setIndicadorAuxBd(nuevoCodigoPkPlanTrat);
						this.errores = compOdonDx.getErrores();
					}
				}
				//************FIN REGISTRO DEL COMPONENTE ODONTOGRAMA DE DIAGNOSTICO****************************
				
				//************REGISTRO DEL COMPONENTE INDICE DE PLACA********************************
				logger.info("¿TIENE LA PLANTLLA EL COMPONENTE DE INDICE DE PLACA? "+this.dtoPlantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteIndicePlaca));
				if(this.errores.isEmpty()&&this.dtoPlantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteIndicePlaca))
				{
					logger.info("\n\n\n");
					logger.info("INDICE PLACA: CodigoPlantilla ingreso: "+Utilidades.convertirAEntero(codigoPkPlantillaIngreso+"")+", por confirmar: "+porConfirmar);
					logger.info("\n\n\n");
					this.getIndicePlaca().setPlantillaIngreso(Utilidades.convertirAEntero(codigoPkPlantillaIngreso+""));
					this.getIndicePlaca().setUsuarioModifica(this.usuario.getLoginUsuario());
					
					this.getIndicePlaca().setPorConfirmar(porConfirmar);
					if(!ComponenteIndicePlaca.accionIndicePlaca(con, this.getIndicePlaca()))
					{
						this.errores.add("", new ActionMessage("errors.problemasGenericos","actualizando la informacion de índice de placa para la valoración: "+this.dtoPlantilla.getNombre()));
					}
					
				}
				//************fin REGISTRO DEL COMPONENTE INDICE DE PLACA****************************
				
				
				//****************************** FIN INGRESO DE COMPONENTES**************************//
				
				
			}
			return codigoPkPlantillaIngreso;
			
		}
		else
		{
			this.errores.add("", new ActionMessage("errors.problemasGenericos","guardando la información de la valoracion: "+this.dtoPlantilla.getNombre()));
		}
		
		if(this.errores.isEmpty())
		{
			this.dtoValoracion.setCodigoPk(codigoPkValoracion);
			this.dtoPlantillasIngreso.setCodigoPK(codigoPkPlantillaIngreso);
		}
		return ConstantesBD.codigoNuncaValidoDouble;
	}
	
	
	public static double ingresarValoracionesOdonto(Connection con,DtoValoracionesOdonto dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValoracionOdontologicaDao().ingresarValoracionesOdonto(con,dto);
	}
	
	public static boolean ingresarDiagnosticos(Connection con,ArrayList<Diagnostico> diagnostico, HashMap diagnosticosRelacionados, double valoracion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValoracionOdontologicaDao().ingresarDiagnosticos(con,diagnostico,diagnosticosRelacionados, valoracion);
	}
	
	public static ArrayList<DtoValDiagnosticosOdo> consultaDiagnosticosValOdo(double valoracion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValoracionOdontologicaDao().consultaDiagnosticosValOdo(valoracion);
	}
	
	public static DtoPlantillasIngresos consultarPacienteTieneValoracion(int codigoPersona, double cita, int plantilla)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValoracionOdontologicaDao().consultarPacienteTieneValoracion(codigoPersona,cita,plantilla);
	}
	
	public static DtoValoracionesOdonto consultarValoracionPaciente (int codigoPaciente, double cita, int plantilla)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValoracionOdontologicaDao().consultarValoracionPaciente(codigoPaciente,cita, plantilla);
	}
	
	public static boolean borrarDx(Connection con,double valoracion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValoracionOdontologicaDao().borrarDx(con,valoracion);
	}
	
	/**
	 * Método implementado para consultar la información de la valoracion odontologica
	 * @param con
	 * @param valoracionOdo
	 */
	public static void consultar(Connection con,DtoValoracionesOdonto valoracionOdo)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValoracionOdontologicaDao().consultar(con, valoracionOdo);
	}
	
	public static String obtenerNombreDiagnosticoPrincipal(int codigoDx)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValoracionOdontologicaDao().obtenerNombreDiagnosticoPrincipal(codigoDx);
	}

	/**
	 * @return the errores
	 */
	public ActionErrors getErrores() {
		return errores;
	}

	/**
	 * @param errores the errores to set
	 */
	public void setErrores(ActionErrors errores) {
		this.errores = errores;
	}

	/**
	 * @return the valoracionDao
	 */
	public ValoracionOdontologicaDao getValoracionDao() {
		return valoracionDao;
	}

	/**
	 * @param valoracionDao the valoracionDao to set
	 */
	public void setValoracionDao(ValoracionOdontologicaDao valoracionDao) {
		this.valoracionDao = valoracionDao;
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
	 * @return the dtoValoracion
	 */
	public DtoValoracionesOdonto getDtoValoracion() {
		return dtoValoracion;
	}

	/**
	 * @param dtoValoracion the dtoValoracion to set
	 */
	public void setDtoValoracion(DtoValoracionesOdonto dtoValoracion) {
		this.dtoValoracion = dtoValoracion;
	}

	/**
	 * @return the dtoPlantillasIngreso
	 */
	public DtoPlantillasIngresos getDtoPlantillasIngreso() {
		return dtoPlantillasIngreso;
	}

	/**
	 * @param dtoPlantillasIngreso the dtoPlantillasIngreso to set
	 */
	public void setDtoPlantillasIngreso(DtoPlantillasIngresos dtoPlantillasIngreso) {
		this.dtoPlantillasIngreso = dtoPlantillasIngreso;
	}

	/**
	 * @return the dtoPlantilla
	 */
	public DtoPlantilla getDtoPlantilla() {
		return dtoPlantilla;
	}

	/**
	 * @param dtoPlantilla the dtoPlantilla to set
	 */
	public void setDtoPlantilla(DtoPlantilla dtoPlantilla) {
		this.dtoPlantilla = dtoPlantilla;
	}

	/**
	 * @return the diagnosticos
	 */
	public ArrayList<Diagnostico> getDiagnosticos() {
		return diagnosticos;
	}

	/**
	 * @param diagnosticos the diagnosticos to set
	 */
	public void setDiagnosticos(ArrayList<Diagnostico> diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	/**
	 * @return the diagnosticosRelacionados
	 */
	public HashMap<String, Object> getDiagnosticosRelacionados() {
		return diagnosticosRelacionados;
	}

	/**
	 * @param diagnosticosRelacionados the diagnosticosRelacionados to set
	 */
	public void setDiagnosticosRelacionados(
			HashMap<String, Object> diagnosticosRelacionados) {
		this.diagnosticosRelacionados = diagnosticosRelacionados;
	}

	/**
	 * @return the infoAntecedenteOdonto
	 */
	public InfoAntecedenteOdonto getInfoAntecedenteOdonto() {
		return infoAntecedenteOdonto;
	}

	/**
	 * @param infoAntecedenteOdonto the infoAntecedenteOdonto to set
	 */
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

}