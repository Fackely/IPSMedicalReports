package com.princetonsa.mundo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadCadena;
import util.UtilidadTexto;
import util.UtilidadValidacion;

import com.princetonsa.dao.AntecedentesVacunasDao;
import com.princetonsa.dao.DaoFactory;

/**
 * AntecedentesVacunas
 */
@SuppressWarnings("rawtypes")
public class AntecedentesVacunas {

	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(AntecedentesVacunas.class);
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private AntecedentesVacunasDao  antecedentesVacunasDao = null;
	
	//----------------------------------ATRIBUTOS DE LA CLASE --------------------------------------//
	/**
	 * Campo para las observaciones generales
	 */
	private String observacionesGrales;
	
	/**
	 * Mapa para el manejo de los tipos de inmunizaciones vacunas
	 */
	private HashMap mapaTiposInmunizacion;
	
	/**
	 * Mapa para guardar la información ingresada en antecedentes vacunas
	 */
	private HashMap mapaAntVacunas;
	
	
	/**
	 * Constructor de la clase, inicializa en vacío todos los atributos
	 */
	public AntecedentesVacunas ()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Este método inicializa los atributos de la clase con valores vacíos
	 */
	public void reset()
	{
		this.observacionesGrales="";
		this.mapaTiposInmunizacion = new HashMap();
		this.mapaAntVacunas = new HashMap();
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
			antecedentesVacunasDao = myFactory.getAntecedentesVacunasDao();
			wasInited = (antecedentesVacunasDao != null);
		}
		return wasInited;
	}
	
	
	/**
	 * Funcion para cosultar informacion 
	 * @param con
	 * @param mapaParam
	 * @return
	 * @throws SQLException 
	 */
	public HashMap consultarInformacion(Connection con, HashMap mapaParam) throws SQLException
	{
		if (antecedentesVacunasDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (AntecedentesVacunas - AntecedentesVacunasDao )");
		}
		
		return antecedentesVacunasDao.consultarInformacion(con, mapaParam); 
	}
	
	/**
	 * Método que inserta y modifica la información ingresada en antecedentes vacunas
	 * @param con
	 * @param codigoPaciente
	 * @param usuario
	 */
	public boolean insertarModificarAntecedentesVacunas(Connection con, int codigoPaciente, UsuarioBasico usuario) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean error=false;
		int resp=0;
		
		if (antecedentesVacunasDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (AntecedentesVacunasDao - insertarModificarAntecedentesVacunas )");
		}
		
		String loginUsuario=usuario.getLoginUsuario();
		String datosMedico= UtilidadTexto.agregarTextoAObservacion(null, null, usuario, false);
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		//---------Se inserta/modifica en antecedentes vacunas y se inserta en antecedentes pacientes --------//
		//-----Se verifica si ya existe antecedentes vacunas para insertar o modificar -------//
		if(UtilidadValidacion.existeAntecedenteVacunas(con, codigoPaciente))
		{
			resp=antecedentesVacunasDao.insertarModificarAntecedenteVacuna (con, codigoPaciente, this.observacionesGrales, false);
		}
		else
		{
			resp=antecedentesVacunasDao.insertarModificarAntecedenteVacuna (con, codigoPaciente, this.observacionesGrales, true);
		}
		
		
		if (this.getMapaTiposInmunizacion() != null && resp>0)
		{
			int numRegistros=Integer.parseInt(this.getMapaTiposInmunizacion("numRegistros")+"");
			
			for(int vacuna=0; vacuna <numRegistros; vacuna++)
			{
			int nroDosis = Integer.parseInt(this.getMapaTiposInmunizacion("numero_dosis_" + vacuna)+"");
			int codigoTipoInmunizacion = Integer.parseInt(this.getMapaTiposInmunizacion("codigo_tipo_" + vacuna)+"");
				//------Se itera las dosis del tipo de inmunizacion -------//
				for (int dosis = 1; dosis<=nroDosis; dosis++) 
				{ 
					String aclaracionDosis="";
					if(UtilidadCadena.noEsVacio(this.getMapaAntVacunas("aclaracion_"+codigoTipoInmunizacion+"_"+dosis)+""))
					{
						aclaracionDosis=this.getMapaAntVacunas("aclaracion_"+codigoTipoInmunizacion+"_"+dosis)+"";
					}
					//-----Si es diferente de vacío se inserta la aclaración de la dosis de la vacuna ---//
					if(UtilidadCadena.noEsVacio(aclaracionDosis))
					{
					 resp=antecedentesVacunasDao.insertarDosisRefuerzo(con, codigoPaciente, codigoTipoInmunizacion, dosis, aclaracionDosis, loginUsuario, datosMedico);
					}
					
					if(resp < 0)
					{
						error=true;
						break;
					}
				}//for dosis
				
				//---------Se inserta el refuerzo si tiene, se envía en dosis -1 --------------//
				String refuerzoVacuna="";
				if(UtilidadCadena.noEsVacio(this.getMapaAntVacunas("refuerzo_"+codigoTipoInmunizacion)+""))
				{
					refuerzoVacuna=this.getMapaAntVacunas("refuerzo_"+codigoTipoInmunizacion)+"";
				}
				//-----Si es diferente de vacío se inserta el refuezo de la vacuna ---//
				if(UtilidadCadena.noEsVacio(refuerzoVacuna))
				{
				resp=antecedentesVacunasDao.insertarDosisRefuerzo(con, codigoPaciente, codigoTipoInmunizacion, -1, refuerzoVacuna, loginUsuario, datosMedico);
				}
				
				//--------Se inserta o modifica el comentario de la vacuna ------------------//
				String comentarioVacuna="";
				String comentarioVacunaAnt="";
				
				if(UtilidadCadena.noEsVacio(this.getMapaAntVacunas("comentarioAnt_"+codigoTipoInmunizacion)+""))
				{
				  comentarioVacunaAnt=this.getMapaAntVacunas("comentarioAnt_"+codigoTipoInmunizacion)+"";
				}
				
				if(UtilidadCadena.noEsVacio(this.getMapaAntVacunas("comentario_"+codigoTipoInmunizacion)+""))
				{
					comentarioVacuna=this.getMapaAntVacunas("comentario_"+codigoTipoInmunizacion)+"";
					comentarioVacuna=(UtilidadTexto.agregarTextoAObservacion(comentarioVacunaAnt,comentarioVacuna,usuario, false));
				}
				else
				{
					comentarioVacuna=comentarioVacunaAnt;
				}
				
				
				
				//---------Se pasan las observaciones generales -----------//
				/*if (!forma.getObservacionesGralesNueva().trim().equals(""))
				{
					forma.setObservacionesGrales(UtilidadTexto.agregarTextoAObservacion(forma.getObservacionesGrales(),forma.getObservacionesGralesNueva(),usuario, false));
					mundo.setObservacionesGrales(forma.getObservacionesGrales());
				}
				else
				{
					mundo.setObservacionesGrales(forma.getObservacionesGrales());
				}*/
				
				//-----Si es diferente de vacío se inserta/modifica el comentario de vacuna ---//
				if(UtilidadCadena.noEsVacio(comentarioVacuna))
				{
				resp=antecedentesVacunasDao.insertarModificarComentarioVacuna(con, codigoPaciente, codigoTipoInmunizacion, comentarioVacuna, loginUsuario, datosMedico);
				}
				
			}//for vacunas
			
		}//if mapaTiposInmunizacion!=null
		
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error)
			{
				myFactory.abortTransaction(con);
				return false;
			}
		else
			{
			    myFactory.endTransaction(con);
			}
		
		return true;	
		
	}

