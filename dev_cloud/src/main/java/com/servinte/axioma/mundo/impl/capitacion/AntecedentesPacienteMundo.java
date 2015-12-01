package com.servinte.axioma.mundo.impl.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.AntecedentesAlergiasDao;
import com.princetonsa.dao.AntecedentesFamiliaresDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.comun.DtoDatosGenericos;
import com.princetonsa.mundo.AntecedentesVacunas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.antecedentes.AntecedenteMedicamento;
import com.princetonsa.mundo.antecedentes.AntecedenteMorbidoMedico;
import com.princetonsa.mundo.antecedentes.AntecedenteMorbidoQuirurgico;
import com.princetonsa.mundo.antecedentes.AntecedentePediatrico;
import com.princetonsa.mundo.antecedentes.AntecedenteToxico;
import com.princetonsa.mundo.antecedentes.AntecedenteTransfusional;
import com.princetonsa.mundo.antecedentes.AntecedentesGinecoObstetricos;
import com.princetonsa.mundo.antecedentes.AntecedentesMedicamentos;
import com.princetonsa.mundo.antecedentes.AntecedentesMorbidos;
import com.princetonsa.mundo.antecedentes.AntecedentesToxicos;
import com.princetonsa.mundo.antecedentes.AntecedentesTransfusionales;
import com.princetonsa.mundo.antecedentes.CategoriaAlergia;
import com.princetonsa.mundo.antecedentes.InfoMadre;
import com.princetonsa.mundo.antecedentes.InfoPadre;
import com.princetonsa.mundo.hojaOftalmologica.AntecedentesOftalmologicos;
import com.servinte.axioma.mundo.fabrica.historiaClinica.HistoriaClinicaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IAntecedentesPacienteMundo;
import com.servinte.axioma.mundo.interfaz.historiaClinica.IAntecedentesMundo;
import com.servinte.axioma.mundo.interfaz.historiaClinica.IAntecedentesVariosMundo;
import com.servinte.axioma.orm.Antecedentes;
import com.servinte.axioma.orm.AntecedentesVarios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad AntecdentesPacienteMundo 
 * @author Cristhian Murillo
 */
@SuppressWarnings("rawtypes")
public class AntecedentesPacienteMundo implements IAntecedentesPacienteMundo
{

	/** Dato para antecedentes de alergias */
	private AntecedentesAlergiasDao antecedentesAlergiasDao;
	
	/** Dato para antecedentes familiares */
	private AntecedentesFamiliaresDao antecedentesFamiliaresDao;
	
	/** Lista de todos los antecedentes a mostrar */
	@SuppressWarnings({ "unused" })
	private ArrayList categoriasAlergias;
	
	/** Lista de todos los antecedentes a mostrar */
	ArrayList<DtoDatosGenericos> listaAntecedentes = new ArrayList<DtoDatosGenericos>();
	
	/** * Tipos de Alergias */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.AntecedentesPacienteMundo");
	
	/** * Mundo de AntecedentesVarios */
	IAntecedentesVariosMundo antecedentesVariosMundo;
	
	/** * Mundo de Antecedentes */
	IAntecedentesMundo antecedentesMundo;
	
	
	
	/**
	 * Constructor de la clase
	*/
	public AntecedentesPacienteMundo()
	{
		this.antecedentesVariosMundo 	= HistoriaClinicaFabricaMundo.crearAntecedentesVariosMundo();
		this.antecedentesMundo			= HistoriaClinicaFabricaMundo.crearAntecedentesMundo();
		
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (myFactory != null) 
		{
			this.antecedentesAlergiasDao 	= myFactory.getAntecedentesAlergiasDao();
			this.antecedentesFamiliaresDao  = myFactory.getAntecedentesFamiliaresDao();
		}
	}


	@Override
	public ArrayList<DtoDatosGenericos> obtenerListaTodosAntecedentesPaciente(int codigoPaciente, int codInstitucion) throws SQLException 
	{
		Connection con = UtilidadBD.abrirConexion();
		DtoDatosGenericos antecedente = new DtoDatosGenericos();
		
		/* Alergias/Infecciones */  
		antecedente = cargarAntecedentesAlergiasInfecciones(codigoPaciente, con); //ok
		if(antecedente != null){
			this.listaAntecedentes.add(antecedente);
		}
		
		/* Familiares*/
		antecedente = cargarAntecedentesFamiliares(codigoPaciente, con); //ok
		if(antecedente != null){
			this.listaAntecedentes.add(antecedente);
		}
		
		/* Familiares Oculares */
		antecedente = cargarAntecedentesFamiliaresOculares(codigoPaciente, con, codInstitucion); //ok
		if(antecedente != null){
			this.listaAntecedentes.add(antecedente);
		}
		
		/* Gineco Obstétricos */
		antecedente = cargarAntecedentesGinecoObstetricos(codigoPaciente, con); // ok?
		if(antecedente != null){
			this.listaAntecedentes.add(antecedente);
		}
		
		/* Medicamentos */
		antecedente = cargarAntecedentesMedicamentos(codigoPaciente, con); //ok
		if(antecedente != null){
			this.listaAntecedentes.add(antecedente);
		}
		
		/* Médicos y Quirurgicos */
		antecedente = cargarAntecedentesMedicamentosQuirurgicos(codigoPaciente, con); //ok
		if(antecedente != null){
			this.listaAntecedentes.add(antecedente);
		}
		
		/* Pediátricos */
		antecedente = cargarAntecedentesPediatricos(codigoPaciente, con); //ok
		if(antecedente != null){
			this.listaAntecedentes.add(antecedente);
		}
		
		/* Personales Oculares */
		antecedente = cargarAntecedentesPersonalesOculares(codigoPaciente, con, codInstitucion); //ok
		if(antecedente != null){
			this.listaAntecedentes.add(antecedente);
		}
		
		/* Tóxicos */
		antecedente = cargarAntecedentesToxicos(codigoPaciente, con); //ok
		if(antecedente != null){
			this.listaAntecedentes.add(antecedente);
		}
		
		/* Transfusionales */
		antecedente = cargarAntecedentesTransfusionales(codigoPaciente, con); //ok
		if(antecedente != null){
			this.listaAntecedentes.add(antecedente);
		}
		
		/* Vacunas */
		antecedente = cargarAntecedentesVacunas(codigoPaciente, con); //ok
		if(antecedente != null){
			this.listaAntecedentes.add(antecedente);
		}
		
		/* Varios */
		antecedente = cargarAntecedentesVarios(codigoPaciente); //ok
		if(antecedente != null){
			this.listaAntecedentes.add(antecedente);
		}
		
		
		UtilidadBD.closeConnection(con);
		
		return this.listaAntecedentes;
	}
	

	


	/**
	 * Consulta las alergias e infecciones del paciente
	 * @param codigoPaciente
	 * @param con
	 * @return DtoDatosGenericos
	 * @throws SQLException
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	@SuppressWarnings("unused")
	public DtoDatosGenericos cargarAntecedentesAlergiasInfecciones(int codigoPaciente, Connection con) throws SQLException 
	{
		String tipoAntecedente = fuenteMensaje.getMessage("AntecedentesPacienteMundo.AlergiasInfecciones");
		DtoDatosGenericos antecedentesTipoAlergiasInfeccionesDatosGenericos = new DtoDatosGenericos();
		antecedentesTipoAlergiasInfeccionesDatosGenericos.setDato1(tipoAntecedente);
		
		ResultSetDecorator rs_antAle = this.antecedentesAlergiasDao.cargarAntecedentesAlergias(con, codigoPaciente);
		
		ResultSetDecorator rs_alePredef;
		
		int codigoCategoria;
		int codigoCategoriaAnterior = -2;
		CategoriaAlergia categoriaAlergia = new CategoriaAlergia();
		ArrayList alergias = new ArrayList();
		
		String observacion = null;
		Date fechaObser = null;
		String horaObser = null;
		
		if(rs_antAle.next())
		{
			rs_alePredef = this.antecedentesAlergiasDao.cargarAlergiasPredef(con, codigoPaciente);
			
			observacion = rs_antAle.getString("observaciones");
			fechaObser = rs_antAle.getDate("fecha");
			horaObser = rs_antAle.getString("hora");
			
			// Alergias predefinidas
			while(rs_alePredef.next())
			{
				codigoCategoria = rs_alePredef.getInt("categoria");
				
				if(codigoCategoriaAnterior != codigoCategoria)
				{
					codigoCategoriaAnterior = codigoCategoria;			
					categoriaAlergia = new CategoriaAlergia(codigoCategoria, rs_alePredef.getString("nombreCategoria"), new ArrayList(), new ArrayList());
				}
				DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
				datoGenericos.setDato1(rs_alePredef.getString("nombreTipo"));
				datoGenericos.setDato2(rs_alePredef.getString("nombreTipo"));
				
				if(!UtilidadTexto.isEmpty(fechaObser+"")){
					datoGenericos.setFecha(fechaObser);
				}
				if(!UtilidadTexto.isEmpty(horaObser)){
					datoGenericos.setHora(horaObser);
				}
				
				antecedentesTipoAlergiasInfeccionesDatosGenericos.getListaDatos().add(datoGenericos);
			}
			
			// Alergias adicionales
			ArrayList<DtoDatosGenericos> listaAdicionales = new ArrayList<DtoDatosGenericos>();
			listaAdicionales = this.antecedentesAlergiasDao.cargarAlergiasAdicNoRs(con, codigoPaciente);
			if(!Utilidades.isEmpty(listaAdicionales)){
				antecedentesTipoAlergiasInfeccionesDatosGenericos.getListaDatos().addAll(listaAdicionales);
			}
		}
		
		//-------------------------------------------------------------------------
		if(!UtilidadTexto.isEmpty(observacion)){
			String etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Observaciones");
			DtoDatosGenericos datoGenericosObser = new DtoDatosGenericos();
			datoGenericosObser.setDato1(etiqueta);
			datoGenericosObser.setDato2(observacion);
			if(!UtilidadTexto.isEmpty(fechaObser+"")){
				datoGenericosObser.setFecha(fechaObser);
			}
			if(!UtilidadTexto.isEmpty(horaObser)){
				datoGenericosObser.setHora(horaObser);
			}
			antecedentesTipoAlergiasInfeccionesDatosGenericos.getListaDatos().add(datoGenericosObser);
		}
		//-------------------------------------------------------------------------
		
		
		if(!Utilidades.isEmpty(antecedentesTipoAlergiasInfeccionesDatosGenericos.getListaDatos())){
			return antecedentesTipoAlergiasInfeccionesDatosGenericos;
		}
		
		else return null;
	}
	
	
	/**
	 * Consulta los antecedentes familiares del paciente
	 * @param codigoPaciente
	 * @param con
	 * @return DtoDatosGenericos
	 * @throws SQLException
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	public DtoDatosGenericos cargarAntecedentesFamiliares(int codigoPaciente, Connection con)throws SQLException
	{
		String tipoAntecedente = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Familiares");
		ResultSetDecorator famGeneral_rs = this.antecedentesFamiliaresDao.cargarFamiliares(con, codigoPaciente); 
			
		String observacion = null;
		Date fechaObser = null;
		String horaObser = null;
		
		
		if (famGeneral_rs.next())
		{
			DtoDatosGenericos antecedentesTipoFamiliares = new DtoDatosGenericos();
			antecedentesTipoFamiliares.setDato1(tipoAntecedente);

			ResultSetDecorator famOtros_rs = this.antecedentesFamiliaresDao.cargarFamiliaresOtros(con, codigoPaciente);

			observacion = famGeneral_rs.getString("observaciones");
			fechaObser = famGeneral_rs.getDate("fecha");
			horaObser = famGeneral_rs.getString("hora");
			

			while(famOtros_rs.next())
			{
				/*
				 famOtros_rs.getInt("codigo")
				*/
				DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
				datoGenericos.setDato1(famOtros_rs.getString("nombre"));
				datoGenericos.setDato2(famOtros_rs.getString("parentesco"));
				
