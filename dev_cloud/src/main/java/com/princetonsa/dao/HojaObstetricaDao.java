/*
 * Creado en Jun 14, 2005
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public interface HojaObstetricaDao
{
	/**
	 * Método para insertar una hoja obstetrica
	 * @param con
	 * @param codigoPaciente -> int
	 * @param embarazo -> int
	 * @param login -> String
	 * @param institucion -> int
	 * @param datosMedico -> String
	 * @param fechaOrden -> String
	 * @param fur -> String
	 * @param fpp -> String
	 * @param fechaUltrasonido -> String
	 * @param fppUltrasonido -> String
	 * @param edadGestacional -> int
	 * @param edadParto -> int
	 * @param finalizacionEmbarazo -> boolean
	 * @param observacionesGrales -> String
	 * @param confiable
	 * @param vigenteAntitetanica
	 * @param dosisAntitetanica
	 * @param mesesGestacionAntitetanica
	 * @param antirubeola
	 * @param fechaTerminacion 
	 * @param motivoFinalizacion 
	 * @param egFinalizar 
	 * @param peso
	 * @param talla
	 * @param embarazoDeseado
	 * @param embarazoPlaneado
	 *
	 * @return codigoHoja
	 */
	
	public int insertarHojaObstetrica( Connection con, int codigoPaciente, int embarazo, String login, int institucion, String datosMedico, 
									   String fechaOrden, String fur, String fpp, String fechaUltrasonido, String fppUltrasonido, int edadGestacional,
									   int edadParto, boolean finalizacionEmbarazo, String observacionesGrales,
									   String confiable,String vigenteAntitetanica, String dosisAntitetanica, String mesesGestacionAntitetanica, 
									   String antirubeola, String fechaTerminacion, String motivoFinalizacion, int egFinalizar,
									   int peso, int talla, String embarazoDeseado, String embarazoPlaneado);
	
	/**
	 * Método para insertar el resumen gestacional a la hoja obstetrica
	 * @param con
	 * @param codigoHojaObstetrica -> int
	 * @param edadGestacionalResumen -> int
	 * @param fechaGestacional -> String
	 * @param horaGestacional  -> String
	 * @param datosMedico -> String
	 *
	 * @return codigoResumenGest
	 */
		
	public int insertarResumenGestacional ( Connection con, int codigoHojaObstetrica, 	int edadGestacionalResumen, String fechaGestacional, String horaGestacional, String datosMedico);
	
	/** Método para insertar detalle del resumen gestacional
	 * @param con
	 * @param codigoResumenGestacional => int
	 * @param codigoTipoResumenGest => int
	 * @param valorTipoResumenGest => String
	 * @return
	 */
	public int insertarDetalleResumenGestacional(Connection con, int codigoResumenGestacional, int codigoTipoResumenGest, String valorTipoResumenGest);
	
	
	/** Método para insertar el histórico del exámen de laboratorio
	 * @param con
	 * @param codigoHistoricoExamenLab => int
	 * @param codigoHojaObstetrica => int
	 * @param datosMedico => String
	 * @param edadGestacionalExamen -> int
	 * @param fechaExamen -> String
	 * @param horaExamen -> String
	 * @return codigoHistoricoExamenLab
	 * 
	 */
	public int insertarHistoricoExamenLab(Connection con, int codigoHojaObstetrica, String datosMedico, int edadGestacionalExamen, String fechaExamen, String horaExamen);
	
	/**
	 * Método para insertar el detalle del exámen de laboratorio
	 * @param con
	 * @param codigoHistoricoExamenLab => int
	 * @param tipoExamen => int
	 * @param resultadoExamenLab = String
	 * @param observacionExamenLab => String
	 * @return
	 */
	public int insertarDetalleExamenLab(Connection con, int codigoHistoricoExamenLab, int tipoExamen, String resultadoExamenLab, String observacionExamenLab);
	
	/**
	 * Método para insertar el documento adjunto de un exámen de laboratorio
	 * @param con
	 * @param codigoDetalleExamenLab => int
	 * @param nombreOriginal => String
	 * @param nombreArchivo => String
	 * @return 
	 */
	public int insertarAdjuntoExamenLab(Connection con, int codigoDetalleExamenLab, String nombreOriginal, String nombreArchivo);
	
	 /** Método para insertar el histórico del ultrasonido
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoHojaObstetrica  => int
	 * @param ultrasonidoFecha => String
	 * @param ultrasonidoHora => String
	  * @param datosMedico  => String
	 * @return codigoHistoricoUltrasonido
	 */
	public int insertarHistoricoUltrasonido(Connection con, int codigoHojaObstetrica, String ultrasonidoFecha, String ultrasonidoHora, String datosMedico);
	
	/**
	 * Método para insertar el detalle del ultrasonidol
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoHistoUltrasonido => int
	 * @param codigoTipoUltrasonido  => int
	 * @param valorTipoUltrasonido  => String
	 * @return 1 si logró insertar sino retorna -1
	 */
	public int insertarDetalleUltrasonido(Connection con, int codigoHistoUltrasonido, int codigoTipoUltrasonido, String valorTipoUltrasonido);
	
	/**
	 * Método para insertar el documento adjunto a un ultrasonido
	 * @param con
	 * @param codigo_histo_ultrasonido => int
	 * @param nombreOriginal => String
	 * @param nombreArchivo => String
	 * @return 
	 */
	public int insertarAdjuntoUltrasonido(Connection con, int codigo_histo_ultrasonido, String nombreOriginal, String nombreArchivo);
	
	/**
	 * Funcion que retorna el listado de mujeres embarazadas
	 * @param con
	 * @return
	 */
	public Collection cargarListado(Connection con);

	/**
	 * Funcion que retorna una coleccion resultado de una buqueda avanzada sobre embarazadas
	 * @param con objeto de conexion
	 * @param apellido
	 * @param nombre
	 * @param id
	 * @param edad
	 * @param fpp
	 * @param edadGestacional
	 * @param nombreMedico
	 * @return
	 */
	public Collection busquedaAbanzada(Connection con,String apellido,String nombre,  String id, int edad, String fpp,int edadGestacional,String nombreMedico);

	/**
	 * Metodo para ingresar un embarazo en los antecedentes solamente con los
	 * datos de la valoracion gineco-obstetrica
	 * @param con
	 * @param fur
	 * @param fpp
	 * @param edadGestacional
	 * @param codigoPaciente
	 * @param usuario
	 * @param numeroEmbarazo
	 * @param duracion
	 * @param tiempoRupturaMemebranas
	 * @param legrado
	 * @param insertarHoja
	 * @return codigo del embarazo insertado, 0 si no se insertó el embarazo
	 */
	public int ingresarEmbarazo(Connection con, String fur, String fpp, String edadGestacional, int codigoPaciente, UsuarioBasico usuario, int numeroEmbarazo, boolean insertarEmbarazo, String duracion, String tiempoRupturaMemebranas, String legrado);

	/**
	 * Método para ingresar un antecenete gineco-obstetrico con los datos necesarios
	 * desde la hoja obstetrica
	 * @param con
	 * @param codigoPaciente
	 * @param usuario
	 */
	public int ingresarAntecedente(Connection con, int codigoPaciente, UsuarioBasico usuario);

	/**
	 * Método para obtener los campos del la hoja obstétrica
	 * que generan log
	 * @param con
	 * @param codigoHojaObstetrica
	 * @return ResultSet
	 */
	public ResultSetDecorator cargarDatosLog(Connection con, int codigoHojaObstetrica);

	/**
	 * Funcion para consultar la hoja obstetrica de un embarazo especefico de un paciente 
	 * @param con
	 * @param numeroEmbarazo
	 * @param codigoPaciente
	 * @return
	 */
	public Collection consultarEmbarazo(Connection con, int numeroEmbarazo, int codigoPaciente);

	/**
	 * Modifica el detalle embarazo gineco-obstétrico, para ingresar los valores correpondientes
	 * a fecha terminación, motivo finalización y eg finalizar  
	 * @param con -> Connection
	 * @param numeroEmbarazo -> int
	 * @param paciente -> int
	 * @param fechaTerminacion -> String
	 * @param motivoFinalizacion -> int
	 * @param otroMotivoFinalizacion -> String
	 * @param egFinalizar -> int
	 * @param legrado
	 * @param tiempoRuptura
	 * @param duracion
	 * @return
	 */
	public int modificarAntecedenteGinecoEmbarazo(Connection con, int numeroEmbarazo, int paciente, String fechaTerminacion, int motivoFinalizacion, String otroMotivoFinalizacion,int egFinalizar, String duracion, String tiempoRuptura, String legrado);

	/**
	 * Método para insertar el historico de antecedentes gineco-obstétrico con la fur
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente  => int
	 * @param fur -> String
	  * @return 
	 **/
	public int insertarAntecedenteGinecoHisto(Connection con, int codigoPaciente, String fur);
	
	
	/**
	 * Funcion para Modificar las Observaciones Generales de Algun Historico Embarazo  
	 * @param con
	 * @param numeroEmbarazo
	 * @param codigoPaciente
	 * @param observaciones_grales
	 * @return
	 */
	
	public int actualizarEmbarazo(Connection con, int numeroEmbarazo, int codigoPaciente, String observaciones_grales);
	
	/**
	 * Método para consultar el historico de los examenes de laboratorio
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @return Collection con los examenes de laboratorio realizados al embarazo pasado por parámetro
	 */
	public Collection consultarHistoricoExamenesLab(Connection con, int codigoPaciente, int codigoEmbarazo);

	/**
	 * Método para consultar los dos últimos historicos de los examenes de laboratorio
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @return Collection con los dos últimos históricos de los examenes de laboratorio realizados al embarazo pasado por parámetro
	 */
	public Collection consultarUltimosHistoricosExamenesLab(Connection con, int codigoPaciente, int codigoEmbarazo);
	
	/**
	 * Método para consultar el resumen gestacional de una hoja obstétrica
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @param orden -> true si es ascendente o false si es descendente
	 * @return Collection con el valor, fecha, hora, edadgestacional del resumen gestacional
	 */
	public Collection consultarResumenGestacional(Connection con, int codigoPaciente, int codigoEmbarazo, boolean orden);
	
	/**
	 * Método para consultar los tipos de resumen gestacional parametrizados a la institucion
	 * @param con
	 * @param institucion
	 * @return Collection con codigo y nombre del resumen gestacional
	 */
	public Collection consultarTiposResumenGestacional (Connection con, int institucion);
	
	/**
	 * Método para consultar los exámenes de laboratorio de una hoja obstétrica
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @param orden -> boolean true si ordenamiento ascendente o false si es descendente
	 * @param consulta -> Identica la consulta a ejecutar 1=Consulta con todos los históricos
	 * 										2= Histórico de los exámenes parametrizados,  3=Histórico nuevos exámenes de laboratorio
	
	 * @return Collection con el resultado, observación, fecha, hora, edadgestacional de los exámenes de laboratorio
	 */
	public Collection consultarExamenesLaboratorio(Connection con, int codigoPaciente, int codigoEmbarazo, boolean orden, int consulta);
	
	/**
	 * Método para consultar los tipos de exámenes de laboratorio
	 * @param con
	 * @param institucion
	 * @return Collection con codigo y nombre del exámen de laboratorio
	 */
	public Collection consultarTiposExamenLaboratorio (Connection con, int institucion);
	
	/**
	 * Método para consultar los tipos de ultrasonido parametrizados por institución
	 * @param con
	 * @param institucion
	 * @return Collection con codigo y nombre del tipo de ultrasonido
	 */
	public Collection consultarTiposUltrasonido (Connection con, int institucion);
	
	/**
	 * Método para consultar el histórico de los ultrasonidos
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @param orden -> boolean true si ordenamiento ascendente o false si es descendente
	 * @return Collection con el tipo_ultrasonido, valor, fecha, hora, codigo_histo_ultrasonido 
	 */
	public Collection consultarHistoricoUltrasonidos(Connection con, int codigoPaciente, int codigoEmbarazo, boolean orden);

	/**
	 * Método para actualizar la hoja Obstetrica desdfe la valoración
	 * @param con
	 * @param fur
	 * @param fpp
	 * @param edadGestacional
	 * @param usuario
	 * @param codigoPersona
	 */
	public void actualizarDatosValoracion(Connection con, String fur, String fpp, String edadGestacional, int codigoPersona);
	
	/**
	 * Método para insertar el detalle otro exámen de laboratorio
	 * @param con
	 * @param codigoHistoricoExamenLab => int
	 * @param descripcionOtro => String
	 * @param resultadoExamenLab => String
	 * @param observacionExamenLab => String
	 * @return 
	 */
	
	public int insertarDetalleOtroExamenLab(Connection con, int codigoHistoricoExamenLab, String descripcionOtro, String resultadoExamenLab, String observacionExamenLab);
	
	/**
	 * Método para insertar el documento adjunto de otro exámen de laboratorio
	 * @param con
	 * @param codigoDetalleOtroExamenLab => int
	 * @param nombreOriginal => String
	 * @param nombreArchivo => String
	 * @return 
	 */
	
	public int insertarAdjuntoOtroExamenLab(Connection con, int codigoDetalleOtroExamenLab, String nombreOriginal, String nombreArchivo);
	
	/**
	 * Método que consulta los tipos de plan manejo parametrizados para la institución, y también los
	 * otros planes de menejo de tipo medicamento que hayan sido ingresados por la opción ingresar otro
	 * @param con
	 * @param institucion
	 * @param codigoPersona
	 * @param numeroEmbarazo
	 * @return
	 */
	public HashMap consultarTiposPlanManejoInstitucion(Connection con, int institucion, int codigoPaciente, int numeroEmbarazo);

	/**
	 * Metodo que inserta el historico de plan de manejo
	 * @param con
	 * @param codigoHojaObstetrica
	 * @param datosMedico
	 * @return
	 */
	public int insertarHistoricoPlanManejo(Connection con, int codigoHojaObstetrica, String datosMedico);
	
	/**
	 * Método que inserta el detalle del tipo de plan de manejo, ya sea parametrizado o otro, dependiendo
	 * del valor del parametro esOto
	 * @param con
	 * @param codHistoPlanManejo
	 * @param codTipoPlanManejo
	 * @param presenta
	 * @param observacion
	 * @param esOtro -> Indica si se va a insertar un plan de manejo parametrizado o otro
	 * @return
	 */
	public int insertarDetallePlanManejo(Connection con, int codHistoPlanManejo, int codTipoPlanManejo, String presenta, String observacion, boolean esOtro);

	/**
	 * Se inserta el nombre del nuevo tipo de plan de manejo 
	 * @param con
	 * @param descripcionOtro
	 * @return
	 */
	public int insertarOtroPlanManejo(Connection con, String descripcionOtro);

	/**
	 * Metodo que consulta el historico de plan manejo tanto de los tipos parametrizados
	 * por institucion como los otros nuevos ingresados
	 * @param con
	 * @param codigoPaciente
	 * @parma nroEmbarazo
	 * @return
	 */
	public HashMap consultaHistoricoPlanManejo(Connection con, int codigoPaciente, int nroEmbarazo);
			
}