//	------------------------------------- SETS Y GETS ---------------------------------------------------//
	
	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public HashMap getMapaTiposInmunizacion() {
		return mapaTiposInmunizacion;
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaTiposInmunizacion(HashMap mapaTiposInmunizacion) {
		this.mapaTiposInmunizacion = mapaTiposInmunizacion;
	}

	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public Object getMapaTiposInmunizacion(String key)
	{
		return mapaTiposInmunizacion.get(key);
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	@SuppressWarnings("unchecked")
	public void setMapaTiposInmunizacion(String key, Object obj) 
	{
		this.mapaTiposInmunizacion.put(key, obj);
	}

	/**
	 * @return Retorna observacionesGrales
	 */
	public String getObservacionesGrales() {
		return observacionesGrales;
	}

	/**
	 * @param Asigna observacionesGrales
	 */
	public void setObservacionesGrales(String observacionesGrales) {
		this.observacionesGrales = observacionesGrales;
	}

	/**
	 * @return Retorna mapaAntVacunas
	 */
	public HashMap getMapaAntVacunas() {
		return mapaAntVacunas;
	}

	/**
	 * @param Asigna mapaAntVacunas 
	 */
	public void setMapaAntVacunas(HashMap mapaAntVacunas) {
		this.mapaAntVacunas = mapaAntVacunas;
	}
	
	/**
	 * @return Retorna mapaAntVacunas.
	 */
	public Object getMapaAntVacunas(String key)
	{
		return mapaAntVacunas.get(key);
	}

	/**
	 * @param Asigna mapaAntVacunas.
	 */
	@SuppressWarnings("unchecked")
	public void setMapaAntVacunas(String key, Object obj) 
	{
		this.mapaAntVacunas.put(key, obj);
	}


}