				if(!UtilidadTexto.isEmpty(famOtros_rs.getDate("fecha")+"")){
					datoGenericos.setFecha(famOtros_rs.getDate("fecha"));
				}
				if(!UtilidadTexto.isEmpty(famOtros_rs.getString("hora"))){
					datoGenericos.setHora(famOtros_rs.getString("hora"));
				}
				
				antecedentesTipoFamiliares.getListaDatos().add(datoGenericos);
			}

			ResultSetDecorator famPredefinidos_rs=this.antecedentesFamiliaresDao.cargarFamiliaresTipos(con, codigoPaciente);
		 	while(famPredefinidos_rs.next())
		 	{
		 		/*
		 		 famPredefinidos_rs.getInt("tipo_enfermedad_familiar")
		 		 famPredefinidos_rs.getString("observaciones")
		 		*/
	 			DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
				datoGenericos.setDato1(famPredefinidos_rs.getString("nombre"));
				datoGenericos.setDato2(famPredefinidos_rs.getString("parentesco"));
				
				if(!UtilidadTexto.isEmpty(famPredefinidos_rs.getDate("fecha")+"")){
					datoGenericos.setFecha(famPredefinidos_rs.getDate("fecha"));
				}
				if(!UtilidadTexto.isEmpty(famPredefinidos_rs.getString("hora"))){
					datoGenericos.setHora(famPredefinidos_rs.getString("hora"));
				}
				antecedentesTipoFamiliares.getListaDatos().add(datoGenericos);
		 	}
		 	
		 	//FIXME aca vot - colocar oservaciones obser aca
		 	/*observacion = famGeneral_rs.getString("observaciones");
			fechaObser = famGeneral_rs.getDate("fecha");
			horaObser = famGeneral_rs.getString("hora"); */
		 	
		 	return antecedentesTipoFamiliares; 
	 	}
		
		
		
		
		else return null;
	}
	
	
	/**
	 * Consulta los antecedentes familiares oculares del paciente
	 * @param codigoPaciente2
	 * @param con
	 * @param codInstitucion
	 * @return DtoDatosGenericos
	 *
	 * @autor Cristhian Murillo
	 *
	*/
	private DtoDatosGenericos cargarAntecedentesFamiliaresOculares(int codigoPaciente, Connection con, int codInstitucion) 
	{
		AntecedentesOftalmologicos  antecedentesOftalmologicos = new AntecedentesOftalmologicos(); 

		String tipoAntecedente = fuenteMensaje.getMessage("AntecedentesPacienteMundo.FamiliaresOculares");
		DtoDatosGenericos antecedentesTipoFamiliaresOculares = new DtoDatosGenericos();
		antecedentesTipoFamiliaresOculares.setDato1(tipoAntecedente);
		
		
		// Tipos de enfermedades personales por institución  
		Collection tiposEnfermedades = antecedentesOftalmologicos.consultarTiposEnferOftalfamiliares(con, codInstitucion, codigoPaciente);
		
		// Ennfermedades oftalmológicas familiares 
		Collection enfermedades = antecedentesOftalmologicos.consutarEnferOftalFamPadece(con, codigoPaciente);
		
		// Tipos de parestescos registrados   
		Collection tiposParentescos = antecedentesOftalmologicos.consultarTiposParentesco(con); 
		
		
		Iterator itTiposEnfermedades = tiposEnfermedades.iterator();
		while (itTiposEnfermedades.hasNext())
		{
			HashMap mapaTiposEnfermedades = (HashMap) itTiposEnfermedades.next();
			
			Iterator itEnfermedades = enfermedades.iterator();
			while (itEnfermedades.hasNext())
			{
				HashMap mapaEnfermedades = (HashMap) itEnfermedades.next();
				
				if(mapaTiposEnfermedades.get("codigo").equals(mapaEnfermedades.get("enfer_oftal_inst")))
				{
					Iterator itTiposParentescos = tiposParentescos.iterator();
					while (itTiposParentescos.hasNext())
					{
						HashMap mapaTiposParentescos = (HashMap) itTiposParentescos.next();
						if(mapaEnfermedades.get("parentesco").equals(mapaTiposParentescos.get("codigo")))
						{
							DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
							datoGenericos.setDato1(mapaTiposEnfermedades.get("nombre")+"");
							datoGenericos.setDato2(mapaTiposParentescos.get("nombre")+"");
							
							if(!UtilidadTexto.isEmpty(mapaEnfermedades.get("fecha")+"")){
								datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(mapaEnfermedades.get("fecha")+""));
							}
							if(!UtilidadTexto.isEmpty(mapaEnfermedades.get("hora")+"")){
								datoGenericos.setHora(mapaEnfermedades.get("hora")+"");
							}
							
							antecedentesTipoFamiliaresOculares.getListaDatos().add(datoGenericos);
						}
					}
				}
			}
		}
		
		if(!Utilidades.isEmpty(antecedentesTipoFamiliaresOculares.getListaDatos())){
			return antecedentesTipoFamiliaresOculares;
		}
		
		else return null;
	}
	
	
	/**
	 * Consulta los antecedentes Gineco Obstétricos del paciente
	 * @param codigoPaciente
	 * @param con
	 * @return DtoDatosGenericos
	 * @throws SQLException
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	public DtoDatosGenericos cargarAntecedentesGinecoObstetricos(int codigoPaciente, Connection con)throws SQLException
	{
		String tipoAntecedente = fuenteMensaje.getMessage("AntecedentesPacienteMundo.GinecoObstetricos");
		DtoDatosGenericos antecedentesTipoGinecoObtetricos = new DtoDatosGenericos();
		antecedentesTipoGinecoObtetricos.setDato1(tipoAntecedente);
		
		DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
		String etiqueta = null;

		AntecedentesGinecoObstetricos antecedentes = new AntecedentesGinecoObstetricos();
		PersonaBasica paciente = new PersonaBasica() ;
		paciente.setCodigoPersona(codigoPaciente);
		antecedentes.setPaciente(paciente);

		antecedentes.cargar(con, 0);
		//AntecedentesGinecoObstetricosHistorico antGH = antecedentes.cargarUltimoRegInfoEmbarazos(con);
		
		
		// No historicos y no modificables
		if(!UtilidadTexto.isEmpty(antecedentes.getRangoEdadMenarquia().getAcronimo()))
		{
			etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.RangoEdadMenarquia");
			datoGenericos = new DtoDatosGenericos();
			datoGenericos.setDato1(etiqueta);
			datoGenericos.setDato2(antecedentes.getRangoEdadMenarquia().getValue());
			
			if(!UtilidadTexto.isEmpty(antecedentes.getFecha()+"")){
				datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(antecedentes.getFecha()));
			}
			datoGenericos.setHora(antecedentes.getHora());
			
			antecedentesTipoGinecoObtetricos.getListaDatos().add(datoGenericos);
		}
		etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.OtraEdadMenarquia");
		datoGenericos = new DtoDatosGenericos();
		datoGenericos.setDato1(etiqueta);
		datoGenericos.setDato2(antecedentes.getOtroEdadMenarquia());
		if(!UtilidadTexto.isEmpty(antecedentes.getFecha())){
			datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(antecedentes.getFecha()));
		}
		datoGenericos.setHora(antecedentes.getHora());
		antecedentesTipoGinecoObtetricos.getListaDatos().add(datoGenericos);
		
		if(!UtilidadTexto.isEmpty(antecedentes.getRangoEdadMenopausia().getAcronimo()))
		{
			etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.RangoEdadMenopausia");
			datoGenericos = new DtoDatosGenericos();
			datoGenericos.setDato1(etiqueta);
			datoGenericos.setDato2(antecedentes.getRangoEdadMenopausia().getValue());
			if(!UtilidadTexto.isEmpty(antecedentes.getFecha())){
				datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(antecedentes.getFecha()));
			}
			datoGenericos.setHora(antecedentes.getHora());
			antecedentesTipoGinecoObtetricos.getListaDatos().add(datoGenericos);
		}
		etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.OtraEdadMenopausia");
		datoGenericos = new DtoDatosGenericos();
		datoGenericos.setDato1(etiqueta);
		datoGenericos.setDato2(antecedentes.getOtroEdadMenopausia());
		if(!UtilidadTexto.isEmpty(antecedentes.getFecha())){
			datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(antecedentes.getFecha()));
		}
		datoGenericos.setHora(antecedentes.getHora());
		antecedentesTipoGinecoObtetricos.getListaDatos().add(datoGenericos);
		
		if(antecedentes.getInicioVidaSexual() != 0 ){
			etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.InicioVidaSexual");
			datoGenericos = new DtoDatosGenericos();
			datoGenericos.setDato1(etiqueta);
			datoGenericos.setDato2(antecedentes.getInicioVidaSexual()+"");
			if(!UtilidadTexto.isEmpty(antecedentes.getFecha())){
				datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(antecedentes.getFecha()));
			}
			datoGenericos.setHora(antecedentes.getHora());
			antecedentesTipoGinecoObtetricos.getListaDatos().add(datoGenericos);
		}

		if(antecedentes.getInicioVidaObstetrica() != 0 ){
			etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.InicioVidaObstetrica");
			datoGenericos = new DtoDatosGenericos();
			datoGenericos.setDato1(etiqueta);
			datoGenericos.setDato2(antecedentes.getInicioVidaObstetrica()+"");
			if(!UtilidadTexto.isEmpty(antecedentes.getFecha())){
				datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(antecedentes.getFecha()));
			}
			datoGenericos.setHora(antecedentes.getHora());
			antecedentesTipoGinecoObtetricos.getListaDatos().add(datoGenericos);
		}
		
		if(!UtilidadTexto.isEmpty(antecedentes.getObservaciones())){
			etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Observaciones");
			datoGenericos = new DtoDatosGenericos();
			datoGenericos.setDato1(etiqueta);
			datoGenericos.setDato2(antecedentes.getObservaciones());
			if(!UtilidadTexto.isEmpty(antecedentes.getFecha())){
				datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(antecedentes.getFecha()));
			}
			datoGenericos.setHora(antecedentes.getHora());
			antecedentesTipoGinecoObtetricos.getListaDatos().add(datoGenericos);
		}

		// Métodos anticonceptivos
		ArrayList listaMetodosAnticonceptivos = antecedentes.getMetodosAnticonceptivos();
		String metodosAnticonceptivos = null;
		HashMap<String, String> mapaAnticonceptivos = new HashMap<String, String>();
		
		
		for( int i=0; i < listaMetodosAnticonceptivos.size(); i++ )
		{
			InfoDatos metodo = (InfoDatos)listaMetodosAnticonceptivos.get(i);
			
			if(UtilidadTexto.isEmpty(metodosAnticonceptivos))
			{
				/* metodo.getAcronimo()) - metodo.getValue()) - metodo.getDescripcion()) */
				metodosAnticonceptivos = metodo.getValue();
				mapaAnticonceptivos.put(metodosAnticonceptivos, metodosAnticonceptivos);
			}
			else{
				if(!mapaAnticonceptivos.containsKey(metodo.getValue())){
					metodosAnticonceptivos += ", "+metodo.getValue();
				}
			}
		}
		
		etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.MetodosAnticonceptivos");
		datoGenericos = new DtoDatosGenericos();
		datoGenericos.setDato1(etiqueta);
		datoGenericos.setDato2(metodosAnticonceptivos);
		if(!UtilidadTexto.isEmpty(antecedentes.getFecha())){
			datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(antecedentes.getFecha()));
		}
		datoGenericos.setHora(antecedentes.getHora());
		antecedentesTipoGinecoObtetricos.getListaDatos().add(datoGenericos);
		
		
		// Embarazos 
		ArrayList embarazos = antecedentes.getEmbarazos();
		etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.NumeroEmbarazos");
		datoGenericos = new DtoDatosGenericos();
		datoGenericos.setDato1(etiqueta);
		datoGenericos.setDato2(embarazos.size()+"");
		if(!UtilidadTexto.isEmpty(antecedentes.getFecha())){
			datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(antecedentes.getFecha()));
		}
		datoGenericos.setHora(antecedentes.getHora());
		antecedentesTipoGinecoObtetricos.getListaDatos().add(datoGenericos);
		
		/* 
		 Solo se está mostrando información básica sin detalle. 
		 Revisar estado "cargar" de AntecedentesGinecoObstetricosAction en caso de querer mostrar más 
		*/
		
		
		if(!Utilidades.isEmpty(antecedentesTipoGinecoObtetricos.getListaDatos())){
			return antecedentesTipoGinecoObtetricos;
		}
		
		else return null;
	}
	
	
	/**
	 * Consulta los antecedentes de Medicamentos del paciente
	 * @param codigoPaciente2
	 * @param con
	 * @return {@link DtoDatosGenericos}
	 *
	 * @autor Cristhian Murillo
	 */
	private DtoDatosGenericos cargarAntecedentesMedicamentos(int codigoPaciente, Connection con) 
	{
		String tipoAntecedente = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Medicamentos");
		DtoDatosGenericos antecedentesTipoMedicamentos = new DtoDatosGenericos();
		antecedentesTipoMedicamentos.setDato1(tipoAntecedente);
		DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
		String etiqueta = null;
		
		AntecedentesMedicamentos medicamentos = new AntecedentesMedicamentos();
		medicamentos.cargar(con, codigoPaciente);
		
		if(!UtilidadTexto.isEmpty(medicamentos.getObservaciones()))
		{
			etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.ObservacionesGenerales");
			datoGenericos = new DtoDatosGenericos();
			datoGenericos.setDato1(etiqueta);
			datoGenericos.setDato2(medicamentos.getObservaciones());
			
			if(medicamentos.getFecha() != null){
				datoGenericos.setFecha(medicamentos.getFecha());
			}
			if(!UtilidadTexto.isEmpty(medicamentos.getHora())){
				datoGenericos.setHora(medicamentos.getHora());
			}

			antecedentesTipoMedicamentos.getListaDatos().add(datoGenericos);
		}

		
		for(int i =1; i<=medicamentos.getMedicamentos().size(); i++)
		{
			AntecedenteMedicamento medicamento = medicamentos.getMedicamento(i-1);
			String detalleMedicamento = null;
			
			if(!UtilidadTexto.isEmpty(medicamento.getDosis())){
				detalleMedicamento += " Dósis:"+medicamento.getDosis();
			}
			if(!UtilidadTexto.isEmpty(medicamento.getFrecuencia())){
				detalleMedicamento += " Freq:"+medicamento.getFrecuencia();
			}
			if(!UtilidadTexto.isEmpty(medicamento.getTipoFrecuencia())){
				detalleMedicamento += " TipoFreq:"+medicamento.getTipoFrecuencia();
			}
			if(!UtilidadTexto.isEmpty(medicamento.getUnidosis())){
				detalleMedicamento += " Unidósis:"+medicamento.getUnidosis();
			}
			if(!UtilidadTexto.isEmpty(medicamento.getFechaInicio())){
				detalleMedicamento += " Inicio:"+medicamento.getFechaInicio();
			}
			if(!UtilidadTexto.isEmpty(medicamento.getFechaFin())){
				detalleMedicamento += " Fin:"+medicamento.getFechaFin();
			}
			if(!UtilidadTexto.isEmpty(medicamento.getTiempoT())){
				detalleMedicamento += " Tiempo:"+medicamento.getTiempoT();
			}
			if(!UtilidadTexto.isEmpty(medicamento.getCantidad())){
				detalleMedicamento += " Cant:"+medicamento.getCantidad();
			}else{
				detalleMedicamento += " Cant:0";
			}
			if(!UtilidadTexto.isEmpty(medicamento.getObservaciones())){
				detalleMedicamento += " Obs:"+medicamento.getObservaciones();
			}
			
			
			//medicamento.getCodigoA()
			
			datoGenericos = new DtoDatosGenericos();
			datoGenericos.setDato1(medicamento.getNombre());
			datoGenericos.setDato2(detalleMedicamento);
			
			if(medicamentos.getFecha() != null){
				datoGenericos.setFecha(medicamentos.getFecha());
			}
			if(!UtilidadTexto.isEmpty(medicamentos.getHora())){
				datoGenericos.setHora(medicamentos.getHora());
			}
			
			antecedentesTipoMedicamentos.getListaDatos().add(datoGenericos);
		}		
	
		
		if(!Utilidades.isEmpty(antecedentesTipoMedicamentos.getListaDatos())){
			return antecedentesTipoMedicamentos;
		}
		
		else return null;
	}
	
	
	/**
	 * Consulta los antecedentes de medicamentos quirurgicos del paciente
	 * @param codigoPaciente
	 * @param con
	 * @return {@link DtoDatosGenericos}
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	private DtoDatosGenericos cargarAntecedentesMedicamentosQuirurgicos(int codigoPaciente, Connection con) 
	{
		String tipoAntecedente = fuenteMensaje.getMessage("AntecedentesPacienteMundo.MedicamentosQuirurgicos");
		DtoDatosGenericos antecedentesTipoMedicamentosQururgicos = new DtoDatosGenericos();
		antecedentesTipoMedicamentosQururgicos.setDato1(tipoAntecedente);
		String etiqueta = null;
		DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
		
		AntecedentesMorbidos morbidos = new AntecedentesMorbidos();
		PersonaBasica paciente = new PersonaBasica() ;
		paciente.setCodigoPersona(codigoPaciente);
		morbidos.setPaciente(paciente);
		morbidos.cargar(con);
		
		if(!UtilidadTexto.isEmpty(morbidos.getObservaciones()))
		{
			etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.ObservacionesGenerales");
			datoGenericos = new DtoDatosGenericos();
			datoGenericos.setDato1(etiqueta);
			datoGenericos.setDato2(morbidos.getObservaciones());
			
			if(morbidos.getFecha() != null){
				datoGenericos.setFecha(morbidos.getFecha());
			}
			if(!UtilidadTexto.isEmpty(morbidos.getHora())){
				datoGenericos.setHora(morbidos.getHora());
			}
			
			antecedentesTipoMedicamentosQururgicos.getListaDatos().add(datoGenericos);
		}
		
		
		// Morbidos Médicos Predefinidos		
		ArrayList antecedentes = morbidos.getAntecedentesMorbidosMedicosPredefinidos();
		for(int i=0; i<antecedentes.size(); i++)
		{
			AntecedenteMorbidoMedico antMorbidoMedPredef = (AntecedenteMorbidoMedico) antecedentes.get(i);
			antecedentesTipoMedicamentosQururgicos.getListaDatos().add(llenarDetalleAntecedenteMedicoQuirurgico(antMorbidoMedPredef));
		}
		
		// Morbidos Médicos Otros
		antecedentes = morbidos.getAntecedentesMorbidosMedicosAdicionales();
		for(int i=0; i<antecedentes.size(); i++) 
		{
			AntecedenteMorbidoMedico antMorbidoMedOtro = (AntecedenteMorbidoMedico) antecedentes.get(i);
			antecedentesTipoMedicamentosQururgicos.getListaDatos().add(llenarDetalleAntecedenteMedicoQuirurgico(antMorbidoMedOtro)); 
		}
		
		// Morbidos Médicos Quirurgicos
		antecedentes = morbidos.getAntecedentesMorbidosQuirurgicos();
		for(int i=0; i<antecedentes.size(); i++)
		{
			AntecedenteMorbidoQuirurgico antMorbidoQuirurgico = (AntecedenteMorbidoQuirurgico) antecedentes.get(i);
			antecedentesTipoMedicamentosQururgicos.getListaDatos().add(llenarDetalleAntecedenteMedicoQuirurgico(antMorbidoQuirurgico)); 
		}
		
		
		if(!Utilidades.isEmpty(antecedentesTipoMedicamentosQururgicos.getListaDatos())){
			return antecedentesTipoMedicamentosQururgicos;
		}else return null;
	}
	
	
	/**
	 * Llena el dto generico con los antecedentes depentiendo del antecedente consutlado
	 * @param antMorbidoMedPredef
	 * @return {@link DtoDatosGenericos}
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	private DtoDatosGenericos llenarDetalleAntecedenteMedicoQuirurgico(AntecedenteMorbidoMedico morbido)
	{
		DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
		String detalleAntecedente = null;
		String etiqueta = null;
		
		if(!UtilidadTexto.isEmpty(morbido.getFechaInicio())){
			detalleAntecedente += morbido.getFechaInicio();
		}
		
		if(!UtilidadTexto.isEmpty(morbido.getTratamiento())){
			if(!UtilidadTexto.isEmpty(detalleAntecedente)){
				detalleAntecedente += " - "+morbido.getTratamiento();
			}else{
				detalleAntecedente += morbido.getTratamiento();
			}
		}

		if(!UtilidadTexto.isEmpty(morbido.getRestriccionDietaria())){
			if(!UtilidadTexto.isEmpty(detalleAntecedente)){
				detalleAntecedente += " - "+morbido.getRestriccionDietaria();
			}else{
				detalleAntecedente += morbido.getRestriccionDietaria();
			}
		}
		
		etiqueta = morbido.getNombre();
		datoGenericos = new DtoDatosGenericos();
		datoGenericos.setDato1(etiqueta);
		
		if(morbido.getFecha() != null){
			datoGenericos.setFecha(morbido.getFecha());
		}
		if(!UtilidadTexto.isEmpty(morbido.getHora())){
			datoGenericos.setHora(morbido.getHora());
		}
		
		datoGenericos.setDato2(etiqueta); 
		
		if(morbido.getFecha() != null){
			datoGenericos.setFecha(morbido.getFecha());
		}
		if(!UtilidadTexto.isEmpty(morbido.getHora())){
			datoGenericos.setHora(morbido.getHora());
		}
		
		if(!UtilidadTexto.isEmpty(detalleAntecedente)){
			datoGenericos.setDato2(detalleAntecedente);
		}
		
		return datoGenericos;
	}
	
	
	/**
	 * Llena el dto generico con los antecedentes depentiendo del antecedente consutlado
	 * @param antMorbidoMedPredef
	 * @return {@link DtoDatosGenericos}
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	private DtoDatosGenericos llenarDetalleAntecedenteMedicoQuirurgico(AntecedenteMorbidoQuirurgico morbido)
	{
		DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
		String detalleAntecedente = null;
		String etiqueta = null;
		
		if(!UtilidadTexto.isEmpty(morbido.getFecha())){
			detalleAntecedente += morbido.getFecha();
		}
		
		if(!UtilidadTexto.isEmpty(morbido.getRecomendaciones())){
			if(!UtilidadTexto.isEmpty(detalleAntecedente)){
				detalleAntecedente += " - "+morbido.getRecomendaciones();
			}else{
				detalleAntecedente += morbido.getRecomendaciones();
			}
		}

		if(!UtilidadTexto.isEmpty(morbido.getObservaciones())){
			if(!UtilidadTexto.isEmpty(detalleAntecedente)){
				detalleAntecedente += " - "+morbido.getObservaciones();
			}else{
				detalleAntecedente += morbido.getObservaciones();
			}
		}
		
		etiqueta = morbido.getNombre();
		datoGenericos = new DtoDatosGenericos();
		datoGenericos.setDato1(etiqueta);
		datoGenericos.setDato2(etiqueta); 
		
		if(morbido.getFecha_ant() != null){
			datoGenericos.setFecha(morbido.getFecha_ant());
		}
		if(!UtilidadTexto.isEmpty(morbido.getHora_ant())){
			datoGenericos.setHora(morbido.getHora_ant());
		}
		
		if(!UtilidadTexto.isEmpty(detalleAntecedente)){
			datoGenericos.setDato2(detalleAntecedente);
		}
		
		return datoGenericos;
	}

	
	/**
	 * Consulta los antecedentes pediatricos del paciente
	 * @param codigoPaciente
	 * @param con
	 * @return {@link DtoDatosGenericos}
	 *
	 * @autor Cristhian Murillo
	*/
	private DtoDatosGenericos cargarAntecedentesPediatricos(int codigoPaciente, Connection con) 
	{
		String tipoAntecedente = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Pediatricos");
		DtoDatosGenericos antecedentesTipoPediatricos = new DtoDatosGenericos();
		antecedentesTipoPediatricos.setDato1(tipoAntecedente);
		String etiqueta = null;
		String detalle = null;
		DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
		
		AntecedentePediatrico antecedente = new AntecedentePediatrico();
		boolean estaAntecedentePaciente = false;
		
		try {
			estaAntecedentePaciente = antecedente.cargar2(con, codigoPaciente);
			
			Date fecha 	= null;
			String hora = null;
			
			if(antecedente.getFecha() != null){
				fecha = antecedente.getFecha();
				hora = antecedente.getHora();
			}
			
			
			if(estaAntecedentePaciente)
			{
				antecedente.setInfoMadre(new InfoMadre() );
				antecedente.getInfoMadre().cargar(con, codigoPaciente);
				if(antecedente.getInfoMadre() != null)
				{
					detalle = "";
					
					if(!UtilidadTexto.isEmpty(antecedente.getInfoMadre().getPrimerApellido())){
						detalle += antecedente.getInfoMadre().getPrimerApellido()+" "; 	
					}
					if(!UtilidadTexto.isEmpty(antecedente.getInfoMadre().getSegundoApellido())){
						detalle += 	antecedente.getInfoMadre().getSegundoApellido()+" ";
					}
					if(!UtilidadTexto.isEmpty(	antecedente.getInfoMadre().getPrimerNombre())){
						detalle += 	antecedente.getInfoMadre().getPrimerNombre()+" ";
					}
					if(!UtilidadTexto.isEmpty(antecedente.getInfoMadre().getSegundoNombre())){
						detalle += 	antecedente.getInfoMadre().getSegundoNombre()+" ";
					}
					if(antecedente.getInfoMadre().getEdad() >0){
						detalle += 	antecedente.getInfoMadre().getEdad()+" ";
					}
					if(!UtilidadTexto.isEmpty(antecedente.getInfoMadre().getObservaciones())){
						detalle += 	antecedente.getInfoMadre().getObservaciones();
					}
					
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Madre");
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					
					if(fecha != null){
						datoGenericos.setFecha(fecha);
					}
					if(!UtilidadTexto.isEmpty(hora)){
						datoGenericos.setHora(hora);
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				
				}
				
				
				//--------------------
				antecedente.setInfoPadre(new InfoPadre() );
				antecedente.getInfoPadre().cargar(con, codigoPaciente);
				if(antecedente.getInfoPadre() != null)
				{
					detalle = "";
					
					if(!UtilidadTexto.isEmpty(antecedente.getInfoPadre().getPrimerApellido())){
						detalle += antecedente.getInfoPadre().getPrimerApellido()+" "; 	
					}
					if(!UtilidadTexto.isEmpty(antecedente.getInfoPadre().getSegundoApellido())){
						detalle += 	antecedente.getInfoPadre().getSegundoApellido()+" ";
					}
					if(!UtilidadTexto.isEmpty(	antecedente.getInfoPadre().getPrimerNombre())){
						detalle += 	antecedente.getInfoPadre().getPrimerNombre()+" ";
					}
					if(!UtilidadTexto.isEmpty(antecedente.getInfoPadre().getSegundoNombre())){
						detalle += 	antecedente.getInfoPadre().getSegundoNombre()+" ";
					}
					if(antecedente.getInfoPadre().getEdad() >0){
						detalle += 	antecedente.getInfoPadre().getEdad()+" ";
					}
					
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Padre");
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					
					if(fecha != null){
						datoGenericos.setFecha(fecha);
					}
					if(!UtilidadTexto.isEmpty(hora)){
						datoGenericos.setHora(hora);
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				
				PersonaBasica paciente = new PersonaBasica() ;
				paciente.setCodigoPersona(codigoPaciente);
				antecedente.setPaciente(paciente);
				antecedente.consultarPatologias(con, paciente.getCodigoPersona());
				
				//FIXME cargar
				/*
				// Si hay una presentación se inicializa este atributo 
				presentacionNacimientoStr = rs.getString("presentacion_nacimiento")+"";
				if( !UtilidadTexto.isEmpty(presentacionNacimientoStr))
				{
					// Averiguo el nombre de la presentación 
					presentacionNacimientoInt = new Integer(presentacionNacimientoStr).intValue();
					setPresentacionNacimiento(new InfoDatos(presentacionNacimientoStr, 
							iaps_dao.consultarNombrePresentacionNacimiento(con, presentacionNacimientoInt)));
				}
				
				// Puede haber otra presentación nacimiento
				else if( !UtilidadTexto.isEmpty(rs.getString("otra_presentacion_nacimiento")))
					setOtraPresentacionNacimiento(rs.getString("otra_presentacion_nacimiento"));

				// Si hay una ubicación del feto se inicializa este atributo 
				ubicacionFetoStr = rs.getString("ubicacion_feto");
				if( !UtilidadTexto.isEmpty(ubicacionFetoStr))
				{
					// Averiguo el nombre de la ubicación 
					setUbicacionFeto(
						new InfoDatos(
							ubicacionFetoStr,
							iaps_dao.consultarNombreUbicacionFeto(
								con,
								Integer.parseInt(ubicacionFetoStr)
							)
						)
					);
				}
				// Puede haber otra ubicación del feto 
				else if(!UtilidadTexto.isEmpty(rs.getString("otra_ubicacion_feto")))
					setOtraUbicacionFeto(rs.getString("otra_ubicacion_feto"));

				*/
						
				if(antecedente.getDuracionExpulsivoHoras() != ConstantesBD.codigoNuncaValido){
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.DuracionExpulsivo");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(antecedente.getDuracionExpulsivoHoras()+" hrs");
					if(antecedente.getDuracionExpulsivoMinutos() != ConstantesBD.codigoNuncaValido){
						datoGenericos.setDato2(antecedente.getDuracionExpulsivoHoras()+":"+antecedente.getDuracionExpulsivoMinutos());
					}
					
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(antecedente.getDuracionPartoHoras() != ConstantesBD.codigoNuncaValido){
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.DuracionParto");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(antecedente.getDuracionPartoHoras()+" hrs");
					if(antecedente.getDuracionPartoMinutos() != ConstantesBD.codigoNuncaValido){
						datoGenericos.setDato2(antecedente.getDuracionPartoHoras()+":"+antecedente.getDuracionPartoMinutos());
					}
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
		
				if(antecedente.getEdadGestacional() != ConstantesBD.codigoNuncaValido){
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.EdadGestacional");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(antecedente.getEdadGestacional()+"");
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(antecedente.getIntrauterinoAnormalidad() != ConstantesBD.codigoNuncaValido){
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.IntrauterinoAnormalidad");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(antecedente.getIntrauterinoAnormalidad()+"");
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
			
				if(antecedente.getPeso() != ConstantesBD.codigoNuncaValido){
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Peso");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(antecedente.getPeso()+"");
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
								
				if(antecedente.getRupturaMembranasHoras() != ConstantesBD.codigoNuncaValido){
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.RupturaMembrana");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(antecedente.getRupturaMembranasHoras()+" hrs");
					if(antecedente.getRupturaMembranasMinutos() != ConstantesBD.codigoNuncaValido){
						datoGenericos.setDato2(antecedente.getRupturaMembranasHoras()+":"+antecedente.getRupturaMembranasMinutos());
					}
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(antecedente.getTalla() != ConstantesBD.codigoNuncaValido){
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Talla");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(antecedente.getTalla()+"");
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
								
				if(antecedente.getPerimetroCefalico() != ConstantesBD.codigoNuncaValido){
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.PerimetroCefalico");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(antecedente.getPerimetroCefalico()+"");
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
						
				if(antecedente.getPerimetroToracico() != ConstantesBD.codigoNuncaValido){
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.PerimetroToracico");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(antecedente.getPerimetroToracico()+"");
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getAmnionitis())){
					detalle = antecedente.getAmnionitis();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Amnionitis");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getAmnionitisFactorAnormal())){
					detalle = antecedente.getAmnionitisFactorAnormal();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.AmnionitisFactorAnormal");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getAnestesia())){
					detalle = antecedente.getAnestesia();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Anestesia");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getAnestesiaMedicamentos())){
					detalle = antecedente.getAnestesiaMedicamentos();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.AnestesiaMedicamentos");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getAnestesiaMedicamentos())){
					detalle = antecedente.getAnestesiaMedicamentos();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.AnestesiaTipo");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getCaracteristicasLiquidoAmniotico())){
					detalle = antecedente.getCaracteristicasLiquidoAmniotico();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.CaracteristicasLiqAmniotico");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getCaracteristicasPlacenta())){
					detalle = antecedente.getCaracteristicasPlacenta();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.CaracteristicasPlacenta");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(antecedente.getCodigoSerologia() != ConstantesBD.codigoNuncaValido){
					detalle = antecedente.getCodigoSerologia()+"";
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.CodigoSerologia");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(antecedente.getCodigoTestSullivan() != ConstantesBD.codigoNuncaValido){
					detalle = antecedente.getCodigoTestSullivan()+"";
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.CodigoTestSullivan");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getComplicacionesParto())){
					detalle = antecedente.getComplicacionesParto();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.ComplicacionesParto");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getControlPrenatal())){
					detalle = antecedente.getControlPrenatal();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.ControlPrenatal");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getCordonUmbilicalCaracteristicas())){
					detalle = antecedente.getCordonUmbilicalCaracteristicas();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.CordonUmbilicalcaracteristicas");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getCordonUmbilicalDescripcion())){
					detalle = antecedente.getCordonUmbilicalDescripcion();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.CordonUmbilicalDescripcion");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getDescripcionSerologia())){
					detalle = antecedente.getDescripcionSerologia();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.DescripcionSerologia");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getDescripcionTestSullivan())){
					detalle = antecedente.getDescripcionTestSullivan();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.DescripcionTestSullivan");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getEdadGestacionalDescripcion())){
					detalle = antecedente.getEdadGestacionalDescripcion();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.EdadGestacionalDescripcion");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getEmbarazosAnterioresOtros())){
					detalle = antecedente.getEmbarazosAnterioresOtros();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.EmbarazosAnterioresOtros");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getFrecuenciaControlPrenatal())){
					detalle = antecedente.getFrecuenciaControlPrenatal();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.FrecuenciaControlPrenatal");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getGemelo())){
					detalle = antecedente.getGemelo();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Gemelo");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					if(!UtilidadTexto.isEmpty(antecedente.getGemeloDescripcion())){
						detalle += " - "+antecedente.getGemeloDescripcion();
					}
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getIncompatibilidadABO())){
					detalle = antecedente.getIncompatibilidadABO();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.IncompatibilidadAbo");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getIncompatibilidadRH())){
					detalle = antecedente.getIncompatibilidadRH();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.IncompatibilidadRh");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getIntrauterinoPeg())){
					detalle = antecedente.getIntrauterinoPeg();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.IntrauterinoPeg");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getIntrauterinoPegCausa())){
					detalle = antecedente.getIntrauterinoPegCausa();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.IntrauterinoPegCausa");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getIntrauterinoAnormalidadCausa())){
					detalle = antecedente.getIntrauterinoAnormalidadCausa();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.IntrauterinoPegCausa");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getIntrauterinoArmonico())){
					detalle = antecedente.getIntrauterinoArmonico();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.IntrauterinoAnormalidadCausa");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getIntrauterinoArmonicoCausa())){
					detalle = antecedente.getIntrauterinoArmonicoCausa();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.IntrauterinoArmonico");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getLiqAmnioticoClaro())){
					detalle = antecedente.getLiqAmnioticoClaro();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.LiqAmniotico");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getLiqAmnioticoMeconiado())){
					detalle = antecedente.getLiqAmnioticoMeconiado();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.LiqAmnioticoMeconiado");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getLiqAmnioticoMeconiadoGrado())){
					detalle = antecedente.getLiqAmnioticoMeconiadoGrado();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.LiqAmnioticoMeconiadoGrado");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getLiqAmnioticoSanguinolento())){
					detalle = antecedente.getLiqAmnioticoSanguinolento();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.LiqAmnioticoSanguinolento");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getLiqAmnioticoFetido())){
					detalle = antecedente.getLiqAmnioticoFetido();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.LiqAmnioticoFetido");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getLugarControlPrenatal())){
					detalle = antecedente.getLugarControlPrenatal();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.LugarControlPrenatal");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}

				if(!UtilidadTexto.isEmpty(antecedente.getMacrosomicos())){
					detalle = antecedente.getMacrosomicos();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Macrosomicos");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getMalformacionesCongenitas())){
					detalle = antecedente.getMalformacionesCongenitas();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.MalformacionesCongenitas");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getMortinatos())){
					detalle = antecedente.getMortinatos();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Mortinatos");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getMuertesFetalesTempranas())){
					detalle = antecedente.getMuertesFetalesTempranas();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Muertes_fetales_tempranas");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getMuestraCordonUmbilical())){
					detalle = antecedente.getMuestraCordonUmbilical();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.MuestraCordonUmbilical");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getMuestraCordonUmbilicalDescripcion())){
					detalle = antecedente.getMuestraCordonUmbilicalDescripcion();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.MuescorUumbilicalDes");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getNst())){
					detalle = antecedente.getNst();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Ns");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getNstDescripcion())){
					detalle = antecedente.getNstDescripcion();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.NstDescripcion");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getObservaciones())){
					detalle = antecedente.getObservaciones();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Observaciones");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getOtrosExamenesTrabajoParto())){
					detalle = antecedente.getOtrosExamenesTrabajoParto();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.OtrosExamenesTrabajoParto");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getPlacentaCaracteristicas())){
					detalle = antecedente.getPlacentaCaracteristicas();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.PlacentaCaracteristicas");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getPlacentaCaracteristicasDescripcion())){
					detalle = antecedente.getPlacentaCaracteristicasDescripcion();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.PlacentaCaracteristicas");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getPerfilBiofisico())){
					detalle = antecedente.getPerfilBiofisico();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.PerfilBiofisico");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getPerfilBiofisicoDescripcion())){
					detalle = antecedente.getPerfilBiofisicoDescripcion();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.PerfilBiofisicoDescripcion");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
			
				if(!UtilidadTexto.isEmpty(antecedente.getPrematuros())){
					detalle = antecedente.getPrematuros();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Prematuros");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getPtc())){
					detalle = antecedente.getPtc();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Ptc");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getPtcDescripcion())){
					detalle = antecedente.getPtcDescripcion();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.PtcDescripcion");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getReanimacion())){
					detalle = antecedente.getReanimacion();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Reanimacion");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getReanimacionAspiracion())){
					detalle = antecedente.getReanimacionAspiracion();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.ReanimacionAspiracion");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getReanimacionMedicamentos())){
					detalle = antecedente.getReanimacionMedicamentos();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.ReanimacionMedicamentos");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getSano())){
					detalle = antecedente.getSano();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Sano");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getSanoDescripcion())){
					detalle = antecedente.getSanoDescripcion();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.SanoDescripcion");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(!UtilidadTexto.isEmpty(antecedente.getSexoDescripcion())){
					detalle = antecedente.getSexoDescripcion();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.SexoDescripcion");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}

				if(!UtilidadTexto.isEmpty(antecedente.getHoraNacimiento())){
					detalle = antecedente.getHoraNacimiento();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.HoraNacimiento");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				
				if(!UtilidadTexto.isEmpty(antecedente.getMotivoTipoPartoOtro())){
					detalle = antecedente.getMotivoTipoPartoOtro();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.MotivoOtroTipoParto");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(antecedente.getTiposParto() !=null){
					Iterator itTiposParto = antecedente.getTiposParto().iterator();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.TipoParto");;
					while (itTiposParto.hasNext())
					{
						InfoDatosBD tiposParto = (InfoDatosBD) itTiposParto.next();
						detalle = tiposParto.getValue()+" - "+tiposParto.getDescripcion();
						datoGenericos = new DtoDatosGenericos();
						datoGenericos.setDato1(etiqueta);
						datoGenericos.setDato2(detalle);
						if(antecedente.getFecha() != null){
							datoGenericos.setFecha(antecedente.getFecha());
						}
						if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
							datoGenericos.setHora(antecedente.getHora()+"");
						}
						antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
					}
				}
				
				if(antecedente.getTipoTrabajoParto() != null){
					detalle = antecedente.getTipoTrabajoParto().getValue()+" - "+antecedente.getTipoTrabajoParto().getDescripcion();
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.TipoTrabajoParto");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(antecedente.getApgarMinuto1() != ConstantesBD.codigoNuncaValido){
					detalle = antecedente.getApgarMinuto1()+"";
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Apgar1");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
					
				if(antecedente.getApgarMinuto5() != ConstantesBD.codigoNuncaValido){
					detalle = antecedente.getApgarMinuto5()+"";
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Apgar5");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
				
				if(antecedente.getApgarMinuto10() != ConstantesBD.codigoNuncaValido){
					detalle = antecedente.getApgarMinuto10()+"";
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Apgar10");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}
					
				if(!UtilidadTexto.isEmpty(antecedente.getCausasSufrimientoFetal())){
					detalle = antecedente.getCausasSufrimientoFetal()+"";
					etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.SufrimientoFetal");;
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					if(antecedente.getFecha() != null){
						datoGenericos.setFecha(antecedente.getFecha());
					}
					if(!UtilidadTexto.isEmpty(antecedente.getHora()+"")){
						datoGenericos.setHora(antecedente.getHora()+"");
					}
					antecedentesTipoPediatricos.getListaDatos().add(datoGenericos);
				}

							
				/*
				// Ahora cargamos las inmunizaciones con sus dosis y/o observaciones 
				inmunizacionesDosis_rs = iaps_dao.consultarAntPediatricoInmunizacionesDosis(con, ai_codigoPaciente);
				inmunizacionesObser_rs = iaps_dao.consultarAntPediatricoInmunizacionesObser(con, ai_codigoPaciente);

				setInmunizacionesPediatricas(new ArrayList() );

				while(inmunizacionesDosis_rs.next() )
				{
					if( (tipoInmunizacion_ = inmunizacionesDosis_rs.getInt("tipo_inmunizacion") ) != tipoInmunizacion_ant)
					{
						inmunizaciones = new InmunizacionesPediatricas();
						inmunizaciones.setCodigoInmunizacion(tipoInmunizacion_);
						inmunizaciones.setNombreInmunizacion(inmunizacionesDosis_rs.getString("nombre") );

						laObservacion = iaps_dao.consultarObservacionInmunizacion(con, ai_codigoPaciente, tipoInmunizacion_);

						/Si no es cadena vacia es porque se encontraba en la bd, se debe asignar y poner estaEnBD en true 
						if(!laObservacion.equals("") )
						{
							inmunizaciones.setObservaciones(laObservacion);
							inmunizaciones.setEstaEnBD('t');
						}

						inmunizaciones.setDosis(new ArrayList() );

						getInmunizacionesPediatricas().add(inmunizaciones);

						// Agrego a la cadena de inmunizaciones con dosis el tipo de inmunización 
						tiposInmunizacionStr += "," + tipoInmunizacion_ + ",";
					}

					laDosis = new Dosis();
					laDosis.setFecha(inmunizacionesDosis_rs.getString("fecha") );

					li_aux = inmunizacionesDosis_rs.getInt("numero_dosis");

					if(li_aux < 0)
						laDosis.setRefuerzo(true);

					laDosis.setNumeroDosis(li_aux);
					laDosis.setEstaEnBD('t');

					inmunizaciones.getDosis().add(laDosis);

					tipoInmunizacion_ant = tipoInmunizacion_;
				}

				if(getInmunizacionesPediatricas().size() == 0)
					setInmunizacionesPediatricas(null);

				// Ahora buscando inmunizaciones que no tienen dosis pero que tienen observación 
				while(inmunizacionesObser_rs.next() )
				{
					if(getInmunizacionesPediatricas() == null)
					{
						setInmunizacionesPediatricas(new ArrayList() );

						inmunizaciones = new InmunizacionesPediatricas();
						inmunizaciones.setCodigoInmunizacion(inmunizacionesObser_rs.getInt("tipo_inmunizacion") );
						inmunizaciones.setNombreInmunizacion(inmunizacionesObser_rs.getString("nombre") );
						inmunizaciones.setObservaciones(inmunizacionesObser_rs.getString("observacion") );
						inmunizaciones.setEstaEnBD('t');

						getInmunizacionesPediatricas().add(inmunizaciones);
					}
					else
					{
						// ver si esta ya en el arreglo de inmunizaciones pedíatricas 
						tipoInmunizacion_ = inmunizacionesObser_rs.getInt("tipo_inmunizacion");

						if(tiposInmunizacionStr.indexOf("," + tipoInmunizacion_ + ",") == -1)
						{
							// En caso de no estar ahi se debe crear la inmunizacion y agregarla 
							inmunizaciones = new InmunizacionesPediatricas();

							inmunizaciones.setCodigoInmunizacion(inmunizacionesObser_rs.getInt("tipo_inmunizacion") );
							inmunizaciones.setNombreInmunizacion(inmunizacionesObser_rs.getString("nombre") );
							inmunizaciones.setObservaciones(inmunizacionesObser_rs.getString("observacion") );
							inmunizaciones.setEstaEnBD('t');

							getInmunizacionesPediatricas().add(inmunizaciones);
						}
					}
				}

			*/
							
							
			}
			
		} catch (SQLException e) {
			Log4JManager.error("Error consultando los antecedentes pedíatricos del paciente", e);
		}
		
		
		if(!Utilidades.isEmpty(antecedentesTipoPediatricos.getListaDatos())){
			return antecedentesTipoPediatricos;
		}else return null;
	}
	
	
	/**
	 * Consulta los antecedentes personales oculares del paciente
	 * @param codigoPaciente
	 * @param con
	 * @param codInstitucion
	 * @return {@link DtoDatosGenericos}
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	private DtoDatosGenericos cargarAntecedentesPersonalesOculares(int codigoPaciente, Connection con, int codInstitucion) 
	{
		String tipoAntecedente = fuenteMensaje.getMessage("AntecedentesPacienteMundo.PersonalesOculares");
		DtoDatosGenericos antecedentesTipoPersonalesOculares = new DtoDatosGenericos();
		antecedentesTipoPersonalesOculares.setDato1(tipoAntecedente);
		String etiqueta = null;
		String detalle = null;
		DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
		
		AntecedentesOftalmologicos  antecedentesOftalmologicos = new AntecedentesOftalmologicos(); 
		
		detalle = antecedentesOftalmologicos.cargarObservaciones(con, codigoPaciente);
		
		
		if(!UtilidadTexto.isEmpty(detalle))
		{
			datoGenericos = new DtoDatosGenericos();
			etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Observaciones");
			datoGenericos.setDato1(etiqueta);
			datoGenericos.setDato2(detalle);
			antecedentesTipoPersonalesOculares.getListaDatos().add(datoGenericos);
		}
		
		
		// Asignamos a los tipos de enfermedades personales por institución 
		Collection listadoEnferPerso = antecedentesOftalmologicos.consultarTiposEnferOftalPersonales(con,codInstitucion, codigoPaciente);
		
		// Consultar las enfermedades del usuario registradas anteriormente
		Collection listadoEnferOftaPersoPaciente = antecedentesOftalmologicos.consutarEnferOftalPersoPaciente(con, codigoPaciente);
		
		// Consultar los procedimientos quirurgicos oftalmologicos del paciente 
		Collection listadoEnferOftaPersoQuirurPaciente = antecedentesOftalmologicos.consutarEnferOftalPersoQuirurPaciente(con, codigoPaciente);
		
		
		Iterator itListadoEnferOftaPersoPaciente = listadoEnferOftaPersoPaciente.iterator();
		while (itListadoEnferOftaPersoPaciente.hasNext())
		{
			HashMap mapaListadoEnferOftaPersoPaciente = (HashMap) itListadoEnferOftaPersoPaciente.next();
			
			if(UtilidadTexto.isEmpty(mapaListadoEnferOftaPersoPaciente.get("tratamiento")+"") 
					|| UtilidadTexto.isEmpty(mapaListadoEnferOftaPersoPaciente.get("desde_cuando")+""))
			{
				Iterator itListadoEnferPerso = listadoEnferPerso.iterator();
				while (itListadoEnferPerso.hasNext())
				{
					HashMap mapaListadoEnferPerso = (HashMap) itListadoEnferPerso.next();
					if(mapaListadoEnferOftaPersoPaciente.get("codigo").equals(mapaListadoEnferPerso.get("codigo")))
					{
						if(!UtilidadTexto.isEmpty(mapaListadoEnferOftaPersoPaciente.get("tratamiento")+"")){
							detalle = mapaListadoEnferOftaPersoPaciente.get("tratamiento")+"";
							if(!UtilidadTexto.isEmpty(mapaListadoEnferOftaPersoPaciente.get("desde_cuando")+"")){
								detalle +=" - "+mapaListadoEnferOftaPersoPaciente.get("desde_cuando")+"";
							}
						}
						else{
							detalle = mapaListadoEnferOftaPersoPaciente.get("desde_cuando")+"";
						}
						
						etiqueta = mapaListadoEnferPerso.get("nombre")+"";
						datoGenericos = new DtoDatosGenericos();
						datoGenericos.setDato1(etiqueta);
						datoGenericos.setDato2(detalle);
						
						if(!UtilidadTexto.isEmpty(mapaListadoEnferOftaPersoPaciente.get("fecha")+"")){
							datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(mapaListadoEnferOftaPersoPaciente.get("fecha")+""));
						}
						if(!UtilidadTexto.isEmpty(mapaListadoEnferOftaPersoPaciente.get("hora")+"")){
							datoGenericos.setHora(mapaListadoEnferOftaPersoPaciente.get("hora")+"");
						}
						
						antecedentesTipoPersonalesOculares.getListaDatos().add(datoGenericos);
					}
				}
			}
		}
		
		
		
		Iterator itListadoEnferOftaPersoQuirurPaciente = listadoEnferOftaPersoQuirurPaciente.iterator();
		while (itListadoEnferOftaPersoQuirurPaciente.hasNext())
		{
			HashMap mapaListadoEnferOftaPersoQuirurPaciente = (HashMap) itListadoEnferOftaPersoQuirurPaciente.next();
			
			if(!UtilidadTexto.isEmpty(mapaListadoEnferOftaPersoQuirurPaciente.get("fecha")+"")){
				detalle = mapaListadoEnferOftaPersoQuirurPaciente.get("fecha")+"";
				if(!UtilidadTexto.isEmpty(mapaListadoEnferOftaPersoQuirurPaciente.get("causa")+"")){
					detalle +=" - "+mapaListadoEnferOftaPersoQuirurPaciente.get("causa")+"";
				}
			}
			else{
				detalle = mapaListadoEnferOftaPersoQuirurPaciente.get("causa")+"";
			}
			
			etiqueta = mapaListadoEnferOftaPersoQuirurPaciente.get("nombre_procedimiento as nombre_procedimiento")+"";
			datoGenericos = new DtoDatosGenericos();
			datoGenericos.setDato1(etiqueta);
			datoGenericos.setDato2(detalle);
			
			if(!UtilidadTexto.isEmpty(mapaListadoEnferOftaPersoQuirurPaciente.get("fecha_ant")+"")){
				datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(mapaListadoEnferOftaPersoQuirurPaciente.get("fecha_ant")+""));
			}
			if(!UtilidadTexto.isEmpty(mapaListadoEnferOftaPersoQuirurPaciente.get("hora_ant")+"")){
				datoGenericos.setHora(mapaListadoEnferOftaPersoQuirurPaciente.get("hora_ant")+"");
			}
			
			antecedentesTipoPersonalesOculares.getListaDatos().add(datoGenericos);
		}
		
		
		if(!Utilidades.isEmpty(antecedentesTipoPersonalesOculares.getListaDatos())){
			return antecedentesTipoPersonalesOculares;
		}
		
		else return null;
		
	}
	
	
	/**
	 * Consulta los antecedentes tóxicos del paciente
	 * @param codigoPaciente
	 * @param con
	 * @return {@link DtoDatosGenericos}
	 *
	 * @autor Cristhian Murillo
	 */
	private DtoDatosGenericos cargarAntecedentesToxicos(int codigoPaciente,	Connection con) 
	{
		String tipoAntecedente = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Toxicos");
		DtoDatosGenericos antecedentesTipoToxico = new DtoDatosGenericos();
		antecedentesTipoToxico.setDato1(tipoAntecedente);
		String etiqueta = null;
		String detalle = null;
		DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
		
		String tag = null;
		
		PersonaBasica paciente = new PersonaBasica();
		paciente.setCodigoPersona(codigoPaciente);
		AntecedentesToxicos toxicos = new AntecedentesToxicos();
		toxicos.setPaciente(paciente);
		
		toxicos.cargar(con);
		
		// Tóxicos Predefinidos		
		ArrayList listaToxicosPredefinidos = toxicos.getAntecedentesToxicosPredefinidos();
		for(int i=0; i<listaToxicosPredefinidos.size(); i++)
		{ 
			AntecedenteToxico antToxicoPredef = (AntecedenteToxico) listaToxicosPredefinidos.get(i);
			detalle = "";
			tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Actual");
			detalle += tag+":"+UtilidadTexto.convertirSN(antToxicoPredef.isActual())+" ";
			
			if(!UtilidadTexto.isEmpty(antToxicoPredef.getTiempoHabito())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.TiempoHabito");
				detalle += tag+":"+antToxicoPredef.getTiempoHabito()+" ";
			}
			if(!UtilidadTexto.isEmpty(antToxicoPredef.getCantidad())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Cantidad");
				detalle += tag+":"+antToxicoPredef.getCantidad()+" ";
			}
			if(!UtilidadTexto.isEmpty(antToxicoPredef.getFrecuencia())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Frecuencia");
				detalle += tag+":"+antToxicoPredef.getFrecuencia()+" ";
			}
			if(!UtilidadTexto.isEmpty(antToxicoPredef.getObservaciones())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Observaciones");
				detalle += tag+":"+antToxicoPredef.getObservaciones()+" ";
			}
			
			datoGenericos = new DtoDatosGenericos();
			etiqueta = antToxicoPredef.getNombre();
			datoGenericos.setDato1(etiqueta);
			datoGenericos.setDato2(detalle);
			
			if(antToxicoPredef.getFecha() != null){
				datoGenericos.setFecha(antToxicoPredef.getFecha());
			}
			if(!UtilidadTexto.isEmpty(antToxicoPredef.getHora())){
				datoGenericos.setHora(antToxicoPredef.getHora());
			}
			
			antecedentesTipoToxico.getListaDatos().add(datoGenericos);
		}
		
		// Tóxicos Otros		
		ArrayList listaToxicosOtros = toxicos.getAntecedentesToxicosAdicionales();
		for(int indice=0; indice<listaToxicosOtros.size(); indice++)
		{ 
			AntecedenteToxico antToxicoOtro = (AntecedenteToxico) listaToxicosOtros.get(indice);
			
			detalle = "";
			tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Actual");
			detalle += tag+":"+UtilidadTexto.convertirSN(antToxicoOtro.isActual())+" ";
			
			if(!UtilidadTexto.isEmpty(antToxicoOtro.getTiempoHabito())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.TiempoHabito");
				detalle += tag+":"+antToxicoOtro.getTiempoHabito()+" ";
			}
			if(!UtilidadTexto.isEmpty(antToxicoOtro.getCantidad())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Cantidad");
				detalle += tag+":"+antToxicoOtro.getCantidad()+" ";
			}
			if(!UtilidadTexto.isEmpty(antToxicoOtro.getFrecuencia())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Frecuencia");
				detalle += tag+":"+antToxicoOtro.getFrecuencia()+" ";
			}
			if(!UtilidadTexto.isEmpty(antToxicoOtro.getObservaciones())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Observaciones");
				detalle += tag+":"+antToxicoOtro.getObservaciones()+" ";
			}
			
			datoGenericos = new DtoDatosGenericos();
			etiqueta = antToxicoOtro.getNombre();
			datoGenericos.setDato1(etiqueta);
			datoGenericos.setDato2(detalle);
			
			if(antToxicoOtro.getFecha() != null){
				datoGenericos.setFecha(antToxicoOtro.getFecha());
			}
			if(!UtilidadTexto.isEmpty(antToxicoOtro.getHora())){
				datoGenericos.setHora(antToxicoOtro.getHora());
			}
			antecedentesTipoToxico.getListaDatos().add(datoGenericos);
			
		}		
		
		if(!Utilidades.isEmpty(antecedentesTipoToxico.getListaDatos())){
			return antecedentesTipoToxico;
		}
		
		else return null;
		
	}
	
	
	/**
	 * Consulta los antecedentes Transfunciones del paciente
	 * @param codigoPaciente
	 * @param con
	 * @return {@link DtoDatosGenericos}
	 *
	 * @autor Cristhian Murillo
	 */
	private DtoDatosGenericos cargarAntecedentesTransfusionales(int codigoPaciente, Connection con) 
	{
		String tipoAntecedente = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Transfusionales");
		DtoDatosGenericos antecedentesTipoTranfucionales = new DtoDatosGenericos();
		antecedentesTipoTranfucionales.setDato1(tipoAntecedente);
		String etiqueta = null;
		String detalle = null;
		String tag = null;
		DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
		
		AntecedentesTransfusionales transfusiones = new AntecedentesTransfusionales();
		transfusiones.cargar(con, codigoPaciente);

		
		for(int i =1; i<=transfusiones.getTransfusiones().size(); i++)
		{
			AntecedenteTransfusional transfusion = transfusiones.getTransfusion(i-1);
			detalle = "";
			
			if(!UtilidadTexto.isEmpty(transfusion.getComponente())){
				/*
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Componente");
				detalle += tag+":"+transfusion.getComponente()+" ";
				*/
				etiqueta = transfusion.getComponente(); //Puede darse el caso de ser nulo?
			}
			
			if(!UtilidadTexto.isEmpty(transfusion.getFecha())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Fecha");
				detalle += tag+":"+transfusion.getFecha()+" ";
			}
			
			if(!UtilidadTexto.isEmpty(transfusion.getCausa())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Causa");
				detalle += tag+":"+transfusion.getCausa()+" ";
			}
			
			if(!UtilidadTexto.isEmpty(transfusion.getLugar())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Lugar");
				detalle += tag+":"+transfusion.getLugar()+" ";
			}
			
			if(!UtilidadTexto.isEmpty(transfusion.getEdad())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Edad");
				detalle += tag+":"+transfusion.getEdad()+" ";
			}
			
			if(!UtilidadTexto.isEmpty(transfusion.getDonante())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Donante");
				detalle += tag+":"+transfusion.getDonante()+" ";
			}
			
			if(!UtilidadTexto.isEmpty(transfusion.getObservaciones())){
				tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Observaciones");
				detalle += tag+":"+transfusion.getObservaciones()+" ";
			}
			
			datoGenericos = new DtoDatosGenericos();
			datoGenericos.setDato1(etiqueta);
			datoGenericos.setDato2(detalle);
			
			if(transfusion.getFecha_at() != null){
				datoGenericos.setFecha(transfusion.getFecha_at());
			}
			if(!UtilidadTexto.isEmpty(transfusion.getHora_at())){
				datoGenericos.setHora(transfusion.getHora_at());
			}
			
			antecedentesTipoTranfucionales.getListaDatos().add(datoGenericos);
		}				
	
		
		if(!Utilidades.isEmpty(antecedentesTipoTranfucionales.getListaDatos())){
			return antecedentesTipoTranfucionales;
		}
		
		else return null;
	}
	
	
	/**
	 * Consulta los antecedentes de Vacunas del paciente
	 * @param codigoPaciente
	 * @param con
	 * @return {@link DtoDatosGenericos}
	 * @throws SQLException 
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	@SuppressWarnings("unchecked")
	private DtoDatosGenericos cargarAntecedentesVacunas(int codigoPaciente,	Connection con) throws SQLException 
	{
		String tipoAntecedente = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Vacunas");
		DtoDatosGenericos antecedentesTipoVacunas = new DtoDatosGenericos();
		antecedentesTipoVacunas.setDato1(tipoAntecedente);
		String etiqueta = null;
		String detalle = null;
		String tag = null;
		DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
		
		
		HashMap mapaParam = new HashMap();  
		AntecedentesVacunas mundo = new AntecedentesVacunas();
		
		// Se establece el código del paciente
		mapaParam.put("codigoPaciente", codigoPaciente+"");
		// Establecer el numero de la consulta 1.
		mapaParam.put("nroConsulta","1"); 
		
		HashMap mapaTiposInmunizacion = new HashMap();
		mapaTiposInmunizacion = mundo.consultarInformacion(con, mapaParam);
		
		// Establecer el numero de la consulta 2.
		mapaParam.put("nroConsulta","2");
		// Se consultan las observaciones de los antecedentes de vacunas del paciente 
		HashMap mapaTemp=new HashMap();
		
		mapaTemp.putAll(mundo.consultarInformacion(con, mapaParam));
		
		
		// Si el número de registros es igual a 1 se realiza la consulta de los datos
		if(UtilidadCadena.noEsVacio(mapaTemp.get("numRegistros")+""))
		{
			if(Integer.parseInt(mapaTemp.get("numRegistros")+"")==1)
			{
				etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.ObservacionesGenerales");
				detalle = mapaTemp.get("observaciones_0")+"";
				datoGenericos = new DtoDatosGenericos();
				datoGenericos.setDato1(etiqueta);
				datoGenericos.setDato2(detalle);
				antecedentesTipoVacunas.getListaDatos().add(datoGenericos);
			
				// Establecer el numero de la consulta 3.
				mapaParam.put("nroConsulta","3");
				
				//----Se consulta la posible información guardada al paciente de dosis, refuerzo y comentarios de las vacunas
				HashMap mapaDatosVacunas = mundo.consultarInformacion(con, mapaParam);
				
				int nroRowTipos = 0;
		        int nroRegDatos = 0;
    			
		        if ( util.UtilidadCadena.noEsVacio(mapaTiposInmunizacion.get("numRegistros")+"") )
    			{
    				nroRowTipos = Integer.parseInt(mapaTiposInmunizacion.get("numRegistros")+"");
    			}
    			
		        if ( util.UtilidadCadena.noEsVacio(mapaDatosVacunas.get("numRegistros")+"") )
    			{
    				nroRegDatos = Integer.parseInt(mapaDatosVacunas.get("numRegistros")+"");
    			}

				for (int vacuna = 0; vacuna<nroRowTipos; vacuna++) 
    			{
					int nroDosis = Integer.parseInt(mapaTiposInmunizacion.get("numero_dosis_" + vacuna)+"");
					int nroRefuerzos = Integer.parseInt(mapaTiposInmunizacion.get("numero_refuerzos_" + vacuna)+"");
					String codigoTipo = mapaTiposInmunizacion.get("codigo_tipo_" + vacuna)+"";
					String nombreVacuna = mapaTiposInmunizacion.get("nombre_tipo_" + vacuna)+"";
					
					for (int dosis = 1; dosis<=nroDosis; dosis++) 
    				{ 
						//etiqueta = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Vacuna");
						if(dosis>1)
						{
							int posicion=-1;
							for(int i=0; i<nroRegDatos; i++)
							{
							  if(codigoTipo.equals(mapaDatosVacunas.get("tipo_inmunizacion_"+i)+"") && (mapaDatosVacunas.get("numero_dosis_"+i)+"").equals(dosis+""))
								{
								  posicion=i;
								  break;
								}
							}
							
							etiqueta = nombreVacuna+"("+dosis+")";
							detalle = "";
							
							
							// Vacuna
							if(util.UtilidadCadena.noEsVacio(mapaDatosVacunas.get("texto_"+posicion)+""))
							{
								tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Dosis");
								detalle += tag+":"+mapaDatosVacunas.get("texto_"+posicion)+" ";
							}
							
							// Refuerzo
							if(dosis==1 && nroRefuerzos>0)
							{
								posicion=-1;
								for(int i=0; i<nroRegDatos; i++)
								{
									if(codigoTipo.equals(mapaDatosVacunas.get("tipo_inmunizacion_"+i)+"") && (mapaDatosVacunas.get("numero_dosis_"+i)+"").equals("-1"))
									{
										posicion=i;
										break;
									}
								}
								
								if(util.UtilidadCadena.noEsVacio(mapaDatosVacunas.get("texto_"+posicion)+""))
								{
									tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Refuerzo");
									detalle += tag+":"+mapaDatosVacunas.get("texto_"+posicion)+" ";
								}
						   	}
						   
							// Comentario
							if(dosis==1)
							{
								posicion=-1;
								for(int i=0; i<nroRegDatos; i++)
								{
									if(codigoTipo.equals(mapaDatosVacunas.get("tipo_inmunizacion_"+i)+"") && (mapaDatosVacunas.get("numero_dosis_"+i)+"").equals("-2"))
									{
										posicion=i;
										break;
									}
								}
								 
								if(util.UtilidadCadena.noEsVacio(mapaDatosVacunas.get("texto_"+posicion)+""))
								{
									String comentario=mapaDatosVacunas.get("texto_"+posicion)+"";
									 
									//-- Se le quitan los \n a la cadena  de curaciones y se le colocan <br> --//
									String comentarioMostrar=util.UtilidadTexto.observacionAHTML(comentario);
										
									tag = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Comentario");
									detalle += tag+":"+comentarioMostrar;
								}
						 	}
						 	
							if(!UtilidadTexto.isEmpty(detalle))
							{
								datoGenericos = new DtoDatosGenericos();
								datoGenericos.setDato1(etiqueta);
								datoGenericos.setDato2(detalle);
								
								if(mapaDatosVacunas.get("fecha_"+posicion) != null){
									datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(mapaDatosVacunas.get("fecha_"+posicion)+""));
								}
								if(mapaDatosVacunas.get("hora_"+posicion) != null){
									datoGenericos.setHora(mapaDatosVacunas.get("hora_"+posicion)+"");
								}
								
								antecedentesTipoVacunas.getListaDatos().add(datoGenericos);
							}
						}																		
    				} 
    			}
			}
		}
		
		if(!Utilidades.isEmpty(antecedentesTipoVacunas.getListaDatos())){
			return antecedentesTipoVacunas;
		}
		
		else return null;
	}
	
	
	
	
	/**
	 * Consulta los antecedentes Varios de un paciente
	 * @param codigoPaciente
	 * @return {@link DtoDatosGenericos}
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	private DtoDatosGenericos cargarAntecedentesVarios(int codigoPaciente) 
	{
		String tipoAntecedente = fuenteMensaje.getMessage("AntecedentesPacienteMundo.Varios");
		DtoDatosGenericos antecedentesTipoVarios = new DtoDatosGenericos();
		antecedentesTipoVarios.setDato1(tipoAntecedente);
		String etiqueta = null;
		String detalle = null;
		DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
		
		UtilidadTransaccion.getTransaccion().begin();
		
		ArrayList<AntecedentesVarios> listaAntecedentesVarios = antecedentesVariosMundo.obtenerAntecedentesVariosPaciente(codigoPaciente);
		ArrayList<Antecedentes> listaTiposAntecedentes = antecedentesMundo.listarAntecedentes();
		
		
		if(!Utilidades.isEmpty(listaAntecedentesVarios))
		{
			for (Antecedentes antecedente : listaTiposAntecedentes) 
			{
				etiqueta = antecedente.getDescripcion();
				detalle = "";
				String hora = null;
				Date fecha = null;
				
				for (AntecedentesVarios antecedentesVarios : listaAntecedentesVarios) 
				{
					if(antecedente.getId() == antecedentesVarios.getAntecedentes().getId())
					{
						detalle += antecedentesVarios.getDescripcion()+", ";
						
						// Uúltima fecha y hora
						hora = antecedentesVarios.getHora();
						fecha = antecedentesVarios.getFecha();
						
					}
				}
				
				
				if(!UtilidadTexto.isEmpty(detalle))
				{
					datoGenericos = new DtoDatosGenericos();
					datoGenericos.setDato1(etiqueta);
					datoGenericos.setDato2(detalle);
					
					datoGenericos.setFecha(fecha);
					datoGenericos.setHora(hora);
					
					antecedentesTipoVarios.getListaDatos().add(datoGenericos);
				}
			}
		}
		
		UtilidadTransaccion.getTransaccion().commit();

		if(!Utilidades.isEmpty(antecedentesTipoVarios.getListaDatos())){
			return antecedentesTipoVarios;
		}
		
		else return null;
		
	}
	
	
	
}
