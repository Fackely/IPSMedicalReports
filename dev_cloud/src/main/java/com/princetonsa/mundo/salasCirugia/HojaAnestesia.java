/*
 * Creado en Febrero 2008
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.InfoDatos;
import util.Listado;
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.parametrizacion.ConstantesSeccionesParametrizables;
import util.salas.ConstantesBDSalas;
import util.salas.UtilidadesSalas;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.salasCirugia.HojaAnestesiaDao;
import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;
import com.princetonsa.dto.salasCirugia.DtoHojaAnestesia;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.parametrizacion.AccesosVascularesHA;
import com.princetonsa.mundo.parametrizacion.Eventos;
import com.servinte.axioma.dto.salascirugia.HojaAnestesiaDto;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * @author Jose Eduardo Arias Doncel
 * Febrero 2008
 */
public class HojaAnestesia
{
		
	//-----------------------------------------------------------------------------------------------
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/************************************************************************************************
	 * ATRIBUTOS HOJA DE ANESTESIA**********************************************
	 * *********************************************************************************************/
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//-----------------------------------------------------------------------------------------------
	
	Logger logger = Logger.getLogger(HojaAnestesia.class);
	
	/**
	 * ActionErrors errores
	 * */
	private ActionErrors errores = new ActionErrors();
	
	/**
	 * HashMap utilitario 
	 * */
	HashMap utilitarioMap;
	
	/**
	 * ArrayList utilitarioList 
	 * */
	ArrayList utilitarioList;
	
	/**
	 * ArrayList utilitarioList1 
	 * */
	ArrayList utilitarioList1;
	
	/**
	 * DtoHojaAnestesia
	 * */
	DtoHojaAnestesia dtoHojaAnes;
	
	/**
	 * String indicadorUtilitario
	 * */
	String  indicadorUtilitario= "";
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Indices***************************************************************************************** 
	/*************************************************************************************************/
	
	/**
	 * Indices para el manejo de las especialidades que Intervienen (Subseccion Especialidades que 
	 * intervienen y Cirujanos Principales. Seccion Informacion General)
	 * */
	public static final String [] indicesEspecialidadesInter = {"numeroSolicitud0_","especialidad1_","nombreEspecialidad2_","asignada3_","estabd4_","especialidadInter5","nombreEspecInter6","cirujanos7_"};
	
	/**
	 * Indices para el manejo de los Cirujanos Especialidades que Intervienen (Subseccion Especialidades que 
	 * intervienen y Cirujanos Principales. Seccion Informacion General)
	 * */
	public static final String [] indicesCirujanosPrincipales = {"consecutivo0_","numeroSolicitud1_","especialidad2_","nombreEspecialidad3_","profesional4_","nombreProfesional5_","asignada6_","cirujano7","nombreCirujano8","estabd9_"};
	
	/**
	 * Indices para el manejo de los Anestesiologos (Seccion Informacion General. Subseccion Anestesiologos )
	 * */
	public static final String [] indicesAnestesiologos = {"numeroSolicitud0_","profesional1_","nombreProfesional2_","fecha3_","hora4_","definitivo5_"};
	
	/**
	 * Indices para el manejo de la Via Aerea 
	 * */
	public static final String [] indicesViaAerea = {"codigo_0","numeroSolicitud1_","institucion2_"};
	
	/**
	 * Indices para el manejo del detalle de Via Aereas 
	 * */
	public static final String [] indicesDetViaAerea = {"viaArea0_","articulo1_","cantidad2_","generoConsumo3_","descripcionArticulo4_","codigoPkMatQx5_"};
	
	
	/**
	 * Indices para el manejo de la Hoja de Anestesia
	 * */
	public static final String [] indicesHojaAnestesia = {"numeroSolicitud0_","anestesiologoCobrable1_","fechaIniciaAnestesia2_","horaIniciaAnestesia3_","fechaFinalizaAnestesia4_","horaFinalizaAnestesia5_","preanestesia6_","datosMedico7_","finalizada8_","fechaFinalizada9_","horaFinalizada10_","fechaGrabacion11_","horaGrabacion12_","observacionesSignosVitales13_","institucion14_","tipoSolicitud15_","activarAnestesiologoCobrable16_","validacionCapitacion17_"};
		
	/**
	 * Indices para el manejo de las Solicitudes Cx
	 * */
	public static final String [] indicesSolicitudesCx = {"numeroSolicitud0_","codigoPeticion1_","fechaIngresoSala2_","horaIngresoSala3_","fechaFinalizaCx4_","horaFinalizaCx5_","indicadorQx6_","fechaSalidaSala7_","horaSalidaSala8_","codigoSalidaSalaPaciente9_","codigoSalidaPacienteCc10_","descripcionSalidaPacienteCc11_","fechaIniciaCx12_","horaIniciaCx13_"};
	
	/**
	 * Indices para el manejo de las Observaciones Generales
	 * */
	public static final String[] indicesObservacionesGenerales = {"codigo0_","numeroSolicitud1_","descripcion2_","fecha3_","hora4_","usuario5_","descripcionUsuario6_","nuevaObservacion7_"};
	
	/**
	 * Indices para el manejo de los medicamentos administrados en la Hoja de Anestesia  
	 * */
	public static final String[] indicesMedAdminHojaAnes = {"codigo0_","numeroSolicitud1_","articulo2_","indicadorOtroMed3_","cantidad4_","seccion5_","descripcionArticulo6_","estabd7_","suministrar8_","tipoLiquido9_","descripcionTipoLiquido10_","espos11_","tienejus12_","codigoPkMatQx13_"};
	
	/**
	 * Indices para el manejo del detalle de medicamentos administrados en la Hoja de Anestesia
	 * */
	public static final String [] indicesDetMedAdminHojaAnes = {"codigo0_","adminHojaAnes1_","dosis2_","fecha3_","hora4_","graficar5_","generoConsumo6_","otroMedicamento7_","sellocalidad8_"};
	
	/**
	 * Indices para el manejo de la infusiones de la hoja de anestesia
	 * */
	public static final String [] indicesInfusionesHojaAnes = {"codigo0_","numeroSolicitud1_","codigoMezcla2_","descripcionMezcla3_","otraInfusion4_","estabd5_","graficar6_","suspendido7_","graficarold6_","suspendidoold7_","hayOtrasInfusiones10","esOtraInfusion11_","codigoMezclaInsCc12_"};
	
	/**
	 * Indices para el manejo de la administracion de Infusiones de la Hoja de Anestesia
	 * */
	public static final String [] indicesAdmInfusionesHojaAnes = {"codigo0_","codInfoHojaAnes1_","fecha2_","hora3_","estabd4_","fechaold2_","horaold3_"};
	
	/**
	 * Indices para el manejo del detalle de las administraciones de Infusiones de la Hoja de Anestesia
	 * */
	public static final String [] indicesDetAdmInfusionesHojaAnes = {"codigo0_","codAdmInfoHojaAnes1_","articulo2_","descripcionArticulo3_","dosis4_","estabd5_","naturalezaArticulo6_","dosisold4_","generoConsumo8_","espos9_","codigoDetMateQx10_"};
	
	/**
	 * Indices para el manejo de la informacion de la hoja de anestesia balances liquidos
	 * */
	public static final String [] indicesBalancesLiquidosHojaAnes = {"numeroSolicitud0_","codigoLiquido1_","cantidad2_","descripcionLiquido3_","estabd4_"};
	
	/**
	 * Indices para el manejo de la informacion de la salidas sala paciente 
	 * */
	public static final String[] indicesSalidasSalaPaciente = {"consecutivo0_","numeroSolicitud1_","salidaPacienteCc2_","descripcionSalidaPaciente3_","fallece4_","fechaSalidaSala5","horaSalidaSala6","falleceCheck7","fechaFallece8","horaFallece9","diagFallece10"};
	
	//-----------------------------------------------------------------------------------------------
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/************************************************************************************************
	 * METODOS PARA LA SECCION DE INFORMACION GENERAL************************************************
	 * *********************************************************************************************/
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//-----------------------------------------------------------------------------------------------
	
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Metodos para la SubSeccion Especialidades que Intervienen y Cirujanos Principales***************
	//Seccion Informacion General
	/*************************************************************************************************/
	
	/**
	 * Insertar Especialidades Intervienen
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String especialidad
	 * @param String asignada
	 * @param String loginUsuario
	 * */
	public boolean insertarEspecialidadesIntervienen(Connection con,String numeroSolicitud,String especialidad,String asignada,String loginUsuario)
	{
		HashMap parametros = new HashMap();
		
		parametros.put(HojaAnestesia.indicesEspecialidadesInter[0],numeroSolicitud);
		parametros.put(HojaAnestesia.indicesEspecialidadesInter[1],especialidad);
		parametros.put(HojaAnestesia.indicesEspecialidadesInter[3],asignada);		
		
		parametros.put("usuarioModifica",loginUsuario);		
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
	
		if(getHojaAnestesiaDao().insertarEspecialidadesIntervienen(con, parametros))
			return true;
		else
			this.errores.add("descripcion",new ActionMessage("errors.problemasBd"));
		
		return false;			
	}
	
	//***********************************************************************************************************
	
	/**
	 * Verifica cuales son los regsitros con estado BD en No y los almacena
	 * @param Connection con
	 * @param HashMap datos (Indices Hoja Anestesia -> indicesCirujanosPrincipales) 
	 * @param String loginUsuario
	 * */
	public boolean insertarEspecialidadesIntervienen(Connection con, HashMap datos,String loginUsuario)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		
		for(int i = 0; i < numRegistros; i++)
		{
			if(datos.get(this.indicesEspecialidadesInter[4]+i).equals(ConstantesBD.acronimoNo))
			{
				if(!insertarEspecialidadesIntervienen(con, 
						datos.get(this.indicesEspecialidadesInter[0]+i).toString(),
						datos.get(this.indicesEspecialidadesInter[1]+i).toString(),
						datos.get(this.indicesEspecialidadesInter[3]+i).toString(),						
						loginUsuario))
					return false;
			}
		}
		
		return true;
	}
	
	//***********************************************************************************************************	
	
	/**
	 * Consultar Especialidades Intervienen
	 * @param Connection con
	 * @param String solicitud
	 * @param String especialidad (opcional)
	 * */
	public HashMap consultarEspecialidadesIntervienen(Connection con,String solicitud, String especialidad)
	{
		HashMap parametros = new HashMap();
		parametros.put("solicitud",solicitud);
		parametros.put("especialidad",especialidad);
		return getHojaAnestesiaDao().consultarEspecialidadesIntervienen(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Actualizar Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarEspecialidadesIntervienen(Connection con,HashMap parametros)
	{
		return getHojaAnestesiaDao().actualizarEspecialidadesIntervienen(con, parametros);
	}
	
	//***********************************************************************************************************	
	
	/**
	 * Eliminar Especialidades Intervienen y los medicos cirujanos relacionados con la especialidad
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String numeroSolicitud (requerido)
	 * @param String especialidad(opcional)
	 * @param String asignadoCirujado (estado de asignado del cirujano)
	 * */
	public boolean eliminarEspecialidadesIntervienen(Connection con,String numeroSolicitud, String especialidad, String asignadoCirujano)
	{
		HashMap parametros  = new HashMap();		
		
		parametros.put(this.indicesEspecialidadesInter[0], numeroSolicitud);
		parametros.put(this.indicesEspecialidadesInter[1], especialidad);
		
		//Elimina las cirujias que dependen de la especialidad
		this.eliminarCirujanosPrincipales(
				con,
				"", 
				numeroSolicitud, 
				especialidad, 
				asignadoCirujano,
				false);
			
		
		//Elimina las especialidades 
		if(!getHojaAnestesiaDao().eliminarEspecialidadesIntervienen(con, parametros))
		{
			this.errores.add("descripcion",new ActionMessage("errors.sinEliminar"));
			return false;
		}
		
		
		return true;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Elimina especialidades a partir del mapa con las llaves de Especialidades
	 * @param Connection con
	 * @param HashMap datos 
	 * @param String asignadoCirujano
	 * */
	public boolean eliminarEspecialidadesIntervienen(Connection con,HashMap datos,String asigandoCirujano)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
				
		for(int i = 0; i< numRegistros; i++)
		{
			if(!eliminarEspecialidadesIntervienen(con,
					datos.get(this.indicesEspecialidadesInter[0]+i).toString(),
					datos.get(this.indicesEspecialidadesInter[1]+i).toString(),					
					asigandoCirujano))
				return false;
		}	
		
		return true;
	}

	//***********************************************************************************************************
	
	
	/**
	 * Insertar Cirujanos Especialidades Intervienen
	 * @param Connection con
	 * @param String numero de Solicitud
	 * @param String especialidad
	 * @param String profesional
	 * @param String asignada
	 * @param String cirujano
	 * @param String loginUsuario
	 * */
	public boolean insertarCirujanosIntervienen(
			Connection con,
			String numeroSolicitud,
			String especialidad, 
			String profesional,
			String asignada,			
			String loginUsuario)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesCirujanosPrincipales[1],numeroSolicitud);
		parametros.put(this.indicesCirujanosPrincipales[2],especialidad);
		parametros.put(this.indicesCirujanosPrincipales[4],profesional);
		parametros.put(this.indicesCirujanosPrincipales[6],asignada);		
		
		parametros.put("usuarioModifica",loginUsuario);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		
		return getHojaAnestesiaDao().insertarCirujanosIntervienen(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Verifica cuales son los regsitros con estado BD en No y los almacena
	 * @param Connection con
	 * @param HashMap datos (Indices Hoja Anestesia -> indicesCirujanosPrincipales) 
	 * @param String loginUsuario
	 * */
	public boolean insertarCirujanosIntervienen(Connection con, HashMap datos,String loginUsuario)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		
		for(int i = 0; i < numRegistros; i++)
		{
			if(datos.get(this.indicesCirujanosPrincipales[9]+i).equals(ConstantesBD.acronimoNo))
			{
				if(!insertarCirujanosIntervienen(con, 
						datos.get(this.indicesCirujanosPrincipales[1]+i).toString(),
						datos.get(this.indicesCirujanosPrincipales[2]+i).toString(),
						datos.get(this.indicesCirujanosPrincipales[4]+i).toString(),
						datos.get(this.indicesCirujanosPrincipales[6]+i).toString(),						
						loginUsuario))
					return false;
			}
		}
		
		return true;
	}
	
	//***********************************************************************************************************	
	
	/**
	 * Consultar Cirujanos Especialidades Intervienen
	 * @param Connection con
	 * @param String solicitud
	 * @param String especialidad
	 * */
	public HashMap consultarCirujanosIntervienen(Connection con,String solicitud,String especialidad)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesCirujanosPrincipales[1],solicitud);
		parametros.put(this.indicesCirujanosPrincipales[2],especialidad);
		return getHojaAnestesiaDao().consultarCirujanosIntervienen(con, parametros);
	}

	//***********************************************************************************************************
	
	/**
	 * Consultar Cirujanos Especialidades Intervienen a partir del HashMap de especialidades
	 * @param Connection con
	 * @param String solicitud
	 * @param HashMap especialidad
	 * */
	public HashMap consultarCirujanosIntervienen(Connection con,HashMap especialidadesMap)
	{
		HashMap respuesta = new HashMap();
		int numRegistros = Integer.parseInt(especialidadesMap.get("numRegistros").toString());		
		
		for(int i = 0 ; i < numRegistros; i++)
		{
			respuesta.put("esp_"+i,this.consultarCirujanosIntervienen(
										con,
										especialidadesMap.get(this.indicesEspecialidadesInter[0]+i).toString(),
										especialidadesMap.get(this.indicesEspecialidadesInter[1]+i).toString()));						
		}

		return respuesta;
	}

	//***********************************************************************************************************
		
	/**
	 * Actualizar Cirujanos Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarCirujanosIntervienen(Connection con,HashMap parametros)
	{
		return getHojaAnestesiaDao().actualizarCirujanosIntervienen(con, parametros);
	}

	//***********************************************************************************************************
	
	/**
	 * Eliminar Especialidades Intervienen
	 * @param Connection con
	 * @param String consecutivo
	 * @param String numeroSolicitud
	 * @param String especialidad
	 * @param String asignado
	 * */
	public boolean eliminarCirujanosPrincipales(
			Connection con,
			String consecutivo, 
			String numeroSolicitud,
			String especialidad,
			String asignado,
			boolean generarError)
	{
		HashMap parametros = new HashMap();
		
		parametros.put(this.indicesCirujanosPrincipales[0],consecutivo);
		parametros.put(this.indicesCirujanosPrincipales[1],numeroSolicitud);
		parametros.put(this.indicesCirujanosPrincipales[2],especialidad);		
		parametros.put(this.indicesCirujanosPrincipales[6],asignado);
		
		if(!getHojaAnestesiaDao().eliminarCirujanosIntervienen(con, parametros))
		{
			if(generarError)
				this.errores.add("descripcion",new ActionMessage("errors.sinEliminar"));
			
			return false;
		}	
		
		return true;		
	}
	
	//***********************************************************************************************************
	
	/**
	 * Eliminar Especialidades Intervienen desde el HashMap de DatosEliminados
	 * @param Connection con
	 * @param HashMap datos	
	 * */
	public boolean eliminarCirujanosPrincipales(Connection con,HashMap datos)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		
		for(int i = 0; i < numRegistros; i++ )
		{
			if(!this.eliminarCirujanosPrincipales(
					con,
					datos.get(HojaAnestesia.indicesCirujanosPrincipales[0]+i).toString(),
					datos.get(HojaAnestesia.indicesCirujanosPrincipales[1]+i).toString(),
					datos.get(HojaAnestesia.indicesCirujanosPrincipales[2]+i).toString(),
					ConstantesBD.acronimoNo,
					true))							
				return false;				
		}
		
		return true;		
	}

	//***********************************************************************************************************	
	
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Metodos para la SubSeccion Anestesiologos*******************************************************
	//Seccion Informacion General
	/*************************************************************************************************/
	
	/**
	 * Insertar Anestesiologos
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String profesional
	 * @param String definitivo
	 * */
	public boolean insertarAnestesiologos(Connection con,String numeroSolicitud,String profesional,String definitivo)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesAnestesiologos[0],numeroSolicitud);
		parametros.put(this.indicesAnestesiologos[1],profesional);
		parametros.put(this.indicesAnestesiologos[3],UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put(this.indicesAnestesiologos[4],UtilidadFecha.getHoraActual());
		parametros.put(this.indicesAnestesiologos[5],definitivo);
		
		if(getHojaAnestesiaDao().insertarAnestesiologos(con, parametros))
		{
			logger.info("--------------------------------");
			logger.info("Inserto el Anestesiologo >>>");
			logger.info("--------------------------------");
			return true;
		}
		else
		{
			this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Insertar El Anestesiologo"));
			return false;
		}
	}
	
	//***********************************************************************************************************	
	
	/**
	 * Consultar los Anestesiologos
	 * @param Connection con
	 * @param String numeroSolicitud (requerido)
	 * @param String profesional (opcional)
	 * @param String definitivo (opcional)
	 * @param String UsuarioBasico usuario
	 * @param boolean llenarDefault 
	 * */
	public HashMap consultarAnestesiologos(
			Connection con,
			String numeroSolicitud, 
			String profesional,
			String definitivo,
			UsuarioBasico usuario,
			boolean llenarDefault)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesAnestesiologos[0],numeroSolicitud);
		parametros.put(this.indicesAnestesiologos[1],profesional);
		parametros.put(this.indicesAnestesiologos[5],definitivo);
		
		parametros = getHojaAnestesiaDao().consultarAnestesiologos(con, parametros);
		
		if(llenarDefault && parametros.get("numRegistros").equals("0"))
		{
			parametros.put("numRegistros","1");
			parametros.put(this.indicesAnestesiologos[0]+"0",numeroSolicitud);
			parametros.put(this.indicesAnestesiologos[1]+"0",usuario.getCodigoPersona());
			parametros.put(this.indicesAnestesiologos[2]+"0",usuario.getNombreUsuario());
			parametros.put(this.indicesAnestesiologos[3]+"0",UtilidadFecha.getFechaActual());
			parametros.put(this.indicesAnestesiologos[4]+"0",UtilidadFecha.getHoraActual());
			parametros.put(this.indicesAnestesiologos[5]+"0","");				
		}
		
		return parametros;	  
	}
	
	//***********************************************************************************************************	
	
	/**
	 * Consultar si existe o no el anestesiologo
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String profesional
	 * */
	public boolean consultarExisteAnestesiologo(Connection con,String numeroSolicitud,String profesional)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesAnestesiologos[0],numeroSolicitud);
		parametros.put(this.indicesAnestesiologos[1],profesional);
		
		return getHojaAnestesiaDao().consultarExisteAnestesiologo(con, parametros);			
	}
	
	//***********************************************************************************************************
	
	/**
	 * Actualizar Anestesiologos
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String profesional
	 * */
	public boolean actualizarAnestesiologos(Connection con,String numeroSolicitud, String profesional)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesAnestesiologos[0],numeroSolicitud);
		parametros.put(this.indicesAnestesiologos[1],profesional);
		parametros.put(this.indicesAnestesiologos[3],UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put(this.indicesAnestesiologos[4],UtilidadFecha.getHoraActual());

		if(getHojaAnestesiaDao().actualizarAnestesiologos(con, parametros))
		{
			logger.info("--------------------------------");
			logger.info("Actualizo el Anestesiologo>>>");
			logger.info("--------------------------------");	
			return true;
		}
		else
		{
			this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar El Anestesiologo"));
			return false;
		}
	}
	
	//***********************************************************************************************************
	
	/**
	 * Actualizar el campo cobrable de la Hoja de Anestesia
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String anestesiologoCobrable
	 * */
	public boolean actualizarCobrable(Connection con,String numeroSolicitud, String anestesiologoCobrable)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesHojaAnestesia[1],anestesiologoCobrable);
		parametros.put(this.indicesHojaAnestesia[0],numeroSolicitud);
		
		return getHojaAnestesiaDao().actualizarCobrable(con, parametros);
	}
	
	//***********************************************************************************************************	
	
	/**
	 * Actualizar el campo definitivo para el cobro de honorarios
	 * @param Connection con
	 * @param HashMap datos con indices de HojaAnestesia.indicesAnestesiologia[]
	 * */
	public boolean actualizarDefinitivoHonorarios(Connection con,HashMap datos)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		String indicesActualizar = "";
		boolean indicadorExisteSi = false;
							
		if(datos.containsKey(this.indicesAnestesiologos[5]) 
				&& !datos.get(this.indicesAnestesiologos[5]).toString().equals(""))
			indicesActualizar = datos.get(this.indicesAnestesiologos[5]).toString();
		
		//Si el Indicador es diferente de vacio
		if(!indicesActualizar.equals(""))
		{ 
			for(int i = 0 ; i < numRegistros; i++)
			{	
				if(datos.get(this.indicesAnestesiologos[5]+i).toString().equals(ConstantesBD.acronimoSi))
				{
					//Encontro que existe un registro marcado como Si
					indicadorExisteSi = true;
					 
					//Se evalua si el campo en Si del cobro de Honorarios cambio de Cirujano
					if(i != Integer.parseInt(indicesActualizar))
					{			
						//Actualizo el Indicador definitivo a NO para el medico que TENIA el cobro de Honorarios
						if(this.actualizarDefinitivoHonorarios(con,
								datos.get(this.indicesAnestesiologos[0]+i).toString(),
								datos.get(this.indicesAnestesiologos[1]+i).toString(),
								ConstantesBD.acronimoNo))
						{
							
							//Actualizo el Indicador definitivo a SI para el medico que TIENE el cobro de Honorarios
							if(this.actualizarDefinitivoHonorarios(con,
									datos.get(this.indicesAnestesiologos[0]+indicesActualizar).toString(),
									datos.get(this.indicesAnestesiologos[1]+indicesActualizar).toString(),
									ConstantesBD.acronimoSi))
							{							
								return true;
							}								
						}						
						
						return false;
					}
					
					return true;
				}
			}
		}
		
		//Si no Existen registros marcados como Si
		if(!indicadorExisteSi 
				&& !indicesActualizar.equals(""))
		{
			if(this.actualizarDefinitivoHonorarios(con,
					datos.get(this.indicesAnestesiologos[0]+indicesActualizar).toString(),
					datos.get(this.indicesAnestesiologos[1]+indicesActualizar).toString(),
					ConstantesBD.acronimoSi))
				return true;
			else
				return false;
		}
		
		
		return true;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Actualizar el campo definitivo para el cobro de honorarios
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String profesional
	 * @param String definitivo
	 * */
	public boolean actualizarDefinitivoHonorarios(Connection con,String numeroSolicitud, String profesional, String definitivo)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesAnestesiologos[0],numeroSolicitud);
		parametros.put(this.indicesAnestesiologos[1],profesional);
		parametros.put(this.indicesAnestesiologos[5],definitivo);
		return this.getHojaAnestesiaDao().actualizarDefinitivoHonorarios(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Consultar la informacion de la Hoja de Anestesia
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String institucion
	 * @param String tipoSolicitud 
	 * */
	public  HashMap consultarHojaAnestesia(Connection con,String numeroSolicitud,int institucion,String tipoSolicitud)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesHojaAnestesia[0],numeroSolicitud);		
		parametros = this.getHojaAnestesiaDao().consultarHojaAnestesia(con, parametros);
		
		int numRegistros = Integer.parseInt(parametros.get("numRegistros").toString());		
				
		if(numRegistros > 0)
		{			
			for(int i = 0; i <numRegistros; i++)
			{
				if(tipoSolicitud.equals(""))
					tipoSolicitud =  parametros.get(this.indicesHojaAnestesia[15]+i).toString();
				
				if(parametros.get(this.indicesHojaAnestesia[1]+i).equals(""))				
					parametros.put(this.indicesHojaAnestesia[1]+i,ConstantesBD.acronimoSi);				
				
				parametros.put(this.indicesHojaAnestesia[16]+i,this.getActivarHonorarioCobrableHojaAnes(institucion, tipoSolicitud));
			}
		}
		else
		{
			parametros.put(this.indicesHojaAnestesia[1]+"0",ConstantesBD.acronimoSi);
			parametros.put(this.indicesHojaAnestesia[16]+"0",this.getActivarHonorarioCobrableHojaAnes(institucion, tipoSolicitud));
		}
			
		return parametros;		
	}
	
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Metodos para la SubSeccion Anestesiologos*******************************************************
	//Seccion Informacion General
	/*************************************************************************************************/
	
	/**
	 * Consultar cantidades detalle via aerea
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String institucion
	 * */
	public HashMap consultarCantidadesDetalleViaAerea(Connection con,String numeroSolicitud,String institucion)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesViaAerea[1],numeroSolicitud);
		parametros.put(this.indicesViaAerea[2],institucion);
		return this.getHojaAnestesiaDao().consultarCantidadesDetalleViaAerea(con, parametros);
	}
	
	//************************************************************************************************
	
	/**
	 * Actualizar el indicativo del detalle de la via aerea
	 * @param Connection con
	 * @param String generoConsumo
	 * @param String viaAerea
	 * @param String articulo
	 * */
	public boolean actualizarGenConsuDetViaArea(Connection con,String generoConsumo,String viaAerea,String articulo,int codigoDetMateQx)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesDetViaAerea[3],generoConsumo);
		parametros.put(this.indicesDetViaAerea[0],viaAerea);
		parametros.put(this.indicesDetViaAerea[1],articulo);
		parametros.put(this.indicesDetViaAerea[5],codigoDetMateQx);
		return this.getHojaAnestesiaDao().actualizarGenConsuDetViaArea(con, parametros);
	}
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Metodos para la SubSeccion Fecha y hora de Ingreso a la Sala************************************
	//Seccion Informacion General
	/*************************************************************************************************/
	
	/**
	 * Consulta la informacion de la Solicitud de Cirugia
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public HashMap consultarInfoSolicitudCx(Connection con, String numeroSolicitud)
	{
		HashMap parametros = new HashMap();
		SolicitudesCx solicitudCx = new SolicitudesCx();		
		solicitudCx.cargarEncabezadoSolicitudCx(con, numeroSolicitud);
		
		parametros.put(this.indicesSolicitudesCx[0],solicitudCx.getNumeroSolicitud());		
		parametros.put(this.indicesSolicitudesCx[1],solicitudCx.getCodigoPeticion());
		
		if(!solicitudCx.getFechaIngresoSala().equals(""))	
			parametros.put(this.indicesSolicitudesCx[2],solicitudCx.getFechaIngresoSala());
		else
			parametros.put(this.indicesSolicitudesCx[2],UtilidadFecha.getFechaActual());
		
		if(!solicitudCx.getHoraIngresoSala().equals(""))
			parametros.put(this.indicesSolicitudesCx[3],solicitudCx.getHoraIngresoSala());
		else
			parametros.put(this.indicesSolicitudesCx[3],"");
		
		//Fecha/Hora Inicial Cirugia
		parametros.put(this.indicesSolicitudesCx[12],solicitudCx.getFechaInicialCx());
		parametros.put(this.indicesSolicitudesCx[13],solicitudCx.getHoraInicialCx());		
		
		//Fecha/Hora Finaliza Cirugia
		parametros.put(this.indicesSolicitudesCx[4],solicitudCx.getFechaFinalCx());
		parametros.put(this.indicesSolicitudesCx[5],solicitudCx.getHoraFinalCx());
		
		//Fecha/Hora Salida Sala
		parametros.put(this.indicesSolicitudesCx[7],solicitudCx.getFechaSalidaSala());
		parametros.put(this.indicesSolicitudesCx[8],solicitudCx.getHoraSalidaSala());
		
		//Codigo Salida Paciente
		parametros.put(this.indicesSolicitudesCx[9],solicitudCx.getCodigoSalidaSalaPaciente());
		//Codigo Salida Paciente Centro Costo
		parametros.put(this.indicesSolicitudesCx[10],solicitudCx.getCodigoSalidaPacienteCc());
		//Descripcion Salida Paciente Centro Costo
		parametros.put(this.indicesSolicitudesCx[11],solicitudCx.getDescripcionSalidaPacienteCc());
		
		//Indicador QX
		parametros.put(this.indicesSolicitudesCx[6],solicitudCx.getIndQx());
						
		return parametros;
	}
	
	
	//***********************************************************************************************************	
	
	/**
	 * Actualizar la fecha y la hora de ingreso a la Sala
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarFechaHoraIngreso(Connection con,String numeroSolicitud,String fechaIngreso, String horaIngreso)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesSolicitudesCx[0],numeroSolicitud);
		parametros.put(this.indicesSolicitudesCx[2],UtilidadFecha.conversionFormatoFechaABD(fechaIngreso));
		parametros.put(this.indicesSolicitudesCx[3],horaIngreso);
		return this.getHojaAnestesiaDao().actualizarFechaHoraIngreso(con, parametros);		
	}
	
	//***********************************************************************************************************
	
	
	//-----------------------------------------------------------------------------------------------
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/************************************************************************************************
	 * METODOS SECCION OBSERVACIONES GENERALES*******************************************************
	 * *********************************************************************************************/
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//-----------------------------------------------------------------------------------------------	
	
	
	/**
	 * Consultar Observaciones Generales
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public HashMap consultarObservacionesGenerales(Connection con,String numeroSolicitud)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesObservacionesGenerales[1],numeroSolicitud);		
		parametros = this.getHojaAnestesiaDao().consultarObservacionesGenerales(con, parametros);
		
		UsuarioBasico usuario = new UsuarioBasico();
		
		try
		{
			//Almacena la informacion del usuario
			for(int i = 0; i<Integer.parseInt(parametros.get("numRegistros").toString()); i++)
			{
				usuario.cargarUsuarioBasico(parametros.get(this.indicesObservacionesGenerales[5]+i).toString());
				parametros.put(this.indicesObservacionesGenerales[6]+i,usuario.getInformacionGeneralPersonalSalud());									
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return parametros;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Insertar Observaciones Generales
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String descripcion	
	 * @param String loginUsuario
	 * */
	public boolean insertarObservacionesGenerales(Connection con,
			String numeroSolicitud,
			String descripcion,			
			String loginUsuario)
	{
		HashMap parametros = new HashMap();
		
		parametros.put(this.indicesObservacionesGenerales[1],numeroSolicitud);
		parametros.put(this.indicesObservacionesGenerales[2],descripcion);
		parametros.put(this.indicesObservacionesGenerales[3],UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put(this.indicesObservacionesGenerales[4],UtilidadFecha.getHoraActual());
		parametros.put(this.indicesObservacionesGenerales[5],loginUsuario);
		
		if(this.getHojaAnestesiaDao().insertarObservacionesGenerales(con,parametros))
			return true; 
		else
		{	
			this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error en la Inserccion de la Observacion General"));
			return false;
		}
	}
	
	
	//-----------------------------------------------------------------------------------------------
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/************************************************************************************************
	 * METODOS SECCION ANESTESICOS Y MEDICAMENTOS SUMINISTRADOS**************************************
	 * METODOS SECCION LIQUIDOS**********************************************************************
	 * METODOS SECCION INFUSIONES********************************************************************
	 * *********************************************************************************************/
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//-----------------------------------------------------------------------------------------------
	
	
	/**
	 * Inserta la informacion de los medicamentos administrados a partir de un mapa con llaves de indicesMedAdminHojaAnes
	 * @param Connection con
	 * @param HashMap datos
	 * @param String loginUsuario
	 * @param int codigoSeccion
	 * */
	public boolean insertarSeccioinsertarAdminisHojaAnest(Connection con, HashMap datos,String loginUsuario,int codigoSeccion)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		int consecutivo = ConstantesBD.codigoNuncaValido;
		String tmp = "";
		boolean respuesta = true;
		
		for(int i = 0; i < numRegistros; i++ )
		{			
			if(datos.get(this.indicesMedAdminHojaAnes[7]+i).toString().equals(ConstantesBD.acronimoNo)  && 
					datos.get(this.indicesMedAdminHojaAnes[8]+i).toString().equals(ConstantesBD.acronimoSi))
			{							
				consecutivo = insertarAdminisHojaAnest(con, 
						datos.get(this.indicesMedAdminHojaAnes[1]).toString(), 
						datos.get(this.indicesMedAdminHojaAnes[2]+i).toString(),
						datos.get(this.indicesMedAdminHojaAnes[9]+i).toString(),
						datos.get(this.indicesDetMedAdminHojaAnes[7]+i).toString(), 
						"",
						codigoSeccion, 
						loginUsuario);
				
				if(consecutivo != ConstantesBD.codigoNuncaValido)
				{
					
					tmp = "";
					if(datos.containsKey(this.indicesDetMedAdminHojaAnes[8]+i))
						tmp = datos.get(this.indicesDetMedAdminHojaAnes[8]+i).toString();
					
					if(insertarDetaAdminisHojaAnest(con,
							consecutivo+"", 
							datos.get(this.indicesDetMedAdminHojaAnes[2]+i).toString(), 
							tmp,
							UtilidadFecha.conversionFormatoFechaABD(datos.get(this.indicesDetMedAdminHojaAnes[3]+i).toString()),
							datos.get(this.indicesDetMedAdminHojaAnes[4]+i).toString(), 
							datos.get(this.indicesDetMedAdminHojaAnes[5]+i).toString(),
							datos.get(this.indicesDetMedAdminHojaAnes[6]+i).toString(),
							loginUsuario))
						respuesta =  true;
					else
					{
						this.errores.add("descripcion",new ActionMessage("errors.problemasBd"));
						respuesta = false;
					}
				}
				else			
				{									
					this.errores.add("descripcion",new ActionMessage("errors.problemasBd"));
					respuesta =  false;
				}
			}
			else if(datos.get(this.indicesMedAdminHojaAnes[7]+i).toString().equals(ConstantesBD.acronimoSi)  && 
						datos.get(this.indicesMedAdminHojaAnes[8]+i).toString().equals(ConstantesBD.acronimoSi))
			{
				
				tmp = "";
				if(datos.containsKey(this.indicesDetMedAdminHojaAnes[8]+i))
					tmp = datos.get(this.indicesDetMedAdminHojaAnes[8]+i).toString();
				
				//Inserta un registro como una nueva dosis de medicamento
				if(insertarDetaAdminisHojaAnest(
						con,
						datos.get(this.indicesMedAdminHojaAnes[0]+i).toString(), 
						datos.get(this.indicesDetMedAdminHojaAnes[2]+i).toString(),
						tmp,
						UtilidadFecha.conversionFormatoFechaABD(datos.get(this.indicesDetMedAdminHojaAnes[3]+i).toString()),
						datos.get(this.indicesDetMedAdminHojaAnes[4]+i).toString(), 
						datos.get(this.indicesDetMedAdminHojaAnes[5]+i).toString(),
						datos.get(this.indicesDetMedAdminHojaAnes[6]+i).toString(),
						loginUsuario))
					respuesta =  true;
				else
				{
					this.errores.add("descripcion",new ActionMessage("errors.problemasBd"));
					respuesta =  false;
				}
			}				
		}
		
		return respuesta;		
	}

	
	//***********************************************************************************************************
	
	
	/**
	 * Insertar Administraciones Hoja de Anestesia
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String articulo
	 * @param String tipoLiquidos
	 * @param String otroMedicamento
	 * @param int cantidad
	 * @param int seccion
	 * @return int numero del consecutivo con que se genero el registro 
	 * */
	public int insertarAdminisHojaAnest(Connection con,
			String numeroSolicitud,
			String articulo,
			String tipoLiquidos,
			String otroMedicamento,
			String cantidad,			
			int seccion,
			String usuarioModifica)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesMedAdminHojaAnes[1],numeroSolicitud);
		parametros.put(this.indicesMedAdminHojaAnes[2],articulo);
		parametros.put(this.indicesMedAdminHojaAnes[9],tipoLiquidos);		
		parametros.put(this.indicesMedAdminHojaAnes[3],otroMedicamento);
		parametros.put(this.indicesMedAdminHojaAnes[4],cantidad);
		parametros.put(this.indicesMedAdminHojaAnes[5],seccion);		
		
		parametros.put("usuarioModifica",usuarioModifica);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		
		return this.getHojaAnestesiaDao().insertarAdminisHojaAnest(con, parametros);
	}
	
	//***********************************************************************************************************
	
	
	/**
	 * Consultar Administraciones Hoja Anestesia 
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String seccion
	 * @param String calcularCantidad (S/N)
	 * @param String estadoIndicativoConsumoGenerado (opcional S/N o vacio -> se calcula la cantidad sin importar el estado)
	 * */
	public HashMap consultarAdminisHojaAnest(Connection con,
			String numeroSolicitud, 
			String seccion,String calcularCantidad,
			String estadoIndicativoConsumoGenerado)
	{
		HashMap parametros = new HashMap();
		int numRegistros = 0;
		String codigosArticulosInsertados = "";
		
		parametros.put(this.indicesMedAdminHojaAnes[1],numeroSolicitud);
		parametros.put(this.indicesMedAdminHojaAnes[5],seccion);
		parametros.put(this.indicesDetMedAdminHojaAnes[6],estadoIndicativoConsumoGenerado );
		parametros.put("calcularCantidad",calcularCantidad);
		
		parametros = this.getHojaAnestesiaDao().consultarAdminisHojaAnest(con, parametros);		
		
		numRegistros = Integer.parseInt(parametros.get("numRegistros").toString());
		
		for(int i = 0 ; i<numRegistros; i++)
		{
			//Captura la informacion de los codigos de articulso almacenados
			codigosArticulosInsertados += parametros.get(this.indicesMedAdminHojaAnes[2]+i).toString()+",";
			
			//carga la informacion de la fecha para nuevas dosis
			parametros.put(this.indicesDetMedAdminHojaAnes[3]+i,UtilidadFecha.getFechaActual());
			
			//carga la informacion de la hora para nuevas dosis
			parametros.put(this.indicesDetMedAdminHojaAnes[4]+i,UtilidadFecha.getHoraActual());
			
			//carga la informacion de Graficar para nuevas dosis 
			parametros.put(this.indicesDetMedAdminHojaAnes[5]+i,ConstantesBD.acronimoNo);
			
			//carga la informacion de Suministrar para nuevas dosis
			parametros.put(this.indicesMedAdminHojaAnes[8]+i,ConstantesBD.acronimoNo);
			
			//carga la informacion de Genero Consumo para nuevas dosis
			parametros.put(this.indicesDetMedAdminHojaAnes[6]+i,ConstantesBD.acronimoNo);
			
			//carga la informacion de otro Medicamento para nuevas dosis
			parametros.put(this.indicesDetMedAdminHojaAnes[7]+i,ConstantesBD.acronimoNo);			
		}
		
		//Numero de Solicitud 
		parametros.put(HojaAnestesia.indicesMedAdminHojaAnes[1],numeroSolicitud);
		//Codigos de los articulos insertados para la busqueda de articulos
		parametros.put("codigosArticulosInsertados",codigosArticulosInsertados);
		
		return parametros;
	}
	
	
	//***********************************************************************************************************
	
	/**
	 * Insertar detalle de los medicamentos administrados
	 * @param Connection con
	 * @param String codigoAdmiHojaAnes
	 * @param String dosis
	 * @param String fecha
	 * @param String hora
	 * @param Strinh graficar (S/N)
	 * @param String generoConsumo (S/N)
	 * @param String loginUsuario 
	 * */
	public boolean insertarDetaAdminisHojaAnest(Connection con,
			String codigoAdmiHojaAnes,
			String dosis,
			String sellocalidad,
			String fecha,
			String hora,
			String graficar,
			String generoConsumo,
			String loginUsuario
			)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesDetMedAdminHojaAnes[1],codigoAdmiHojaAnes);
		parametros.put(this.indicesDetMedAdminHojaAnes[2],dosis);
		parametros.put(this.indicesDetMedAdminHojaAnes[3],fecha);
		parametros.put(this.indicesDetMedAdminHojaAnes[4],hora);
		parametros.put(this.indicesDetMedAdminHojaAnes[5],graficar);		
		parametros.put(this.indicesDetMedAdminHojaAnes[6],generoConsumo);
		parametros.put(this.indicesDetMedAdminHojaAnes[8],sellocalidad);		
		
		parametros.put("usuarioModifica",loginUsuario);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));		
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		
		
		return this.getHojaAnestesiaDao().insertarDetaAdminisHojaAnest(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Consultar el detalle de Administraciones Hoja Anestesia 
	 * @param Connection con
	 * @param String codigo Administracion Hoja Anestesia (Requerido)	
	 * */
	public HashMap consultarDetAdminisHojaAnest(Connection con,String codigoAdmHojaAnes)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesDetMedAdminHojaAnes[1],codigoAdmHojaAnes);
		return this.getHojaAnestesiaDao().consultarDetAdminisHojaAnest(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Consulta DETALLADA del detalle de Administraciones Hoja Anestesia 
	 * @param Connection con
	 * @param String codigo Administracion Hoja Anestesia (Requerido)
	 * @param String codigo Detalle Administracion Hoja Anestesia (Opcional)
	 * @param String dosis (Opcional)
	 * @param String sellocalidad (Opcional)
	 * @param String fecha (Opcional)
	 * @param String hora (Opcional)
	 * @param String graficar (Opcional)
	 * */
	public HashMap consultarDetAdminisHojaAnest(Connection con,
			String codigoAdmHojaAnes,
			String codigoDetalle,
			String dosis,
			String sellocalidad,
			String fecha,
			String hora,
			String graficar		
			)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesDetMedAdminHojaAnes[0],codigoDetalle);
		parametros.put(this.indicesDetMedAdminHojaAnes[1],codigoAdmHojaAnes);
		parametros.put(this.indicesDetMedAdminHojaAnes[2],dosis);
		parametros.put(this.indicesDetMedAdminHojaAnes[3],fecha);
		parametros.put(this.indicesDetMedAdminHojaAnes[4],hora);
		parametros.put(this.indicesDetMedAdminHojaAnes[5],graficar);
		parametros.put(this.indicesDetMedAdminHojaAnes[8],sellocalidad);
		
		return this.getHojaAnestesiaDao().consultarDetAdminisHojaAnest(con, parametros);
	}	
	
	
	//***********************************************************************************************************
	
	/**
	 * Actualizar detalle de los medicamentos administrados
	 * @param Connection con
	 * @param String codigoDetalle
 	 * @param String dosis
	 * @param String fecha
	 * @param String hora
	 * @param String graficar
	 * @param String LoginUsuario
	 * */
	public boolean actualizarDetaAdminisHojaAnest(Connection con,
			String codigoDetalle,
			String dosis,
			String sellocalidad,
			String fecha,
			String hora,
			String graficar,
			String loginUsuario)
	{
		HashMap parametros  = new HashMap();
		
		parametros.put(this.indicesDetMedAdminHojaAnes[0],codigoDetalle);
		parametros.put(this.indicesDetMedAdminHojaAnes[2],dosis);
		parametros.put(this.indicesDetMedAdminHojaAnes[3],fecha);
		parametros.put(this.indicesDetMedAdminHojaAnes[4],hora);
		parametros.put(this.indicesDetMedAdminHojaAnes[5],graficar);
		parametros.put(this.indicesDetMedAdminHojaAnes[8],sellocalidad);
		
		parametros.put("usuarioModifica",loginUsuario);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());		
		return this.getHojaAnestesiaDao().actualizarDetaAdminisHojaAnest(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Actualizar detalle de los medicamentos administrados
	 * @param Connection con
	 * @param HashMap datos
	 * @param String loginUsuario
	 * */
	public boolean actualizarDetaAdminisHojaAnest(
			Connection con,
			HashMap datos,
			String loginUsuario)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		String tmp = "";
		
		for(int i = 0 ; i < numRegistros ; i++)
		{
			if(this.validacionesFueModificadoDetalleAdmHojaAnes(con,datos,i))
			{				
				tmp = "";
				if(datos.containsKey(this.indicesDetMedAdminHojaAnes[8]+i))
					tmp = datos.get(this.indicesDetMedAdminHojaAnes[8]+i).toString();
					
				if(!this.actualizarDetaAdminisHojaAnest(con,
						datos.get(this.indicesDetMedAdminHojaAnes[0]+i).toString(), 
						datos.get(this.indicesDetMedAdminHojaAnes[2]+i).toString(), 
						tmp,
						UtilidadFecha.conversionFormatoFechaABD(datos.get(this.indicesDetMedAdminHojaAnes[3]+i).toString()), 
						datos.get(this.indicesDetMedAdminHojaAnes[4]+i).toString(), 
						datos.get(this.indicesDetMedAdminHojaAnes[5]+i).toString(),
						loginUsuario))
				{
					this.errores.add("descripcion",new ActionMessage("errors.problemasBd"));
					return false;
				}					
			}
		}			
		
		return true;		
	}
		
	//***********************************************************************************************************
		
	/**
	 * Consultar los Articulos de la Hoja de Anestesia
	 * @param Connection con
	 * @param int institucion (requerido)
	 * @param int centroCosto
	 * @param boolean activo
	 * @param int seccion
	 * @param String articulo
	 * @param String descripcionArticulo
	 * */
	public ArrayList consultarArticulosHojaAnestesia(Connection con,
			int institucion, 
			int centrosCosto,
			boolean activo,
			int seccion,
			String articulo,
			String descripcionArticulo,
			String codigosInsertados)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion",institucion);
		parametros.put("centroCosto",centrosCosto);
		parametros.put("activo",activo);
		parametros.put("seccion",seccion);
		parametros.put("articulo",articulo);
		parametros.put("descripcionArticulo",descripcionArticulo);
		parametros.put("codigoInsertados",codigosInsertados);
		
		return this.getHojaAnestesiaDao().consultarArticulosHojaAnestesia(con, parametros);
	}
	
	//	-----------------------------------------------------------------------------------------------
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/************************************************************************************************
	 * METODOS SECCION INFUSIONES********************************************************************
	 * *********************************************************************************************/
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//-----------------------------------------------------------------------------------------------
	
	/**
	 * Consultar Infusiones Hoja Anestesia
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public HashMap consultarInfusionesHojaAnestesia(Connection con,String numeroSolicitud)
	{
		HashMap parametros = new HashMap();
		String codigos = "", hayOtrasInfusiones = ConstantesBD.acronimoNo;
		parametros.put(this.indicesInfusionesHojaAnes[1],numeroSolicitud);
		parametros = this.getHojaAnestesiaDao().consultarInfusionesHojaAnestesia(con, parametros);
		
		for(int i = 0; i < Integer.parseInt(parametros.get("numRegistros").toString()); i++)
		{
			//Suspendido = No y OtraInfusion = No
			if(parametros.get(this.indicesInfusionesHojaAnes[7]+i).toString().equals(ConstantesBD.acronimoNo) && 
					parametros.get(this.indicesInfusionesHojaAnes[11]+i).toString().equals(ConstantesBD.acronimoNo) && 
						!parametros.get(this.indicesInfusionesHojaAnes[12]+i).toString().equals(""))
				codigos +=parametros.get(this.indicesInfusionesHojaAnes[12]+i)+",";
			
			//verifica si existe Infusiones ingresadas por el usuario desde la funcionalidad
			if(parametros.get(this.indicesInfusionesHojaAnes[11]+i).toString().equals(ConstantesBD.acronimoSi))
				hayOtrasInfusiones = ConstantesBD.acronimoSi;
			
			//Fecha
			parametros.put(HojaAnestesia.indicesAdmInfusionesHojaAnes[2]+i,UtilidadFecha.getFechaActual());
			//Hora
			parametros.put(HojaAnestesia.indicesAdmInfusionesHojaAnes[3]+i,UtilidadFecha.getHoraActual());			
		}
		
		parametros.put("codigosMezclaInsertados",codigos);
		parametros.put(this.indicesInfusionesHojaAnes[10],hayOtrasInfusiones);		
		
		return parametros;
	}
	
	//***********************************************************************************************************	
	
	/**
	 * Insertar Infusiones Hoja de Anestesia
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String codigoMezcla
	 * @param String otraInfusion
	 * @param String graficar
	 * @param String suspendido
	 * */
	public int insertarInfusionesHojaAnes(Connection con,
			String numeroSolicitud,
			String codigoMezclaCC,
			String otraInfusion,
			String graficar,
			String suspendido,
			String usuarioLogin, 
			String esOtraInfusion)
	{
		HashMap parametros = new HashMap();
		int consecutivo = ConstantesBD.codigoNuncaValido;
		
		parametros.put(this.indicesInfusionesHojaAnes[1],numeroSolicitud);
		
		if(esOtraInfusion.equals(ConstantesBD.acronimoSi))
			parametros.put(this.indicesInfusionesHojaAnes[12],"");
		else
			parametros.put(this.indicesInfusionesHojaAnes[12],codigoMezclaCC);
		
		if(esOtraInfusion.equals(ConstantesBD.acronimoSi))		
			parametros.put(this.indicesInfusionesHojaAnes[4],otraInfusion);
		else
			parametros.put(this.indicesInfusionesHojaAnes[4],"");
		
		parametros.put(this.indicesInfusionesHojaAnes[6],graficar);
		parametros.put(this.indicesInfusionesHojaAnes[7],suspendido);
		
		parametros.put("usuarioModifica",usuarioLogin);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		
		consecutivo = this.getHojaAnestesiaDao().insertarInfusionesHojaAnes(con, parametros);
		
		if(consecutivo == ConstantesBD.codigoNuncaValido)		
			this.errores.add("descripcion",new ActionMessage("errors.notEspecific","El consecutivo de Infusion es Incorrecto"));		
		
		return consecutivo;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Actualiza la información de infusiones de la Hoja de Anestesia
	 * @param Connection con
	 * @param String codigo
	 * @param String graficar
	 * @param String suspender
	 * @param String usuarioLogin
	 * */
	public boolean actualizarInfusionesHojaAnes(Connection con, 
			String codigo,
			String graficar,
			String suspender,
			String usuarioLogin)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesInfusionesHojaAnes[0],codigo);
		parametros.put(this.indicesInfusionesHojaAnes[6],graficar);
		parametros.put(this.indicesInfusionesHojaAnes[7],suspender);
		
		parametros.put("usuarioModifica",usuarioLogin);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());		
		
		return this.getHojaAnestesiaDao().actualizarInfusionesHojaAnes(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Actualiza la información de infusiones de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap datos
	 * @param String usuarioLogin
	 * */
	public boolean actualizarInfusionesHojaAnes(Connection con,HashMap datos,String usuarioLogin)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		
		for(int i=0; i<numRegistros; i++)
		{
			if(datos.get(this.indicesInfusionesHojaAnes[5]+i).toString().equals(ConstantesBD.acronimoSi))
			{
				if(!this.actualizarInfusionesHojaAnes(con,
						datos.get(this.indicesInfusionesHojaAnes[0]+i).toString(),
						datos.get(this.indicesInfusionesHojaAnes[6]+i).toString(),
						datos.get(this.indicesInfusionesHojaAnes[7]+i).toString(),
						usuarioLogin))
					return false;
			}
		}
		
		return true;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Consulta la informacion de las administraciones de las infusiones de la hoja de anestesia
	 * @param Connection con
	 * @param String codigoInfusiones (requerido)
	 * @param String codigoAdmInfusiones (opcional)
	 * @param String fecha (opcional)
	 * @param String hora (opcional)
	 * */
	public HashMap consultarAdmInfusionesHojaAnes(Connection con,
			String codigoInfusiones,
			String codigoAdmInfusiones,
			String fecha, 
			String hora)
	{
		HashMap parametros  = new HashMap();
		parametros.put(this.indicesAdmInfusionesHojaAnes[0],codigoAdmInfusiones);
		parametros.put(this.indicesAdmInfusionesHojaAnes[1],codigoInfusiones);
		parametros.put(this.indicesAdmInfusionesHojaAnes[2],fecha);
		parametros.put(this.indicesAdmInfusionesHojaAnes[3],hora);
		
		return this.getHojaAnestesiaDao().consultarAdmInfusionesHojaAnes(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Actualiza la informacion de las administraciones de la hoja de anestesia
	 * @param Connection con
	 * @param codigoAdministracionInfusion
	 * @param String fecha
	 * @param String hora
	 * */
	public boolean actualizarAdmInfusionesHojaAnes(Connection con,
			String codigoAdmInfusion,
			String fecha,
			String hora,
			String loginUsuario)
	{
		HashMap parametros = new HashMap();
		
		parametros.put(this.indicesAdmInfusionesHojaAnes[0],codigoAdmInfusion);
		parametros.put(this.indicesAdmInfusionesHojaAnes[2],UtilidadFecha.conversionFormatoFechaABD(fecha));
		parametros.put(this.indicesAdmInfusionesHojaAnes[3],hora);
		
		parametros.put("usuarioModifica",loginUsuario);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		
		return this.getHojaAnestesiaDao().actualizarAdmInfusionesHojaAnes(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Actualiza la informacion de las administraciones de la hoja de anestesia
	 * @param Connection con
	 * @param HashMap datos
	 * @param String loginUsuario
	 * */
	public boolean actualizarAdmInfusionesHojaAnes(Connection con, HashMap datos,String loginUsuario)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		
		for(int i = 0; i<numRegistros; i++)
		{			
			if(!datos.get(this.indicesAdmInfusionesHojaAnes[2]+i).toString().equals(datos.get(this.indicesAdmInfusionesHojaAnes[5]+i).toString()) || 
					!datos.get(this.indicesAdmInfusionesHojaAnes[3]+i).toString().equals(datos.get(this.indicesAdmInfusionesHojaAnes[6]+i).toString()))
			{		
				if(!this.actualizarAdmInfusionesHojaAnes(con,
						datos.get(this.indicesAdmInfusionesHojaAnes[0]+i).toString(),
						datos.get(this.indicesAdmInfusionesHojaAnes[2]+i).toString(),
						datos.get(this.indicesAdmInfusionesHojaAnes[3]+i).toString(),
						loginUsuario))
					return false;
			}			
			
			//Actualiza el detalle de las dosis de los articulos si existe
			if(datos.containsKey("dosis_"+i+"_numRegistros") && 
					!datos.get("dosis_"+i+"_numRegistros").toString().equals("0"))
			{			
				if(!this.actualizarDetInfusionesHojaAnes(con,"dosis_"+i+"_",datos,loginUsuario))
					return false;
			}							
		}
		
		return true;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Insertar Admisiones Infusiones Hoja de Anestesia
	 * @param Connection con
	 * @param int codigo Infusiones Hoja Anestesia
	 * @param String fecha
	 * @param String hora
	 * @param String usuarioLogin
	 * */
	public int insertarAdmInfusionesHojaAnes(
			Connection con,
			int codInfoHojaAnes,
			String fecha,
			String hora,
			String usuarioLogin)
	{
		HashMap parametros = new HashMap();
		int consecutivo = ConstantesBD.codigoNuncaValido;
		
		parametros.put(this.indicesAdmInfusionesHojaAnes[1],codInfoHojaAnes);
		parametros.put(this.indicesAdmInfusionesHojaAnes[2],UtilidadFecha.conversionFormatoFechaABD(fecha));
		parametros.put(this.indicesAdmInfusionesHojaAnes[3],hora);
		
		parametros.put("usuarioModifica",usuarioLogin);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		
		consecutivo = this.getHojaAnestesiaDao().insertarAdmInfusionesHojaAnes(con, parametros);
		
		if(consecutivo == ConstantesBD.codigoNuncaValido)
			this.errores.add("descripcion",new ActionMessage("errors.notEspecific","El consecutivo de Administración de Infusion es Incorrecto"));
		
		return consecutivo;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Consultar Infusiones Agrupadas por mezcla y articulo 
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public HashMap consultarInfusionesAgrupadas(Connection con,String numeroSolicitud)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesInfusionesHojaAnes[1],numeroSolicitud);
		
		return this.getHojaAnestesiaDao().consultarInfusionesAgrupadas(con, parametros);
	}
	//***********************************************************************************************************
	/**
	 * Consulta la informacion de los detalle de  administraciones de las infusiones de la hoja de anestesia
	 * @param Connection con
	 * @param String codigoAdmInfusion (requerida si el codigo de la infusion es vacia)
	 * @param String codigoInfusion (opcional), se activa cuando no se envia el codigo de la adminitracion. se toma la ultima administracion
	 * @param String codigoDetInfusion (opcional)
	 * @param String codigoArticulo (opcional)
	 * @param String dosis (opcional)
	 * @param boolean esHistorial  
	 * */
	public HashMap consultarDetInfusionesHojaAnes(Connection con,
			String codigoAdmInfusion,
			String codigoInfusion,
			String codigoDetInfusion,
			String codigoArticulo,
			String dosis,
			boolean esHistorial)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesDetAdmInfusionesHojaAnes[1],codigoAdmInfusion);
		parametros.put(this.indicesAdmInfusionesHojaAnes[1],codigoInfusion);
		parametros.put(this.indicesDetAdmInfusionesHojaAnes[0],codigoDetInfusion);
		parametros.put(this.indicesDetAdmInfusionesHojaAnes[2],codigoArticulo);
		parametros.put(this.indicesDetAdmInfusionesHojaAnes[4],dosis);
		
		parametros = this.getHojaAnestesiaDao().consultarDetInfusionesHojaAnes(con, parametros);		
		int numRegistros = Integer.parseInt(parametros.get("numRegistros").toString());
		String codigoInsert = "";
		
		for(int i = 0 ; i<numRegistros ; i++)
		{
			codigoInsert += parametros.get(this.indicesDetAdmInfusionesHojaAnes[2]+i)+",";
			
			if(!esHistorial)
				parametros.put(this.indicesDetAdmInfusionesHojaAnes[4]+i,"");			
		}
		
		parametros.put("codigosArticulosInsertados",codigoInsert);
		
		return parametros;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Insertar Detalle de Admisiones Infusiones Hoja de Anestesia
	 * @param Connection con
	 * @param int codigo Administracion de Infusiones
	 * @param String articulo
	 * @param String dosis
	 * */
	public int insertarDetInfusionesHojaAnes(Connection con,int codigoAdmInfoHojaAnes, String articulo,String dosis,String loginUsuario)
	{
		HashMap parametros = new HashMap();
		int consecutivo = ConstantesBD.codigoNuncaValido;
		
		parametros.put(this.indicesDetAdmInfusionesHojaAnes[1],codigoAdmInfoHojaAnes);
		parametros.put(this.indicesDetAdmInfusionesHojaAnes[2],articulo);
		parametros.put(this.indicesDetAdmInfusionesHojaAnes[4],dosis);
		
		parametros.put("usuarioModifica",loginUsuario);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		
		consecutivo =  this.getHojaAnestesiaDao().insertarDetInfusionesHojaAnes(con, parametros);
		
		if(consecutivo == ConstantesBD.codigoNuncaValido)
			this.errores.add("descripcion",new ActionMessage("errors.notEspecific","El consecutivo del Detalle de Infusion es Incorrecto"));
		
		return consecutivo;	
	}
	
	//***********************************************************************************************************

	/**
	 * Insertar Detalle de Admisiones Infusiones Hoja de Anestesia
	 * @param Connection con
	 * @param int codigoAdmInfoHojaAnes
	 * @param HashMap datos
	 * @param Strin loginUsuario
	 * */
	public boolean insertarDetInfusionesHojaAnes(Connection con,int codigoAdmInfoHojaAnes,HashMap datos,String loginUsuario)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		
		for(int i = 0 ; i < numRegistros; i++)
		{
			if(insertarDetInfusionesHojaAnes(con, 
					codigoAdmInfoHojaAnes, 
					datos.get(this.indicesDetAdmInfusionesHojaAnes[2]+i).toString(), 
					datos.get(this.indicesDetAdmInfusionesHojaAnes[4]+i).toString(), 
					loginUsuario) == ConstantesBD.codigoNuncaValido)
			{
				return false;
			}
		}		
		
		return true;
	}
	
	
	//***********************************************************************************************************
	
	/**
	 * Actualiza la informacion del Detalle de la administracion de infusiones de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * @param loginUsuario
	 * */
	public boolean actualizarDetInfusionesHojaAnes(Connection con,String preFijo,HashMap datos,String loginUsuario)
	{
		int numRegistros = Integer.parseInt(datos.get(preFijo+"numRegistros").toString());
		
		for(int i = 0 ; i<numRegistros; i++)
		{			
			if(!datos.get(preFijo+this.indicesDetAdmInfusionesHojaAnes[4]+i).toString().equals(datos.get(preFijo+this.indicesDetAdmInfusionesHojaAnes[7]+i).toString()))
			{
				
				if(!this.actualizarDetInfusionesHojaAnes(con,
						datos.get(preFijo+this.indicesDetAdmInfusionesHojaAnes[0]+i).toString(),
						datos.get(preFijo+this.indicesDetAdmInfusionesHojaAnes[4]+i).toString(),
						loginUsuario))					
					return false;
			}
		}	
		
		return true;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Actualiza el indicativo de genero consumo del detalle de los medicamentos administrados
	 * @param Connection con
	 * @param String generoConsumo
	 * @param String codigoArticulo
	 * @param String numeroSolicitud
	 * @param String seccion
	 * @param String loginUsuario
	 * */
	public boolean actualizarGenConsumoDetaAdminis(Connection con,
			String generoConsumo,
			String codigoArticulo,
			String numeroSolicitud,
			int codigoDetMateQx,
			String seccion,
			String loginUsuario)
	{
		HashMap parametros = new HashMap();
		
		parametros.put(this.indicesDetMedAdminHojaAnes[6],generoConsumo);
		parametros.put(this.indicesMedAdminHojaAnes[2],codigoArticulo);
		parametros.put(this.indicesMedAdminHojaAnes[1],numeroSolicitud);
		parametros.put(this.indicesMedAdminHojaAnes[5],seccion);			
		parametros.put(this.indicesMedAdminHojaAnes[13],codigoDetMateQx);
		
		parametros.put("usuarioModifica",loginUsuario);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());		
		
		return this.getHojaAnestesiaDao().actualizarGenConsumoDetaAdminis(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Actualiza la informacion del Detalle de la administracion de infusiones de la Hoja de Anestesia
	 * @param Connection con
	 * @param String codigoDetInfu
	 * @param String dosis
	 * @param loginUsuario
	 * */
	public boolean actualizarDetInfusionesHojaAnes(Connection con,String codigoDetInfu,String dosis,String loginUsuario)
	{
		HashMap parametros = new HashMap();
		
		parametros.put(this.indicesDetAdmInfusionesHojaAnes[0],codigoDetInfu);
		parametros.put(this.indicesDetAdmInfusionesHojaAnes[4],dosis);	
		
		parametros.put("usuarioModifica",loginUsuario);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());		
		
		return this.getHojaAnestesiaDao().actualizarDetInfusionesHojaAnes(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Consultar las Mezclas parametrizadas en el sistema
	 * @param Connection con
	 * @param String institucion
	 * @param String codigoInsertados
 	 * @param String consecutivoMezcla
	 * @param String nombreMezcla
	 * @param String centroCosto
	 * */
	public ArrayList consultarMezclas(
			Connection con,
			String institucion,
			String codigoInsertados, 
			String consecutivoMezcla,
			String nombreMezcla,
			int centroCentro)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion",institucion);
		parametros.put("codigoInsertados",codigoInsertados);
		parametros.put("consecutivo",consecutivoMezcla);
		parametros.put("centroCosto",centroCentro);
		parametros.put("nombre",nombreMezcla);
		
		return this.getHojaAnestesiaDao().consultarMezclas(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Consultar la informacion de los articulos asociados a una mezcla
	 * @param Connection con
	 * @param String codigoMezclaCC
	 * */
	public HashMap consultarArticulosMezcla(Connection con,String codigoMezclaCC)
	{
		HashMap parametros = new HashMap();
		parametros.put("mezclaCC",codigoMezclaCC);
				
		parametros = this.getHojaAnestesiaDao().consultarArticulosMezcla(con,parametros);		
		int numRegistros = Integer.parseInt(parametros.get("numRegistros").toString());
		String codigoInsert = "";
		
		for(int i = 0 ; i<numRegistros ; i++)
			codigoInsert += parametros.get(this.indicesDetAdmInfusionesHojaAnes[2]+i)+",";						
		
		parametros.put("codigosArticulosInsertados",codigoInsert);
		
		return parametros;		
	}
	
	//***********************************************************************************************************
	/**
	 * Consulta el historial de las Infusiones
	 * @param Connection 
	 * @para String codigoInfusion
	 * */
	public HashMap consultarHistorialInfusiones(Connection con,String codigoInfusion)
	{
		HashMap parametros = new HashMap();
		HashMap detalle = new HashMap();
		int numRegistros = 0;
		int numDetalle = 0;
		
		parametros = this.consultarAdmInfusionesHojaAnes(con,codigoInfusion,"","","");		
		numRegistros = Integer.parseInt(parametros.get("numRegistros").toString());
				
		for(int i=0; i<numRegistros; i++)		
		{
			detalle = this.consultarDetInfusionesHojaAnes(con,
					parametros.get(this.indicesAdmInfusionesHojaAnes[0]+i).toString(),
					"",
					"",
					"",
					"",
					true);

			numDetalle = Integer.parseInt(detalle.get("numRegistros").toString());
			parametros.put("dosis_"+i+"_numRegistros",numDetalle);
			
			for(int j=0; j<numDetalle; j++)
			{
				parametros.put("dosis_"+i+"_"+this.indicesDetAdmInfusionesHojaAnes[0]+j,detalle.get(this.indicesDetAdmInfusionesHojaAnes[0]+j));				
				parametros.put("dosis_"+i+"_"+this.indicesDetAdmInfusionesHojaAnes[1]+j,detalle.get(this.indicesDetAdmInfusionesHojaAnes[1]+j));
				parametros.put("dosis_"+i+"_"+this.indicesDetAdmInfusionesHojaAnes[2]+j,detalle.get(this.indicesDetAdmInfusionesHojaAnes[2]+j));
				parametros.put("dosis_"+i+"_"+this.indicesDetAdmInfusionesHojaAnes[3]+j,detalle.get(this.indicesDetAdmInfusionesHojaAnes[3]+j));				
				parametros.put("dosis_"+i+"_"+this.indicesDetAdmInfusionesHojaAnes[4]+j,detalle.get(this.indicesDetAdmInfusionesHojaAnes[4]+j));
				parametros.put("dosis_"+i+"_"+this.indicesDetAdmInfusionesHojaAnes[7]+j,detalle.get(this.indicesDetAdmInfusionesHojaAnes[7]+j));
				parametros.put("dosis_"+i+"_"+this.indicesDetAdmInfusionesHojaAnes[5]+j,detalle.get(this.indicesDetAdmInfusionesHojaAnes[5]+j));
				parametros.put("dosis_"+i+"_"+this.indicesDetAdmInfusionesHojaAnes[6]+j,detalle.get(this.indicesDetAdmInfusionesHojaAnes[6]+j));
			}			
		}
		
		return parametros;		
	}	
	
	//***********************************************************************************************************
	
	/**
	 * Actualiza el indicador de genero consumo para los articulos de un mezcla
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String generoConsumo
	 * @param String codigoArticulo
	 * @param String codigoMezlca
	 * @param String otraInfusion
	 * */
	public boolean actualizarGenConsuArtiMezcla(Connection con,
			String numeroSolicitud,
			String generoConsumo,
			String codigoArticulo,
			String codigoMezclaCC,
			int codigoDetMateQx,
			String otraInfusion,
			String loginUsuario)
	{
		HashMap parametros = new HashMap();
		parametros.put(indicesDetAdmInfusionesHojaAnes[8],generoConsumo);
		parametros.put(indicesDetAdmInfusionesHojaAnes[2],codigoArticulo);
		parametros.put(indicesInfusionesHojaAnes[1],numeroSolicitud);		
		parametros.put(indicesInfusionesHojaAnes[12],codigoMezclaCC);
		parametros.put(indicesInfusionesHojaAnes[4],otraInfusion);		
		parametros.put(indicesDetAdmInfusionesHojaAnes[10],codigoDetMateQx);
		
		parametros.put("usuarioModifica",loginUsuario);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		
		return this.getHojaAnestesiaDao().actualizarGenConsuArtiMezcla(con, parametros);
	}
	//***********************************************************************************************************
	
	//-----------------------------------------------------------------------------------------------
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/************************************************************************************************
	 * METODOS SECCION BALANCES LIQUIDOS*************************************************************
	 * *********************************************************************************************/
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//-----------------------------------------------------------------------------------------------
	
	/**
	 * Insertar Hoja Anestesia Balance Liquidos
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String codigoLoquidoInst
	 * @param String cantidad
	 * @param String loginUsuario 
	 * */
	public boolean insertarBalancesLiquidosHojaAnes(Connection con,
			String numeroSolicitud,
			String codigoLiquidoInst,
			String cantidad,
			String loginUsuario)
	{
		HashMap parametros = new HashMap();
		parametros.put(HojaAnestesia.indicesBalancesLiquidosHojaAnes[0], numeroSolicitud);
		parametros.put(HojaAnestesia.indicesBalancesLiquidosHojaAnes[1], codigoLiquidoInst);
		parametros.put(HojaAnestesia.indicesBalancesLiquidosHojaAnes[2], cantidad);	
		
		parametros.put("usuarioModifica",loginUsuario);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		
		return this.getHojaAnestesiaDao().insertarBalancesLiquidosHojaAnes(con, parametros); 
	}
	
	//***********************************************************************************************************
	
	/**
	 * Insertar Hoja Anestesia Balance Liquidos
	 * @param Connection con
	 * @param HashMap datos
	 * @param String loginUsuario 
	 * */
	public boolean insertarBalancesLiquidosHojaAnes(Connection con, HashMap datos,String loginUsuario)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());		
		
		for(int i = 0 ; i < numRegistros; i++)
		{
			if(datos.get(this.indicesBalancesLiquidosHojaAnes[4]+i).toString().equals(ConstantesBD.acronimoNo))
			{
				if(!this.insertarBalancesLiquidosHojaAnes(con,
						datos.get(this.indicesBalancesLiquidosHojaAnes[0]+i).toString(),
						datos.get(this.indicesBalancesLiquidosHojaAnes[1]+i).toString(),
						datos.get(this.indicesBalancesLiquidosHojaAnes[2]+i).toString(),						
						loginUsuario))
					return false;
			}
		}		
		
		return true;
	}
			
	//***********************************************************************************************************
	
	/**
	 * Consulta la información de la Hoja de Anestesia Balance Liquidos, si el parametros esDevolverMapa es true
	 * retorna un HashMap con los datos del Balance de Liquidos si es FALSE retorna el valor total de Balance de 
	 * liquidos
	 * @param Connection con
	 * @param String numeroSolicitud 
	 * */
	public Object consultarBalanceLiquidos(Connection con,String numeroSolicitud,boolean esDevolverMapa)
	{
		HashMap parametros = new HashMap();
		HashMap hemo = new HashMap();
		HashMap liquidos = new HashMap();		
		this.utilitarioMap = new HashMap();
		int i = 0;		
		int total = 0 ;
		
		//Consulta la información de otros liquidos
		parametros.put(this.indicesBalancesLiquidosHojaAnes[0],numeroSolicitud);		
		parametros = this.getHojaAnestesiaDao().consultarBalanceLiquidos(con, parametros);		
		parametros.put("codigosInsertados",this.getCodigosInsertados(parametros,this.indicesBalancesLiquidosHojaAnes[1]));
		
		//Consulta la información de los liquidos ingresados en la sección de Hemoderivados
		hemo = this.consultarAdminisHojaAnest(con, numeroSolicitud,ConstantesSeccionesParametrizables.seccionHemoderivados+"",ConstantesBD.acronimoSi,"");

		//Consulta la información de los liquidos ingresados en la sección de liquidos
		liquidos = this.consultarAdminisHojaAnest(con, numeroSolicitud,ConstantesSeccionesParametrizables.seccionLiquidos+"",ConstantesBD.acronimoSi,"");			
		
		
		//NOTA: SOLO SE LLENARON EN EL MAPA UTILITARIO LOS DATOS NECESARIOS PARA LA FUNCIóN DE BALANCE 
		//SI SE REQUIERE OTROS POR FAVOR INGRESARLOS

				
		//Se reccore el mapa de Liquidos
		int numRegistros = Integer.parseInt(liquidos.get("numRegistros").toString());
	
		for(i=0; i<numRegistros; i++)
		{			
			//cantidad
			this.utilitarioMap.put(this.indicesMedAdminHojaAnes[4]+i,liquidos.get(this.indicesMedAdminHojaAnes[4]+i).toString());			
			//seccion
			this.utilitarioMap.put(this.indicesMedAdminHojaAnes[5]+i,liquidos.get(this.indicesMedAdminHojaAnes[5]+i).toString());
			//descripción articulo
			this.utilitarioMap.put(this.indicesMedAdminHojaAnes[6]+i,liquidos.get(this.indicesMedAdminHojaAnes[6]+i).toString());
			//Tipo Liquido
			this.utilitarioMap.put(this.indicesMedAdminHojaAnes[9]+i,liquidos.get(this.indicesMedAdminHojaAnes[9]+i).toString());
			//descripcion tipo liquido
			this.utilitarioMap.put(this.indicesMedAdminHojaAnes[10]+i,liquidos.get(this.indicesMedAdminHojaAnes[10]+i).toString());
			
			//Captura el valor del total
			if(!this.utilitarioMap.get(this.indicesMedAdminHojaAnes[4]+i).toString().equals(""))
				total += Integer.parseInt(this.utilitarioMap.get(this.indicesMedAdminHojaAnes[4]+i).toString());
		}
		
		
		//Se reccore el mapa de Hemoderivados
		numRegistros = Integer.parseInt(hemo.get("numRegistros").toString());
		for(int j=0; j<numRegistros; j++)
		{				
			//cantidad
			this.utilitarioMap.put(this.indicesMedAdminHojaAnes[4]+i,hemo.get(this.indicesMedAdminHojaAnes[4]+j).toString());			
			//seccion
			this.utilitarioMap.put(this.indicesMedAdminHojaAnes[5]+i,hemo.get(this.indicesMedAdminHojaAnes[5]+j).toString());
			//descripción articulo
			this.utilitarioMap.put(this.indicesMedAdminHojaAnes[6]+i,hemo.get(this.indicesMedAdminHojaAnes[6]+j).toString());
			
			//Captura el valor del total
			if(!this.utilitarioMap.get(this.indicesMedAdminHojaAnes[4]+i).toString().equals(""))
				total += Integer.parseInt(this.utilitarioMap.get(this.indicesMedAdminHojaAnes[4]+i).toString());
			
			i++;
		}		
				
		//Tamaóo del mapa
		this.utilitarioMap.put("numRegistros",i);
		this.utilitarioMap.put("totalSecciones",total);
		
		total -= getTotalCampo(parametros,this.indicesBalancesLiquidosHojaAnes[2]);
		parametros.put("valorTotal",total);
		
		if(esDevolverMapa)
			return parametros;
		else
			return total;
		
	}
	
	//***********************************************************************************************************
	
	/**
	 * Actualiza la información de los balances de liquidos
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String codigoLiquido
	 * @param String cantidad
	 * @param String loginUsuario
	 * */
	public boolean actualizarBalancesLiquidos(
			Connection con,
			String numeroSolicitud,
			String codigoLiquido,
			String cantidad,
			String loginUsuario)
	{
		HashMap parametros = new HashMap();
				
		parametros.put(this.indicesBalancesLiquidosHojaAnes[0],numeroSolicitud);
		parametros.put(this.indicesBalancesLiquidosHojaAnes[1],codigoLiquido);
		parametros.put(this.indicesBalancesLiquidosHojaAnes[2],cantidad);
		
		parametros.put("usuarioModifica",loginUsuario);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		
		return this.getHojaAnestesiaDao().actualizarBalancesLiquidos(con, parametros);
		
	}

	//***********************************************************************************************************	
	
	/**
	 * Actualiza la información de los balances de liquidos
	 * @param Connection con
	 * @param HashMap datos
	 * @param String loginUsuario
	 * */
	public boolean actualizarBalancesLiquidos(Connection con, HashMap datos,String loginUsuario)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		
		for(int i = 0; i<numRegistros; i++)
		{
			if(datos.get(this.indicesBalancesLiquidosHojaAnes[4]+i).toString().equals(ConstantesBD.acronimoSi))
			{
				if(!this.actualizarBalancesLiquidos(con, 
						datos.get(this.indicesBalancesLiquidosHojaAnes[0]+i).toString(),
						datos.get(this.indicesBalancesLiquidosHojaAnes[1]+i).toString(),
						datos.get(this.indicesBalancesLiquidosHojaAnes[2]+i).toString(),						
						loginUsuario))
					return false;
			}
		}
		
		return true;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Consultar otros Liquidos Balance Liquidos Hoja de Anestesia
	 * @param Connection con
	 * @param String institucion
	 * @param String centroCosto
	 * @param String descripcionLiquido
	 * */
	public ArrayList consultarOtrosLiquidosBalanceLiq(Connection con,String institucion,String centroCosto,String codigosInsertados,String descripcionLiquido)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion",institucion);
		parametros.put("centroCosto",centroCosto);
		parametros.put("codigosInsertados",codigosInsertados);
		parametros.put("descripcionLiquido",descripcionLiquido);		
		
		return this.getHojaAnestesiaDao().consultarOtrosLiquidosBalanceLiq(con, parametros);
	}
	
	//***********************************************************************************************************
	
	//-----------------------------------------------------------------------------------------------
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/************************************************************************************************
	 * METODOS SECCION SALIDA PACIENTE***************************************************************
	 * *********************************************************************************************/
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//-----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo centralizado para las validaciones de las seccion requeridas para la salida del paciente
	 * @param Connection con 
	 * @param String numeroSolicitud
	 * @param int institucion
	 * */
	public boolean centralValidacionesSeccionesHojaAnestesia(Connection con, String numeroSolicitud,int institucion)
	{
		this.errores = new ActionErrors();
		boolean respuesta = true;
		this.utilitarioList = new ArrayList();
		HashMap parametros = new HashMap();
		HashMap hojaAnestesiaMap = new HashMap();		
				
		SolicitudesCx solicitudCx = new SolicitudesCx();		
		solicitudCx.cargarEncabezadoSolicitudCx(con, numeroSolicitud);		
		hojaAnestesiaMap = consultarHojaAnestesia(con,numeroSolicitud,institucion,"");		
		
		//****************************************************************
		//Valida que la Hoja de Anestesia este creada
		if(hojaAnestesiaMap.get("numRegistros").equals("0"))
		{
			this.cargarErrorElementoAp("errors.notEspecific","No existe Hoja de Anestesia, aun no ha guardado en ninguna de las Secciones la Información Requerida. " +
					"Inicie ingresando Información en la Sección: Información General. Subsección: Fecha y Hora de Ingreso a la Sala ","");							
			respuesta = false;
			return respuesta;
		}
		
		//****************************************************************
		//Sección: Información General. Subsección: Fecha y Hora de Ingreso a la Sala 
		//Valida que la fecha no esten vacias
		if(solicitudCx.getFechaIngresoSala().equals("") || 
				solicitudCx.getHoraIngresoSala().equals(""))
		{
			this.cargarErrorElementoAp("prompt.generico","Sección: Información General. Subsección: Fecha y Hora de Ingreso a la Sala ","");								
			this.cargarErrorElementoAp("errors.required","La Fecha/Hora Ingreso Sala ","");
			respuesta = false;						
		}		
		
		//****************************************************************
		//Sección: Información General. Subsección: Especialidades que Intervienen y Cirujanos Principales
		//Consulta la información de las especialidades
		parametros = this.consultarEspecialidadesIntervienen(con,numeroSolicitud,"");
		int numRegistros = Integer.parseInt(parametros.get("numRegistros").toString());
		
		//Valida que existan especialidades
		if(numRegistros > 0)
		{
			//Valida que la especialidad posea por lo menos un cirujano
			for(int i = 0; i<numRegistros; i++)
			{
				if(this.consultarCirujanosIntervienen(
						con,
						numeroSolicitud,parametros.get(this.indicesEspecialidadesInter[1]+i).toString()).get("numRegistros").toString().equals("0"))
				{
					if(respuesta)
						this.cargarErrorElementoAp("prompt.generico","Sección: Información General. Subsección: Especialidades que Intervienen y Cirujanos Principales","");
					
					this.cargarErrorElementoAp("errors.notEspecific","La especialidad "+parametros.get(this.indicesEspecialidadesInter[2]+i)+" requiere por lo menos un Cirujano asignado.","");
					respuesta = false;
				}
			}			
		}
		else
		{
			this.cargarErrorElementoAp("prompt.generico","Sección: Información General. Subsección: Especialidades que Intervienen y Cirujanos Principales","");								
			this.cargarErrorElementoAp("errors.notEspecific","Debe existir por lo menos una especialidad con por lo menos un cirujano asociado","");
			respuesta = false;
		}	
		
		//****************************************************************
		//Sección: Información General. Subsección: Anestesiologos		
		parametros = this.consultarAnestesiologos(con, numeroSolicitud,"",ConstantesBD.acronimoSi,null,false);
		
		//Valida que exista por lo menos un anestesiologo marcado como si para cancelar honorarios
		if(parametros.get("numRegistros").toString().equals("0"))
		{
			this.cargarErrorElementoAp("prompt.generico","Sección: Información General. Subsección: Anestesiologos ","");								
			this.cargarErrorElementoAp("errors.notEspecific","Debe seleccionar un Anestesiólogo para el Cobro de Honorarios ","");
			respuesta = false;
		}
		
		//Verifica que en la hoja de anestesia se seleccion S/N en anestesiologo cobrable
		if(hojaAnestesiaMap.get(this.indicesHojaAnestesia[1]+"0").toString().equals(""))
		{
			this.cargarErrorElementoAp("prompt.generico","Sección: Información General. Subsección: Anestesiologos","");								
			this.cargarErrorElementoAp("errors.notEspecific","Debe elegir si los Honorarios se cobran o No","");
			respuesta = false;
		}
		
		//****************************************************************
		//Sección Eventos
		//verifica que exista la información inicio/fin de la cirugia
		
		if(solicitudCx.getFechaInicialCx().equals("") || solicitudCx.getHoraInicialCx().equals("") || 
				solicitudCx.getFechaFinalCx().equals("") || solicitudCx.getHoraFinalCx().equals(""))
		{
			this.cargarErrorElementoAp("prompt.generico","Sección: Eventos ","");								
			this.cargarErrorElementoAp("errors.notEspecific","La información de Inicio y Fin de Cirugia es requerida ","");
			respuesta = false;
		}		
		
		//Verifica que exista la información inicio/fin de anestesia		
		if(hojaAnestesiaMap.get(this.indicesHojaAnestesia[2]+"0").toString().equals("") || hojaAnestesiaMap.get(this.indicesHojaAnestesia[3]+"0").toString().equals("") 
				|| hojaAnestesiaMap.get(this.indicesHojaAnestesia[4]+"0").toString().equals("") || hojaAnestesiaMap.get(this.indicesHojaAnestesia[5]+"0").toString().equals(""))
		{
			this.cargarErrorElementoAp("prompt.generico","Sección: Eventos ","");								
			this.cargarErrorElementoAp("errors.notEspecific","La información de Inicio y Fin de Anestesia es requerida ","");
			respuesta = false;
		}		
		
		//Verifica las fechas de los eventos requeridos y con el indicador de lleve fecha fin en si
		parametros = Eventos.consultarEventosHojaAnesLlevaFin(con,Integer.parseInt(numeroSolicitud),ConstantesBD.acronimoSi);		
		
		for(int i = 0 ; i< Integer.parseInt(parametros.get("numRegistros").toString()); i++)
		{	
			if(parametros.get("fechaInicial_"+i).toString().equals("") || parametros.get("horaInicial_"+i).toString().equals("") 
					|| parametros.get("fechaFinal_"+i).toString().equals("") || parametros.get("horaFinal_"+i).toString().equals(""))
			{
				this.cargarErrorElementoAp("prompt.generico","Sección: Eventos ","");								
				this.cargarErrorElementoAp("errors.notEspecific","La información de Inicio y Fin del Evento "+parametros.get("nombre_"+i).toString()+" es requerida. ","");
				respuesta = false;
			}
		}
		//****************************************************************		
		return respuesta;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Insertar Salidas de Paciente
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String codigSalidaPacienteInstCCosto
	 * @param String loginUsuario
	 * */
	public int insertarSalidaSalaPaciente(Connection con,
			String numeroSolicitud,
			String codigSalidaPacienteInstCCosto,
			String loginUsuario)
	{
		HashMap parametros = new HashMap();
		parametros.put("usuarioModifica",loginUsuario);
		parametros.put(this.indicesSalidasSalaPaciente[1],numeroSolicitud);
		parametros.put(this.indicesSalidasSalaPaciente[2],codigSalidaPacienteInstCCosto);
		
		return this.getHojaAnestesiaDao().insertarSalidaSalaPaciente(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Consultar la información de la Hoja de Anestesia
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String consecutivo (Opcional)
	 * */
	public HashMap consultarSalidasPaciente(Connection con,String numeroSolicitud,String consecutivo)
	{
		HashMap parametros = new HashMap();
		parametros.put(HojaAnestesia.indicesSalidasSalaPaciente[0],numeroSolicitud);
		parametros.put(HojaAnestesia.indicesSalidasSalaPaciente[1],consecutivo);
		
		return this.getHojaAnestesiaDao().consultarSalidasPaciente(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Consultar Salidas de paciente
	 * @param Connection con
	 * @param String institucion 
	 * @param String centro Costo
	 * @param boolean esPacienteMuerto
	 * */
	public ArrayList consultarSalidasPacienteInstCCosto(Connection con,String institucion,String centroCosto,boolean esPacienteMuerto)	
	{
		HashMap parametros = new HashMap();
		parametros.put("centroCosto",centroCosto);
		parametros.put("institucion",institucion);
		
		if(esPacienteMuerto)
			parametros.put("fallece",ConstantesBD.acronimoSi);
		else
			parametros.put("fallece",ConstantesBD.acronimoNo);
	
		return this.getHojaAnestesiaDao().consultarSalidasPacienteInstCCosto(con, parametros);
	}
	
	//************************************************************************************************************
	
	/**
	 * Consulta la información para la generación de los consumos
	 * @param Connection con
	 * @param String institucion
	 * @param String numeroSolicitud
	 * */
	public HashMap consultarGeneracionConsumos(Connection con, String institucion,String numeroSolicitud)
	{
		HashMap parametros = new HashMap();
		HashMap mapaSeccion = new HashMap();	
		this.utilitarioMap = new HashMap();
		int cont = 0;	
		

		//Consulta la información de los liquidos ingresados en la sección de liquidos
		mapaSeccion = this.consultarAdminisHojaAnest(
				con, 
				numeroSolicitud,
				ConstantesSeccionesParametrizables.seccionLiquidos+"",
				ConstantesBD.acronimoSi,
				ConstantesBD.acronimoNo);		
				
		//**********************************************
		//Se reccore el mapa de Liquidos
		int numRegistros = Integer.parseInt(mapaSeccion.get("numRegistros").toString());
	
		for(int i=0; i<numRegistros; i++)
		{
			//Si la cantidad es diferente de cero
			if(!mapaSeccion.get(this.indicesMedAdminHojaAnes[4]+i).toString().equals("0") 
				&& !mapaSeccion.get(this.indicesMedAdminHojaAnes[4]+i).toString().equals(""))
			{
				//cantidad
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[4]+cont,mapaSeccion.get(this.indicesMedAdminHojaAnes[4]+i).toString());			
				//seccion
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[5]+cont,mapaSeccion.get(this.indicesMedAdminHojaAnes[5]+i).toString());
				//codigo articulo
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[2]+cont,mapaSeccion.get(this.indicesMedAdminHojaAnes[2]+i).toString());
				//descripción articulo
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[6]+cont,mapaSeccion.get(this.indicesMedAdminHojaAnes[6]+i).toString());
				//Tipo Liquido
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[9]+cont,mapaSeccion.get(this.indicesMedAdminHojaAnes[9]+i).toString());
				//descripción tipo liquido
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[10]+cont,mapaSeccion.get(this.indicesMedAdminHojaAnes[10]+i).toString());		
				//Indicador de generación de consumo
				this.utilitarioMap.put(this.indicesDetMedAdminHojaAnes[6]+cont,ConstantesBD.acronimoSi);
				
				cont++;
			}
		}
		
		//**********************************************
		//Consulta la información de los liquidos ingresados en la sección de Anestesicos y Medicamentos administrados
		mapaSeccion = this.consultarAdminisHojaAnest(
				con, 
				numeroSolicitud,
				ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis+"",
				ConstantesBD.acronimoSi,
				ConstantesBD.acronimoNo);

				
		//Se reccore el mapa de Anestesicos y Medicamentos Administrados
		numRegistros = Integer.parseInt(mapaSeccion.get("numRegistros").toString());
		for(int j=0; j<numRegistros; j++)
		{
			if(!mapaSeccion.get(this.indicesMedAdminHojaAnes[4]+j).toString().equals("0") 
				&& !mapaSeccion.get(this.indicesMedAdminHojaAnes[4]+j).toString().equals(""))
			{
				//cantidad
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[4]+cont,mapaSeccion.get(this.indicesMedAdminHojaAnes[4]+j).toString());			
				//seccion
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[5]+cont,mapaSeccion.get(this.indicesMedAdminHojaAnes[5]+j).toString());
				//codigo articulo
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[2]+cont,mapaSeccion.get(this.indicesMedAdminHojaAnes[2]+j).toString());
				//descripción articulo
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[6]+cont,mapaSeccion.get(this.indicesMedAdminHojaAnes[6]+j).toString());
				//Indicador de generación de consumo
				this.utilitarioMap.put(this.indicesDetMedAdminHojaAnes[6]+cont,ConstantesBD.acronimoSi);
				
				cont++;
			}
		}
		
		//**********************************************
		//Consulta la información de los articulos ingresados en la Seccion. información General. Subseccion via Aerea
		mapaSeccion = this.consultarCantidadesDetalleViaAerea(con, numeroSolicitud, institucion);
		numRegistros = Integer.parseInt(mapaSeccion.get("numRegistros").toString());
		
		//Se recorre el mapa de Cantidades
		for(int i = 0 ; i<numRegistros; i++)
		{
			if(!mapaSeccion.get(this.indicesDetViaAerea[2]+i).toString().equals(""))
			{
				//Cantidad
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[4]+cont,mapaSeccion.get(this.indicesDetViaAerea[2]+i).toString());
				//Codigo articulo
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[2]+cont,mapaSeccion.get(this.indicesDetViaAerea[1]+i).toString());
				//Codigo de aerea
				this.utilitarioMap.put(this.indicesDetViaAerea[0]+cont,mapaSeccion.get(this.indicesDetViaAerea[0]+i).toString());
				//Indicador de generación de consumo
				this.utilitarioMap.put(this.indicesDetMedAdminHojaAnes[6]+cont,ConstantesBD.acronimoSi);
				//seccion
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[5]+cont,ConstantesSeccionesParametrizables.subSeccionViaArea+"");
				
				cont++;
			}
		}
		
		//**********************************************
		//Consulta la informacion de la seccion Informacion General - Subseccion Accesos Vasculares
		mapaSeccion = AccesosVascularesHA.obtenerListadoAccesosVasculares(con, Integer.parseInt(numeroSolicitud));
		numRegistros = Integer.parseInt(mapaSeccion.get("numRegistros").toString());
		
		for(int i=0; i<Utilidades.convertirAEntero(mapaSeccion.get("numRegistros")+""); i++)
		{
			boolean tieneConsumo=UtilidadTexto.getBoolean(mapaSeccion.get("generoconsumo_"+i).toString());
			
			if(!tieneConsumo)
			{
				//CodigoPk
				this.utilitarioMap.put("cod_acc_vascular_hoja_anes_"+cont,mapaSeccion.get("cod_acc_vascular_hoja_anes_"+i).toString());
				//Cantidad
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[4]+cont,mapaSeccion.get("cantidad_"+i).toString());
				//Codigo articulo
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[2]+cont,mapaSeccion.get("articulo_"+i).toString());				
				//Descripcion Articulo
				this.utilitarioMap.put("descarticulo_"+cont,mapaSeccion.get("descarticulo_"+i).toString());
				//Indicador de generación de consumo
				this.utilitarioMap.put(this.indicesDetMedAdminHojaAnes[6]+cont,ConstantesBD.acronimoSi);
				//seccion
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[5]+cont,ConstantesSeccionesParametrizables.subSeccionAccesosVasculares+"");
				
				
				cont++;
			}
		}		
		
		//**********************************************
		//Consulta la información de las Infusiones
		mapaSeccion = this.consultarInfusionesAgrupadas(con, numeroSolicitud);
		numRegistros = Integer.parseInt(mapaSeccion.get("numRegistros").toString());
				
		for(int j = 0 ; j<numRegistros; j++)
		{
			if(!mapaSeccion.get(this.indicesDetAdmInfusionesHojaAnes[4]+j).toString().equals("0")
					&& !mapaSeccion.get(this.indicesDetAdmInfusionesHojaAnes[4]+j).toString().equals(""))
			{
				//Codigo de la mezcla
				this.utilitarioMap.put(this.indicesInfusionesHojaAnes[12]+cont,mapaSeccion.get(this.indicesInfusionesHojaAnes[12]+j));
				//descripción de la mezcla
				this.utilitarioMap.put(this.indicesInfusionesHojaAnes[3]+cont,mapaSeccion.get(this.indicesInfusionesHojaAnes[3]+j));
				//Otra Infusion
				this.utilitarioMap.put(this.indicesInfusionesHojaAnes[4]+cont,mapaSeccion.get(this.indicesInfusionesHojaAnes[4]+j));
				//Indicador de otra Infusion
				this.utilitarioMap.put(this.indicesInfusionesHojaAnes[11]+cont,mapaSeccion.get(this.indicesInfusionesHojaAnes[11]+j));
				//codigo articulo
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[2]+cont,mapaSeccion.get(this.indicesDetAdmInfusionesHojaAnes[2]+j));
				//descripción articulo
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[6]+cont,mapaSeccion.get(this.indicesDetAdmInfusionesHojaAnes[3]+j).toString());
				//Cantidad total de dosis
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[4]+cont,mapaSeccion.get(this.indicesDetAdmInfusionesHojaAnes[4]+j));		
				//Seccion 
				this.utilitarioMap.put(this.indicesMedAdminHojaAnes[5]+cont,ConstantesSeccionesParametrizables.seccionInfusiones+"");
				//Indicador de generación de consumo
				this.utilitarioMap.put(this.indicesDetMedAdminHojaAnes[6]+cont,ConstantesBD.acronimoSi);
				
				cont++;
			}
		}
						
		//Tamaóo del mapa
		this.utilitarioMap.put("numRegistros",cont);		
		return this.utilitarioMap;
	}
	
	//************************************************************************************************************
	
	/**
	 * Guarda la información de la generacion de consumos para los articulos con cantidad de las secciones
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param HashMap datos
	 * @param UsuarioBasico usuario
	 * */
	public boolean insertarConsumosSecciones(Connection con,String numeroSolicitud,HashMap datos,UsuarioBasico usuario)
	{
		boolean respuesta = true;		
		
		//Verifica que exista el consumo y que no este finalizado		
		if(MaterialesQx.existeConsumoMateriales(con, numeroSolicitud, true))				
		{
			MaterialesQx materiales = new MaterialesQx();
			materiales.setUsuario(usuario.getLoginUsuario());
			materiales.setNumeroSolicitud(Integer.parseInt(numeroSolicitud));			
			
			if(materiales.reversionFinalizacionConsumo(con)>0)				
				this.cargarErrorElementoAp("errors.notEspecific","Los Medicamentos Registrados se Adicionaron al Consumo de Materiales.","");			
			else
			{
				respuesta = false;
				this.errores.add("descripcion",new ActionMessage("errors.notEspecific","No Fue posible Adicionar los Medicamentos al Consumo de Materiales. (Reversar Finalización). "));
				return respuesta;
			}
		}
		
		//Generar el consumo si no existe
		MaterialesQx.insertarEncabezadoMaterialesQxAutomatico(con, numeroSolicitud, usuario);
		
		//Genera los consumos de los articulos
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		int codigoDetMateQx = 0;
		
		for(int i = 0 ; i < numRegistros; i++)
		{
			//Valida que se hubiera activado el indicador de la generación de consumo
			if(datos.get(this.indicesDetMedAdminHojaAnes[6]+i).toString().equals(ConstantesBD.acronimoSi))
			{					
				//inserta el consumo del medicamento
				codigoDetMateQx = MaterialesQx.insertarConsumoArticuloAutomatico(
									con,
									numeroSolicitud,
									UtilidadTexto.aproximarSiguienteUnidad(datos.get(indicesMedAdminHojaAnes[2]+i).toString()),
									UtilidadTexto.aproximarSiguienteUnidad(datos.get(indicesMedAdminHojaAnes[4]+i).toString()),
									usuario.getLoginUsuario());
				
				if(codigoDetMateQx > 0)
				{
					//Seccion de Liquidos
					if(datos.get(this.indicesMedAdminHojaAnes[5]+i).toString().equals(ConstantesSeccionesParametrizables.seccionLiquidos+""))
					{
						//actualiza la información de los articulos indicando que se les genero el consumo
						if(!this.actualizarGenConsumoDetaAdminis(con,
								ConstantesBD.acronimoSi,
								datos.get(indicesMedAdminHojaAnes[2]+i).toString(),
								numeroSolicitud,
								codigoDetMateQx,
								ConstantesSeccionesParametrizables.seccionLiquidos+"",
								usuario.getLoginUsuario()))							
						{
							this.errores.add("descripcion",new ActionMessage("errors.notEspecific","No se actualizo el indicador de Generación de Cargo para el Articulo [Liquidos] "+datos.get(indicesMedAdminHojaAnes[6]+i).toString()));
							respuesta = false;							
						}							
					}
					
					//sección de Anestesicos y Medicamentos administrados
					if(datos.get(this.indicesMedAdminHojaAnes[5]+i).toString().equals(ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis+""))
					{
						//actualiza la información de los articulos indicando que se les genero el consumo
						if(!this.actualizarGenConsumoDetaAdminis(con,
								ConstantesBD.acronimoSi,
								datos.get(indicesMedAdminHojaAnes[2]+i).toString(),
								numeroSolicitud,
								codigoDetMateQx,
								ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis+"",
								usuario.getLoginUsuario()))							
						{
							this.errores.add("descripcion",new ActionMessage("errors.notEspecific","No se actualizo el indicador de Generación de Cargo para el Articulo [Anestesicos - Med. Administrados] "+datos.get(indicesMedAdminHojaAnes[6]+i).toString()));
							respuesta = false;							
						}							
					}
					
					//seccion Informacion General. Subseccion Via Aerea
					if(datos.get(this.indicesMedAdminHojaAnes[5]+i).toString().equals(ConstantesSeccionesParametrizables.subSeccionViaArea+""))
					{
						//actualiza el indicador de los articulos del detalle de la via aerea
						if(!this.actualizarGenConsuDetViaArea(con,
								ConstantesBD.acronimoSi,datos.get(this.indicesDetViaAerea[0]+i).toString(),
								datos.get(indicesMedAdminHojaAnes[2]+i).toString(),
								codigoDetMateQx
								))
						{
							this.errores.add("descripcion",new ActionMessage("errors.notEspecific","No se actualizo el indicador de Generación de Cargo para el Articulo [Via Aerea] "+datos.get(this.indicesMedAdminHojaAnes[6]+i).toString()));
							respuesta = false;
						}
					}
					
					
					//Seccion Informacion General - Subseccion Accesos Vasculares
					if(datos.get(this.indicesMedAdminHojaAnes[5]+i).toString().equals(ConstantesSeccionesParametrizables.subSeccionAccesosVasculares+""))
					{
						logger.info("codigo accesos >> "+datos.get("cod_acc_vascular_hoja_anes_"+i).toString());
						if(!AccesosVascularesHA.actualizarGeneroConsumo(
								con,
								Double.parseDouble(datos.get("cod_acc_vascular_hoja_anes_"+i).toString()),
								codigoDetMateQx))
						{
							this.errores.add("descripcion",new ActionMessage("errors.notEspecific","No se actualizo el indicador de Generación de Cargo para el Articulo [Accesos Vasculares] "+datos.get("descarticulo_"+i).toString()));
							respuesta = false;
						}
					
					}
					
					//seccion de Infusiones
					if(datos.get(this.indicesMedAdminHojaAnes[5]+i).toString().equals(ConstantesSeccionesParametrizables.seccionInfusiones+""))
					{
						//actualiza la información de los articulos indicando que se les genero consumo							
						if(!this.actualizarGenConsuArtiMezcla(con,
								numeroSolicitud,
								ConstantesBD.acronimoSi,
								datos.get(indicesMedAdminHojaAnes[2]+i).toString(),						
								datos.get(this.indicesInfusionesHojaAnes[12]+i).toString(),
								codigoDetMateQx,
								datos.get(this.indicesInfusionesHojaAnes[4]+i).toString(),
								usuario.getLoginUsuario()))
						{
							this.errores.add("descripcion",new ActionMessage("errors.notEspecific","No se actualizo el indicador de Generación de Cargo para el Articulo [Infusiones] "+datos.get(this.indicesMedAdminHojaAnes[6]+i).toString()));
							respuesta = false;	
						}
					}
				}
				else
				{
					this.errores.add("descripcion",new ActionMessage("errors.notEspecific","No se genero el consumo para el articulo "+datos.get(indicesMedAdminHojaAnes[6]+i).toString()));
					respuesta = false;										
				}
			}
		}
			
		return respuesta;
	}
	
	//************************************************************************************************************

	/**
	 * Actualiza las cantidades de detalle materiales qx
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public static boolean actualizarCantidadesMaterialesQx(Connection con, String numeroSolicitud)
	{
		//Actualiza las cantidades del pedido Qx. para la seccion de liquidos
		actualizarCantidadesDetMatQx(con, ConstantesSeccionesParametrizables.seccionLiquidos, numeroSolicitud);
		//Actualiza las cantidades del pedido Qx. para la seccion de Anestesicos y medicamentos administrados
		actualizarCantidadesDetMatQx(con, ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis, numeroSolicitud);
		//Actualiza las cantidades del pedido Qx. para la seccion de Infusiones
		actualizarCantidadesDetMatQx(con, ConstantesSeccionesParametrizables.seccionInfusiones, numeroSolicitud);				
		//Actualiza las cantidades del pedido Qx. para la seccion de Via Area
		actualizarCantidadesDetMatQx(con, ConstantesSeccionesParametrizables.subSeccionViaArea, numeroSolicitud);
		//Actualiza las cantidades del pedido Qx. para la seccion de accesos Vasculares
		actualizarCantidadesDetMatQx(con, ConstantesSeccionesParametrizables.subSeccionAccesosVasculares, numeroSolicitud);
		
		return true;
	}

	//************************************************************************************************************
	
	/**
	 * Genera la liquidación de la Cirugia
	 * @param Connection con
	 * @param LiquidacionServicios mundoLiquidacionServicios
	 * @param String numeroSolicitud
	 * @param UsuarioBasico usuario
	 * */
	public boolean generarliquidacion(
			Connection con,
			LiquidacionServicios mundoLiquidacionServicios,
			String numeroSolicitud,
			UsuarioBasico usuario) throws IPSException
	{
		//*******************************
		//Por requerimiento no se debe mostrar ningun mensaje al usuario sobre la generacion de la liquidacion 
		//*******************************
		
		this.setUtilitarioList1(new ArrayList());
		
		//Valida que la HQ este finalizada y el Consumo de Materiales este finalizado
		if(UtilidadTexto.getBoolean(HojaQuirurgica.estaFinalizadaHojaQx(con,numeroSolicitud)) && UtilidadesSalas.consultarConsumoMaterialesFinalizado(con, Utilidades.convertirAEntero(numeroSolicitud)))
		{				
			//Valida parametros Liquidaci�n Autom�tica de Cirug�as o Liquidaci�n Autom�tica de No Qx este activo
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getLiquidacionAutomaticaCirugias(usuario.getCodigoInstitucionInt())) ||
					UtilidadTexto.getBoolean(ValoresPorDefecto.getLiquidacionAutomaticaNoQx(usuario.getCodigoInstitucionInt()))){
			
				if(!mundoLiquidacionServicios.realizarLiquidacion(con, numeroSolicitud, usuario, true))
				{
					//logger.info("valir del arra en el mundo >> "+mundoLiquidacionServicios.getMensajesError().size());
					//this.setUtilitarioList1(mundoLiquidacionServicios.getMensajesError());
					//this.cargarErrorElementoAp("errors.notEspecific","No se Genero la Liquidaci�n de Cirugia","");
					return false;																
				
				}else 
					return true;
			}
			//this.setUtilitarioList1(mundoLiquidacionServicios.getMensajesError());
		}
		else
		{
			//this.cargarErrorElementoAp("errors.notEspecific","La Hoja Quir�rgica debe Estar en estado Finalizada para la Generaci�n de la Liquidaci�n.","");
			return false;
		}
		
		//this.cargarErrorElementoAp("errors.notEspecific","Se Genero la Liquidaci�n de Cirugia","");
		return true;
	}
	
	//************************************************************************************************************
	
	/**
	 * Actualiza el estado de la solicitud
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String tipoCargoSolicitud
	 * @param String peticion
	 * @param int codigoMedico
	 * */
	public boolean actualizarEstadoSolicitud(
			Connection con,
			String numeroSolicitud,
			String tipoCargoSolicitud,
			String peticion,
			int codigoMedico)
	{
		//Verifica si esta finalizada la hoja de Anestesia 
		String estaFinalizadaHojaQx = HojaQuirurgica.estaFinalizadaHojaQx(con,numeroSolicitud);
		
		//Validación del estado de la Hoja Quirurgica
		if(HojaQuirurgica.existeHojaQx(con,numeroSolicitud))
		{
			//Verifica si el tipo de cargo es Cirugia
			if(tipoCargoSolicitud.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia))
			{
				if(estaFinalizadaHojaQx.equals(ConstantesBD.acronimoSi))
				{
					HojaQuirurgica.cambiarEstadoSolicitud(con,ConstantesBD.codigoEstadoHCInterpretada+"",numeroSolicitud,codigoMedico+"");
					HojaQuirurgica.cambiarEstadoPeticion(con,ConstantesBD.codigoEstadoPeticionAtendida+"", peticion);
				}					
				else
					HojaQuirurgica.cambiarEstadoSolicitud(con,ConstantesBD.codigoEstadoHCRespondida+"",numeroSolicitud,codigoMedico+"");			
				
			}
			else if(tipoCargoSolicitud.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento))
			{
				//Verifica que ninguno de los servicios tenga requerido el indicador de requiere Interpretacion
				if(this.esRequerioInterServicioSoli(con, numeroSolicitud).equals(ConstantesBD.acronimoNo))
				{
					if(estaFinalizadaHojaQx.equals(ConstantesBD.acronimoSi))
					{
						HojaQuirurgica.cambiarEstadoSolicitud(con,ConstantesBD.codigoEstadoHCInterpretada+"",numeroSolicitud,codigoMedico+"");
						HojaQuirurgica.cambiarEstadoPeticion(con,ConstantesBD.codigoEstadoPeticionAtendida+"", peticion);
					}
					else
						HojaQuirurgica.cambiarEstadoSolicitud(con,ConstantesBD.codigoEstadoHCRespondida+"",numeroSolicitud,codigoMedico+"");					
				}
				else
				{
					this.cargarErrorElementoAp("errors.notEspecific","El Estado de la Solicitud no pudo ser actualizada a Interpretada, La Solicitud posee Servicios que requieren ser interpretados.","");
					return false;					
				}				
			}					
		}
		else
		{
			this.cargarErrorElementoAp("errors.notEspecific","El Estado de la Solicitud no pudo ser actualizada a Interpretada, la Hoja Quirúrgica no se encuentra Finalizada o no ha sido Creada","");
			return false;
		}	
		
		if(estaFinalizadaHojaQx.equals(ConstantesBD.acronimoSi))
			this.cargarErrorElementoAp("errors.notEspecific","El Estado de la Solicitud fue actualizada a Interpretada.","");
		else
			this.cargarErrorElementoAp("errors.notEspecific","El Estado de la Solicitud fue actualizada a Respondida.","");
		
		return true;
	}
	
	//************************************************************************************************************
	
	/**
	 * Finaliza y desfinaliza la Hoja de Anestesia
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String salidaPaciente (codigo de las salidas del paciente)
	 * @param String esFinalizadaoHojaAnestesia
	 * @param String fechaIngSala
	 * @param String horaIngSala
	 * @param String fechaSalSala
	 * @param String horaSalSala
	 * @param UsuarioBasico usuario
	 * @param String fechaFallece
	 * @param String horaFallece
	 * @param String diagAcronimoFallece
	 * @param String diagTipoCieFallece
	 * */
	public boolean finalizarHojaAnestesia(
			Connection con,
			String numeroSolicitud,
			String peticion,
			String codigoSalidaPaciente ,
			String esFinalizadaHojaAnestesia,
			String fechaIngSala,
			String horaIngSala,
			String fechaSalSala,
			String horaSalSala,
			UsuarioBasico usuario,
			PersonaBasica paciente,
			String fechaFallece,
			String horaFallece,
			String diagAcronimoFallece,
			String diagTipoCieFallece,
			ArrayList<DtoArticuloIncluidoSolProc> arrayArtIncluidos,
			HashMap justificacionArticuloIncl,
			HashMap medicamentosNoPosMap,
			HashMap medicamentosPosMap,
			HashMap sustitutosNoPosMap,
			HashMap diagnosticosDefinitivos) throws IPSException
	{
		boolean respuesta = true;
		indicadorUtilitario = "";
		String registroDesde=null;
		
		//Verifica si la Hoja de Anestesia se encuentra finalizada
		if(esFinalizadaHojaAnestesia.equals(ConstantesBD.acronimoNo))
		{
			if(!codigoSalidaPaciente.equals(""))
			{					
				String datosMedico = usuario.getInformacionGeneralPersonalSalud();
				if(datosMedico.length() > 254)
					datosMedico = datosMedico.substring(0,254);
				
				//verifica el indicativo registrado desde (Hoja Anestesia)
				registroDesde = consultarIndicativoRegistroDesde(con, Integer.valueOf(numeroSolicitud.toString()));
					
				if(registroDesde==null || registroDesde.isEmpty() || registroDesde.equals(ConstantesBDSalas.acronimoRegistroHojaQuirurgica))
					actualizarIndicativoRegistroDesde(con, Integer.valueOf(numeroSolicitud.toString()), ConstantesBDSalas.acronimoRegistroHojaAnestesia);
				
				//Actualiza la informacion de la Hoja de Anestesia
				if(this.actualizarHojaAnestesiaSalidaPaciemte(con,
						datosMedico,
						ConstantesBD.acronimoSi,
						UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
						UtilidadFecha.getHoraActual(),
						numeroSolicitud,
						usuario.getCodigoInstitucion()))
				{
					//Inserta información en la tabla salida paciente
					int consecutivo = this.insertarSalidaSalaPaciente(con, numeroSolicitud, codigoSalidaPaciente, usuario.getLoginUsuario());
										
					if(consecutivo != ConstantesBD.codigoNuncaValido )
					{						
						HashMap datos = new HashMap();
						datos.put("fechaIngSala",fechaIngSala);
						datos.put("horaIngSala",horaIngSala);
						datos.put("fechaSalSala",fechaSalSala);
						datos.put("horaSalSala",horaSalSala);
						datos.put("salPac", consecutivo);
						datos.put("desSelec",ConstantesBD.acronimoNo);
						datos.put("numSol",numeroSolicitud);
						datos.put("usuario",usuario.getLoginUsuario());
						
						//Actualiza la información en la tabla solicitudes_cirugia
						if(HojaQuirurgica.actualizarSalidaPaciente(con, datos))
						{							
							HashMap fallece = new HashMap();
							fallece.put("fechaFallece",fechaFallece);
							fallece.put("horaFallece",horaFallece);
							fallece.put("numeroSolicitud",numeroSolicitud);
							fallece.put("diagAcronimo",diagAcronimoFallece);
							fallece.put("diagTipoCie",diagTipoCieFallece);						
							
							HojaQuirurgica.ActualizarFallece(con,fallece);
							//Indica si la operacion es de reversion o de salida paciente
							indicadorUtilitario = "salidaPaciente";
							respuesta = true;		
							
							//Genera los cargos directos de articulos incluidos
							if(!RespuestaProcedimientos.generarCargosDirectosArtIncluidosHojasQA(
									con, 
									usuario, 
									paciente,
									Utilidades.convertirAEntero(numeroSolicitud),
									arrayArtIncluidos,
									justificacionArticuloIncl,
									medicamentosNoPosMap,
									medicamentosPosMap,
									sustitutosNoPosMap,
									diagnosticosDefinitivos,
									true))
							{
								this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Generar los Cargos Directos de Articulos Incluidos"));
								respuesta = false;
							}							
						}
						else
						{
							this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar la Salida del Paciente en Solicitudes Cirugia"));
							respuesta = false;
						}							
					}
					else
					{
						this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Ingresar registro en Salida Paciente"));
						respuesta = false;
					}
				}
				else
				{
					this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar la Hoja de Anestesia en estado Finalizada"));
					respuesta = false;								
				}
			}
			else
			{
				this.errores.add("descripcion",new ActionMessage("errors.notEspecific","La Hoja de Anestesia no esta finalizada, el estado no puede ser la desfinalización."));
				respuesta = false;			
			}
		}
		else if (esFinalizadaHojaAnestesia.equals(ConstantesBD.acronimoSi))
		{
			//Se va a deseleccionar la salida del paciente
			if(codigoSalidaPaciente.equals(""))
			{
				//Actualiza el indicativo de registro 
				actualizarIndicativoRegistroDesde(con, Integer.valueOf(numeroSolicitud.toString()), ConstantesBDSalas.acronimoRegistroHojaAnestesia);
				
				//Actualiza la informaci�n de la Hoja de Anestesia
				if(this.actualizarHojaAnestesiaSalidaPaciemte(con,
						"",
						ConstantesBD.acronimoNo,
						"",
						"",
						numeroSolicitud,
						usuario.getCodigoInstitucion()))
				{
					HashMap datos = new HashMap();
					datos.put("fechaIngSala",fechaIngSala);
					datos.put("horaIngSala",horaIngSala);
					datos.put("fechaSalSala","");
					datos.put("horaSalSala","");
					datos.put("salPac","");
					datos.put("desSelec",ConstantesBD.acronimoSi);
					datos.put("numSol",numeroSolicitud);
					datos.put("usuario",usuario.getLoginUsuario());
					
					//Actualiza la informaci�n en la tabla solicitudes_cirugia
					if(HojaQuirurgica.actualizarSalidaPaciente(con, datos))
					{						
						if(HojaQuirurgica.cambiarEstadoSolicitud(con,ConstantesBD.codigoEstadoHCRespondida+"",numeroSolicitud,usuario.getCodigoPersona()+"") && 
								HojaQuirurgica.cambiarEstadoPeticion(con,ConstantesBD.codigoEstadoPeticionPendiente+"",peticion))
						{															
							respuesta = true;
							//Indica si la operacion es de reversion o de salida paciente
							indicadorUtilitario = "reversarPaciente";
														
							//Genera los cargos directos de articulos incluidos
							if(!RespuestaProcedimientos.generarCargosDirectosArtIncluidosHojasQA(
									con, 
									usuario, 
									paciente,
									Utilidades.convertirAEntero(numeroSolicitud),
									arrayArtIncluidos,
									justificacionArticuloIncl,
									new HashMap(),
									new HashMap(),
									new HashMap(),
									new HashMap(),
									false))
							{
								this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Generar los Cargos Directos de Articulos Incluidos"));
								respuesta = false;
							}							
						}
						else
						{
							this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar el Estado de la Solicitud y Peticion "));
							respuesta = false;
						}
					}
					else
					{
						this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar la Salida del Paciente en Solicitudes Cirugia"));
						respuesta = false;
					}							
				}
				else
				{
					this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Reversar la salida de la Hoja de Anestesia."));
					respuesta = false;		
				}
			}
			//Se modificara el motivo de la salida del paciente
			else
			{
				//Actualiza el indicativo de registro 
				actualizarIndicativoRegistroDesde(con, Integer.valueOf(numeroSolicitud.toString()), ConstantesBDSalas.acronimoRegistroHojaAnestesia);
				
				//Actualiza la informaci�n de la Hoja de Anestesia
				if(this.actualizarHojaAnestesiaSalidaPaciemte(con,
						usuario.getInformacionGeneralPersonalSalud().substring(0,254),
						ConstantesBD.acronimoSi,
						UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
						UtilidadFecha.getHoraActual(),
						numeroSolicitud,
						usuario.getCodigoInstitucion()))
				{
					//Inserta informaci�n en la tabla salida paciente
					int consecutivo = this.insertarSalidaSalaPaciente(con, numeroSolicitud, codigoSalidaPaciente, usuario.getLoginUsuario());
										
					if(consecutivo != ConstantesBD.codigoNuncaValido)
					{						
						HashMap datos = new HashMap();
						datos.put("fechaIngSala",fechaIngSala);
						datos.put("horaIngSala",horaIngSala);
						datos.put("fechaSalSala",fechaSalSala);
						datos.put("horaSalSala",horaSalSala);
						datos.put("salPac", consecutivo);
						datos.put("desSelec",ConstantesBD.acronimoNo);
						datos.put("numSol",numeroSolicitud);
						datos.put("usuario",usuario.getLoginUsuario());
						
						//Actualiza la informaci�n en la tabla solicitudes_cirugia
						if(HojaQuirurgica.actualizarSalidaPaciente(con, datos))
						{
							HashMap fallece = new HashMap();
							fallece.put("fechaFallece",fechaFallece);
							fallece.put("horaFallece",horaFallece);
							fallece.put("numeroSolicitud",numeroSolicitud);
							fallece.put("diagAcronimo",diagAcronimoFallece);
							fallece.put("diagTipoCie",diagTipoCieFallece);						
							
							HojaQuirurgica.ActualizarFallece(con,fallece);
							
							respuesta = true;
						}
						else
						{
							this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar la Salida del Paciente en Solicitudes Cirugia"));
							respuesta = false;
						}							
					}
					else
					{
						this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Ingresar registro en Salida Paciente"));
						respuesta = false;
					}
				}
				else
				{
					this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar la Hoja de Anestesia en estado Finalizada"));
					respuesta = false;								
				}
			}				
		}
		
		return respuesta;
	}
	
	//************************************************************************************************************
	
	//-----------------------------------------------------------------------------------------------
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/************************************************************************************************
	 * METODOS GENERALES*****************************************************************************
	 * *********************************************************************************************/
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//-----------------------------------------------------------------------------------------------
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Metodos para la Validacion**********************************************************************
	/*************************************************************************************************/
		
	
	/**
	 * Verifica el ingreso a la funcionalidad validando el paciente cargado y el usuario que sea profesional de la salud
	 * @param Connection con 
	 * @param PersonaBasica paciente
	 * @param UsuarioBasico usuario
	 * @return true si pasa las validaciones, false de lo contrario y carga el atributo de errores
	 * */
	public boolean validacionIngresoFuncional(Connection con, PersonaBasica paciente, UsuarioBasico usuario)  
	{
		
		//Valida que el usuario que ingresa sea profesional de la salud
		if(usuario == null )
		{
			this.errores.add("descripcion",new ActionMessage("errors.usuario.noCargado"));
			return false;
		}
		else if (!UtilidadValidacion.esProfesionalSalud(usuario))
		{
			this.errores.add("descripcion",new ActionMessage("errors.usuario.noAutorizado"));
			return false;
		} 
		
		//Valida que el paciente este cargado en session
		if(paciente.getCodigoPersona()<1)
		{		
			this.errores.add("descripcion",new ActionMessage("errors.paciente.noCargado"));
			return false;
		}
		
		if(validacionEsCuentaValida(con,paciente))
		{
			//Verifica que el estado del Ingreso se encuentre abierto			
			if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con,paciente.getCodigoIngreso()).getCodigo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
			{
				this.errores.add("descripcion",new ActionMessage("errors.ingresoEstadoDiferenteAbierto"));
				return false;
			}	
		}
		else		
			return false;		
		
		return true;		
	}	
	
	//***********************************************************************************************************
	
	/**
	 * Verifica si el paciente posee cuenta valida
	 * @param Connection con
	 * @param PersonaBasica paciente
	 * */
	public boolean validacionEsCuentaValida(Connection con, PersonaBasica paciente)
	{
		//Se verifica si al paciente se le puede crear un nuevo ingreso		
		RespuestaValidacion respPrevia = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
		
		if(respPrevia.puedoSeguir)
			return true;
		else
		{
			this.errores.add("descripcion",new ActionMessage(respPrevia.textoRespuesta));
			return false;			
		}
	}
	
	//***********************************************************************************************************
	
	
	/**
	 * Valida si el usuario cargado en session puede modificar la funcionalidad o solo consultarla, 
	 * especialidad = anestisiologia , ocupacion = realiza cirugias 
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * @param validarEspecialidad
	 * @return true si pasa las validaciones, false de lo contrario
	 * */
	public static boolean validacionEsModificableHojaXMedico(Connection con,UsuarioBasico usuario,boolean validarEspecialidad)
	{		
		
		if(UtilidadValidacion.esOcupacionQueRealizaCx(con,usuario.getCodigoOcupacionMedica(),usuario.getCodigoInstitucionInt()) && 
				!validarEspecialidad)
			return true;
				
		//C�digo de la especialidad anestesiolog�a parametrizado en valores por defecto
		if(validarEspecialidad)
		{
			int especialidadAnestesiologia=Integer.parseInt(ValoresPorDefecto.getEspecialidadAnestesiologia(usuario.getCodigoInstitucionInt(), true));
		
			if(UtilidadValidacion.esOcupacionQueRealizaCx(con,usuario.getCodigoOcupacionMedica(),usuario.getCodigoInstitucionInt()) && 
					UtilidadValidacion.esMedicoEspecialidad(con, usuario.getCodigoPersona(), especialidadAnestesiologia))
				return true;
		}
				
		return false;
	}
	
	//***********************************************************************************************************
	
	
	/**
	 * Valida si el usuario tiene rol de la funcionalidad
	 * @param Connection con
	 * @param String loginUsuario
	 * @param int nroFuncionalidad
	 * @param String nombreFuncionalidad
	 * @return true si posee rol, false de lo contrario
	 * */
	public boolean validacionRolFuncionalidad(
			Connection con,			
			UsuarioBasico usuario, 
			int nroFuncionalidad,
			String nombreFuncionalidad)
	{	
		
		if(!Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(),nroFuncionalidad))
		{
			this.errores.add("descripcion",new ActionMessage("errors.usuarioSinRolFuncionalidad",usuario.getLoginUsuario(),nombreFuncionalidad));
			return false;		
		}
		
		/*		
		 * Se solicita quitar esta validacion por la tarea 60161 la cual fue la misma que solicito ponerla; puesto que el 
		 * analista funcional de software oscar cely se equivoca en la interpretacion del documento. 
		 if (!UtilidadValidacion.esOcupacionQueRealizaCx(con, usuario.getCodigoOcupacionMedica(), usuario.getCodigoInstitucionInt()))
		 {
				this.errores.add("descripcion",new ActionMessage("errors.profesionalRealizaCirugias",usuario.getLoginUsuario()));
				return false;	
		 }*/
							
		return true;		
	}
	
	//***********************************************************************************************************
	
	/**
	 * Verifica que los datos para la creacion de la solicitud esten correctos
	 * @param HashMap datos Solicitud
	 * @param HashMap datos Peticion con una solo posicion o registro seleccionado
	 * @param String indexPeticion
	 * @return true si no ocurre errores, false de lo contrario y carga el atributo de errores del objecto
	 * */
	public boolean validacionDatosSolicitud(HashMap datosSolicitud, HashMap datosPeticion,String indexPeticion)
	{		
		
		boolean respuesta = true;
		
		//Validacion de la fecha de Solicitud	
		if(datosSolicitud.get("fechaSolicitud2").toString().equals(""))
		{
			this.errores.add("descripcion",new ActionMessage("errors.required","Fecha de Solicitud de la Petici�n ["+datosPeticion.get("codigoPeticion2_0")+"] del Registro Nro. "+indexPeticion));
			respuesta = false;
		}
		else
		{
			if(!UtilidadFecha.validarFecha(datosSolicitud.get("fechaSolicitud2").toString()))
			{
				this.errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido",datosSolicitud.get("fechaSolicitud2").toString()+" de la Petici�n ["+datosPeticion.get("codigoPeticion2_0")+"] del Registro Nro. "+indexPeticion));
				respuesta = false;
			}
		}
		
		//Validacion de la Hora de Solicitud
		if(datosSolicitud.get("horaSolicitud3").toString().equals(""))
		{
			this.errores.add("descripcion",new ActionMessage("errors.required","Hora de Solicitud de la Petici�n ["+datosPeticion.get("codigoPeticion2_0")+"] del Registro Nro. "+indexPeticion));
			respuesta = false;
		}
		else
		{
			if(!UtilidadFecha.validacionHora(datosSolicitud.get("horaSolicitud3").toString()).puedoSeguir)
			{
				this.errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido",datosSolicitud.get("horaSolicitud3").toString()+" de la Petici�n ["+datosPeticion.get("codigoPeticion2_0")+"] del Registro Nro. "+indexPeticion));
				respuesta = false;
			}
		}
		
		//Validacion del centro de costo
		if(datosSolicitud.get("ccSolicitado0").toString().equals(""))
		{
			this.errores.add("descripcion",new ActionMessage("errors.required","Centros de Costo de la Petici�n ["+datosPeticion.get("codigoPeticion2_0")+"] del Registro Nro. "+indexPeticion));
			respuesta = false;
		}
		
		//Validacion de la especialidad		
		if(datosSolicitud.get("especialidad4").toString().equals(""))
		{
			this.errores.add("descripcion",new ActionMessage("errors.required","La Especialidad de la Petici�n ["+datosPeticion.get("codigoPeticion2_0")+"] del Registro Nro. "+indexPeticion));
			respuesta = false;
		}
						
		return respuesta;
	}		
		
	
	//***********************************************************************************************************
	
	/**
	 * M�todo para saber si la Hoja de anestesia o Hoja Quir�rgica est� en estado finalizada y si esta Creada
	 * @param con -> conexion
	 * @param nroSolicitud
	 * @param consultarHojaQx true = hojaQuirurgica, false = hojaAnestesia
	 * @return InfoDatos. acronimo = finalizada (S,N), descripcion = creada (S,N)
	 */
	public static InfoDatos validacionEsFinalizadaCreadaHoja(Connection con, int nroSolicitud, boolean consultarHojaQx)
	{
		return getHojaAnestesiaDao().esFinalizadaCreadaHoja(con, nroSolicitud, consultarHojaQx);
	}
	
	//***********************************************************************************************************
	
	/**
	 * M�todo para saber si la Hoja de anestesia o Hoja Quir�rgica est� en estado finalizada y si esta Creada
	 * @param con -> conexion
	 * @param nroSolicitud
	 * @param consultarHojaQx true = hojaQuirurgica, false = hojaAnestesia
	 * @return InfoDatos. acronimo = finalizada (S,N), descripcion = creada (S,N)
	 */
	public static boolean esFinalizadaHojaAnestesia(Connection con,int nroSolicitud)
	{
		boolean resp=false;			
		
		InfoDatos info= getHojaAnestesiaDao().esFinalizadaCreadaHoja(con, nroSolicitud, false);
		if(info.getAcronimo().equals(ConstantesBD.acronimoSi))
			resp=true;
		
		return resp;
	}
	
	//***********************************************************************************************************
	
	/**
	 * M�todo para saber si es posible modificar la informaci�n de la Hoja de Anestesia, 
	 * se crea una conexion interna por lo que se debe llamar en los casos en los cuales 
	 * no se posee una conexion disponible. se evalua que el usuario cumpla con las validaciones
	 * y que la Hoja de Anestesia no este finalizada
	 * 
	 * @param UsuarioBasico usuario 
	 * @param int nroSolicitud	 
	 */
	public static boolean esSoloConsulta(UsuarioBasico usuario,int nroSolicitud)
	{
		boolean resp=false;				
		Connection con= UtilidadBD.abrirConexion();
					
		InfoDatos info= getHojaAnestesiaDao().esFinalizadaCreadaHoja(con, nroSolicitud, false);			
			
		//Indica si solo se puede consultar la pagina y no modificarla
		resp =  UtilidadTexto.getBoolean(validacionEsSoloConsulta
										 (
											validacionEsModificableHojaXMedico(con, usuario,true)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo,
											info.getAcronimo())
										 );	
				
		UtilidadBD.closeConnection(con);		
		return resp;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Validacion para el ingreso de especialidades intervienen
	 * Metodo para la SubSeccion Especialidades que Intervienen y Cirujanos Principales (Seccion Informacion General) 
	 * @param HashMap mapa
	 * */
	public boolean validacionEspecialidadesIntervienen(HashMap mapaVerificar)
	{
		int numRegistros = Integer.parseInt(mapaVerificar.get("numRegistros").toString());
		
		for(int i = 0; i < numRegistros; i++)
		{
			if(mapaVerificar.get(this.indicesEspecialidadesInter[5]).toString().equals(mapaVerificar.get(this.indicesEspecialidadesInter[1]+i).toString()))
			{
				this.errores.add("descripcion",new ActionMessage("errors.notEspecific","La Especialidad "+mapaVerificar.get(this.indicesEspecialidadesInter[6]).toString()+" ya se encuentra Ingresada en el Registro Nro."+(i+1)));
				return false;
			}
		}
		
		return true;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Validacion para el ingreso de Cirujanos Principales
	 * Metodo para la SubSeccion Especialidades que Intervienen y Cirujanos Principales (Seccion Informacion General) 
	 * @param HashMap mapa
	 * */
	public boolean validacionCirujanosPrincipales(HashMap mapaVerificar)
	{
		int numRegistros = Integer.parseInt(mapaVerificar.get("numRegistros").toString());
		
		for(int i = 0; i < numRegistros; i++)
		{
			if(mapaVerificar.get(this.indicesCirujanosPrincipales[7]).toString().equals(mapaVerificar.get(this.indicesCirujanosPrincipales[4]+i).toString()))
			{
				this.errores.add("descripcion",new ActionMessage("errors.notEspecific","El Medico Cirujano "+mapaVerificar.get(this.indicesCirujanosPrincipales[5]+i).toString()+" ya se encuentra Ingresado para la Especialidad "+mapaVerificar.get(this.indicesCirujanosPrincipales[3]+i).toString()));
				return false;
			}
		}
		
		return true;
	}
	
	
	//***********************************************************************************************************
	/***
	 * Validaciones de la fecha y hora de Ingreso a la Sala
	 * @param String fechaIngresoSala
	 * @param String horaIngresoSala
	 * @param String fechaIngresoPaciente
	 * @param String horaIngresoPaciente
	 * @param int institucion
	 * */
	public boolean validacionIngresoFechaHoraIngresoSala(
			Connection con,
			int numeroSolicitud,
			String fechaIngresoSala, 
			String horaIngresoSala,
			String fechaEgresoSala,
			String horaEgresoSala,
			String fechaIngresoPaciente,
			String horaIngresoPaciente,
			String fechaInicioCx,
			String horaInicioCx,			
			int institucion)
	{
		boolean respuesta = true;
		
		//validacion de la fecha y hora de ingreso a la sala 
		if(fechaIngresoSala.equals("") || horaIngresoSala.equals(""))
		{
			this.errores.add("descripcion",new ActionMessage("errors.required","La Fecha/Hora de Ingreso Sala " ));
			respuesta = false;
			return respuesta;
		}	
				
		//Validacion del formato de la fecha
		if(!UtilidadFecha.validarFecha(fechaIngresoSala))
		{
			this.errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido",fechaIngresoSala));
			respuesta = false;
		}
		else
		{
			//validacion de la hora
			if(!UtilidadFecha.validacionHora(horaIngresoSala).puedoSeguir)
			{
				this.errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido",horaIngresoSala));
				respuesta = false;				
			}
			else
			{
				String[] fechaHora = {"",""};
				String mensaje = "";
				
				if(UtilidadCadena.noEsVacio(ValoresPorDefecto.getMinutosMaximosRegistroAnestesia(institucion)))
				{
					//Validaci�n de los minutos m�ximos permitidos para el registro de anestesia con fecha diferente del sistema
					int minutosLimite = Utilidades.convertirAEntero(ValoresPorDefecto.getMinutosMaximosRegistroAnestesia(institucion), true);
					
					if(minutosLimite == ConstantesBD.codigoNuncaValido)
					{
						minutosLimite = 0;
						mensaje = " Actual ";
					}
					else
						mensaje = " Maxima Permitida con Fecha Diferente del Sistema ";												
					
					fechaHora = UtilidadFecha.incrementarMinutosAFechaHora(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(), minutosLimite, false);					
					
				}
				else
				{
					fechaHora[0] = UtilidadFecha.getFechaActual();
					fechaHora[1] = UtilidadFecha.getHoraActual();
					mensaje = " Actual ";
				}
				
				if(UtilidadFecha.compararFechas(fechaIngresoSala,horaIngresoSala,fechaIngresoPaciente,horaIngresoPaciente).isTrue())
				{
					if(!UtilidadFecha.compararFechas(fechaHora[0], fechaHora[1],fechaIngresoSala,horaIngresoSala).isTrue())					
					{
						this.errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual"," de Ingreso Sala "+fechaIngresoSala+" - "+horaIngresoSala,mensaje+fechaHora[0]+" - "+fechaHora[1]));
						respuesta = false;
					}
					else
					{
						//validacion de la fecha y hora Inicio/Finalizaci�n de la Cirugia 
						if(!fechaInicioCx.toString().equals("") && 
								!horaInicioCx.toString().equals(""))
						{
							if(!UtilidadFecha.compararFechas(fechaInicioCx,horaInicioCx,fechaIngresoSala,horaIngresoSala).isTrue())
							{
								this.errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual"," de Ingreso Sala "+fechaIngresoSala+" - "+horaIngresoSala," de Inicio/Fin de la Cirugia "+fechaInicioCx+" - "+horaInicioCx));
								respuesta = false;					
							}
						}
					}
				}
				else
				{
					this.errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual",fechaIngresoSala+" -  "+horaIngresoSala,fechaIngresoPaciente+" - "+horaIngresoPaciente));
					respuesta = false;
				}
			}
		}	
		
		//Valida la ocupacion de la sala 
		if(respuesta)
		{
			if(UtilidadFecha.validarFecha(fechaEgresoSala) 
					&& UtilidadFecha.validacionHora(horaEgresoSala).puedoSeguir 
						&& UtilidadFecha.validarFecha(fechaIngresoSala) 
							&& UtilidadFecha.validacionHora(horaIngresoSala).puedoSeguir)
			{
				
				//******VALIDACION DE LA OCUPACI�N DE LA SALA*************************************
				HashMap datosSala=HojaQuirurgica.estaSalaOcupada(
						con, 
						numeroSolicitud,
						ConstantesBD.codigoNuncaValido,
						fechaIngresoSala,
						horaIngresoSala,
						fechaEgresoSala,								
						horaEgresoSala);
				
				if(UtilidadTexto.getBoolean(datosSala.get("estaSalaOcupada")+""))
				{		
					int numReg=Utilidades.convertirAEntero(datosSala.get("numRegistros")+"");
					for (int i=0;i<numReg;i++)			
						errores.add("descripcion",new ActionMessage("error.salasCirugia.salaOcupada", "entre el rango de fecha/hora Sala: "+UtilidadFecha.conversionFormatoFechaAAp(datosSala.get("fechaIngresoSala_"+i)+"") +" "+datosSala.get("horaIngresoSala_"+i)+" - "+UtilidadFecha.conversionFormatoFechaAAp(datosSala.get("fechaSalidaSala_"+i)+"") +" "+datosSala.get("horaSalidaSala_"+i), ""));
					respuesta = false;
				}
				//********************************************************************************
			}
		}
			
		return respuesta;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Evalua si solo se mostrara los datos para la consulta
	 * @param String esModificableHojaXMedico
	 * @param String esFinalizadaHoja
	 * */
	public static String validacionEsSoloConsulta(String esModificableHojaXMedico,String esFinalizadaHoja)
	{
		if(esModificableHojaXMedico.equals(ConstantesBD.acronimoNo) 
				|| esFinalizadaHoja.equals(ConstantesBD.acronimoSi))
				return ConstantesBD.acronimoSi;
		
		return ConstantesBD.acronimoNo;
	}
	
	//***********************************************************************************************************
	/**
	 * Verifica si existen medicamentos administrados para ingresar una nueva dosis
	 * @param HashMap mapa
	 * @param booelan esDetalle
	 * */
	public boolean validacionesExistDosisAnesteMedporIngresar(HashMap mapa,boolean esDetalle)
	{
		int numRegistros = Integer.parseInt(mapa.get("numRegistros").toString());
		
		for(int i = 0; i<numRegistros; i++)
		{			
			//Indica que existe por lo menos un registro que fue marcado para ser SUMINISTRADO
			if(!esDetalle && mapa.get(this.indicesMedAdminHojaAnes[8]+i).toString().equals(ConstantesBD.acronimoSi))
				return true;
			
			//Indica que existe por lo menos un registros que puede ser modificado en el historial de medicamentos SUMINISTRADOS
			if(esDetalle)
				return true;
		}
		
		return false;
	}
	//***********************************************************************************************************
	
	/**
	 * Verifica si existen por lo menos un articulo al cual se le generara consumo
	 * @param HashMap mapa
	 * @param booelan esDetalle
	 * */
	public boolean validacionesExisteArticuloGenerarCons(HashMap mapa,String indice)
	{	
		int numRegistros = Integer.parseInt(mapa.get("numRegistros").toString());
		
		for(int i = 0; i<numRegistros; i++)
		{
			if(mapa.get(indice+i).toString().equals(ConstantesBD.acronimoSi))
				return true;
		}
		
		return false;
	}
	//***********************************************************************************************************
	
	/**
	 * Validacion del ingreso de la fecha/hora ingreso sala
	 * @param String fechaIngresoSala
	 * @param String horaIngresoSala
	 * */
	public boolean validacionesFechaHoraIngreso(String fechaIngresoSala, String horaIngresoSala, String codigoSeccion)
	{
		boolean respuesta = true;
		String complemento = "";
		this.errores = new ActionErrors();
		
		//validacion de la fecha y hora de ingreso a la sala 
		if(fechaIngresoSala.equals("") || horaIngresoSala.equals(""))
		{
			if(codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionLiquidos+""))
				complemento = "Para el Ingreso a la Secci�n L�quidos :  ";
			else if(codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis+""))
				complemento = "Para el Ingreso a la Secci�n Anest�sicos y Medicamentos Administrados:   ";
			else if(codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionHemoderivados+""))
				complemento = "Para el Ingreso a la Secci�n Hemoderivados :   ";
			else if(codigoSeccion.equals(ConstantesSeccionesParametrizables.seccionInfusiones+""))
				complemento = "Para el Ingreso a la Secci�n Infusiones : ";
			
			this.errores.add("descripcion",new ActionMessage("errors.required",complemento+" La Fecha/Hora de Ingreso de la Secci�n Informaci�n General - Subseccion Fecha y Hora Ingreso Sala " ));
			respuesta = false;
			return respuesta;
		}
		
		return respuesta;
	}
	
	//***********************************************************************************************************
	
	
	/**
	 * Validaciones para los datos de la funcionalidad seccion Anestesicos y Medicamentos Suministrados
	 * @param HashMap mapa
	 * @param boolean esEncabezado
	 * @param String fechaIngresoSala
	 * @param String horaIngresoSala
	 * @param int codigoSeccion
	 * */
	public boolean validacionesDatosAnestesicosMedicaAdm(HashMap mapa, HashMap mapaJ, String fechaIngresoSala, String horaIngresoSala,boolean esDetalle,int codigoSeccion)
	{
		int numRegistros = Integer.parseInt(mapa.get("numRegistros").toString());
		boolean respuesta = true;
		this.errores = new ActionErrors();
				
		//validacion de la fecha y hora de ingreso a la sala 
		if(fechaIngresoSala.equals("") || horaIngresoSala.equals(""))
		{
			this.errores.add("descripcion",new ActionMessage("errors.required","La Fecha/Hora de Ingreso de la Seccion Informacion General - Subseccion Fecha y Hora Ingreso Sala " ));
			respuesta = false;
			return respuesta;
		}
		logger.info("VALIDACION DE ADMINISTRACIONWES!!!");		
		for(int i = 0; i<numRegistros; i++)
		{
			logger.info("codigoSeccion: "+codigoSeccion);
			logger.info("esDetalle: "+esDetalle);
			logger.info(this.indicesMedAdminHojaAnes[8]+i+": *"+mapa.get(this.indicesMedAdminHojaAnes[8]+i)+"*");
			logger.info(this.indicesDetMedAdminHojaAnes[6]+i+": *"+mapa.get(this.indicesDetMedAdminHojaAnes[6]+i)+"*");
			
			//Suministrar y Genero consumo
			if((!esDetalle && mapa.get(this.indicesMedAdminHojaAnes[8]+i).toString().equals(ConstantesBD.acronimoSi))
				|| (esDetalle && mapa.get(this.indicesDetMedAdminHojaAnes[6]+i).toString().equals(ConstantesBD.acronimoNo)))
			{					
				//Dosis
				if(mapa.get(this.indicesDetMedAdminHojaAnes[2]+i).toString().equals(""))
				{
					this.errores.add("descripcion",new ActionMessage("errors.required","La Dosis del Registro "+(i+1) ));
					respuesta = false;
				}
				else
				{
					if(Utilidades.convertirAEntero(mapa.get(this.indicesDetMedAdminHojaAnes[2]+i).toString(), true) <= 0)  
					{
						this.errores.add("descripcion",new ActionMessage("errors.integerMayorQue","La Dosis del Registro "+(i+1),"0"));
						respuesta = false;
					}
				}
				
				//Sello de calidad
				if(mapa.containsKey(this.indicesDetMedAdminHojaAnes[8]+i) && 
						mapa.get(this.indicesDetMedAdminHojaAnes[8]+i).toString().equals("") && 
							codigoSeccion == ConstantesSeccionesParametrizables.seccionHemoderivados)
				{
					this.errores.add("descripcion",new ActionMessage("errors.required","El Sello de Calidad del Registro "+(i+1) ));
					respuesta = false;
				}				
				
				//Validacion del formato de la fecha
				if(!UtilidadFecha.validarFecha(mapa.get(this.indicesDetMedAdminHojaAnes[3]+i).toString()))
				{
					this.errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," del Registro "+(i+1)+" "+mapa.get(this.indicesDetMedAdminHojaAnes[3]+i).toString()));
					respuesta = false;
				}
				else
				{
					//validacion de la hora
					if(!UtilidadFecha.validacionHora(mapa.get(this.indicesDetMedAdminHojaAnes[4]+i).toString()).puedoSeguir)
					{
						this.errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido"," del Registro "+(i+1)));
						respuesta = false;				
					}
					else
					{
						if(UtilidadFecha.compararFechas(mapa.get(this.indicesDetMedAdminHojaAnes[3]+i).toString(),mapa.get(this.indicesDetMedAdminHojaAnes[4]+i).toString(),fechaIngresoSala,horaIngresoSala).isTrue())
						{
							if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),mapa.get(this.indicesDetMedAdminHojaAnes[3]+i).toString(),mapa.get(this.indicesDetMedAdminHojaAnes[4]+i).toString()).isTrue())
							{
								this.errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual",mapa.get(this.indicesDetMedAdminHojaAnes[3]+i).toString()+" - "+mapa.get(this.indicesDetMedAdminHojaAnes[4]+i).toString(),UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()));
								respuesta = false;
							}
						}
						else
						{
							this.errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual",mapa.get(this.indicesDetMedAdminHojaAnes[3]+i).toString()+" - "+mapa.get(this.indicesDetMedAdminHojaAnes[4]+i).toString(),fechaIngresoSala+" - "+horaIngresoSala));
							respuesta = false;
						}
					}
				}
				if(codigoSeccion != ConstantesSeccionesParametrizables.seccionHemoderivados)
				{
					//logger.info("Entro al validate y al check>>>>>>>>>>>"+mapaJ);
					//******************Validacion Justificaciones No Pos**********//
					if(mapaJ != null)
					{
						logger.info("Siiii lleva el mapa Jus>>>>>>>>>>>");
						if(mapa.get("espos11_"+i).equals("NOPOS") || mapa.get("espos11_"+i).equals("f"))
		    			{
							logger.info("Entro al articulo no pos>>>>>>>>>>>");
		    				if(!mapaJ.containsKey(mapa.get("articulo2_"+i)+"_yajustifico") || mapaJ.get(mapa.get("articulo2_"+i)+"_yajustifico").equals("false"))
		    				{
		    					logger.info("Entro al no contiene llave>>>>>>>>>>>");
		        				errores.add("", new ActionMessage("errors.required", "La justificacion no pos del articulo "+mapa.get("articulo2_"+i)));
		        				respuesta = false;
		        			}
		    			}
					}
					//************************************************************//
				}
				
				if(codigoSeccion==ConstantesSeccionesParametrizables.seccionLiquidos)
				{
					logger.info("PASO POR SQUI: "+this.indicesMedAdminHojaAnes[9]+i+": "+mapa.get(this.indicesMedAdminHojaAnes[9]+i));
					if(UtilidadTexto.isEmpty(mapa.get(this.indicesMedAdminHojaAnes[9]+i)+""))
					{
						this.errores.add("descripcion",new ActionMessage("errors.required","El tipo de liquido del registro "+(i+1)));
						respuesta = false;
					}
				}
			}
		}
		
		return respuesta;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Valida si el registro fue modificado, si lo fue retorna true, de lo contrario false 
	 * @param Connection con
	 * @param HashMap datos
	 * @param int pos 
	 * */
	public boolean validacionesFueModificadoDetalleAdmHojaAnes(Connection con,HashMap datos,int pos)
	{
		String tmp = "";
		if(datos.containsKey(this.indicesDetMedAdminHojaAnes[8]+pos))
			tmp = datos.get(this.indicesDetMedAdminHojaAnes[8]+pos).toString();
		
		if(this.consultarDetAdminisHojaAnest(
				con,
				datos.get(this.indicesDetMedAdminHojaAnes[1]+pos).toString(),
				datos.get(this.indicesDetMedAdminHojaAnes[0]+pos).toString(),
				datos.get(this.indicesDetMedAdminHojaAnes[2]+pos).toString(),
				tmp,
				UtilidadFecha.conversionFormatoFechaABD(datos.get(this.indicesDetMedAdminHojaAnes[3]+pos).toString()),
				datos.get(this.indicesDetMedAdminHojaAnes[4]+pos).toString(),
				datos.get(this.indicesDetMedAdminHojaAnes[5]+pos).toString()).get("numRegistros").toString().equals("0"))
		{
			return true;
		}			
		
		return false;
	}
	
	//***********************************************************************************************************
	
	/**
	 * validaciones para almacenar la informaci�n de las administraciones de Infusiones 
	 * @param HashMap datos
	 * */
	public boolean validacionesDatosDetInfusiones(HashMap mapa, HashMap mapaJ)
	{
		int numRegistros = Integer.parseInt(mapa.get("numRegistros").toString());
		boolean respuesta = true;
		this.errores = new ActionErrors();		
		
		
		if(numRegistros <= 0)
		{
			this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Debe Existir por lo menos 1 Articulo."));
			return false;
		}
		
		for(int i = 0; i<numRegistros; i++)
		{
			//Validaciones sobre la dosis		
			if(mapa.get(this.indicesDetAdmInfusionesHojaAnes[4]+i).toString().equals(""))
			{
				this.errores.add("descripcion",new ActionMessage("errors.required","La Dosis del Registro "+(i+1)));
				respuesta = false;
			}
			else
			{
				if(Utilidades.convertirADouble(mapa.get(this.indicesDetAdmInfusionesHojaAnes[4]+i).toString(), true) <= 0)  
				{
					this.errores.add("descripcion",new ActionMessage("errors.integerMayorQue","La Dosis del Registro "+(i+1),"0"));
					respuesta = false;
				}
			}
			//******************Validacion Justificaciones No Pos**********//
			if(mapaJ != null)
			{
				logger.info("Siiii lleva el mapa Jus>>>>>>>>>>>");
				if(mapa.get(this.indicesDetAdmInfusionesHojaAnes[9]+i).equals("NOPOS") || mapa.get(this.indicesDetAdmInfusionesHojaAnes[9]+i).equals("f"))
				{
					logger.info("Entro al articulo no pos>>>>>>>>>>>");
					if(!mapaJ.containsKey(mapa.get("articulo2_"+i)+"_yajustifico") || mapaJ.get(mapa.get("articulo2_"+i)+"_yajustifico").equals("false"))
					{
						logger.info("Entro al no contiene llave>>>>>>>>>>>");
	    				errores.add("", new ActionMessage("errors.required", "La justificacion no pos del articulo "+mapa.get("articulo2_"+i)));
	    				respuesta = false;
	    			}
				}
			}
			//***************************************************************//
		}
		
		return respuesta;
	}	
	
	//***********************************************************************************************************
	
	/**
	 * validaciones para almacenar la informacion de las administraciones de Infusiones del Historial
	 * @param HashMap mapa
	 * @param String fechaIngresoSala
	 * @param String horaIngresoSala
	 * */
	public boolean validacionesDatosHistorialInfusiones(HashMap mapa, HashMap mapaJ, String fechaIngresoSala, String horaIngresoSala)
	{
		int numRegistros = Integer.parseInt(mapa.get("numRegistros").toString());
		int numDetalle = 0;
		boolean respuesta = true;
		this.errores = new ActionErrors();
		
		//		validacion de la fecha y hora de ingreso a la sala 
		if(fechaIngresoSala.equals("") || horaIngresoSala.equals(""))
		{
			this.errores.add("descripcion",new ActionMessage("errors.required","La Fecha/Hora de Ingreso de la Seccion Informacion General - Subseccion Fecha y Hora Ingreso Sala " ));
			respuesta = false;
			return respuesta;
		}
		
		
		for(int i = 0; i < numRegistros; i++)
		{
			if(this.validacionesDatosInfusionesHojaAnes(mapa, i, fechaIngresoSala, horaIngresoSala," de la Administraci�n Nro. "+(i+1)+" "))
			{
				numDetalle = Integer.parseInt(mapa.get("dosis_"+i+"_numRegistros").toString());
				
				for(int j = 0; j<numDetalle; j++)
				{
					//Validaciones sobre la dosis					
					if(mapa.get("dosis_"+i+"_"+this.indicesDetAdmInfusionesHojaAnes[4]+j).toString().equals(""))
					{
						this.errores.add("descripcion",new ActionMessage("errors.required","La Dosis del Articulo ["+mapa.get("dosis_"+i+"_"+this.indicesDetAdmInfusionesHojaAnes[3]+j)+"] de la Administraci�n Nro. "+(i+1)));
						return false;
					}
					else
					{
						if(Utilidades.convertirADouble(mapa.get("dosis_"+i+"_"+this.indicesDetAdmInfusionesHojaAnes[4]+j).toString(), true) <= 0)  
						{
							this.errores.add("descripcion",new ActionMessage("errors.integerMayorQue","La Dosis del Articulo ["+mapa.get("dosis_"+i+"_"+this.indicesDetAdmInfusionesHojaAnes[3]+j)+"] de la Administraci�n Nro. "+(i+1),"0"));
							return false;
						}
					}
				}				
			}			
			else
				return false;				
		}
		
		return true;
	}	
	
	//***********************************************************************************************************
	
	
	/**
	 * Validaciones para la insercion de datos de Infusiones
	 * @param HashMap mapa 
	 * @param int pos
	 * */
	public boolean validacionesDatosInfusionesHojaAnes(HashMap mapa, int pos, String fechaIngresoSala, String horaIngresoSala,String posFijo )
	{		
		boolean respuesta = true;
		this.errores = new ActionErrors();
		
		//		validacion de la fecha y hora de ingreso a la sala 
		if(fechaIngresoSala.equals("") || horaIngresoSala.equals(""))
		{
			this.errores.add("descripcion",new ActionMessage("errors.required","La Fecha/Hora de Ingreso de la Seccion Informacion General - Subseccion Fecha y Hora Ingreso Sala " ));
			respuesta = false;
			return respuesta;
		}
		
		
		//validacion de la fecha 
		if(mapa.get(this.indicesAdmInfusionesHojaAnes[2]+pos).toString().equals(""))
		{
			this.errores.add("descripcion",new ActionMessage("errors.required","La Fecha "+posFijo ));
			respuesta = false;
		}
		
		//validacion de la hora
		if(mapa.get(this.indicesAdmInfusionesHojaAnes[3]+pos).toString().equals(""))
		{
			this.errores.add("descripcion",new ActionMessage("errors.required","La Hora de Ingreso "+posFijo));
			respuesta = false;
		}
		
		if(!respuesta)
			return false;
		
		
		//Validaci�n del formato de la fecha
		if(!UtilidadFecha.validarFecha(mapa.get(this.indicesAdmInfusionesHojaAnes[2]+pos).toString()))
		{
			this.errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido",mapa.get(this.indicesAdmInfusionesHojaAnes[2]+pos).toString()+posFijo));
			respuesta = false;
		}
		else
		{
			//validaci�n de la hora
			if(!UtilidadFecha.validacionHora(mapa.get(this.indicesAdmInfusionesHojaAnes[3]+pos).toString()).puedoSeguir)
			{
				this.errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido",mapa.get(this.indicesAdmInfusionesHojaAnes[3]+pos).toString()+posFijo));
				respuesta = false;				
			}
			else
			{				
				if(!UtilidadFecha.compararFechas(fechaIngresoSala,horaIngresoSala,mapa.get(this.indicesAdmInfusionesHojaAnes[2]+pos).toString(),mapa.get(this.indicesAdmInfusionesHojaAnes[3]+pos).toString()).isTrue())
				{
					if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),mapa.get(this.indicesAdmInfusionesHojaAnes[2]+pos).toString(),mapa.get(this.indicesAdmInfusionesHojaAnes[3]+pos).toString()).isTrue())
					{
						this.errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual",mapa.get(this.indicesAdmInfusionesHojaAnes[2]+pos).toString()+ " - " +mapa.get(this.indicesAdmInfusionesHojaAnes[3]+pos).toString()," Actual "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+posFijo));
						respuesta = false;
					}
				}
				else
				{
					this.errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual",mapa.get(this.indicesAdmInfusionesHojaAnes[2]+pos).toString()+" - "+mapa.get(this.indicesAdmInfusionesHojaAnes[3]+pos).toString()+posFijo," del Ingreso a la Sala "+fechaIngresoSala+" -  "+horaIngresoSala));
					respuesta = false;
				}
			}
		}
		
		return respuesta;
	}	
	
	//***********************************************************************************************************
	/**
	 * Verifica si existe o no la nueva otra mezcla a ingresar
	 * @param HashMap datos
	 * @param String nomNuevaMezcla
	 * */
	public boolean validacionesExisteOtraMezcla(HashMap datos, String nomNuevaMezcla)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		
		for(int i = 0 ; i< numRegistros; i++)
		{
			if(datos.get(this.indicesInfusionesHojaAnes[4]+i).toString().toUpperCase().equals(nomNuevaMezcla.toUpperCase()))
			{
				this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Insertar Otra Infusion, ya existe un Registro con la descripci�n: "+nomNuevaMezcla.toUpperCase()));
				return true;
			}
		}
		
		return false;
	}
	
	//***********************************************************************************************************
	
	/**
	 * validaciones para almacenar la informaci�n del Balance de Liquidos 
	 * @param HashMap datos
	 * */
	public boolean validacionesDatosBalanceLiquidos(HashMap mapa)
	{
		int numRegistros = Integer.parseInt(mapa.get("numRegistros").toString());
		boolean respuesta = true;
		this.errores = new ActionErrors();		
		
		
		if(numRegistros <= 0)
		{
			this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Debe Existir por lo menos 1 Liquido."));
			return false;
		}
		
		for(int i = 0; i<numRegistros; i++)
		{
			//Validaciones sobre la dosis		
			if(mapa.get(this.indicesBalancesLiquidosHojaAnes[2]+i).toString().equals(""))
			{
				this.errores.add("descripcion",new ActionMessage("errors.required","La Cantidad del Liquido "+mapa.get(this.indicesBalancesLiquidosHojaAnes[3]+i)));
				respuesta = false;
			}
			else
			{
				if(Utilidades.convertirADouble(mapa.get(this.indicesBalancesLiquidosHojaAnes[2]+i).toString(), true) <= 0)  
				{
					this.errores.add("descripcion",new ActionMessage("errors.integerMayorQue","La Cantidad del Liquido "+mapa.get(this.indicesBalancesLiquidosHojaAnes[3]+i),"0"));
					respuesta = false;
				}
			}
		}
		
		return respuesta;
	}	
	
	//************************************************************************************************	
	
	/**
	 * Validaciones sobre los datos de la salida del paciente
	 * @param HashMap mapa
	 * @param String fechaIngresoSala
	 * @param String horaIngresoSala
	 * @param String fechaFinalizaCirugia
	 * @param String horaFinalizaCirugia
	 * */
	public boolean validacionesDatosSalidaPaciente(
			Connection con,
			int numeroSolicitud,
			HashMap mapa,
			String fechaIngresoSala,
			String horaIngresoSala,						
			String fechaFinalizaCirugia,
			String horaFinalizaCirugia,
			ArrayList<DtoArticuloIncluidoSolProc> arrayArtInc,
			HashMap justificacionMap)
	{
		boolean respuesta = true;
		this.errores = new ActionErrors();
		
		//validacion de la fecha y hora de ingreso a la sala 
		if(fechaIngresoSala.equals("") || horaIngresoSala.equals(""))
		{
			this.errores.add("descripcion",new ActionMessage("errors.required","La Fecha/Hora de Ingreso de la Seccion Informacion General - Subseccion Fecha y Hora Ingreso Sala " ));
			respuesta = false;
			return respuesta;
		}	
		
		//validacion de la fecha 
		if(mapa.get(this.indicesSalidasSalaPaciente[5]).toString().equals(""))
		{
			this.errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Salida Paciente Sala "));
			respuesta = false;
		}
		
		//validacion de la hora
		if(mapa.get(this.indicesSalidasSalaPaciente[6]).toString().equals(""))
		{
			this.errores.add("descripcion",new ActionMessage("errors.required","La Hora de Salida Paciente Sala "));
			respuesta = false;
		}
		
		if(!respuesta)
			return false;
		
		
		//Validaci�n del formato de la fecha
		if(!UtilidadFecha.validarFecha(mapa.get(this.indicesSalidasSalaPaciente[5]).toString()))
		{
			this.errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido",mapa.get(this.indicesSalidasSalaPaciente[5]).toString()));
			respuesta = false;
		}
		else
		{
			//validaci�n de la hora
			if(!UtilidadFecha.validacionHora(mapa.get(this.indicesSalidasSalaPaciente[6]).toString()).puedoSeguir || mapa.get(this.indicesSalidasSalaPaciente[6]).toString().length()<5)
			{
				this.errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido",mapa.get(this.indicesSalidasSalaPaciente[6]).toString()));
				respuesta = false;				
			}
			else
			{
				if(UtilidadFecha.compararFechas(mapa.get(this.indicesSalidasSalaPaciente[5]).toString(),mapa.get(this.indicesSalidasSalaPaciente[6]).toString(),fechaIngresoSala,horaIngresoSala).isTrue())
				{
					if(UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),mapa.get(this.indicesSalidasSalaPaciente[5]).toString(),mapa.get(this.indicesSalidasSalaPaciente[6]).toString()).isTrue())
					{
						if(!UtilidadFecha.compararFechas(mapa.get(this.indicesSalidasSalaPaciente[5]).toString(),mapa.get(this.indicesSalidasSalaPaciente[6]).toString(),fechaFinalizaCirugia,horaFinalizaCirugia).isTrue())
						{
							this.errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual"," de Salida Sala Paciente: "+mapa.get(this.indicesSalidasSalaPaciente[5]).toString()+ " - " +mapa.get(this.indicesSalidasSalaPaciente[6]).toString()," de Finalizaci�n de Cirugia "+fechaFinalizaCirugia+" - "+horaFinalizaCirugia));
							respuesta = false;		
						}												
					}
					else
					{
						this.errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual"," de Salida Sala Paciente: "+mapa.get(this.indicesSalidasSalaPaciente[5]).toString()+ " - " +mapa.get(this.indicesSalidasSalaPaciente[6]).toString(),UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()));
						respuesta = false;						
					}
				}
				else
				{
					this.errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual"," de Salida Sala Paciente: "+mapa.get(this.indicesSalidasSalaPaciente[5]).toString()+ " - " +mapa.get(this.indicesSalidasSalaPaciente[6]).toString()," de Ingreso Sala: "+fechaIngresoSala+" -  "+horaIngresoSala));
					respuesta = false;
				}
			}
		}
		
		//Validaci�n de los datos de fallece paciente
		if(respuesta && mapa.get(this.indicesSalidasSalaPaciente[7]).toString().equals(ConstantesBD.acronimoSi))
		{
			//Validaci�n del formato de la fecha
			if(!UtilidadFecha.validarFecha(mapa.get(this.indicesSalidasSalaPaciente[8]).toString()))
			{
				this.errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," de Fallecimiento "+mapa.get(this.indicesSalidasSalaPaciente[8]).toString()));
				respuesta = false;
			}
			else
			{
				//validaci�n de la hora
				if(!UtilidadFecha.validacionHora(mapa.get(this.indicesSalidasSalaPaciente[9]).toString()).puedoSeguir)
				{
					this.errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido"," de Fallecimiento "+mapa.get(this.indicesSalidasSalaPaciente[9]).toString()));
					respuesta = false;				
				}
				else
				{
					if(UtilidadFecha.compararFechas(mapa.get(this.indicesSalidasSalaPaciente[8]).toString(),mapa.get(this.indicesSalidasSalaPaciente[9]).toString(),fechaIngresoSala,horaIngresoSala).isTrue())
					{
						if(UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),mapa.get(this.indicesSalidasSalaPaciente[8]).toString(),mapa.get(this.indicesSalidasSalaPaciente[9]).toString()).isTrue())
						{
							if(!UtilidadFecha.compararFechas(mapa.get(this.indicesSalidasSalaPaciente[5]).toString(),mapa.get(this.indicesSalidasSalaPaciente[6]).toString(),mapa.get(this.indicesSalidasSalaPaciente[8]).toString(),mapa.get(this.indicesSalidasSalaPaciente[9]).toString()).isTrue())
							{
								this.errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual"," de Fallecimiento "+mapa.get(this.indicesSalidasSalaPaciente[8]).toString()+" - "+mapa.get(this.indicesSalidasSalaPaciente[9]).toString()," de Salida Sala Paciente: "+mapa.get(this.indicesSalidasSalaPaciente[5]).toString()+ " - " +mapa.get(this.indicesSalidasSalaPaciente[6]).toString()));
								respuesta = false;		
							}												
						}
						else
						{
							this.errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual"," de Fallecimiento: "+mapa.get(this.indicesSalidasSalaPaciente[8]).toString()+ " - " +mapa.get(this.indicesSalidasSalaPaciente[9]).toString(),UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()));
							respuesta = false;						
						}
					}
					else
					{
						this.errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual"," de Fallecimiento: "+mapa.get(this.indicesSalidasSalaPaciente[8]).toString()+ " - " +mapa.get(this.indicesSalidasSalaPaciente[9]).toString()," de Ingreso Sala: "+fechaIngresoSala+" -  "+horaIngresoSala));
						respuesta = false;
					}
				}
			}	
			
			//Validaci�n diagnosticos
			if(mapa.get(this.indicesSalidasSalaPaciente[10]).toString().equals(""))
			{
				this.errores.add("descripcion",new ActionMessage("errors.required","El Diagnostico de Muerte "));
				respuesta = false;	
			}
		}
		
		
		//Validacion de la Justificacion No Pos
		//Se verifica que la operacion no sea deseleccionar la salida del paciente			
		if(!mapa.get(HojaAnestesia.indicesSalidasSalaPaciente[2]).toString().equals(""))
		{
			this.errores = RespuestaProcedimientos.valicacionesArticulosIncluidosProc(this.errores,arrayArtInc,justificacionMap);
		}
		
		if(respuesta)
		{
			if(UtilidadFecha.validarFecha(mapa.get(this.indicesSalidasSalaPaciente[5]).toString()) 
					&& UtilidadFecha.validacionHora(mapa.get(this.indicesSalidasSalaPaciente[6]).toString()).puedoSeguir 
						&& UtilidadFecha.validarFecha(fechaIngresoSala) 
							&& UtilidadFecha.validacionHora(horaIngresoSala).puedoSeguir)
			{
				//******VALIDACION DE LA OCUPACI�N DE LA SALA*************************************
				HashMap datosSala=HojaQuirurgica.estaSalaOcupada(
						con, 
						numeroSolicitud,
						ConstantesBD.codigoNuncaValido,
						fechaIngresoSala,
						horaIngresoSala,
						mapa.get(this.indicesSalidasSalaPaciente[5]).toString(),								
						mapa.get(this.indicesSalidasSalaPaciente[6]).toString());
				
				if(UtilidadTexto.getBoolean(datosSala.get("estaSalaOcupada")+""))
				{
					int numReg=Utilidades.convertirAEntero(datosSala.get("numRegistros")+"");
					for (int i=0;i<numReg;i++)
						errores.add("descripcion",new ActionMessage("error.salasCirugia.salaOcupada", "entre el rango de fecha/hora Sala: "+UtilidadFecha.conversionFormatoFechaAAp(datosSala.get("fechaIngresoSala_"+i)+"") +" "+datosSala.get("horaIngresoSala_"+i)+" - "+UtilidadFecha.conversionFormatoFechaAAp(datosSala.get("fechaSalidaSala_"+i)+"") +" "+datosSala.get("horaSalidaSala_"+i)+"", ""));
					respuesta = false;
				}
				//********************************************************************************	
			}
		}
		
		return respuesta;	
	}
	
	//************************************************************************************************
	
	/**
	 * Validaci�n para la existencia o no del Consumo de materiales
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public boolean validacionEsPosibleGenerarConsumosMateriales(Connection con, String numeroSolicitud)
	{		
		//Verifica que exista el consumo y que no este finalizado
		if(!MaterialesQx.existeConsumoMateriales(con, numeroSolicitud, true))		
			return true;		
		else
		{
			this.cargarErrorElementoAp("errors.notEspecific","El Consumo de Materiales de la Solicitud se encuentra Finalizado.","");
			return false;
		}
	}
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Metodos para la Hoja de Anestesia
	/*************************************************************************************************/
	
	/**
	 * Insertar Hoja de Anestesia 
	 * @param Connection con
	 * @param String numeroSolicitud (requerido)
	 * @param String institucion (requerido)
	 * @param String fechaIniciaAnestesia
	 * @param String horaIniciaAnestesia (requerido)
	 * @param String fechaFinalizaAnestesia
	 * @param String horaFinalizaAnestesia
	 * @param String preanestesia
	 * @param String datosMedico
	 * @param String finalizada
	 * @param String fechaFinalizada
	 * @param String horaFinalizada
	 * @param String fechaGrabacion
	 * @param String horaGrabacion
	 * @param String observacionesSignosVitales
	 * @param String anestesiologoCobrable
	 * */ 
	public boolean insertarHojaAnestesia(Connection con,
			String numeroSolicitud,
			String institucion,
			String fechaIniciaAnestesia,
			String horaIniciaAnestesia,
			String fechaFinalizaAnestesia,
			String horaFinalizaAnestesia,
			String preanestesia,
			String datosMedico,
			String finalizada,
			String fechaFinalizada,
			String horaFinalizada,
			String fechaGrabacion,
			String horaGrabacion,
			String observacionesSignosVitales,
			String anestesiologoCobrable,
			boolean validacionCapitacion)
	{
		HashMap parametros = new HashMap();
		parametros.put(HojaAnestesia.indicesHojaAnestesia[0],numeroSolicitud);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[14],institucion);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[2],fechaIniciaAnestesia);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[3],horaIniciaAnestesia);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[4],fechaFinalizaAnestesia);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[5],horaFinalizaAnestesia);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[6],preanestesia);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[7],datosMedico);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[8],finalizada);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[9],fechaFinalizada);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[10],horaFinalizada);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[11],fechaGrabacion);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[12],horaGrabacion);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[13],observacionesSignosVitales);
		parametros.put(HojaAnestesia.indicesHojaAnestesia[1],anestesiologoCobrable);		
		
		//Se valida parametro si es true se le asigna descripcion para guardar en la tabla hoja_anestesia de la bd
		if(validacionCapitacion){
			String mensaje = "Se ha respondido la Orden sin autorización de capitación subcontratada";
			parametros.put(HojaAnestesia.indicesHojaAnestesia[17], mensaje);
		}
		
		return getHojaAnestesiaDao().insertarHojaAnestesia(con, parametros);		
	}
	
	
	//***********************************************************************************************************
	/**
	 * Crea la Hoja de Anestesia con informacion Basica 
	 * @param Connection con
	 * @param String existePaginaHojaAnestesia
	 * @param String numeroSolicitud
	 * @param int institucion
	 * @param String tipoSolicitud
	 * */
	public boolean crearHojaAnestesiaBasica(Connection con,
			String existePaginaHojaAnestesia,
			String numeroSolicitud,
			int institucion,
			String tipoSolicitud,
			String codigoPersonaCrea,
			boolean validacionCapitacion)
	{		
		if(existePaginaHojaAnestesia.equals(ConstantesBD.acronimoNo))
		{
			if(!this.insertarHojaAnestesia(
					con, 
					numeroSolicitud, 
					institucion+"", 
					"", //UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()), 
					"", //UtilidadFecha.getHoraActual(), 
					"", 
					"", 
					"", 
					"", 
					"", 
					"", 
					"", 
					"", 
					"", 
					"", 
					ConstantesBD.acronimoSi,
					validacionCapitacion
					))
			{
				this.errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Insertar la Hoja de Anestesia"));
				return false;
			}
			else
			{
				logger.info("--------------------------------");
				logger.info("Se Creo la Hoja de Anestesia >>>");
				logger.info("--------------------------------");			
				return true;
			}
		}
		else		
			return true;
	}
	//***********************************************************************************************************
	
	/**
	 * Actualiza la informaci�n de la Hoja de Anestesia en la salida del paciente
	 * @param Connection con
	 * @param String datosMedico (requerido cuando finalizada es SI)
	 * @param Strin finalizada (requerido)
	 * @param String fechaFinalizada (requerido cuando finalizada es SI)
	 * @param String horaFinalizada (requerido cuando finalizada es SI)
	 * @param String institucion (requerido cuando finalizada es SI)
	 * */
	public boolean actualizarHojaAnestesiaSalidaPaciemte(
			Connection con,
			String datosMedico,
			String finalizada,
			String fechaFinalizada,
			String horaFinalizada,
			String numeroSolicitud,
			String institucion)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesHojaAnestesia[7],datosMedico);
		parametros.put(this.indicesHojaAnestesia[8],UtilidadTexto.getBoolean(finalizada));
		parametros.put(this.indicesHojaAnestesia[9],fechaFinalizada);
		parametros.put(this.indicesHojaAnestesia[10],horaFinalizada);
		parametros.put(this.indicesHojaAnestesia[0],numeroSolicitud);
		parametros.put(this.indicesHojaAnestesia[14],institucion);
		
		return this.getHojaAnestesiaDao().actualizarHojaAnestesiaSalidaPaciemte(con, parametros);
	}
	
	//***********************************************************************************************************
	
	/**
	 * Retorna el valor para el indicador de Honorarios Cobrables segun Validaciones de los Parametros Generales
	 * @param int institucion
	 * @param String tipoSolicitud
	 * */
	public String getActivarHonorarioCobrableHojaAnes(int institucion,String tipoSolicitud)
	{		
		if(tipoSolicitud.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia) && 
				ValoresPorDefecto.getIndicativoCobrableHonorariosCirugia(institucion).equals(ConstantesBD.acronimoSi))
			return ConstantesBD.acronimoSi;
		
		if(tipoSolicitud.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento) && 
				ValoresPorDefecto.getIndicativoCobrableHonorariosNoCruento(institucion).equals(ConstantesBD.acronimoSi))
			return ConstantesBD.acronimoSi;			
			
		return ConstantesBD.acronimoNo;
	}
	
	//***********************************************************************************************************
	
	/**
	 * Consulta la informaci�n de las Peticiones
	 * @param Connection con
	 * @param int institucion
	 * @param int paciente
	 * @param int cuenta
	 * @param String codigoPeticion
	 * */
	public  HashMap consultarPeticiones(
			Connection con, 
			int institucion, 
			int paciente,
			int ingreso,
			String codigoPeticion)
	{
		HashMap criterios = new HashMap ();
		
		criterios.put("institucion", institucion);
		criterios.put("ingreso", ingreso);
		criterios.put("paciente", paciente);		
		criterios.put("codigoPeticion",codigoPeticion);
		
		return HojaQuirurgica.consultarPeticiones(con,criterios);		
	}
	
	
	/**
	 * Consulta la informaci�n de la peticion especifica
	 * @param Connection con
	 * @param String codigoPeticion
	 * */
	public  HashMap consultarPeticionEspecifica(
			Connection con,			
			String codigoPeticion,
			int codigoSolicitud)
	{			
		HashMap peticion = HojaQuirurgica.consultarDatosPeticion(con,codigoPeticion);
		HashMap resultado = new HashMap(); 
		resultado.put("numRegistros","0");
		
		if(!peticion.get("numRegistros").toString().equals("0"))
		{
			//Cuenta
			resultado.put(HojaQuirurgica.indicesPeticiones[0]+"0","");
			//Paciente
			resultado.put(HojaQuirurgica.indicesPeticiones[1]+"0","");
			//Codigo Peticion
			resultado.put(HojaQuirurgica.indicesPeticiones[2]+"0",peticion.get(HojaQuirurgica.indicesDatosPeticion[0]));
			//Fecha Cirugia
			resultado.put(HojaQuirurgica.indicesPeticiones[3]+"0",peticion.get(HojaQuirurgica.indicesDatosPeticion[1]));
			//Consecutivo Ordenes
			resultado.put(HojaQuirurgica.indicesPeticiones[4]+"0","");
			//Estado Medico 
			resultado.put(HojaQuirurgica.indicesPeticiones[5]+"0","");
			//Numero Solicitud
			resultado.put(HojaQuirurgica.indicesPeticiones[6]+"0",codigoSolicitud);
			//Solicitante
			resultado.put(HojaQuirurgica.indicesPeticiones[7]+"0",peticion.get(HojaQuirurgica.indicesDatosPeticion[4]));
			//Especialidad
			resultado.put(HojaQuirurgica.indicesPeticiones[8]+"0","");
			//Servicios
			resultado.put(HojaQuirurgica.indicesPeticiones[9]+"0",new HashMap());
			//esAsociado
			resultado.put(HojaQuirurgica.indicesPeticiones[10]+"0",codigoSolicitud>0?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			//Codigo Solicitante
			resultado.put(HojaQuirurgica.indicesPeticiones[11]+"0",peticion.get(HojaQuirurgica.indicesDatosPeticion[6]));		
			resultado.put("numRegistros","1"); 
		}			
		
		logger.info("valor de mapa >> "+peticion+" \n\n "+resultado);
		return resultado;
	}	
	
	/*************************************************************************************************/
	//*************************************************************************************************
	//Metodos para la Solicitud************************************************************************
	/*************************************************************************************************/
	
	/**
	 * Crear Solicitud
	 * @param Connection con
	 * @param HashMap peticionMap
	 * @param HashMap solicitudMap
	 * @param HashMap serviciosMap
	 * @param ActionErrors errores
	 * @param UsuarioBasico usuario
	 * @param PersonaBasica paciente
	 * @param String idCuenta 
	 * @return true si genero la solicitud, false si no y carga el atributo de errores del objeto
	 * */
	public boolean crearSolicitud(
			Connection con,
			HashMap peticionMap,
			HashMap solicitudMap,
			HashMap serviciosMap,			
			UsuarioBasico usuario,
			PersonaBasica paciente) throws IPSException
	{		
		
		this.errores = new ActionErrors();
		
		this.errores = HojaQuirurgica.generarSolicitudCirugia(
				con, 
				peticionMap, 
				solicitudMap,
				serviciosMap, 
				this.errores, 
				usuario, 
				paciente);
		
		if(this.errores.isEmpty())
			return true;
		else
			return false;	
	}
	
	
	/*************************************************************************************************/
	//*************************************************************************************************
	//Metodos Utilitarios******************************************************************************
	/*************************************************************************************************/
		
			
	/**
	 * Instancia el Dao
	 * */
	public static HojaAnestesiaDao getHojaAnestesiaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaAnestesiaDao();
	}
	
	//*************************************************************************************************
	
	/**
	 * Carga un array con elementos Ap
	 * @param String llave
	 * @param String atributo1
	 * @param String atributo2
	 * */
	public void cargarErrorElementoAp(String llave, String atributo1, String atributo2)
	{
		ElementoApResource elemento = new ElementoApResource(llave);
		
		if(!atributo1.equals(""))
			elemento.agregarAtributo(atributo1);
		
		if(!atributo2.equals(""))
			elemento.agregarAtributo(atributo2);
		
		this.utilitarioList.add(elemento);
	}
	
	
	//*************************************************************************************************
	
	/**
	 *  Ordena Un Mapa HashMap a partir del patron de ordenamiento
	 *  @param HashMap mapaOrdenar
	 *  @param String patronOrdenar
	 *  @param String ultimoPatron
	 *  @return Mapa Ordenado
	 **/
	public HashMap accionOrdenarMapa(HashMap mapaOrdenar,
									 String patronOrdenar,
									 String ultimoPatron)
	{					
		String[] indices = (String[])mapaOrdenar.get("INDICES_MAPA");
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");		
		mapaOrdenar = (Listado.ordenarMapa(indices,patronOrdenar,ultimoPatron,mapaOrdenar,numReg));	
		mapaOrdenar.put("numRegistros",numReg+"");
		mapaOrdenar.put("INDICES_MAPA",indices);		
		return mapaOrdenar;
	}
	
	//*************************************************************************************************
	
	//Captura la cantidad para los otros liquidos de la seccion Balance Liquidos
	public static int getTotalCampo(HashMap datos,String campo)
	{		
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		int sum  = 0;
		
		for(int j = 0; j < numRegistros; j++)
		{
			if(!datos.get(campo+j).toString().equals(""))
				sum+= Integer.parseInt(datos.get(campo+j).toString());	
		}		
		
		return sum;
	}	
	
	//*************************************************************************************************

	/**
	 * @return the errores
	 */
	public ActionErrors getErrores() {
		if(errores == null)
			errores = new ActionErrors();	
			
		return errores;
	}
	
	//*************************************************************************************************

	/**
	 * @param errores the errores to set
	 */
	public void setErrores(ActionErrors errores) {
		this.errores = errores;
	}
	
	//*************************************************************************************************	
	
	/**
	 * @param HashMap datos
	 * @param String campoCodigo
	 * */
	public static String getCodigosInsertados(HashMap datos,String campoCodigo)
	{
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		String codigoInsert = "";
		
		for(int i = 0 ; i<numRegistros ; i++)
		{
			if(!datos.get(campoCodigo+i).equals(""))
				codigoInsert += datos.get(campoCodigo+i)+",";
		}
		
		return codigoInsert;
	}	
	
	//*************************************************************************************************

	/**
	 * @return the utilitarioMap
	 */
	public HashMap getUtilitarioMap() {
		return utilitarioMap;
	}
	
	//*************************************************************************************************

	/**
	 * @param utilitarioMap the utilitarioMap to set
	 */
	public void setUtilitarioMap(HashMap utilitarioMap) {
		this.utilitarioMap = utilitarioMap;
	}
	
	//*************************************************************************************************

	/**
	 * @return the utilitarioList
	 */
	public ArrayList getUtilitarioList() {
		return utilitarioList;
	}
	
	//*************************************************************************************************

	/**
	 * @param utilitarioList the utilitarioList to set
	 */
	public void setUtilitarioList(ArrayList utilitarioList) {
		this.utilitarioList = utilitarioList;
	}
	
	//*************************************************************************************************
	
	/**
	 * Verifica si los servicios de la solicitud poseen indicativo requiere interpretacion en Si
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public String esRequerioInterServicioSoli(Connection con,String numeroSolicitud)
	{
		HashMap parametros = new HashMap();
		parametros.put("numeroSolicitud",numeroSolicitud);
		
		return this.getHojaAnestesiaDao().esRequerioInterServicioSoli(con, parametros);
	}
	
	//**************************************************************************************************
	
	/**
	 * Actualiza las cantidades del detalle de materiales qx
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean actualizarCantidadesDetMatQx(
			Connection con,
			int seccion,
			String numeroSolicitud)
	{
		HashMap parametros = new HashMap();
		parametros.put("seccion",seccion);
		parametros.put("numeroSolicitud",numeroSolicitud);
		
		return getHojaAnestesiaDao().actualizarCantidadesDetMatQx(con,parametros);		
	}
	
	//**************************************************************************************************
			
	//-----------------------------------------------------------------------------------------------
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/************************************************************************************************
	 * METODOS PARA EL DTO DE HOJA DE ANESTESIA******************************************************
	 * *********************************************************************************************/
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//-----------------------------------------------------------------------------------------------
	
	/**
	 * Carga la informaci�n de la Hoja de Anestesia dentro del DTO. se puede
	 * a�adir posteriormente validaciones para mostrar o no las subsecciones
	 * 
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param boolean cargarSeccionInfGeneral
	 * @param boolean cargarSeccionObservaciones
	 * @param boolean cargarSeccionBalanceLiquidos
	 * */ 
	public DtoHojaAnestesia cargarDtoHojaAnestesia(
			Connection con,
			String numeroSolicitud,
			boolean cargarSeccionInfGeneral,			
			boolean cargarSeccionAnestesicosMedica,
			boolean cargarSeccionLiquidos,
			boolean cargarSeccionHemoderivados,
			boolean cargarSeccionInfusiones,
			boolean cargarSeccionBalanceLiquidos,
			boolean cargarSeccionObservaciones,			
			int institucion)
	{
		this.dtoHojaAnes = new DtoHojaAnestesia();
		
		dtoHojaAnes.setIndicadorCargoDto(ConstantesBD.acronimoNo);
		
		//Valida que el numero de solicitud no se encuentre vacio
		if(!numeroSolicitud.equals(""))
		{
			/****************************************************************************************/
			//Se carga la informaci�n general de la hoja de anestesia
			dtoHojaAnes.setInfoHojaAnestesiaMap(this.consultarHojaAnestesia(con, numeroSolicitud, institucion, ""));			
			
			/****************************************************************************************/
			//Se carga la informaci�n de la seccion general
			if(cargarSeccionInfGeneral)
			{
				//Carga la informaci�n de la subsecci�n Especialidad que interviene y Cirujanos Principales
				dtoHojaAnes.setEspecialidadesMap(this.consultarEspecialidadesIntervienen(con, numeroSolicitud,""));
				dtoHojaAnes.setCirujanosMap(this.consultarCirujanosIntervienen(con,dtoHojaAnes.getEspecialidadesMap()));
				
				if(!dtoHojaAnes.getEspecialidadesMap("numRegistros").toString().equals("0"))
				{
					dtoHojaAnes.setIndicadorInfoEspInterCirPrin(ConstantesBD.acronimoSi);
					dtoHojaAnes.setIndicadorCargoDto(ConstantesBD.acronimoSi);
				}
				else
					dtoHojaAnes.setIndicadorInfoEspInterCirPrin(ConstantesBD.acronimoNo);
				
				//Carga la informaci�n de la subseccion Anestesiologos
				dtoHojaAnes.setAnestesiologosMap(this.consultarAnestesiologos(con,numeroSolicitud,"","",null,false));				
				dtoHojaAnes.setAnestesiologosMap(this.indicesHojaAnestesia[1],dtoHojaAnes.getInfoHojaAnestesiaMap(this.indicesHojaAnestesia[1]+0));
				
				if(!dtoHojaAnes.getAnestesiologosMap("numRegistros").toString().equals("0"))
				{
					dtoHojaAnes.setIndicadorInfoAnestesiologos(ConstantesBD.acronimoSi);
					dtoHojaAnes.setIndicadorCargoDto(ConstantesBD.acronimoSi);
				}
				else
					dtoHojaAnes.setIndicadorInfoAnestesiologos(ConstantesBD.acronimoNo);	
				
				//Carga la informaci�n de la subseccion Fecha y Hora Ingreso Sala
				dtoHojaAnes.setSolicitudCxMap(this.consultarInfoSolicitudCx(con,numeroSolicitud));								
			}
			
			/*****************************************************************************************/
			//Se Carga la informaci�n de la secci�n de Anestesicos y Medicamentos Administrados
			dtoHojaAnes.setIndicadorInfoAnestesicosMedAdminis(ConstantesBD.acronimoNo);
			if(cargarSeccionAnestesicosMedica)
			{			
				dtoHojaAnes.setAnestesicosMedAdmMap1(this.consultarAdminisHojaAnest(
						con,
						numeroSolicitud,
						ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis+"",
						ConstantesBD.acronimoNo,
						""));
								
				for(int i=0; i<Integer.parseInt(dtoHojaAnes.getAnestesicosMedAdmMap1("numRegistros").toString()); i++)			
					dtoHojaAnes.setAnestesicosMedAdmMap1
					(
						"detalle_"+i,
						consultarDetAdminisHojaAnest(con,dtoHojaAnes.getAnestesicosMedAdmMap1(HojaAnestesia.indicesMedAdminHojaAnes[0]+i)+"")
					);
				
				if(!dtoHojaAnes.getAnestesicosMedAdmMap1("numRegistros").toString().equals("0"))
					dtoHojaAnes.setIndicadorInfoAnestesicosMedAdminis(ConstantesBD.acronimoSi);
				else
					dtoHojaAnes.setIndicadorInfoAnestesicosMedAdminis(ConstantesBD.acronimoNo);				
			}
			/****************************************************************************************/
						
			/*****************************************************************************************/
			//Se Carga la informaci�n de la Secci�n de Liquidos
			dtoHojaAnes.setIndicadorInfoliquidos(ConstantesBD.acronimoNo);
			if(cargarSeccionLiquidos)
			{			
				dtoHojaAnes.setLiquidosMap1(this.consultarAdminisHojaAnest(
						con,
						numeroSolicitud,
						ConstantesSeccionesParametrizables.seccionLiquidos+"",
						ConstantesBD.acronimoNo,
						""));
								
				for(int i=0; i<Integer.parseInt(dtoHojaAnes.getLiquidosMap1("numRegistros").toString()); i++)			
					dtoHojaAnes.setLiquidosMap1
					(
						"detalle_"+i,
						consultarDetAdminisHojaAnest(con,dtoHojaAnes.getLiquidosMap1(HojaAnestesia.indicesMedAdminHojaAnes[0]+i)+"")
					);
				
				if(!dtoHojaAnes.getLiquidosMap1("numRegistros").toString().equals("0"))
					dtoHojaAnes.setIndicadorInfoliquidos(ConstantesBD.acronimoSi);
				else
					dtoHojaAnes.setIndicadorInfoliquidos(ConstantesBD.acronimoNo);				
			}
			/****************************************************************************************/
			
			/*****************************************************************************************/
			//Se Carga la informaci�n de la Secci�n de Hemoderivados
			dtoHojaAnes.setIndicadorInfoHemoderivados(ConstantesBD.acronimoNo);
			if(cargarSeccionHemoderivados)
			{			
				dtoHojaAnes.setHemoderivadosMap1(this.consultarAdminisHojaAnest(
						con,
						numeroSolicitud,
						ConstantesSeccionesParametrizables.seccionHemoderivados+"",
						ConstantesBD.acronimoNo,
						""));
								
				for(int i=0; i<Integer.parseInt(dtoHojaAnes.getHemoderivadosMap1("numRegistros").toString()); i++)			
					dtoHojaAnes.setHemoderivadosMap1
					(
						"detalle_"+i,
						consultarDetAdminisHojaAnest(con,dtoHojaAnes.getHemoderivadosMap1(HojaAnestesia.indicesMedAdminHojaAnes[0]+i)+"")
					);
				
				if(!dtoHojaAnes.getHemoderivadosMap1("numRegistros").toString().equals("0"))
					dtoHojaAnes.setIndicadorInfoHemoderivados(ConstantesBD.acronimoSi);
				else
					dtoHojaAnes.setIndicadorInfoHemoderivados(ConstantesBD.acronimoNo);				
			}
			/****************************************************************************************/
			
			/****************************************************************************************/
			//Se Carga la informaci�n de la seccion Infusiones
			dtoHojaAnes.setIndicadorInfoInfusiones(ConstantesBD.acronimoNo);
			if(cargarSeccionInfusiones)
			{
				dtoHojaAnes.setInfusionesMap1(consultarInfusionesHojaAnestesia(
						con,
						numeroSolicitud));
				
				for(int i=0; i<Integer.parseInt(dtoHojaAnes.getInfusionesMap1("numRegistros").toString()); i++)			
					dtoHojaAnes.setInfusionesMap1
					(
						"detalle_"+i,
						consultarHistorialInfusiones(con,dtoHojaAnes.getInfusionesMap1(HojaAnestesia.indicesInfusionesHojaAnes[0]+i).toString())
					);
				
				if(!dtoHojaAnes.getInfusionesMap1("numRegistros").toString().equals("0"))
					dtoHojaAnes.setIndicadorInfoInfusiones(ConstantesBD.acronimoSi);
				else
					dtoHojaAnes.setIndicadorInfoInfusiones(ConstantesBD.acronimoNo);			
			}
			/****************************************************************************************/
								
			/****************************************************************************************/
			//Se Carga la informaci�n de la seccion de Balance de Liquidos
			dtoHojaAnes.setIndicadorInfoBalanceLiquidos(ConstantesBD.acronimoNo);
			if(cargarSeccionBalanceLiquidos)
			{
				//Captura la informaci�n de la seccion Balance Liquidos
				dtoHojaAnes.setBalanceLiquidosMap1((HashMap)this.consultarBalanceLiquidos(con,numeroSolicitud,true));
				
				//Captura la informaci�n de las secciones Homederivados y liquidos
				//este mapa se toma del mundo y se llena en el metodo consultarBalanceLiquidos
				dtoHojaAnes.setBalanceLiquidosMap2(this.getUtilitarioMap());
								
				if(!dtoHojaAnes.getBalanceLiquidosMap1("numRegistros").toString().equals("0") || 
						!dtoHojaAnes.getBalanceLiquidosMap2("numRegistros").toString().equals("0"))
				{
					dtoHojaAnes.setIndicadorInfoBalanceLiquidos(ConstantesBD.acronimoSi);
					dtoHojaAnes.setIndicadorCargoDto(ConstantesBD.acronimoSi);
				}				
			}
			
			/****************************************************************************************/
					
			/****************************************************************************************/			
			//Se carga la informaci�n de la seccion observaciones
			dtoHojaAnes.setIndicadorInfoObservaciones(ConstantesBD.acronimoNo);
			if(cargarSeccionObservaciones)
			{ 
				dtoHojaAnes.setObservacionesMap(this.consultarObservacionesGenerales(con,numeroSolicitud));
				
				if(!dtoHojaAnes.getObservacionesMap("numRegistros").toString().equals("0"))
				{
					dtoHojaAnes.setIndicadorInfoObservaciones(ConstantesBD.acronimoSi);
					dtoHojaAnes.setIndicadorCargoDto(ConstantesBD.acronimoSi);
				}					
			}						
			
			/*****************************************************************************************/
		}	
		
		return this.dtoHojaAnes;
	}

	/**
	 * @return the utilitarioList1
	 */
	public ArrayList getUtilitarioList1() {
		return utilitarioList1;
	}

	/**
	 * @param utilitarioList1 the utilitarioList1 to set
	 */
	public void setUtilitarioList1(ArrayList utilitarioList1) {
		this.utilitarioList1 = utilitarioList1;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param graficar
	 * @return
	 */
	public static HashMap<Object, Object> cargarGraficaHojaAnestesia(Connection con, int numeroSolicitud, String graficar, boolean liquidos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaAnestesiaDao().cargarGraficaHojaAnestesia(con, numeroSolicitud, graficar, liquidos);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param graficar
	 * @return
	 */
	public static HashMap<Object, Object> cargarGraficaInfusionesHA(Connection con, int numeroSolicitud, String graficar)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaAnestesiaDao().cargarGraficaInfusionesHA(con, numeroSolicitud, graficar);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param usuario
	 * @return
	 */
	public boolean insertarActualizarAnestesiologos(Connection con, int numeroSolicitud, UsuarioBasico usuario )
	{
		if(consultarExisteAnestesiologo(con,numeroSolicitud+"",usuario.getCodigoPersona()+""))
		{	
			if(!actualizarAnestesiologos(con, numeroSolicitud+"", usuario.getCodigoPersona()+""))
				return false;
		}
		else
		{			
			if(!insertarAnestesiologos(	con, numeroSolicitud+"", usuario.getCodigoPersona()+"",	ConstantesBD.acronimoNo))
				return false;
		}
		return true;
	}

	/**
	 * @return the indicadorUtilitario
	 */
	public String getIndicadorUtilitario() {
		return indicadorUtilitario;
	}

	/**
	 * @param indicadorUtilitario the indicadorUtilitario to set
	 */
	public void setIndicadorUtilitario(String indicadorUtilitario) {
		this.indicadorUtilitario = indicadorUtilitario;
	}
	
	/**
	 * Consultar el Indicativo de Registro
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public String consultarIndicativoRegistroDesde(
			Connection con,
			int numeroSolicitud)
	{
		return this.getHojaAnestesiaDao().consultarIndicativoRegistroDesde(con,numeroSolicitud);
	}
	
	/**
	 * Actualizar Indicativo registro desde
	 * 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public boolean actualizarIndicativoRegistroDesde(Connection con, int numeroSolicitud, String registradaDesde){
		
		HashMap parametros = new HashMap();
		parametros.put(this.indicesHojaAnestesia[0],numeroSolicitud);
		parametros.put(this.indicesHojaAnestesia[17],registradaDesde);
		
		return getHojaAnestesiaDao().actualizarIndicativoRegistroDesde(con, parametros);
	}
	
	/**
	 * Consultar Cirujanos Intervienen solicitud
	 * @param Connection con
	 * @param String solicitud
	 * @param String especialidad
	 * */
	public HashMap consultarCirujanosIntervienenSolicitud(Connection con,String solicitud)
	{
		HashMap parametros = new HashMap();
		parametros.put(this.indicesCirujanosPrincipales[1],solicitud);
		return getHojaAnestesiaDao().consultarCirujanosSolicitud(con, solicitud);
	}

	/**
	 * Metodo que consulta los datos de la hoja de anestesia relacionada a un numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return HojaAnestesiaDto
	 * @author Oscar Pulido
	 * @created 19/07/2013
	 */
	public HojaAnestesiaDto consultarHojaAnestesia(Connection con, int numeroSolicitud) throws IPSException {
		return getHojaAnestesiaDao().consultarHojaAnestesia(con,numeroSolicitud);
	}
	
}
